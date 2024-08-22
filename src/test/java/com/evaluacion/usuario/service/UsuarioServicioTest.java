package com.evaluacion.usuario.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.evaluacion.usuario.model.Usuario;
import com.evaluacion.usuario.repository.UsuarioRepositorio;
import com.evaluacion.usuario.seguridad.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

public class UsuarioServicioTest
{
    @InjectMocks
    private UsuarioServicio usuarioServicio;

    @Mock
    private UsuarioRepositorio usuarioRepositorio;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearUsuarioExitoso()
    {
        // Datos de entrada
        Usuario usuario = new Usuario();
        usuario.setCorreo("juan@rodriguez.org");
        usuario.setNombre("Juan Rodriguez");
        usuario.setContraseña("hunter2");

        // Mocks
        when(usuarioRepositorio.findByCorreo(anyString())).thenReturn(Optional.empty());
        when(jwtTokenProvider.crearToken(anyString())).thenReturn("mocked-token");
        when(usuarioRepositorio.save(any(Usuario.class))).thenReturn(usuario);

        // Ejecución
        Usuario usuarioCreado = usuarioServicio.crearUsuario(usuario);

        // Verificaciones
        assertNotNull(usuario, "El usuario creado no debería ser null");
        assertEquals(usuario.getId(), usuarioCreado.getId(), "El ID del usuario debería coincidir");
        assertEquals("mocked-token", usuarioCreado.getToken());
        assertTrue(usuarioCreado.isEstaActivo());
        verify(usuarioRepositorio, times(1)).save(usuario);
    }

    @Test
    public void testCrearUsuarioCorreoYaRegistrado()
    {
        // Datos de entrada
        Usuario usuario = new Usuario();
        usuario.setCorreo("juan@rodriguez.org");

        // Mocks
        when(usuarioRepositorio.findByCorreo(anyString())).thenReturn(Optional.of(usuario));

        // Ejecución y verificación de excepción
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioServicio.crearUsuario(usuario);
        });

        assertEquals("El correo ya registrado", exception.getReason());
    }

    @Test
    void testObtenerUsuarioPorId() {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(usuario));

        Usuario usuarioObtenido = usuarioServicio.obtenerUsuarioPorId(id);

        assertNotNull(usuarioObtenido);
        assertEquals(id, usuarioObtenido.getId());
        verify(usuarioRepositorio, times(1)).findById(id);
    }
}

