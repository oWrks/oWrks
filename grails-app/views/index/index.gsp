<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><g:message code="index.index.title"
		default="Welcome to oWrks" /></title>
<meta name="layout" content="main" />
</head>
<body>
	<div class="box" id="slider">
		<div id="slides">
			<div class="slides_container">
				<div class="slide">
					<h2>oWrks - Eure Arbeit im richtigen Flow</h2>
					<div class="col50percent">
						<p>oWrks bietet eine agile Umgebung für kollaborative
							oWos (Online-Workshops) Eurer Projekt-, Lern- oder
							Arbeitsgruppen. Mit einem durchdachten Workflow aus 5 Phasen
							werden kleine und große Gruppen in ihrer Selbstorganisation im
							Arbeitsprozess unterstützt.</p>
						<p>
							<g:link controller="register" class="button">Join oWrks</g:link>
						</p>
					</div>
					<div class="col50percent">
						<img style="padding: 10px 0 0 40px;"
							src="${resource(dir:'images',file:'0phasen.png')}" alt="Phasen" />
					</div>
				</div>
				<div class="slide">
					<h2>Starting - Wo alles beginnt!</h2>
					<div class="col50percent">
						<p>Du willst dir ein interessantes Thema mit anderen
							erarbeiten? Oder du suchst eine Organisationsmöglichkeit für ein
							spannendes (Team-)Projekt? Dann lege einen oWo (Online-Workshop)
							an und definiere die zentralen Lern- und/oder Arbeitsziele.</p>
						<p>
							<g:link controller="register" class="button">Join oWrks</g:link>
						</p>
					</div>
					<div class="col50percent">
						<img style="padding-left: 40px;"
							src="${resource(dir:'images',file:'1starting.png')}"
							alt="Starting" />
					</div>
				</div>
				<div class="slide">
					<h2>Forming - Finde Performer für Deinen Online-Workshop</h2>
					<div class="col50percent">
						<p>Du hast einen tollen oWo erstellt und brauchst noch Leute
							für Deine Arbeitsgruppe? Suche und finde passende Performer über
							die oWrks-Community oder in Deinem persönlichen Netzwerk.</p>
						<p>
							<g:link controller="register" class="button">Join oWrks</g:link>
						</p>
					</div>
					<div class="col50percent">
						<img style="padding-left: 100px;" width="300"
							src="${resource(dir:'images',file:'2forming.png')}" alt="Forming" />
					</div>
				</div>
				<div class="slide">
					<h2>Storming - Organisiert Eure Gruppe mit oWrks</h2>
					<div class="col50percent">
						<p>Lernt Euch jetzt kennen und legt die Randbedingungen zu
							Eurem oWo fest. Einigt Euch auf Kommunikationskanäle innerhalb
							und außerhalb von oWrks. Definiert Arbeitsschritte, Termine
							und Ziele, wie es Euch gefällt. Legt erste Missions
							(Meilensteine) fest und verteilt erste Steps (ToDos). oWrks
							hilft Euch dabei!</p>
						<p>
							<g:link controller="register" class="button">Join oWrks</g:link>
						</p>
					</div>
					<div class="col50percent">
						<img style="padding-left: 100px;" height="180"
							src="${resource(dir:'images',file:'3storming.png')}"
							alt="Storming" />
					</div>
				</div>
				<div class="slide">
					<h2>Performing - Arbeitet effizient zusammen</h2>
					<div class="col50percent">
						<p>Erarbeitet Eure Ergebnisse Schritt für Schritt mit
							einfachen und effektiven Mitteln. Nutzt die Tools, die Ihr
							braucht. oWrks stellt die wichtigsten für Euch bereit.
							Tauscht Euch aus, gebt Euch Feedback. oWrks bietet Euch
							hierfür die geeigneten Kommunikationskanäle.</p>
						<p>
							<g:link controller="register" class="button">Join oWrks</g:link>
						</p>
					</div>
					<div class="col50percent">
						<img style="padding-left: 40px;"
							src="${resource(dir:'images',file:'4performing.png')}"
							alt="Performing" />
					</div>
				</div>
				<div class="slide">
					<h2>Publishing - Es lebe die kollektive Intelligenz!*</h2>
					<div class="col50percent">
						<p>
							Lernen lebt von den Gedanken und Ideen vieler. Teilt Euer Wissen
							und Eure Ergebnisse mit der Welt, veröffentlicht Eure
							Arbeitsergebnisse auf oWrks und überall wo Ihr wollt. Ihr
							könnt dafür belohnt werden...<br /> <span class="small">*
								dieses Feature folgt.</span>
						</p>
						<p>
							<g:link controller="register" class="button">Join oWrks</g:link>
						</p>
					</div>
					<div class="col50percent">
						<img style="padding-left: 100px;" height="150"
							src="${resource(dir:'images',file:'5publishing.png')}"
							alt="Publishing" />
					</div>
				</div>
			</div>
		</div>
		<div class="clear"></div>
	</div>
	<div class="col1">
		<h1>Neueste oWos <g:link controller="owo" action="list" class="button right">oWo-Liste</g:link></h1>
		<g:render template="owoTeaser" model="[ 'owos': newestOwos ]"></g:render>
		<!--<g:render template="tagcloud"></g:render>-->
	</div>

	<div class="col2">
		<g:render template="../likeButtons"></g:render>
		<h1>Neueste Steps</h1>
		<g:render template="stepTeaser" model="[ 'steps': newestSteps ]"></g:render>


		<h1>Neueste Nutzer</h1>
		<g:render template="userTeaser" model="[ 'users': newestUsers ]"></g:render>
	</div>
	<div class="clear"></div>

</body>
</html>