package org.techuni.TechUniInviteSystem.security;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserAuthority {
    INVITE_WRITE,;

    public String getAuthority() {
        return this.name();
    }
}
