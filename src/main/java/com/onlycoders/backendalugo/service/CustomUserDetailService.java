package com.onlycoders.backendalugo.service;

import com.onlycoders.backendalugo.model.entity.login.RetornaLogin;
import com.onlycoders.backendalugo.model.entity.login.UsuarioLogin;
import com.onlycoders.backendalugo.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UsuarioRepository repository;

    @Autowired
    public CustomUserDetailService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        RetornaLogin retornaLogin = repository.verificaLogin(login);

        UsuarioLogin user = new UsuarioLogin();
        user.setIdUsuario(retornaLogin.getId_Usuario());
        user.setLogin(retornaLogin.getLogin());
        user.setPassword(retornaLogin.getPassword());
        user.setAdmin(retornaLogin.getAdmin());
        user.setAtivo(retornaLogin.getAtivo());

        Optional.of(user)
                .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado"));
        return User
                .builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.isAdmin() ? "ADMIN" : "USER")
                .disabled(!user.isAtivo())
                .build();
        }
}
