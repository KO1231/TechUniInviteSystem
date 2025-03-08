package org.techuni.TechUniInviteSystem.sample.discord;

import discord4j.discordjson.json.MemberData;
import discord4j.discordjson.json.UserData;
import discord4j.rest.util.PermissionSet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDataSample {

    @Builder.Default
    UserData user = UserDataSample.builder().build().intoData();

    @Builder.Default
    String permissions = PermissionSet.all().toString();

    @Builder.Default
    boolean deaf = false;

    @Builder.Default
    boolean mute = false;

    public MemberData intoData() {
        return MemberData.builder().user(user).permissions(permissions).deaf(deaf).mute(mute).build();
    }

}
