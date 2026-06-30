package com.centroweg.sgrr.dto;

import com.centroweg.sgrr.model.PerfilUsuario;

public record UsuarioResponseDTO(

        Long id,
        String nome,
        String email,
        PerfilUsuario perfil

) {
}
