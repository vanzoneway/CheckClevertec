package ru.clevertec.check.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVWorker implements CSVReader, CSVWriter {

    @Override
    public List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> csvData = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] values = line.split(";");
                csvData.add(values);
            }
        } catch (Exception e) {
            throw new IOException("BAD REQUEST");
        }
        return csvData;
    }

    @Override
    public void writeCSV(String filePath, List<String[]> csvData) throws IllegalArgumentException {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : csvData) {
                fileWriter.write(String.join(";", row));
                fileWriter.newLine();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("BAD REQUEST");
        }
    }

    @Override
    public void writeCSVToConsole(List<String[]> csvData) {
        for (String[] row : csvData) {
            System.out.println(String.join(";", row));
        }
    }


}
