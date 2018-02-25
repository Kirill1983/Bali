<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>


<json:array name="projects" var="project" items="${projectsTestsList}">
	<json:object>
      <json:property name="name" value="${project.project}"/>
    
      <json:array name="tests" var="test" items="${project.tests}">
      	<json:object>
      		<json:property name="test" value="${test}"/>
      	</json:object>          
      </json:array>
        
    </json:object>
</json:array>


 

	