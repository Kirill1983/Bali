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
 var focusWarnId = 0;

// Rerun start ********************************** 
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

// Params start ********************************** 


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

// Report view **************************

 
 function updateWarningCaseLayerIfShown(){

   if (isWarningCaseLayerShown())
   	showWarningCaseLayer();

 }

 function isWarningCaseLayerShown(){

    var warningCasesLayer = document.getElementById("warningCasesLayer");
    if ( warningCasesLayer.style.visibility == 'hidden')
    	return false;
    else 
        return true;
          
 } 


 function switchPrevWarningCase(){
   prevSelect();
   updateMsgAddMainPage()
   showWarningCaseLayerNG(focusWarnId);
 }

 function switchNextWarningCase(){
   nextSelect();
   updateMsgAddMainPage()
   showWarningCaseLayerNG(focusWarnId);
 }

 function nextSelect(){
	var warnSelect = document.getElementById(focusWarnId);
	if (warnSelect.selectedIndex == (warnSelect.length-1))
		warnSelect.selectedIndex = 0;
	else 
		warnSelect.selectedIndex++;
 }

 function prevSelect(){
	var warnSelect = document.getElementById(focusWarnId);
	if (warnSelect.selectedIndex == 0)
		warnSelect.selectedIndex = warnSelect.length-1;
	else 
		warnSelect.selectedIndex--;
 }

 function hideWarningCaseLayer(){
    var warningCasesLayer = document.getElementById("warningCasesLayer");
    warningCasesLayer.style.visibility = 'hidden'; 
    var treeLog = document.getElementById("treelog");
    treeLog.style.visibility = 'hidden';
    isShown = false;   
 }

 function showWarningCaseLayerNG(){

   var warningCasesLayer = document.getElementById("warningCasesLayer");
   warningCasesLayer.style.left = document.body.scrollLeft;
   warningCasesLayer.style.top  = document.body.scrollTop;
   warningCasesLayer.innerHTML = createWarningCaseLayerHTMLNG();
   warningCasesLayer.style.visibility = 'visible'; 
   isShown = true;   
		
 }

 function createWarningCaseLayerHTMLNG(){

  var warnSelect = document.getElementById(focusWarnId);
  var warnCaseId = warnSelect.selectedIndex;

  var warn = data.warns[focusWarnId];
  var warnCase = warn.cases[warnCaseId];

  var h = document.body.clientHeight;
  var w = document.body.clientWidth;

  var h1 = h/5;
  var h2 = h-h1;

  var innerHTML = "<table width="+w+" height="+h1+" >"+
  "<tr> <td width='200px'> "+

  "<div id=id1 > Test: "+warnCase.test+
  "<BR> Suite: "+warnCase.suite+
  "<BR> Case: "+(warnCaseId+1)+"/"+warnSelect.length+
  "</div> " +

  "<BR> <font color=blue> " +
  " <a id=prev style='text-decoration:underline;' onclick='switchPrevWarningCase();' href='javascript::return%20false;'> <-prev  </a> &nbsp;" + 
  " <a id=next style='text-decoration:underline;' onclick='switchNextWarningCase();' href='javascript::return%20false;'> next-> </a> &nbsp;" +
  " <a id=exit style='text-decoration:underline;' onclick='hideWarningCaseLayer();'  href='javascript::return%20false;'> exit </a>" +
  " </font>"+
  " </td>"+
  
  "<td> Error: <div  id=id1dmsg > "+warn.msg+" <BR> "+warnCase.msgAdd+" </div></td>" +
  "<td> JIRA-1235 </td>" +
  "</tr></table> ";

  innerHTML +=  "<table width="+w+" height="+h2+" align=center style='text-align: center'>";
  innerHTML +=  addArtifactsToHTML(warnCase.test, warnCase.suite, warnCase.artifacts);
  innerHTML +=  "</table>";
  return innerHTML;

 }


 function updateMsgAddMainPage(){
    var warnSelect = document.getElementById(focusWarnId)
    var warnCaseId = warnSelect.selectedIndex;
     
    var warn = data.warns[focusWarnId];
    var warnCase = warn.cases[warnCaseId];

    document.getElementById("id"+focusWarnId+'msgAdd').innerHTML = "<font color=grey> "+warnCase.msgAdd+" </font>"; 
 }


 function addArtifactsToHTML(testTitle, suiteTitle, artifacts){

  var innerHTML = "";
  var artifactLink = "";
  for (i = 0; i < artifacts.length; i++){
    item = artifacts[i];
    if (item.type == 'HREF'){
      artifactLink += "<A href='"+item.href+"'>"+item.name+"</A> &nbsp; &nbsp;";
    }
  }

  for (i = 0; i < artifacts.length; i++){
        item = artifacts[i];
	if (item.type == 'TreeLog'){
	     artifactLink += "<A href='#' onclick=\"previewTreeLog(event,\'"+testTitle+"\',\'"+item.id+"\',\'"+item.threadId+"\',\'"+item.nodeUrl+"\', \'"+suiteTitle+"\');\" >treelog</A> &nbsp; &nbsp;";
	   }
	}

  if (artifactLink != "")
   innerHTML +=  "<tr><td> <font size=3> "+artifactLink+"</font> </td></tr>";
  
  for (i = 0; i < artifacts.length; i++){
    item = artifacts[i];
    if (item.type == 'IMG')
      innerHTML += "<tr><td><img src='"+item.img+"'></img></td></tr>";
  }

  for (i = 0; i < artifacts.length; i++){
     item = artifacts[i];
     if (item.type == 'IFrameView') 
      innerHTML += "<tr><td><iframe src='"+item.href+"' width='80%' height=300 frameborder=1 > </iframe></td></tr>";    
  }

   return innerHTML;
 }

