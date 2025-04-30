import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas de la clase Reglas")
public class ReglasTests {
    @ParameterizedTest
    @Order(1)
    @DisplayName("Obtener símbolo de la pieza")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "p, ♟",
            "P, ♙",
            "t, ♜",
            "T, ♖",
            "c, ♞",
            "C, ♘",
            "a, ♝",
            "A, ♗",
            "d, ♛",
            "D, ♕",
            "r, ♚",
            "R, ♔"
    })
    void obtenerSimboloPieza(char tipo, String simbolo) {
        assertEquals(simbolo, Reglas.simbolo(new Pieza(tipo)));
    }

    @ParameterizedTest
    @Order(2)
    @DisplayName("Transformar coordenadas de ajedrez a índices de matriz")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "a1, 7, 0",
            "h8, 0, 7",
            "e4, 4, 4",
            "b7, 1, 1",
            "g2, 6, 6",
            "d5, 3, 3",
            "h1, 7, 7",
            "a8, 0, 0",
            "c3, 5, 2",
            "f6, 2, 5",
            "b2, 6, 1",
            "g7, 1, 6"
    })
    void transformarCoordenadas(String coordenada, int fila, int columna) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream pantalla = new PrintStream(salida);
        assertEquals(fila, Reglas.filaAIndice(coordenada.substring(1), pantalla), String.format("La fila %c debe corresponderse al índice %d", coordenada.charAt(1), fila));
        assertEquals(columna, Reglas.columnaAIndice(coordenada.substring(0, 1), pantalla), String.format("La columna %c debe corresponderse al índice %d", coordenada.charAt(0), columna));
        assertEquals("", salida.toString(), "Si las coordenadas son correctas, no se debe mostrar ningún mensaje");
    }

    @ParameterizedTest
    @Order(3)
    @DisplayName("Transformar coordenadas de fila de ajedrez a índices de matriz (errores)")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "i, La fila i no tiene el formato correcto (número del 1 al 8)",
            "0, La fila 0 está fuera de rango (número del 1 al 8)",
            "9, La fila 9 está fuera de rango (número del 1 al 8)",
            "a, La fila a no tiene el formato correcto (número del 1 al 8)",
            "h12gh, La fila h12gh no tiene el formato correcto (número del 1 al 8)"
    })
    void transformarCoordenadasFilaError(String fila, String mensaje) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream pantalla = new PrintStream(salida);
        assertEquals(-1, Reglas.filaAIndice(fila, pantalla), "Una coordenada de fila incorrecta debe devolver el índice -1");
        assertEquals(mensaje, salida.toString().trim(), "Si las coordenadas no son correctas, se debe mostrar un mensaje de error");
    }

    @ParameterizedTest
    @Order(4)
    @DisplayName("Transformar coordenadas de columna de ajedrez a índices de matriz (errores)")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "i, La columna i está fuera de rango (letra de 'a' a 'h')",
            "0, La columna 0 no tiene el formato correcto (letra de 'a' a 'h')",
            "9, La columna 9 no tiene el formato correcto (letra de 'a' a 'h')",
            "l, La columna l está fuera de rango (letra de 'a' a 'h')",
            "h12gh, La columna h12gh no tiene el formato correcto (letra de 'a' a 'h')"
    })
    void transformarCoordenadasColumnaError(String columna, String mensaje) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream pantalla = new PrintStream(salida);
        assertEquals(-1, Reglas.columnaAIndice(columna, pantalla), "Una coordenada de columna incorrecta debe devolver el índice -1");
        assertEquals(mensaje, salida.toString().trim(), "Si las coordenadas no son correctas, se debe mostrar un mensaje de error");
    }

    @ParameterizedTest
    @Order(5)
    @DisplayName("Movimientos del peón")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
        // formato: "FEN, movimiento, color, resultado, descripción"
        // Peones: avance una casilla
        "8/8/8/3Pp3/3PP3/4T3/P3C3/8, d5d6, BLANCO, true, Peón blanco: avance una casilla",
        "8/8/8/3Pp3/3PP3/4T3/P3C3/8, a2a3, BLANCO, true, Peón blanco: avance una casilla desde posición inicial",
        "8/8/8/3Pp3/3PP3/4T3/P3C3/8, e4e5, BLANCO, false, Peón blanco: avance con casilla ocupada",
        "8/8/8/3Pp3/3PP3/4T3/P3C3/8, d4d5, BLANCO, false, Peón blanco: avance con casilla ocupada por pieza del mismo color",
        "8/p3c3/4t3/3pp3/3pP3/8/8/8, d4d3, NEGRO, true, Peón negro: avance una casilla",
        "8/p3c3/4t3/3pp3/3pP3/8/8/8, a7a6, NEGRO, true, Peón negro: avance una casilla desde posición inicial",
        "8/p3c3/4t3/3pp3/3pP3/8/8/8, e5e4, NEGRO, false, Peón negro: avance con casilla ocupada",
        "8/p3c3/4t3/3pp3/3pP3/8/8/8, d5d4, NEGRO, false, Peón negro: avance con casilla ocupada por pieza del mismo color",

        // Peones: avance de dos casillas
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, a2a4, BLANCO, true, Peón blanco: avance dos casillas desde posición inicial",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, b2b4, BLANCO, true, Peón blanco: avance dos casillas desde posición inicial",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, c2c4, BLANCO, false, Peón blanco: avance dos casillas con casilla intermedia ocupada",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, e2e4, BLANCO, false, Peón blanco: avance dos casillas con casilla intermedia ocupada",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, e3e5, BLANCO, false, Peón blanco: avance dos casillas desde posición no inicial",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, f4f6, BLANCO, false, Peón blanco: avance dos casillas desde posición no inicial",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, d2d4, BLANCO, false, Peón blanco: avance dos casillas a casilla ocupada",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, f2f4, BLANCO, false, Peón blanco: avance dos casillas a casilla ocupada",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, h7h5, NEGRO, true, Peón negro: avance dos casillas desde posición inicial",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, g7g5, NEGRO, true, Peón negro: avance dos casillas desde posición inicial",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, f7f5, NEGRO, false, Peón negro: avance dos casillas con casilla intermedia ocupada",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, d7d5, NEGRO, false, Peón negro: avance dos casillas con casilla intermedia ocupada",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, d6d4, NEGRO, false, Peón negro: avance dos casillas desde posición no inicial",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, c5c3, NEGRO, false, Peón negro: avance dos casillas desde posición no inicial",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, e7e5, NEGRO, false, Peón negro: avance dos casillas a casilla ocupada",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, c7c5, NEGRO, false, Peón negro: avance dos casillas a casilla ocupada",

        // Peones: capturas
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, b2c3, BLANCO, true, Peón blanco: captura diagonal",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, d2c3, BLANCO, true, Peón blanco: captura diagonal",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, e3d4, BLANCO, true, Peón blanco: captura diagonal",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, d2e3, BLANCO, false, Peón blanco: captura de pieza del mismo color",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, f2e3, BLANCO, false, Peón blanco: captura de pieza del mismo color",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, c2d3, BLANCO, false, Peón blanco: movimiento diagonal sin captura",
        "8/8/8/8/3p1P2/2p1P3/PPPPPP2/8, a2b3, BLANCO, false, Peón blanco: movimiento diagonal sin captura",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, g7f6, NEGRO, true, Peón negro: captura diagonal",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, e7f6, NEGRO, true, Peón negro: captura diagonal",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, d6e5, NEGRO, true, Peón negro: captura diagonal",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, c7d6, NEGRO, false, Peón negro: captura de pieza del mismo color",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, e7d6, NEGRO, false, Peón negro: captura de pieza del mismo color",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, h7g6, NEGRO, false, Peón negro: movimiento diagonal sin captura",
        "8/2pppppp/3p1P2/2p1P3/8/8/8/8, f7g6, NEGRO, false, Peón negro: movimiento diagonal sin captura",

        // Peones: movimientos inválidos
        "P7/8/8/8/8/8/8/8, a8a7, BLANCO, false, Peón blanco: movimiento hacia atrás",
        "P7/8/8/8/8/8/8/8, a8a6, BLANCO, false, Peón blanco: movimiento hacia atrás",
        "P7/8/8/8/8/8/8/8, a8a5, BLANCO, false, Peón blanco: movimiento hacia atrás",
        "P7/8/8/8/8/8/8/8, a8a4, BLANCO, false, Peón blanco: movimiento hacia atrás",
        "P7/8/8/8/8/8/8/8, a8a3, BLANCO, false, Peón blanco: movimiento hacia atrás",
        "P7/8/8/8/8/8/8/8, a8a2, BLANCO, false, Peón blanco: movimiento hacia atrás",
        "P7/8/8/8/8/8/8/8, a8a1, BLANCO, false, Peón blanco: movimiento hacia atrás",
        "P7/8/8/8/8/8/8/8, a8b7, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "P7/8/8/8/8/8/8/8, a8c6, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "P7/8/8/8/8/8/8/8, a8d5, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "P7/8/8/8/8/8/8/8, a8e4, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "P7/8/8/8/8/8/8/8, a8f3, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "P7/8/8/8/8/8/8/8, a8g2, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "P7/8/8/8/8/8/8/8, a8h1, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "P7/8/8/8/8/8/8/8, a8b8, BLANCO, false, Peón blanco: movimiento horizontal",
        "P7/8/8/8/8/8/8/8, a8c8, BLANCO, false, Peón blanco: movimiento horizontal",
        "P7/8/8/8/8/8/8/8, a8d8, BLANCO, false, Peón blanco: movimiento horizontal",
        "P7/8/8/8/8/8/8/8, a8e8, BLANCO, false, Peón blanco: movimiento horizontal",
        "P7/8/8/8/8/8/8/8, a8f8, BLANCO, false, Peón blanco: movimiento horizontal",
        "P7/8/8/8/8/8/8/8, a8g8, BLANCO, false, Peón blanco: movimiento horizontal",
        "P7/8/8/8/8/8/8/8, a8h8, BLANCO, false, Peón blanco: movimiento horizontal",
        "7P/8/8/8/8/8/8/8, h8a8, BLANCO, false, Peón blanco: movimiento horizontal",
        "7P/8/8/8/8/8/8/8, h8b8, BLANCO, false, Peón blanco: movimiento horizontal",
        "7P/8/8/8/8/8/8/8, h8c8, BLANCO, false, Peón blanco: movimiento horizontal",
        "7P/8/8/8/8/8/8/8, h8d8, BLANCO, false, Peón blanco: movimiento horizontal",
        "7P/8/8/8/8/8/8/8, h8e8, BLANCO, false, Peón blanco: movimiento horizontal",
        "7P/8/8/8/8/8/8/8, h8f8, BLANCO, false, Peón blanco: movimiento horizontal",
        "7P/8/8/8/8/8/8/8, h8g8, BLANCO, false, Peón blanco: movimiento horizontal",
        "7P/8/8/8/8/8/8/8, h8g7, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "7P/8/8/8/8/8/8/8, h8f6, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "7P/8/8/8/8/8/8/8, h8e5, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "7P/8/8/8/8/8/8/8, h8d4, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "7P/8/8/8/8/8/8/8, h8c3, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "7P/8/8/8/8/8/8/8, h8b2, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "7P/8/8/8/8/8/8/8, h8a1, BLANCO, false, Peón blanco: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/p7, a1a2, NEGRO, false, Peón negro: movimiento hacia atrás",
        "8/8/8/8/8/8/8/p7, a1a3, NEGRO, false, Peón negro: movimiento hacia atrás",
        "8/8/8/8/8/8/8/p7, a1a4, NEGRO, false, Peón negro: movimiento hacia atrás",
        "8/8/8/8/8/8/8/p7, a1a5, NEGRO, false, Peón negro: movimiento hacia atrás",
        "8/8/8/8/8/8/8/p7, a1a6, NEGRO, false, Peón negro: movimiento hacia atrás",
        "8/8/8/8/8/8/8/p7, a1a7, NEGRO, false, Peón negro: movimiento hacia atrás",
        "8/8/8/8/8/8/8/p7, a1a8, NEGRO, false, Peón negro: movimiento hacia atrás",
        "8/8/8/8/8/8/8/p7, a1b2, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/p7, a1c3, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/p7, a1d4, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/p7, a1e5, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/p7, a1f6, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/p7, a1g7, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/p7, a1h8, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/p7, a1b1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/p7, a1c1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/p7, a1d1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/p7, a1e1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/p7, a1f1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/p7, a1g1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/p7, a1h1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/7p, h1g1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/7p, h1f1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/7p, h1e1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/7p, h1d1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/7p, h1c1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/7p, h1b1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/7p, h1a1, NEGRO, false, Peón negro: movimiento horizontal",
        "8/8/8/8/8/8/8/7p, h1g2, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/7p, h1f3, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/7p, h1e4, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/7p, h1d5, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/7p, h1c6, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/7p, h1b7, NEGRO, false, Peón negro: movimiento diagonal hacia atrás",
        "8/8/8/8/8/8/8/7p, h1a8, NEGRO, false, Peón negro: movimiento diagonal hacia atrás"
    })
    void movimientosDelPeon(String fen, String movimiento, Color color, boolean esperado, String descripcion) {
        // Configurar el tablero con la posición FEN
        validarMovimiento(fen, movimiento, color, esperado, descripcion);
    }

    @ParameterizedTest
    @Order(6)
    @DisplayName("Movimientos de la torre")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            // formato: "FEN, movimiento, color, resultado, descripción"
            // BLANCAS
            "8/8/8/3p4/8/4T3/8/8, e3e7, BLANCO, true, Torre: movimiento vertical largo",
            "8/8/8/3p4/8/4T3/8/8, e3a3, BLANCO, true, Torre: movimiento horizontal largo",
            "8/8/8/3p4/8/4T3/8/8, e3e4, BLANCO, true, Torre: movimiento vertical corto",
            "8/8/8/3p4/8/4T3/8/8, e3d4, BLANCO, false, Torre: movimiento diagonal (inválido)",
            // Movimientos válidos: Verticales y Horizontales
            "8/8/8/8/8/4T3/8/8, e3e8, BLANCO, true, Torre: movimiento vertical hacia arriba sin obstáculos",
            "8/8/8/8/8/4T3/8/8, e3e1, BLANCO, true, Torre: movimiento vertical hacia abajo sin obstáculos",
            "8/8/8/8/8/4T3/8/8, e3h3, BLANCO, true, Torre: movimiento horizontal hacia la derecha sin obstáculos",
            "8/8/8/8/8/4T3/8/8, e3a3, BLANCO, true, Torre: movimiento horizontal hacia la izquierda sin obstáculos",
            // Movimientos válidos con captura de una pieza enemiga
            "8/8/8/3p4/8/4T3/8/8, e3e5, BLANCO, true, Torre: captura vertical de pieza enemiga",
            "8/8/8/8/8/4T2p/8/8, e3g3, BLANCO, true, Torre: captura horizontal de pieza enemiga",
            // Movimientos inválidos (por reglas de la torre)
            "8/8/8/3p4/8/4T3/8/8, e3d4, BLANCO, false, Torre: movimiento diagonal (inválido)",
            "8/8/8/3p4/8/4T3/8/8, e3c5, BLANCO, false, Torre: movimiento en L (inválido, como un caballo)",
            // Movimientos bloqueados por una pieza del mismo color
            "8/8/8/8/8/4T3/8/4A3, e3e1, BLANCO, false, Torre: bloqueada por pieza aliada en el camino",
            "8/8/8/8/8/4T1A1/8/8, e3h3, BLANCO, false, Torre: bloqueada por pieza aliada en el camino",
            // Movimientos bloqueados por una pieza del oponente (sin captura)
            "8/8/8/8/8/4Tp2/8/8, e3g3, BLANCO, false, Torre: movimiento horizontal bloqueado por enemigo",
            "8/8/4t3/8/8/4T3/8/8, e3e8, BLANCO, false, Torre: movimiento vertical bloqueado por enemigo",
            // Movimientos desde otra posición (más variedad)
            "8/8/T7/8/8/8/8/8, a6a1, BLANCO, true, Torre: movimiento largo descendente",
            "8/8/T7/8/8/8/8/8, a6f6, BLANCO, true, Torre: movimiento largo horizontal",
            "8/8/T7/8/8/8/8/8, a6c4, BLANCO, false, Torre: movimiento diagonal inválido",
            // NEGRAS
            // Movimientos básicos de la torre negra
            "8/8/8/3P4/8/4t3/8/8, e3e7, NEGRO, true, Torre negra: movimiento vertical largo",
            "8/8/8/3P4/8/4t3/8/8, e3a3, NEGRO, true, Torre negra: movimiento horizontal largo",
            "8/8/8/3P4/8/4t3/8/8, e3e4, NEGRO, true, Torre negra: movimiento vertical corto",
            "8/8/8/3P4/8/4t3/8/8, e3d4, NEGRO, false, Torre negra: movimiento diagonal (inválido)",

            // Movimientos válidos: Verticales y Horizontales
            "8/8/8/8/8/4t3/8/8, e3e8, NEGRO, true, Torre negra: movimiento vertical hacia arriba sin obstáculos",
            "8/8/8/8/8/4t3/8/8, e3e1, NEGRO, true, Torre negra: movimiento vertical hacia abajo sin obstáculos",
            "8/8/8/8/8/4t3/8/8, e3h3, NEGRO, true, Torre negra: movimiento horizontal hacia la derecha sin obstáculos",
            "8/8/8/8/8/4t3/8/8, e3a3, NEGRO, true, Torre negra: movimiento horizontal hacia la izquierda sin obstáculos",

            // Movimientos válidos con captura de una pieza enemiga
            "8/8/8/3P4/8/4t3/8/8, e3e5, NEGRO, true, Torre negra: captura vertical de pieza enemiga",
            "8/8/8/8/8/4t2P/8/8, e3g3, NEGRO, true, Torre negra: captura horizontal de pieza enemiga",

            // Movimientos inválidos (por reglas de la torre)
            "8/8/8/3P4/8/4t3/8/8, e3d4, NEGRO, false, Torre negra: movimiento diagonal (inválido)",
            "8/8/8/3P4/8/4t3/8/8, e3c5, NEGRO, false, Torre negra: movimiento en L (inválido, como un caballo)",

            // Movimientos bloqueados por una pieza del mismo color
            "8/8/8/8/8/4t3/8/4a3, e3e1, NEGRO, false, Torre negra: bloqueada por pieza aliada en el camino",
            "8/8/8/8/8/4t1a1/8/8, e3h3, NEGRO, false, Torre negra: bloqueada por pieza aliada en el camino",

            // Movimientos bloqueados por una pieza del oponente (sin captura)
            "8/8/8/8/8/4tP2/8/8, e3g3, NEGRO, false, Torre negra: movimiento horizontal bloqueado por enemigo",
            "8/8/4T3/8/8/4t3/8/8, e3e8, NEGRO, false, Torre negra: movimiento vertical bloqueado por enemigo",

            // Movimientos desde otra posición (más variedad)
            "8/8/t7/8/8/8/8/8, a6a1, NEGRO, true, Torre negra: movimiento largo descendente",
            "8/8/t7/8/8/8/8/8, a6f6, NEGRO, true, Torre negra: movimiento largo horizontal",
            "8/8/t7/8/8/8/8/8, a6c4, NEGRO, false, Torre negra: movimiento diagonal inválido"
    })
    void movimientosDeLaTorre(String fen, String movimiento, Color color, boolean esperado, String descripcion) {
        // Configurar el tablero con la posición FEN
        validarMovimiento(fen, movimiento, color, esperado, descripcion);
    }

    @ParameterizedTest
    @Order(7)
    @DisplayName("Movimientos del alfil")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            // formato: "FEN, movimiento, color, resultado, descripción"
            // BLANCAS
            // Movimientos diagonales largos y cortos
            "8/8/8/8/8/4A3/8/8, e3b6, BLANCO, true, Alfil: movimiento diagonal largo hacia arriba izquierda",
            "8/8/8/8/8/4A3/8/8, e3h6, BLANCO, true, Alfil: movimiento diagonal largo hacia arriba derecha",
            "8/8/8/8/8/4A3/8/8, e3b6, BLANCO, true, Alfil: movimiento diagonal largo hacia arriba izquierda",
            "8/8/8/8/8/4A3/8/8, e3d4, BLANCO, true, Alfil: movimiento diagonal corto",

            // Capturas válidas de piezas enemigas
            "8/8/1p6/8/8/4A3/8/8, e3b6, BLANCO, true, Alfil: captura diagonal de pieza enemiga",
            "8/8/8/6p1/8/4A3/8/8, e3g5, BLANCO, true, Alfil: captura diagonal de pieza enemiga",

            // Movimientos inválidos (alfil solo se mueve en diagonal)
            "8/8/8/8/8/4A3/8/8, e3e7, BLANCO, false, Alfil: movimiento vertical (inválido)",
            "8/8/8/8/8/4A3/8/8, e3a3, BLANCO, false, Alfil: movimiento horizontal (inválido)",
            "8/8/8/8/8/4A3/8/8, e3d5, BLANCO, false, Alfil: movimiento en L (como un caballo)",

            // Movimientos bloqueados por una pieza aliada
            "8/8/8/8/3A4/4A3/8/8, e3b6, BLANCO, false, Alfil: bloqueado por pieza aliada en el camino",
            "8/8/8/8/5A2/4A3/8/8, e3g7, BLANCO, false, Alfil: bloqueado por pieza aliada en el camino",

            // Movimientos bloqueados por una pieza enemiga (sin captura)
            "8/8/8/2p5/8/4A3/8/8, e3b6, BLANCO, false, Alfil: movimiento diagonal bloqueado por enemigo sin captura",
            "8/8/8/8/8/4A3/2p5/8, e3g7, BLANCO, false, Alfil: movimiento diagonal bloqueado por enemigo sin captura",

            // NEGRAS
            // Movimientos diagonales largos y cortos
            "8/8/8/8/8/4a3/8/8, e3b6, NEGRO, true, Alfil negro: movimiento diagonal largo hacia arriba izquierda",
            "8/8/8/8/8/4a3/8/8, e3h6, NEGRO, true, Alfil negro: movimiento diagonal largo hacia arriba derecha",
            "8/8/8/8/8/4a3/8/8, e3b6, NEGRO, true, Alfil negro: movimiento diagonal largo hacia arriba izquierda",
            "8/8/8/8/8/4a3/8/8, e3d4, NEGRO, true, Alfil negro: movimiento diagonal corto",

            // Capturas válidas de piezas enemigas
            "a7/8/8/8/8/8/8/7A, a8h1, NEGRO, true, Alfil negro: captura diagonal de pieza enemiga",
            "8/8/4a3/8/2A5/8/8/8, e6c4, NEGRO, true, Alfil negro: captura diagonal de pieza enemiga",

            // Movimientos inválidos (alfil solo se mueve en diagonal)
            "8/8/8/8/8/4a3/8/8, e3e7, NEGRO, false, Alfil negro: movimiento vertical (inválido)",
            "8/8/8/8/8/4a3/8/8, e3a3, NEGRO, false, Alfil negro: movimiento horizontal (inválido)",
            "8/8/8/8/8/4a3/8/8, e3d5, NEGRO, false, Alfil negro: movimiento en L (como un caballo)",

            // Movimientos bloqueados por una pieza aliada
            "8/8/8/8/8/5a2/6t1/8, f3h1, NEGRO, false, Alfil negro: bloqueado por pieza aliada en el camino",
            "8/8/8/8/5a2/4a3/8/8, e3g7, NEGRO, false, Alfil negro: bloqueado por pieza aliada en el camino",
            "8/8/8/8/8/5a2/6t1/8, f3g2, NEGRO, false, Alfil negro: bloqueado por pieza aliada en casilla objetivo",

            // Movimientos bloqueados por una pieza enemiga (sin captura)
            "8/8/8/2P5/8/4a3/8/8, e3b6, NEGRO, false, Alfil negro: movimiento diagonal bloqueado por enemigo sin captura",
            "8/8/8/8/8/4a3/2P5/8, e3g7, NEGRO, false, Alfil negro: movimiento diagonal bloqueado por enemigo sin captura"
    })

    void movimientosDelAlfil(String fen, String movimiento, Color color, boolean esperado, String descripcion) {
        // Configurar el tablero con la posición FEN
        validarMovimiento(fen, movimiento, color, esperado, descripcion);
    }

    @ParameterizedTest
    @Order(8)
    @DisplayName("Movimientos del caballo")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            // Movimientos válidos del caballo blanco
            "8/8/8/8/8/4C3/8/8, e3d5, BLANCO, true, Caballo blanco: movimiento en L hacia arriba izquierda",
            "8/8/8/8/8/4C3/8/8, e3f5, BLANCO, true, Caballo blanco: movimiento en L hacia arriba derecha",
            "8/8/8/8/8/4C3/8/8, e3c4, BLANCO, true, Caballo blanco: movimiento en L hacia izquierda arriba",
            "8/8/8/8/8/4C3/8/8, e3g4, BLANCO, true, Caballo blanco: movimiento en L hacia derecha arriba",
            "8/8/8/8/8/4C3/8/8, e3c2, BLANCO, true, Caballo blanco: movimiento en L hacia izquierda abajo",
            "8/8/8/8/8/4C3/8/8, e3g2, BLANCO, true, Caballo blanco: movimiento en L hacia derecha abajo",
            "8/8/8/8/8/4C3/8/8, e3d1, BLANCO, true, Caballo blanco: movimiento en L hacia abajo izquierda",
            "8/8/8/8/8/4C3/8/8, e3f1, BLANCO, true, Caballo blanco: movimiento en L hacia abajo derecha",

            // Capturas del caballo blanco
            "8/8/8/3p4/8/4C3/8/8, e3d5, BLANCO, true, Caballo blanco: captura de pieza enemiga",
            "8/8/8/8/8/4C3/8/3p4, e3d1, BLANCO, true, Caballo blanco: captura de pieza enemiga",

            // Movimientos inválidos del caballo blanco
            "8/8/8/8/8/4C3/8/8, e3e5, BLANCO, false, Caballo blanco: movimiento vertical inválido",
            "8/8/8/8/8/4C3/8/8, e3a3, BLANCO, false, Caballo blanco: movimiento horizontal inválido",
            "8/8/8/8/8/4C3/8/8, e3c5, BLANCO, false, Caballo blanco: movimiento diagonal inválido",

            // Movimientos válidos del caballo negro
            "8/8/8/8/8/4c3/8/8, e3d5, NEGRO, true, Caballo negro: movimiento en L hacia arriba izquierda",
            "8/8/8/8/8/4c3/8/8, e3f5, NEGRO, true, Caballo negro: movimiento en L hacia arriba derecha",
            "8/8/8/8/8/4c3/8/8, e3c4, NEGRO, true, Caballo negro: movimiento en L hacia izquierda arriba",
            "8/8/8/8/8/4c3/8/8, e3g4, NEGRO, true, Caballo negro: movimiento en L hacia derecha arriba",
            "8/8/8/8/8/4c3/8/8, e3c2, NEGRO, true, Caballo negro: movimiento en L hacia izquierda abajo",
            "8/8/8/8/8/4c3/8/8, e3g2, NEGRO, true, Caballo negro: movimiento en L hacia derecha abajo",
            "8/8/8/8/8/4c3/8/8, e3d1, NEGRO, true, Caballo negro: movimiento en L hacia abajo izquierda",
            "8/8/8/8/8/4c3/8/8, e3f1, NEGRO, true, Caballo negro: movimiento en L hacia abajo derecha",

            // Capturas del caballo negro
            "8/8/8/3P4/8/4c3/8/8, e3d5, NEGRO, true, Caballo negro: captura de pieza enemiga",
            "8/8/8/8/8/4c3/8/3P4, e3d1, NEGRO, true, Caballo negro: captura de pieza enemiga",

            // Movimientos inválidos del caballo negro
            "8/8/8/8/8/4c3/8/8, e3e5, NEGRO, false, Caballo negro: movimiento vertical inválido",
            "8/8/8/8/8/4c3/8/8, e3a3, NEGRO, false, Caballo negro: movimiento horizontal inválido",
            "8/8/8/8/8/4c3/8/8, e3c5, NEGRO, false, Caballo negro: movimiento diagonal inválido",

            // Casilla objetivo bloqueada
            "8/8/8/3P4/8/4C3/8/8, e3d5, BLANCO, false, Caballo blanco: casilla objetivo con pieza de su color",
            "8/8/8/8/8/4C3/8/3P4, e3d1, BLANCO, false, Caballo blanco: casilla objetivo con pieza de su color",
            "8/8/8/3p4/8/4c3/8/8, e3d5, NEGRO, false, Caballo negro: casilla objetivo con pieza de su color",
            "8/8/8/8/8/4c3/8/3p4, e3d1, NEGRO, false, Caballo negro: casilla objetivo con pieza de su color",
    })
    void movimientosDelCaballo(String fen, String movimiento, Color color, boolean esperado, String descripcion) {
        validarMovimiento(fen, movimiento, color, esperado, descripcion);
    }

    @ParameterizedTest
    @Order(9)
    @DisplayName("Movimientos de la reina")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            // Posición inicial: e3
            "8/8/8/8/8/4D3/8/8, e3e8, BLANCO, true, Dama blanca: movimiento vertical hacia arriba sin obstáculos desde e3",
            "8/8/8/8/8/4D3/8/8, e3e1, BLANCO, true, Dama blanca: movimiento vertical hacia abajo sin obstáculos desde e3",
            "8/8/8/8/8/4D3/8/8, e3h3, BLANCO, true, Dama blanca: movimiento horizontal hacia la derecha desde e3",
            "8/8/8/8/8/4D3/8/8, e3a3, BLANCO, true, Dama blanca: movimiento horizontal hacia la izquierda desde e3",

            // Posición inicial: d5
            "8/8/8/3D4/8/8/8/8, d5h5, BLANCO, true, Dama blanca: movimiento horizontal hacia la derecha desde d5",
            "8/8/8/3D4/8/8/8/8, d5a5, BLANCO, true, Dama blanca: movimiento horizontal hacia la izquierda desde d5",
            "8/8/8/3D4/8/8/8/8, d5d8, BLANCO, true, Dama blanca: movimiento vertical hacia arriba desde d5",
            "8/8/8/3D4/8/8/8/8, d5d1, BLANCO, true, Dama blanca: movimiento vertical hacia abajo desde d5",
            "8/8/8/3D4/8/8/8/8, d5h1, BLANCO, true, Dama blanca: movimiento diagonal larga hacia abajo-derecha desde d5",
            "8/8/8/3D4/8/8/8/8, d5a2, BLANCO, true, Dama blanca: movimiento diagonal larga hacia abajo-izquierda desde d5",

            // Posición inicial: a1 (Esquina)
            "8/8/8/8/8/8/8/D7, a1h1, BLANCO, true, Dama blanca: movimiento horizontal extremo desde esquina a1",
            "8/8/8/8/8/8/8/D7, a1a8, BLANCO, true, Dama blanca: movimiento vertical extremo desde esquina a1",
            "8/8/8/8/8/8/8/D7, a1h8, BLANCO, true, Dama blanca: movimiento diagonal larga desde esquina a1",

            // Posición inicial: g6
            "8/8/6D1/8/8/8/8/8, g6g1, BLANCO, true, Dama blanca: movimiento vertical descendente desde g6",
            "8/8/6D1/8/8/8/8/8, g6g8, BLANCO, true, Dama blanca: movimiento vertical ascendente desde g6",
            "8/8/6D1/8/8/8/8/8, g6h7, BLANCO, true, Dama blanca: movimiento diagonal corta desde g6",

            // Posición inicial: b7
            "8/1D6/8/8/8/8/8/8, b7b1, BLANCO, true, Dama blanca: movimiento vertical descendente desde b7",
            "8/1D6/8/8/8/8/8/8, b7h7, BLANCO, true, Dama blanca: movimiento horizontal largo desde b7",

            // Posición inicial: c4 (capturas)
            "8/8/2p5/8/2D5/8/8/8, c4c6, BLANCO, true, Dama blanca: captura vertical de pieza enemiga desde c4",
            "6p1/8/8/8/2D5/8/8/8, c4g8, BLANCO, true, Dama blanca: captura diagonal de pieza enemiga desde c4",

            // Dama negra (`d`) - Equivalentes
            "8/8/8/8/8/4d3/8/8, e3e8, NEGRO, true, Dama negra: movimiento vertical hacia arriba sin obstáculos desde e3",
            "8/8/8/8/8/4d3/8/8, e3e1, NEGRO, true, Dama negra: movimiento vertical hacia abajo sin obstáculos desde e3",
            "8/8/8/8/8/4d3/8/8, e3h3, NEGRO, true, Dama negra: movimiento horizontal hacia la derecha desde e3",
            "8/8/8/8/8/4d3/8/8, e3a3, NEGRO, true, Dama negra: movimiento horizontal hacia la izquierda desde e3",
            "8/8/8/3d4/8/8/8/8, d5h5, NEGRO, true, Dama negra: movimiento horizontal hacia la derecha desde d5",
            "8/8/8/3d4/8/8/8/8, d5a5, NEGRO, true, Dama negra: movimiento horizontal hacia la izquierda desde d5",
            "8/8/8/3d4/8/8/8/8, d5h1, NEGRO, true, Dama negra: movimiento diagonal larga hacia abajo-derecha desde d5",
            "8/8/8/3d4/8/8/8/8, d5a2, NEGRO, true, Dama negra: movimiento diagonal larga hacia abajo-izquierda desde d5",
            "8/8/8/8/8/8/8/d7, a1h1, NEGRO, true, Dama negra: movimiento horizontal extremo desde esquina a1",
            "8/8/8/8/8/8/8/d7, a1a8, NEGRO, true, Dama negra: movimiento vertical extremo desde esquina a1",
            "8/8/8/8/8/8/8/d7, a1h8, NEGRO, true, Dama negra: movimiento diagonal larga desde esquina a1",
            "8/8/6d1/8/8/8/8/8, g6g1, NEGRO, true, Dama negra: movimiento vertical descendente desde g6",
            "8/8/6d1/8/8/8/8/8, g6g8, NEGRO, true, Dama negra: movimiento vertical ascendente desde g6",
            "8/8/6d1/8/8/8/8/8, g6h7, NEGRO, true, Dama negra: movimiento diagonal corta desde g6",
            "8/8/8/8/2D4d/8/8/8 , h4c4, NEGRO, true, Dama blanca: captura horizontal de pieza enemiga",
            "8/4D3/8/8/8/d7/8/8, a3e7, NEGRO, true, Dama blanca: captura diagonal de pieza enemiga",
    })
    void movimientosDeLaReina(String fen, String movimiento, Color color, boolean esperado, String descripcion) {
        validarMovimiento(fen, movimiento, color, esperado, descripcion);
    }

    @ParameterizedTest
    @Order(10)
    @DisplayName("Movimientos del rey")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            // Movimientos básicos del rey
            "8/8/8/8/8/3R4/8/8, d3c2, BLANCO, true, Rey blanco: movimiento diagonal",
            "8/8/8/8/8/3R4/8/8, d3e3, BLANCO, true, Rey blanco: movimiento horizontal",
            "8/8/8/8/8/3R4/8/8, d3d4, BLANCO, true, Rey blanco: movimiento vertical",
            "8/8/1r6/8/8/8/8/8, b6a5, NEGRO, true, Rey negro: movimiento diagonal",
            "8/8/1r6/8/8/8/8/8, b6a6, NEGRO, true, Rey negro: movimiento horizontal",
            "8/8/1r6/8/8/8/8/8, b6b7, NEGRO, true, Rey negro: movimiento vertical",

            // Movimiento inválido del rey
            "8/8/8/8/8/3R4/8/8, d3d7, BLANCO, false, Rey blanco: movimiento de más de una casilla",
            "8/8/8/8/8/3R4/8/8, d3a1, BLANCO, false, Rey blanco: movimiento de más de una casilla",
            "8/8/8/8/8/3R4/8/8, d3d5, BLANCO, false, Rey blanco: movimiento de más de una casilla",
            "8/8/8/8/8/3R4/8/8, d3a3, BLANCO, false, Rey blanco: movimiento de más de una casilla",

            // Captura de pieza enemiga
            "8/8/8/8/8/3Rp3/8/8, d3e3, BLANCO, true, Rey blanco: captura horizontal de pieza enemiga",
            "8/8/8/8/8/3Rp3/8/8, d3c3, BLANCO, true, Rey blanco: captura horizontal de pieza enemiga",
            "8/8/8/8/8/3Rp3/8/8, d3d4, BLANCO, true, Rey blanco: captura vertical de pieza enemiga",

            // Movimientos bloqueados
            "8/8/8/8/8/3RA3/8/8, d3e3, BLANCO, false, Rey blanco: bloqueado por pieza aliada",
            "8/8/8/8/3a4/3r4/8/8, d3d4, NEGRO, false, Rey: bloqueado por pieza aliada",
    })
    void movimientosDelRey(String fen, String movimiento, Color color, boolean esperado, String descripcion) {
        validarMovimiento(fen, movimiento, color, esperado, descripcion);
    }


    private static void validarMovimiento(String fen, String movimiento, Color color, boolean esperado, String descripcion) {
        // Configurar el tablero con la posición FEN
        Tablero tablero = new Tablero(fen);

        // Parsear el movimiento
        int filaOrigen = Reglas.filaAIndice(movimiento.substring(1, 2), new PrintStream(OutputStream.nullOutputStream()));
        int columnaOrigen = Reglas.columnaAIndice(movimiento.substring(0, 1), new PrintStream(OutputStream.nullOutputStream()));
        int filaDestino = Reglas.filaAIndice(movimiento.substring(3, 4), new PrintStream(OutputStream.nullOutputStream()));
        int columnaDestino = Reglas.columnaAIndice(movimiento.substring(2, 3), new PrintStream(OutputStream.nullOutputStream()));

        // Obtener la pieza y validar el movimiento
        Pieza pieza = tablero.getPieza(filaOrigen, columnaOrigen);
        assertNotNull(pieza, "Debe haber una pieza en la posición de origen");
        assertEquals(color, pieza.getColor(), "La pieza debe ser del color especificado");

        // Verificar el resultado del movimiento
        boolean resultado = Reglas.movimientoValidoPieza(tablero, new Movimiento(pieza, filaOrigen, columnaOrigen, filaDestino, columnaDestino));
        assertEquals(esperado, resultado, descripcion);
    }

    @ParameterizedTest
    @Order(11)
    @DisplayName("Final de partida")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            // formato: "FEN, resultado, descripción"
            // Partida en jaque mate
            "8/8/8/8/8/8/8/8, true, BLANCO, Tablero vacío debe ser final de partida",
            "8/8/8/8/8/8/8/8, true, NEGRO, Tablero vacío debe ser final de partida",
            "8/8/3T4/ppp5/r/8/8/8, true, BLANCO, Blancas no tienen rey: debería ser final de partida",
            "8/8/3T4/ppp5/r/8/8/8, false, NEGRO, Negras tienen rey: no debería ser final de partida",
            "8/8/3T4/ppp5/R/8/8/8, false, BLANCO, Blancas tienen rey: no debería ser final de partida",
            "8/8/3T4/ppp5/R/8/8/8, true, NEGRO, Negras no tienen rey: debería ser final de partida"
    })
    void finalDePartida(String fen, boolean resultado, Color color, String descripcion) {
        Tablero tablero = new Tablero(fen);
        assertEquals(resultado, Reglas.finalDePartida(tablero, color));
    }

    @Test
    @Order(12)
    @DisplayName("Pedir movimiento")
    @ExtendWith(SinSalidaDeSistema.class)
    void pedirMovimiento() {
        Tablero tablero = new Tablero("tcadract/pppppppp/8/8/8/8/PPPPPPPP/TCADRACT");

        // Entrada simulada del usuario
        String entrada = """
e2e5
adsf12
j4
j4e4
e4e5
e2e4
""";

        // Crear un stream para capturar la salida
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        // Crear un scanner con la entrada simulada
        Scanner scanner = new Scanner(entrada);

        // Ejecutar el método
        Movimiento resultado = Reglas.solicitarMovimiento(tablero, Color.BLANCO, ps, scanner);
        // Verificar mensajes
        assertTrue(baos.toString().contains("Movimiento no válido."), "Debe mostrar mensaje de movimiento no válido");
        assertTrue(baos.toString().contains("No hay una pieza del color correcto en la posición de origen."), "Debe mostrar mensaje de pieza incorrecta");
        assertTrue(baos.toString().contains("Movimiento fuera de los límites del tablero."), "Debe mostrar mensaje de movimiento fuera de los límites");
        assertTrue(baos.toString().contains("Formato de movimiento incorrecto. Debe ser de 4 caracteres (ej. e2e4)."), "Debe mostrar mensaje de formato incorrecto");

        // Verificar movimiento válido
        assertEquals(new Movimiento(new Pieza('P'), 6, 4, 4, 4), resultado, "Debe ser el movimiento e2e4");
    }

    @Test
    @Order(13)
    @DisplayName("Transformar índice de columna a coordenada")
    void indiceAColumna() {
        // Probar cada columna
        for (int i = 0; i < 8; i++) {
            String resultado = Reglas.indiceAColumna(i);
            assertEquals(String.valueOf((char) ('a' + i)), resultado, "La columna " + i + " debe ser " + (char) ('a' + i));
        }

        // Verificar excepción para índice fuera de rango y mensaje correcto
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            Reglas.indiceAColumna(-1);
        });
        assertEquals("Índice de columna fuera de rango", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> {
            Reglas.indiceAColumna(8);
        });
        assertEquals("Índice de columna fuera de rango", e.getMessage());
    }

    @Test
    @Order(14)
    @DisplayName("Transformar indice de fila a coordenada")
    void indiceAFila() {
        // Probar cada fila
        for (int i = 0; i < 8; i++) {
            String resultado = Reglas.indiceAFila(i);
            assertEquals(String.valueOf(8 - i), resultado, "La fila " + i + " debe ser " + (8 - i));
        }

        // Verificar excepción para índice fuera de rango y mensaje correcto
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            Reglas.indiceAFila(-1);
        });
        assertEquals("Índice de fila fuera de rango", e.getMessage());
        e = assertThrows(IllegalArgumentException.class, () -> {
            Reglas.indiceAFila(8);
        });
        assertEquals("Índice de fila fuera de rango", e.getMessage());
    }
}
