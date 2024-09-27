<%@ page import="ru.rmntim.web.labwork2.repository.PointRepository" %>
<%@ page import="ru.rmntim.web.labwork2.models.Point" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>First Lab</title>
    <link rel="stylesheet" href="styles/reset.css">
    <link rel="stylesheet" href="styles/main.css">
</head>
<body>
<nav class="navbar">
    <div id="info">
        Timoshkin Roman
        <br/>
        P3231
        <br/>
        <span>v. <span class="fire">666</span></span>
    </div>
    <a href="https://github.com/rmntim" target="_blank" id="github">github</a>
</nav>
<main class="container">
    <section class="results">
        <table id="result-table">
            <thead>
            <tr>
                <th>x</th>
                <th>y</th>
                <th>r</th>
                <th>result</th>
            </tr>
            </thead>
            <tbody>
            <%
                PointRepository repo = (PointRepository) request.getSession().getAttribute("bean");
                if (repo != null) {
                    for (Point point : repo.getPoints()) {
            %>
            <tr>
                <td>
                    <%= point.x() %>
                </td>
                <td>
                    <%= point.y() %>
                </td>
                <td>
                    <%= point.r() %>
                </td>
                <td>
                    <%= point.isInside() ? "inside" : "outside" %>
                </td>
            </tr>
            <%
                    }
                }
            %>
            </tbody>
        </table>
        <div id="recall">
            <a href="index.jsp">go back</a>
        </div>
    </section>
</main>
<footer id="copyright">all rights belong to ur mom,&nbsp;
    <a href="http://www.wtfpl.net/txt/copying/"
       target="_blank">WTFPL</a>
    , 2024
</footer>
</body>
</html>
