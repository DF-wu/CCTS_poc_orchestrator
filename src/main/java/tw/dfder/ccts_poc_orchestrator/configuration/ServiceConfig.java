package tw.dfder.ccts_poc_orchestrator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * To load application.yml setting
 * @Author df
 * @version v1
 */
@Configuration
public class ServiceConfig {
    @Value("${serviceInfo.name}")
    public static String serviceName;

    @Value("${serviceInfo.pact}")
    public static String correspondingPact;

    @Value("${serviceInfo.destinations}")
    public static List<String> destinations;

}
