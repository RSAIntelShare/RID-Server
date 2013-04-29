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
 *  @author Hariprasath Mohankumar
*/	
 -->
  
<p:declare-step name="main" xmlns:p="http://www.w3.org/ns/xproc" xmlns:c="http://www.w3.org/ns/xproc-step" xmlns:ridagent-q="http://www.emc.com/cto/ridagent/xproc" version="1.0">
	
		<p:input port="source"/>
	<p:output port="result" primary="true"/>
    
	<p:input port="xqueryscript"/>

	<p:input port="xqueryParameters" kind="parameter"/>
    
    <!-- import message processing pipelines-->
	<p:import href="classpath:addResourceWatchList.xpl"/>
	
	
	<!-- Schema Validation of the message-->
	<!-- Pending -->
	<!-- Will become a library call -->
	
	
	  
	<ridagent-q:addResourceWatchList name="addResourceWatchList">
	 	 <p:input port="source">
			<p:pipe step="main" port="source"/>
		 </p:input>
		 <p:input port="xqueryscript">
			<p:pipe step="main" port="xqueryscript"/>
		</p:input>
		 <p:input port="xqueryParameters">
			<p:pipe step="main" port="xqueryParameters"/>
		</p:input>
	 </ridagent-q:addResourceWatchList>
	 
</p:declare-step>