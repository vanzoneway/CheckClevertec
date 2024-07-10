package ru.clevertec.check.servlets;

import ru.clevertec.check.exception.ExceptionHandler;
import ru.clevertec.check.service.CheckService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

@WebServlet("/check")
public class CheckServlet extends HttpServlet {

    CheckService checkService = new CheckService();

    public CheckServlet() throws SQLException, ClassNotFoundException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            checkService.createCheck(response, request);
        } catch (Exception e) {
            ExceptionHandler.handleException(e, response);
        }
    }
}
