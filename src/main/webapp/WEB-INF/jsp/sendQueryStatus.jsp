<!--  
/*
 * Copyright (c) 2013, EMC Corporation ("EMC"). 
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 * 
*/

/*	
 *  @author Vijayanand Bharadwaj
 *	@author John P. Field
*/	
-->

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="org.springframework.http.HttpStatus"%>
<%@page import="org.springframework.web.util.HtmlUtils"%>
 
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="postResponse" type="java.util.Map" scope="request"/>


<html>
<head>
    <title>RIDSystem - Send Query Status</title>
</head>
<body>
<%
if((Boolean)postResponse.get("success"))
{
	out.println("Query sent successfully!");
	out.println("<br><br>");
    
	
	if((Integer)postResponse.get("statusCode")==org.springframework.http.HttpStatus.OK.value()||(Integer)postResponse.get("statusCode")==org.springframework.http.HttpStatus.ACCEPTED.value())
	{
		out.println("Returned expected response status codes");
		out.println("<br><br>");
	}
	else
	{
		out.println("Response status codes NOT as per RID RFC! Determine if you need to resend.");
		out.println("<br><br>");
	}
	
	
out.println("Response status code =" +postResponse.get("statusCode"));
out.println("<br><br>");
out.println("Response body = " + "<br>" + HtmlUtils.htmlEscape(postResponse.get("responseBody").toString()));
out.println("<br><br>");
}
else
	{
	out.println("Query NOT sent! Determine if you need to resend.");
	out.println("<br><br>");
	}
%>

<br> <br>
<a href="${contextPath}/RIDSender/send/query">Send Query</a>
<br> <br>
<a href="${contextPath}/RIDSender">Home</a>
 
</body>
</html>