import grails.core.GrailsApplication
import org.grails.orm.hibernate.cfg.HibernateMappingContext
import org.grails.orm.hibernate.validation.HibernateDomainClassValidator
import org.grails.validation.GrailsDomainClassValidator
import validatortest.FooOne
import validatortest2.FooTwo

class BootStrap {

    HibernateMappingContext grailsDomainClassMappingContext
    GrailsApplication grailsApplication

    def init = { servletContext ->
        println "================================================================="
        println "technically ok here, but it is confusing that validators beans are defined for dataSources the domains are not mapped to"

        def validatorBeans = grailsApplication.mainContext.getBeansOfType HibernateDomainClassValidator
        validatorBeans.each {
            println it.value.hibernateDatastore.dataSourceName
        }

        println "================================================================="
        println "this shows that only two of the four validators are actually registered with the HibernateMappingContext"
        println "and the dataSourceNames do not necessarily match the dataSource the domain class is mapped to"

        def validatorsInMappingContext = grailsDomainClassMappingContext.entityValidators

        validatorsInMappingContext.each {
            println "${it.key.name} - ${it.value.hibernateDatastore.dataSourceName}"
        }

        println "================================================================="
        println "just verification that indeed only those two validators are resolved"

        def peFooOne = grailsDomainClassMappingContext.getPersistentEntity(FooOne.class.name)
        def valFooOne = grailsDomainClassMappingContext.getEntityValidator(peFooOne)
        println "${peFooOne.name} - ${valFooOne.hibernateDatastore.dataSourceName}"

        def peFooTwo = grailsDomainClassMappingContext.getPersistentEntity(FooTwo.class.name)
        def valFooTwo = grailsDomainClassMappingContext.getEntityValidator(peFooTwo)
        println "${peFooTwo.name} - ${valFooTwo.hibernateDatastore.dataSourceName}"
    }
    def destroy = {
    }
}
