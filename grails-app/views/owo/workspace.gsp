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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><g:message code="owo.workspace.title" default="Arbeitsumgebung" /></title>
		<meta name="layout" content="main" />
		<g:javascript>
			
		</g:javascript>
		<jq:jquery>
			$('#etherpad').load(function(){
		        $('#etherpad').contents().find('#topbar').hide();
		    });
		</jq:jquery>
	</head>
<body>
	<div class="box">
		<iframe id="etherpad" src="http://titanpad.com/${owoInstance?.hashTag?.replace('#','')}" name="etherpad" width="920" height="600">
			<p>iFrame ist not supported</p>
		</iframe>
	</div>
</body>
</html>