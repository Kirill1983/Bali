// Cookie start **********************************

var today = new Date();
var expiry = new Date(today.getTime() + 8 * 24 * 60 * 60 * 1000);
var expiryLong = new Date(today.getTime() + 160 * 24 * 60 * 60 * 1000);
var expiryToday = new Date(today.getTime() + 8 * 60 * 60 * 1000);

var color = "rgb(200, 200, 230)";
var tip = null;
var id  = "id1";

function caclWarnIDs(){

	var ids = "";
 	tags=document.body.getElementsByTagName('INPUT');
 	for(var j=0; j<tags.length; j++)
   	   if (tags[j].className == 'action')
	     if (tags[j].checked)
		ids += tags[j].name+",";

	if (ids.length > 0)
		ids = ids.substring(0, ids.length-1);

	return ids;

 }



function getCookieVal(offset) {
	var endstr = document.cookie.indexOf(";", offset);
	if (endstr == -1) { endstr = document.cookie.length; }

	return unescape(document.cookie.substring(offset, endstr));
}

function GetCookie (name) {
	var arg = name + "=";
	var alen = arg.length;
	
	var clen = document.cookie.length;
	var i = 0;
	while (i < clen) {
		var j = i + alen;
		if (document.cookie.substring(i, j) == arg) {
			return getCookieVal (j);
		}
		i = document.cookie.indexOf(" ", i) + 1;
		if (i == 0) break;
	}

	return null;
}

function DeleteCookie (name, path, domain) {
	path = "/";
	if (GetCookie(name)) {
		document.cookie = name + "=" +
		((path) ?   "; path=" + path : "") +
		((domain) ? "; domain=" + domain : "") + 
                "; expires=Thu, 01-Jan-70 00:00:01 GMT";
	}
}

