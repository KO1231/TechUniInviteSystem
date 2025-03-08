package org.techuni.TechUniInviteSystem.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.techuni.TechUniInviteSystem.controller.request.LoginRequest;
import org.techuni.TechUniInviteSystem.controller.response.LoginSuccessResponse;
import org.techuni.TechUniInviteSystem.db.TestUser;
import org.techuni.TechUniInviteSystem.domain.user.UserModel;
import org.techuni.TechUniInviteSystem.error.MyHttpException;
import org.techuni.TechUniInviteSystem.security.JwtTokenProvider;
import org.techuni.TechUniInviteSystem.type.AbstractIntegrationTest;
import org.techuni.TechUniInviteSystem.util.StringUtil;

public class LoginControllerIT extends AbstractIntegrationTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Value("${jwt.issuer}")
    private String expectedTokenIssuer;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    @Override
    @AfterEach
    protected void afterEach() {
        super.afterEach();
        clearUsers();
    }

    @ParameterizedTest
    @MethodSource("validRequestProvider")
    void 正_ログインをすることができる(final String userName, final String password) throws Exception {
        registerUser(TestUser.builder(userName) //
                .rawPassword(password) //
                .build());
        final var request = post("/login", null, new LoginRequest(userName, password));

        // execute
        final var result = mockMvc.perform(request);
        final var response = result //
                .andExpect(status().isOk()) //
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) //
                .andReturn() //
                .getResponse().getContentAsString();

        // verify
        final var parsedResponse = parseResponse(response, LoginSuccessResponse.class);

        final var _decodedToken = tokenProvider.decode(parsedResponse.getToken());
        assertThat(_decodedToken.isPresent()).isTrue();

        final var decodedToken = _decodedToken.get();
        assertThat(decodedToken.getIssuer()).isEqualTo(expectedTokenIssuer);
        assertThat(decodedToken.getSubject()).isEqualTo(userName);
        assertThat(decodedToken.getIssuedAt()).isNotNull();
        if (jwtExpirationInMs > 0) {
            assertThat(decodedToken.getExpiresAt()).isNotNull();
            final var expectedExpireAt = new Date(decodedToken.getIssuedAt().getTime() + jwtExpirationInMs);
            assertThat(decodedToken.getExpiresAt()).isEqualTo(expectedExpireAt);
        } else {
            assertThat(decodedToken.getExpiresAt()).isNull();
        }
    }

    private static Stream<Arguments> validRequestProvider() {
        final var userName = "validUserName";
        final var passwords = new ArrayList<>(List.of("validUser@Password012"));

        // 英字
        for (char large = 'A'; large <= 'Z'; large++) {
            final var sample = "0123456789-a" + large;
            passwords.add(sample);

        }
        for (char small = 'a'; small <= 'z'; small++) {
            final var sample = "0123456789-A" + small;
            passwords.add(sample);
        }

        // 数字
        for (var i = 0; i <= 9; i++) {
            final var sample = "TESTAlphabet-" + i;
            passwords.add(sample);
        }

        // 記号
        for (var c : UserModel.ALLOWED_PASSWORD_SPECIAL_CHARS) {
            final var sample = "TESTAlphabet0" + c;
            passwords.add(sample);
        }

        final var allCondition = passwords.stream() //
                .map(StringUtil::shuffleString) //
                .map(password -> Arguments.of(userName, password));

        // 境界
        final var boundary = Stream.of(Arguments.of("Aa@1-aaaaa", "Aa@1-aaaaa"), // min
                Arguments.of("Aa@1-aaaaa" + "a".repeat(150 - 10), "Aa@1-aaaaa" + "a".repeat(64 - 10)) // max
        );

        return Stream.concat(allCondition, boundary);
    }

    @ParameterizedTest
    @MethodSource("invalidRequestProvider")
    void 異_要件を満たさない情報でログインできない(final String userName, final String password) throws Exception {
        final var request = post("/login", null, new LoginRequest(userName, password));

        // execute
        final var result = mockMvc.perform(request);
        final var exception = result //
                .andExpect(status().isUnauthorized()) //
                .andReturn() //
                .getResolvedException();

        // verify
        assertThat(exception) //
                .isNotNull() //
                .isInstanceOf(MyHttpException.class) //
                .matches(e -> ((MyHttpException) e).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }

    private static Stream<Arguments> invalidRequestProvider() {
        final var validUsername = "validUserName";
        final var validPassword = "validUser@Password012";

        final var basic = Stream.of( //
                // null系
                Arguments.of(null, null), Arguments.of(null, validPassword), Arguments.of(validUsername, null),
                // 空文字系
                Arguments.of("", ""), Arguments.of("", validPassword), Arguments.of(validUsername, ""),
                // 空白文字系
                Arguments.of(" ", " "), Arguments.of(validUsername, " "), Arguments.of(" ", validPassword), Arguments.of("　", "　"),
                Arguments.of(validUsername, "　"), Arguments.of("　", validPassword),
                // ユーザー名系
                Arguments.of("a", validPassword), // 10文字未満
                Arguments.of("a".repeat(200), validPassword), // 150文字超過
                // パスワード系
                Arguments.of(validUsername, "aA1-"), // 10文字未満
                Arguments.of(validUsername, "aA1-" + "a".repeat(200)) // 64文字超過
        );

        // ？のみ系
        final var onlyCase = Stream.of("abc", // 英小文字のみ
                "ABC", // 英大文字のみ
                "123", // 数字のみ
                "^-@", // 記号のみ
                "aAb", // 英小文字と英大文字
                "a1b", // 英小文字と数字
                "a^b", // 英小文字と記号
                "A1B", // 英大文字と数字
                "A^B", // 英大文字と記号
                "1^1", // 数字と記号
                "aA1", // 英小文字と英大文字と数字
                "aA^", // 英小文字と英大文字と記号
                "a1^", // 英小文字と数字と記号
                "A1^" // 英大文字と数字と記号
        ).map(c -> c.repeat(5)) //
                .flatMap(c -> Stream.of(Arguments.of(validUsername, c), Arguments.of(c, validPassword)));

        return Stream.concat(basic, onlyCase) //
                .map(a -> Arguments.of(StringUtil.shuffleString((String) a.get()[0]), StringUtil.shuffleString((String) a.get()[1])));
    }

    @Test
    void 異_正しくないパスワードでログインできない() throws Exception {
        registerUser(TestUser.builder("validUserName") //
                .rawPassword("validUser@Password012") //
                .build());
        final var request = post("/login", null, new LoginRequest("validUserName", "incorrectUser@Password123"));

        // execute
        final var result = mockMvc.perform(request);
        final var exception = result //
                .andExpect(status().isUnauthorized()) //
                .andReturn() //
                .getResolvedException();

        // verify
        assertThat(exception) //
                .isNotNull() //
                .isInstanceOf(MyHttpException.class) //
                .matches(e -> ((MyHttpException) e).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void 異_存在しないユーザーでログインできない() throws Exception {
        clearUsers();
        final var request = post("/login", null, new LoginRequest("notExistUserName", "notFoundUser@Password123"));

        // execute
        final var result = mockMvc.perform(request);
        final var exception = result //
                .andExpect(status().isUnauthorized()) //
                .andReturn() //
                .getResolvedException();

        // verify
        assertThat(exception) //
                .isNotNull() //
                .isInstanceOf(MyHttpException.class) //
                .matches(e -> ((MyHttpException) e).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }

    @Test
    void 異_無効なユーザーでログインできない() throws Exception {
        final String username = "disableUserName", password = "disableUser@Password012";
        registerUser(TestUser.builder(username) //
                .rawPassword(password) //
                .isEnable(false) //
                .build());
        final var request = post("/login", null, new LoginRequest(username, password));

        // execute
        final var result = mockMvc.perform(request);
        final var exception = result //
                .andExpect(status().isUnauthorized()) //
                .andReturn() //
                .getResolvedException();

        // verify
        assertThat(exception) //
                .isNotNull() //
                .isInstanceOf(MyHttpException.class) //
                .matches(e -> ((MyHttpException) e).getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }


}
