package ru.tinkoff.fintechspringrest.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tinkoff.fintechspringrest.service.UserService;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/students/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/students/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/students/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/students/**").hasRole("ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/courses/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/courses/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/courses/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/courses/**").hasRole("ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
                .and()
                .csrf().disable()
                .formLogin().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }
}
