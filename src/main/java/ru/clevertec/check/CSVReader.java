package ru.clevertec.check;

import java.util.List;

public interface CSVReader {

    List<String[]> readCSV(String fileName) throws IllegalArgumentException;


}
