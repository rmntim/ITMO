package ru.rmntim.web;

import com.fastcgi.FCGIInterface;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    private static class ValidationError extends Exception {
        public ValidationError(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        var fcgi = new FCGIInterface();
        while (fcgi.FCGIaccept() >= 0) {
            System.out.print("Content-type: application/json\r\n\r\n");

            var uriString = System.getProperties().getProperty("REQUEST_URI");

            if (uriString == null || uriString.isEmpty()) {
                System.err.println("No request URI provided");
                continue;
            }

            try {
                var uri = new URI(uriString);
                var params = splitQuery(uri);
                var validParams = validateParams(params);
                var result = calculate(validParams.get("x"), validParams.get("y"), validParams.get("r"));

                if (result) {
                    System.out.println("{ \"result\": \"hit\" }");
                } else {
                    System.out.println("{ \"result\": \"miss\" }");
                }
            } catch (URISyntaxException e) {
                System.err.println("Invalid request URI: " + uriString);
            } catch (ValidationError e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private static Map<String, Float> validateParams(Map<String, String> params) throws ValidationError {
        var result = new HashMap<String, Float>();

        var x = params.get("x");
        if (x == null || x.isEmpty()) {
            throw new ValidationError("x is invalid");
        }

        try {
            var xx = Integer.parseInt(x);
            if (xx < -3 || xx > 5) {
                throw new ValidationError("x has forbidden value");
            }
            result.put("x", (float) xx);
        } catch (NumberFormatException e) {
            throw new ValidationError("x is not a number");
        }

        var y = params.get("y");
        if (y == null || y.isEmpty()) {
            throw new ValidationError("y is invalid");
        }

        try {
            var yy = Float.parseFloat(y);
            if (yy < -3 || yy > 5) {
                throw new ValidationError("y has forbidden value");
            }
            result.put("y", yy);
        } catch (NumberFormatException e) {
            throw new ValidationError("y is not a number");
        }

        var r = params.get("r");
        if (r == null || r.isEmpty()) {
            throw new ValidationError("r is invalid");
        }

        try {
            var rr = Float.parseFloat(r);
            if (rr < 1 || rr > 3) {
                throw new ValidationError("r has forbidden value");
            }
            result.put("r", rr);
        } catch (NumberFormatException e) {
            throw new ValidationError("r is not a number");
        }

        return result;
    }

    private static Map<String, String> splitQuery(URI uri) {
        var query_pairs = new LinkedHashMap<String, String>();
        var query = uri.getQuery();
        var pairs = query.split("&");
        for (var pair : pairs) {
            var idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8), URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8));
        }
        return query_pairs;
    }

    private static boolean calculate(float x, float y, float r) {
        if (x > 0 && y > 0) {
            return false;
        }
        if (x > 0 && y < 0) {
            if ((x * x + y * y) > (r / 2) * (r / 2)) {
                return false;
            }
        }
        if (x < 0 && y < 0) {
            if ((x / 2 + y) < -r / 2) {
                return false;
            }
        }
        if (x < 0 && y > 0) {
            if (x < -r / 2 || y > r) {
                return false;
            }
        }
        return true;
    }
}