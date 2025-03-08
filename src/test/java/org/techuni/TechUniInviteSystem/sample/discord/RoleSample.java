package org.techuni.TechUniInviteSystem.sample.discord;

import discord4j.core.object.entity.Role.Flag;
import discord4j.discordjson.Id;
import discord4j.discordjson.json.RoleData;
import discord4j.rest.util.Color;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@Builder
public class RoleSample {

    @Builder.Default
    Id id = Id.of("1" + RandomStringUtils.insecure().nextNumeric(17)); // random 18桁

    @Builder.Default
    String name = "SampleRole";

    @Builder.Default
    Color color = Color.of(0);

    @Builder.Default
    boolean hoist = false;

    @Builder.Default
    int position = 0;

    @Builder.Default
    PermissionSet permissions = PermissionSet.none();

    @Builder.Default
    boolean managed = false;

    @Builder.Default
    boolean mentionable = false;

    @Builder.Default
    int flags = Flag.IN_PROMPT.getFlag();

    public RoleData intoData() {
        return RoleData.builder() //
                .id(id) //
                .name(name) //
                .color(color.getRGB()) //
                .hoist(hoist) //
                .position(position) //
                .permissions(permissions.getRawValue()) //
                .managed(managed) //
                .mentionable(mentionable) //
                .flags(flags) //
                .build();
    }

    public static class RoleSampleBuilder {

        private final static Random random = new Random();

        public RoleSampleBuilder randomPermissions() {
            final var permissions = new ArrayList<>(Arrays.asList(Permission.values()));
            Collections.shuffle(permissions);

            this.permissions(PermissionSet.of(permissions.subList(0, random.nextInt(permissions.size())) //
                    .toArray(Permission[]::new)));
            return this;
        }
    }


}
