<%@ page import="owrks.owo.Owo" %>
<g:if test="${(params.action != 'tag')}">
	<div id="workflowState">
		<h3>oWo ${owoInstance?.title}</h3>
		<owo:renderWorkflowState owo="${owoInstance}" />
		<div class="clear"></div>
	</div>
</g:if>
<nav:renderOwoNav owo="${owoInstance}" />
<div class="clear"></div>