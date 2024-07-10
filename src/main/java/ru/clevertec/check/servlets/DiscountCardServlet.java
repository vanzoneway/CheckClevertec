package ru.clevertec.check.servlets;

import ru.clevertec.check.exception.ExceptionHandler;
import ru.clevertec.check.exception.NoValidParametersException;
import ru.clevertec.check.model.DiscountCard;
import ru.clevertec.check.service.DiscountCardService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Objects;

@WebServlet("/discountcards")
public class DiscountCardServlet extends HttpServlet {

    DiscountCardService discountCardService = new DiscountCardService();

    public DiscountCardServlet() throws SQLException, ClassNotFoundException {
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            if (Objects.isNull(request.getParameter("id")))
                throw new NoValidParametersException("THERE IS NO SUCH DISCOUNT CARD");

            discountCardService.returnDiscountCard(Integer.parseInt(request.getParameter("id")), response);

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        try {
            discountCardService.postDiscountCard(request);
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception, response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response){
        try {
            discountCardService.updateDiscountCard(Integer.parseInt(request.getParameter("id")), request);
        } catch (Exception e) {
            ExceptionHandler.handleException(e, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response){

        try {
            if (Objects.isNull(request.getParameter("id")))
                throw new NoValidParametersException("WRONG WRITTEN ID PARAMETER");
            discountCardService.deleteDiscountCard(Integer.parseInt(request.getParameter("id")));
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception, response);
        }
    }

}


