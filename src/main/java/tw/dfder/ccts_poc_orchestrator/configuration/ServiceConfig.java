package tw.dfder.ccts_poc_orchestrator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * To load application.yml setting
 * @Author df
 * @version v1
 */
@Configuration
@ConfigurationProperties(prefix = "serviceInfo")
public class ServiceConfig {
//    @Value("${serviceInfo.name}")
    public String serviceName;
//
//    @Value("${serviceInfo.pact}")
//    public static String correspondingPact;

    public List<String> destinations;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }
}
