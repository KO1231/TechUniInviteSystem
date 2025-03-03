package org.techuni.TechUniInviteSystem.db.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CustomInviteMapper {

    void addUsedCountToInvite(@Param("inviteId") int inviteId, @Param("amount") int amount);
}
