package org.techuni.TechUniInviteSystem.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordAuthRequestResponse;
import org.techuni.TechUniInviteSystem.controller.response.invite.DiscordJoinSuccessResponse;
import org.techuni.TechUniInviteSystem.db.repository.InviteRepository;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.DiscordUsingInviteAddtionalData;
import org.techuni.TechUniInviteSystem.error.ErrorCode;
import org.techuni.TechUniInviteSystem.error.MyHttpException;
import org.techuni.TechUniInviteSystem.sample.InviteSample;
import org.techuni.TechUniInviteSystem.service.invite.DiscordInviteService;
import org.techuni.TechUniInviteSystem.type.AbstractUnitTest;

public class InviteServiceTest extends AbstractUnitTest {

    @Mock
    InviteRepository inviteRepository;

    @Mock
    DiscordInviteService discordInviteService;

    @InjectMocks
    InviteService inviteService;

    @BeforeAll
    public void beforeAll() {
        // mock
        try {
            setField("zoneId", zoneId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 正_正しい内容で招待を作成できる() {
        /* input setup */
        final var inviteDto = InviteSample.builder().build().intoDto();
        final var resultDto = InviteSample.builder().dbId(1).build().intoDto();

        /* mock */
        when(inviteRepository.createInvite(inviteDto)).thenReturn(resultDto);
        when(discordInviteService.createInvite(resultDto)).thenReturn(resultDto);

        /* execute */
        inviteService.createInvite(inviteDto);
    }

    @Test
    void 正_有効な招待を受諾できる() {
        /* input setup */
        final var inviteDto = InviteSample.builder().dbId(1).build().intoDto();
        final var response = new DiscordAuthRequestResponse("123456789123456789", "http://localhost:8080", "state");

        /* mock */
        when(discordInviteService.acceptInvite(inviteDto)).thenReturn(response);

        /* execute */
        assertThat(inviteService.acceptInvite(inviteDto)) //
                .isEqualTo(response.intoView());
    }

    @Test
    void 正_招待できる() {
        /* input setup */
        final var resultDto = InviteSample.builder().dbId(1).build().intoDto();
        final var usingAdditionalData = new DiscordUsingInviteAddtionalData("sampleAPIAuthenticatedCode");
        final var response = new DiscordJoinSuccessResponse(123456789123456789L);

        /* mock */
        doNothing().when(inviteRepository).useInvite(resultDto.getDbId());
        when(discordInviteService.useInvite(resultDto, usingAdditionalData)).thenReturn(response);

        /* execute */
        assertThat(inviteService.useInvite(resultDto, usingAdditionalData)) //
                .isEqualTo(response);
    }

    @Test
    void 異_すでに登録されている招待を作成できない() {
        /* input setup */
        final var inviteDto = InviteSample.builder().dbId(1).build().intoDto();

        // 期待される例外が発生することを検証
        assertThatThrownBy(() -> {
            inviteService.createInvite(inviteDto);
        }).isInstanceOf(MyHttpException.class) //
                .matches(e -> ((MyHttpException) e).getErrorCode().equals(ErrorCode.INVITATION_CREATE_REGISTERED_INVITE));
    }

    @ParameterizedTest
    @MethodSource("invalidCreateInviteAcceptProvider")
    void 異_無効な招待を作成できない(final InviteDto inviteDto) {

        // 期待される例外が発生することを検証
        assertThatThrownBy(() -> {
            inviteService.createInvite(inviteDto);
        }).isInstanceOf(MyHttpException.class) //
                .matches(e -> ((MyHttpException) e).getErrorCode().equals(ErrorCode.INVITATION_CREATE_INVALID_INVITE));
    }

    private Stream<InviteDto> invalidCreateInviteAcceptProvider() {
        final var expiredDate = ZonedDateTime.now(zoneId).minusSeconds(1);

        return Stream.of( //
                InviteSample.builder() //
                        .disabled() //
                        .build().intoDto(), // disable

                InviteSample.builder() //
                        .fullUsed(0) //
                        .build().intoDto(), // used

                InviteSample.builder() //
                        .expiresAt(expiredDate) //
                        .build().intoDto(), // expired

                InviteSample.builder() //
                        .disabled().fullUsed(0).build().intoDto(), // disable & used

                InviteSample.builder() //
                        .fullUsed(0).expiresAt(expiredDate) //
                        .build().intoDto(), // used & expired

                InviteSample.builder() //
                        .disabled().expiresAt(expiredDate) //
                        .build().intoDto(), // disable & expired

                InviteSample.builder() //
                        .disabled().fullUsed(0).expiresAt(expiredDate) //
                        .build().intoDto() // disable & used & expired
        );
    }

    @ParameterizedTest
    @MethodSource("invalidInviteAcceptProvider")
    void 異_無効な招待を受諾できない(final InviteDto inviteDto) {
        assertThatThrownBy(() -> {
            inviteService.acceptInvite(inviteDto);
        }).isInstanceOf(MyHttpException.class) //
                .matches(e -> ((MyHttpException) e).getErrorCode().equals(ErrorCode.INVITATION_INVALID));
    }

    private Stream<InviteDto> invalidInviteAcceptProvider() {
        final var expiredDate = ZonedDateTime.now(zoneId).minusDays(1);

        return Stream.of( //
                InviteSample.builder().registered() //
                        .disabled() //
                        .build().intoDto(), // disable

                InviteSample.builder().registered() //
                        .fullUsed() //
                        .build().intoDto(), // used

                InviteSample.builder().registered() //
                        .expiresAt(expiredDate) //
                        .build().intoDto(), // expired

                InviteSample.builder().registered() //
                        .disabled().fullUsed().build().intoDto(), // disable & used

                InviteSample.builder().registered() //
                        .fullUsed().expiresAt(expiredDate) //
                        .build().intoDto(), // used & expired

                InviteSample.builder().registered() //
                        .disabled().expiresAt(expiredDate) //
                        .build().intoDto(), // disable & expired

                InviteSample.builder().registered() //
                        .disabled().fullUsed().expiresAt(expiredDate) //
                        .build().intoDto() // disable & used & expired
        );
    }

    private void setField(String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        final var field = InviteService.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(inviteService, value);
    }
}
