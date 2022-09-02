package com.api.ChallengeBackend;

import com.api.ChallengeBackend.dao.repository.RoleRepository;
import com.api.ChallengeBackend.domain.models.ERole;
import com.api.ChallengeBackend.domain.models.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class ChallengeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {


			roleRepository.saveAll(
					List.of(
					new Role(ERole.ROLE_USER),
					new Role(ERole.ROLE_ADMIN),
					new Role(ERole.ROLE_MODERATOR)
					)
			);
		};
	}

}
