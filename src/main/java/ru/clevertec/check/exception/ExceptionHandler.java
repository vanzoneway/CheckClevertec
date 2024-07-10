package ru.clevertec.check.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.postgresql.util.PSQLException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExceptionHandler {

    public static void handleException(Exception e, HttpServletResponse response) {
        Class<? extends Exception> exceptionClass = e.getClass();

        try {
            if (
                    exceptionClass == NoValidParametersException.class
                            || exceptionClass == NoSuchProductIdException.class
                            || exceptionClass == NotEnoughMoneyException.class
                            || exceptionClass == NotEnoughQuantityInStock.class
                            || exceptionClass == JsonParseException.class
                            || exceptionClass == NumberFormatException.class
                            || exceptionClass == JsonMappingException.class
                            || exceptionClass == UnrecognizedPropertyException.class
                            || exceptionClass == PSQLException.class
            ) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);

            } else if (exceptionClass == ProductNotFoundException.class) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }


    }

}

