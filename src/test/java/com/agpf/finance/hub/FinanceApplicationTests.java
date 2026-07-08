package com.agpf.finance.hub;

import com.agpf.finance.hub.dtos.auth.RegisterRequestDTO;
import com.agpf.finance.hub.enums.user.UserRoleType;
import com.agpf.finance.hub.repositories.user.UserRepository;
import com.agpf.finance.hub.services.auth.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:finance;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.docker.compose.enabled=false"
})
class FinanceApplicationTests {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void registersUserWithDefaultRole() {
		authService.register(new RegisterRequestDTO(
				"usuario@finance.test",
				"usuariofinance",
				"Usuário",
				"Finance",
				"senha12345"
		));

		var savedUser = userRepository.findByEmail("usuario@finance.test").orElseThrow();

		assertThat(savedUser.getId()).isNotNull();
		assertThat(savedUser.getRole()).isEqualTo(UserRoleType.USER);
	}
}
