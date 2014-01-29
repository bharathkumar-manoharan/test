<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Profile</title>
</head>
<body>

<%-- <img src="<%= session.getAttribute("imageurl") %>" /> --%>
<%-- <% String fname = session.getAttribute("firstname").toString(); --%>
<!-- String name = session.getAttribute("name").toString(); -->
<!-- String gender = session.getAttribute("gender").toString();%> -->
<%-- <h1>Hi,<%=fname%></h1> --%>
<fieldset>
<legend>My Profile</legend>
<table><tr>
<td><img src="<%=session.getAttribute("imageurl")%>" width="100" height="100" /></td><td></td><td></td><td></td>
<%-- <td><h3>Name :<%=name%></h3> --%>
<%-- <h3>Gender :<%=gender%></h3> --%>
</td><td></td><td></td><td ></td>
<td>
<h3>Emailid :<%=session.getAttribute("email")%></h3></td>
</tr>
</table>
</fieldset> 
</body>
</html>