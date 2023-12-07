package org.menjivar.springcloud.msvc.cursos.services.contrato;

import org.menjivar.springcloud.msvc.cursos.clients.UsuarioClientRest;
import org.menjivar.springcloud.msvc.cursos.models.Usuario;
import org.menjivar.springcloud.msvc.cursos.models.entity.Curso;
import org.menjivar.springcloud.msvc.cursos.models.entity.CursoUsuario;
import org.menjivar.springcloud.msvc.cursos.repositories.CursoRepository;
import org.menjivar.springcloud.msvc.cursos.services.service.CursoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursosServiceImpl implements CursoService {
    private final CursoRepository repository;

    private final UsuarioClientRest client;

    public CursosServiceImpl(CursoRepository repository, UsuarioClientRest client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void Eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        repository.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> oCurso = repository.findById(cursoId);
        if(oCurso.isPresent()){
            Usuario usuarioMsvc = client.detalle(usuario.getId());

            Curso curso = oCurso.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);

            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> oCurso = repository.findById(cursoId);
        if(oCurso.isPresent()){
            Usuario usuarioNuevoMsvc = client.crearUsuario(usuario);

            Curso curso = oCurso.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);

            repository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> oCurso = repository.findById(cursoId);
        if(oCurso.isPresent()){
            Usuario usuarioMsvc = client.detalle(usuario.getId());

            Curso curso = oCurso.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.removedCursoUsuario(cursoUsuario);

            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = repository.findById(id);
        if(o.isPresent()){
            Curso curso = o.get();
            if(!curso.getCursoUsuarios().isEmpty()){
               // List<Long> ids = curso.getCursoUsuarios().stream().map(cursoUsuario -> cursoUsuario.getId()).collect(Collectors.toList());
                List<Long> ids = curso.getCursoUsuarios().stream().map(CursoUsuario::getId).toList();

                List<Usuario> usuarios = client.listaDeUsuariosPorCursos(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }
}
