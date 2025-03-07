package org.techuni.TechUniInviteSystem.type;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@UnitTest
@NoArgsConstructor
public abstract class AbstractUnitTest {

    @BeforeAll
    protected void beforeAll() {
        Assertions.setMaxStackTraceElementsDisplayed(100);
    }

    @BeforeEach
    protected void beforeEach() {}

    @AfterEach
    protected void afterEach() {}

    @AfterAll
    protected void afterAll() {}

}
