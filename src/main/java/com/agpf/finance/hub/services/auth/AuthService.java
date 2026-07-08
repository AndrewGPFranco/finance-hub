package com.agpf.finance.hub.services.auth;

import com.agpf.finance.hub.dtos.auth.RegisterRequestDTO;
import com.agpf.finance.hub.enums.user.UserRoleType;
import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.agpf.finance.hub.utils.DateUtils.getLocalDateTimeAmericaSP;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final UserRoleType DEFAULT_ROLE = UserRoleType.USER;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequestDTO request) {
        if (this.userRepository.existsByEmail(request.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado.");

        if (this.userRepository.existsByUsername(request.username()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nome de usuário já cadastrado.");

        var now = getLocalDateTimeAmericaSP();

        this.userRepository.save(User.builder()
                .email(request.email()).username(request.username()).firstName(request.firstName())
                .lastName(request.lastName()).passwordHash(this.passwordEncoder.encode(request.password()))
                .role(DEFAULT_ROLE).createdAt(now).updatedAt(now).build());
    }
}
