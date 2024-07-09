package runner;

import org.junit.jupiter.api.Test;
import ru.clevertec.check.CheckRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class CheckRunnerTest {

    @Test
    void testCheckRunnerWithValidInput() {
        String[] args = {
                "1-5",
                "2-3",
                "discountCard=ABC123",
                "balanceDebitCard=100.50",
                "saveToFile=output.txt",
                "datasource.url=jdbc:mysql://localhost:3306/mydb",
                "datasource.username=myuser",
                "datasource.password=mypassword"
        };

        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            CheckRunner.main(args);

            String expectedOutput = "ERROR,";
            assertFalse(outContent.toString().contains(expectedOutput));
            assertTrue(new File("output.txt").exists());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            System.setOut(System.out);
        }
    }

}