package org.techuni.TechUniInviteSystem.db.repository;

import jakarta.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscord;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscordExample;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscordJoinedUser;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscordState;
import org.techuni.TechUniInviteSystem.db.mapper.InviteWithDiscordStateMapper;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteDiscordJoinedUserMapper;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteDiscordMapper;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteDiscordStateMapper;
import org.techuni.TechUniInviteSystem.domain.invite.models.additional.DiscordInviteAdditionalData;

@Repository
@AllArgsConstructor
public class DiscordInviteRepository {

    private final ZoneId ZONE;
    private final InviteDiscordStateMapper inviteDiscordStateMapper;
    private final InviteDiscordJoinedUserMapper inviteDiscordJoinedUserMapper;
    private final InviteWithDiscordStateMapper inviteWithDiscordStateMapper;
    private final InviteDiscordMapper inviteDiscordMapper;

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

    @PreDestroy
    public void deleteAllStateWhenShutdown() {
        inviteDiscordStateMapper.deleteByExample(null);
    }

    public void addJoinedUser(final int inviteId, final long userId) {
        final var joinedUser = new InviteDiscordJoinedUser();

        joinedUser.setInviteId(inviteId);
        joinedUser.setUserId(userId);
        joinedUser.setJoinedAt(LocalDateTime.now(ZONE));

        inviteDiscordJoinedUserMapper.insert(joinedUser);
    }

    public void createInvite(final int inviteId, long guild_id, String nickname) {
        final var data = new InviteDiscord();

        data.setInviteId(inviteId);
        data.setGuildId(guild_id);
        data.setNickname(nickname);

        inviteDiscordMapper.insert(data);
    }

    public DiscordInviteAdditionalData getAdditionalData(int dbId) {
        final var example = new InviteDiscordExample();
        example.or() //
                .andInviteIdEqualTo(dbId);

        final var discordInvite = inviteDiscordMapper.selectByExample(example) //
                .stream() //
                .findFirst() //
                .orElseThrow(() -> new IllegalStateException("Additional data not found."));

        return new DiscordInviteAdditionalData(String.valueOf(discordInvite.getGuildId()), discordInvite.getNickname());
    }

}
