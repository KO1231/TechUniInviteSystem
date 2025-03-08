package org.techuni.TechUniInviteSystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import discord4j.common.util.Snowflake;
import discord4j.discordjson.json.MemberData;
import discord4j.discordjson.json.RoleData;
import discord4j.rest.RestClient;
import discord4j.rest.entity.RestGuild;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.DiscordInviteAdditionalData;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.error.MyHttpException;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPI;
import org.techuni.TechUniInviteSystem.sample.InviteSample;
import org.techuni.TechUniInviteSystem.sample.discord.MemberDataSample;
import org.techuni.TechUniInviteSystem.sample.discord.RoleSample;
import org.techuni.TechUniInviteSystem.type.AbstractUnitTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DiscordAPIServiceTest extends AbstractUnitTest {

    @Mock
    RestClient restClient;

    @Mock
    DiscordAPI discordAPI;

    @InjectMocks
    DiscordAPIService discordAPIService;

    @Test
    void 正_サーバーに招待させることができる() {
        // Setup
        final var additionalData = new DiscordInviteAdditionalData("1234567890123456789", "sample user");
        final var invite = InviteSample.builder().additionalData(additionalData).build().intoDto();
        final var inviteModel = invite.intoModel(DiscordInviteModel.class);
        final var resultMember = MemberDataSample.builder().build().intoData();

        final var guildId = Long.parseLong(inviteModel.getAdditionalData().getGuildID());

        // mock
        when(discordAPI.isGuildMember(guildId)).thenReturn(false);
        when(discordAPI.joinGuild(guildId, inviteModel.getAdditionalData().getNickname())) //
                .thenReturn(resultMember);

        // Execute
        assertThat(discordAPIService.joinGuild(discordAPI, inviteModel)) //
                .isEqualTo(resultMember);
    }

    @ParameterizedTest
    @MethodSource("checkPermissionsProvider")
    void 正_自BOTのサーバーにおける特定の権限を所有有無を確認できる(boolean expectedResult, PermissionSet checkPermissions, List<PermissionSet> hasRolesPermissions) {
        // Setup
        final var guildId = 1234567890123456789L;

        final var hasRoles = hasRolesPermissions.stream() //
                .map(p -> RoleSample.builder().permissions(p).build().intoData()) //
                .toList();
        final var dummyRoles = IntStream.range(0, 10) //
                .mapToObj(i -> RoleSample.builder().randomPermissions().build().intoData()) //
                .toList();

        // mock
        final var mockGuild = Mockito.mock(RestGuild.class);
        final var mockSelfMember = Mockito.mock(MemberData.class);
        when(restClient.getGuildById(Snowflake.of(guildId))).thenReturn(mockGuild);

        when(mockGuild.getSelfMember()).thenReturn(Mono.fromSupplier(() -> mockSelfMember));
        when(mockSelfMember.roles()).thenReturn(hasRoles.stream().map(RoleData::id).toList());
        when(mockGuild.getRoles()).thenReturn(Flux.fromStream(Stream.concat(hasRoles.stream(), dummyRoles.stream())));

        // Execute
        assertThat(discordAPIService.checkBotHasPermission(guildId, checkPermissions)).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> checkPermissionsProvider() {
        return Stream.of( //
                // basic
                Arguments.of(true, PermissionSet.none(), List.of(PermissionSet.none())), //
                Arguments.of(true, PermissionSet.none(), List.of(PermissionSet.none())), //
                Arguments.of(false, PermissionSet.all(), List.of(PermissionSet.none())), //
                Arguments.of(true, PermissionSet.all(), List.of(PermissionSet.all())), //

                // ADMINISTRATORをもっていたら無条件true
                Arguments.of(true, PermissionSet.none(), List.of(PermissionSet.of(Permission.ADMINISTRATOR))), //
                Arguments.of(true, PermissionSet.all().andNot(PermissionSet.of(Permission.ADMINISTRATOR)),
                        List.of(PermissionSet.of(Permission.ADMINISTRATOR))), //

                // use case (単一)
                Arguments.of(true, PermissionSet.of(Permission.CREATE_INSTANT_INVITE), List.of(PermissionSet.of(Permission.CREATE_INSTANT_INVITE))),
                //
                Arguments.of(true, PermissionSet.of(Permission.MANAGE_NICKNAMES), List.of(PermissionSet.of(Permission.MANAGE_NICKNAMES))), //
                Arguments.of(false, PermissionSet.of(Permission.CREATE_INSTANT_INVITE), List.of(PermissionSet.of(Permission.MANAGE_NICKNAMES))), //
                Arguments.of(false, PermissionSet.of(Permission.MANAGE_NICKNAMES), List.of(PermissionSet.of(Permission.CREATE_INSTANT_INVITE))), //

                // use case (複数・一権限)
                Arguments.of(true, PermissionSet.of(Permission.CREATE_INSTANT_INVITE),
                        List.of(PermissionSet.of(Permission.CREATE_INSTANT_INVITE, Permission.MANAGE_NICKNAMES))), //
                Arguments.of(true, PermissionSet.of(Permission.MANAGE_NICKNAMES),
                        List.of(PermissionSet.of(Permission.CREATE_INSTANT_INVITE, Permission.MANAGE_NICKNAMES))), //
                Arguments.of(true, PermissionSet.of(Permission.CREATE_INSTANT_INVITE, Permission.MANAGE_NICKNAMES),
                        List.of(PermissionSet.of(Permission.CREATE_INSTANT_INVITE, Permission.MANAGE_NICKNAMES))), //

                // use case (複数・複数権限)
                Arguments.of(true, PermissionSet.of(Permission.CREATE_INSTANT_INVITE),
                        List.of(PermissionSet.of(Permission.CREATE_INSTANT_INVITE), PermissionSet.of(Permission.MANAGE_NICKNAMES))), //
                Arguments.of(true, PermissionSet.of(Permission.MANAGE_NICKNAMES),
                        List.of(PermissionSet.of(Permission.CREATE_INSTANT_INVITE), PermissionSet.of(Permission.MANAGE_NICKNAMES))), //
                Arguments.of(true, PermissionSet.of(Permission.CREATE_INSTANT_INVITE, Permission.MANAGE_NICKNAMES),
                        List.of(PermissionSet.of(Permission.CREATE_INSTANT_INVITE), PermissionSet.of(Permission.MANAGE_NICKNAMES))), //

                Arguments.of(false, PermissionSet.of(Permission.CREATE_INSTANT_INVITE, Permission.MANAGE_NICKNAMES),
                        List.of(PermissionSet.of(Permission.CREATE_INSTANT_INVITE))), //
                Arguments.of(false, PermissionSet.of(Permission.CREATE_INSTANT_INVITE, Permission.MANAGE_NICKNAMES),
                        List.of(PermissionSet.of(Permission.MANAGE_NICKNAMES))) //

        );
    }

    @Test
    void 異_すでにユーザーが参加しているサーバーに招待させない() {
        // Setup
        final var additionalData = new DiscordInviteAdditionalData("1234567890123456789", "sample user");
        final var invite = InviteSample.builder().additionalData(additionalData).build().intoDto();
        final var inviteModel = invite.intoModel(DiscordInviteModel.class);

        final var guildId = Long.parseLong(inviteModel.getAdditionalData().getGuildID());

        // mock
        when(discordAPI.isGuildMember(guildId)).thenReturn(true);

        // Execute
        assertThatThrownBy(() -> discordAPIService.joinGuild(discordAPI, inviteModel)) //
                .isInstanceOf(MyHttpException.class) //
                .matches(e -> ((MyHttpException) e).getErrorCode().equals(ErrorCode.DISCORD_INVITATION_ALREADY_JOINED));
    }

}