function SetCookie (name, value, expires, path, domain, secure) {
	path = "/";
	document.cookie = name + "=" + escape(value) +
	((expires) ? "; expires=" + expires.toGMTString() : "") +
	((path)    ? "; path=" + path : "") + 
	((domain)  ? "; domain=" + domain : "") + 
	((secure)  ? "; secure" : "");

}
// Cookie end **********************************


 var isShow=false;
 var isBugShow=false;
 var warningSelectObj=null;


 function nextSelect(){
	if (warningSelectObj.selectedIndex == (warningSelectObj.length-1))
		warningSelectObj.selectedIndex = 0;
	else 
		warningSelectObj.selectedIndex++;
 }

 function prevSelect(){
	if (warningSelectObj.selectedIndex == 0)
		warningSelectObj.selectedIndex = warningSelectObj.length-1;
	else 
		warningSelectObj.selectedIndex--;
 }

 
 function updateWarningIdsList(){
	var ids = "";
 	tags=document.body.getElementsByTagName('INPUT');
 	for(var j=0; j<tags.length; j++)
   	   if (tags[j].className == 'action')
	     if (tags[j].checked)
		ids += tags[j].name+",";

	if (ids.length > 0)
	ids = ids.substring(0, ids.length-1);

	document.getElementById("warningIDs").value = ids;
 }

 function rerunMarkedWarnings(){

	var resultDir = document.getElementById("resultDir").value;	
	
	var ids = "";
 	tags=document.body.getElementsByTagName('INPUT');
 	for(var j=0; j<tags.length; j++)
   	   if (tags[j].className == 'action')
	     if (tags[j].checked)
		ids += tags[j].name+",";

	if (ids.length > 0)
		ids = ids.substring(0, ids.length-1);

	alert("Rerun warning ids: "+ids+" test contexts in suite "+resultDir);


	$.get("/bali/form/status/rerun?resultDir="+resultDir+"&warningIDs="+ids,
			  function(data) {
			  }
	  		);
	setTimeout("window.location.reload();", 2000); 

 }

 function rerunWarning(id){
	var resultDir = document.getElementById("resultDir").value;	
	$.get("/bali/form/status/rerun?resultDir="+resultDir+"&warningIDs="+id,
			  function(data) {
			  }
	  		);
	setTimeout("window.location.reload();", 1500); 

 }



 // если нажали на элементе вниз или вверх и поменялся активный скриншот для отображения
 function keyPress(ev){


	ev = ev || window.event;
	var code = (ev.keyCode || ev.which);

	if (ev.target.id == 'email')
		return true;

	if ((code != 37) && (code != 38) && (code != 39) && (code != 40) && (code != 32) && (code != 27))
		return true;

        if (isWarningCaseLayerShown()) {
	   if ((code == 37) || (code == 38) || (code == 39) || (code == 40)){
	 	showWarningCaseLayer();
		return true;
	   }
	}


        // пробел - включает - выключает отображение
        if (code == 32 || code == 27) {
		if (isWarningCaseLayerShown())
		 hideWarningCaseLayer();
	}



 }

 function isWarningCaseLayerShown(){

    var warningCasesLayer = document.getElementById("warningCasesLayer");

    if ( warningCasesLayer.style.visibility == 'hidden')
    	return false;
    else 
        return true;
       
   
 } 


 function getProject(){
	var prj = "";


	prj = projectname.value;	

	if (prj == "") {
		if (GetCookie('bprj') != null)
			prj = GetCookie('bprj');
		else
			prj = "";
	}

	return prj;
 }


 function saveEmail(obj){
	SetCookie(obj.id, obj.value, expiryLong);
	
 }

 function saveProjectParam(obj){
	var project = bprj.value;
	SetCookie(obj.id+project, obj.value, expiryLong);
 }

 function getProjectParam(obj){
	var project = "";
	if (document.getElementById('bprj'))
		project = bprj.value;
	else
		project = getProject();
	
	return GetCookie(obj+project);
 }

 function loadEmail(){
	if (document.getElementById('email'))
		email.value = GetCookie('email');
	
 }


 function updateWarningCaseLayerIfShown(){

   if (isWarningCaseLayerShown())
   	showWarningCaseLayer();

 }

 function switchPrevWarningCase(){
   prevSelect();
   updateMsgAddMainPage()
   showWarningCaseLayer();
 }

 function switchNextWarningCase(){
   nextSelect();
   updateMsgAddMainPage()
   showWarningCaseLayer();
 }

 function showWarningCaseLayer(){

   var warningCasesLayer = document.getElementById("warningCasesLayer");
   warningCasesLayer.style.left = document.body.scrollLeft;
   warningCasesLayer.style.top  = document.body.scrollTop;

   var innerHTML = createWarningCaseLayerHTML();
   warningCasesLayer.innerHTML = innerHTML;

   warningCasesLayer.style.visibility = 'visible'; 
   isShown = true;
   warningSelectObj.focus();
   
		
 }

 function updateMsgAddMainPage(){

    eval(warningSelectObj.options[warningSelectObj.selectedIndex].value);
    var jid    = d[0];
    var msgAdd = d[3];

    document.getElementById(jid+'msgAdd').innerHTML = "<font color=grey> "+msgAdd+" </font>"; 

 }

 function hideWarningCaseLayer(){
    var warningCasesLayer = document.getElementById("warningCasesLayer");
    warningCasesLayer.style.visibility = 'hidden'; 
    var treeLog = document.getElementById("treelog");
    treeLog.style.visibility = 'hidden';
    isShown = false;   
 }

 function previewTreeLog(event, test, id, threadId, nodeUrl, resultDir) {
	  
	  var treelog = document.getElementById("treelog");

	  if (treelog.style.visibility == 'visible'){
		  treelog.style.visibility = 'hidden';
		  return;
	  }  
	  
	  //alert(nodeUrl+"/form/status/treeloghtml?resultDir="+resultDir+"&className="+test+"&threadId="+threadId+"&id="+id);
	  
	  $.get(nodeUrl+"/form/status/treeloghtml?resultDir="+resultDir+"&className="+test+"&threadId="+threadId+"&id="+id,
			  function(data) {
		  		$("#treelog").html(data);
	
		  		treelog.style.left = event.clientX;
		  		treelog.style.top  = event.clientY;
		  		treelog.style.visibility = 'visible'; 

		  		refreshPage();
		  		
			  }
	  		);
	  		
}

 function createWarningCaseLayerHTML(){

	// d[0] - jid
	// d[1] - suiteTitle
	// d[2] - testTitle
	// d[3] - msgAdd

  eval(warningSelectObj.options[warningSelectObj.selectedIndex].value);

  var jid = d[0];
  var suiteTitle = d[1];
  var testTitle  = d[2];
  var msgAdd     = d[3];
  var msg = document.getElementById(jid+"msg").innerHTML;
 
 
  var h = document.body.clientHeight;
  var w = document.body.clientWidth;

  var h1 = h/5;
  var h2 = h-h1;

  var innerHTML = "<table width="+w+" height="+h1+" >"+
  "<tr> <td width='200px'> "+

  "<div id=id1 > Test: "+testTitle+
  "<BR> Suite: "+suiteTitle+
  "<BR> Case: "+(warningSelectObj.selectedIndex+1)+"/"+warningSelectObj.length+
  "</div> " +

  "<BR> <font color=blue> " +
  " <a id=prev style='text-decoration:underline;' onclick='switchPrevWarningCase();' href='javascript::return%20false;'> <-prev  </a> &nbsp;" + 
  " <a id=next style='text-decoration:underline;' onclick='switchNextWarningCase();' href='javascript::return%20false;'> next-> </a> &nbsp;" +
  " <a id=exit style='text-decoration:underline;' onclick='hideWarningCaseLayer();'  href='javascript::return%20false;'> exit </a>" +
  " </font>"+
  " </td>"+
  
  "<td> Error: <div  id=id1dmsg > "+msg+" <BR> "+msgAdd+" </div></td>" +
  "<td> JIRA-1235 </td>" +
  "</tr></table> ";

  innerHTML +=  "<table width="+w+" height="+h2+" align=center style='text-align: center'>";

  var href = "";
  for (i = 4; i < d.length; i++){
    item = d[i];
    if (item.type == 'HREF'){
      href += "<A href='"+item.href+"'>"+item.name+"</A> &nbsp; &nbsp;";
    }
  }

  for (i = 4; i < d.length; i++){
	    item = d[i];
	    if (item.type == 'TreeLog'){
	      href += "<A href='#' onclick=\"previewTreeLog(event,\'"+testTitle+"\',\'"+item.id+"\',\'"+item.threadId+"\',\'"+item.nodeUrl+"\', \'"+suiteTitle+"\');\" >treelog</A> &nbsp; &nbsp;";
	    }
	  }

  if (href != "")
   innerHTML +=  "<tr><td> <font size=3> "+href+"</font> </td></tr>";
  
  for (i = 4; i < d.length; i++){
    item = d[i];
    if (item.type == 'IMG')
      innerHTML += "<tr><td><img src='"+item.img+"'></img></td></tr>";
    
  }

  for (i = 4; i < d.length; i++){
	 item = d[i];
	 if (item.type == 'IFrameView')
	  innerHTML += "<tr><td><iframe src='"+item.href+"' width='80%' height=300 frameborder=1 > </iframe></td></tr>";    
  }

  innerHTML +=  "</table>";
  return innerHTML;

 }


 

 // если изменился элемент для отображения - обнови ссылку на слое
 function updateDivLink(){
	// d[0] - jid
	// d[1] - url
	// d[2] - jpg
	// d[3] - html
	// d[4] - testLog
	// d[5] - testTitle
	// d[6] - suiteTitle
	// d[7] - msgAdd

	

	
  if ((warningSelectObj)) {
	warningSelectObj.focus();

	clearSelectColor();
	warningSelectObj.style.color = "#0000AA";

	eval(warningSelectObj.options[warningSelectObj.selectedIndex].value);
	var lay = d[0];


	document.getElementById(id+"durl").href     = d[1]; 
	document.getElementById(id+"djpg").src      = d[2]; 
	document.getElementById(id+'dhtml').href    = d[3]; 
	document.getElementById(id+'dtestLog').href = d[4]; 

	//информация об ошибке на странице. специфика
	document.getElementById(lay+'msgAdd').innerHTML = "<font color=grey> "+d[7]+" </font>"; 


	//информация об ошибке на слое
        document.getElementById(id+"dmsg").innerHTML  =                 							                                                                document.getElementById(lay+"msg").innerHTML+"<BR>"+
							        document.getElementById(lay+"msgAdd").innerHTML;

	//информация о тесте на странице
        document.getElementById(id+"dtest").innerHTML =   "Test: "+warningSelectObj.options[warningSelectObj.selectedIndex].text+"<BR>"+
							  "Desc: "+d[5]+" <BR>"+
		                 			  "Suite: "+d[6]+" <BR>"+
                                                          "Case#: "+(warningSelectObj.selectedIndex+1)+"/"+warningSelectObj.length;

	// слой баги поменять
	if (document.getElementById(lay+"bugdiv") != null) {
		document.getElementById("bugdiv").innerHTML = document.getElementById(lay+"bugdiv").innerHTML; 
	} else {
		if (document.getElementById("bugdiv") != null){
	          var submitonline = "<BR> <a href='#aabb123' onclick='submit();'>auto submit</a>";
           	  var bugIDlink = "<A target='_blank' href='https://jira.yandex-team.ru/secure/CreateIssue!default.jspa'> <font color='red'> create new 			issue </font> </A>" + submitonline;

	          document.getElementById("bugdiv").innerHTML = bugIDlink; 
		}
	}
	
	


  }

 }




function onPageLoad() {

   // вешаем обработчик причины ошибки, чтобы сохранялись куки (select причины)
   tags=document.body.getElementsByTagName('SELECT');
   for(var j=0; j<tags.length; j++)
   	if (tags[j].className == 'action') {
		if (GetCookie(tags[j].id+"")) tags[j].selectedIndex = GetCookie(tags[j].id+"")*1 ;

		tags[j].onchange = function(ev) {
			SetCookie(ev.target.id, ev.target.selectedIndex, expiry);
		}
	}
		

   // вешаем обработчик причины ошибки, чтобы сохранялись куки (checkbox причины)
   tags=document.body.getElementsByTagName('INPUT');
   for(var j=0; j<tags.length; j++)
   	if (tags[j].className == 'action') {

		if (GetCookie(tags[j].id+"")) {
			if (GetCookie(tags[j].id+"") == 'true')	tags[j].checked = true;
		}

		tags[j].onclick = function(ev) {
			SetCookie(ev.target.id, ev.target.checked, expiryToday);
		}
	}


    warningSelectObj=id1s;
    
}


function updateSave(){

	bcc.value = getProjectParam('bcc');
	basn.value = getProjectParam('basn');

}
