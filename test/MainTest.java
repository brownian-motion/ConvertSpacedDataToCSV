import com.brownian.research.abinit.cut3d.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests various command-line arguments and inputs to main()
 */
@Tag("params")
class MainTest {

    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
    }

    @AfterEach
    void tearDown() {
        System.setIn(null);
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    void verboseProducesMoreErrorOutput() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("1 2 3 4".getBytes());
        System.setIn(inputStream);

        Main.main(new String[0]);

        String regularOutput = errorStream.toString();

        outputStream.reset();
        inputStream.reset();

        Main.main(new String[] { "--verbose"});

        assertNotEquals(regularOutput,errorStream.toString());
    }

    @ParameterizedTest
    @EnumSource(TestCase.class)
    private void testUsingStdinStdout(TestCase testCase){
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testCase.input.getBytes());
        System.setIn(inputStream);
        Main.main(new String[0]);

        assertEquals(testCase.expectedOutput,outputStream.toString());
    }

    @ParameterizedTest
    @EnumSource(TestCase.class)
    private void testUsingStreamsDirectly(TestCase testCase){
        Main.executeConversion(new InputStreamReader(new ByteArrayInputStream(testCase.input.getBytes())),new OutputStreamWriter(outputStream));
        assertEquals(testCase.expectedOutput,outputStream.toString());
    }

    private enum TestCase{
        ONE_LINE("1 2 3 4","1,2,3,4"),
        ACCEPTS_TWO_LINES("1 2 3 4\n5 6 7 8","1,2,3,4\n5,6,7,8"),
        ACCEPTS_CHARACTERS_AS_WELL_AS_NUMBERS("a b c d\n1 2 3 4","a,b,c,d\n1,2,3,4"),
        ACCEPTS_DIFFERENT_LINE_LENGTHS("1 2 3\n4 5 6 7 8","1,2,3\n4,5,6,7,8"),
        TRIMS_EDGES(" 1 2 3 4  \n   5 6 7 8    ","1,2,3,4\n5,6,7,8"),
        HANDLES_MANY_SPACES_BETWEEN_ELEMENTS("1 2  3   4\n5     6      7       8","1,2,3,4\n5,6,7,8"),
        ;

        String input;
        String expectedOutput;

        TestCase(String input, String expectedOutput){
            this.input=input;
            this.expectedOutput = expectedOutput;
        }
    }

}