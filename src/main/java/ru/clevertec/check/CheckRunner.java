package main.java.ru.clevertec.check;


import java.util.ArrayList;
import java.util.Arrays;

public class CheckRunner {
    public static void main(String[] args) {

        final String pathToProducts = "./src/main/resources/products.csv";
        final String pathToDiscountCards = "./src/main/resources/discountCards.csv";
        final String pathToResult = "./src/main/result.csv";

        CSVWorker CSVWorker = new CSVWorker();

        try{
            CheckService checkService = new CheckService(args, pathToProducts, pathToDiscountCards, pathToResult);
            checkService.createCheck();
        }catch(Exception e){
            CSVWorker.writeCSV(pathToResult, new ArrayList<>(Arrays.asList(
                    new String[] {"ERROR"},
                    new String[] {e.getMessage()}
            )));
        }



        }
    }