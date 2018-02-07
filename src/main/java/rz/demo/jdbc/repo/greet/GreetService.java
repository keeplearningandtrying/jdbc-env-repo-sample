package rz.demo.jdbc.repo.greet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Rashidi Zin, GfK
 */
@AllArgsConstructor
@Service
public class GreetService {

    private GreetProperties properties;

    public String greet(String greet) {
        return String.format("%s, my name is %s", greet, properties.getName());
    }

}
