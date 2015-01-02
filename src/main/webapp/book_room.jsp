<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>EYDOKIA - book a room</title>
        <link type="text/css" rel="stylesheet" 
              href="${pageContext.request.contextPath}/css/main.css" />
    </head>
    <body>
        <%@ include file="/WEB-INF/jspf/header.jspf" %>
        
        <h2 class="error-msg">${requestScope.errorMsg}</h2>
        
        <p>Room:</p><select name="room_id" form="book_room">
            <c:forEach var="room" items="${requestScope.rooms_list}"> 
                <option value="${room.roomId}">${room.name}</option>
            </c:forEach>
        </select>
        <p>Schedule:</p><select name="schedule_type_id" form="book_room">
            <c:forEach var="schedule_type" items="${requestScope.schedule_types_list}"> 
                <option value="${schedule_type.scheduleTypeId}">${schedule_type.name}</option>
            </c:forEach>
        </select>
        <form action="${pageContext.request.contextPath}/book_room" method="post" id="book_room">
            <fieldset>
                <input type="date" name="start_date" />
                <input type="date" name="end_date" />
                <input type="time" name="start_time" />
                <input type="time" name="end_time" />
                <input type="text" name="description" placeholder="Description" />
                <input type="submit" name="book" value="Book" />
            </fieldset>
        </form>
        <%@ include file="/WEB-INF/jspf/room_entries.jspf" %>
    </body>
</html>
