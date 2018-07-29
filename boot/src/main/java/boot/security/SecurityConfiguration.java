package boot.security;

import static web.common.RequestMappings.ADMIN_CONSOLE_CUSTOMER;
import static web.common.RequestMappings.ADMIN_CONSOLE_DIRECTOR;
import static web.common.RequestMappings.ADMIN_CONSOLE_RECEPTIONIST;
import static web.common.RequestMappings.ADMIN_CONSOLE_STATUTE;
import static web.common.RequestMappings.ADMIN_CONSOLE_USER_ACTIVATION;
import static web.common.RequestMappings.AUTH;
import static web.common.RequestMappings.CUSTOMER_ACTIVATION;
import static web.common.RequestMappings.getAntMatcher;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import query.model.user.UserType;
import web.publicApi.signIn.service.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private CustomUserDetailsService customUserDetailsService;
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    private JwtTokenProvider tokenProvider;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()

                .csrf()
                .disable()

                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                .antMatchers(getAntMatcher(AUTH))
                .permitAll()

                .antMatchers(getAntMatcher(CUSTOMER_ACTIVATION))
                .permitAll()

                .antMatchers(getAntMatcher(ADMIN_CONSOLE_USER_ACTIVATION))
                .hasAuthority(getAuthorityName(UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_CONSOLE_CUSTOMER))
                .hasAuthority(getAuthorityName(UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_CONSOLE_DIRECTOR))
                .hasAuthority(getAuthorityName(UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_CONSOLE_RECEPTIONIST))
                .hasAuthority(getAuthorityName(UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_CONSOLE_STATUTE))
                .hasAuthority(getAuthorityName(UserType.DIRECTOR))

                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    private String getAuthorityName(UserType userType) {
        return userType.name();
    }
}
