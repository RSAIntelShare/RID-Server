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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html>
<head><title>EMC OCTO RID System</title></head>

<body>

<h1>EMC OCTO RID System</h1>

Welcome to the EMC OCTO RID System.
<br><br>
<a href="http://www.emc.com">EMC</a>
<ol>
<li><a href ="${contextPath}/RIDSender/createQueryForm">Create</a> a RID Query</li>

<li><a href ="${contextPath}/RIDSender/sendQueryForm">Send</a> a RID Query</li>

<li><a href ="${contextPath}/RIDSender/createReportForm">Create</a> a RID Report</li>

<li><a href ="${contextPath}/RIDSender/createWatchListForm">Create</a>  Malware Watch List Report</li>

<li><a href ="${contextPath}/RIDSender/createIndicatorsForm">Create</a>  Indicators Watch List Report</li>

<li><a href ="${contextPath}/RIDSender/createPhishingForm">Create </a>  Spear Phishing Report</li>

<li><a href ="${contextPath}/RIDSender/createDDOSForm">Create </a>  DDOS Report</li>

<li><a href ="${contextPath}/RIDSender/viewWatchListForm">View </a>  a Report</li>

<li><a href ="${contextPath}/RIDSender/sendReportForm">Send</a> a RID Report</li>

</ol>

</body>
</html>




 

