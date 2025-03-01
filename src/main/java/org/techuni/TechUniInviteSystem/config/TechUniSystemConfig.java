package org.techuni.TechUniInviteSystem.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
public class TechUniSystemConfig {

    // swagger表示設定(デフォルト非表示)
    @Value("${techuni.swagger.show:false}")
    private boolean showSwagger;
}
