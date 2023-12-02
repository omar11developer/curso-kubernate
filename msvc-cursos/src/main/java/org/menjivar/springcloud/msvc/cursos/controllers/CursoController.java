package org.menjivar.springcloud.msvc.cursos.controllers;

import org.menjivar.springcloud.msvc.cursos.entity.Curso;
import org.menjivar.springcloud.msvc.cursos.services.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CursoController {
    private final CursoService service;

    public CursoController(CursoService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<?> listar(){
        List<Curso> cursos = service.listar();
        if (cursos.isEmpty()){
          return   ResponseEntity.notFound().build();
        }
       return ResponseEntity.ok(cursos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Curso> oCurso = service.porId(id);
        if (oCurso.isPresent()){
            return ResponseEntity.ok(oCurso.get());
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Curso curso){
        Curso cursoDb = service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Curso curso){
        Optional<Curso> oCurso = service.porId(id);
        if (oCurso.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Curso cursoUpdate = oCurso.get();
        cursoUpdate.setNombre(curso.getNombre());
        service.guardar(cursoUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoUpdate);
    }


    @DeleteMapping
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> oCurso = service.porId(id);
        if (oCurso.isPresent()){
            service.Eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
