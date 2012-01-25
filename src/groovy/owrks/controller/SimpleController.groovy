package owrks.controller

import grails.util.GrailsNameUtils

import com.frickle.grails.plugins.taggable.*

/**
 * Package:		photoo.controller
 * User:		timo
 * Date:		22.12.2010
 * Time:		02:33:55
 */
abstract class SimpleController extends AController{
	
	static allowedMethods = [save: "POST"]

	def taggableService
	def grailsApplication
	def entityClass
	
	def currentEntity
	def afterSaveAction = "show"
	
	SimpleController(){
		
	}

	/**
	 * set basic variables for view
	 */
	def afterInterceptor(model){
		super.afterInterceptor(model)
		if(params.action.equals('save'))
			addTags()
			
		// set entity name
		if(entityClass){
			// entity name
			String entityName = GrailsNameUtils.getShortName(entityClass)
			model.entityName = g.message(code: "default." + entityName.toLowerCase(), default: entityName)
			
			// headline
			model.entityHeadline = g.message(
				code: entityName.toLowerCase() + "." + params.action, 
				default: g.message(
					code: "default.${params.action}",
					default: "{0} / $params.action",
					args: [model.entityName]
				),
				args: [model.entityName] 
			)
		}
	}
	
	// TODO: Taggable plugin required to use this method
	/*private void addTags(){
       	def entity = getCurrentEntity()
		if(entity instanceof Taggable == false)return

		if(entity.id && params.tags){
			entity.setTags(photooService.parseTags(params.tags))
        }
   }*/
	
	protected Object getCurrentEntity(){
		if(!entityClass)return
		
		// get by set value
		if(currentEntity)return currentEntity

		// get from db
		if(!params.id)return	
		return entityClass.get(params.id)
	}

	def setCurrentEntity(Object entity){
		currentEntity = entity
	}

	def createEntity(){
		return entityClass?.newInstance([:])
	}

	def hasCurrentEntity(){
		return getCurrentEntity() != null
	}

	def getEntityName(){
		if(!entityClass)return
		
		String name = entityClass?.getName()
		return name.substring(name.lastIndexOf('.')+1)
	}

	def getEntityLabel(){
		if(!entityClass)return
		return getEntityName()?.toLowerCase() + '.label'
	}
	

	def index = {
        redirect(action: "list", params: params)
    }
	
	protected getEntities(def params){
		// get entities
		// @todo: filter
		entityClass.withCriteria(){
			// limit
			if(params.max){
				maxResults(params.max.toInteger())
				
			}
			if(params.offset){
				firstResult(params.offset.toInteger())
			}
			
			order(params.sort, params.order)
			
			// constrain to photographer
			eq('photographer', getCurrentPhotographer())
		}
	}

    def list = {
		// buffer params
		String pSort = params.sort
		String pOrder = params.order
		int pOffset = params.offset as Integer
		int pMax = params.max as Integer
		
		// set default params
		params.sort = 'id'
		params.order = 'asc'
		params.offset = 0
		params.max = null
		
		// count objects
		def entityTotal = getEntities(params).size()
		
		// get paged results		
		params.order = pOrder ?: 'desc'
		params.sort = pSort ?: 'id'
		params.max = (pMax ?: 10)
		params.offset = pOffset ?: 0

		// render list view
        render(view: "/${params.controller}/list", model: [
			entityList: getEntities(params), 
			entityTotal: entityTotal
		])
    }

	def create = {
        def entity = createEntity()
        entity.properties = params
        render([view: "/${params.controller}/edit", model: [entity: entity]])
    }
	def edit = {
        def entity = getCurrentEntity()

        if (!entity) {
            addMessage "${message(code: 'default.not.found.message', args: [message(code: getEntityLabel(), default: getEntityName()), params.id])}"
            redirect(action: "list")
        }
        else {
             render([view: "/${params.controller}/edit", model: [entity: entity]])
        }
    }	

	def save = {
		// hook delete action
		if(params.id && params._deleteButton){
			redirect(action: "delete", id: params.id)
			return
		}

		// get current
		def entity = getCurrentEntity()
		if(entity){
			// version check
			if (params.version) {
                def version = params?.version?.toLong()
                if (version && entity.version > version) {

                    entity.errors.rejectValue("version", "default.optimistic.locking.failure", [entity] as Object[], "Another user has updated this Entity while you were editing")
                    render(view: "/${params.controller}/edit", model: [entity: entity])
                    return
                }
            }
		}else{
			// create
			entity = createEntity()
			setCurrentEntity(entity)
		}

		// set properties
		entity.properties = params

		if (entity.save(flush: true)) {
			addMessage "${message(code: 'default.saved.message', args: [entity])}"           
            redirect(action: afterSaveAction, id: entity.id)
        }
        else {
            render(view: "/${params.controller}/edit", model: [entity: entity])
        }
    }

	def show = {
        def entity = getCurrentEntity()
        if (!entity) {
			addMessage "${message(code: 'default.not.found.message', args: [message(code: getEntityLabel(), default: getEntityName()), params.id])}"
 
            redirect(action: "list")
        }
        else {
             render([view: "/${params.controller}/show", model: [entity: entity]])
        }
    }

	def delete = {
        def entity = entityClass.get(params.id)
        if (entity) {
            try {
                entity.delete(flush: true)               
				addMessage "${message(code: 'default.deleted.message', args: [entity])}"
                redirect(url: request.getHeader('referer'))
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				addMessage "${message(code: 'default.not.deleted.message', args: [entity])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: getEntityLabel(), default: getEntityName()), params.id])}"			
            redirect(url: request.getHeader('referer'))
        }
    }
}
