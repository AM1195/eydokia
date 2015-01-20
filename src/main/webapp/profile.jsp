<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EYDOKIA - ${requestScope.user.username}'s profile</title>
        <link type="text/css" rel="stylesheet" 
              href="${pageContext.request.contextPath}/css/main.css" />
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/header.jspf" %>
        
        ${requestScope.user.username} <br />
        ${requestScope.user.fullName} <br />
        ${requestScope.user.email} <br />
        ${requestScope.user.extraInfo} <br />
        
        <h2 class="error-msg">${requestScope.errorMsg}</h2>
        
        <c:choose>
        <c:when test="${sessionScope.user.userId == requestScope.user.userId}">
            <form action="${pageContext.request.contextPath}/change_password?username=${sessionScope.user.username}" method="post">
                <fieldset>
                    <input type="password" name="old_password" placeholder="Old password" />
                    <input type="password" name="password" placeholder="New password" />
                    <input type="password" name="_password" placeholder="New password again" />
                    <input type="submit" name="change_password" value="Change password" />
                </fieldset>
            </form>
                
            <%@ include file="/WEB-INF/jspf/user_entries.jspf" %>
        </c:when>
        <c:otherwise>
            <%@ include file="/WEB-INF/jspf/date_entries.jspf" %>
        </c:otherwise>
        </c:choose>
    </body>
</html>
