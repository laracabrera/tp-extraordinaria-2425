import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Pruebas automatizadas")
@SelectClasses({
        PiezaTests.class,
        TableroTests.class,
        ReglasTests.class,
        MovimientoTests.class,
        AjedrezTests.class
})
public class Pruebas {
}
