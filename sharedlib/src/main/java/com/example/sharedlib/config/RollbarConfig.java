package com.example.sharedlib.config;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
        "com.example.sharedlib",
        "com.rollbar.spring"
})
public class RollbarConfig {

    @Value("${rollbar.accessToken}")
    String accessToken;

    @Bean
    public Rollbar rollbar() {
        return new Rollbar(getRollbackConfigurations(
                accessToken
        ));
    }

    private Config getRollbackConfigurations(String accessToken) {
        return RollbarSpringConfigBuilder.withAccessToken(accessToken)
                .environment("DEV")
                .build();
    }
}
