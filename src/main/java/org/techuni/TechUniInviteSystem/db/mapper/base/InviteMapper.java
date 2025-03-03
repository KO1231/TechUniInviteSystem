package org.techuni.TechUniInviteSystem.db.mapper.base;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.techuni.TechUniInviteSystem.db.entity.base.Invite;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteExample;

@Mapper
public interface InviteMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    long countByExample(InviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    int deleteByExample(InviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    int insert(Invite row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    int insertSelective(Invite row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    List<Invite> selectByExample(InviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    Invite selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("row") Invite row, @Param("example") InviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    int updateByExample(@Param("row") Invite row, @Param("example") InviteExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Invite row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Invite row);
}