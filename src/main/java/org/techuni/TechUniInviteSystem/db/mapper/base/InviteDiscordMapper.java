package org.techuni.TechUniInviteSystem.db.mapper.base;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscord;
import org.techuni.TechUniInviteSystem.db.entity.base.InviteDiscordExample;

@Mapper
public interface InviteDiscordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    long countByExample(InviteDiscordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    int deleteByExample(InviteDiscordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer inviteId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    int insert(InviteDiscord row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    int insertSelective(InviteDiscord row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    List<InviteDiscord> selectByExample(InviteDiscordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    InviteDiscord selectByPrimaryKey(Integer inviteId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("row") InviteDiscord row, @Param("example") InviteDiscordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    int updateByExample(@Param("row") InviteDiscord row, @Param("example") InviteDiscordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(InviteDiscord row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(InviteDiscord row);
}