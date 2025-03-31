import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas de la clase Tablero")
public class TableroTests {
    @Test
    @Order(1)
    @DisplayName("Creación de tablero vacío")
    @ExtendWith(SinSalidaDeSistema.class)
    public void testCrearTableroVacio() {
        Tablero t = new Tablero("8/8/8/8/8/8/8/8");
        assertEquals(8, t.getNumFilas(), "El tablero debería tener 8 filas");
        assertEquals(8, t.getNumColumnas(), "El tablero debería tener 8 columnas");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                assertNull(t.getPieza(i, j), "Todas las casillas deberían estar vacías al crear un tablero vacío");
            }
        }
    }

    @Test
    @Order(2)
    @DisplayName("Creación de tablero con piezas")
    @ExtendWith(SinSalidaDeSistema.class)
    public void testCrearTableroConPiezas() {
        String t1 = "7r/8/8/8/8/8/4P3/4R3";
        String t2 = "t3r2t/8/8/8/8/8/8/T3R2T";
        String t3 = "1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T";
        Tablero t = new Tablero(t1);
        assertEquals(8, t.getNumFilas(), "El tablero debería tener 8 filas");
        assertEquals(8, t.getNumColumnas(), "El tablero debería tener 8 columnas");
        assertEquals(new Pieza('r'), t.getPieza(0, 7), "La casilla (0, 7) debería contener una torre negra para el tablero 7r/8/8/8/8/8/4P3/4R3");
        assertEquals(new Pieza('P'), t.getPieza(6, 4), "La casilla (6, 4) debería contener un peón blanco para el tablero 7r/8/8/8/8/8/4P3/4R3");
        assertEquals(new Pieza('R'), t.getPieza(7, 4), "La casilla (7, 4) debería contener al rey blanco para el tablero 7r/8/8/8/8/8/4P3/4R3");
        // El resto de las casillas deberían estar vacías
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i == 0 && j == 7) || (i == 6 && j == 4) || (i == 7 && j == 4)) {
                    continue;
                }
                assertNull(t.getPieza(i, j), String.format("La casilla (%d, %d) debería estar vacía para el tablero 7r/8/8/8/8/8/4P3/4R3", i, j));
            }
        }

        t = new Tablero(t2);
        assertEquals(8, t.getNumFilas(), "El tablero debería tener 8 filas");
        assertEquals(8, t.getNumColumnas(), "El tablero debería tener 8 columnas");
        assertEquals(new Pieza('t'), t.getPieza(0, 0), "La casilla (0, 0) debería contener una torre negra para el tablero t3r2t/8/8/8/8/8/8/8/T3R2T");
        assertEquals(new Pieza('T'), t.getPieza(7, 0), "La casilla (7, 0) debería contener una torre blanca para el tablero t3r2t/8/8/8/8/8/8/8/T3R2T");
        assertEquals(new Pieza('t'), t.getPieza(0, 7), "La casilla (0, 7) debería contener una torre negra para el tablero t3r2t/8/8/8/8/8/8/8/T3R2T");
        assertEquals(new Pieza('T'), t.getPieza(7, 7), "La casilla (7, 7) debería contener una torre blanca para el tablero t3r2t/8/8/8/8/8/8/8/T3R2T");
        assertEquals(new Pieza('r'), t.getPieza(0, 4), "La casilla (0, 4) debería contener al rey negro para el tablero t3r2t/8/8/8/8/8/8/8/T3R2T");
        assertEquals(new Pieza('R'), t.getPieza(7, 4), "La casilla (7, 4) debería contener al rey blanco para el tablero t3r2t/8/8/8/8/8/8/8/T3R2T");
        // El resto de las casillas deberían estar vacías
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i == 0 && j == 0) || (i == 7 && j == 0) || (i == 0 && j == 7) || (i == 7 && j == 7) || (i == 0 && j == 4) || (i == 7 && j == 4)) {
                    continue;
                }
                assertNull(t.getPieza(i, j), String.format("La casilla (%d, %d) debería estar vacía para el tablero t3r2t/8/8/8/8/8/8/8/T3R2T", i, j));
            }
        }

        t = new Tablero(t3);
        assertEquals(8, t.getNumFilas(), "El tablero debería tener 8 filas");
        assertEquals(8, t.getNumColumnas(), "El tablero debería tener 8 columnas");
        assertEquals(new Pieza('t'), t.getPieza(0, 1), "La casilla (0, 1) debería contener una torre negra para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        assertEquals(new Pieza('a'), t.getPieza(1, 3), "La casilla (1, 3) debería contener un alfil negro para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        assertEquals(new Pieza('c'), t.getPieza(2, 6), "La casilla (2, 6) debería contener un caballo negro para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        assertEquals(new Pieza('P'), t.getPieza(3, 2), "La casilla (3, 2) debería contener un peón blanco para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        assertEquals(new Pieza('R'), t.getPieza(3, 5), "La casilla (3, 5) debería contener al rey blanco para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        assertEquals(new Pieza('d'), t.getPieza(4, 5), "La casilla (4, 5) debería contener la reina negra para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        assertEquals(new Pieza('A'), t.getPieza(5, 7), "La casilla (5, 7) debería contener un alfil blanco para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        assertEquals(new Pieza('C'), t.getPieza(6, 4), "La casilla (6, 4) debería contener un caballo blanco para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        assertEquals(new Pieza('r'), t.getPieza(7, 0), "La casilla (7, 0) debería contener al rey negro para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        assertEquals(new Pieza('T'), t.getPieza(7, 7), "La casilla (7, 7) debería contener una torre blanca para el tablero 1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
    }

    @ParameterizedTest
    @Order(3)
    @DisplayName("Agregar una pieza fuera de rango debería lanzar una excepción")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "12, 7",
            "3, 20",
            "-1, 4",
            "5, -12",
            "10, 10",
            "-10, -10"
    })
    public void testAgregarPiezaFueraDeRango(ArgumentsAccessor arguments) {
        Tablero t = new Tablero("8/8/8/8/8/8/8/8");
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> t.agregarPieza(arguments.getInteger(0), arguments.getInteger(1), new Pieza('R')));
        assertEquals(String.format("La casilla (%d, %d) está fuera de rango%n", arguments.getInteger(0), arguments.getInteger(1)), e.getMessage(),
                String.format("Agregar una pieza a la casilla (%d, %d) debería lanzar una excepción", arguments.getInteger(0), arguments.getInteger(1)));
    }

    @ParameterizedTest
    @Order(4)
    @DisplayName("Agregar una pieza a una casilla ocupada debería lanzar una excepción")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "0, 0, 0, 0",
            "7, 7, 7, 7",
            "3, 4, 3, 4",
            "5, 2, 5, 2"
    })
    public void testAgregarPiezaCasillaOcupada(ArgumentsAccessor arguments) {
        Tablero t = new Tablero("8/8/8/8/8/8/8/8");
        Pieza p = new Pieza('R');
        t.agregarPieza(arguments.getInteger(0), arguments.getInteger(1), p);
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> t.agregarPieza(arguments.getInteger(2), arguments.getInteger(3), new Pieza('r')));
        assertEquals(String.format("La casilla (%d, %d) está ocupada por la pieza %s%n", arguments.getInteger(2), arguments.getInteger(3), Reglas.simbolo(p)), e.getMessage(),
                "Agregar una pieza a una casilla ocupada debería lanzar una excepción");
    }

    @ParameterizedTest
    @Order(5)
    @DisplayName("Agregar una pieza a una casilla vacía debería funcionar")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "0, 0, R",
            "7, 7, r",
            "3, 4, P",
            "5, 2, p"
    })
    public void testAgregarPiezaCasillaVacia(ArgumentsAccessor arguments) {
        Tablero t = new Tablero("8/8/8/8/8/8/8/8");
        Pieza p = new Pieza(arguments.getCharacter(2));
        t.agregarPieza(arguments.getInteger(0), arguments.getInteger(1), p);
        assertEquals(p, t.getPieza(arguments.getInteger(0), arguments.getInteger(1)),
                String.format("La casilla (%d, %d) debería contener la pieza %s", arguments.getInteger(0), arguments.getInteger(1), Reglas.simbolo(p)));
    }

    @ParameterizedTest
    @Order(6)
    @DisplayName("Obtener una pieza fuera de rango debería lanzar una excepción")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "12, 7",
            "3, 20",
            "-1, 4",
            "5, -12",
            "10, 10",
            "-10, -10"
    })
    public void testGetPiezaFueraDeRango(ArgumentsAccessor arguments) {
        Tablero t = new Tablero("8/8/8/8/8/8/8/8");
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> t.getPieza(arguments.getInteger(0), arguments.getInteger(1)));
        assertEquals(String.format("La casilla (%d, %d) está fuera de rango%n", arguments.getInteger(0), arguments.getInteger(1)), e.getMessage(),
                String.format("Obtener la pieza de la casilla (%d, %d) debería lanzar una excepción", arguments.getInteger(0), arguments.getInteger(1)));
    }

    @ParameterizedTest
    @Order(7)
    @DisplayName("Obtener una pieza de una casilla vacía debería devolver null")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "0, 0",
            "7, 7",
            "3, 4",
            "5, 2"
    })
    public void testGetPiezaCasillaVacia(ArgumentsAccessor arguments) {
        Tablero t = new Tablero("8/8/8/8/8/8/8/8");
        assertNull(t.getPieza(arguments.getInteger(0), arguments.getInteger(1)),
                String.format("La casilla (%d, %d) debería estar vacía", arguments.getInteger(0), arguments.getInteger(1)));
    }

    @ParameterizedTest
    @Order(8)
    @DisplayName("Obtener una pieza de una casilla ocupada debería devolver la pieza correcta")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "0, 0, R",
            "7, 7, r",
            "3, 4, P",
            "5, 2, p"
    })
    public void testGetPiezaCasillaOcupada(ArgumentsAccessor arguments) {
        Tablero t = new Tablero("8/8/8/8/8/8/8/8");
        Pieza p = new Pieza(arguments.getCharacter(2));
        t.agregarPieza(arguments.getInteger(0), arguments.getInteger(1), p);
        assertEquals(p, t.getPieza(arguments.getInteger(0), arguments.getInteger(1)),
                String.format("La casilla (%d, %d) debería contener la pieza %s", arguments.getInteger(0), arguments.getInteger(1), Reglas.simbolo(p)));
    }

    @Test
    @Order(9)
    @DisplayName("Representación del tablero")
    @ExtendWith(SinSalidaDeSistema.class)
    public void testToString() {
        Tablero t = new Tablero("tcadract/pppppppp/8/8/8/8/PPPPPPPP/TCADRACT");
        String esperado = """
  a b c d e f g h
 ┌─┬─┬─┬─┬─┬─┬─┬─┐
8│♜│♞│♝│♛│♚│♝│♞│♜│8
 ├─┼─┼─┼─┼─┼─┼─┼─┤
7│♟│♟│♟│♟│♟│♟│♟│♟│7
 ├─┼─┼─┼─┼─┼─┼─┼─┤
6│ │ │ │ │ │ │ │ │6
 ├─┼─┼─┼─┼─┼─┼─┼─┤
5│ │ │ │ │ │ │ │ │5
 ├─┼─┼─┼─┼─┼─┼─┼─┤
4│ │ │ │ │ │ │ │ │4
 ├─┼─┼─┼─┼─┼─┼─┼─┤
3│ │ │ │ │ │ │ │ │3
 ├─┼─┼─┼─┼─┼─┼─┼─┤
2│♙│♙│♙│♙│♙│♙│♙│♙│2
 ├─┼─┼─┼─┼─┼─┼─┼─┤
1│♖│♘│♗│♕│♔│♗│♘│♖│1
 └─┴─┴─┴─┴─┴─┴─┴─┘
  a b c d e f g h
""";
        assertEquals(esperado, t.toString(), "La representación del tablero debería ser la esperada");

        t = new Tablero("8/8/8/8/8/8/8/8");
        esperado = """
  a b c d e f g h
 ┌─┬─┬─┬─┬─┬─┬─┬─┐
8│ │ │ │ │ │ │ │ │8
 ├─┼─┼─┼─┼─┼─┼─┼─┤
7│ │ │ │ │ │ │ │ │7
 ├─┼─┼─┼─┼─┼─┼─┼─┤
6│ │ │ │ │ │ │ │ │6
 ├─┼─┼─┼─┼─┼─┼─┼─┤
5│ │ │ │ │ │ │ │ │5
 ├─┼─┼─┼─┼─┼─┼─┼─┤
4│ │ │ │ │ │ │ │ │4
 ├─┼─┼─┼─┼─┼─┼─┼─┤
3│ │ │ │ │ │ │ │ │3
 ├─┼─┼─┼─┼─┼─┼─┼─┤
2│ │ │ │ │ │ │ │ │2
 ├─┼─┼─┼─┼─┼─┼─┼─┤
1│ │ │ │ │ │ │ │ │1
 └─┴─┴─┴─┴─┴─┴─┴─┘
  a b c d e f g h
""";
        assertEquals(esperado, t.toString(), "La representación del tablero debería ser la esperada");

        t = new Tablero("1t6/3a4/6c1/2P2R2/5d2/7A/4C3/r6T");
        esperado = """
  a b c d e f g h
 ┌─┬─┬─┬─┬─┬─┬─┬─┐
8│ │♜│ │ │ │ │ │ │8
 ├─┼─┼─┼─┼─┼─┼─┼─┤
7│ │ │ │♝│ │ │ │ │7
 ├─┼─┼─┼─┼─┼─┼─┼─┤
6│ │ │ │ │ │ │♞│ │6
 ├─┼─┼─┼─┼─┼─┼─┼─┤
5│ │ │♙│ │ │♔│ │ │5
 ├─┼─┼─┼─┼─┼─┼─┼─┤
4│ │ │ │ │ │♛│ │ │4
 ├─┼─┼─┼─┼─┼─┼─┼─┤
3│ │ │ │ │ │ │ │♗│3
 ├─┼─┼─┼─┼─┼─┼─┼─┤
2│ │ │ │ │♘│ │ │ │2
 ├─┼─┼─┼─┼─┼─┼─┼─┤
1│♚│ │ │ │ │ │ │♖│1
 └─┴─┴─┴─┴─┴─┴─┴─┘
  a b c d e f g h
""";
        assertEquals(esperado, t.toString(), "La representación del tablero debería ser la esperada");
    }

    @ParameterizedTest
    @Order(10)
    @DisplayName("Comprobación de si el rey está comido")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "BLANCO, false",
            "NEGRO, true"
    })
    void reyMuerto(Color color, boolean expected) {
        Tablero tablero = new Tablero("8/8/8/8/8/8/8/8");
        tablero.agregarPieza(0, 4, new Pieza('R'));
        boolean result = tablero.reyMuerto(color);
        assertEquals(expected, result);
    }

    @ParameterizedTest
    @Order(11)
    @DisplayName("Realizar movimiento debe actualizar el estado del tablero")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "8/8/8/3R4/8/8/8/8, R, 3, 3, 4, 4",
            "c7/8/8/8/8/8/8/8, c, 0, 0, 2, 1"
    })
    void testRealizarMovimientoActualizaTablero(String tablero, char pieza, int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        Tablero t = new Tablero(tablero);
        t.realizarMovimiento(new Movimiento(new Pieza(pieza), filaOrigen, columnaOrigen, filaDestino, columnaDestino));
        assertNotNull(t.getPieza(filaDestino, columnaDestino), "La casilla a la que se mueve una pieza no debería estar vacía");
        assertEquals(new Pieza(pieza), t.getPieza(filaDestino, columnaDestino), "La pieza debería haberse movido correctamente");
        assertNull(t.getPieza(filaOrigen, columnaOrigen), "La casilla de origen debería estar vacía después del movimiento");
    }
}
