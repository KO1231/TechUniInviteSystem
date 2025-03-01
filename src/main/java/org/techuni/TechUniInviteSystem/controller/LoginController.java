package org.techuni.TechUniInviteSystem.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.techuni.TechUniInviteSystem.controller.request.LoginRequest;
import org.techuni.TechUniInviteSystem.controller.response.LoginSuccessResponse;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.security.JwtTokenProvider;

@Slf4j
@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<LoginSuccessResponse> login(@Validated @RequestBody LoginRequest loginRequest, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw ErrorCode.LOGIN_REQUEST_VALIDATION_ERROR.exception();
        }

        try {
            final var authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUser(), loginRequest.getPassword()));

            final var token = tokenProvider.generateToken(authentication);
            return ResponseEntity.ok().body(new LoginSuccessResponse(token));
        } catch (AccountStatusException e) {
            throw ErrorCode.LOGIN_USER_DENIED.exception(loginRequest.getUser());
        } catch (AuthenticationServiceException e) {
            log.error("Unexpected error occurred.", e);
            throw ErrorCode.LOGIN_UNEXCEPTED_ERROR.exception();
        } catch (AuthenticationException e) {
            throw ErrorCode.LOGIN_FAILED.exception();
        } catch (Exception e) {
            log.error("Unexpected error occurred.", e);
            throw ErrorCode.LOGIN_UNEXCEPTED_ERROR.exception(); // 外部にはただのログイン失敗として扱う。
        }
    }
}