function previewTreeLog(event, test, id, threadId, nodeUrl, resultDir) {
	  
	  var treelog = document.getElementById("treelog");

	  if (treelog.style.visibility == 'visible'){
		  treelog.style.visibility = 'hidden';
		  return;
	  }  
	  
	  
	  $.get(nodeUrl+"/form/status/treeloghtml?resultDir="+resultDir+"&className="+test+"&threadId="+threadId+"&id="+id,
			  function(data) {
		  		$("#treelog").html(data);
	
		  		treelog.style.left = event.clientX;
		  		treelog.style.top  = event.clientY;
		  		treelog.style.visibility = 'visible'; 

		  		
			  }
	  		);
	  		
}

 

// ���� ������ �� �������� ���� ��� ����� � ��������� �������� �������� ��� �����������

 function keyPress(ev){

	ev = ev || window.event;
	var code = (ev.keyCode || ev.which);

	if (ev.target.id == 'email')
		return true;

	if ((code != 37) && (code != 38) && (code != 39) && (code != 40) && (code != 32) && (code != 27)){
		return true;
        }

        //if (isWarningCaseLayerShown()) {
	   if ((code == 37) || (code == 38) || (code == 39) || (code == 40)){
                alert(code);
	 	showWarningCaseLayer();
		return true;
	   }
	//}

        // ������ - �������� - ��������� �����������
        if (code == 32 || code == 27) {
		if (isWarningCaseLayerShown())
   		  hideWarningCaseLayer();
                else{
		  showWarningCaseLayerNG();	
		}

	}

 }


function onPageLoad() {

   // ������ ���������� ������� ������, ����� ����������� ���� (select �������)
   tags=document.body.getElementsByTagName('SELECT');
   for(var j=0; j<tags.length; j++)
   	if (tags[j].className == 'action') {
		if (GetCookie(tags[j].id+"")) tags[j].selectedIndex = GetCookie(tags[j].id+"")*1 ;

		tags[j].onchange = function(ev) {
			SetCookie(ev.target.id, ev.target.selectedIndex, expiry);
		}
	}
		

   // ������ ���������� ������� ������, ����� ����������� ���� (checkbox �������)
   tags=document.body.getElementsByTagName('INPUT');
   for(var j=0; j<tags.length; j++) {

   	if (tags[j].className == 'action') {

		if (GetCookie(tags[j].id+"")) 
			if (GetCookie(tags[j].id+"") == 'true')	tags[j].checked = true;

		tags[j].onclick = function(ev) {
			SetCookie(ev.target.id, ev.target.checked, expiryToday);
		}
		
	}

   	if (tags[j].className == 'warnCases') {
		tags[j].onchange = function(ev) {
			var id = ev.target.id;
			focusWarnId = id;
			updateMsgAddMainPage();
		}
	}
   }


   tags=document.body.getElementsByTagName('select');
   for(var j=0; j<tags.length; j++) 
   	if (tags[j].className == 'warnCases') {

		tags[j].onchange = function(ev) {

			var id = ev.target.id;
			focusWarnId = id;

			if (isWarningCaseLayerShown())				
				showWarningCaseLayerNG();
			else
				updateMsgAddMainPage();
			
		}

	}
   


   tags=document.body.getElementsByTagName('a');
   for(var j=0; j<tags.length; j++)
   	if (tags[j].className == 'view') {
		warnId = tags[j].getAttribute("warnid");

		tags[j].onclick = function(ev) {
			var id = ev.target.getAttribute("warnid");
			focusWarnId = id;
			showWarningCaseLayerNG();
		}

	}

    document.onkeypress = function (ev) { keyPress(ev);}
    warningCasesLayer.onkeypress = function (ev) { keyPress(ev);}

    
}


function updateSave(){

	bcc.value = getProjectParam('bcc');
	basn.value = getProjectParam('basn');

}
