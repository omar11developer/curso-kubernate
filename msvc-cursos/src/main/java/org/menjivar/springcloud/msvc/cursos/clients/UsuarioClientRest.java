package org.menjivar.springcloud.msvc.cursos.clients;

import org.menjivar.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "localhost:8001")
public interface UsuarioClientRest {
    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);

    @PostMapping("/")
    Usuario crearUsuario(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> listaDeUsuariosPorCursos(@RequestParam Iterable<Long> ids);

}
