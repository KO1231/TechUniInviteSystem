package org.techuni.TechUniInviteSystem.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min = 10, max = 150)
    String user;

    @NotBlank
    @Size(min = 10, max = 64) // bcryptは72バイトまで, NISC, NISTを参考
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[\\^$*.\\[\\]{}()?!@#%&,><:;|_\\-~`]).+$") // 英大小文字数字と ^$*.[]{}()?"!@#%&,><':;|_-~`
    String password;
}
