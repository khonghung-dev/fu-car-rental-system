package vn.edu.fpt.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import vn.edu.fpt.auth.service.AuthService;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(AuthService authService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(authService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider authenticationProvider) throws Exception {
        http
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/login", "/access-denied").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("accountName")
                        .passwordParameter("password")
                        .authenticationDetailsSource(request -> {
                            String accountName = request.getParameter("accountName");
                            String password = request.getParameter("password");
                            if (accountName == null || accountName.isBlank() || accountName.length() > 100
                                    || password == null || password.isEmpty()) {
                                throw new BadCredentialsException("Tên tài khoản hoặc mật khẩu không hợp lệ.");
                            }
                            return new WebAuthenticationDetails(request);
                        })
                        .successHandler((request, response, authentication) -> {
                            request.getSession().removeAttribute("loginAccountName");
                            String destination;
                            if (authentication.getAuthorities().stream()
                                    .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))) {
                                destination = "/admin/home";
                            } else if (authentication.getAuthorities().stream()
                                    .anyMatch(authority -> "ROLE_CUSTOMER".equals(authority.getAuthority()))) {
                                destination = "/customer/home";
                            } else {
                                throw new AccessDeniedException("Bạn không có quyền truy cập.");
                            }
                            response.sendRedirect(request.getContextPath() + destination);
                        })
                        .failureHandler((request, response, exception) -> {
                            request.getSession().removeAttribute("loginAccountName");
                            String accountName = request.getParameter("accountName");
                            if (accountName != null && !accountName.isBlank() && accountName.length() <= 100) {
                                request.getSession().setAttribute("loginAccountName", accountName);
                            }
                            response.sendRedirect(request.getContextPath() + "/login?error");
                        })
                        .permitAll())
                .exceptionHandling(exceptions -> exceptions.accessDeniedPage("/access-denied"))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }
}
