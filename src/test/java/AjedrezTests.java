import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AjedrezTests {
    static Stream<Arguments> provideInvalidArgumentCounts() {
        return Stream.of(
                Arguments.of(new String[]{}, "Se ha ejecutado la aplicación sin argumentos: debe mostrar mensaje de error y finalizar"),
                Arguments.of(new String[]{"8/8/8/8/8/8/8/8", "BLANCO"}, "Se ha ejecutado la aplicación con argumentos insuficientes: debe mostrar mensaje de error y finalizar"),
                Arguments.of(new String[]{"8/8/8/8/8/8/8/8", "BLANCO", "partida.txt", "extra"}, "Se ha ejecutado la aplicación con argumentos de más: debe mostrar mensaje de error y finalizar")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidArgumentCounts")
    void testNumeroIncorrectoDeArgumentos(String[] args, String escenario) throws Exception {
        String output = captureSystemOut(() -> Ajedrez.main(new String[]{}));

        assertTrue(output.contains("Número incorrecto de argumentos"));
    }

    @Test
    void testArgumentoTurnoInvalido() {
        String output = captureSystemOut(() -> Ajedrez.main(new String[]{"8/8/8/8/8/8/8/8", "AZUL", "partida.txt"}));
        assertTrue(output.contains("El argumento TURNO debe ser BLANCO o NEGRO"));
    }

    @Test
    void testErrorAlCrearTablero() {
        String output = captureSystemOut(() -> Ajedrez.main(new String[]{"TABLERO_INVALIDO", "BLANCO", "partida.txt"}));

        assertTrue(output.contains("Error al crear el tablero"));
    }

    @Test
    void testModoReproduccion() {
        String output = captureSystemOut(() -> Ajedrez.main(new String[]{"movimientos.txt"}));

        assertTrue(output.contains("Tablero inicial:"));
    }

    // Para capturar la salida por consola sin SystemLambda
    private String captureSystemOut(Runnable action) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            action.run();
        } finally {
            System.setOut(originalOut);
        }

        return outContent.toString();
    }

    @Test
    @DisplayName("Modo jugar")
    @ExtendWith(SinSalidaDeSistema.class)
    void testModoJugar() {
        String contenido = """
                e2e4
                d7d5
                e4d5
                e7e6
                c2c4
                d8g5
                b2b4
                g5e5
                g1e2
                g8f6
                c1a3
                f6g4
                d1a4
                a7a6
                a4e8
                """;
        try {
            Path archivo = Files.createTempFile("registro", "txt");
            // Crear un stream para capturar la salida
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            Ajedrez.jugar(new Tablero("tcadract/pppppppp/8/8/8/8/PPPPPPPP/TCADRACT"), Color.BLANCO, ps, new Scanner(contenido), archivo.toString());
            assertTrue(baos.toString().contains("Fin de la partida. Ganan las blancas"), "La partida debería terminar con victoria de las blancas");
            assertTrue(Files.exists(archivo), "El archivo de registro debería existir");
            assertEquals(contenido, Files.readString(archivo), "El contenido del archivo de registro debería ser el esperado");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Modo reproducir")
    @ExtendWith(SinSalidaDeSistema.class)
    void testModoReproducir() {
        String contenido = """
                e2e4
                d7d5
                e4d5
                e7e6
                c2c4
                d8g5
                b2b4
                g5e5
                g1e2
                g8f6
                c1a3
                f6g4
                d1a4
                a7a6
                a4e8
                """;
        try {
            Path archivo = Files.createTempFile("movimientos", "txt");
            Files.writeString(archivo, contenido);
            // Crear un stream para capturar la salida
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            Ajedrez.reproducir(archivo.toString(), ps, new Scanner(String.valueOf('\n').repeat(14)));
            assertTrue(baos.toString().contains("Fin de la partida. Ganan las blancas"), "La partida debería terminar con victoria de las blancas");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
