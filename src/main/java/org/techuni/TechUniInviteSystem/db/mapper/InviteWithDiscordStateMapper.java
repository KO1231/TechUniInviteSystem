package org.techuni.TechUniInviteSystem.db.mapper;

import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.techuni.TechUniInviteSystem.db.entity.base.Invite;

@Mapper
public interface InviteWithDiscordStateMapper {

    Invite getInviteByState(@Param("state") String state);

    void cleanState(@Param("nowTime") LocalDateTime nowTime, @Param("stateExpireTime") LocalDateTime stateExpireTime);
}
