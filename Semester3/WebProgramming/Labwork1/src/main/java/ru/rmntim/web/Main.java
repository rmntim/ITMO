package ru.rmntim.web;

import com.fastcgi.FCGIInterface;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    private record Params(int x, float y, float r) {
    }

    public static void main(String[] args) {
        var fcgi = new FCGIInterface();
        while (fcgi.FCGIaccept() >= 0) {
            System.out.print("Content-type: application/json\r\n\r\n");

            var uriString = System.getProperties().getProperty("REQUEST_URI");

            if (uriString == null || uriString.isEmpty()) {
                System.out.println("No request URI provided");
                continue;
            }

            try {
                var uri = new URI(uriString);
                var params = splitQuery(uri);
                var validParams = validateParams(params);
                var result = calculate(validParams);

                if (result) {
                    System.out.println("{ \"result\": \"hit\" }");
                } else {
                    System.out.println("{ \"result\": \"miss\" }");
                }
            } catch (URISyntaxException e) {
                System.out.println("Invalid request URI: " + uriString);
            }
        }
    }

    private static Params validateParams(Map<String, String> params) {
        var x = Integer.parseInt(params.get("x"));
        var y = Float.parseFloat(params.get("y"));
        var r = Float.parseFloat(params.get("r"));

        return new Params(x, y, r);
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

    private static boolean calculate(Params params) {
        return false;
    }
}