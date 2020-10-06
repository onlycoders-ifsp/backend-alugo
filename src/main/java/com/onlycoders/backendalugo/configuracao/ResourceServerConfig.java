package com.onlycoders.backendalugo.configuracao;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/usuarios/cadastro","/produtos","/usuarios/produto","/usuarios", "/produtos/usuario").permitAll()
                .antMatchers("/usuarios/**", "/produtos/**")
                .authenticated()
            .anyRequest().denyAll();
    }
}
