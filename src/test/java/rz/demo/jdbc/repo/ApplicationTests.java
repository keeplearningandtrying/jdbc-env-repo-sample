package rz.demo.jdbc.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rz.demo.jdbc.repo.greet.GreetService;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    private GreetService service;

    @Test
    public void greetAndResponseContainsNameFromDatabase() {
        String response = service.greet("Hello");

        assertThat(response).isEqualTo("Hello, my name is Demo");
    }
}
