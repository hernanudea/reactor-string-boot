package dev.velasquez.reactor;

import dev.velasquez.reactor.models.Comentarios;
import dev.velasquez.reactor.models.Usuario;
import dev.velasquez.reactor.models.UsuarioConComentarios;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class ReactorStringBootApplication implements CommandLineRunner {


    private static final String PREFIJO = "Nombre: {}";
    private static final String ERROR_MESSAGE = "Nombres o pueden ser vacio";
    private static final String FINALIZADO = "Ha finalizado la ejecución del observable con exito";

    public static void main(String[] args) {
        SpringApplication.run(ReactorStringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        useFluxAndSubscribe();
//        useMapOperator();
//        useFilterOperator();
//        desdeList();
//        flatMapOperator();
//        ejemploToString();
//        ejemploToCollectList();
//        ejemploUsuarioComentarioFlatMap();
//        ejemploZipWithByCodeium();
//        ejemploZipWith();
        // ejemploZipWithRange();
        //showTable(5);
//        ejemploInterval();
        ejemploDelayElements();


    }


    public void useFluxAndSubscribe() {
        Flux<String> nombre = Flux.just("Samuel", "Matias", "Eliana", "Hernan")
                .doOnNext(e -> {
                    if (e.isEmpty()) {
                        throw new RuntimeException(ERROR_MESSAGE);
                    }
                    log.info(PREFIJO, e);
                });

        // subscribe() puede llevar 1, 2 ó 3 argumentos
        nombre.subscribe(e -> log.info(PREFIJO, e),
                error -> log.info(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info(FINALIZADO);
                    }
                });
    }

    public void useMapOperator() {
        Flux<Usuario> nombres = Flux.just("Samuel", "Matias", "Eliana", "Hernan")
                .map(nombre -> new Usuario(nombre.toUpperCase(), null))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException(ERROR_MESSAGE);
                    }
                    log.info(PREFIJO, usuario.toString());
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        // subscribe() puede llevar 1, 2 ó 3 argumentos
        nombres.subscribe(e -> log.info(PREFIJO, e.getNombre()),
                error -> log.info(error.getMessage()),
                () -> log.info(FINALIZADO));
    }

    public void useFilterOperator() {
        Flux<Usuario> nombres = Flux.just("Samuel Velasquez", "Matias Velasquez", "Eliana Cuadros", "Hernan Velasquez", "Bruce Lee", "Bruce Willis", "Nala Velasquez")
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(u -> u.getApellido() != null)
                .filter(u -> u.getApellido().equalsIgnoreCase("Velasquez"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException(ERROR_MESSAGE);
                    }
                    log.info(PREFIJO, usuario.toString().concat(":").concat(usuario.getApellido()));
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        nombres.subscribe(e -> log.info(PREFIJO, e.getNombre()),
                error -> log.info(error.getMessage()),
                () -> log.info(FINALIZADO));
    }

    public void desdeList() {
      /*  las bases de datos revuelan la lista, podriamos convertirla en un Flux
        convertirlo en un flux y
      */
        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("Samuel Velasquez");
        usuariosList.add("Matias Velasquez");
        usuariosList.add("Eliana Cuadros");
        usuariosList.add("Hernan Velasquez");
        usuariosList.add("Nala Velasquez");

        Flux<String> nombres = Flux.fromIterable(usuariosList);
        Flux<Usuario> usuarios = nombres.map(
                        nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .filter(u -> u.getApellido() != null)
                .filter(u -> u.getApellido().equalsIgnoreCase("Velasquez"))
                .doOnNext(usuario -> {
                    if (usuario == null) {
                        throw new RuntimeException(ERROR_MESSAGE);
                    }
                    log.info(PREFIJO, usuario.toString().concat(":").concat(usuario.getApellido()));
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                });

        usuarios.subscribe(e -> log.info(String.valueOf(e)));

    }

    public void flatMapOperator() {

        List<String> usuariosList = new ArrayList<>();
        usuariosList.add("Samuel Velasquez");
        usuariosList.add("Matias Velasquez");
        usuariosList.add("Eliana Cuadros");
        usuariosList.add("Hernan Velasquez");
        usuariosList.add("Nala Velasquez");

        Flux.fromIterable(usuariosList)
                .map(nombre -> new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase()))
                .flatMap(user -> {
                    if (user.getApellido().equalsIgnoreCase("Velasquez")) {
                        return Mono.just(user);
                    }
                    return Mono.empty();
                })
                .map(usuario -> {
                    String nombre = usuario.getNombre().toLowerCase();
                    usuario.setNombre(nombre);
                    return usuario;
                }).subscribe(u -> log.info(PREFIJO, u.toString()));
    }

    public void ejemploToString() {
        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("Samuel", "Velasquez"));
        usuariosList.add(new Usuario("Matias", "Velasquez"));
        usuariosList.add(new Usuario("Eliana", "Cuadros"));
        usuariosList.add(new Usuario("Hernan", "Velasquez"));
        usuariosList.add(new Usuario("Nala", "Velasquez"));


        Flux.fromIterable(usuariosList)
                .map(usuario -> usuario.getNombre().toUpperCase().concat(" ").concat(usuario.getApellido().toUpperCase()))
                .flatMap(nombre -> {
                    if (nombre.contains("Velasquez".toUpperCase())) {
                        return Mono.just(nombre);
                    }
                    return Mono.empty();
                })
                .map(String::toLowerCase).subscribe(u -> log.info(PREFIJO, u));
    }

    private void ejemploToCollectList() {
        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("Samuel", "Velasquez"));
        usuariosList.add(new Usuario("Matias", "Velasquez"));
        usuariosList.add(new Usuario("Eliana", "Cuadros"));
        usuariosList.add(new Usuario("Hernan", "Velasquez"));
        usuariosList.add(new Usuario("Nala", "Velasquez"));

        // convertimos la lista completa en un Flux
        Flux.fromIterable(usuariosList)
                .collectList()
                .subscribe(lista -> lista.forEach(item -> log.info(item.toString())));
    }

    public void ejemploUsuarioComentarioFlatMap() { // corregir
        Mono<Usuario> usuarioMono = Mono.just(new Usuario("Samuel", "Velasquez"));
        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentario("Comentario1");
            comentarios.addComentario("Comentario2");
            comentarios.addComentario("Comentario3");
            return comentarios;
        });

        usuarioMono.flatMap(u -> comentariosUsuarioMono.map(c -> new UsuarioConComentarios(u, c)))
                .subscribe(uc -> log.info(uc.toString()));
    }

    //    Operador zipWith, recibe dos flujos y los combina
    public void ejemploZipWithByCodeium() {
        List<Usuario> usuariosList = new ArrayList<>();
        usuariosList.add(new Usuario("Samuel", "Velasquez"));
        usuariosList.add(new Usuario("Matias", "Velasquez"));
        usuariosList.add(new Usuario("Eliana", "Cuadros"));
        usuariosList.add(new Usuario("Hernan", "Velasquez"));
        usuariosList.add(new Usuario("Nala", "Velasquez"));

        Flux<Usuario> usuariosFlux = Flux.fromIterable(usuariosList);
        Flux<Integer> edadesFlux = Flux.range(20, 5);

        usuariosFlux.zipWith(edadesFlux)
                .subscribe(tuple -> {
                    Usuario usuario = tuple.getT1();
                    Integer edad = tuple.getT2();
                    log.info("Usuario: " + usuario + ", Edad: " + edad);
                });
    }

    public void ejemploZipWith() { // corregir
        Mono<Usuario> usuarioMono = Mono.just(new Usuario("Samuel", "Velasquez"));
        Mono<Comentarios> comentariosUsuarioMono = Mono.fromCallable(() -> {
            Comentarios comentarios = new Comentarios();
            comentarios.addComentario("Comentario1");
            comentarios.addComentario("Comentario2");
            comentarios.addComentario("Comentario3");
            return comentarios;
        });

        Mono<UsuarioConComentarios> usuarioConComentariosMono = usuarioMono.zipWith(comentariosUsuarioMono, (usuario, comentarioUsuario) ->
                new UsuarioConComentarios(usuario, comentarioUsuario));

        usuarioConComentariosMono.subscribe(uc -> log.info(uc.toString()));

    }

    public void ejemploZipWithRange() {
        Flux<Integer> rangos = Flux.range(1, 6);
        Flux.just(1, 2, 3, 4, 5)
                .map(i -> i * 3)
                .zipWith(rangos, (uno, dos) ->
                        String.format("Primer flux: %d, Segundo flux: %d", uno, dos))
                .subscribe(log::info);

    }

    public void showTable(int num) {
        Flux<Integer> numbers = Flux.range(1, 10);
        Flux<Integer> product = numbers.map(i -> i * num);
        product.zipWith(numbers, (num1, num2) -> String.format("%d x %d = %d", num, num2, num1))
                .subscribe(log::info);
    }

    public void ejemploInterval() {
        Flux<Integer> rango = Flux.range(1, 12);
        Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));

        rango.zipWith(retraso, (ra, re) -> ra)
                .doOnNext(i -> log.info(i.toString()))
                //.subscribe();
                .blockLast();

        /* la programación reactiva es no bloqueante
        si uso .subscribe(); no se bloquea, termina main y continua en optro hilo la ejecución
        si uso .blockLast(); se bloquea hasta que termine la secuencia, permite verla, no util en codigo real, solo para el ejemplo
         */
    }

    public void ejemploDelayElements() {
        Flux<Integer> rango = Flux.range(1, 12)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(i -> log.info(i.toString()));

//        rango.subscribe();
        rango.blockLast();
//        de esta manera lo genera en diferentes hilos
    }
}
