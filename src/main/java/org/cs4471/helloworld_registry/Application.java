package org.cs4471.helloworld_registry;

import org.cs4471.helloworld_registry.service.RegistryHeartbeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements ApplicationRunner {
	@Autowired
	private RegistryHeartbeat registryHeartbeat;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		registryHeartbeat.start();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("Registry : Started");
	}
}
