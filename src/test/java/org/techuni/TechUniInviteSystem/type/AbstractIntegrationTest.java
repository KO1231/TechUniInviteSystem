package org.techuni.TechUniInviteSystem.type;

import java.util.Optional;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.techuni.TechUniInviteSystem.db.TestUser;
import org.techuni.TechUniInviteSystem.db.TestUserRepository;
import org.techuni.TechUniInviteSystem.db.entity.User;
import org.techuni.TechUniInviteSystem.db.repository.UserRepository;
import org.techuni.TechUniInviteSystem.domain.user.UserDto;
import org.techuni.TechUniInviteSystem.domain.user.UserModel;
import org.techuni.TechUniInviteSystem.security.MyPasswordEncoder;
import org.techuni.TechUniInviteSystem.security.UserAuthority;
import org.techuni.TechUniInviteSystem.util.AuthTokenUtil;
import org.techuni.TechUniInviteSystem.util.GsonUtil;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@IntegrationTest
@NoArgsConstructor
public abstract class AbstractIntegrationTest {

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private GsonUtil gsonUtil;
    @Autowired
    private AuthTokenUtil authTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyPasswordEncoder passwordEncoder;
    @Autowired
    private TestUserRepository testUserRepository;

    @Autowired
    protected MockMvc mockMvc;

    private TransactionStatus allTransactionsStatus;
    private TransactionStatus eachTransactionStatus;


    @BeforeAll
    protected void beforeAll() {
        Assertions.setMaxStackTraceElementsDisplayed(100);
        allTransactionsStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    @BeforeEach
    protected void beforeEach() {
        final var def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_NESTED);
        eachTransactionStatus = transactionManager.getTransaction(def);
    }

    @AfterEach
    protected void afterEach() {
        transactionManager.rollback(eachTransactionStatus);
    }

    @AfterAll
    protected void afterAll() {
        transactionManager.rollback(allTransactionsStatus);
    }

    protected UserModel registerUser(TestUser testUser) {
        testUser.setPassHash(passwordEncoder.passwordEncoder());

        // execute
        final var generatedId = testUserRepository.insertUser(testUser);
        if (!testUser.getAuthorities().isEmpty()) {
            testUserRepository.insertUserAuthority(testUser.getId(), testUser.getAuthorities().stream().map(UserAuthority::getAuthority).toList());
        }

        return new UserDto(new User(testUser.getId(), testUser.getUuid(), testUser.isEnable(), testUser.getName(), testUser.getPassHash(),
                testUser.getAuthorities())).intoModel();
    }

    protected void clearUsers() {
        testUserRepository.resetUserAuthority();
        testUserRepository.resetUser();
    }

    protected MockHttpServletRequestBuilder get(final String url, final UserModel user, final Object content) {
        final var headers = new HttpHeaders();
        Optional.ofNullable(user).ifPresent(u -> //
        headers.setBearerAuth(authTokenUtil.generateToken(u)));

        return MockMvcRequestBuilders.get(url) //
                .contentType("application/json") //
                .content(gsonUtil.getGson().toJson(content)) //
                .headers(headers).servletPath(url);
    }

    protected MockHttpServletRequestBuilder post(final String url, final UserModel user, final Object content) {
        final var headers = new HttpHeaders();
        Optional.ofNullable(user).ifPresent(u -> //
        headers.setBearerAuth(authTokenUtil.generateToken(u)));

        return MockMvcRequestBuilders.post(url) //
                .contentType("application/json") //
                .content(gsonUtil.getGson().toJson(content)) //
                .headers(headers).servletPath(url);
    }

    protected MockHttpServletRequestBuilder put(final String url, final UserModel user, final Object content) {
        final var headers = new HttpHeaders();
        Optional.ofNullable(user).ifPresent(u -> //
        headers.setBearerAuth(authTokenUtil.generateToken(u)));

        return MockMvcRequestBuilders.put(url) //
                .contentType("application/json") //
                .content(gsonUtil.getGson().toJson(content)) //
                .headers(headers).servletPath(url);
    }

    protected MockHttpServletRequestBuilder delete(final String url, final UserModel user, final Object content) {
        final var headers = new HttpHeaders();
        Optional.ofNullable(user).ifPresent(u -> //
        headers.setBearerAuth(authTokenUtil.generateToken(u)));

        return MockMvcRequestBuilders.delete(url) //
                .contentType("application/json") //
                .content(gsonUtil.getGson().toJson(content)) //
                .headers(headers).servletPath(url);
    }

    protected <T> T parseResponse(String responseJson, Class<T> clazz) {
        return gsonUtil.getGson().fromJson(responseJson, clazz);
    }
}
