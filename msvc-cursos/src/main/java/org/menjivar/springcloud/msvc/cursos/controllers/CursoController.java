package org.menjivar.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import jakarta.validation.Valid;
import org.menjivar.springcloud.msvc.cursos.models.Usuario;
import org.menjivar.springcloud.msvc.cursos.models.entity.Curso;
import org.menjivar.springcloud.msvc.cursos.services.service.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        Optional<Curso> oCurso = service.porIdConUsuarios(id); //service.porId(id);
        if (oCurso.isPresent()){
            return ResponseEntity.ok(oCurso.get());
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Curso curso, BindingResult result){
        if(result.hasErrors()){
           return validar(result);
        }
        Curso cursoDb = service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){
        Optional<Curso> oCurso = service.porId(id);
        if (oCurso.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if(result.hasErrors()){
            return validar(result);
        }
        Curso cursoUpdate = oCurso.get();
        cursoUpdate.setNombre(curso.getNombre());
        service.guardar(cursoUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoUpdate);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> oCurso = service.porId(id);
        if (oCurso.isPresent()){
            service.Eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try {
            o = service.asignarUsuario(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Message", "No existe el usuario por el id o error en la comunicacion: "+ e.getMessage()));
        }

        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try {
            o = service.crearUsuario(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Message", "No se pudo crer el usuario o error en la comunicacion: "+ e.getMessage()));
        }

        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try {
            o = service.eliminarUsuario(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("Message", "No existe el usuario por el id o error en la comunicacion: "+ e.getMessage()));
        }

        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario-cursos/{id}")
    public ResponseEntity<?> eliminarCursoUsuario(@PathVariable Long id){
        service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }


    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> errores.put(err.getField(), "El campo " + err.getField() + " "+ err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }

}
