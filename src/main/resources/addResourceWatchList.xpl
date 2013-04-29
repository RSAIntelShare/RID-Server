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
 
 
 <p:library xmlns:p="http://www.w3.org/ns/xproc"
	 xmlns:c="http://www.w3.org/ns/xproc-step"
	 xmlns:ridagent="http://www.emc.com/cto/ridagent"
	 xmlns:ridagent-q="http://www.emc.com/cto/ridagent/xproc"
	 xmlns:iodef-rid="urn:ietf:params:xml:ns:iodef-rid-2.0"
	 xmlns:agent="http://www.emc.com/cto/agent"
	 
	 version="1.0">

<p:declare-step type="ridagent-q:addResourceWatchList" name="addResourceWatchList">
	

	<p:input port='xqueryscript'/>

	 
	<p:input port="xqueryParameters" kind="parameter"/>
	<!-- p:input port="stylesheet"/-->
	<!-- p:input port="stylesheetParameters" kind="parameter"/-->
	<p:input port="source"/>
	<p:option name="iDAssignmentxPath"/>

	<p:output port='result' primary='true'/>
	
	<!--
	<p:output port='result' primary='true'>
		<p:pipe step="xslt" port="result"/>
	</p:output>
	-->
	<!--
	<p:output port='headers' sequence='true'>
		<p:pipe step="locXML" port="result"/>
	</p:output>
	-->
	
 <!-- This pipeline will execute the xquery passed in, against the XML document
	     passed in.  That input XML document is the resource representation.  The xQuery then
	     will have the xDB URI embedded because ultimately the new resource representation will 
	     be inserted into xDB.  
	     Before the input XML document is passed into the xQuery, however, some pre-processing to
	     assign a unique ID to the resource is applied.  The p:uuid step is used for this and the
	     xPath expression used in the p:uuid step is passed into this pipeline in the idAssignmentxPath
	     option. (note, there is currently a bug in calumet that makes it so the idAssignmentxPath is 
	     fixed in the pipeline itself)
	     When the xQuery has been executed, writing the resource into the database, the result is
	     enhanced with hyperlinks to related resources. 
	     The xquery is passed in, as is the xslt for insertion of hyperlinks -->
	     
 <!-- wrap with parent element so an id can be added. Need to specify the wrapper element here -->
 
 
 		<!--<p:wrap-sequence name="wrapiodefriddoc" wrapper="XMLDocument" wrapper-prefix="iodef-rid" wrapper-namespace="urn:ietf:params:xml:ns:iodef-rid-2.0">
			<p:input port='source'>
				<p:pipe step='addResourceWatchList' port='source'/>
			</p:input>
		</p:wrap-sequence>
				<p:add-attribute name="adddtype" attribute-name="dtype" attribute-value="xml" match="//iodef-rid:XMLDocument">
					<p:input port='source'>
				<p:pipe step='wrapiodefriddoc' port='result'/>
			</p:input>
		</p:add-attribute>
						<p:wrap-sequence name="wrapreportschema" wrapper="ReportSchema" wrapper-prefix="iodef-rid" wrapper-namespace="urn:ietf:params:xml:ns:iodef-rid-2.0">
			<p:input port='source'>
				<p:pipe step='adddtype' port='result'/>
			</p:input>
		</p:wrap-sequence>
						<p:wrap-sequence name="wrapridpolicy" wrapper="RIDPolicy" wrapper-prefix="iodef-rid"  wrapper-namespace="urn:ietf:params:xml:ns:iodef-rid-2.0">
			<p:input port='source'>
				<p:pipe step='wrapreportschema' port='result'/>
			</p:input>
		</p:wrap-sequence>
		<p:add-attribute name="addition" attribute-name="MsgType" attribute-value="Report" match="//iodef-rid:RIDPolicy">
					<p:input port='source'>
				<p:pipe step='wrapridpolicy' port='result'/>
			</p:input>
		</p:add-attribute>
				<p:add-attribute name="addDest" attribute-name="MsgDestination" attribute-value="RIDSystem" match="//iodef-rid:RIDPolicy">
					<p:input port='source'>
				<p:pipe step='addition' port='result'/>
			</p:input>
		</p:add-attribute>
		
		
		
		
								<p:wrap-sequence name="wraprid" wrapper="RID" wrapper-prefix="iodef-rid" wrapper-namespace="urn:ietf:params:xml:ns:iodef-rid-2.0">
			<p:input port='source'>
				<p:pipe step='addDest' port='result'/>
			</p:input>
		</p:wrap-sequence>
				<p:add-attribute name="add" attribute-name="lang" attribute-value="en" match="//iodef-rid:RID">
					<p:input port='source'>
				<p:pipe step='wraprid' port='result'/>
			</p:input>
		</p:add-attribute> -->
		

				<p:wrap-sequence name="wrap" wrapper="RIDAgent" wrapper-prefix="ridagent" wrapper-namespace="http://www.emc.com/cto/ridagent">
			<p:input port='source'>
				<p:pipe step='addResourceWatchList' port='source'/>
			</p:input>
		</p:wrap-sequence>

	     
 <!--  add an element to this resource to hold the server assigned ID - this allows us to get at that assigned ID regardless
	   of the name of the element that has the id assigned - as specified by the $iDAssignmentxPath value passed in -->
	
	     
		<p:insert name="addIdRegister" match="/*" position="first-child">
			<p:input port='source'>
				<p:pipe step='wrap' port='result'/>
			</p:input>
			<p:input port='insertion'>
				<p:inline>
				<agent:id>replace</agent:id>    
				</p:inline>
			</p:input>	
		</p:insert>
	
	

	     
	<!-- assign a uuid to this resource -->
	
	<p:uuid name='uuid'>
		<p:input port='source'>
			<p:pipe step='addIdRegister' port='result'/>
		</p:input>
		<p:with-option name='match' select='concat("*/agent:id/text()","")'  xmlns:agent="http://www.emc.com/cto/agent"/>
	</p:uuid>
	
	
	
	<!-- generate the URL for the newly created resource -->
	
	<!--
	<p:variable name="baseU" select="/c:param-set/c:param[@name='baseURL']/@value">
		<p:pipe step="main" port="stylesheetParameters"/>
	</p:variable>
	<p:string-replace name="locXML" match="/Location/text()">
		<p:input port="source">
			<p:inline>
				<Location>here</Location>
			</p:inline>
		</p:input>
		<p:with-option name='replace' select="concat('&quot;',$baseU, '/', /*/temp:id/text(),'&quot;')" xmlns:temp="http://www.emc.com/cto/xmlrestfw" xmlns:pat="http://www.emc.com/cto/TAXII">
			<p:pipe step='uuid' port='result'/>
		</p:with-option>		
	</p:string-replace>
	-->

	
	<!-- execute xQuery - source is the input resource representation.  The url of the
		 xDB is in the query itself (it is against the root of the database -->
	
  <p:xquery name="xquery">
		<p:input port='source'>
	    	<p:pipe step='uuid' port='result'/>
		</p:input>
		<p:input port="query">
			<p:pipe step="addResourceWatchList" port="xqueryscript" />
		</p:input>
		<p:input port="parameters">
			<p:pipe step="addResourceWatchList" port="xqueryParameters"/>
		</p:input>
	</p:xquery>	
	

	<!-- We check the result of the xQuery to make sure there was no error. -->
	<!-- Try to get the (locally generated) IncidentID back from the xquery.  -->
	<!-- if it is NOT there, then something went wrong, and we send a negative Ack -->
	<!-- if it was OK, then send a positive Ack --> 	
	
	<p:choose name="checkXquery">

		<p:xpath-context>
			<p:pipe step="xquery" port="result"/>
		</p:xpath-context>


		<p:when test="/iodef:IncidentID" xmlns:iodef="urn:ietf:params:xml:ns:iodef-1.42">
		
		<!-- If we got an IncidentID back then the xquery should have returned as expected -->
			
			<p:output port="result">
				<p:pipe step="addId" port="result"/>
			</p:output>
			
			<p:insert name="addId" match="/iodef-rid:RID/iodef-rid:RIDPolicy/iodef-rid:TrafficType" position="after">
			  <p:input port='source'>
				<p:inline>
                  <iodef-rid:RID xmlns:iodef-rid="urn:ietf:params:xml:ns:iodef-rid-2.0"
                       		   xmlns:iodef="urn:ietf:params:xml:ns:iodef-1.42" lang="en">
                    <iodef-rid:RIDPolicy MsgType="Acknowledgment" MsgDestination="RIDSystem">
                      <iodef-rid:PolicyRegion region="IntraConsortium"/>
                      <iodef:Node>
                          <iodef:NodeName>N/A</iodef:NodeName>
                      </iodef:Node>
                      <iodef-rid:TrafficType type="Other"/>
                    </iodef-rid:RIDPolicy>
                    <iodef-rid:RequestStatus AuthorizationStatus="Pending"/>
                  </iodef-rid:RID>
			    </p:inline>
			  </p:input>
			  <p:input port='insertion'>
			    	<p:pipe step="xquery" port="result" />
			  </p:input>	
		    </p:insert>
		</p:when>
		<p:otherwise>
			<p:output port="result">
				<p:pipe step="genError" port="result" />
			</p:output>
			<p:identity name="genError">
				<p:input port="source">
					<p:inline>
                        <iodef-rid:RID xmlns:iodef-rid="urn:ietf:params:xml:ns:iodef-rid-2.0"
                            		   xmlns:iodef="urn:ietf:params:xml:ns:iodef-1.42" lang="en">
                            <iodef-rid:RIDPolicy MsgType="Acknowledgment" MsgDestination="RIDSystem">
                                <iodef-rid:PolicyRegion region="IntraConsortium"/>
                                <iodef:Node>
                                    <iodef:NodeName>N/A</iodef:NodeName>
                                </iodef:Node>
                                <iodef-rid:TrafficType type="Other"/>
                                <iodef:IncidentID name='DUMMY-CIRT'>N/A</iodef:IncidentID>
                            </iodef-rid:RIDPolicy>
                            <iodef-rid:RequestStatus AuthorizationStatus="Denied" Justification="Other"/>
                        </iodef-rid:RID>
					</p:inline>
				</p:input>
			</p:identity>		
		</p:otherwise>
	</p:choose>
	

	<!-- insert hyperlinks -->
	<!--
 	<p:xslt name="xslt">
		<p:input port='source'>
			<p:pipe step='xquery' port='result'/>
		</p:input>
		<p:input port='stylesheet'>
			<p:pipe step='main' port='stylesheet'/>
		</p:input>
  		<p:input port='parameters'>
			<p:empty/>  		
  		</p:input>
  		<p:with-param port='parameters' name='baseURL' select='/Location/text()'>
			<p:pipe step='locXML' port='result'/>		
		</p:with-param>
	</p:xslt>
    -->
</p:declare-step>

</p:library>