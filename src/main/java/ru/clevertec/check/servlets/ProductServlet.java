package ru.clevertec.check.servlets;

import ru.clevertec.check.exception.ExceptionHandler;
import ru.clevertec.check.exception.NoValidParametersException;
import ru.clevertec.check.service.ProductService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    ProductService productService = new ProductService();

    public ProductServlet() throws SQLException, ClassNotFoundException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            if(Objects.isNull(request.getParameter("id"))) {
                productService.returnAllProducts(response);
            }else  {
                productService.returnProduct(Integer.parseInt(request.getParameter("id")), response);
            }

        } catch (Exception exception) {
            ExceptionHandler.handleException(exception, response);
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        try {
            productService.updateProduct(Integer.parseInt(request.getParameter("id")), request);
        } catch (Exception exception) {
            ExceptionHandler.handleException(exception, response);
        }

    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response){
        try {
            if(Objects.isNull(request.getParameter("id")))
                throw new NoValidParametersException("WRONG WRITTEN ID PARAMETER");
            productService.deleteProduct(Integer.parseInt(request.getParameter("id")));
        }catch (Exception exception) {
            ExceptionHandler.handleException(exception, response);
        }
    }


}
