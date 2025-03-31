import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SinSalidaDeSistema implements BeforeEachCallback, AfterEachCallback {
    private PrintStream originalOut;
    private ByteArrayOutputStream bos;

    @Override
    public void beforeEach(ExtensionContext context) {
        originalOut = System.out;
        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        System.setOut(originalOut);
        assertEquals("", bos.toString(), "Se ha imprimido texto en System.out. TODO el texto debe imprimirse en el argumento PrintStream pantalla de los métodos");
    }
}
