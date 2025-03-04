package org.techuni.TechUniInviteSystem.controller.request.invite;

import static java.util.Objects.isNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Length;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;

@Getter
@EqualsAndHashCode
@SuperBuilder
@Jacksonized
@AllArgsConstructor(onConstructor_ = @JsonCreator)
public class CreateInviteRequest {

    @Length(max = 255)
    @JsonProperty("searchId")
    private final String searchId;

    @NotNull
    @JsonProperty("targetApp")
    private final TargetApplication targetApp;

    @Builder.Default
    @JsonProperty("maxUsed")
    private final int maxUsed = 1;

    @JsonProperty("expirationDate")
    private final ZonedDateTime expirationDate;

    @JsonIgnore
    @AssertTrue
    public boolean isExpirationDateValid() {
        return isNull(expirationDate) || expirationDate.isAfter(ZonedDateTime.now());
    }
}
