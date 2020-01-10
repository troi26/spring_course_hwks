package course.spring.bloggerclient.config;

import course.spring.bloggerclient.domain.UsersService;
import course.spring.bloggerclient.exception.NonexisitngEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
//                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/info").permitAll()
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger*/**").permitAll()
                .antMatchers(HttpMethod.GET, "/posts/**", "/users/**").authenticated()
                .antMatchers(HttpMethod.POST, "/**").hasAnyRole("BLOGGER", "ADMIN")
                .antMatchers(HttpMethod.PUT).hasAnyRole("BLOGGER", "ADMIN")
                .antMatchers(HttpMethod.DELETE).hasAnyRole("BLOGGER", "ADMIN")
                .and()
                .formLogin()
//                .loginPage("/login")
                .permitAll()
//                .usernameParameter("email")
//                .successHandler(authenticationSuccessHandler)
//                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutUrl("/logout");
    }

    @Bean
    public UserDetailsService userDetailsService(UsersService usersService) {

        return email -> {
            try {
                String name = usersService.findByEmail(email).getName();
                return usersService.findByEmail(email);
            } catch (NonexisitngEntityException ex) {
                throw new UsernameNotFoundException(ex.getMessage(), ex);
            }
        };

    }

}
