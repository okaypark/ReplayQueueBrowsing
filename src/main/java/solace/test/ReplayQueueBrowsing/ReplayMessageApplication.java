package solace.test.ReplayQueueBrowsing;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ReplayMessageApplication {

	@SneakyThrows
    public static void main(String[] args) {SpringApplication.run(ReplayMessageApplication.class, args);}

}
