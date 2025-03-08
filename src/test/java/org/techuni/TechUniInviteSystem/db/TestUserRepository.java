package org.techuni.TechUniInviteSystem.db;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TestUserRepository {

    long insertUser(@Param("insertUser") TestUser insertUser);

    void insertUserAuthority(@Param("id") long id, @Param("authorities") List<String> authorities);

    void resetUser();

    void resetUserAuthority();
}
