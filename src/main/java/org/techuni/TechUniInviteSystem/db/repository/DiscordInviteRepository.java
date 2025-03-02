package org.techuni.TechUniInviteSystem.db.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscordState;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteDiscordStateMapper;

@Repository
@AllArgsConstructor
public class DiscordInviteRepository {

    private final ZoneId ZONE;
    private final InviteDiscordStateMapper inviteDiscordStateMapper;

    public void addInviteState(final int inviteId, final String stateString) {
        final var state = new InviteDiscordState();

        state.setInviteId(inviteId);
        state.setState(stateString);
        state.setCreatedAt(LocalDateTime.now(ZONE));

        inviteDiscordStateMapper.insert(state);
    }

}
