<%@ page import="org.springframework.util.ClassUtils" %>
<g:each var="result" in="${results}" status="index">
	<div class="box owo-list-item">
		<g:set var="className" value="${ClassUtils.getShortName(result?.getClass())}" />
         <g:set var="link" value="${createLink(controller: className[0].toLowerCase() + className[1..-1], action: 'show', id: result.id)}" />
         <h2>
         	<!--  <a href="${link}">${className} #${result.id}</a>  -->
         	<a href="${link}">${result.title}</a>
         </h2>
		<img src="<g:createLink controller="image" action="owo" id="${result.id}" />"
				alt="${result.title}" class="left" />
         <g:set var="desc" value="${result.goal}" />
         <g:if test="${desc.size() > 120}">
         	<g:set var="desc" value="${desc[0..120] + '...'}" />
         </g:if>

        	<p class="left">
        	${desc.encodeAsHTML()}<br />
        	Erstellt von: <g:link controller="profile" id="${result.owner.id }">${result.owner}</g:link>
        	<br />Freie Pl&auml;tze: ${result.maxPerson}</p>
         <!-- <div class="displayLink">${link}</div>  -->
			<p class="right">
				<al:owoJoinLink owo="${result}"/> 
			</p>
	<div class="clear"></div>
  </div>
</g:each>