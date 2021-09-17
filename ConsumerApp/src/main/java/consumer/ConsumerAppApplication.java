package consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumerAppApplication implements CommandLineRunner{
	
	private static final Logger log = LoggerFactory.getLogger(ConsumerAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConsumerAppApplication.class, args);
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Starting...");
		log.info("Starting...");
		
	}

}
