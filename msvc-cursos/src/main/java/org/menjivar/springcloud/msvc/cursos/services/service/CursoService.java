package org.menjivar.springcloud.msvc.cursos.services.service;

import org.menjivar.springcloud.msvc.cursos.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);

    Curso guardar(Curso curso);

    void Eliminar(Long id);

}
