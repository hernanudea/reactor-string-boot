package dev.velasquez.reactor.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Comentarios {
    private List<String> comentarios;

    public void addComentario(String comentario) {
        this.comentarios.add(comentario);
    }

    @Override
    public String toString() {
        return "Comentarios=" + comentarios;
    }
}
