package org.techuni.TechUniInviteSystem.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.techuni.TechUniInviteSystem.controller.response.ErrorResponse;
import org.techuni.TechUniInviteSystem.error.AbstractHttpException;

/**
 * Web アプリケーション全体のエラーコントローラー。 エラー情報を HTML や JSON で出力する。 ErrorController インターフェースの実装クラス。
 */
@Slf4j
@Controller
@Getter
@RequestMapping("${server.error.path:${error.path:/error}}") // エラーページへのマッピング
public class MyErrorController implements ErrorController {

    private static final List<HttpStatus> allowOutputStatus = List.of( //
            HttpStatus.NOT_FOUND, //
            HttpStatus.UNAUTHORIZED, //
            HttpStatus.BAD_REQUEST //
    );

    /**
     * エラーページのパス。
     */
    @Value("${server.error.path:${error.path:/error}}")
    private String errorPath;

    /**
     * HTML レスポンス用の ModelAndView オブジェクトを返す。
     *
     * @param request リクエスト情報
     * @return HTML レスポンス用の ModelAndView オブジェクト
     */
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView myErrorHtml(HttpServletRequest request) {
        final var errorInfo = new ErrorInfo(request);

        // HTTP ステータスを決める
        final var status = getStatus(errorInfo);

        final var viewableMessage = errorInfo.exception.map(AbstractHttpException::getUserOutputMessage) //
                .orElse(null);

        // 出力したい情報をセットする
        return new ErrorResponse(status, viewableMessage).createView();
    }

    /**
     * JSON レスポンス用の ResponseEntity オブジェクトを返す。
     *
     * @param request リクエスト情報
     * @return JSON レスポンス用の ResponseEntity オブジェクト
     */
    @RequestMapping
    public ResponseEntity<ErrorResponse> myErrorJson(HttpServletRequest request) {
        final var errorInfo = new ErrorInfo(request);

        // HTTP ステータスを決める
        final var status = getStatus(errorInfo);

        final var viewableMessage = errorInfo.exception.map(AbstractHttpException::getUserOutputMessage) //
                .orElse(null);

        // 出力する情報はHttpStatusから全て設定する。(内部のエラーメッセージ等は出力しない。)
        return new ResponseEntity<>(new ErrorResponse(status, viewableMessage), status);
    }

    private record ErrorInfo(Optional<AbstractHttpException> exception, String exceptionType, String message, String request_uri, String servlet,
            String status_code) {

        public ErrorInfo(HttpServletRequest req) {
            this(generateException(req.getAttribute(RequestDispatcher.ERROR_EXCEPTION)),
                    String.valueOf(req.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE)),
                    String.valueOf(req.getAttribute(RequestDispatcher.ERROR_MESSAGE)),
                    String.valueOf(req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI)),
                    String.valueOf(req.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME)),
                    String.valueOf(req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)));
        }

        private static Optional<AbstractHttpException> generateException(Object exception) {
            return Optional.ofNullable(exception) //
                    .filter(AbstractHttpException.class::isInstance) //
                    .map(AbstractHttpException.class::cast);
        }

        public int getIntStatusCode() throws NumberFormatException {
            return Integer.parseInt(status_code);
        }

        public String toString() {
            return String.format("ErrorInfo[exception=%s, exceptionType=%s, message=%s, request_uri=%s, servlet=%s, status_code=%s]", //
                    exception, exceptionType, message, request_uri, servlet, status_code);
        }
    }

    private static HttpStatus getStatus(ErrorInfo errorInfo) {
        // unexceptedなエラーはすべてNOT_FOUNDに(セキュリティ上)
        HttpStatus status;

        try {
            status = HttpStatus.valueOf(errorInfo.getIntStatusCode());
        } catch (IllegalArgumentException ignored) { // IllegalArgumentException, NumberFormatException などなど...
            // 例外が発生した場合は 404 にする
            log.error("ErrorController catch unexcepted status code: {}", errorInfo);
            return HttpStatus.NOT_FOUND;
        }

        if (!status.isError()) {
            log.warn("ErrorController unexcepted catch non-error status: {}", errorInfo);
        }

        if (allowOutputStatus.contains(status)) {
            return status;
        }

        if (status.is4xxClientError()) {
            log.debug("ErrorController catch not allowed client error status code (so return 404): {}", errorInfo);
        } else if (status.is5xxServerError()) {
            log.error("ErrorController catch not allowed server error status code (so return 404): {}", errorInfo);
        } else {
            log.warn("ErrorController catch not allowed status code (so return 404): {}", errorInfo);
        }
        return HttpStatus.NOT_FOUND;
    }
}
