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
 -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<html>
<head>
<script type="text/javascript" src="/js/jquery-1.9.1.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>DDOS Form</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function() {
	function malware () {
	    this.ipaddresspos = 0;
	    this.hashpos=0;
	    this.hashanother=[];
	    this.ipanother=[];
	    this.registrypos=0;
	}

	var instance= new malware();
	instance.hashanother[0]=0;
	instance.ipanother[0]=0;
	var arr=[];
	arr.push(instance);
	
	$(document).on('click',".addIP",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		arr[index].ipaddresspos++;
		var Position=arr[index].ipaddresspos;
		arr[index].ipanother[Position]=0;
		//alert(Position);
    		var appendTxt = "<tr>";
    		appendTxt=appendTxt+"<td>System Category:<select name=\"Node["+index+"].address["+ Position +"].systemcategory\"> <option value=\"source\">Source</option><option value=\"target\">Target</option><option value=\"watchlist-source\">WatchList Source</option> <option value=\"watchlist-target\">WatchList Target</option> </select>";
    		appendTxt=appendTxt+"Node Role:<select name=\"Node["+index+"].address["+ Position +"].role\"> <option value=\"\">--Please Select</option> <option value=\"c2-server\">Command and Control Server</option> <option value=\"malware-distribution\">Malware Distribution</option><option value=\"dns-spoof\">DNS Spoof</option><option value=\"phishing\">Phishing</option><option value=\"spear-phishing\">Spear Phishing</option><option value=\"other\">Other</option> </select>";
    		appendTxt=appendTxt+"Node Category:<select name=\"Node["+index+"].address["+ Position +"].category\"> <option value=\"server-internal\">Internal Server</option> <option value=\"server-public\">Public Server</option> <option value=\"www\">WWW</option><option value=\"mail\">Mail</option><option value=\"ext-value\">Ext-Value</option> </select>";
    		appendTxt=appendTxt+"Grouping:<select name=\"Node["+index+"].address["+ Position +"].logicaloperator\"> <option value=\"or\">Not Related</option> <option value=\"and\">Related</option></select>";
    		appendTxt=appendTxt+"Traffic Spoofed?<select name=\"Node["+index+"].address["+ Position +"].spoofed\"> <option value=\"\">--Please Select</option><option value=\"yes\">Yes</option> <option value=\"no\">No</option></select></td></tr>";
    		appendTxt = appendTxt+"<tr><td><input id=\"Node"+ index + ".address" + Position + ".system"+ 0 +".value\" name=\"Node["+ index +"].address[" + Position + "].system["+ 0 +"].value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].address["+ Position +"].system["+ 0 +"].type\"> <option value=\"ipv4-addr\">IPV4</option> <option value=\"ipv6-addr\">IPV6</option> <option value=\"site-uri\">URL</option><option value=\"ipv4-net\">IPV4 Net Block</option><option value=\"name\">Name</option><option value=\"asn\">ASN</option><option value=\"ext-value\">ext-value</option> </select>";
    		appendTxt=appendTxt+"<input type=\"button\" id=\""+Position+"\" data-row=\""+ 0 +"\" class=\"addService\"  value=\"Add Service\" /> </td>";
    		appendTxt = appendTxt+"</tr>";
    		appendTxt=appendTxt+"<tr><td><input type=\"button\" id=\""+ Position +"\" class=\"addAddress\"  value=\"Add Address\" /> </td></tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);
	});
	
	$(document).on('click',".addService",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		var Position=Number($(this).attr("id"));	
		var IPPosition=Number($(this).attr("data-row"));
		//alert(Position);
    		var appendTxt="Protocol*:<select name=\"Node["+index+"].address["+ Position +"].system["+IPPosition+"].protocolno\"> <option value=\"6\">TCP</option> <option value=\"17\">UDP</option></select>";
    		appendTxt =appendTxt+"Port*:<input id=\"Node"+ index + ".address" + Position + ".system"+ IPPosition +".portno\" name=\"Node["+ index +"].address[" + Position + "].system["+IPPosition+"].portno\" size=\"40\" />";
    		appendTxt =appendTxt+"UserAgent:<input id=\"Node"+ index + ".address" + Position + ".system"+ IPPosition +".useragent\" name=\"Node["+ index +"].address[" + Position + "].system["+IPPosition+"].useragent\" size=\"40\" />";
    		//alert(appendTxt);
			$(this).before(appendTxt);
			$(this).hide();
	});
	$(document).on('click',".addAddress",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		//var Position=arr[index].hashpos;
		var Position=Number($(this).attr("id"));
		arr[index].ipanother[Position]++;
		var anotherIP=arr[index].ipanother[Position];		
		//alert(Position);
    		var appendTxt = "<tr>";
    		appendTxt = appendTxt+"<td><input id=\"Node"+ index + ".address" + Position + ".system"+ anotherIP +".value\" name=\"Node["+ index +"].address[" + Position + "].system["+ anotherIP +"].value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].address["+ Position +"].system["+ anotherIP +"].type\"> <option value=\"ipv4-addr\">IPV4</option> <option value=\"ipv6-addr\">IPV6</option> <option value=\"site-uri\">URL</option><option value=\"ipv4-net\">IPV4 Net Block</option><option value=\"name\">Name</option><option value=\"asn\">ASN</option><option value=\"ext-value\">ext-value</option> </select>";
    		appendTxt=appendTxt+"<input type=\"button\" id=\""+Position+"\" data-row=\""+ anotherIP +"\" class=\"addService\"  value=\"Add Service\" /> </td>";
    		appendTxt = appendTxt+"</tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);
	});
});
</script>
<form:form method="post" action="${contextPath}/RIDSender/create/DDOS" name="classForm" id="classForm" commandName="classCommand">
	<table id="0">
		<tr>
			<td>&nbsp;&nbsp;<b>DDOS Information Sharing</b></td>
		</tr>
		<tr>
				<td>Description*<spring:bind path="description">
				<form:textarea path="${status.expression}" rows="5" cols="40"/>
	  			</spring:bind>
	  		</td>
		</tr>
				<tr>
				<td>ReportTime*{YY:MM:DD:HH:MI:SE}<spring:bind path="reporttime">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
		<tr>
				<td>StartTime*{YY:MM:DD:HH:MI:SE}<spring:bind path="starttime">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
				<tr>
				<td>DetectTime*{YY:MM:DD:HH:MI:SE}<spring:bind path="detecttime">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
				<tr>
				<td>Expectation<spring:bind path="Node[0].expectation">
				<form:textarea path="${status.expression}" rows="5" cols="40"/>
	  			</spring:bind>
	  		</td>
		</tr>
		<tr>
			<td>Name:<spring:bind path="Node[0].refName">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
		<tr>
			<td>URL:<spring:bind path="Node[0].refURL">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
		<tr>
			<td>Confidence Score:<spring:bind path="Node[0].confidence">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
		<tr>
			<td>System Information*</td>
		</tr>
		<tr>
			<td>
				System Category:<select name="Node[0].address[0].systemcategory" >
				<option value="source">Source</option>
				<option value="target">Target</option>
				<option value="watchlist-source">WatchList Source</option>
				<option value="watchlist-target">WatchList Target</option>
				</select>
				Node Role:<select name="Node[0].address[0].role" >
				<option value="">--Please Select</option>
				<option value="c2-server">Command and Control Server</option>
				<option value="malware-distribution">Malware Distribution</option>
				<option value="dns-spoof">DNS Spoof</option>
				<option value="phishing">Phishing</option>
				<option value="spear-phishing">Spear Phishing</option>
				<option value="other">Other</option>
				</select>
	  			Node Category:<select name="Node[0].address[0].category" >
				<option value="server-internal">Internal Server</option>
				<option value="server-public">Public Server</option>
				<option value="www">WWW</option>
				<option value="mail">Mail</option>
				<option value="ext-value">Ext-Value</option>                          
				</select>
			    Grouping:<select name="Node[0].address[0].logicaloperator" >
				<option value="or">Not Related</option>
				<option value="and">Related</option>                          
				</select>
				Traffic Spoofed?<select name="Node[0].address[0].spoofed" >
				<option value="">--Please Select</option>
				<option value="yes">Yes</option>
				<option value="no">No</option>                          
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<spring:bind path="Node[0].address[0].system[0].value">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
				<select name="Node[0].address[0].system[0].type" >
				<option value="ipv4-addr">IPV4</option>
				<option value="ipv6-addr">IPV6</option>
				<option value="site-uri">URL</option>
				<option value="ipv4-net">IPV4 Net Block</option>
				<option value="name">Name</option>
				<option value="asn">ASN</option>
				<option value="ext-value">ext-value</option>                           
				</select>
				<input type="button" id="0" data-row="0" class="addService" value="Add Service"/>      
			</td>
		</tr>
		<tr><td><input type="button" id="0" class="addAddress" value="Add Address"/> </td></tr>
		<tr>
			<td><input type="button" id="add" class="addIP" value="Add Node"/> </td>
		</tr>
		<tr>
		<td>
			<spring:bind path="Node[0].httpcomments">
					HTTP Information<form:textarea path="${status.expression}" rows="2" cols="40"/>
	  		</spring:bind>
		</td>
		</tr>
		<!--  <tr>
		<td>
			<spring:bind path="Node[0].expectation">
					 Expectation: <form:textarea path="${status.expression}" rows="2" cols="40"/>
	  		</spring:bind>
	  		<select name="Node[0].type" >
				<option value="ipv4-addr">IPV4</option>
				<option value="ipv6-addr">IPV6</option>
				<option value="site-uri">URL</option>
				<option value="ipv4-net">IPV4 Net Block</option>
				<option value="name">Name</option>
				<option value="asn">ASN</option>
				<option value="ext-value">ext-value</option>                           
				</select>
		</td>
		</tr>  -->
	<tr><td><input type="submit" id="submitRow" value="Generate DDOS Report" /></td></tr>
	</table>
</form:form>

</body>
</html>