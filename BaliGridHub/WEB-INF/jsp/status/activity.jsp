<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">


<script type="text/javascript">

  function onStop() {  
    suitesForm.submit();    
  }
  
</script>
  

<table style="padding-left:40px; padding-top:40px;">
<tr><td class="svcHdr" style="width:250px;"> Active Suite Contexts Execution List: </td></tr>
			
	<c:forEach items="${gridExecContexts}" var="context">
    	<tr>
    		<td class="svcBody">  <a href="/baligridhub/form/status/suitecontext?resultDir=${context.resultDir}"> ${context.resultDir} </a>  </td>
    	</tr>
	</c:forEach>		
	
</table>




