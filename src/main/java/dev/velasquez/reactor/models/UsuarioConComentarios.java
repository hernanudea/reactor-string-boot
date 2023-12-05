package dev.velasquez.reactor.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UsuarioConComentarios {
    private Usuario usuario;
    private Comentarios comentarios;

    @Override
    public String toString() {
        return "UsuarioConComentarios{" +
                "usuario=" + usuario +
                ", comentarios=" + comentarios +
                '}';
    }
}
