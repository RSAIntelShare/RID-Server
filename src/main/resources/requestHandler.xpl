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
    xmlns:c="http://www.w3.org/ns/xproc-step" xmlns:ridagent-p="http://www.emc.com/cto/ridagent/xproc" xmlns:agent="http://www.emc.com/cto/agent"
    version="1.0">

    <p:input port="source"/>
    
    <p:output port="result" primary="true" sequence="true">
    	<p:pipe step="RIDMessageType" port='result'/>    
    </p:output>
    
    <p:output port="error" sequence="true">
    	<p:pipe step="RIDMessageType" port='error'/>
    </p:output>
    

    <p:input port="addReportXqueryScript"/>
    <p:input port="addQueryXqueryScript"/>
	<p:input port="addWatchXqueryScript"/>

    <p:input port="xqueryParameters" kind="parameter"/>


    <!-- import message processing pipelines-->

    <p:import href="classpath:addResource.xpl"/>
    <p:import href="classpath:validation.xpl"/>

    <!-- Schema Validation of the message -->
     

    <p:try>
        <p:group>
            <ridagent-p:iodef-rid-schema-validate name="iodef-rid-schema-validate">
                <p:input port="source">
                    <p:pipe step="main" port="source"/>
                </p:input>
            </ridagent-p:iodef-rid-schema-validate>
        </p:group>
        <p:catch>  
        	<!-- There has been a schema validation failure.  Ignore the inbound message (which is in some way malformed) -->
        	<!-- We substitute an Acknowledgment message template, and use the @MsgType="ext-value" to bypass the persistence steps -->
        	<!-- and take the "otherwise" path in the choose step -->
            <p:identity>
                <p:input port="source">
                    <p:inline>
                        <iodef-rid:RID xmlns:iodef-rid="urn:ietf:params:xml:ns:iodef-rid-2.0"
                            		   xmlns:iodef="urn:ietf:params:xml:ns:iodef-1.42" lang="en">
                            <iodef-rid:RIDPolicy MsgType="ext-value" MsgDestination="RIDSystem">
                                <iodef-rid:PolicyRegion region="IntraConsortium"/>
                                <iodef:Node>
                                    <iodef:NodeName>N/A</iodef:NodeName>
                                </iodef:Node>
                                <iodef-rid:TrafficType type="Other"/>
                                <iodef:IncidentID name="DUMMY-CIRT">N/A</iodef:IncidentID>
                            </iodef-rid:RIDPolicy>
                            <iodef-rid:RequestStatus AuthorizationStatus="Denied" Justification="UnrecognizedFormat"/>
                        </iodef-rid:RID>
                    </p:inline>
                </p:input>
            </p:identity>
        </p:catch>
    </p:try>


    <!-- Validate the digital signature & decrypt the incoming request-->
    <!-- Pending -->
    <!-- Will become a library call -->



    <!-- Check for RID message type and route to sub pipeline -->
    <!-- For some reason the p:choose can't directly pipe from the input of the main step so need an intermediate one-->
    <p:identity name="forChoose">
        <!-- p:input port="source" -->
            <!-- p:pipe step="iodef-rid-schema-validate" port="result"/ -->
            <!-- p:pipe step='main' port='source'/ -->
        <!-- /p:input -->
    </p:identity>


    <!-- Validate the digital signature & decrypt the incoming request-->
    <!-- Pending -->
    <!-- Will become a library call -->



    <!-- Check for RID message type and route to sub pipeline -->
    <!-- For some reason the p:choose can't directly pipe from the input of the main step so need an intermediate one-->


    <p:choose name="RIDMessageType">
        <p:when test="//*[@MsgType='InvestigationRequest']">

            <p:insert name="addIdRegister" match="/*" position="first-child">
                <p:input port="source">
                    <p:pipe step="main" port="source"/>
                </p:input>
                <p:input port="insertion">
                    <p:inline>
                        <agent:id xmlns:agent="http://www.emc.com/cto/agent"
                            >TRACE_REQUEST</agent:id>
                    </p:inline>
                </p:input>
            </p:insert>
            <p:output port="result" primary="true" sequence="true">
                <p:pipe step="addIdRegister" port="result"/>
            </p:output>
            <p:output port="error" sequence="true">
                <p:empty/>
            </p:output>
            
        </p:when>

        <p:when test="//*[@MsgType='TraceRequest']">
            <p:insert name="addIdRegister" match="/*" position="first-child">
                <p:input port="source">
                    <p:pipe step="main" port="source"/>
                </p:input>
                <p:input port="insertion">
                    <p:inline>
                        <agent:id xmlns:agent="http://www.emc.com/cto/agent"
                            >TRACE_REQUEST</agent:id>
                    </p:inline>
                </p:input>
            </p:insert>
            <p:output port="result" primary="true" sequence="true">
                <p:pipe step="addIdRegister" port="result"/>
            </p:output>
            <p:output port="error" sequence="true">
                <p:empty/>
            </p:output>

        </p:when>

        <p:when test="//*[@MsgType='Query']">
            <ridagent-p:addResource name="addResource">
                <p:input port="source">
                    <p:pipe step="main" port="source"/>
                </p:input>
                <p:input port="xqueryscript">
                    <p:pipe step="main" port="addQueryXqueryScript"/>
                </p:input>
                <p:input port="xqueryParameters">
                    <p:pipe step="main" port="xqueryParameters"/>
                </p:input>
            </ridagent-p:addResource>
            <p:output port="result" primary="true" sequence="true">
                <p:pipe step="addResource" port="result"/>
            </p:output>
            <p:output port="error" sequence="true">
                <p:empty/>
            </p:output>

        </p:when>

        <p:when test="//*[@MsgType='Acknowledgement']">
            <p:insert name="addIdRegister" match="/*" position="first-child">
                <p:input port="source">
                    <p:pipe step="main" port="source"/>
                </p:input>
                <p:input port="insertion">
                    <p:inline>
                        <agent:id xmlns:agent="http://www.emc.com/cto/agent"
                            >ACKNOWLEDGEMENT</agent:id>
                    </p:inline>
                </p:input>
            </p:insert>
            <p:output port="result" primary="true" sequence="true">
                <p:pipe step="addIdRegister" port="result"/>
            </p:output>
            <p:output port="error" sequence="true">
                <p:empty/>
            </p:output>

        </p:when>

        <p:when test="//*[@MsgType='Result']">
            <p:insert name="addIdRegister" match="/*" position="first-child">
                <p:input port="source">
                    <p:pipe step="main" port="source"/>
                </p:input>
                <p:input port="insertion">
                    <p:inline>
                        <agent:id xmlns:agent="http://www.emc.com/cto/agent">RESULT</agent:id>
                    </p:inline>
                </p:input>
            </p:insert>
            <p:output port="result" primary="true" sequence="true">
                <p:pipe step="addIdRegister" port="result"/>
            </p:output>
            <p:output port="error" sequence="true">
                <p:empty/>
            </p:output>

        </p:when>

        <p:when test="//*[@MsgType='Report']">
            <ridagent-p:addResource name="addResource">
                <p:input port="source">
                    <p:pipe step="main" port="source"/>
                </p:input>
                <p:input port="xqueryscript">
                    <p:pipe step="main" port="addReportXqueryScript"/>
                </p:input>
                <p:input port="xqueryParameters">
                    <p:pipe step="main" port="xqueryParameters"/>
                </p:input>
            </ridagent-p:addResource>
            <p:output port="result" primary="true" sequence="true">
                <p:pipe step="addResource" port="result"/>
            </p:output>
            <p:output port="error" sequence="true">
                <p:empty/>
            </p:output>

        </p:when>
		        <p:when test="//*[@MsgType='report']">
            <ridagent-p:addResource name="addResource">
                <p:input port="source">
                    <p:pipe step="main" port="source"/>
                </p:input>
                <p:input port="xqueryscript">
                    <p:pipe step="main" port="addWatchXqueryScript"/>
                </p:input>
                <p:input port="xqueryParameters">
                    <p:pipe step="main" port="xqueryParameters"/>
                </p:input>
            </ridagent-p:addResource>
            <p:output port="result" primary="true" sequence="true">
                <p:pipe step="addResource" port="result"/>
            </p:output>
            <p:output port="error" sequence="true">
                <p:empty/>
            </p:output>

        </p:when>

        <p:otherwise>

            <p:output port="error" sequence="true">
                <p:empty/>
            </p:output>

            <p:output port="result" primary="true" sequence="true">
                <p:pipe step="ack_invalid_message" port="result"/>
            </p:output>
            
            <p:string-replace name="ack_invalid_message" match="@MsgType">
            	<p:with-option name="replace" select="concat('&quot;','Acknowledgment', '&quot;')"/>
            </p:string-replace>

        </p:otherwise>
    </p:choose>

</p:declare-step>