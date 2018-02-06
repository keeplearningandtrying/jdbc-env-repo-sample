package rz.demo.jdbc.repo.factory;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;

/**
 * @author Rashidi Zin, GfK
 */
@EnableAutoConfiguration
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class CloudBootstrapConfiguration {

}
