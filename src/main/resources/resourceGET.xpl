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

<p:declare-step name="main" xmlns:p="http://www.w3.org/ns/xproc"
	xmlns:c="http://www.w3.org/ns/xproc-step" version="1.0">
	<p:input port='xqueryscript' />
	<!--p:input port="stylesheet"/-->
	<!--p:input port="stylesheetParameters" kind="parameter"/-->
	<p:input port="xqueryParameters" kind="parameter"/>
	<p:output port='result' sequence='true' primary='true' />
	<p:output port='error' sequence="true">
		<p:pipe step='checkXquery' port='error' />
	</p:output>
	
	<!-- This pipeline will execute the xquery passed in, against the root of the XML database and will
	     take the result and enhance it with hyperlinks to related resources. 
	     The xquery is passed in, as is the xslt for insertion of hyperlinks -->

	<!-- execute xQuery against the root of the database -->
	<p:xquery name="xquery">
		<p:input port='source'>
			<p:document href="xhive:/" />
		</p:input>
		<p:input port="query">
			<p:pipe step="main" port="xqueryscript" />
		</p:input>
		<p:input port="parameters">
			<p:pipe step='main' port='xqueryParameters'/>
		</p:input>
	</p:xquery>

	<!-- check the result of the xQuery to make sure there was no error -->
	<p:choose name="checkXquery">
		<p:variable name="error" select="/error/code">
			<p:pipe step="xquery" port="result" />
		</p:variable>
		<!-- in case of error, return error xml out of pipeline -->
		<p:when test="$error">
			<p:output port="error">
				<p:pipe step="genError" port="result" />
			</p:output>
			<p:output port="result" sequence='true' primary="true">
				<p:empty />
			</p:output>
			<p:string-replace name="genError" match="/error/code/text()">
				<p:input port="source">
					<p:inline>
						<error><code>err</code><description>description</description></error>
					</p:inline>
				</p:input>
				<p:with-option name='replace' select="$error" />
			</p:string-replace>
		</p:when>
		<p:otherwise>
			<p:output port="error" sequence="true">
				<p:empty />
			</p:output>
			<p:output port="result" sequence='true' primary="true">
					<p:pipe step='default' port='result'/>
			</p:output>
			
			<p:identity name="default">
     		<p:input port="source">
     		<p:pipe step='xquery' port='result'/>
     		</p:input>
     		</p:identity>
     		
 

			<!-- insert hyperlinks -->
			<!--
			<p:xslt>
				<p:input port='source'>
					<p:pipe step='xquery' port='result'/>
				</p:input>
				<p:input port='stylesheet'>
					<p:pipe step='main' port='stylesheet'/>
				</p:input>
				<p:input port='parameters'>
					<p:pipe step='main' port='stylesheetParameters'/>
				</p:input>
			</p:xslt>
			-->
		</p:otherwise>
	</p:choose>
	
</p:declare-step>
