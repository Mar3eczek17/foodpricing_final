package eu.senlainc.course.foodpricing.config;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class VelocityConfig {

    @Value("${velocity.resource.loader}")
    private String resourceLoader;

    @Value("${velocity.resource.loader.class}")
    private String resourceLoaderClass;

    @Bean
    public VelocityEngine velocityEngine() {
        Properties properties = new Properties();
        properties.setProperty(RuntimeConstants.RESOURCE_LOADER, resourceLoader);
        properties.setProperty("classpath.resource.loader.class", resourceLoaderClass);

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.init(properties);

        return velocityEngine;
    }
}