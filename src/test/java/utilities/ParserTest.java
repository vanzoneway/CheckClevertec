package utilities;

import org.junit.jupiter.api.Test;
import ru.clevertec.check.InputParams;
import ru.clevertec.check.Item;
import ru.clevertec.check.Parser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void testParseBasicCommandArgs() {
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
            InputParams params = Parser.parseBasicCommandArgs(args);
            assertEquals(2, params.getItems().size());
            assertEquals("ABC123", params.getDiscountCard());
            assertEquals(100.50, params.getBalanceDebitCard(), 0.01);
            assertEquals("output.txt", params.getSaveToFile());
            assertEquals("jdbc:mysql://localhost:3306/mydb", params.getDatasourceUrl());
            assertEquals("myuser", params.getDatasourceUsername());
            assertEquals("mypassword", params.getDatasourcePassword());
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    void testParseBasicCommandArgsWithInvalidInput() {
        String[] args = {
                "1-5",
                "2-abc",
                "discountCard=ABC123",
                "balanceDebitCard=100.50",
                "saveToFile=output.txt",
                "datasource.url=jdbc:mysql://localhost:3306/mydb",
                "datasource.username=myuser",
                "datasource.password=mypassword"
        };

        assertThrows(IOException.class, () -> {
            Parser.parseBasicCommandArgs(args);
        });
    }
}