package org.techuni.TechUniInviteSystem.controller.request.invite;

import static java.util.Objects.isNull;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

@Getter
@EqualsAndHashCode
@SuperBuilder
public class CreateInviteRequest {

    @Length(max = 255)
    private final String searchId;

    @NotBlank
    private final String targetApp;

    private final ZonedDateTime expirationDate;

    @AssertTrue
    public boolean isExpirationDateValid() {
        return isNull(expirationDate) || expirationDate.isAfter(ZonedDateTime.now());
    }
}
