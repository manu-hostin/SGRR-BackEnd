package com.centroweg.sgrr.service;

import com.centroweg.sgrr.dto.UsuarioRequestDTO;
import com.centroweg.sgrr.mapper.UsuarioMapper;
import com.centroweg.sgrr.model.PerfilUsuario;
import com.centroweg.sgrr.model.Usuario;
import com.centroweg.sgrr.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Garante que a senha informada pelo usuario nunca e gravada em texto
 * puro no banco - ela deve sair sempre como hash (BCrypt) do mapper.
 */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void deveSalvarSenhaComoHashENaoEmTextoPuro() {
        UsuarioMapper usuarioMapper = new UsuarioMapper(passwordEncoder);
        UsuarioService usuarioService = new UsuarioService(usuarioRepository, usuarioMapper, passwordEncoder);

        String senhaOriginal = "minhaSenha123";
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Ana", "ana@teste.com", senhaOriginal, PerfilUsuario.USUARIO);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });

        usuarioService.salvar(dto);

        org.mockito.Mockito.verify(usuarioRepository).save(captor.capture());
        String senhaSalva = captor.getValue().getSenha();

        assertNotEquals(senhaOriginal, senhaSalva);
        assertTrue(passwordEncoder.matches(senhaOriginal, senhaSalva));
    }

}
