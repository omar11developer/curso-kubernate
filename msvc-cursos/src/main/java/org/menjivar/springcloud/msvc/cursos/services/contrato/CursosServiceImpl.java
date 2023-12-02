package org.menjivar.springcloud.msvc.cursos.services.contrato;

import org.menjivar.springcloud.msvc.cursos.entity.Curso;
import org.menjivar.springcloud.msvc.cursos.repositories.CursoRepository;
import org.menjivar.springcloud.msvc.cursos.services.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class CursosServiceImpl implements CursoService {
    private final CursoRepository repository;

    public CursosServiceImpl(CursoRepository repository) {
        this.repository = repository;
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
}
