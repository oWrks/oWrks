package owrks
/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import grails.plugins.springsecurity.Secured

import org.compass.core.engine.SearchEngineQueryParseException

import owrks.owo.Owo

/**
 * Basic web interface for Grails Searchable Plugin 
 *
 * @author Maurice Nicholson
 */
@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class SearchableController {
	
	def searchableService
	def owoService

	/**
	 * Default search action, which performs a search on oWos
	 * @see	Options	http://grails.org/Searchable+Plugin+-+Methods+-+search#Options
	 * @param q:String	the string to search for
	 * @author Raoul
	 */
	def index = {
		if (!params.q?.trim()) {
			return [:]
		}
		try {
			params.suggestQuery = true
			params.max = 5	// default 10
			params.escape = true
			// TODO: directly search only for not-private owos
			def res = Owo.search(params.q, params)
			// remove the private owos from the result
			res.results = res.results.findAll { it.type != 'private' }

			def suggestedOwos = owoService.getLatestOwos(params)
			return [searchResult: res, suggestedOwos: suggestedOwos]
		} catch (SearchEngineQueryParseException ex) {
			return [parseException: true]
		}
	}

	/*
	 * This closure does the per-hit highlighting
	 * sr is the actual Map returned by search, so use that for storage
	 * usage:	Domain.search("summer winds", withHighlighter: songHighlighter)
	 * @author Raoul
	 */
	private def owoHighlighter = { highlighter, index, sr ->
		// lazy-init the storage
		if (!sr.highlights) {
			sr.highlights = []
		}
		// store highlighted song lyrics; "lyrics" is a // searchable-property of the Song domain class
		sr.highlights[index] = highlighter.fragment("description")
	}

	/**
	 * Perform a bulk index of every searchable object in the database
	 */
	def indexAll = {
		Thread.start { searchableService.index() }
		render("bulk index started in a background thread")
	}

	/**
	 * Perform a bulk index of every searchable object in the database
	 */
	def unindexAll = {
		searchableService.unindex()
		render("unindexAll done")
	}
}