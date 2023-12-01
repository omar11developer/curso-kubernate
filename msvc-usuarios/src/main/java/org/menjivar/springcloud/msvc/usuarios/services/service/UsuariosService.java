package org.menjivar.springcloud.msvc.usuarios.services.service;

import org.menjivar.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuariosService {
    List<Usuario> listar();
    Optional<Usuario> findById(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
}
