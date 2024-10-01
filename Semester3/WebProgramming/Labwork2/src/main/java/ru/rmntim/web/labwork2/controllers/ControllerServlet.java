package ru.rmntim.web.labwork2.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            var x = request.getParameter("x");
            var y = request.getParameter("y");
            var r = request.getParameter("r");

            if (x == null || y == null || r == null) {
                request.setAttribute("error", String.format(ERROR_MSG, "x, y, and r are required"));
                request.getRequestDispatcher("./error.jsp").forward(request, response);
                return;
            }

            if (x.isEmpty() || y.isEmpty() || r.isEmpty()) {
                request.setAttribute("error", String.format(ERROR_MSG, "x, y, and r must not be empty"));
                request.getRequestDispatcher("./error.jsp").forward(request, response);
                return;
            }

            var dx = Double.parseDouble(x);
            var dy = Double.parseDouble(y);
            var dr = Double.parseDouble(r);

            if (!ALLOWED_X.contains(dx)) {
                request.setAttribute("error", String.format(ERROR_MSG, "x must be in " + ALLOWED_X));
                request.getRequestDispatcher("./error.jsp").forward(request, response);
                return;
            }

            if (!ALLOWED_R.contains(dr)) {
                request.setAttribute("error", String.format(ERROR_MSG, "r must be in " + ALLOWED_R));
                request.getRequestDispatcher("./error.jsp").forward(request, response);
                return;
            }

            if (dy < -5 || dy > 5) {
                request.setAttribute("error", String.format(ERROR_MSG, "y must be in [-5, 5]"));
                request.getRequestDispatcher("./error.jsp").forward(request, response);
                return;
            }

            request.getRequestDispatcher("./calculate").forward(request, response);
        } catch (NumberFormatException | NullPointerException e) {
            request.setAttribute("error", e.toString());
            request.getRequestDispatcher("./error.jsp").forward(request, response);
        }
    }

}
