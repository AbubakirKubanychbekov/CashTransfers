package cashtransfer.cashtransfers.controller;

import cashtransfer.cashtransfers.entities.User;
import cashtransfer.cashtransfers.services.CashRegisterService;
import cashtransfer.cashtransfers.services.UserService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Abubakir Dev
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@PermitAll
public class AuthenticationApi {

    private final UserService userService;
    private final CashRegisterService cashRegisterService;

    @GetMapping
    public String redirectToSignIn() {
        return "redirect:/auth/signin";
    }

    @GetMapping("/signin")
    public String signInPage() {
        return "signIn";
    }

    @GetMapping("/signup")
    public String signUpPage() {
        return "signUp";
    }

    @PostMapping("/signin")
    public String signIn(@RequestParam String email, @RequestParam String password, Model model) {
        boolean isAuthenticated = userService.authenticate(email, password);
        if (isAuthenticated) {
            return "redirect:/cash_registers";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "signIn";
        }
    }

//    @PostMapping("/signup")
//    public String signUp(@RequestParam String email,
//                         @RequestParam String password,
//                         RedirectAttributes redirectAttributes) {
//        try {
//            // Регистрируем пользователя
//            userService.register(email, password);
//
//            // Получаем информацию о новом пользователе
//            User newUser = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
//
//            // Создаем новый кассовый регистр для нового пользователя, если его еще нет
//            if (!cashRegisterService.existsByUser(newUser)) {
//            }
//
//            return "redirect:/cash_registers";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
//            return "redirect:/auth/signup";
//        }
//    }

    @PostMapping("/signup")
    public String signUp(@RequestParam String email,
                         @RequestParam String password,
                         RedirectAttributes redirectAttributes) {
        try {
            // Регистрируем пользователя
            userService.register(email, password);

            // Получаем информацию о новом пользователе
            User newUser = userService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            // Устанавливаем пользователя в контекст аутентификации
            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, null, newUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // После успешной регистрации делаем редирект на защищенную страницу
            return "redirect:/cash_registers";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/auth/signup";
        }
    }
}