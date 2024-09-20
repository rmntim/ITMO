package ru.rmntim.web.labwork2.controllers;

import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.rmntim.web.labwork2.models.ControllerError;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    private static final String ERROR_MSG = "Incorrect data provided: %s";
    // vieboni
    private static final Set<Double> ALLOWED_X = DoubleStream
            .iterate(-2, i -> i <= 2, i -> i + .5)
            .boxed().collect(Collectors.toSet());

    private static final Set<Double> ALLOWED_R = DoubleStream
            .iterate(1, i -> i <= 3, i -> i + .5)
            .boxed().collect(Collectors.toSet());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var x = request.getParameter("x");
        var y = request.getParameter("y");
        var r = request.getParameter("r");

        if (x == null || y == null || r == null) {
            errorResponse(response, String.format(ERROR_MSG, "x, y, and r are required"));
            return;
        }

        if (x.isEmpty() || y.isEmpty() || r.isEmpty()) {
            errorResponse(response, String.format(ERROR_MSG, "x, y, and r must not be empty"));
            return;
        }

        try {
            var dx = Double.parseDouble(x);
            var dy = Double.parseDouble(y);
            var dr = Double.parseDouble(r);

            if (!ALLOWED_X.contains(dx)) {
                errorResponse(response, String.format(ERROR_MSG, "x must be in " + ALLOWED_X));
                return;
            }

            if (!ALLOWED_R.contains(dr)) {
                errorResponse(response, String.format(ERROR_MSG, "r must be in " + ALLOWED_R));
                return;
            }

            if (dy < -5 || dy > 5) {
                errorResponse(response, String.format(ERROR_MSG, "y must be in [-5, 5]"));
                return;
            }

            request.getRequestDispatcher("./calculate").forward(request, response);
        } catch (NumberFormatException | NullPointerException | ServletException e) {
            errorResponse(response, e.toString());
        }
    }

    private void errorResponse(HttpServletResponse response, String error) throws IOException {
        var errorObj = new ControllerError(error);
        var jsonb = JsonbBuilder.create();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json");
        response.getWriter().write(jsonb.toJson(errorObj));
    }
}
