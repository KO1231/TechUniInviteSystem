package org.techuni.TechUniInviteSystem.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorMessage {

    /**
     * OTHER
     */
    INTERNAL_UNEXPECTED_ERROR("Unexpected error occurred. Message: %s"), //
    UNEXPECTED_ERROR("Some error occurred. Please try again later. If you have this error repeatedly, please contact to the inviter."), //

    /**
     * LOGIN
     **/
    LOGIN_USER_DENIED("Denied user tried to login. (User: %s)"), //
    LOGIN_UNEXCEPTED_ERROR("Unexpected error occurred."), //

    /**
     * INVITATION
     */
    INVITATION_INVALID(
            "This invitation is not available. Please check the code availibility (e.g. expiration date, usage limit). If you think this is a mistake, please contact to the inviter."), //
    INTERNAL_INVITATION_ALREADY_USED("Request Already Used Invitation Code. (Code: %s)"), //
    INTERNAL_INVITATION_INVALID("Request Invalid Invitation Code. (Code: %s)"), //

    /* DISCORD INVITATION */
    INTERNAL_DISCORD_ALREADY_JOINED(
            "The Discord Invitation target already joined to the guild. (DbId: %s, InvitationCode: %s, Guild: %s, User: %s)"), //
    DISCORD_ALREADY_JOINED(
            "Your account have already joined to the discord server. If you think this is a mistake, please contact to the inviter."), //
    INTERNAL_DISCORD_LOGIN_FAILED("Discord login failed."), //
    DISCORD_LOGIN_FAILED("Login to Your account is failed. If you think this is a mistake, please try again."), //
    INTERNAL_DISCORD_LOGIN_UNEXPECTED_ERROR("Unexpected error occurred in Discord login sequence."), //
    INTERNAL_DISCORD_UNEXPECTED_ERROR("Unexpected error occurred in Discord API."), //
    DISCORD_UNEXPECTED_ERROR("Unexpected error occurred in Discord API. If you have this error repeatedly, please contact to the inviter."), //
    INTERNAL_DISCORD_LOAD_GUILD_LIST_FAILED("Failed to load guild list from Discord API. (User: %s)"), //
    INTERNAL_DISCORD_LOAD_USER_INFO_FAILED("Failed to load user info from Discord API."), //
    INTERNAL_DISCORD_LOGIN_DENIED("User denied to login to Discord."), //
    DISCORD_LOGIN_DENIED("You denied to login to Discord. If you think this is a mistake, please try again."), //
    ;

    private final String message;
}
