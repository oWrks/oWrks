
import grails.util.Environment;

import javax.servlet.http.HttpServletRequest

import owrks.admin.Page
import owrks.owo.Mission
import owrks.owo.Owo
import owrks.owo.Step
import owrks.user.Profile
import owrks.user.Role
import owrks.user.User
import owrks.user.UserMessage
import owrks.user.UserRole

/**
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
*/

/**
 * The init-closure will be called on every startup.
 * @author David Crome
 */
class BootStrap {

	def init = { servletContext ->

		log.info "Bootstraping owrks application..."
		log.info "Current Envinronment ${Environment.current}"

		HttpServletRequest.metaClass.isXhr = {
			->
			'XMLHttpRequest' == delegate.getHeader('X-Requested-With')
		}

		if(!User.findByUsername("admin")) {
			// create roles
			def adminRole = new Role(authority: 'ROLE_ADMIN').save(failOnError: true)
			def userRole = new Role(authority: 'ROLE_USER').save(failOnError: true)

			// create admin accounts
			
			def admin = new User(username: 'admin', password: 'admin', password2: 'admin', enabled: true,
					firstName: 'agile', lastName: 'learner', email: 'admin@oWrks.com').save(flush: true, failOnError: true)

			UserRole.create(admin, adminRole)

		
			//***********************************
			//oWos, missions und steps zum testen
			//***********************************

			// create another test oWo
			def owob = new Owo(title: 'How to create a good oWo'
					, goal: 'If you want to find good performers for your owo, you should follow this oWo wich will introduce you tho the art of creating owos.'
					, invitationText: 'Hallo Leute, ich lade euch alle zu meinem oWo ein!!'
					, startDate: new Date()
					, endDate: new Date()+1
					, owner: admin
					, type: 'community'
					, workflowState: 'Starting'
					, picture: 'noun-rakete.png'
					).save(flush: true, failOnError: true)
			owob.addTags([
				'Create',
				'oWo',
				'Help',
				'Introduction'
			])

			def mission1 = new Mission(creator: admin
					, title: 'How to start a good oWo'
					, description: 'First of all we have to find a good title and check if there are similar owos'
					, endDate: new Date()
					, startDate: new Date()
					, owo: owob
					).save(flush: true, failOnError: true)


			def step1 = new Step(createdBy: admin
					, assignedTo: admin
					, title: 'First Step: Keep the Title short'
					, description: 'A Title is kept short with ease. I will show you..'
					, dueDate: new Date()
					, published: false
					, mission: mission1)
					.save(flush: true, failOnError: true)


			def step2 = new Step(createdBy: admin
					, assignedTo: admin
					, title: 'Second Step: The description'
					, description: 'Wouldnt it be nice if every oWo has a concise description? Start your description with a question. Then proceed with some more info.'
					, dueDate: new Date()
					, published: false
					, mission: mission1)
					.save(flush: true, failOnError: true)

			def step3 = new Step(createdBy: admin
					, assignedTo: admin
					, title: 'Third Step: Invite the right people'
					, description: 'Dont just tweet out but invite people directly first. Think about their roles in the oWo and let them know it.'
					, dueDate: new Date()
					, published: false
					, mission: mission1)
					.save(flush: true, failOnError: true)


			def mission2 = new Mission(creator: admin
					, title: 'Creating Content the way others find it valuable'
					, description: 'Even Einstein said: if you cant say it with simple words you probably did not get the point by yourself'
					, endDate: new Date()
					, startDate: new Date()
					, owo: owob
					).save(flush: true, failOnError: true)

			def step4 = new Step(createdBy: admin
					, assignedTo: admin
					, title: 'Explain things with simple words'
					, description: 'Learn how to create valuable assets for your team'
					, dueDate: new Date()
					, published: false
					, mission: mission2)
					.save(flush: true, failOnError: true)



			

			//***********************************
			// statische seiten wie impressum
			//***********************************
			def pageImprint = new Page(name: "Impressum", title: "Impressum", creator: admin, content: """
			<h3>oWrks</h3>
			
			<p><strong>Beteiligte Personen:</strong><br />
			David Crome<br />
			Lusia Erldorfer<br />
			Lukas Gotter<br />
			Stefanie Grünewald<br />
			Raoul Jaeckel<br />
			Katrin Köhler<br />
			Anja C. Wagner</p>
			<p>Postanschrift:<br />
			Katrin Köhler<br /> 
			Lincolnstr. 60<br />
			10315 Berlin</p>
			<p>E-Mail: <a href="mailto:info@owrks.com">info@owrks.com</a></p>
			<p>oWrks gibts auch auf <a href="http://www.facebook.com/oWrks" target="_blank">Facebook</a>, <a href="http://twitter.com/#!/oWrks" target="blank">Twitter</a> und <a href="http://oWrks.posterous.com" target="_blank">Posterous</a></p>
			
			 <h3>Haftung für Inhalte</h3>
    <p>Die Inhalte unserer Seiten wurden mit größter Sorgfalt erstellt. 
      Für die Richtigkeit, Vollständigkeit und Aktualität der Inhalte 
      können wir jedoch keine Gewähr übernehmen. Als Diensteanbieter sind wir gemäß § 7 Abs.1 TMG für 
      eigene Inhalte auf diesen Seiten nach den allgemeinen Gesetzen verantwortlich. 
      Nach §§ 8 bis 10 TMG sind wir als Diensteanbieter jedoch nicht 
      verpflichtet, übermittelte oder gespeicherte fremde Informationen zu 
      überwachen oder nach Umständen zu forschen, die auf eine rechtswidrige 
      Tätigkeit hinweisen. Verpflichtungen zur Entfernung oder Sperrung der 
      Nutzung von Informationen nach den allgemeinen Gesetzen bleiben hiervon 
      unberührt. Eine diesbezügliche Haftung ist jedoch erst ab dem 
      Zeitpunkt der Kenntnis einer konkreten Rechtsverletzung möglich. Bei 
      Bekanntwerden von entsprechenden Rechtsverletzungen werden wir diese Inhalte 
      umgehend entfernen.</p>
      <h3>Durch Nutzer erstellte Inhalte</h3>
       <p>Nutzer von oWrks bestätigen mit Ihrer Registrierung bei oWrks, dass alle zu oWrks beigetragenen Inhalte unter der <a href="http://creativecommons.org/licenses/by-sa/3.0/" target="_blank">Creative Commons Attribution Share Alike Lizenz</a> stehen. 
			Nutzer von oWrks bestätigen und garantieren, keine Inhalte in der Art beizutragen, dass diese Copyright, Urheberrechte oder Privatsphäre dritter Personen
			einschränken oder verletzen oder in irgendeiner Art gegen Gesetze oder Rechte Dritter verstoßen. oWrks behält sich bei Verstoß gegen diese Punkte das Recht vor, alle Inhalte des Benutzers zu entfernen und den Benutzer aus oWrks auszuschließen.</p>
       
    <h3>Haftung für Links</h3>
    <p>Unser Angebot enthält Links zu externen Webseiten Dritter, auf deren 
      Inhalte wir keinen Einfluss haben. Deshalb können wir für diese 
      fremden Inhalte auch keine Gewähr übernehmen. Für die Inhalte 
      der verlinkten Seiten ist stets der jeweilige Anbieter oder Betreiber der 
      Seiten verantwortlich. Die verlinkten Seiten wurden zum Zeitpunkt der Verlinkung 
      auf mögliche Rechtsverstöße überprüft. Rechtswidrige 
      Inhalte waren zum Zeitpunkt der Verlinkung nicht erkennbar. Eine permanente 
      inhaltliche Kontrolle der verlinkten Seiten ist jedoch ohne konkrete Anhaltspunkte 
      einer Rechtsverletzung nicht zumutbar. Bei Bekanntwerden von Rechtsverletzungen 
      werden wir derartige Links umgehend entfernen. Hyperlinks auf <a href="www.owrks.net" target="_blank">www.owrks.net</a> sind willkommen.</p>
    <h3>Urheberrecht</h3>
    <p>Die durch die Seitenbetreiber erstellten Inhalte und Werke auf diesen Seiten 
      unterliegen dem deutschen Urheberrecht. Die Vervielfältigung, Bearbeitung, Verbreitung und 
      jede Art der Verwertung außerhalb der Grenzen des Urheberrechtes bedürfen 
      der schriftlichen Zustimmung des jeweiligen Autors bzw. Erstellers. Downloads 
      und Kopien dieser Seite sind nur für den privaten, nicht kommerziellen 
      Gebrauch gestattet. Soweit die Inhalte auf dieser Seite nicht vom Betreiber erstellt wurden, 
      werden die Urheberrechte Dritter beachtet. Insbesondere werden Inhalte Dritter als solche 
      gekennzeichnet. Sollten Sie trotzdem auf eine Urheberrechtsverletzung aufmerksam werden, bitten wir um einen entsprechenden Hinweis. 
      Bei Bekanntwerden von Rechtsverletzungen werden wir derartige Inhalte umgehend entfernen.</p>
         
    <h3>Datenschutz</h3>
    <p>Soweit auf unseren Seiten personenbezogene Daten (beispielsweise Name oder eMail-Adressen) erhoben werden, erfolgt dies stets auf freiwilliger Basis. Diese Daten werden ohne Ihre ausdrückliche Zustimmung nicht an Dritte weitergegeben.   
    </p>
    <p>Wir weisen darauf hin, dass die Datenübertragung im Internet (z.B. 
      bei der Kommunikation per E-Mail) Sicherheitslücken aufweisen kann. 
      Ein lückenloser Schutz der Daten vor dem Zugriff durch Dritte ist nicht 
      möglich. </p>
    <p>Der Nutzung von im Rahmen der Impressumspflicht veröffentlichten Kontaktdaten 
      durch Dritte zur Übersendung von nicht ausdrücklich angeforderter 
      Werbung und Informationsmaterialien wird hiermit ausdrücklich widersprochen. 
      Die Betreiber der Seiten behalten sich ausdrücklich rechtliche Schritte 
      im Falle der unverlangten Zusendung von Werbeinformationen, etwa durch Spam-Mails, 
      vor.</p><br />
<h3>Datenschutzerklärung für die Nutzung von Facebook-Plugins
		 (Like-Button)</h3>
		 <p>Auf unseren Seiten sind Plugins des sozialen Netzwerks Facebook,
		 1601 South California Avenue, Palo Alto, CA 94304, USA integriert.
		 Die Facebook-Plugins erkennen Sie an dem Facebook-Logo oder
		 dem "Like-Button" ("Gefällt mir") auf unserer Seite. Eine Übersicht
		 über die Facebook-Plugins finden Sie hier:
		 <a href="http://developers.facebook.com/docs/plugins/" 
		 target="_blank">http://developers.facebook.com/docs/plugins/</a>.<br />
		 Wenn Sie unsere Seiten
		 besuchen, wird über das Plugin eine direkte Verbindung zwischen Ihrem
		 Browser und dem Facebook-Server hergestellt. Facebook erhält dadurch
		 die Information, dass Sie mit Ihrer IP-Adresse unsere Seite
		 besucht haben. Wenn Sie den Facebook "Like-Button" anklicken während
		 Sie in Ihrem Facebook-Account eingeloggt sind, können Sie die Inhalte
		 unserer Seiten auf Ihrem Facebook-Profil verlinken. Dadurch kann
		 Facebook den Besuch unserer Seiten Ihrem Benutzerkonto zuordnen. Wir
		 weisen darauf hin, dass wir als Anbieter der Seiten keine Kenntnis vom
		 Inhalt der übermittelten Daten sowie deren Nutzung durch Facebook
		 erhalten. Weitere Informationen hierzu finden Sie in der
		 Datenschutzerklärung von facebook unter
		 <a href="http://de-de.facebook.com/policy.php" target="_blank">
		 http://de-de.facebook.com/policy.php</a></p>
     <p>Wenn Sie nicht wünschen, dass Facebook den Besuch unserer Seiten Ihrem 
     Facebook-Nutzerkonto zuordnen kann, loggen Sie sich bitte aus Ihrem
     Facebook-Benutzerkonto aus.</p><br />
<h3>Datenschutzerklärung für die Nutzung von Google +1</h3>
		 <p><i>Erfassung und Weitergabe von Informationen:</i><br />
     Mithilfe der Google +1-Schaltfläche können Sie Informationen weltweit
     veröffentlichen. über die Google +1-Schaltfläche erhalten Sie und andere
     Nutzer personalisierte Inhalte von Google und unseren Partnern. Google
     speichert sowohl die Information, dass Sie für einen Inhalt +1 gegeben
     haben, als auch Informationen über die Seite, die Sie beim Klicken auf
     +1 angesehen haben. Ihre +1 können als Hinweise zusammen mit Ihrem
     Profilnamen und Ihrem Foto in Google-Diensten, wie etwa in
     Suchergebnissen oder in Ihrem Google-Profil, oder an anderen Stellen auf
     Websites und Anzeigen im Internet eingeblendet werden.<br />
     Google zeichnet Informationen über Ihre +1-Aktivitäten auf, um die 
     Google-Dienste für Sie und andere zu verbessern. Um die Google 
     +1-Schaltfläche verwenden zu können, benötigen Sie ein weltweit 
     sichtbares, öffentliches Google-Profil, das zumindest den für das 
     Profil gewählten Namen enthalten muss. Dieser Name wird in allen 
     Google-Diensten verwendet. In manchen Fällen kann dieser Name auch 
     einen anderen Namen ersetzen, den Sie beim Teilen von Inhalten über 
     Ihr Google-Konto verwendet haben. Die Identität Ihres Google-Profils 
     kann Nutzern angezeigt werden, die Ihre E-Mail-Adresse kennen oder 
     über andere identifizierende Informationen von Ihnen verfügen.<br />
     <br />
     <i>Verwendung der erfassten Informationen:</i><br />
     Neben den oben erläuterten Verwendungszwecken werden die von Ihnen 
     bereitgestellten Informationen gemäß den geltenden 
     Google-Datenschutzbestimmungen genutzt. Google veröffentlicht 
     möglicherweise zusammengefasste Statistiken über die +1-Aktivitäten 
     der Nutzer bzw. gibt diese an Nutzer und Partner weiter, wie etwa
     Publisher, Inserenten oder verbundene Websites. </p><br />
<h3>Datenschutzerklärung für die Nutzung von Twitter</h3>
		 <p>Auf unseren Seiten sind Funktionen des Dienstes Twitter eingebunden.
		 Diese Funktionen werden angeboten durch die Twitter Inc., 795
		 Folsom St., Suite 600, San Francisco, CA 94107, USA. Durch das Benutzen
		 von Twitter und der Funktion "Re-Tweet" werden die von Ihnen
		 besuchten Webseiten mit Ihrem Twitter-Account verknüpft und anderen
		 Nutzern bekannt gegeben. Dabei werden auch Daten an Twitter übertragen.</p>
     <p>Wir weisen darauf hin, dass wir als Anbieter der Seiten keine
     Kenntnis vom Inhalt der übermittelten Daten sowie deren Nutzung durch
     Twitter erhalten. Weitere Informationen hierzu finden Sie in der
     Datenschutzerklärung von Twitter unter
     <a href="http://twitter.com/privacy" target="_blank">http://twitter.com/privacy</a>.</p>
     <p>Ihre Datenschutzeinstellungen bei Twitter können Sie in den
     Konto-Einstellungen unter
     <a href="http://twitter.com/account/settings" target="_blank">http://twitter.com/account/settings</a> ändern.</p>
<br />
<p><i>Quellen: <a href="http://www.e-recht24.de/muster-disclaimer.htm" target="_blank">Disclaimer eRecht24</a>, <a href="http://www.e-recht24.de/artikel/datenschutz/6590-facebook-like-button-datenschutz-disclaimer.html" target="_blank">eRecht24 Facebook Datenschutzerklärung</a>, <a href="http://www.google.com/intl/de/+/policy/+1button.html" target="_blank">Datenschutzerklärung Google +1</a>, <a href="http://twitter.com/privacy" target="_blank">Datenschutzerklärung für Twitter</a></i></p>
""").save(flush: true, failOnError: true)


def pageAbout = new Page(name: "About", title: "About", creator: admin, content: """
<h2>Was ist oWrks?</h2>
<p>oWrks ist ein Projekt im Rahmen des Masterstudiengangs Internationale Medieninformatik an der HTW Berlin.</p>

<p>
<strong>Beteiligte Studierende:</strong><br />
David Crome<br />
Lusia Erldorfer<br />
Lukas Gotter<br />
Stefanie Grünewald<br />
Raoul Jäckel<br />
Katrin Köhler</p>
<p><strong>betreut durch:</strong>
Anja C. Wagner</p>

<h2>Was bietet oWrks?</h2>
<div class="info-box">
	<div class="col50percent">
		<h3>oWrks - Eure Arbeit im richtigen Flow</h3>
		<p>oWrks bietet eine agile Umgebung für kollaborative
			oWos (Online-Workshops) Eurer Projekt-, Lern- oder
			Arbeitsgruppen. Mit einem durchdachten Workflow aus 5 Phasen
			werden kleine und große Gruppen in ihrer Selbstorganisation im
			Arbeitsprozess unterstützt.</p>
	</div>
	<div class="col50percent">
		<img style="padding: 10px 0 0 40px;"
			src="images/0phasen.png" alt="Phasen" />
	</div>
	<div class="clear"></div>
</div>
<div class="info-box">
	<div class="col50percent">
		<h3>Starting - Wo alles beginnt!</h3>
		<p>Du willst dir ein interessantes Thema mit anderen
			erarbeiten? Oder du suchst eine Organisationsmöglichkeit für ein
			spannendes (Team-)Projekt? Dann lege einen oWo (Online-Workshop)
			an und definiere die zentralen Lern- und/oder Arbeitsziele.</p>
	</div>
	<div class="col50percent">
		<img style="padding-left: 40px;"
			src="images/1starting.png"
			alt="Starting" />
	</div>
	<div class="clear"></div>
</div>
<div class="info-box">
	<div class="col50percent">
		<h3>Forming - Finde Performer für Deinen Online-Workshop</h3>
		<p>Du hast einen tollen oWo erstellt und brauchst noch Leute
			für Deine Arbeitsgruppe? Suche und finde passende Performer über
			die oWrks-Community oder in Deinem persönlichen Netzwerk.</p>
	</div>
	<div class="col50percent">
		<img style="padding-left: 100px;" width="300"
			src="images/2forming.png" alt="Forming" />
	</div>
	<div class="clear"></div>
</div>
<div class="info-box">
	<div class="col50percent">
		<h3>Storming - Organisiert Eure Gruppe mit oWrks</h3>
		<p>Lernt Euch jetzt kennen und legt die Randbedingungen zu
			Eurem oWo fest. Einigt Euch auf Kommunikationskanäle innerhalb
			und außerhalb von oWrks. Definiert Arbeitsschritte, Termine
			und Ziele, wie es Euch gefällt. Legt erste Missions
			(Meilensteine) fest und verteilt erste Steps (ToDos). oWrks
			hilft Euch dabei!</p>
	</div>
	<div class="col50percent">
		<img style="padding-left: 100px;" height="180"
			src="images/3storming.png"
			alt="Storming" />
	</div>
	<div class="clear"></div>
</div>
<div class="info-box">
	<div class="col50percent">
		<h3>Performing - Arbeitet effizient zusammen</h3>
		<p>Erarbeitet Eure Ergebnisse Schritt für Schritt mit
			einfachen und effektiven Mitteln. Nutzt die Tools, die Ihr
			braucht. oWrks stellt die wichtigsten für Euch bereit.
			Tauscht Euch aus, gebt Euch Feedback. oWrks bietet Euch
			hierfür die geeigneten Kommunikationskanäle.</p>
	</div>
	<div class="col50percent">
		<img style="padding-left: 40px;"
			src="images/4performing.png"
			alt="Performing" />
	</div>
	<div class="clear"></div>
</div>
<div class="info-box">
	<div class="col50percent">
		<h3>Publishing - Es lebe die kollektive Intelligenz!*</h3>
		<p>
			Lernen lebt von den Gedanken und Ideen vieler. Teilt Euer Wissen
			und Eure Ergebnisse mit der Welt, veröffentlicht Eure
			Arbeitsergebnisse auf oWrks und überall wo Ihr wollt. Ihr
			könnt dafür belohnt werden...<br /> <span class="small">*
				dieses Feature folgt.</span>
		</p>
	</div>
	<div class="col50percent">
		<img style="padding-left: 100px;" height="150px"
			src="images/5publishing.png"
			alt="Publishing" />
	</div>
	<div class="clear"></div>
</div>
""").save(flush: true, failOnError: true)


		}
	}


	def destroy = {
	}
}
