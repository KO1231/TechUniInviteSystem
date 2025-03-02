package org.techuni.TechUniInviteSystem.db.repository;

import static java.util.Objects.isNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscordExample;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteExample;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteDiscordMapper;
import org.techuni.TechUniInviteSystem.db.mapper.base.InviteMapper;
import org.techuni.TechUniInviteSystem.domain.invite.InviteDto;
import org.techuni.TechUniInviteSystem.domain.invite.TargetApplication;

@Repository
@AllArgsConstructor
public class InviteRepository {

    private final static ZoneId ZONE = ZoneId.of("Asia/Tokyo");

    private final InviteMapper inviteMapper;
    private final InviteDiscordMapper inviteDiscordMapper;

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

        final var expiresAt = Optional.ofNullable(invite.getExpiresAt()).map(t -> t.atZone(ZONE));
        final boolean isEnable = !invite.getIsDisabled() && expiresAt.map(t -> t.isAfter(ZonedDateTime.now(ZONE))).orElse(true);

        final var additionalData = getAdditionalData(TargetApplication.getById(invite.getTargetAppId()), invite.getId());

        return new InviteDto(invite.getId(), invite.getCode(), invite.getSearchId(), isEnable, TargetApplication.getById(invite.getTargetAppId()),
                expiresAt.orElse(null), additionalData);
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
        }

        return result;
    }

}
