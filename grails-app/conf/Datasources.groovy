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

datasources = {

	datasource(name: 'preRegister') {
		domainClasses([owrks.user.AlphaUser])
		driverClassName('com.mysql.jdbc.Driver')
		url('jdbc:mysql://localhost/owrks-beta_accounts')
		username('owrksbeta')
		password('b3t4')
		dbCreate('update')
		logSql(false)
		environments(['production'])
		dialect(org.hibernate.dialect.MySQL5InnoDBDialect)
		hibernate {
			cache {
				use_second_level_cache(false)
				use_query_cache(false)
			}
		}
	}
}
