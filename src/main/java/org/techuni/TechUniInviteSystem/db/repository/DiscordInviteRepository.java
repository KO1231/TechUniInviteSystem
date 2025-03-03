package org.techuni.TechUniInviteSystem.db.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscordJoinedUser;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscordState;
import org.techuni.TechUniInviteSystem.db.mapper.InviteWithDiscordStateMapper;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteDiscordJoinedUserMapper;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteDiscordStateMapper;

@Repository
@AllArgsConstructor
public class DiscordInviteRepository {

    private final ZoneId ZONE;
    private final InviteDiscordStateMapper inviteDiscordStateMapper;
    private final InviteDiscordJoinedUserMapper inviteDiscordJoinedUserMapper;
    private final InviteWithDiscordStateMapper inviteWithDiscordStateMapper;

    public void addInviteState(final int inviteId, final String stateString) {
        final var state = new InviteDiscordState();

        state.setInviteId(inviteId);
        state.setState(stateString);
        state.setCreatedAt(LocalDateTime.now(ZONE));

        inviteDiscordStateMapper.insert(state);
    }

    public void cleanState(TemporalAmount stateExpireTime) {
        inviteWithDiscordStateMapper.cleanState(LocalDateTime.now(ZONE),
                Optional.ofNullable(stateExpireTime).map(LocalDateTime.now(ZONE)::minus).orElse(null));
    }

    public void addJoinedUser(final int inviteId, final long userId) {
        final var joinedUser = new InviteDiscordJoinedUser();

        joinedUser.setInviteId(inviteId);
        joinedUser.setUserId(userId);
        joinedUser.setJoinedAt(LocalDateTime.now(ZONE));

        inviteDiscordJoinedUserMapper.insert(joinedUser);
    }

}
