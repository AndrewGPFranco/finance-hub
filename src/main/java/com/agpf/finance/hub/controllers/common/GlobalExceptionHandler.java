package com.agpf.finance.hub.controllers.common;

import com.agpf.finance.hub.exceptions.BusinessException;
import com.agpf.finance.hub.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Ocorreu um erro inesperado, tente novamente mais tarde.");
        return "redirect:/dashboard";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", exception.getMessage());
        return "redirect:/dashboard";
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", exception.getMessage());
        return "redirect:/dashboard";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public String handleUsernameNotFoundException(UsernameNotFoundException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", exception.getMessage());
        return "redirect:/auth/login";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Existem campos inválidos no formulário.");
        return "redirect:/dashboard";
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String handleBadCredentialsException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Email ou senha inválidos.");
        return "redirect:/auth/login";
    }

    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Autenticação necessária para acessar este recurso.");
        return "redirect:/auth/login";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Você não tem permissão para acessar este recurso.");
        return "redirect:/dashboard";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", exception.getReason());
        return "redirect:/dashboard";
    }
}
