package owrks.admin
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

import grails.plugins.springsecurity.Secured
import owrks.admin.Page;
import owrks.controller.AController;

@Secured(['ROLE_ADMIN'])	
class PageController extends AController {

	static defaultAction = "dynamicPage"
	
	def beforeInterceptor = [action:this.&loadPage,except:['list', 'show']]
	
	def loadPage(){
		params.action = (actionName[0].toLowerCase() + actionName[1..(actionName.length()-1)])
		params.page = Page.findByName(params.action)
	}
	
	def afterInterceptor = { model, modelAndView ->
		model.page = params.page
		//def viewName = resExists("/pages/${params.action}") ? "/pages/${params.action}" : "/pages/default"
		//def viewName = params.page.name
		//modelAndView.viewName = viewName
	}
	
	def resExists(resName) {
		def engine = grailsAttributes.pagesTemplateEngine
		def resUri = grailsAttributes.getViewUri(resName, request)
		def resource = engine.getResourceForUri(resUri)
		return resource?.file && resource?.exists()
	}
	
	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def dynamicPage = {
		/*if(page)
			render view: "dynamicPage", model: [ page: page ]
		else
			redirect( uri: "/" )*/
	}
	
	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def impressum = {
		render view: "dynamicPage"
	}
	
	@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
	def about = {
		render view: "dynamicPage"
	}
	
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [pageInstanceList: Page.list(params), pageInstanceTotal: Page.count()]
    }

    def create = {
        def pageInstance = new Page()
        pageInstance.properties = params
        return [pageInstance: pageInstance]
    }

    def save = {
        def pageInstance = new Page(params)
		pageInstance.creator = getCurrentUser()
        if (!pageInstance.hasErrors() && pageInstance.save()) {
            flash.message = "page.created"
            flash.args = [pageInstance.id]
            flash.defaultMessage = "Page ${pageInstance.id} created"
            redirect(action: "show", id: pageInstance.id)
        }
        else {
            render(view: "create", model: [pageInstance: pageInstance])
        }
    }
	
    def show = {
        def pageInstance = Page.get(params.id)
        if (!pageInstance) {
            flash.message = "page.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Page not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [pageInstance: pageInstance]
        }
    }

    def edit = {
        def pageInstance = Page.get(params.id)
        if (!pageInstance) {
            flash.message = "page.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Page not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [pageInstance: pageInstance]
        }
    }

    def update = {
        def pageInstance = Page.get(params.id)
        if (pageInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (pageInstance.version > version) {
                    
                    pageInstance.errors.rejectValue("version", "page.optimistic.locking.failure", "Another user has updated this Page while you were editing")
                    render(view: "edit", model: [pageInstance: pageInstance])
                    return
                }
            }
            pageInstance.properties = params
			pageInstance.lastEditor = getCurrentUser()
            if (!pageInstance.hasErrors() && pageInstance.save()) {
                flash.message = "page.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Page ${params.id} updated"
                redirect(action: "show", id: pageInstance.id)
            }
            else {
                render(view: "edit", model: [pageInstance: pageInstance])
            }
        }
        else {
            flash.message = "page.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Page not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def pageInstance = Page.get(params.id)
        if (pageInstance) {
            try {
                pageInstance.delete()
                flash.message = "page.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Page ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "page.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Page ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "page.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Page not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
