<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://www.informatica.com/tags/pnBali" prefix="pn"%>


<c:set var="selectedMenu"><tiles:getAsString name="selectedMenu"/></c:set>


<html>
<head>
<title>Bali Fort</title>
<head>
	<meta content="text/html; charset=UTF-8" http-equiv="Content-Type">
	<link type="text/css" href="/bali/data/css/Main.css" rel="StyleSheet">
	<link type="text/css" href="/bali/data/css/Default.css" rel="StyleSheet">
	<script src="/bali/data/js/pnCommon.js" type="text/javascript"></script>

<body id="InfHtmlBody" onload="">
 
  <table cellpadding="0" cellspacing="0" id="InfMPage">
  <tr>
    <td class="InfHeader">
    
      <div><img src="/bali/data/images/logo_cloud_blue2.png"/></div>
      <table cellpadding="0" cellspacing="0" width="100%">
      	<tr>
        	<td class="InfNavTabs">
        		<pn:navbar selectedMenu="${selectedMenu}" hideTabs="false"/>
      			<%-- header td,tr,table are closed by main navigation --%>

  <tr>
    <td class="InfPgBody" colspan="2">
      <tiles:insertAttribute name="body"/>
    </td>
  </tr>
  
  </table>

</body>

</html>