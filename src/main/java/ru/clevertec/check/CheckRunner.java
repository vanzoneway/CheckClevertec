package ru.clevertec.check;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class CheckRunner {
    public static void main(String[] args) {


        String defaultSaveToFile = "./src/main/result.csv";
        InputParams inputParams = null;

        CSVWorker CSVWorker = new CSVWorker();

        try {
            inputParams = Parser.parseBasicCommandArgs(args);
        } catch (Exception e) {
            logErrorResult(CSVWorker, defaultSaveToFile, e);
            System.exit(1);
        }

        ProductRepository productRepository = new JdbcProductRepository(inputParams);
        DiscountCardRepository discountCardRepository = new JdbcDiscountCardRepository(inputParams);

        try {
            assert inputParams != null;
            CheckService checkService = new CheckService(inputParams, discountCardRepository,  productRepository);
            checkService.createCheck();
        } catch (Exception e) {
            if (Objects.isNull(inputParams.getSaveToFile())) {
                logErrorResult(CSVWorker, defaultSaveToFile, e);
            } else {
                logErrorResult(CSVWorker, inputParams.getSaveToFile(), e);
            }
        }


    }

    private static void logErrorResult(CSVWorker CSVWorker, String pathToResult, Exception e) {
        CSVWorker.writeCSV(pathToResult, new ArrayList<>(Arrays.asList(
                new String[]{"ERROR"},
                new String[]{e.getMessage()}
        )));
        CSVWorker.writeCSVToConsole(new ArrayList<>(Arrays.asList(
                new String[]{"ERROR"},
                new String[]{e.getMessage()}
        )));
    }
}