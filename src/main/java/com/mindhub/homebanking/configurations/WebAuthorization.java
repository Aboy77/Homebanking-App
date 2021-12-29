package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity

@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/index.html", "/style.css", "/log.js", "/img/**", "/api/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients", "/api/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/current/account", "/api/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.DELETE, "/api/clients/current/cards/**").hasAuthority("CLIENT")
                .antMatchers("/manager.html", "/manager.js", "/rest/**", "/api/clients").hasAuthority("ADMIN")
                .antMatchers("/web/**", "/api/**", "/pdf/transactions/download").hasAuthority("CLIENT");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout");

        http.sessionManagement();

        http.csrf().disable();

        http.headers().frameOptions().disable();

        http.exceptionHandling().authenticationEntryPoint((req,res,exp) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        http.formLogin().successHandler((req,res, auth) -> clearAuthenticationAttributes(req));

        http.formLogin().failureHandler((req,res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
