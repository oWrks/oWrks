// Remove the slf4j jar which is used by atmosphere
/*eventWarStart = { warName ->
	if (grailsEnv == "production") {
		println "Removing SLF4J Extensions JAR"
		Ant.delete(
		   file:"${stagingDir}/WEB-INF/lib/slf4j-log4j12-1.5.8.jar")
	}
}*/