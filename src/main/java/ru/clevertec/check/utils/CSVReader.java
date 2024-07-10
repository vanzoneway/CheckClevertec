package ru.clevertec.check.utils;

import java.io.IOException;
import java.util.List;

public interface CSVReader {

    List<String[]> readCSV(String fileName) throws IllegalArgumentException, IOException;

}
