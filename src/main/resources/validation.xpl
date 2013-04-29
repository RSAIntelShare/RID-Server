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

 <p:library xmlns:p="http://www.w3.org/ns/xproc"
	 xmlns:c="http://www.w3.org/ns/xproc-step"
	 xmlns:ridagent="http://www.emc.com/cto/ridagent"
	 xmlns:ridagent-p="http://www.emc.com/cto/ridagent/xproc"
	 xmlns:iodef-rid="urn:ietf:params:xml:ns:iodef-rid-2.0"
	 version="1.0">	
 
  
 	<p:declare-step type="ridagent-p:iodef-rid-schema-validate" name="iodef-rid-schema-validate">

      <p:input port="source" primary="true"/>
      <p:output port="result" primary="true"/>
      
		<p:validate-with-xml-schema  use-location-hints="false" try-namespaces="false" assert-valid="true" mode="strict">
    		<p:input port='source'>
				<p:pipe step='iodef-rid-schema-validate' port='source'/>
    		</p:input>        	 
    		<p:input port="schema">
      			<p:document href="xhive:/schemas/iodef-rid-2.0.xsd"></p:document>
      			<p:document href="xhive:/schemas/iodef-1.42.xsd"></p:document>
      			<p:document href="xhive:/schemas/xmldsig-core-schema.xsd"></p:document>      			
    		</p:input>                       
	      </p:validate-with-xml-schema>

	</p:declare-step>
	
</p:library>