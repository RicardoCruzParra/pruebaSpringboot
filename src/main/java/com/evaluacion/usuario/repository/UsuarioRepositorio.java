package com.evaluacion.usuario.repository;

import com.evaluacion.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepositorio extends JpaRepository<Usuario, UUID>
{
    Optional<Usuario> findByCorreo(String correo);
}
