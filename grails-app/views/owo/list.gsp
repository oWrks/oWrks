<%@ page import="owrks.owo.Owo" %>
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
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'owo.label', default: 'Owo')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <jq:jquery>
        	$(document).ready(function(){
        	
        		// hide first layout-switch image
        		$('img.switchLayout:last').hide();
        		
			    $('a#all').click(function(){
			    	if(!$(this).hasClass('down')) {
				        $(this).toggleClass("down");
			        	$('a#public').toggleClass("down", !$(this).hasClass('down'));
			        	$('a#community').toggleClass("down", !$(this).hasClass('down'));
			        }
			    });
			    
			    $('a#public').click(function(){
			    	if(!$(this).hasClass('down')) {
				        $(this).toggleClass("down");
				        $('a#all').toggleClass("down", !$(this).hasClass('down'));
			        	$('a#community').toggleClass("down", !$(this).hasClass('down'));
			        }
			    });
			    
			    $('a#community').click(function(){
			    	if(!$(this).hasClass('down')) {
				        $(this).toggleClass("down");
				        $('a#public').toggleClass("down", !$(this).hasClass('down'));
			        	$('a#all').toggleClass("down", !$(this).hasClass('down'));
			        }
			    });
			});
			
			$('img.switchLayout').click(function(){
				$('div#gridList').toggle();
				$('div#tableList').toggle();
				$('ul.filter').toggle();
				$('img.switchLayout').toggle();
			});
        </jq:jquery>
    </head>
    <body>
        <div class="list box">
            <h2 class="info-list-header"><g:message code="default.list.label" args="[entityName]" default="${owoInstanceName}" /> <g:link controller="owo" action="create" class="button right">oWo erstellen</g:link></h2>
            <div id="owoFilter">
	            <span style="float:left;">oWos filtern:</span>
	            <ul style="display: ${listLayout == 'list' ? 'block' : 'none'}" class="filter">
		        	<li><g:remoteLink elementId="all" name="all" title="Alle" action="list" update="tableListContent" params="${[id: 1, listLayout: 'list']}" class="down"><g:message code="owo.list.all" default="Alle"/></g:remoteLink></li>
		        	<li><g:remoteLink elementId="public" name="public" title="Public" action="list" update="tableListContent" params="${[id: 2, listLayout: 'list']}">Public</g:remoteLink></li>
		        	<li><g:remoteLink elementId="community" name="community" title="Community" action="list" update="tableListContent" params="${[id: 3, listLayout: 'list']}">Community</g:remoteLink></li>
	        	</ul>
	        	<ul style="display: ${(!listLayout || listLayout == 'grid') ? 'block' : 'none'}" class="filter">
		        	<li><g:remoteLink elementId="all" name="all" title="Alle" action="list" update="gridList" params="${[id: 1, listLayout: 'grid']}" class="down"><g:message code="owo.list.all" default="Alle"/></g:remoteLink></li>
		        	<li><g:remoteLink elementId="public" name="public" title="Public" action="list" update="gridList" params="${[id: 2, listLayout: 'grid']}">Public</g:remoteLink></li>
		        	<li><g:remoteLink elementId="community" name="community" title="Community" action="list" update="gridList" params="${[id: 3, listLayout: 'grid']}">Community</g:remoteLink></li>
	        	</ul>
	        	<div id="layoutSwitcher">
	        		<span>Layout:</span>
	        		<img src="${resource( dir: 'images', file:'list-table.gif') }" alt="Listen Darstellung" title="Listen Darstellung" class="switchLayout" id="listLayout" />
	        		<img src="${resource( dir: 'images', file:'list-grid.gif') }" alt="Grid Darstellung" title="Grid Darstellung" class="switchLayout" id="gridLayout" />
	        	</div>
	        	<div class="clear"></div>
	        </div>
	        <div id="owoList">
	        	<div id="tableList" style="display: ${listLayout == 'list' ? 'block' : 'none'}">
		            <table>
		                <thead>
		                    <tr>
		                        <th></th>
		                        <g:sortableColumn property="title" title="${message(code: 'owo.title.label', default: 'Titel')}" params="[ listLayout: 'list' ]"  />
		                        <th>Ziel</th>
		                        <!--<g:sortableColumn property="goal" title="${message(code: 'owo.goal.label', default: 'Ziel')}" params="[ listLayout: 'list' ]"  />-->
		                        <g:sortableColumn property="owner.username" title="${message(code: 'owo.owner.label', default: 'Ersteller')}" params="[ listLayout: 'list' ]"  />
		                        <!-- <g:sortableColumn property="maxPerson" title="${message(code: 'owo.freeSlots.label', default: 'Frei Plätze')}" params="[ listLayout: 'list' ]"  /> -->
		                        <th><a href="#">${message(code: 'owo.freeSlots.label', default: 'Frei Plätze')}</a></th>
		                        <sec:ifAllGranted roles="ROLE_ADMIN">
		                        	<th></th>
		                        </sec:ifAllGranted>
		                    </tr>
		                </thead>
		                <tbody id="tableListContent">
			                <g:render template="owoListTable" model="['owoInstanceList': owoInstanceList]" />
		                </tbody>
		            </table>
		            <div class="paginateButtons">
		                <g:paginate total="${owoInstanceTotal}" params="[ listLayout: 'list' ]" />
		            </div>
		        </div>
		        <div id="gridList" style="display: ${(!listLayout || listLayout == 'grid') ? 'block' : 'none'}">
		        	<g:render template="owoListGrid" model="[ owoInstanceList: owoInstanceList, owoInstanceTotal: owoInstanceTotal]" />
		        </div>
			</div>
        </div>
    </body>
</html>