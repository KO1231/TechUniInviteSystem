package org.techuni.TechUniInviteSystem.db.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.techuni.TechUniInviteSystem.db.entity.User;

@Mapper
public interface UserMapper {

    User getUserByName(@Param("name") String name);
}
