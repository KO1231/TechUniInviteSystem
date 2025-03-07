package org.techuni.TechUniInviteSystem.db.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.techuni.TechUniInviteSystem.db.entity.base.Invite;

@Mapper
public interface CustomInviteMapper {

    void addUsedCountToInvite(@Param("inviteId") int inviteId, @Param("amount") int amount);

    int insertInvite(@Param("invite") Invite invite);
}
