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

<c:query xmlns:c="http://www.w3.org/ns/xproc-step"><![CDATA[

declare namespace iodef-rid="urn:ietf:params:xml:ns:iodef-rid-2.0";
declare namespace iodef="urn:ietf:params:xml:ns:iodef-1.42";
declare namespace ridagent="http://www.emc.com/cto/ridagent";
declare namespace agent="http://www.emc.com/cto/agent";

declare variable $input external;


let $query := if (not(empty(.)))
              then .
	    	  else xhive:parse($input)


(: Get the id for this query:)
let $queryid := data($query/*/agent:id)


(: Add query:)
return (xhive:create-library(concat("/", $queryid)), 
        xhive:insert-document(concat("/", $queryid, "/report.xml"), $query), 
	    document { element iodef:IncidentID {
	    				attribute name {"DUMMY-CIRT"}, $query//agent:id[1]/text()}}
	    
	    )
	  
]]></c:query>