<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register Here</title>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

</head>
<body>
<!-- <img src="/images/loginwith.jpg" usemap=#loginmap>
<map name=loginmap><area shape=rect coords=10,10,395,188 href="http://en.wikipedia.org/wiki/G">
<area href="/facebookOauth" coords="435,225,818,15" shape="rect">
</map> -->
<a href="/facebookOauthCallback"><img src="images/facebooklogo.gif"/></a><br>
<a href="/googleOauthCallback"><img src="images/google.jpg"/></a><br>

 <form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
        <input type="file" name="myFile">
        <input type="submit" value="Submit">
    </form>
    
</body>
</html>