package com.maputo.maputo.configuration;

import com.maputo.maputo.constant.SecurityConstant;
import com.maputo.maputo.filter.JWTAuthorizationFilter;
import com.maputo.maputo.filter.JwtAccessDeniedHandler;
import com.maputo.maputo.filter.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//security at method level
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private JWTAuthorizationFilter authorizationFilter;
    private JwtAccessDeniedHandler accessDeniedHandler;
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder encoder;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                 JwtAccessDeniedHandler accessDeniedHandler,
                                 JWTAuthorizationFilter authorizationFilter,
                                 @Qualifier("userDetailsService") UserDetailsService userDetailsService,
                                 BCryptPasswordEncoder encoder
                                 ){
        this.userDetailsService=userDetailsService;
        this.accessDeniedHandler=accessDeniedHandler;
        this.authenticationEntryPoint=jwtAuthenticationEntryPoint;
        this.authorizationFilter=authorizationFilter;
        this.encoder=encoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)//telling spring security to use our userdetailsservice
                .passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//disabling cross site request forgery
                .cors().and()//cross origin resource sharing means forbid anyone from other domains access application
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//stateless because I am using JWT
                .and().authorizeRequests()//authorize all requests
                .antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()//allowing pub URLs
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(accessDeniedHandler)//telling to use our accessDeniedHandler
                .authenticationEntryPoint(authenticationEntryPoint)//telling to use our authentciationEntryPoint
                .and().addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
