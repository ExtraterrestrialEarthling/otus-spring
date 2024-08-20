package ru.otus.hw.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerImpl implements ErrorController {


    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status instanceof Integer) {
            int statusCode = (Integer) status;
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
            if (httpStatus == HttpStatus.FORBIDDEN) {
                return "access-restricted";
            }
        }
        return "error";
    }
}
