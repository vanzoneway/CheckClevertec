package ru.clevertec.check.utils;

import java.util.List;

public interface CSVWriter {

    void writeCSV(String filePath, List<String[]> data) throws IllegalArgumentException;

    void writeCSVToConsole(List<String[]> data);
}
