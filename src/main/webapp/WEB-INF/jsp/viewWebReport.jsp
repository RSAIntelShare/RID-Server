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

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>RIDSystem - View Web View</title>
</head>
<body>
	<h2>
		<b>View Function</b>
	</h2>

	<p>
		<b>Report View</b>
	</p>
	Description: ${eventdata.description}
	<br>
	Report Time: ${eventdata.reporttime}
		<br>
	Start Time: ${eventdata.starttime}
		<br>
	Stop Time: ${eventdata.stoptime}
		<br>
	Detect Time: ${eventdata.detecttime}
	<ul>
		<c:forEach var="Node" items="${eventdata.node}">
		<b>Event Information:</b>
		<br>
			<c:forEach var="ipaddress" items="${Node.address}">
			<b>System / Network Information:</b>
			<br>
				<br>
			SystemCategory: ${ipaddress.systemcategory}
			<br>
				<br>
			AttackType: ${ipaddress.role} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Category: ${ipaddress.category}
			<br>
				<br>
				<c:forEach var="system" items="${ipaddress.system}">
					<ul>
						<li>Type:
							${system.type}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Value: ${system.value} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Additional Data: &nbsp;&nbsp;&nbsp; Proto No: ${system.protocolno}
							&nbsp;&nbsp;&nbsp; Port No: ${system.portno} &nbsp;&nbsp;&nbsp; User Agent: ${system.useragent}
						</li>
					</ul>
				</c:forEach>
			</c:forEach>
			<br><br><br>
			<c:forEach var="phishing" items="${Node.phishing}">
			<b>System / Network Information:</b>
							<br>
				<br>
			SystemCategory: ${phishing.systemcategory}
			<br>
				<br>
			AttackType: ${phishing.role} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Category: ${phishing.category}
			<br>
				<br>
				<c:forEach var="system" items="${phishing.system}">
					<ul>
						<li>Type:
							${system.type}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Value: ${system.value}  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Additional Data: &nbsp;&nbsp;&nbsp; Proto No: ${system.protocolno}
							&nbsp;&nbsp;&nbsp; Port No: ${system.portno}
						</li>
					</ul>
				</c:forEach>
				<br><br>
				<c:forEach var="emailinfo" items="${phishing.emailinfo}">
				<b>DomainData & EmailInfo:</b>
				<br>
					<br>
				Domain Name: ${emailinfo.domain} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   Date Domain was checked: ${emailinfo.domaindate}
				<br>
					<br>
					<c:forEach var="dns" items="${emailinfo.dns}">
						<ul>
							<li>Record Type: ${dns.type}
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Record Value:
								${dns.value}</li>
						</ul>

					</c:forEach>
					<br>
					<br>
				Email ID: ${emailinfo.emailid} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Subject:  ${emailinfo.subject} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Mailer ID:  ${emailinfo.mailerid}
				<br>
					<br>

				</c:forEach>
			</c:forEach>
			<br><br><br>
			<c:forEach var="hash" items="${Node.hash}">
			<b>Hash Information:</b>
			<br>
				<br>
		Hash Type: ${hash.type}
		<br>
				<br>
				<c:forEach var="value" items="${hash.value}">
					<ul>
						<li>Hash Algorithm:
							${value.hash_type}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							Value: ${value.value}</li>
					</ul>
				</c:forEach>

			</c:forEach>
			<br>
			<br>
			<br>
			<c:forEach var="dsig" items="${Node.dsig}">
			<b>Signature Information:</b>
			<br>
			Type: ${dsig.type}
			<br>
			Signature Method:  ${dsig.signature_method}
			<br>
			Signature Value:  ${dsig.signature_value}
			<br>
			Digest Method:  ${dsig.hash_type}
			<br>
			Digest Value:  ${dsig.hash_value}  
			<br> 
			X509 Issuer CA : ${dsig.issuer} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    X509 Serial Number:  ${dsig.issuernumber}
			<br><br>
			</c:forEach>
			<br><br><br>
			<c:forEach var="registry" items="${Node.registry}">
			<b>Windows Registry Entries:</b>
				<ul>
					<li>Action:
						${registry.action}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Key:
						${registry.key}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Value:
						${registry.value}</li>
				</ul>
			</c:forEach>
		</c:forEach>
	</ul>
	<br>
	<a href="${contextPath}/RIDSender">Home</a>
</body>
</html>