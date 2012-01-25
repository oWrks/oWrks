<script type="text/javascript">
<!--
	function deletePerformer(data,userid){
		$("#leftPerformerBox"+userid).slideUp("normal",function() { $(this).remove(); });
	}

	function moveToPerformerBox(data,userid){
		$("#rightPerformerBox"+userid).remove();
		$("#leftPerformerBox").prepend(data);
		$("#leftPerformerBox"+userid).effect('highlight', {}, 2000);
	}

	function declineRequest(data,userid){
		$("#rightPerformerBox"+userid).slideUp("normal",function() { $(this).remove(); });
	}
	
//-->
</script>

<div class="col50percent">
		<div class="box info-box">
		
			<h2 class="box-header">Aktive Performer</h2>
			<app:remoteField style="margin: 10px 0 0 10px;" title="Suche Perfomer" class="inputHelp" name="userName" action="loadPerformersByUserName" paramName="userName" params="${[id: owoInstance?.id, userBox:'leftPerformerBox', showButton:'deletePerformer']}" update="leftPerformerBox"/>
			<div id="Performers">
				<g:render template="userList" model="['userList':performerUserList, 'userBox':'leftPerformerBox', 'showButton':'deletePerformer']"></g:render>
			</div>
		</div>
</div>
<g:if test="${owoInstance?.type != 'private'}">
	<div class="col50percent">
		<div class="box info-box">
			<h2 class="box-header">Offene Anfragen</h2>
			
			<div id="Requesters">
				<g:if test="${requestingUsers.size() == 0}">
						<p>Es liegen keine offenen Anfragen zu diesem oWo vor</p>
				</g:if>
				<g:render template="userList" model="['userList':requestingUsers, 'userBox':'rightPerformerBox', 'showButton':'confirmRequestAjax']"></g:render>
			</div>
		</div>
	</div>
</g:if>