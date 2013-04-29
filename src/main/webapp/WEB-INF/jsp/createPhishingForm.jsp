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
<title>Malware</title>
</head>
<body>
<script type="text/javascript">
$(document).ready(function() {
	function malware () {
	    this.ipaddresspos = 0;
	    this.hashpos=0;
	    this.filepos=[];
	    this.ipanother=[];
	    this.signaturepos=0;
	    this.phishingpos=0;
	    this.phishinganother=[];
	    this.phishingemail=[];
	    
	}
	var instance= new malware();
	instance.ipanother[0]=0;
	instance.phishinganother[0]=0;
	instance.phishingemail[0]=0;
	instance.filepos[0]=0;
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
    		appendTxt=appendTxt+"<td>System Category:<select name=\"Node["+index+"].address["+ Position +"].systemcategory\"> <option value=\"source\">Source</option> <option value=\"target\">Target</option> </select>";
    		appendTxt=appendTxt+"Node Role:<select name=\"Node["+index+"].address["+ Position +"].role\"> <option value=\"\">--Please Select</option> <option value=\"c2-server\">Command and Control Server</option> <option value=\"malware-distribution\">Malware Distribution</option><option value=\"dns-spoof\">DNS Spoof</option><option value=\"phishing\">Phishing</option><option value=\"spear-phishing\">Spear Phishing</option><option value=\"other\">Other</option> </select>";
    		appendTxt=appendTxt+"Node Category:<select name=\"Node["+index+"].address["+ Position +"].category\"> <option value=\"server-internal\">Internal Server</option> <option value=\"server-public\">Public Server</option> <option value=\"www\">WWW</option><option value=\"mail\">Mail</option><option value=\"ext-value\">Ext-Value</option> </select></td></tr>";
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
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].address["+ Position +"].system["+ anotherIP +"].type\"> <option value=\"ipv4-addr\">IPV4</option> <option value=\"ipv6-addr\">IPV6</option> <option value=\"site-uri\">URL</option><option value=\"ipv4-net\">IPV4 Net Block</option><option value=\"name\">Name</option><option value=\"asn\">ASN</option><option value=\"ext-value\">ext-value</option> </select>";
    		appendTxt=appendTxt+"<input type=\"button\" id=\""+Position+"\" data-row=\""+ anotherIP +"\" class=\"addService\"  value=\"Add Service\" /> </td>";
    		appendTxt = appendTxt+"</tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);
	});
	
	
	
	$(document).on('click',".addemailIP",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		arr[index].phishingpos++;
		var Position=arr[index].phishingpos;
		arr[index].phishinganother[Position]=0;
		arr[index].phishingemail[Position]=0;
		//alert(Position);
		    var appendTxt = "<tr>";
    		appendTxt=appendTxt+"<td>System Category:<select name=\"Node["+index+"].phishing["+ Position +"].systemcategory\"> <option value=\"source\">Source</option> <option value=\"target\">Target</option> </select>";
    		appendTxt=appendTxt+"Node Role:<select name=\"Node["+index+"].phishing["+ Position +"].role\"> <option value=\"\">--Please Select</option> <option value=\"c2-server\">Command and Control Server</option> <option value=\"malware-distribution\">Malware Distribution</option><option value=\"dns-spoof\">DNS Spoof</option><option value=\"phishing\">Phishing</option><option value=\"spear-phishing\">Spear Phishing</option><option value=\"other\">Other</option> </select>";
    		appendTxt=appendTxt+"Node Category:<select name=\"Node["+index+"].phishing["+ Position +"].category\"> <option value=\"server-internal\">Internal Server</option> <option value=\"server-public\">Public Server</option> <option value=\"www\">WWW</option><option value=\"mail\">Mail</option><option value=\"ext-value\">Ext-Value</option> </select></td></tr>";
    		appendTxt = appendTxt+"<tr><td><input id=\"Node"+ index + ".phishing" + Position + ".system"+ 0 +".value\" name=\"Node["+ index +"].phishing[" + Position + "].system["+ 0 +"].value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].phishing["+ Position +"].system["+ 0 +"].type\"> <option value=\"ipv4-addr\">IPV4</option> <option value=\"ipv6-addr\">IPV6</option> <option value=\"site-uri\">URL</option><option value=\"ipv4-net\">IPV4 Net Block</option><option value=\"name\">Name</option><option value=\"asn\">ASN</option><option value=\"ext-value\">ext-value</option> </select>";
    		appendTxt=appendTxt+"<input type=\"button\" id=\""+Position+"\" data-row=\""+ 0 +"\" class=\"addemailService\"  value=\"Add Service\" /> </td>";
    		appendTxt = appendTxt+"</tr>";
    		appendTxt=appendTxt+"<tr><td><input type=\"button\" id=\""+ Position +"\" class=\"addemailAddress\"  value=\"Add Address\" /> </td></tr>";
    		appendTxt=appendTxt+"<tr><td><input type=\"button\" id=\""+ Position +"\" class=\"addemailDomain\"  value=\"Add Email & Domain Data\" /> </td></tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);
	});
	$(document).on('click',".addemailService",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		var Position=Number($(this).attr("id"));	
		var IPPosition=Number($(this).attr("data-row"));
		//alert(Position);
    		var appendTxt="<select name=\"Node["+index+"].phishing["+ Position +"].system["+IPPosition+"].protocolno\"> <option value=\"6\">TCP</option> <option value=\"17\">UDP</option></select>";
    		appendTxt =appendTxt+"<input id=\"Node"+ index + ".phishing" + Position + ".system"+ IPPosition +".portno\" name=\"Node["+ index +"].phishing[" + Position + "].system["+IPPosition+"].portno\" size=\"40\" />";
    		//alert(appendTxt);
			$(this).before(appendTxt);
			$(this).hide();
	});
	$(document).on('click',".addemailAddress",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		//var Position=arr[index].hashpos;
		var Position=Number($(this).attr("id"));
		arr[index].phishinganother[Position]++;
		var anotherIP=arr[index].phishinganother[Position];		
		//alert(Position);
    		var appendTxt = "<tr>";
    		appendTxt = appendTxt+"<td><input id=\"Node"+ index + ".phishing" + Position + ".system"+ anotherIP +".value\" name=\"Node["+ index +"].phishing[" + Position + "].system["+ anotherIP +"].value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].phishing["+ Position +"].system["+ anotherIP +"].type\"> <option value=\"ipv4-addr\">IPV4</option> <option value=\"ipv6-addr\">IPV6</option> <option value=\"site-uri\">URL</option><option value=\"ipv4-net\">IPV4 Net Block</option><option value=\"name\">Name</option><option value=\"asn\">ASN</option><option value=\"ext-value\">ext-value</option> </select>";
    		appendTxt=appendTxt+"<input type=\"button\" id=\""+Position+"\" data-row=\""+ anotherIP +"\" class=\"addemailService\"  value=\"Add Service\" /> </td>";
    		appendTxt = appendTxt+"</tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);
	});
	
	
	
	$(document).on('click',".addemailDomain",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		//var Position=arr[index].hashpos;
		var Position=Number($(this).attr("id"));
		var anotherIP=arr[index].phishingemail[Position];
		arr[index].phishingemail[Position]++;		
		//alert(Position);
    		var appendTxt = "<tr>";
    		appendTxt = appendTxt+"<td>Email ID:<input id=\"Node"+ index + ".phishing" + Position + ".emailinfo"+ anotherIP +".emailid\" name=\"Node["+ index +"].phishing[" + Position + "].emailinfo["+ anotherIP +"].emailid\" size=\"40\" />";
    		appendTxt = appendTxt+"Email Subject:<input id=\"Node"+ index + ".phishing" + Position + ".emailinfo"+ anotherIP +".subject\" name=\"Node["+ index +"].phishing[" + Position + "].emailinfo["+ anotherIP +"].subject\" size=\"40\" />";
    		appendTxt = appendTxt+"Mailer:<input id=\"Node"+ index + ".phishing" + Position + ".emailinfo"+ anotherIP +".mailerid\" name=\"Node["+ index +"].phishing[" + Position + "].emailinfo["+ anotherIP +"].mailerid\" size=\"40\" /> </td> </tr>";
    		appendTxt = appendTxt+"<tr><td>Domain Name:<input id=\"Node"+ index + ".phishing" + Position + ".emailinfo"+ anotherIP +".domain\" name=\"Node["+ index +"].phishing[" + Position + "].emailinfo["+ anotherIP +"].domain\" size=\"40\" />";
    		appendTxt = appendTxt+"Domain Checked Date:{YY:MM:DD:HH:MI:SE}<input id=\"Node"+ index + ".phishing" + Position + ".emailinfo"+ anotherIP +".domaindate\" name=\"Node["+ index +"].phishing[" + Position + "].emailinfo["+ anotherIP +"].domaindate\" size=\"40\" /> </td> </tr>";
    		appendTxt=appendTxt+"<tr><td>DNS Record Type:<select name=\"Node["+index+"].phishing["+ Position +"].emailinfo["+ anotherIP +"].dns["+ 0 +"].type\"> <option value=\"\">--Please Select</option> <option value=\"MX\">MX</option> <option value=\"SPF\">SPF</option><option value=\"A\">A</option><option value=\"TXT\">TXT</option> </select>";
    		appendTxt=appendTxt+"Value:<input id=\"Node"+ index + ".phishing" + Position + ".emailinfo"+ anotherIP +".dns"+ 0 +".value\" name=\"Node["+ index +"].phishing[" + Position + "].emailinfo["+ anotherIP +"].dns["+ 0 +"].value\" size=\"40\" />";
    		appendTxt = appendTxt+"</td></tr>";
    		appendTxt=appendTxt+"<tr><td>DNS Record Type:<select name=\"Node["+index+"].phishing["+ Position +"].emailinfo["+ anotherIP +"].dns["+ 1 +"].type\"> <option value=\"\">--Please Select</option> <option value=\"MX\">MX</option> <option value=\"SPF\">SPF</option><option value=\"A\">A</option><option value=\"TXT\">TXT</option> </select>";
    		appendTxt=appendTxt+"Value:<input id=\"Node"+ index + ".phishing" + Position + ".emailinfo"+ anotherIP +".dns"+ 1 +".value\" name=\"Node["+ index +"].phishing[" + Position + "].emailinfo["+ anotherIP +"].dns["+ 1 +"].value\" size=\"40\" />";
    		appendTxt = appendTxt+"</td></tr>";
    		appendTxt=appendTxt+"<tr><td>DNS Record Type:<select name=\"Node["+index+"].phishing["+ Position +"].emailinfo["+ anotherIP +"].dns["+ 2 +"].type\"> <option value=\"\">--Please Select</option> <option value=\"MX\">MX</option> <option value=\"SPF\">SPF</option><option value=\"A\">A</option><option value=\"TXT\">TXT</option> </select>";
    		appendTxt=appendTxt+"Value:<input id=\"Node"+ index + ".phishing" + Position + ".emailinfo"+ anotherIP +".dns"+ 2 +".value\" name=\"Node["+ index +"].phishing[" + Position + "].emailinfo["+ anotherIP +"].dns["+ 2 +"].value\" size=\"40\" />";
    		appendTxt = appendTxt+"</td></tr>";
    		appendTxt=appendTxt+"<tr><td>DNS Record Type:<select name=\"Node["+index+"].phishing["+ Position +"].emailinfo["+ anotherIP +"].dns["+ 3 +"].type\"> <option value=\"\">--Please Select</option> <option value=\"MX\">MX</option> <option value=\"SPF\">SPF</option><option value=\"A\">A</option><option value=\"TXT\">TXT</option> </select>";
    		appendTxt=appendTxt+"Value:<input id=\"Node"+ index + ".phishing" + Position + ".emailinfo"+ anotherIP +".dns"+ 3 +".value\" name=\"Node["+ index +"].phishing[" + Position + "].emailinfo["+ anotherIP +"].dns["+ 3 +"].value\" size=\"40\" />";
    		appendTxt = appendTxt+"</td></tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);
	});	
	
	
	$(document).on('click',".addHash",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		arr[index].hashpos++;
		var Position=arr[index].hashpos;
		arr[index].filepos[Position]=0;
		//alert(Position);
    		var appendTxt = "<tr>";
    		appendTxt = appendTxt+"<td>File Name<input id=\"Node"+ index + ".hash" + Position +  ".file" + 0 + ".filename\" name=\"Node["+ index +"].hash[" + Position + "].file[" + 0 + "].filename\" size=\"40\" />";
    		appendTxt = appendTxt+"File Size<input id=\"Node"+ index + ".hash" + Position + ".file" + 0 + ".filesize\" name=\"Node["+ index +"].hash[" + Position + "].file[" + 0 + "].filesize\" size=\"40\" />";
    		appendTxt=appendTxt+"</td></tr>";
    		appendTxt=appendTxt+"<tr><td>";
    		appendTxt=appendTxt+"<input type=\"button\" id=\""+Position+"\"  class=\"addFile\"  value=\"Add FileInfo\" /> </td>";
    		appendTxt=appendTxt+"</td></tr>";
    		appendTxt =appendTxt+"<tr>";
    		appendTxt = appendTxt+"<td><input id=\"Node"+ index + ".hash" + Position + ".value"+ 0 +".value\" name=\"Node["+ index +"].hash[" + Position + "].value[" + 0 + "].value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].hash["+ Position +"].value["+ 0 +"].hash_type\"> <option value=\"http://www.w3.org/2001/04/xmlenc#md5\">MD5</option> <option value=\"http://www.w3.org/2001/04/xmlenc#sha1\">SHA1</option><option value=\"http://www.w3.org/2001/04/xmlenc#sha256\">SHA256</option> </select>";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].hash["+ Position +"].type\"><option value=\"\">--Please Select</option> <option value=\"file_hash\">File Hash</option> <option value=\"email_hash\">Email_Hash</option> </select></td>";
    		appendTxt = appendTxt+"</tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);			
	});
	$(document).on('click',".addFile",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		var Position=Number($(this).attr("id"));
		//alert(index);
		arr[index].filepos[Position]++;
		var filePosition=arr[index].filepos[Position];
		//alert(Position);
    		var appendTxt = "<tr>";
    		appendTxt = appendTxt+"<td>File Name<input id=\"Node"+ index + ".hash" + Position + ".file" + filePosition + ".filename\" name=\"Node["+ index +"].hash[" + Position + "].file[" + filePosition + "].filename\" size=\"40\" />";
    		appendTxt = appendTxt+"File Size<input id=\"Node"+ index + ".hash" + Position + ".file" + filePosition + ".filesize\" name=\"Node["+ index +"].hash[" + Position + "].file[" + filePosition + "].filesize\" size=\"40\" />";
    		appendTxt=appendTxt+"</td></tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);			
	});
	$(document).on('click',".addSignature",function() {
		var index=Number($(this).parents("table:eq(0)").attr("id"));
		//alert(index);
		arr[index].hashpos++;
		var Position=arr[index].signaturepos;
		//alert(Position);
    		var appendTxt = "<tr>";
    		appendTxt = appendTxt+"<td><input id=\"Node"+ index + ".dsig" + Position + ".hash_value\" name=\"Node["+ index +"].dsig[" + Position + "].hash_value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].dsig["+ Position +"].hash_type\"> <option value=\"http://www.w3.org/2001/04/xmlenc#md5\">MD5</option> <option value=\"http://www.w3.org/2001/04/xmlenc#sha1\">SHA1</option><option value=\"http://www.w3.org/2001/04/xmlenc#sha256\">SHA256</option> </select>";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].dsig["+ Position +"].type\"> <option value=\"\">--Please Select</option> <option value=\"PGP_email_ds\">PGP Email Signature</option><option value=\"PKI_email_ds\">PKI Email Signature</option> </select>";
    		appendTxt=appendTxt+"</td></tr>";
    		appendTxt = "<tr>";
    		appendTxt = appendTxt+"<td>Cert Issuer Name:<input id=\"Node"+ index + ".dsig" + Position + ".issuer\" name=\"Node["+ index +"].dsig[" + Position + "].issuer\" size=\"40\" />";
    		appendTxt = appendTxt+"Serial Number:<input id=\"Node"+ index + ".dsig" + Position + ".issuernumber\" name=\"Node["+ index +"].dsig[" + Position + "].issuernumber\" size=\"40\" />";
    		appendTxt=appendTxt+"</td></tr>";
    		appendTxt =appendTxt+"<tr>";
    		appendTxt = appendTxt+"<td><input id=\"Node"+ index + ".dsig" + Position + ".signature_value\" name=\"Node["+ index +"].dsig[" + Position + "].signature_value\" size=\"40\" />";
    		appendTxt=appendTxt+"<select name=\"Node["+index+"].dsig["+ Position +"].signature_method\"><option value=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha512\">RSA SHA512</option> <option value=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\">RSA SHA1</option> <option value=\"http://www.w3.org/2000/09/xmldsig#dsa-sha1\">DSA SHA1</option> </select>";
    		appendTxt=appendTxt+"Valid ?<select name=\"Node["+index+"].dsig["+ Position +"].validity\"><option value=\"true\">Signature Valid</option> <option value=\"false\">Signature Not Valid</option></select></td>";
    		appendTxt = appendTxt+"</tr>";
    		//alert(appendTxt);
			$(this).parent().parent().before(appendTxt);			
	});
	
});
</script>
<form:form method="post" action="${contextPath}/RIDSender/create/Phishing" name="classForm" id="classForm" commandName="classCommand">
	<table id="0">
		<tr>
			<td>&nbsp;&nbsp;<b>SpearPhising Indicator(s) Sharing</b></td>
		</tr>
		<tr>
				<td>Description<spring:bind path="description">
				<form:textarea path="${status.expression}" rows="5" cols="40"/>
	  			</spring:bind>
	  		</td>
		</tr>
		<tr>
				<td>ReportTime{YY:MM:DD:HH:MI:SE}<spring:bind path="reporttime">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
		<tr>
				<td>StartTime{YY:MM:DD:HH:MI:SE}<spring:bind path="starttime">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
		<tr>
				<td>StopTime{YY:MM:DD:HH:MI:SE}<spring:bind path="stoptime">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
				<tr>
				<td>DetectTime{YY:MM:DD:HH:MI:SE}<spring:bind path="detecttime">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		</td>
		</tr>
		<tr>
			<td>Malicious Node(s)</td>
		</tr>
		<tr>
			<td>
				System Category:<select name="Node[0].address[0].systemcategory" >
				<option value="source">Source</option>
				<option value="target">Target</option>
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
			<td>Phishing Email Data Sharing</td>
		</tr>
		<tr>
			<td>
				System Category:<select name="Node[0].phishing[0].systemcategory" >
				<option value="source">Source</option>
				<option value="target">Target</option>
				</select>
				Node Role:<select name="Node[0].phishing[0].role" >
				<option value="">--Please Select</option>
				<option value="c2-server">Command and Control Server</option>
				<option value="malware-distribution">Malware Distribution</option>
				<option value="dns-spoof">DNS Spoof</option>
				<option value="phishing">Phishing</option>
				<option value="spear-phishing">Spear Phishing</option>
				<option value="other">Other</option>
				</select>
	  			Node Category:<select name="Node[0].phishing[0].category" >
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
				<spring:bind path="Node[0].phishing[0].system[0].value">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
				<select name="Node[0].phishing[0].system[0].type" >
				<option value="ipv4-addr">IPV4</option>
				<option value="ipv6-addr">IPV6</option>
				<option value="site-uri">URL</option>
				<option value="ipv4-net">IPV4 Net Block</option>
				<option value="name">Name</option>
				<option value="asn">ASN</option>
				<option value="ext-value">ext-value</option>                          
				</select>
				<input type="button" id="0" data-row="0" class="addemailService" value="Add Service"/>      
			</td>
		</tr>
		<tr><td><input type="button" id="0" class="addemailAddress" value="Add Address"/> </td></tr>
		<tr> <td><input type="button" id="0" class="addemailDomain" value="Add Email & Domain Data"/></td></tr>		
		<tr>
			<td><input type="button" id="add" class="addemailIP" value="Add Node"/> </td>
		</tr>
		
		<tr>
			<td>Attachment Hashe(s)</td>
		</tr>
		<tr>
		<td> File Name
			   	<spring:bind path="Node[0].hash[0].file[0].filename">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  		File Size:<spring:bind path="Node[0].hash[0].file[0].filesize">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>    
		</td>
		</tr>
		<tr>
		<td><input type="button" id="0" class="addFile" value="Add FileInfo"/></td>
		</tr>
		<tr>
			<td>
				<spring:bind path="Node[0].hash[0].hash_value">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  			<select name="Node[0].hash[0].hash_type" >
				<option value="http://www.w3.org/2001/04/xmlenc#md5">MD5</option>
				<option value="http://www.w3.org/2001/04/xmlenc#sha1">SHA1</option>
				<option value="http://www.w3.org/2001/04/xmlenc#sha256">SHA256</option>     
				</select>
				<select name="Node[0].hash[0].type" >
				<option value="">--Please Select</option>
				<option value="file_hash">File Hash</option>
				<option value="email_hash">Email_Hash</option>
				</select>
			</td>
		</tr>
		<tr>
			<td><input type="button" id="addhash" class="addHash" value="Add Hash"/> </td>
		</tr>
		
		<tr>
			<td>Email Digital Signature(s)</td>
		</tr>
		<tr>
			<td>
				<spring:bind path="Node[0].dsig[0].hash_value">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  			<select name="Node[0].dsig[0].hash_type" >
				<option value="http://www.w3.org/2001/04/xmlenc#md5">MD5</option>
				<option value="http://www.w3.org/2001/04/xmlenc#sha1">SHA1</option>
				<option value="http://www.w3.org/2001/04/xmlenc#sha256">SHA256</option>     
				</select>
				<select name="Node[0].dsig[0].type" >
				<option value="">--Please Select</option>
				<option value="PGP_email_ds">PGP Email Signature</option>
				<option value="PKI_email_ds">PKI Email Signature</option>
				</select>
			</td>
		</tr>
		<tr>
		<td>
				Cert Issuer Name:<spring:bind path="Node[0].dsig[0].issuer">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  			Serial Number:<spring:bind path="Node[0].dsig[0].issuernumber">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
		</td>
		</tr>
		<tr>
			<td>
				<spring:bind path="Node[0].dsig[0].signature_value">
					<form:input path="${status.expression}" size="40" />
	  			</spring:bind>
	  			<select name="Node[0].dsig[0].signature_method" >
				<option value="http://www.w3.org/2001/04/xmldsig-more#rsa-sha512">RSA SHA512</option>
				<option value="http://www.w3.org/2000/09/xmldsig#rsa-sha1">RSA SHA1</option>
				<option value="http://www.w3.org/2000/09/xmldsig#dsa-sha1">DSA SHA1</option>     
				</select>
	  			Valid ?<select name="Node[0].dsig[0].validity" >
				<option value="true">Signature Valid</option>
				<option value="false">Signature Not Valid</option>    
				</select>
			</td>
		</tr>
		<tr>
			<td><input type="button" class="addSignature" value="Add Signature"/> </td>
		</tr>		

		
	</table>
	<input type="submit" id="submitRow" value="Generate SpearPhish Report" />
</form:form>

</body>
</html>