package org.techuni.TechUniInviteSystem.db.entity.base;

import java.time.LocalDateTime;

public class InviteDiscordJoinedUser {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invite_discord_joined_user.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invite_discord_joined_user.invite_id
     *
     * @mbg.generated
     */
    private Integer inviteId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invite_discord_joined_user.user_id
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column invite_discord_joined_user.joined_at
     *
     * @mbg.generated
     */
    private LocalDateTime joinedAt;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invite_discord_joined_user.id
     *
     * @return the value of invite_discord_joined_user.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invite_discord_joined_user.id
     *
     * @param id the value for invite_discord_joined_user.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invite_discord_joined_user.invite_id
     *
     * @return the value of invite_discord_joined_user.invite_id
     *
     * @mbg.generated
     */
    public Integer getInviteId() {
        return inviteId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invite_discord_joined_user.invite_id
     *
     * @param inviteId the value for invite_discord_joined_user.invite_id
     *
     * @mbg.generated
     */
    public void setInviteId(Integer inviteId) {
        this.inviteId = inviteId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invite_discord_joined_user.user_id
     *
     * @return the value of invite_discord_joined_user.user_id
     *
     * @mbg.generated
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invite_discord_joined_user.user_id
     *
     * @param userId the value for invite_discord_joined_user.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column invite_discord_joined_user.joined_at
     *
     * @return the value of invite_discord_joined_user.joined_at
     *
     * @mbg.generated
     */
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column invite_discord_joined_user.joined_at
     *
     * @param joinedAt the value for invite_discord_joined_user.joined_at
     *
     * @mbg.generated
     */
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord_joined_user
     *
     * @mbg.generated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        InviteDiscordJoinedUser other = (InviteDiscordJoinedUser) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInviteId() == null ? other.getInviteId() == null : this.getInviteId().equals(other.getInviteId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getJoinedAt() == null ? other.getJoinedAt() == null : this.getJoinedAt().equals(other.getJoinedAt()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table invite_discord_joined_user
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInviteId() == null) ? 0 : getInviteId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getJoinedAt() == null) ? 0 : getJoinedAt().hashCode());
        return result;
    }
}