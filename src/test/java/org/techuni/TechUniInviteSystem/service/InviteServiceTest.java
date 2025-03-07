package org.techuni.TechUniInviteSystem.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.techuni.TechUniInviteSystem.db.repository.InviteRepository;
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
    void 異_すでに登録されている招待を作成できない() {
        /* input setup */
        final var inviteDto = InviteSample.builder().dbId(1).build().intoDto();

        // 期待される例外が発生することを検証
        assertThatThrownBy(() -> {
            inviteService.createInvite(inviteDto);
        }).isInstanceOf(MyHttpException.class) //
                .matches(e -> ((MyHttpException) e).getErrorCode().equals(ErrorCode.INVITATION_CREATE_REGISTERED_INVITE));
    }
}
