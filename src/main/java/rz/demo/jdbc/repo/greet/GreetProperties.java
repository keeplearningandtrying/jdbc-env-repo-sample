package rz.demo.jdbc.repo.greet;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rashidi Zin, GfK
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.greet")
public class GreetProperties {

    private String name;

}
