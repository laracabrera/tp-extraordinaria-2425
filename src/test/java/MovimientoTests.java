import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas de la clase Movimiento")
public class MovimientoTests {
    @ParameterizedTest
    @Order(1)
    @DisplayName("Constructor de movimientos")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "P, 1, 1, 2, 2",
            "t, 1, 1, 2, 2",
            "c, 1, 1, 2, 2",
            "A, 1, 1, 2, 2",
            "d, 1, 1, 2, 2",
            "R, 1, 1, 2, 2"
    })
    void testMovimiento(String tipoPieza, int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino) {
        Movimiento m = new Movimiento(new Pieza(tipoPieza.charAt(0)), filaOrigen, columnaOrigen, filaDestino, columnaDestino);
        assertEquals(new Pieza(tipoPieza.charAt(0)), m.pieza);
        assertEquals(filaOrigen, m.filaOrigen);
        assertEquals(columnaOrigen, m.columnaOrigen);
        assertEquals(filaDestino, m.filaDestino);
        assertEquals(columnaDestino, m.columnaDestino);
    }

    @ParameterizedTest
    @Order(2)
    @DisplayName("toString de movimientos")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "P, 1, 1, 2, 2, Pb7c6",
            "t, 4, 7, 0, 0, th4a8",
            "c, 7, 0, 2, 0, ca1a6",
            "A, 6, 4, 4, 4, Ae2e4"
    })
    void testToString(String tipoPieza, int filaOrigen, int columnaOrigen, int filaDestino, int columnaDestino, String expected) {
        Movimiento m = new Movimiento(new Pieza(tipoPieza.charAt(0)), filaOrigen, columnaOrigen, filaDestino, columnaDestino);
        assertEquals(expected, m.toString());
    }

    @ParameterizedTest
    @Order(3)
    @DisplayName("equals de movimientos")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "P, 1, 1, 2, 2, P, 1, 1, 2, 2, true",
            "t, 4, 7, 0, 0, t, 4, 7, 0, 0, true",
            "c, 7, 0, 2, 0, c, 7, 0, 2, 0, true",
            "A, 6, 4, 4, 4, A, 6, 4, 4, 4, true",
            "P, 1, 1, 2, 2, t, 4, 7, 0, 0, false",
            "t, 4, 7, 0, 0, c, 7, 0, 2, 0, false",
            "c, 7, 0, 2, 0, A, 6, 4, 4, 4, false",
            "A, 6, 4, 4, 4, P, 1, 1, 2, 2, false"
    })
    void testEquals(String tipoPieza1, int filaOrigen1, int columnaOrigen1, int filaDestino1, int columnaDestino1, String tipoPieza2, int filaOrigen2, int columnaOrigen2, int filaDestino2, int columnaDestino2, boolean expected) {
        Movimiento m1 = new Movimiento(new Pieza(tipoPieza1.charAt(0)), filaOrigen1, columnaOrigen1, filaDestino1, columnaDestino1);
        Movimiento m2 = new Movimiento(new Pieza(tipoPieza2.charAt(0)), filaOrigen2, columnaOrigen2, filaDestino2, columnaDestino2);
        assertEquals(expected, m1.equals(m2));
    }

    @Test
    @Order(4)
    @DisplayName("Comparación de un movimiento consigo mismo")
    @ExtendWith(SinSalidaDeSistema.class)
    public void testEqualsMovimientoMismo() {
        Movimiento m = new Movimiento(new Pieza('p'), 1, 1, 2, 2);
        assertEquals(m, m, "Un movimiento debería ser igual a sí mismo");
    }

    @Test
    @Order(5)
    @DisplayName("Comparación de un movimiento con otro objeto")
    @ExtendWith(SinSalidaDeSistema.class)
    public void testEqualsMovimientoOtroObjeto() {
        Movimiento m = new Movimiento(new Pieza('p'), 1, 1, 2, 2);
        assertFalse(m.equals(new Tablero("8/8/8/8/8/8/8/8")), "Un movimiento no debería ser igual a otro objeto");
    }
}
