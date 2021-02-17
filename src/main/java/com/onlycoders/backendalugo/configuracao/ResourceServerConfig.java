package com.onlycoders.backendalugo.configuracao;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

import java.time.Duration;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .antMatchers("/usuarios/usuario-logado").permitAll()
                .antMatchers("/usuarios/**").not().hasAuthority("ROLE_ADMIN")
                .antMatchers("/produtos/**").not().hasAuthority("ROLE_ADMIN")
                .antMatchers("/aluguel/avaliacao/retorna/**").permitAll()
                .antMatchers("/aluguel/notificacoes").permitAll()
                .antMatchers("/aluguel/**").hasAuthority("ROLE_USER")
                .antMatchers("/pagamento/retorno-pagamento").permitAll()
                .anyRequest().permitAll()
        .and()
                .headers()
                .cacheControl().disable()
                .contentTypeOptions().disable()
                .httpStrictTransportSecurity().includeSubDomains(true).maxAgeInSeconds(Duration.ofDays(365).toMinutes()/60)
                .and()
                .frameOptions().sameOrigin()
                .xssProtection().disable()
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)
                .and().contentSecurityPolicy("script-src 'self' https://api.mercadopago.com https://viacep.com.br");
    }
}
