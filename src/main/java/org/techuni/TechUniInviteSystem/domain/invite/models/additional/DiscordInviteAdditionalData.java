package org.techuni.TechUniInviteSystem.domain.invite.models.additional;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class DiscordInviteAdditionalData extends AbstractInviteAdditionalData {

    String guildID;
    String nickname;

    @Override
    public Map<String, Object> intoMap() {
        return Map.of("guildID", guildID, "nickname", nickname);
    }

    public static DiscordInviteAdditionalData fromMap(Map<String, Object> map) {
        return new DiscordInviteAdditionalData(map.get("guildID").toString(), (String) map.get("nickname"));
    }
}
