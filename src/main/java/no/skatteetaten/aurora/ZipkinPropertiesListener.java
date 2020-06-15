package no.skatteetaten.aurora;

import java.util.Properties;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;
import org.springframework.core.env.PropertiesPropertySource;

public class ZipkinPropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment environment = event.getEnvironment();
        if (environment.acceptsProfiles(Profiles.of("openshift"))) {
            Properties props = new Properties();
            props.setProperty("spring.zipkin.enabled", "true");
            environment.getPropertySources().addLast(new PropertiesPropertySource("aurora.zipkin", props));
        }
    }
}
