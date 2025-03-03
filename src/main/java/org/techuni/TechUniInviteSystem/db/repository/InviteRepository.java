package org.techuni.TechUniInviteSystem.db.repository;

import static java.util.Objects.isNull;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.techuni.TechUniInviteSystem.db.entity.base.Invite;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscordExample;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteExample;
import org.techuni.TechUniInviteSystem.db.mapper.InviteWithDiscordStateMapper;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteDiscordMapper;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteMapper;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;

@Repository
@AllArgsConstructor
public class InviteRepository {

    private final ZoneId ZONE;
    private final InviteMapper inviteMapper;
    private final InviteDiscordMapper inviteDiscordMapper;
    private final InviteWithDiscordStateMapper inviteWithDiscordStateMapper;

    public InviteDto getInviteByCode(String code) {
        final var searchExample = new InviteExample();

        searchExample.or() //
                .andCodeEqualTo(code);

        final var invite = inviteMapper.selectByExample(searchExample) //
                .stream() //
                .findFirst() //
                .orElse(null);

        if (isNull(invite)) {
            return null;
        }

        return InviteDto.fromDB(invite, ZONE, getAdditionalData(TargetApplication.getById(invite.getTargetAppId()), invite.getId()));
    }

    public InviteDto getInviteByState(String state) {

        final var invite = inviteWithDiscordStateMapper.getInviteByState(state);

        if (isNull(invite)) {
            return null;
        }

        return InviteDto.fromDB(invite, ZONE, getAdditionalData(TargetApplication.getById(invite.getTargetAppId()), invite.getId()));
    }

    private Map<String, Object> getAdditionalData(TargetApplication targetApplication, int dbId) {
        final var result = new HashMap<String, Object>();

        if (targetApplication == TargetApplication.DISCORD) {
            final var example = new InviteDiscordExample();
            example.or() //
                    .andInviteIdEqualTo(dbId);

            final var discordInvite = inviteDiscordMapper.selectByExample(example) //
                    .stream() //
                    .findFirst() //
                    .orElseThrow(() -> new IllegalStateException("Additional data not found."));

            result.put("guildID", discordInvite.getGuildId());
            result.put("nickname", discordInvite.getNickname());
        }

        return result;
    }

    public void useInvite(int inviteId) {
        final var updateInviteSelective = new Invite();
        updateInviteSelective.setIsUsed(true);

        final var example = new InviteExample();
        example.or() //
                .andIdEqualTo(inviteId);

        inviteMapper.updateByExampleSelective(updateInviteSelective, example);
    }

}
