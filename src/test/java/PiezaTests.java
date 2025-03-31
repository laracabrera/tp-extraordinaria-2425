import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Pruebas de la clase Pieza")
public class PiezaTests {
    @ParameterizedTest
    @DisplayName("Creación de pieza con caracter válido")
    @ExtendWith(SinSalidaDeSistema.class)
    @Order(1)
    @CsvSource({
            "p, PEON, NEGRO",
            "t, TORRE, NEGRO",
            "c, CABALLO, NEGRO",
            "a, ALFIL, NEGRO",
            "d, REINA, NEGRO",
            "r, REY, NEGRO",
            "P, PEON, BLANCO",
            "T, TORRE, BLANCO",
            "C, CABALLO, BLANCO",
            "A, ALFIL, BLANCO",
            "D, REINA, BLANCO",
            "R, REY, BLANCO"
    })
    public void testCreacionPiezaValida(ArgumentsAccessor arguments) {
        Pieza p = new Pieza(arguments.getCharacter(0));
        TipoPieza tipoPieza = arguments.get(1, TipoPieza.class);
        Color color = arguments.get(2, Color.class);
        assertEquals(tipoPieza, p.getTipoPieza(),
                String.format("Crear una pieza con el caracter %c debería crear un/a %s", arguments.getCharacter(0), tipoPieza.toString()));
        assertEquals(color, p.getColor(),
                String.format("Crear una pieza con el caracter %c debería crear una pieza de color %s", arguments.getCharacter(0), color.toString()));
    }

    @ParameterizedTest
    @Order(2)
    @DisplayName("Creación de pieza con caracter NO válido")
    @ExtendWith(SinSalidaDeSistema.class)
    @ValueSource(strings = {"x", "h", "w", "G"})
    public void testCreacionPiezaNoValida(char c) {
        IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, () -> new Pieza(c));
        assertEquals("Caracter de pieza no reconocido", e.getMessage(),
                String.format("Crear una pieza con el caracter %c debería lanzar una excepción", c));
    }

    @Test
    @Order(3)
    @DisplayName("Movida de pieza")
    @ExtendWith(SinSalidaDeSistema.class)
    public void testMovidaPieza() {
        Pieza p = new Pieza('p');
        assertFalse(p.getMovida(), "Una pieza recién creada no debería estar movida");
        p.marcarMovida();
        assertTrue(p.getMovida(), "Marcar una pieza como movida debería cambiar su estado a movida");
    }

    @Test
    @Order(4)
    @DisplayName("Comparación de una pieza consigo misma")
    @ExtendWith(SinSalidaDeSistema.class)
    public void testEqualsPiezaMisma() {
        Pieza p = new Pieza('p');
        assertTrue(p.equals(p), "Una pieza debería ser igual a sí misma");
    }

    @Test
    @Order(5)
    @DisplayName("Comparación de una pieza con otro objeto")
    @ExtendWith(SinSalidaDeSistema.class)
    public void testEqualsPiezaOtroObjeto() {
        Pieza p = new Pieza('p');
        assertNotEquals(new Object(), p, "Una pieza no debería ser igual a otro objeto");
    }

    @ParameterizedTest
    @Order(6)
    @DisplayName("Comparación de piezas")
    @ExtendWith(SinSalidaDeSistema.class)
    @CsvSource({
            "p, p, true",
            "t, t, true",
            "c, c, true",
            "a, a, true",
            "d, d, true",
            "r, r, true",
            "P, P, true",
            "T, T, true",
            "C, C, true",
            "A, A, true",
            "D, D, true",
            "R, R, true",
            "p, t, false",
            "t, c, false",
            "c, a, false",
            "a, d, false",
            "d, r, false",
            "r, p, false",
            "P, T, false",
            "T, C, false",
            "C, A, false",
            "A, D, false",
            "D, R, false",
            "R, P, false"
    })
    public void testEqualsPieza(ArgumentsAccessor arguments) {
        Pieza p1 = new Pieza(arguments.getCharacter(0));
        Pieza p2 = new Pieza(arguments.getCharacter(1));
        boolean expected = arguments.getBoolean(2);
        assertEquals(expected, p1.equals(p2),
                String.format("Las piezas %s y %s deberían ser %s", p1.toString(), p2.toString(), expected ? "iguales" : "diferentes"));
    }
}
