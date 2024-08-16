package com.evaluacion.usuario.service;

import com.evaluacion.usuario.model.Usuario;
import com.evaluacion.usuario.repository.UsuarioRepositorio;
import com.evaluacion.usuario.seguridad.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UsuarioServicio
{
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public Usuario crearUsuario(Usuario usuario)
    {
        if (usuarioRepositorio.findByCorreo(usuario.getCorreo()).isPresent())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya registrado");
        }
        // Validación de correo y contraseña

        // Generar token JWT
        String token = jwtTokenProvider.crearToken(usuario.getCorreo());

        usuario.setToken(token);
        usuario.setCreado(LocalDateTime.now());
        usuario.setModificado(LocalDateTime.now());
        usuario.setUltimoIngreso(LocalDateTime.now());
        usuario.setEstaActivo(true);

        return usuarioRepositorio.save(usuario);
    }
}