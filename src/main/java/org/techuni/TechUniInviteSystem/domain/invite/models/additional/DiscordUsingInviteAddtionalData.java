package org.techuni.TechUniInviteSystem.domain.invite.models.additional;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DiscordUsingInviteAddtionalData extends AbstractUsingInviteAdditionalData {

    String code;

}
