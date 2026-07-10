package com.agpf.finance.hub.utils;

import com.agpf.finance.hub.models.user.User;
import com.agpf.finance.hub.services.auth.AuthenticatedUser;
import org.springframework.security.core.Authentication;

import java.util.Objects;

public class UserUtils {

    private UserUtils() {}

    public static User getUser(Authentication authentication) {
        var principal = (AuthenticatedUser) Objects.requireNonNull(authentication.getPrincipal());
        return principal.user();
    }

}
