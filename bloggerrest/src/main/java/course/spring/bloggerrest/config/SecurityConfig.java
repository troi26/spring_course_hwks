package course.spring.bloggerrest.config;

import course.spring.bloggerrest.security.RestAuthenticationEntryPoint;
import course.spring.bloggerrest.security.RestSavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestSavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                    .authorizeRequests()
                    .antMatchers("/actuator/info").permitAll()
                    .antMatchers("/actuator/health").permitAll()
                    .antMatchers("/v2/api-docs").permitAll()
                    .antMatchers("/swagger*/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/users").hasRole("Administrator")
                    .antMatchers(HttpMethod.GET, "/api/users/[a-zA-Z0-9]+").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/users").hasRole("Administrator")
                    .antMatchers(HttpMethod.PUT, "/api/users").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/users").authenticated()

                    .antMatchers(HttpMethod.GET, "/api/posts").authenticated()
                    .antMatchers(HttpMethod.GET, "/api/posts/[a-zA-Z0-9]+").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/posts").authenticated()
                    .antMatchers(HttpMethod.PUT, "/api/posts").authenticated()
                    .antMatchers(HttpMethod.DELETE, "/api/posts").authenticated()
                .and()
                    .formLogin()
                    .permitAll()
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and()
                    .logout();

    }

        @Bean
        public UserDetailsService userDetailsService () {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder()
                .username("blogger").password("blogger")
                .roles("Blogger").build());

        manager.createUser(User.withDefaultPasswordEncoder()
                .username("admin").password("admin")
                .roles("Administrator").build());
        return manager;
    }

}
