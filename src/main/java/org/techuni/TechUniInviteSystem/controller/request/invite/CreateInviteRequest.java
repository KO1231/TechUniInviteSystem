package org.techuni.TechUniInviteSystem.controller.request.invite;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;

@Getter
@EqualsAndHashCode
@SuperBuilder
public class CreateInviteRequest {

    @Length(max = 255)
    private final String searchId;

    @NotNull
    private final TargetApplication targetApp;

    @Builder.Default
    private final int maxUsed = 1;

    private final ZonedDateTime expirationDate;

    @JsonIgnore
    @AssertTrue
    public boolean isExpirationDateValid() {
        return isNull(expirationDate) || expirationDate.isAfter(ZonedDateTime.now());
    }
}
