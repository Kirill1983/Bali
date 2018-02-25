<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>


<json:array name="projects" var="project" items="${projects}">
	<json:object>
      <json:property name="name" value="${project.name}"/>
      <json:property name="loadDate" value="${project.loadDateFmt}"/>
      <json:property name="parentprojectname" value="${project.parentProject.name}"/>
      <json:property name="testcount" value="${project.testcount}"/>
   
      <json:array name="storages" var="storage" items="${project.resourceStorages}">
      	<json:object>
      		<json:property name="resourceClassSimpleName" value="${storage.factory.resourceClass.simpleName}"/>
      		<json:property name="resourceClassName" value="${storage.factory.resourceClass.name}"/>
      		<json:property name="loadCmdHelp" value="${storage.factory.loadCmdHelp}"/>
      		<json:property name="upCount" value="${storage.upCount}"/>
      		<json:property name="totalCount" value="${storage.size}"/>
      		
      		<json:array name="resources" var="resource" items="${storage.resources}">
      		  <json:object>
      			<json:property name="type" value="${resource.type}"/>
      			<json:property name="name" value="${resource.name}"/>
      			<json:property name="resstatus" value="${resource.status}"/>
      			<json:property name="description" value="${resource.description}"/> 
      			
      			<json:property name="executorName" value="${resource.testExecContext.testExecutor.name}"/> 
      			<json:property name="resultDir" value="${resource.testExecContext.suiteExecContext.resultDir}"/> 
      			<json:property name="options" value="${resource.testExecContext.suiteExecContext.suiteMdl.options}"/> 
      			<json:property name="shortClassName" value="${resource.testExecContext.shortClassName}"/> 
     		    <json:property name="errorCount" value="${resource.testExecContext.errorCount}"/>
     		    <json:property name="status" value="${resource.testExecContext.status.desc}"/>
     	 		<json:property name="startDate" value="${resource.testExecContext.startDateFmt}"/>
      			<json:property name="endDate" value="${resource.testExecContext.endDateFmt}"/>
      			<json:property name="elapsed" value="${resource.testExecContext.elapsedFmt}"/>      			     		 
      		  </json:object>          
      		</json:array>   		
      		
      		
      		
      	</json:object>          
      </json:array>
    
      
    
    </json:object>
</json:array>


 

	