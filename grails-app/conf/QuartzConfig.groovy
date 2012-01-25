
quartz {
    autoStartup = true
    jdbcStore = false
    waitForJobsToCompleteOnShutdown = true
}

environments {
	production {
		quartz {
			autoStartup = true
		}
	}
    test {
        quartz {
            autoStartup = false
        }
    }
	standalone {
		quartz {
			autoStartup = false
		}
	}
}
