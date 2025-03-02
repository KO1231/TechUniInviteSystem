package org.techuni.TechUniInviteSystem.db.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.techuni.TechUniInviteSystem.db.entity.base.Invite;

@Mapper
public interface InviteWithDiscordStateMapper {

    Invite getInviteByState(@Param("state") String state);

}
