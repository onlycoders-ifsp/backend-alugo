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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
        RetornaLogin retornaLogin;

        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();

        String loginType = request.getParameter("login_type");

        System.out.println(loginType.toLowerCase());
        if (loginType.equalsIgnoreCase("username") || loginType.equalsIgnoreCase("email")){
            retornaLogin = repository.verificaLogin(login.toLowerCase(), loginType.toLowerCase());
        }
        else {
            throw new UsernameNotFoundException("Parametro 'login_type' incorreto");
        }

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
