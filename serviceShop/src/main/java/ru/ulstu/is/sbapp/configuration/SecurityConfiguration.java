package ru.ulstu.is.sbapp.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ru.ulstu.is.sbapp.controller.UserSignupMvcController;
import ru.ulstu.is.sbapp.model.UserRole;
import ru.ulstu.is.sbapp.service.UserService;

import static ru.ulstu.is.sbapp.model.UserRole.ADMIN;
import static ru.ulstu.is.sbapp.model.UserRole.CUSTOMER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    private static final String LOGIN_URL = "/login";

    private final UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
        createAdminOnStartup();
    }

    private void createAdminOnStartup() {
        final String admin = "admin";
        if (userService.findByLogin(admin) == null) {
            log.info("Admin user successfully created");
            userService.createUser(admin, admin, admin, admin, admin, ADMIN);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin().and()
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(UserSignupMvcController.SIGNUP_URL).permitAll()
                .antMatchers(HttpMethod.GET, LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.GET, "/orders").authenticated()
                .antMatchers(HttpMethod.GET, "/requests").authenticated()
                .antMatchers(HttpMethod.GET, "/types").authenticated()
                .antMatchers("/orders/**").hasRole(ADMIN.name())
                .antMatchers("/requests/**").hasAnyRole(ADMIN.name(), CUSTOMER.name())
                .antMatchers("/types/**").hasRole(ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .and()
                .logout().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/js/**")
                .antMatchers("/templates/**")
                .antMatchers("/webjars/**");
    }
}
