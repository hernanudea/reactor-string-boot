package dev.velasquez.reactor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@Slf4j
@SpringBootApplication
public class ReactorStringBootApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ReactorStringBootApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Flux<String> nombre = Flux.just("Samuel", "Matias", "Eliana", "Hernan")
                .doOnNext(e -> {
                    if (e.isEmpty()) {
                        throw new RuntimeException("Nombres o pueden ser vacio");
                    }
                    log.info("Nombre: {}", e);
                });

        // subscribe() puede llevar 1, 2 ó 3 argumentos
        nombre.subscribe(e -> log.info("Nombre: {}", e),
                error -> log.info(error.getMessage()),
                new Runnable() {
                    @Override
                    public void run() {
                        log.info("Ha finalizado la ejecución del observable con exito");
                    }
                });
    }
}
