
<%@page import="java.util.Random"%>
<%@page import="mypackage.Incrementor"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>User Form</title>
    </head>
    <body> 
        <%!int i;
        %>  
        <%String click = request.getParameter("click");
            if (click == null) {
                i = Incrementor.getClick();
            } else {
                i = Incrementor.addClickWithSave();
            }
        %>
        <%=i%> 
        <form action="MyJsp.jsp" method="POST">
            <input type="hidden" name = "click" value="yes">
            <input type="submit" value="Click" />
        </form>
    </body>
</html>
