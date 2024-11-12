package org.cs4471.registry;

import org.cs4471.registry.service.RegistryHeartbeat;
import org.cs4471.registry.service.RegistryRegistrar;
import org.cs4471.registry.service.ServiceEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@SpringBootApplication
public class Application implements ApplicationRunner {
	@Value("${service.registrar}")
	private String primaryURL;

	@Value("${service.registrarbackup}")
	private String backupURL;

	@Autowired
	private RegistryRegistrar registrar;

	@Autowired
	private RegistryHeartbeat registryHeartbeat;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// Startup sync with registry
		String[] args1 = args.getSourceArgs();
		if (args1.length > 0) {
			ArrayList<LinkedHashMap<String, String>> entries = null;

			String which = args1[0];
			if (which.equals("primary")) {
				System.out.println("Registry (primary): Startup sync with backup registry...");

				entries = WebClient.builder().baseUrl(backupURL).build().get().uri("/getServices").retrieve().bodyToMono(ArrayList.class)
						.timeout(Duration.ofSeconds(30))
						.onErrorResume(Exception.class, ex -> Mono.just(new ArrayList()))
						.block();
			}
			else if (which.equals("backup")) {
				System.out.println("Registry (backup): Startup sync with primary registry...");

				entries = WebClient.builder().baseUrl(primaryURL).build().get().uri("/getServices").retrieve().bodyToMono(ArrayList.class)
						.timeout(Duration.ofSeconds(30))
						.onErrorResume(Exception.class, ex -> Mono.just(new ArrayList()))
						.block();
			}

			if (entries != null && !entries.isEmpty()) {
				int count = 0;
				for (LinkedHashMap<String, String> s : entries) {
					// Prevent any empty string from being entered
					if (s.containsKey("name") && !s.get("name").isEmpty()
							&& s.containsKey("url") && !s.get("url").isEmpty()
							&& s.containsKey("desc") && !s.get("desc").isEmpty()) {
						registrar.addService(new ServiceEntry(s.get("name"), s.get("url"), s.get("desc")));
						count++;
					}
				}
				System.out.println(String.format("Registry: Synced %d services", count));
			}
			else System.out.println("Registry : No sync needed (registry is down or empty list)");
		}

		registryHeartbeat.start();
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("Registry : Started");
	}
}
