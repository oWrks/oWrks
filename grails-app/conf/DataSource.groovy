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

dataSource {
    pooled = true
	logSql = false
	driverClassName = "org.hsqldb.jdbcDriver"
	//username = "sa"
	//password = ""
	//url = "jdbc:hsqldb:file:prodDb;shutdown=true"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:hsqldb:file:devDB;shutdown=true"
        }
    }
    test {
        dataSource {
            dbCreate = "create-drop"
            url = "jdbc:hsqldb:mem:testDb"
        }
    }
    production {
        dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			username = "username"
			password = "password"
			driverClassName = "com.mysql.jdbc.Driver"
			url = "jdbc:mysql://localhost/dbname"
			
			// see http://commons.apache.org/dbcp/configuration.html
			properties {
				maxActive = 50
				maxIdle = 25
				minIdle = 1
				initialSize = 1
				
				timeBetweenEvictionRunsMillis = 1000 * 60 * 30
				numTestsPerEvictionRun = 3
				minEvictableIdleTimeMillis = 1000 * 60 * 30
				
				maxWait = 10000
	
				testOnBorrow = true
				testWhileIdle = true
				testOnReturn = false
	
				validationQuery = "SELECT 1"
			}
        }
    }
	standalone {
		dataSource {
			pooled = false
			driverClassName = "org.hsqldb.jdbcDriver"
			username = "sa"
			password = ""
			dbCreate = "update"
			url = "jdbc:hsqldb:file:prodDb;shutdown=true"
		}
	}
}
