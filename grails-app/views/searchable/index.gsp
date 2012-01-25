<%@ page import="org.springframework.util.ClassUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.SearchableUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.searchable.util.StringQueryUtils" %>
<html>
	<head>
<!--  
* This file is part of oWrks.
*
* oWrks is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
*  any later version.
*
* oWrks is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with oWrks.  If not, see <http://www.gnu.org/licenses/>.
-->
	    <meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	    <meta name="layout" content="main" />
	    <title>Suche nach: <g:if test="${params.q && params.q?.trim() != ''}">${params.q}</g:if></title>
	    <script type="text/javascript">
	        var focusQueryInput = function() {
	            document.getElementById("q").focus();
	        }
	    </script>
  	</head>
  	<body onload="focusQueryInput();">
	    <g:set var="haveQuery" value="${params.q?.trim()}" />
	    <g:set var="haveResults" value="${searchResult?.results}" />
		
		<g:if test="${searchResult?.suggestedQuery}">
		    <div class="title">
		       <p><strong>Kein oWo gefunden</strong>. Meinten Sie vielleicht: <g:link controller="searchable" action="index" params="[q: searchResult.suggestedQuery]"><%= StringQueryUtils.highlightTermDiffs(params.q.trim(), searchResult.suggestedQuery) %></g:link></p>
		    </div>
	    </g:if>
	
	    <g:if test="${parseException}">
	      <g:render template="parseException" model=" ['params': params ]" />
	    </g:if>
		
		<g:if test="${haveQuery && haveResults}">
			<div class="title">
		        <p><g:message code = "search.showResult1" default="Showing" /> <strong>${searchResult.offset + 1}</strong> - <strong>${searchResult.results.size() + searchResult.offset}</strong> <g:message code = "search.showResult2" default="of" /> <strong>${searchResult.total}</strong>
		        	<g:message code = "search.showResult3" default="results for" /> <strong>${params.q}</strong></p>
		        <g:render template="owoList" model="['results': searchResult.results]"></g:render>
				<div class="paging">
					<g:if test="${haveResults}">
					     <p><g:message code = "page" default = "Page" />:
						     <g:set var="totalPages" value="${Math.ceil(searchResult.total / searchResult.max)}" />
						     <g:if test="${totalPages == 1}">
						     	<span class="currentStep">1</span>
						     </g:if>
						     <g:else>
						     	<g:paginate controller="searchable" action="index" params="[q: params.q]" total="${searchResult.total}" prev="&lt; previous" next="next &gt;"/>
						     </g:else>
				     	 </p>
					</g:if>
				</div>
		    </div>
	    </g:if>
	    <g:else>
	    	<div class="title">
	    	<p>Ansonsten gibt es vielleicht ein passendes oWo zu deinem Suchbegriff?</p>
	    	</div>
	    	<g:render template="owoList" model="['results': suggestedOwos]"></g:render>
	    </g:else>

  </body>
</html>