package org.menjivar.springcloud.msvc.usuarios.controllers;

import org.menjivar.springcloud.msvc.usuarios.models.entity.Usuario;
import org.menjivar.springcloud.msvc.usuarios.services.service.UsuariosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario){
        Usuario oUsuario = service.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(oUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Usuario usuario){
        Optional<Usuario> oUsuario = service.findById(id);
        if (oUsuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe el usuario que deseas editar");
        }
        Usuario usuarioUpdate = oUsuario.get();
        usuarioUpdate.setNombre(usuario.getNombre());
        usuarioUpdate.setEmail(usuario.getEmail());
        usuarioUpdate.setPassword(usuario.getPassword());
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
}
