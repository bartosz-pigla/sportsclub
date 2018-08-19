package boot.security;

import static query.model.user.UserType.getNames;
import static web.common.RequestMappings.ADMIN_API_BOOKING;
import static web.common.RequestMappings.ADMIN_API_BOOKING_BY_ID;
import static web.common.RequestMappings.ADMIN_API_BOOKING_CONFIRM;
import static web.common.RequestMappings.ADMIN_API_BOOKING_DETAIL;
import static web.common.RequestMappings.ADMIN_API_BOOKING_DETAIL_BY_ID;
import static web.common.RequestMappings.ADMIN_API_BOOKING_FINISH;
import static web.common.RequestMappings.ADMIN_API_BOOKING_REJECT;
import static web.common.RequestMappings.ADMIN_API_CUSTOMER;
import static web.common.RequestMappings.ADMIN_API_DIRECTOR;
import static web.common.RequestMappings.ADMIN_API_RECEPTIONIST;
import static web.common.RequestMappings.ADMIN_API_SPORT_OBJECT;
import static web.common.RequestMappings.ADMIN_API_SPORT_OBJECT_POSITION;
import static web.common.RequestMappings.ADMIN_API_SPORT_OBJECT_POSITION_BY_NAME;
import static web.common.RequestMappings.ADMIN_API_STATUTE;
import static web.common.RequestMappings.ADMIN_API_USER_ACTIVATION;
import static web.common.RequestMappings.AUTH;
import static web.common.RequestMappings.CUSTOMER_ACTIVATION;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_BY_ID;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_CANCEL;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_DETAIL;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_DETAIL_BY_ID;
import static web.common.RequestMappings.CUSTOMER_API_BOOKING_SUBMIT;
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

                .antMatchers(getAntMatcher(ADMIN_API_USER_ACTIVATION))
                .hasAuthority(UserType.DIRECTOR.name())

                .antMatchers(getAntMatcher(ADMIN_API_CUSTOMER))
                .hasAuthority(UserType.DIRECTOR.name())

                .antMatchers(getAntMatcher(ADMIN_API_DIRECTOR))
                .hasAuthority(UserType.DIRECTOR.name())

                .antMatchers(getAntMatcher(ADMIN_API_RECEPTIONIST))
                .hasAuthority(UserType.DIRECTOR.name())

                .antMatchers(getAntMatcher(ADMIN_API_STATUTE))
                .hasAuthority(UserType.DIRECTOR.name())

                .antMatchers(getAntMatcher(ADMIN_API_SPORT_OBJECT))
                .hasAuthority(UserType.DIRECTOR.name())

                .antMatchers(getAntMatcher(ADMIN_API_SPORT_OBJECT_POSITION))
                .hasAuthority(UserType.DIRECTOR.name())

                .antMatchers(getAntMatcher(ADMIN_API_SPORT_OBJECT_POSITION_BY_NAME))
                .hasAuthority(UserType.DIRECTOR.name())

                .antMatchers(getAntMatcher(ADMIN_API_BOOKING))
                .hasAnyAuthority(getNames(UserType.RECEPTIONIST, UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_API_BOOKING_BY_ID))
                .hasAnyAuthority(getNames(UserType.RECEPTIONIST, UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_API_BOOKING_CONFIRM))
                .hasAnyAuthority(getNames(UserType.RECEPTIONIST, UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_API_BOOKING_REJECT))
                .hasAnyAuthority(getNames(UserType.RECEPTIONIST, UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_API_BOOKING_FINISH))
                .hasAnyAuthority(getNames(UserType.RECEPTIONIST, UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_API_BOOKING_DETAIL))
                .hasAnyAuthority(getNames(UserType.RECEPTIONIST, UserType.DIRECTOR))

                .antMatchers(getAntMatcher(ADMIN_API_BOOKING_DETAIL_BY_ID))
                .hasAnyAuthority(getNames(UserType.RECEPTIONIST, UserType.DIRECTOR))

                .antMatchers(getAntMatcher(CUSTOMER_API_BOOKING))
                .hasAuthority(UserType.CUSTOMER.name())

                .antMatchers(getAntMatcher(CUSTOMER_API_BOOKING_BY_ID))
                .hasAuthority(UserType.CUSTOMER.name())

                .antMatchers(getAntMatcher(CUSTOMER_API_BOOKING_SUBMIT))
                .hasAuthority(UserType.CUSTOMER.name())

                .antMatchers(getAntMatcher(CUSTOMER_API_BOOKING_CANCEL))
                .hasAuthority(UserType.CUSTOMER.name())

                .antMatchers(getAntMatcher(CUSTOMER_API_BOOKING_DETAIL))
                .hasAuthority(UserType.CUSTOMER.name())

                .antMatchers(getAntMatcher(CUSTOMER_API_BOOKING_DETAIL_BY_ID))
                .hasAuthority(UserType.CUSTOMER.name())

                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
