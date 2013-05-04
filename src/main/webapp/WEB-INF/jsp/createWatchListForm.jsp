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
<title>WatchList Form</title>
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
    		appendTxt=appendTxt+"<td>System Category:<select name=\"Node["+index+"].address["+ Position +"].systemcategory\"> <option value=\"watchlist-source\">WatchList Source</option> <option value=\"watchlist-target\">WatchList Target</option> </select>";
    		appendTxt=appendTxt+"Node Role:<select name=\"Node["+index+"].address["+ Position +"].role\"> <option value=\"\">--Please Select</option> <option value=\"c2-server\">Command and Control Server</option> <option value=\"malware-distribution\">Malware Distribution</option><option value=\"dns-spoof\">DNS Spoof</option><option value=\"phishing\">Phishing</option><option value=\"spear-phishing\">Spear Phishing</option><option value=\"other\">Other</option> </select>";
    		appendTxt=appendTxt+"Node Category:<select name=\"Node["+index+"].address["+ Position +"].category\"> <option value=\"server-internal\">Internal Server</option> <option value=\"server-public\">Public Server</option> <option value=\"www\">WWW</option><option value=\"mail\">Mail</option><option value=\"ext-value\">Ext-Value</option> </select></td></tr>";
    		appendTxt = appendTxt+"<tr><td><input id=\"Node"+ index + ".address" + Position + ".system"+ 0 +".value\" name=\"Node["+ index +"].address[" + Position + "].system["+ 0 +"].value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].address["+ Position +"].system["+ 0 +"].type\"> <option value=\"ipv4-addr\">IPV4</option> <option value=\"ipv6-addr\">IPV6</option> <option value=\"site-uri\">URL</option><option value=\"ipv4-net\">IPV4 Net Block</option><option value=\"name\">Name</option> </select>";
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
    		var appendTxt="<select name=\"Node["+index+"].address["+ Position +"].system["+IPPosition+"].protocolno\"> <option value=\"6\">TCP</option> <option value=\"17\">UDP</option></select>";
    		appendTxt =appendTxt+"<input id=\"Node"+ index + ".address" + Position + ".system"+ IPPosition +".portno\" name=\"Node["+ index +"].address[" + Position + "].system["+IPPosition+"].portno\" size=\"40\" />";
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
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].address["+ Position +"].system["+ anotherIP +"].type\"> <option value=\"ipv4-addr\">IPV4</option> <option value=\"ipv6-addr\">IPV6</option> <option value=\"site-uri\">URL</option><option value=\"ipv4-net\">IPV4 Net Block</option><option value=\"name\">Name</option> </select>";
    		appendTxt=appendTxt+"<input type=\"button\" id=\""+Position+"\" data-row=\""+ anotherIP +"\" class=\"addService\"  value=\"Add Service\" /> </td>";
    		appendTxt = appendTxt+"</tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);
	});
	
	
	$(document).on('click',".addHash",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		arr[index].hashpos++;
		var Position=arr[index].hashpos;
		arr[index].hashanother[Position]=0;
		//alert(Position);
    		var appendTxt = "<tr>";
    		appendTxt = appendTxt+"<td><input id=\"Node"+ index + ".hash" + Position + ".value"+ 0 +".value\" name=\"Node["+ index +"].hash[" + Position + "].value[" + 0 + "].value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].hash["+ Position +"].value["+ 0 +"].hash_type\"> <option value=\"http://www.w3.org/2001/04/xmlenc#md5\">MD5</option> <option value=\"http://www.w3.org/2001/04/xmlenc#sha1\">SHA1</option><option value=\"http://www.w3.org/2001/04/xmlenc#sha256\">SHA256</option> </select>";
    		appendTxt=appendTxt+"<input type=\"button\" id=\""+ Position +"\" class=\"addAnother\"  value=\"Add Another Representation\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].hash["+ Position +"].type\"><option value=\"\">--Please Select</option> <option value=\"file_hash\">File Hash</option> <option value=\"email_hash\">Email_Hash</option> </select></td>";
    		appendTxt = appendTxt+"</tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);			
	});
	$(document).on('click',".addAnother",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		//var Position=arr[index].hashpos;
		var Position=Number($(this).attr("id"));
		arr[index].hashanother[Position]++;
		var anotherHash=arr[index].hashanother[Position];
		//alert(Position);
		//alert(anotherHash);
    		var appendTxt ="<input id=\"Node"+ index + ".hash" + Position + ".value"+ anotherHash +".value\" name=\"Node["+ index +"].hash[" + Position + "].value[" + anotherHash + "].value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].hash["+ Position +"].value["+ anotherHash +"].hash_type\"> <option value=\"http://www.w3.org/2001/04/xmlenc#md5\">MD5</option> <option value=\"http://www.w3.org/2001/04/xmlenc#sha1\">SHA1</option><option value=\"http://www.w3.org/2001/04/xmlenc#sha256\">SHA256</option> </select>";
    		//alert(appendTxt);
			$(this).before(appendTxt);			
	});

	
	
	$(document).on('click',".addReg",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		arr[index].registrypos++;
		var Position=arr[index].registrypos;		
		//alert(Position);
    		var appendTxt = "<tr>";
    		appendTxt=appendTxt+"<td>Key:";
    		appendTxt = appendTxt+"<input id=\"Node"+ index + ".registry" + Position + ".key\" name=\"Node["+ index +"].registry[" + Position + "].key\" size=\"40\" />";
    		appendTxt=appendTxt+"Value:";
    		appendTxt = appendTxt+"<input id=\"Node"+ index + ".registry" + Position + ".value\" name=\"Node["+ index +"].registry[" + Position + "].value\" size=\"40\" />";
    		appendTxt=appendTxt+"Action:";    		
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].registry["+ Position +"].action\"> <option value=\"add_value\">Add Value</option> <option value=\"delete_value\">Delete Value</option> <option value=\"modify_value\">Modify Value</option>";
    		appendTxt=appendTxt+"<option value=\"add_key\">Add Key</option> <option value=\"delete_key\">Delete Key</option><option value=\"modify_key\">Modify Key</option> </select>  </td>";
    		appendTxt = appendTxt+"</tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);	
	});
});
</script>
<form:form method="post" action="${contextPath}/RIDSender/create/Indicators" name="classForm" id="classForm" commandName="classCommand">
	<table id="0">
		<tr>
			<td>&nbsp;&nbsp;<b>WatchList Indicator(s) Sharing</b></td>
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
			<td>Malicious Node(s)*</td>
		</tr>
		<tr>
			<td>
				System Category:<select name="Node[0].address[0].systemcategory" >
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
				</select>
				<input type="button" id="0" data-row="0" class="addService" value="Add Service"/>      
			</td>
		</tr>
		<tr><td><input type="button" id="0" class="addAddress" value="Add Address"/> </td></tr>
		<tr>
			<td><input type="button" id="add" class="addIP" value="Add Node"/> </td>
		</tr>
		<tr>
			<td>Malware Hashe(s)*</td>
		</tr>
		<tr>
			<td>
				<spring:bind path="Node[0].hash[0].value[0].value">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  			<select name="Node[0].hash[0].value[0].hash_type" >
				<option value="http://www.w3.org/2001/04/xmlenc#md5">MD5</option>
				<option value="http://www.w3.org/2001/04/xmlenc#sha1">SHA1</option>
				<option value="http://www.w3.org/2001/04/xmlenc#sha256">SHA256</option>     
				</select>
				<input type="button" id="0" class="addAnother" value="Add Another Representation"/>
				<select name="Node[0].hash[0].type" >
				<option value="">--Please Select</option>
				<option value="file_hash_watchlist">File Hash</option>
				<option value="email_hash_watchlist">Email_Hash</option>
				</select>
			</td>
		</tr>
		<tr>
			<td><input type="button" id="addhash" class="addHash" value="Add Hash"/> </td>
		</tr>
		<tr>
		<td>
		 Registry Key Value Pair(s)*
		</td>
		</tr>
		<tr>
				<td>Key:<form:input path="Node[0].registry[0].key" size="40" />
				Value:<form:input path="Node[0].registry[0].value" size="40" />
				Action:<select name="Node[0].registry[0].action" >
				<option value="add_value">Add Value</option>
				<option value="delete_value">Delete Value</option>
				<option value="modify_value">Modify Value</option>
				<option value="add_key">Add Key</option>
				<option value="delete_key">Delete Key</option>
				<option value="modify_key">Modify Key</option>               
				</select>
				</td>
		</tr>
		<tr>
			<td><input type="button" id="addreg" class="addReg" value="Add Key-Value"/> </td>
		</tr>

		
	</table>
	<input type="submit" id="submitRow" value="Generate WatchList" />
</form:form>

</body>
</html>