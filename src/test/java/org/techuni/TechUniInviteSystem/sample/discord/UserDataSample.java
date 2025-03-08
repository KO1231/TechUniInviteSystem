package org.techuni.TechUniInviteSystem.sample.discord;

import discord4j.discordjson.Id;
import discord4j.discordjson.json.UserData;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataSample {

    @Builder.Default
    Id id = Id.of(1234567890123456789L);

    @Builder.Default
    Optional<String> globalName = Optional.of("TechUniSampleUser");

    @Builder.Default
    String username = "TechUniSampleUser";

    @Builder.Default
    String discriminator = "1234";

    public UserData intoData() {
        return UserData.builder().id(id).globalName(globalName).username(username).discriminator(discriminator).build();
    }

}
