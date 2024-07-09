package utilities;

import org.junit.jupiter.api.Test;
import ru.clevertec.check.CSVWorker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class CSVWorkerTest {


    @Test
    void testReadCSVWithValidFile() {
        CSVWorker csvWorker = new CSVWorker();
        String filePath = "test.csv";
        createTestCSVFile(filePath, "John;Doe;30\nJane;Doe;25\nBob;Smith;40");

        try {
            List<String[]> csvData = csvWorker.readCSV(filePath);
            assertEquals(3, csvData.size());
            assertArrayEquals(new String[] {"John", "Doe", "30"}, csvData.get(0));
            assertArrayEquals(new String[] {"Jane", "Doe", "25"}, csvData.get(1));
            assertArrayEquals(new String[] {"Bob", "Smith", "40"}, csvData.get(2));
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            deleteTestCSVFile(filePath);
        }
    }

    @Test
    void testReadCSVWithNonExistentFile() {
        CSVWorker csvWorker = new CSVWorker();
        String filePath = "non-existent.csv";

        assertThrows(IllegalArgumentException.class, () -> csvWorker.readCSV(filePath));
    }

    @Test
    void testReadCSVWithEmptyFile() {
        CSVWorker csvWorker = new CSVWorker();
        String filePath = "empty.csv";
        createTestCSVFile(filePath, "");

        try {
            List<String[]> csvData = csvWorker.readCSV(filePath);
            assertTrue(csvData.isEmpty());
        } catch (IllegalArgumentException e) {
            fail("Unexpected exception: " + e.getMessage());
        } finally {
            deleteTestCSVFile(filePath);
        }
    }

    
    private void createTestCSVFile(String filePath, String content) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            fail("Failed to create test CSV file: " + e.getMessage());
        }
    }

    private void deleteTestCSVFile(String filePath) {
        File file = new File(filePath);
        file.delete();
    }
}