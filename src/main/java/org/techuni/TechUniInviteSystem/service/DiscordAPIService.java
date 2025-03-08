package org.techuni.TechUniInviteSystem.service;

import discord4j.common.util.Snowflake;
import discord4j.discordjson.json.MemberData;
import discord4j.rest.RestClient;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPI;

@Service
@Slf4j
@AllArgsConstructor
public class DiscordAPIService {

    private final RestClient restClient;

    public MemberData joinGuild(final DiscordAPI api, final DiscordInviteModel invite) {
        final var discordInvite = invite.getAdditionalData();

        final var guildIdStr = discordInvite.getGuildID();
        final var guildId = Long.parseLong(guildIdStr);

        if (api.isGuildMember(guildId)) {
            throw ErrorCode.DISCORD_INVITATION_ALREADY_JOINED.exception(String.valueOf(invite.getDbId()), invite.getInvitationCode().toString(),
                    guildIdStr, api.userString());
        }

        return api.joinGuild(guildId, discordInvite.getNickname());
    }

    public boolean checkBotHasPermission(final long guildId, final PermissionSet expected) {
        final var guild = restClient.getGuildById(Snowflake.of(guildId));
        final var hasRoles = Optional.ofNullable(guild.getSelfMember().block()) //
                .map(MemberData::roles) //
                .orElseThrow(() -> ErrorCode.DISCORD_GUILD_ACCESS_ERROR.exception(String.valueOf(guildId)));

        // サーバーに入っているならば、expected none -> always true
        if (expected.isEmpty()) {
            return true;
        }

        // hasRolesがemptyのとき、必ずあるはずのBOT権限も取得できていない -> ロール管理権限がない。
        // ロール管理権限がないときは、ロールによるエラーハンドリングを諦めて招待実行時エラーによるハンドリングで運用する。(最小権限のみを必要とするという非機能要件による)
        if (hasRoles.isEmpty()) {
            return true;
        }

        // logic ref. https://discord.com/developers/docs/topics/permissions
        final var hasPermissions = guild.getRoles() //
                .filter(r -> hasRoles.contains(r.id())) //
                .map(r -> PermissionSet.of(r.permissions())) //
                .toStream() //
                .reduce(PermissionSet.none(), PermissionSet::or);

        if (hasPermissions.contains(Permission.ADMINISTRATOR)) {
            return true;
        }
        return hasPermissions.and(expected).equals(expected);
    }
}
