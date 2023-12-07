package org.menjivar.springcloud.msvc.usuarios.services.implementacion;

import org.menjivar.springcloud.msvc.usuarios.clients.CursoClientRest;
import org.menjivar.springcloud.msvc.usuarios.models.entity.Usuario;
import org.menjivar.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.menjivar.springcloud.msvc.usuarios.services.service.UsuariosService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class UsuarioServiceImpl implements UsuariosService {

    private final UsuarioRepository repository;
    private final CursoClientRest client;

    public UsuarioServiceImpl(UsuarioRepository repository, CursoClientRest client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
        client.eliminarCursoUsuario(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> ListarPorIds(Iterable<Long> ids) {
        return (List<Usuario>) repository.findAllById(ids);
    }
}
