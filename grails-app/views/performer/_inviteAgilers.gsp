<script type="text/javascript">
<!--
	function moveToRight(data,userid){
		$("#rightInviteBox").append(data);
		$("#leftInviteBox"+userid).slideUp("normal",function() { $(this).remove(); });
		$("#rightInviteBox"+userid).effect('highlight', {}, 2000);
	}

	function moveToLeft(data,userid){
		$("#rightInviteBox"+userid).remove();
		$("#leftInviteBox").prepend(data);
		$("#leftInviteBox"+userid).effect('highlight', {}, 2000);
	}
//-->
</script>

<div class="col50percent">
		<div class="box info-box">
			<h2 class="box-header">Lade oWrker zu deinem oWo ein</h2>
			<app:remoteField style="margin: 10px 0 0 10px;" title="Suche oWrker" class="inputHelp" name="userName" action="loadUsersByUserName" paramName="userName" params="${[id: owoInstance?.id, userBox:'leftInviteBox', showButton:'invite']}" update="leftInviteBox"/>
			<div id="notInvitedUsers">
				<g:render template="userList" model="['userList':notInvitedUsers, 'userBox':'leftInviteBox', 'showButton':'invite']"></g:render>
			</div>
		</div>
</div>
<div class="col50percent">
		<div class="box info-box">
			<h2 class="box-header">Eingeladene oWrker</h2>
			
			<div id="invitedUsers">
				<g:render template="userList" model="['userList':invitedUsers, 'userBox':'rightInviteBox', 'showButton':'unInvite']"></g:render>
			</div>
		</div>
</div>


