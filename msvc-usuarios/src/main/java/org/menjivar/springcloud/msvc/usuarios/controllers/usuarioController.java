package org.menjivar.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import org.menjivar.springcloud.msvc.usuarios.models.entity.Usuario;
import org.menjivar.springcloud.msvc.usuarios.services.service.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class usuarioController {

    private final UsuariosService service;

    public usuarioController(UsuariosService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<?> listar(){
        List<Usuario> usuarios = service.listar();
        if(usuarios.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron usuarios");
        }
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Usuario> oUsuario = service.findById(id);
        if(oUsuario.isPresent()){
            return ResponseEntity.ok(oUsuario.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Usuario usuario, BindingResult result){
        if(result.hasErrors()){
            return validar(result);
        }
        if(service.buscarPorEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje","Ya existe un usuario con este correo"));
        }
        Usuario oUsuario = service.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(oUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){
        Optional<Usuario> oUsuario = service.findById(id);
        if (oUsuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el usuario que deseas editar");
        }
        if(result.hasErrors()){
            return validar(result);
        }
        Usuario usuarioUpdate = oUsuario.get();
        usuarioUpdate.setNombre(usuario.getNombre());
        usuarioUpdate.setEmail(usuario.getEmail());
        usuarioUpdate.setPassword(usuario.getPassword());
        if( !usuario.getEmail().equalsIgnoreCase(usuarioUpdate.getEmail()) && service.buscarPorEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje","Ya existe un usuario con este correo"));
        }
        service.guardar(usuarioUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Usuario> oUsuario = service.findById(id);
        if (oUsuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el usuario que deseas editar");
        }
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCursos(@RequestParam List<Long> ids){
        List<Usuario> usuarios = service.ListarPorIds(ids);
        if(usuarios.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron usuarios");
        }
        return ResponseEntity.ok(usuarios);
    }



    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> errores.put(err.getField(), "El campo " + err.getField() + " "+ err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }


}
