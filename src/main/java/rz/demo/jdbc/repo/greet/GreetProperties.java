package rz.demo.jdbc.repo.greet;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Rashidi Zin, GfK
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.greet")
public class GreetProperties {

    private String name;

}
