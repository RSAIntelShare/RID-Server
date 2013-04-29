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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>RIDSystem - Send WatchList</title>
</head>
<body>
<h2>Send RID WatchList</h2>
<form:form method="post" action="${contextPath}/RIDSender/send/WatchList" commandName="params">

<table>
   
    <tr>
        <td><form:label path="id">WatchList ID</form:label></td>
        <td><form:input path="id" /></td>
    </tr>
    
    
    <tr>
        <td><form:label path="destination">Peer RID System URI</form:label></td>
        <td><form:input path="destination" /></td>
    </tr>
   
     
    <tr>
        <td colspan="2">
            <input type="submit" value="Send WatchList"/>
        </td>
    </tr>
</table>  

</form:form>


</body>
</html>