package org.techuni.TechUniInviteSystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import discord4j.rest.util.PermissionSet;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordJoinSuccessResponse;
import org.techuni.TechUniInviteSystem.db.repository.DiscordInviteRepository;
import org.techuni.TechUniInviteSystem.domain.invite.models.DiscordInviteModel;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.DiscordInviteAdditionalData;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.DiscordUsingInviteAddtionalData;
import org.techuni.TechUniInviteSystem.external.discord.DiscordAPIFactory;
import org.techuni.TechUniInviteSystem.external.discord.template.variables.JoinServerDMVariable;
import org.techuni.TechUniInviteSystem.sample.InviteSample;
import org.techuni.TechUniInviteSystem.sample.discord.MemberDataSample;
import org.techuni.TechUniInviteSystem.service.invite.DiscordInviteService;
import org.techuni.TechUniInviteSystem.type.AbstractUnitTest;

public class DiscordInviteServiceTest extends AbstractUnitTest {

    @Mock
    DiscordInviteRepository discordInviteRepository;

    @Mock
    DiscordAPIFactory discordAPIFactory;

    @Mock
    DiscordAPIService apiService;

    @Mock
    DiscordDMService discordDMService;

    @InjectMocks
    DiscordInviteService discordInviteService;

    String clientId;
    String authenticatedEndpoint;

    @BeforeAll
    public void beforeAll() {
        clientId = "1" + RandomStringUtils.insecure().nextNumeric(17); // random 18桁
        authenticatedEndpoint = "http://localhost:8080/authenticated";

        // mock
        try {
            setField("clientId", clientId);
            setField("authenticatedEndpoint", authenticatedEndpoint);
            setField("zoneId", zoneId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 正_正しい内容で招待を作成できる() {
        /* input setup */
        final var additionalData = new DiscordInviteAdditionalData("123456789123456789", "sample user");
        final var registeredInviteDto = InviteSample.builder().additionalData(additionalData).dbId(1).build().intoDto();

        final var guildId = Long.parseLong(additionalData.getGuildID());
        final var inviteDBid = registeredInviteDto.getDbId();

        /* mock */
        when(apiService.checkBotHasPermission(eq(guildId), any())).thenReturn(true);
        doNothing().when(discordInviteRepository).createInvite(inviteDBid, guildId, additionalData.getNickname());

        /* execute */
        assertThat(discordInviteService.createInvite(registeredInviteDto)) //
                .isEqualTo(registeredInviteDto);
    }

    @Test
    void 正_招待を受諾できる() throws NoSuchFieldException, IllegalAccessException {
        /* input setup */
        final var additionalData = new DiscordInviteAdditionalData("123456789123456789", "sample user");
        final var registeredInviteDto = InviteSample.builder().additionalData(additionalData).dbId(1).build().intoDto();

        final var guildId = Long.parseLong(additionalData.getGuildID());
        final var inviteDBid = registeredInviteDto.getDbId();

        final int stateLength = (int) getFieldValue("STATE_LENGTH");

        /* mock */
        when(apiService.checkBotHasPermission(eq(guildId), any())).thenReturn(true);
        doNothing().when(discordInviteRepository).addInviteState(eq(inviteDBid), any());

        /* execute */
        assertThat(discordInviteService.acceptInvite(registeredInviteDto)) //
                .isNotNull() //
                .matches(r -> r.getClientID().equals(clientId)) //
                .matches(r -> r.getRedirectURI().equals(authenticatedEndpoint)) //
                .matches(r -> StringUtils.isAlphanumeric(r.getState()) && r.getState().length() == stateLength);
    }

    @Test
    void 正_招待できる() {
        /* input setup */
        final var useInviteAdditionalData = new DiscordUsingInviteAddtionalData("sampleAPIAuthenticatedCode");

        final var additionalData = new DiscordInviteAdditionalData("123456789123456789", "sample user");
        final var registeredInviteDto = InviteSample.builder().additionalData(additionalData).dbId(1).build().intoDto();
        final var model = registeredInviteDto.intoModel(DiscordInviteModel.class);

        final var createdMember = MemberDataSample.builder().permissions(PermissionSet.none().toString()).build().intoData();

        final var guildId = Long.parseLong(additionalData.getGuildID());
        final var inviteDBid = registeredInviteDto.getDbId();
        final var joinedUserId = createdMember.user().id().asLong();

        /* mock */
        when(apiService.checkBotHasPermission(eq(guildId), any())).thenReturn(true);
        when(discordAPIFactory.createAPI(any())).thenReturn(null);
        when(apiService.joinGuild(any(), eq(model))).thenReturn(createdMember);
        doNothing().when(discordInviteRepository).addJoinedUser(eq(inviteDBid), eq(joinedUserId));
        doNothing().when(discordDMService).scheduleDM(eq(joinedUserId), eq(new JoinServerDMVariable( //
                joinedUserId, //
                Optional.ofNullable(additionalData.getNickname()).orElse(createdMember.user().username()) //
        )));

        /* execute */
        final var expected = new DiscordJoinSuccessResponse(guildId);
        assertThat(discordInviteService.useInvite(registeredInviteDto, useInviteAdditionalData)) //
                .isEqualTo(expected);
    }

    private Object getFieldValue(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        final var field = DiscordInviteService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(discordInviteService);
    }

    private void setField(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        final var field = DiscordInviteService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(discordInviteService, value);
    }

}
