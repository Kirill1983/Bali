

// sarissa.js

function Sarissa(){}
Sarissa.VERSION="0.9.9.4";Sarissa.PARSED_OK="Document contains no parsing errors";Sarissa.PARSED_EMPTY="Document is empty";Sarissa.PARSED_UNKNOWN_ERROR="Not well-formed or other error";Sarissa.IS_ENABLED_TRANSFORM_NODE=false;Sarissa.REMOTE_CALL_FLAG="gr.abiss.sarissa.REMOTE_CALL_FLAG";Sarissa._lastUniqueSuffix=0;Sarissa._getUniqueSuffix=function(){return Sarissa._lastUniqueSuffix++;};Sarissa._SARISSA_IEPREFIX4XSLPARAM="";Sarissa._SARISSA_HAS_DOM_IMPLEMENTATION=document.implementation&&true;Sarissa._SARISSA_HAS_DOM_CREATE_DOCUMENT=Sarissa._SARISSA_HAS_DOM_IMPLEMENTATION&&document.implementation.createDocument;Sarissa._SARISSA_HAS_DOM_FEATURE=Sarissa._SARISSA_HAS_DOM_IMPLEMENTATION&&document.implementation.hasFeature;Sarissa._SARISSA_IS_MOZ=Sarissa._SARISSA_HAS_DOM_CREATE_DOCUMENT&&Sarissa._SARISSA_HAS_DOM_FEATURE;Sarissa._SARISSA_IS_SAFARI=navigator.userAgent.toLowerCase().indexOf("safari")!=-1||navigator.userAgent.toLowerCase().indexOf("konqueror")!=-1;Sarissa._SARISSA_IS_SAFARI_OLD=Sarissa._SARISSA_IS_SAFARI&&(parseInt((navigator.userAgent.match(/AppleWebKit\/(\d+)/)||{})[1],10)<420);Sarissa._SARISSA_IS_IE=document.all&&window.ActiveXObject&&navigator.userAgent.toLowerCase().indexOf("msie")>-1&&navigator.userAgent.toLowerCase().indexOf("opera")==-1;Sarissa._SARISSA_IS_OPERA=navigator.userAgent.toLowerCase().indexOf("opera")!=-1;if(!window.Node||!Node.ELEMENT_NODE){Node={ELEMENT_NODE:1,ATTRIBUTE_NODE:2,TEXT_NODE:3,CDATA_SECTION_NODE:4,ENTITY_REFERENCE_NODE:5,ENTITY_NODE:6,PROCESSING_INSTRUCTION_NODE:7,COMMENT_NODE:8,DOCUMENT_NODE:9,DOCUMENT_TYPE_NODE:10,DOCUMENT_FRAGMENT_NODE:11,NOTATION_NODE:12};}
if(Sarissa._SARISSA_IS_SAFARI_OLD){HTMLHtmlElement=document.createElement("html").constructor;Node=HTMLElement={};HTMLElement.prototype=HTMLHtmlElement.__proto__.__proto__;HTMLDocument=Document=document.constructor;var x=new DOMParser();XMLDocument=x.constructor;Element=x.parseFromString("<Single />","text/xml").documentElement.constructor;x=null;}
if(typeof XMLDocument=="undefined"&&typeof Document!="undefined"){XMLDocument=Document;}
if(Sarissa._SARISSA_IS_IE){Sarissa._SARISSA_IEPREFIX4XSLPARAM="xsl:";var _SARISSA_DOM_PROGID="";var _SARISSA_XMLHTTP_PROGID="";var _SARISSA_DOM_XMLWRITER="";Sarissa.pickRecentProgID=function(idList){var bFound=false,e;var o2Store;for(var i=0;i<idList.length&&!bFound;i++){try{var oDoc=new ActiveXObject(idList[i]);o2Store=idList[i];bFound=true;}catch(objException){e=objException;}}
if(!bFound){throw"Could not retrieve a valid progID of Class: "+idList[idList.length-1]+". (original exception: "+e+")";}
idList=null;return o2Store;};_SARISSA_DOM_PROGID=null;_SARISSA_THREADEDDOM_PROGID=null;_SARISSA_XSLTEMPLATE_PROGID=null;_SARISSA_XMLHTTP_PROGID=null;XMLHttpRequest=function(){if(!_SARISSA_XMLHTTP_PROGID){_SARISSA_XMLHTTP_PROGID=Sarissa.pickRecentProgID(["Msxml2.XMLHTTP.6.0","MSXML2.XMLHTTP.3.0","MSXML2.XMLHTTP","Microsoft.XMLHTTP"]);}
return new ActiveXObject(_SARISSA_XMLHTTP_PROGID);};Sarissa.getDomDocument=function(sUri,sName){if(!_SARISSA_DOM_PROGID){_SARISSA_DOM_PROGID=Sarissa.pickRecentProgID(["Msxml2.DOMDocument.6.0","Msxml2.DOMDocument.3.0","MSXML2.DOMDocument","MSXML.DOMDocument","Microsoft.XMLDOM"]);}
var oDoc=new ActiveXObject(_SARISSA_DOM_PROGID);if(sName){var prefix="";if(sUri){if(sName.indexOf(":")>1){prefix=sName.substring(0,sName.indexOf(":"));sName=sName.substring(sName.indexOf(":")+1);}else{prefix="a"+Sarissa._getUniqueSuffix();}}
if(sUri){oDoc.loadXML('<'+prefix+':'+sName+" xmlns:"+prefix+"=\""+sUri+"\""+" />");}else{oDoc.loadXML('<'+sName+" />");}}
return oDoc;};Sarissa.getParseErrorText=function(oDoc){var parseErrorText=Sarissa.PARSED_OK;if(oDoc&&oDoc.parseError&&oDoc.parseError.errorCode&&oDoc.parseError.errorCode!=0){parseErrorText="XML Parsing Error: "+oDoc.parseError.reason+"\nLocation: "+oDoc.parseError.url+"\nLine Number "+oDoc.parseError.line+", Column "+
oDoc.parseError.linepos+":\n"+oDoc.parseError.srcText+"\n";for(var i=0;i<oDoc.parseError.linepos;i++){parseErrorText+="-";}
parseErrorText+="^\n";}
else if(oDoc.documentElement===null){parseErrorText=Sarissa.PARSED_EMPTY;}
return parseErrorText;};Sarissa.setXpathNamespaces=function(oDoc,sNsSet){oDoc.setProperty("SelectionLanguage","XPath");oDoc.setProperty("SelectionNamespaces",sNsSet);};XSLTProcessor=function(){if(!_SARISSA_XSLTEMPLATE_PROGID){_SARISSA_XSLTEMPLATE_PROGID=Sarissa.pickRecentProgID(["Msxml2.XSLTemplate.6.0","MSXML2.XSLTemplate.3.0"]);}
this.template=new ActiveXObject(_SARISSA_XSLTEMPLATE_PROGID);this.processor=null;};XSLTProcessor.prototype.importStylesheet=function(xslDoc){if(!_SARISSA_THREADEDDOM_PROGID){_SARISSA_THREADEDDOM_PROGID=Sarissa.pickRecentProgID(["MSXML2.FreeThreadedDOMDocument.6.0","MSXML2.FreeThreadedDOMDocument.3.0"]);}
xslDoc.setProperty("SelectionLanguage","XPath");xslDoc.setProperty("SelectionNamespaces","xmlns:xsl='http://www.w3.org/1999/XSL/Transform'");var converted=new ActiveXObject(_SARISSA_THREADEDDOM_PROGID);try{converted.resolveExternals=true;converted.setProperty("AllowDocumentFunction",true);}
catch(e){}
if(xslDoc.url&&xslDoc.selectSingleNode("//xsl:*[local-name() = 'import' or local-name() = 'include']")!=null){converted.async=false;converted.load(xslDoc.url);}
else{converted.loadXML(xslDoc.xml);}
converted.setProperty("SelectionNamespaces","xmlns:xsl='http://www.w3.org/1999/XSL/Transform'");var output=converted.selectSingleNode("//xsl:output");if(output){this.outputMethod=output.getAttribute("method");}
else{delete this.outputMethod;}
this.template.stylesheet=converted;this.processor=this.template.createProcessor();this.paramsSet=[];};XSLTProcessor.prototype.transformToDocument=function(sourceDoc){var outDoc;if(_SARISSA_THREADEDDOM_PROGID){this.processor.input=sourceDoc;outDoc=new ActiveXObject(_SARISSA_DOM_PROGID);this.processor.output=outDoc;this.processor.transform();return outDoc;}
else{if(!_SARISSA_DOM_XMLWRITER){_SARISSA_DOM_XMLWRITER=Sarissa.pickRecentProgID(["Msxml2.MXXMLWriter.6.0","Msxml2.MXXMLWriter.3.0","MSXML2.MXXMLWriter","MSXML.MXXMLWriter","Microsoft.XMLDOM"]);}
this.processor.input=sourceDoc;outDoc=new ActiveXObject(_SARISSA_DOM_XMLWRITER);this.processor.output=outDoc;this.processor.transform();var oDoc=new ActiveXObject(_SARISSA_DOM_PROGID);oDoc.loadXML(outDoc.output+"");return oDoc;}};XSLTProcessor.prototype.transformToFragment=function(sourceDoc,ownerDoc){this.processor.input=sourceDoc;this.processor.transform();var s=this.processor.output;var f=ownerDoc.createDocumentFragment();var container;if(this.outputMethod=='text'){f.appendChild(ownerDoc.createTextNode(s));}else if(ownerDoc.body&&ownerDoc.body.innerHTML){container=ownerDoc.createElement('div');container.innerHTML=s;while(container.hasChildNodes()){f.appendChild(container.firstChild);}}
else{var oDoc=new ActiveXObject(_SARISSA_DOM_PROGID);if(s.substring(0,5)=='<?xml'){s=s.substring(s.indexOf('?>')+2);}
var xml=''.concat('<my>',s,'</my>');oDoc.loadXML(xml);container=oDoc.documentElement;while(container.hasChildNodes()){f.appendChild(container.firstChild);}}
return f;};XSLTProcessor.prototype.setParameter=function(nsURI,name,value){value=value?value:"";if(nsURI){this.processor.addParameter(name,value,nsURI);}else{this.processor.addParameter(name,value);}
nsURI=""+(nsURI||"");if(!this.paramsSet[nsURI]){this.paramsSet[nsURI]=[];}
this.paramsSet[nsURI][name]=value;};XSLTProcessor.prototype.getParameter=function(nsURI,name){nsURI=""+(nsURI||"");if(this.paramsSet[nsURI]&&this.paramsSet[nsURI][name]){return this.paramsSet[nsURI][name];}else{return null;}};XSLTProcessor.prototype.clearParameters=function(){for(var nsURI in this.paramsSet){for(var name in this.paramsSet[nsURI]){if(nsURI!=""){this.processor.addParameter(name,"",nsURI);}else{this.processor.addParameter(name,"");}}}
this.paramsSet=[];};}else{if(Sarissa._SARISSA_HAS_DOM_CREATE_DOCUMENT){Sarissa.__handleLoad__=function(oDoc){Sarissa.__setReadyState__(oDoc,4);};_sarissa_XMLDocument_onload=function(){Sarissa.__handleLoad__(this);};Sarissa.__setReadyState__=function(oDoc,iReadyState){oDoc.readyState=iReadyState;oDoc.readystate=iReadyState;if(oDoc.onreadystatechange!=null&&typeof oDoc.onreadystatechange=="function"){oDoc.onreadystatechange();}};Sarissa.getDomDocument=function(sUri,sName){var oDoc=document.implementation.createDocument(sUri?sUri:null,sName?sName:null,null);if(!oDoc.onreadystatechange){oDoc.onreadystatechange=null;}
if(!oDoc.readyState){oDoc.readyState=0;}
oDoc.addEventListener("load",_sarissa_XMLDocument_onload,false);return oDoc;};if(window.XMLDocument){}
else if(Sarissa._SARISSA_HAS_DOM_FEATURE&&window.Document&&!Document.prototype.load&&document.implementation.hasFeature('LS','3.0')){Sarissa.getDomDocument=function(sUri,sName){var oDoc=document.implementation.createDocument(sUri?sUri:null,sName?sName:null,null);return oDoc;};}
else{Sarissa.getDomDocument=function(sUri,sName){var oDoc=document.implementation.createDocument(sUri?sUri:null,sName?sName:null,null);if(oDoc&&(sUri||sName)&&!oDoc.documentElement){oDoc.appendChild(oDoc.createElementNS(sUri,sName));}
return oDoc;};}}}
if(!window.DOMParser){if(Sarissa._SARISSA_IS_SAFARI){DOMParser=function(){};DOMParser.prototype.parseFromString=function(sXml,contentType){var xmlhttp=new XMLHttpRequest();xmlhttp.open("GET","data:text/xml;charset=utf-8,"+encodeURIComponent(sXml),false);xmlhttp.send(null);return xmlhttp.responseXML;};}else if(Sarissa.getDomDocument&&Sarissa.getDomDocument()&&Sarissa.getDomDocument(null,"bar").xml){DOMParser=function(){};DOMParser.prototype.parseFromString=function(sXml,contentType){var doc=Sarissa.getDomDocument();doc.loadXML(sXml);return doc;};}}
if((typeof(document.importNode)=="undefined")&&Sarissa._SARISSA_IS_IE){try{document.importNode=function(oNode,bChildren){var tmp;if(oNode.nodeName=='#text'){return document.createTextNode(oNode.data);}
else{if(oNode.nodeName=="tbody"||oNode.nodeName=="tr"){tmp=document.createElement("table");}
else if(oNode.nodeName=="td"){tmp=document.createElement("tr");}
else if(oNode.nodeName=="option"){tmp=document.createElement("select");}
else{tmp=document.createElement("div");}
if(bChildren){tmp.innerHTML=oNode.xml?oNode.xml:oNode.outerHTML;}else{tmp.innerHTML=oNode.xml?oNode.cloneNode(false).xml:oNode.cloneNode(false).outerHTML;}
return tmp.getElementsByTagName("*")[0];}};}catch(e){}}
if(!Sarissa.getParseErrorText){Sarissa.getParseErrorText=function(oDoc){var parseErrorText=Sarissa.PARSED_OK;if((!oDoc)||(!oDoc.documentElement)){parseErrorText=Sarissa.PARSED_EMPTY;}else if(oDoc.documentElement.tagName=="parsererror"){parseErrorText=oDoc.documentElement.firstChild.data;parseErrorText+="\n"+oDoc.documentElement.firstChild.nextSibling.firstChild.data;}else if(oDoc.getElementsByTagName("parsererror").length>0){var parsererror=oDoc.getElementsByTagName("parsererror")[0];parseErrorText=Sarissa.getText(parsererror,true)+"\n";}else if(oDoc.parseError&&oDoc.parseError.errorCode!=0){parseErrorText=Sarissa.PARSED_UNKNOWN_ERROR;}
return parseErrorText;};}
Sarissa.getText=function(oNode,deep){var s="";var nodes=oNode.childNodes;for(var i=0;i<nodes.length;i++){var node=nodes[i];var nodeType=node.nodeType;if(nodeType==Node.TEXT_NODE||nodeType==Node.CDATA_SECTION_NODE){s+=node.data;}else if(deep===true&&(nodeType==Node.ELEMENT_NODE||nodeType==Node.DOCUMENT_NODE||nodeType==Node.DOCUMENT_FRAGMENT_NODE)){s+=Sarissa.getText(node,true);}}
return s;};if(!window.XMLSerializer&&Sarissa.getDomDocument&&Sarissa.getDomDocument("","foo",null).xml){XMLSerializer=function(){};XMLSerializer.prototype.serializeToString=function(oNode){return oNode.xml;};}
Sarissa.stripTags=function(s){return s?s.replace(/<[^>]+>/g,""):s;};Sarissa.clearChildNodes=function(oNode){while(oNode.firstChild){oNode.removeChild(oNode.firstChild);}};Sarissa.copyChildNodes=function(nodeFrom,nodeTo,bPreserveExisting){if(Sarissa._SARISSA_IS_SAFARI&&nodeTo.nodeType==Node.DOCUMENT_NODE){nodeTo=nodeTo.documentElement;}
if((!nodeFrom)||(!nodeTo)){throw"Both source and destination nodes must be provided";}
if(!bPreserveExisting){Sarissa.clearChildNodes(nodeTo);}
var ownerDoc=nodeTo.nodeType==Node.DOCUMENT_NODE?nodeTo:nodeTo.ownerDocument;var nodes=nodeFrom.childNodes;var i;if(typeof(ownerDoc.importNode)!="undefined"){for(i=0;i<nodes.length;i++){nodeTo.appendChild(ownerDoc.importNode(nodes[i],true));}}else{for(i=0;i<nodes.length;i++){nodeTo.appendChild(nodes[i].cloneNode(true));}}};Sarissa.moveChildNodes=function(nodeFrom,nodeTo,bPreserveExisting){if((!nodeFrom)||(!nodeTo)){throw"Both source and destination nodes must be provided";}
if(!bPreserveExisting){Sarissa.clearChildNodes(nodeTo);}
var nodes=nodeFrom.childNodes;if(nodeFrom.ownerDocument==nodeTo.ownerDocument){while(nodeFrom.firstChild){nodeTo.appendChild(nodeFrom.firstChild);}}else{var ownerDoc=nodeTo.nodeType==Node.DOCUMENT_NODE?nodeTo:nodeTo.ownerDocument;var i;if(typeof(ownerDoc.importNode)!="undefined"){for(i=0;i<nodes.length;i++){nodeTo.appendChild(ownerDoc.importNode(nodes[i],true));}}else{for(i=0;i<nodes.length;i++){nodeTo.appendChild(nodes[i].cloneNode(true));}}
Sarissa.clearChildNodes(nodeFrom);}};Sarissa.xmlize=function(anyObject,objectName,indentSpace){indentSpace=indentSpace?indentSpace:'';var s=indentSpace+'<'+objectName+'>';var isLeaf=false;if(!(anyObject instanceof Object)||anyObject instanceof Number||anyObject instanceof String||anyObject instanceof Boolean||anyObject instanceof Date){s+=Sarissa.escape(""+anyObject);isLeaf=true;}else{s+="\n";var isArrayItem=anyObject instanceof Array;for(var name in anyObject){s+=Sarissa.xmlize(anyObject[name],(isArrayItem?"array-item key=\""+name+"\"":name),indentSpace+"   ");}
s+=indentSpace;}
return(s+=(objectName.indexOf(' ')!=-1?"</array-item>\n":"</"+objectName+">\n"));};Sarissa.escape=function(sXml){return sXml.replace(/&/g,"&amp;").replace(/</g,"&lt;").replace(/>/g,"&gt;").replace(/"/g,"&quot;").replace(/'/g,"&apos;");};Sarissa.unescape=function(sXml){return sXml.replace(/&apos;/g,"'").replace(/&quot;/g,"\"").replace(/&gt;/g,">").replace(/&lt;/g,"<").replace(/&amp;/g,"&");};Sarissa.updateCursor=function(oTargetElement,sValue){if(oTargetElement&&oTargetElement.style&&oTargetElement.style.cursor!=undefined){oTargetElement.style.cursor=sValue;}};Sarissa.updateContentFromURI=function(sFromUrl,oTargetElement,xsltproc,callback,skipCache){try{Sarissa.updateCursor(oTargetElement,"wait");var xmlhttp=new XMLHttpRequest();xmlhttp.open("GET",sFromUrl,true);xmlhttp.onreadystatechange=function(){if(xmlhttp.readyState==4){try{var oDomDoc=xmlhttp.responseXML;if(oDomDoc&&Sarissa.getParseErrorText(oDomDoc)==Sarissa.PARSED_OK){Sarissa.updateContentFromNode(xmlhttp.responseXML,oTargetElement,xsltproc);if(callback){callback(sFromUrl,oTargetElement);}}
else{throw Sarissa.getParseErrorText(oDomDoc);}}
catch(e){if(callback){callback(sFromUrl,oTargetElement,e);}
else{throw e;}}}};if(skipCache){var oldage="Sat, 1 Jan 2000 00:00:00 GMT";xmlhttp.setRequestHeader("If-Modified-Since",oldage);}
xmlhttp.send("");}
catch(e){Sarissa.updateCursor(oTargetElement,"auto");if(callback){callback(sFromUrl,oTargetElement,e);}
else{throw e;}}};Sarissa.updateContentFromNode=function(oNode,oTargetElement,xsltproc){try{Sarissa.updateCursor(oTargetElement,"wait");Sarissa.clearChildNodes(oTargetElement);var ownerDoc=oNode.nodeType==Node.DOCUMENT_NODE?oNode:oNode.ownerDocument;if(ownerDoc.parseError&&ownerDoc.parseError.errorCode!=0){var pre=document.createElement("pre");pre.appendChild(document.createTextNode(Sarissa.getParseErrorText(ownerDoc)));oTargetElement.appendChild(pre);}
else{if(xsltproc){oNode=xsltproc.transformToDocument(oNode);}
if(oTargetElement.tagName.toLowerCase()=="textarea"||oTargetElement.tagName.toLowerCase()=="input"){oTargetElement.value=new XMLSerializer().serializeToString(oNode);}
else{try{oTargetElement.appendChild(oTargetElement.ownerDocument.importNode(oNode,true));}
catch(e){oTargetElement.innerHTML=new XMLSerializer().serializeToString(oNode);}}}}
catch(e){throw e;}
finally{Sarissa.updateCursor(oTargetElement,"auto");}};Sarissa.formToQueryString=function(oForm){var qs="";for(var i=0;i<oForm.elements.length;i++){var oField=oForm.elements[i];var sFieldName=oField.getAttribute("name")?oField.getAttribute("name"):oField.getAttribute("id");if(sFieldName&&((!oField.disabled)||oField.type=="hidden")){switch(oField.type){case"hidden":case"text":case"textarea":case"password":qs+=sFieldName+"="+encodeURIComponent(oField.value)+"&";break;case"select-one":qs+=sFieldName+"="+encodeURIComponent(oField.options[oField.selectedIndex].value)+"&";break;case"select-multiple":for(var j=0;j<oField.length;j++){var optElem=oField.options[j];if(optElem.selected===true){qs+=sFieldName+"[]"+"="+encodeURIComponent(optElem.value)+"&";}}
break;case"checkbox":case"radio":if(oField.checked){qs+=sFieldName+"="+encodeURIComponent(oField.value)+"&";}
break;}}}
return qs.substr(0,qs.length-1);};Sarissa.updateContentFromForm=function(oForm,oTargetElement,xsltproc,callback){try{Sarissa.updateCursor(oTargetElement,"wait");var params=Sarissa.formToQueryString(oForm)+"&"+Sarissa.REMOTE_CALL_FLAG+"=true";var xmlhttp=new XMLHttpRequest();var bUseGet=oForm.getAttribute("method")&&oForm.getAttribute("method").toLowerCase()=="get";if(bUseGet){xmlhttp.open("GET",oForm.getAttribute("action")+"?"+params,true);}
else{xmlhttp.open('POST',oForm.getAttribute("action"),true);xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");xmlhttp.setRequestHeader("Content-length",params.length);xmlhttp.setRequestHeader("Connection","close");}
xmlhttp.onreadystatechange=function(){try{if(xmlhttp.readyState==4){var oDomDoc=xmlhttp.responseXML;if(oDomDoc&&Sarissa.getParseErrorText(oDomDoc)==Sarissa.PARSED_OK){Sarissa.updateContentFromNode(xmlhttp.responseXML,oTargetElement,xsltproc);if(callback){callback(oForm,oTargetElement);}}
else{throw Sarissa.getParseErrorText(oDomDoc);}}}
catch(e){if(callback){callback(oForm,oTargetElement,e);}
else{throw e;}}};xmlhttp.send(bUseGet?"":params);}
catch(e){Sarissa.updateCursor(oTargetElement,"auto");if(callback){callback(oForm,oTargetElement,e);}
else{throw e;}}
return false;};Sarissa.FUNCTION_NAME_REGEXP=new RegExp("");Sarissa.getFunctionName=function(oFunc,bForce){var name;if(!name){if(bForce){name="SarissaAnonymous"+Sarissa._getUniqueSuffix();window[name]=oFunc;}
else{name=null;}}
if(name){window[name]=oFunc;}
return name;};Sarissa.setRemoteJsonCallback=function(url,callback,callbackParam){if(!callbackParam){callbackParam="callback";}
var callbackFunctionName=Sarissa.getFunctionName(callback,true);var id="sarissa_json_script_id_"+Sarissa._getUniqueSuffix();var oHead=document.getElementsByTagName("head")[0];var scriptTag=document.createElement('script');scriptTag.type='text/javascript';scriptTag.id=id;scriptTag.onload=function(){};if(url.indexOf("?")!=-1){url+=("&"+callbackParam+"="+callbackFunctionName);}
else{url+=("?"+callbackParam+"="+callbackFunctionName);}
scriptTag.src=url;oHead.appendChild(scriptTag);return id;};

// jquery.cookie.js

jQuery.cookie=function(name,value,options){if(typeof value!='undefined'){options=options||{};if(value===null){value='';options.expires=-1;}
var expires='';if(options.expires&&(typeof options.expires=='number'||options.expires.toUTCString)){var date;if(typeof options.expires=='number'){date=new Date();date.setTime(date.getTime()+(options.expires*24*60*60*1000));}else{date=options.expires;}
expires='; expires='+date.toUTCString();}
var path=options.path?'; path='+(options.path):'';var domain=options.domain?'; domain='+(options.domain):'';var secure=options.secure?'; secure':'';document.cookie=[name,'=',encodeURIComponent(value),expires,path,domain,secure].join('');}else{var cookieValue=null;if(document.cookie&&document.cookie!=''){var cookies=document.cookie.split(';');for(var i=0;i<cookies.length;i++){var cookie=jQuery.trim(cookies[i]);if(cookie.substring(0,name.length+1)==(name+'=')){cookieValue=decodeURIComponent(cookie.substring(name.length+1));break;}}}
return cookieValue;}};

// jquery.hotkeys.js

(function(jQuery){jQuery.fn.__bind__=jQuery.fn.bind;jQuery.fn.__unbind__=jQuery.fn.unbind;jQuery.fn.__find__=jQuery.fn.find;var hotkeys={version:'0.7.8',override:/keydown|keypress|keyup/g,triggersMap:{},specialKeys:{27:'esc',9:'tab',32:'space',13:'return',8:'backspace',145:'scroll',20:'capslock',144:'numlock',19:'pause',45:'insert',36:'home',46:'del',35:'end',33:'pageup',34:'pagedown',37:'left',38:'up',39:'right',40:'down',112:'f1',113:'f2',114:'f3',115:'f4',116:'f5',117:'f6',118:'f7',119:'f8',120:'f9',121:'f10',122:'f11',123:'f12'},shiftNums:{"`":"~","1":"!","2":"@","3":"#","4":"$","5":"%","6":"^","7":"&","8":"*","9":"(","0":")","-":"_","=":"+",";":":","'":"\"",",":"<",".":">","/":"?","\\":"|"},newTrigger:function(type,combi,callback){var result={};result[type]={};result[type][combi]={cb:callback,disableInInput:false};return result;}};if(jQuery.browser.mozilla){hotkeys.specialKeys=jQuery.extend(hotkeys.specialKeys,{96:'0',97:'1',98:'2',99:'3',100:'4',101:'5',102:'6',103:'7',104:'8',105:'9'});}
jQuery.fn.find=function(selector){this.query=selector;return jQuery.fn.__find__.apply(this,arguments);};jQuery.fn.unbind=function(type,combi,fn){if(jQuery.isFunction(combi)){fn=combi;combi=null;}
if(combi&&typeof combi==='string'){var selectorId=((this.prevObject&&this.prevObject.query)||(this[0].id&&this[0].id)||this[0]).toString();var hkTypes=type.split(' ');for(var x=0;x<hkTypes.length;x++){delete hotkeys.triggersMap[selectorId][hkTypes[x]][combi];}}
return this.__unbind__(type,fn);};jQuery.fn.bind=function(type,data,fn){var handle=type.match(hotkeys.override);if(jQuery.isFunction(data)||!handle){return this.__bind__(type,data,fn);}
else{var result=null,pass2jq=jQuery.trim(type.replace(hotkeys.override,''));if(pass2jq){result=this.__bind__(pass2jq,data,fn);}
if(typeof data==="string"){data={'combi':data};}
if(data.combi){for(var x=0;x<handle.length;x++){var eventType=handle[x];var combi=data.combi.toLowerCase(),trigger=hotkeys.newTrigger(eventType,combi,fn),selectorId=((this.prevObject&&this.prevObject.query)||(this[0].id&&this[0].id)||this[0]).toString();trigger[eventType][combi].disableInInput=data.disableInInput;if(!hotkeys.triggersMap[selectorId]){hotkeys.triggersMap[selectorId]=trigger;}
else if(!hotkeys.triggersMap[selectorId][eventType]){hotkeys.triggersMap[selectorId][eventType]=trigger[eventType];}
var mapPoint=hotkeys.triggersMap[selectorId][eventType][combi];if(!mapPoint){hotkeys.triggersMap[selectorId][eventType][combi]=[trigger[eventType][combi]];}
else if(mapPoint.constructor!==Array){hotkeys.triggersMap[selectorId][eventType][combi]=[mapPoint];}
else{hotkeys.triggersMap[selectorId][eventType][combi][mapPoint.length]=trigger[eventType][combi];}
this.each(function(){var jqElem=jQuery(this);if(jqElem.attr('hkId')&&jqElem.attr('hkId')!==selectorId){selectorId=jqElem.attr('hkId')+";"+selectorId;}
jqElem.attr('hkId',selectorId);});result=this.__bind__(handle.join(' '),data,hotkeys.handler)}}
return result;}};hotkeys.findElement=function(elem){if(!jQuery(elem).attr('hkId')){if(jQuery.browser.opera||jQuery.browser.safari){while(!jQuery(elem).attr('hkId')&&elem.parentNode){elem=elem.parentNode;}}}
return elem;};hotkeys.handler=function(event){var target=hotkeys.findElement(event.currentTarget),jTarget=jQuery(target),ids=jTarget.attr('hkId');if(ids){ids=ids.split(';');var code=event.which,type=event.type,special=hotkeys.specialKeys[code],character=!special&&String.fromCharCode(code).toLowerCase(),shift=event.shiftKey,ctrl=event.ctrlKey,alt=event.altKey||event.originalEvent.altKey,mapPoint=null;for(var x=0;x<ids.length;x++){if(hotkeys.triggersMap[ids[x]][type]){mapPoint=hotkeys.triggersMap[ids[x]][type];break;}}
if(mapPoint){var trigger;if(!shift&&!ctrl&&!alt){trigger=mapPoint[special]||(character&&mapPoint[character]);}
else{var modif='';if(alt)modif+='alt+';if(ctrl)modif+='ctrl+';if(shift)modif+='shift+';trigger=mapPoint[modif+special];if(!trigger){if(character){trigger=mapPoint[modif+character]||mapPoint[modif+hotkeys.shiftNums[character]]||(modif==='shift+'&&mapPoint[hotkeys.shiftNums[character]]);}}}
if(trigger){var result=false;for(var x=0;x<trigger.length;x++){if(trigger[x].disableInInput){var elem=jQuery(event.target);if(jTarget.is("input")||jTarget.is("textarea")||elem.is("input")||elem.is("textarea")){return true;}}
result=result||trigger[x].cb.apply(this,[event]);}
return result;}}}};window.hotkeys=hotkeys;return jQuery;})(jQuery);

// jquery.metadata.js

(function($){$.extend({metadata:{defaults:{type:'class',name:'metadata',cre:/({.*})/,single:'metadata'},setType:function(type,name){this.defaults.type=type;this.defaults.name=name;},get:function(elem,opts){var settings=$.extend({},this.defaults,opts);if(!settings.single.length)settings.single='metadata';var data=$.data(elem,settings.single);if(data)return data;data="{}";if(settings.type=="class"){var m=settings.cre.exec(elem.className);if(m)
data=m[1];}else if(settings.type=="elem"){if(!elem.getElementsByTagName)
return undefined;var e=elem.getElementsByTagName(settings.name);if(e.length)
data=$.trim(e[0].innerHTML);}else if(elem.getAttribute!=undefined){var attr=elem.getAttribute(settings.name);if(attr)
data=attr;}
if(data.indexOf('{')<0)
data="{"+data+"}";data=eval("("+data+")");$.data(elem,settings.single,data);return data;}}});$.fn.metadata=function(opts){return $.metadata.get(this[0],opts);};})(jQuery);

// jquery.tree.js

/*
 * jsTree 0.9.9a
 * http://jstree.com/
 *
 * Copyright (c) 2009 Ivan Bozhanov (vakata.com)
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Date: 2009-10-06
 *
 */

(function($) {
	// jQuery plugin
	$.tree = {
		datastores	: { },
		plugins		: { },
		defaults	: {
			data	: {
				async	: false,		// Are async requests used to load open_branch contents
				type	: "html",		// One of included datastores
				opts	: { method: "GET", url: false } // Options passed to datastore
			},
			selected	: false,		// FALSE or STRING or ARRAY
			opened		: [],			// ARRAY OF INITIALLY OPENED NODES
			languages	: [],			// ARRAY of string values (which will be used as CSS classes - so they must be valid)
			ui		: {
				dots		: true,		// BOOL - dots or no dots
				animation	: 0,		// INT - duration of open/close animations in miliseconds
				scroll_spd	: 4,
				theme_path	: false,	// Path to the theme CSS file - if set to false and theme_name is not false - will lookup jstree-path-here/themes/theme-name-here/style.css
				theme_name	: "default",// if set to false no theme will be loaded
				selected_parent_close	: "select_parent", // false, "deselect", "select_parent"
				selected_delete			: "select_previous" // false, "select_previous"
			},
			types	: {
				"default" : {
					clickable	: true, // can be function
					renameable	: true, // can be function
					deletable	: true, // can be function
					creatable	: true, // can be function
					draggable	: true, // can be function
					max_children	: -1, // -1 - not set, 0 - no children, 1 - one child, etc // can be function
					max_depth		: -1, // -1 - not set, 0 - no children, 1 - one level of children, etc // can be function
					valid_children	: "all", // all, none, array of values // can be function
					icon : {
						image : false,
						position : false
					}
				}
			},
			rules	: {
				multiple	: false,	// FALSE | CTRL | ON - multiple selection off/ with or without holding Ctrl
				multitree	: "none",	// all, none, array of tree IDs to accept from
				type_attr	: "rel",	// STRING attribute name (where is the type stored as string)
				createat	: "bottom",	// STRING (top or bottom) new nodes get inserted at top or bottom
				drag_copy	: "ctrl",	// FALSE | CTRL | ON - drag to copy off/ with or without holding Ctrl
				drag_button	: "left",	// left, right or both
				use_max_children	: true,
				use_max_depth		: true,

				max_children: -1,
				max_depth	: -1,
				valid_children : "all"
			},
			lang : {
				new_node	: "New folder",
				loading		: "Loading ..."
			},
			callback	: {
				beforechange: function(NODE,TREE_OBJ) { return true },
				beforeopen	: function(NODE,TREE_OBJ) { return true },
				beforeclose	: function(NODE,TREE_OBJ) { return true },
				beforemove	: function(NODE,REF_NODE,TYPE,TREE_OBJ) { return true }, 
				beforecreate: function(NODE,REF_NODE,TYPE,TREE_OBJ) { return true }, 
				beforerename: function(NODE,LANG,TREE_OBJ) { return true }, 
				beforedelete: function(NODE,TREE_OBJ) { return true }, 
				beforedata	: function(NODE,TREE_OBJ) { return { id : $(NODE).attr("id") || 0 } }, // PARAMETERS PASSED TO SERVER
				ondata		: function(DATA,TREE_OBJ) { return DATA; },		// modify data before parsing it
				onparse		: function(STR,TREE_OBJ) { return STR; },		// modify string before visualizing it
				onhover		: function(NODE,TREE_OBJ) { },					// node hovered
				onselect	: function(NODE,TREE_OBJ) { },					// node selected
				ondeselect	: function(NODE,TREE_OBJ) { },					// node deselected
				onchange	: function(NODE,TREE_OBJ) { },					// focus changed
				onrename	: function(NODE,TREE_OBJ,RB) { },				// node renamed
				onmove		: function(NODE,REF_NODE,TYPE,TREE_OBJ,RB) { },	// move completed
				oncopy		: function(NODE,REF_NODE,TYPE,TREE_OBJ,RB) { },	// copy completed
				oncreate	: function(NODE,REF_NODE,TYPE,TREE_OBJ,RB) { },	// node created
				ondelete	: function(NODE,TREE_OBJ,RB) { },				// node deleted
				onopen		: function(NODE,TREE_OBJ) { },					// node opened
				onopen_all	: function(TREE_OBJ) { },						// all nodes opened
				onclose_all	: function(TREE_OBJ) { },						// all nodes closed
				onclose		: function(NODE,TREE_OBJ) { },					// node closed
				error		: function(TEXT,TREE_OBJ) { },					// error occured
				ondblclk	: function(NODE,TREE_OBJ) { TREE_OBJ.toggle_branch.call(TREE_OBJ, NODE); TREE_OBJ.select_branch.call(TREE_OBJ, NODE); },
				onrgtclk	: function(NODE,TREE_OBJ,EV) { },				// right click - to prevent use: EV.preventDefault(); EV.stopPropagation(); return false
				onload		: function(TREE_OBJ) { },
				oninit		: function(TREE_OBJ) { },
				onfocus		: function(TREE_OBJ) { },
				ondestroy	: function(TREE_OBJ) { },
				onsearch	: function(NODES, TREE_OBJ) { NODES.addClass("search"); },
				ondrop		: function(NODE,REF_NODE,TYPE,TREE_OBJ) { },
				check		: function(RULE,NODE,VALUE,TREE_OBJ) { return VALUE; },
				check_move	: function(NODE,REF_NODE,TYPE,TREE_OBJ) { return true; }
			},
			plugins : { }
		},

		create		: function () { return new tree_component(); },
		focused		: function () { return tree_component.inst[tree_component.focused]; },
		reference	: function (obj) { 
			var o = $(obj); 
			if(!o.size()) o = $("#" + obj);
			if(!o.size()) return null; 
			o = (o.is(".tree")) ? o.attr("id") : o.parents(".tree:eq(0)").attr("id"); 
			return tree_component.inst[o] || null; 
		},
		rollback	: function (data) {
			for(var i in data) {
				if(!data.hasOwnProperty(i)) continue;
				var tmp = tree_component.inst[i];
				var lock = !tmp.locked;

				// if not locked - lock the tree
				if(lock) tmp.lock(true);
				// Cancel ongoing rename
				tmp.inp = false;
				tmp.container.html(data[i].html).find(".dragged").removeClass("dragged").end().find(".hover").removeClass("hover");

				if(data[i].selected) {
					tmp.selected = $("#" + data[i].selected);
					tmp.selected_arr = [];
					tmp.container
						.find("a.clicked").each( function () {
							tmp.selected_arr.push(tmp.get_node(this));
						});
				}
				// if this function set the lock - unlock
				if(lock) tmp.lock(false);

				delete lock;
				delete tmp;
			}
		},
		drop_mode	: function (opts) {
			opts = $.extend(opts, { show : false, type : "default", str : "Foreign node" });
			tree_component.drag_drop.foreign	= true;
			tree_component.drag_drop.isdown		= true;
			tree_component.drag_drop.moving		= true;
			tree_component.drag_drop.appended	= false;
			tree_component.drag_drop.f_type		= opts.type;
			tree_component.drag_drop.f_data		= opts;


			if(!opts.show) {
				tree_component.drag_drop.drag_help	= false;
				tree_component.drag_drop.drag_node	= false;
			}
			else {
				tree_component.drag_drop.drag_help	= $("<div id='jstree-dragged' class='tree tree-default'><ul><li class='last dragged foreign'><a href='#'><ins>&nbsp;</ins>" + opts.str + "</a></li></ul></div>");
				tree_component.drag_drop.drag_node	= tree_component.drag_drop.drag_help.find("li:eq(0)");
			}
			if($.tree.drag_start !== false) $.tree.drag_start.call(null, false);
		},
		drag_start	: false,
		drag		: false,
		drag_end	: false
	};
	$.fn.tree = function (opts) {
		return this.each(function() {
			var conf = $.extend({},opts);
			if(tree_component.inst && tree_component.inst[$(this).attr('id')]) tree_component.inst[$(this).attr('id')].destroy();
			if(conf !== false) new tree_component().init(this, conf);
		});
	};

	// core
	function tree_component () {
		return {
			cntr : ++tree_component.cntr,
			settings : $.extend({},$.tree.defaults),

			init : function(elem, conf) {
				var _this = this;
				this.container = $(elem);
				if(this.container.size == 0) return false;
				tree_component.inst[this.cntr] = this;
				if(!this.container.attr("id")) this.container.attr("id","jstree_" + this.cntr); 
				tree_component.inst[this.container.attr("id")] = tree_component.inst[this.cntr];
				tree_component.focused = this.cntr;
				this.settings = $.extend(true, {}, this.settings, conf);

				// DEAL WITH LANGUAGE VERSIONS
				if(this.settings.languages && this.settings.languages.length) {
					this.current_lang = this.settings.languages[0];
					var st = false;
					var id = "#" + this.container.attr("id");
					for(var ln = 0; ln < this.settings.languages.length; ln++) {
						st = tree_component.add_css(id + " ." + this.settings.languages[ln]);
						if(st !== false) st.style.display = (this.settings.languages[ln] == this.current_lang) ? "" : "none";
					}
				}
				else this.current_lang = false;
				// THEMES
				this.container.addClass("tree");
				if(this.settings.ui.theme_name !== false) {
					if(this.settings.ui.theme_path === false) {
						$("script").each(function () { 
							if(this.src.toString().match(/jquery\.tree.*?js$/)) { _this.settings.ui.theme_path = this.src.toString().replace(/jquery\.tree.*?js$/, "") + "themes/" + _this.settings.ui.theme_name + "/style.css"; return false; }
						});
					}
					if(this.settings.ui.theme_path != "" && $.inArray(this.settings.ui.theme_path, tree_component.themes) == -1) {
						tree_component.add_sheet({ url : this.settings.ui.theme_path });
						tree_component.themes.push(this.settings.ui.theme_path);
					}
					this.container.addClass("tree-" + this.settings.ui.theme_name);
				}
				// TYPE ICONS
				var type_icons = "";
				for(var t in this.settings.types) {
					if(!this.settings.types.hasOwnProperty(t)) continue;
					if(!this.settings.types[t].icon) continue;
					if( this.settings.types[t].icon.image || this.settings.types[t].icon.position) {
						if(t == "default")  type_icons += "#" + this.container.attr("id") + " li > a ins { ";
						else type_icons += "#" + this.container.attr("id") + " li[rel=" + t + "] > a ins { ";
						if(this.settings.types[t].icon.image) type_icons += " background-image:url(" + this.settings.types[t].icon.image + "); ";
						if(this.settings.types[t].icon.position) type_icons += " background-position:" + this.settings.types[t].icon.position + "; ";
						type_icons += "} ";
					}
				}
				if(type_icons != "") tree_component.add_sheet({ str : type_icons });

				if(this.settings.rules.multiple) this.selected_arr = [];
				this.offset = false;
				this.hovered = false;
				this.locked = false;

				if(tree_component.drag_drop.marker === false) tree_component.drag_drop.marker = $("<div>").attr({ id : "jstree-marker" }).hide().appendTo("body");
				this.callback("oninit", [this]);
				this.refresh();
				this.attach_events();
				this.focus();
			},
			refresh : function (obj) {
				if(this.locked) return this.error("LOCKED");
				var _this = this;
				if(obj && !this.settings.data.async) obj = false;
				this.is_partial_refresh = obj ? true : false;

				// SAVE OPENED
				this.opened = Array();
				if(this.settings.opened != false) {
					$.each(this.settings.opened, function (i, item) {
						if(this.replace(/^#/,"").length > 0) { _this.opened.push("#" + this.replace(/^#/,"")); }
					});
					this.settings.opened = false;
				}
				else {
					this.container.find("li.open").each(function (i) { if(this.id) { _this.opened.push("#" + this.id); } });
				}

				// SAVE SELECTED
				if(this.selected) {
					this.settings.selected = Array();
					if(obj) {
						$(obj).find("li:has(a.clicked)").each(function () {
							if(this.id) _this.settings.selected.push("#" + this.id);
						});
					}
					else {
						if(this.selected_arr) {
							$.each(this.selected_arr, function () {
								if(this.attr("id")) _this.settings.selected.push("#" + this.attr("id"));
							});
						}
						else {
							if(this.selected.attr("id")) this.settings.selected.push("#" + this.selected.attr("id"));
						}
					}
				}
				else if(this.settings.selected !== false) {
					var tmp = Array();
					if((typeof this.settings.selected).toLowerCase() == "object") {
						$.each(this.settings.selected, function () {
							if(this.replace(/^#/,"").length > 0) tmp.push("#" + this.replace(/^#/,""));
						});
					}
					else {
						if(this.settings.selected.replace(/^#/,"").length > 0) tmp.push("#" + this.settings.selected.replace(/^#/,""));
					}
					this.settings.selected = tmp;
				}

				if(obj && this.settings.data.async) {
					this.opened = Array();
					obj = this.get_node(obj);
					obj.find("li.open").each(function (i) { _this.opened.push("#" + this.id); });
					if(obj.hasClass("open")) obj.removeClass("open").addClass("closed");
					if(obj.hasClass("leaf")) obj.removeClass("leaf");
					obj.children("ul:eq(0)").html("");
					return this.open_branch(obj, true, function () { _this.reselect.apply(_this); });
				}

				var _this = this;
				var _datastore = new $.tree.datastores[this.settings.data.type]();
				if(this.container.children("ul").size() == 0) {
					this.container.html("<ul class='ltr' style='direction:ltr;'><li class='last'><a class='loading' href='#'><ins>&nbsp;</ins>" + (this.settings.lang.loading || "Loading ...") + "</a></li></ul>");
				}
				_datastore.load(this.callback("beforedata",[false,this]),this,this.settings.data.opts,function(data) {
					data = _this.callback("ondata",[data, _this]);
					_datastore.parse(data,_this,_this.settings.data.opts,function(str) {
						str = _this.callback("onparse", [str, _this]);
						_this.container.empty().append($("<ul class='ltr'>").html(str));
						_this.container.find("li:last-child").addClass("last").end().find("li:has(ul)").not(".open").addClass("closed");
						_this.container.find("li").not(".open").not(".closed").addClass("leaf");
						_this.reselect();
					});
				});
			},
			reselect : function (is_callback) {
				var _this = this;

				if(!is_callback)	this.cl_count = 0;
				else				this.cl_count --;
				// REOPEN BRANCHES
				if(this.opened && this.opened.length) {
					var opn = false;
					for(var j = 0; this.opened && j < this.opened.length; j++) {
						if(this.settings.data.async) {
							var tmp = this.get_node(this.opened[j]);
							if(tmp.size() && tmp.hasClass("closed") > 0) {
								opn = true;
								var tmp = this.opened[j].toString().replace('/','\\/');
								delete this.opened[j];
								this.open_branch(tmp, true, function () { _this.reselect.apply(_this, [true]); } );
								this.cl_count ++;
							}
						}
						else this.open_branch(this.opened[j], true);
					}
					if(this.settings.data.async && opn) return;
					if(this.cl_count > 0) return;
					delete this.opened;
				} 
				if(this.cl_count > 0) return;

				// DOTS and RIGHT TO LEFT
				this.container.css("direction","ltr").children("ul:eq(0)").addClass("ltr");
				if(this.settings.ui.dots == false)	this.container.children("ul:eq(0)").addClass("no_dots");

				// REPOSITION SCROLL
				if(this.scrtop) {
					this.container.scrollTop(_this.scrtop);
					delete this.scrtop;
				}
				// RESELECT PREVIOUSLY SELECTED
				if(this.settings.selected !== false) {
					$.each(this.settings.selected, function (i) {
						if(_this.is_partial_refresh)	_this.select_branch($(_this.settings.selected[i].toString().replace('/','\\/'), _this.container), (_this.settings.rules.multiple !== false) );
						else							_this.select_branch($(_this.settings.selected[i].toString().replace('/','\\/'), _this.container), (_this.settings.rules.multiple !== false && i > 0) );
					});
					this.settings.selected = false;
				}
				this.callback("onload", [_this]);
			},

			get : function (obj, format, opts) {
				if(!format) format = this.settings.data.type;
				if(!opts) opts = this.settings.data.opts;
				return new $.tree.datastores[format]().get(obj, this, opts);
			},

			attach_events : function () {
				var _this = this;

				this.container
					.bind("mousedown.jstree", function (event) {
						if(tree_component.drag_drop.isdown) {
							tree_component.drag_drop.move_type = false;
							event.preventDefault();
							event.stopPropagation();
							event.stopImmediatePropagation();
							return false;
						}
					})
					.bind("mouseup.jstree", function (event) {
						setTimeout( function() { _this.focus.apply(_this); }, 5);
					})
					.bind("click.jstree", function (event) { 
						//event.stopPropagation(); 
						return true;
					});
				$("li", this.container.get(0))
					.live("click", function(event) { // WHEN CLICK IS ON THE ARROW
						if(event.target.tagName != "LI") return true;
						_this.off_height();
						if(event.pageY - $(event.target).offset().top > _this.li_height) return true;
						_this.toggle_branch.apply(_this, [event.target]);
						event.stopPropagation();
						return false;
					});
				$("a", this.container.get(0))
					.live("click", function (event) { // WHEN CLICK IS ON THE TEXT OR ICON
						if(event.which && event.which == 3) return true;
						if(_this.locked) {
							event.preventDefault(); 
							event.target.blur();
							return _this.error("LOCKED");
						}
						_this.select_branch.apply(_this, [event.target, event.ctrlKey || _this.settings.rules.multiple == "on"]);
						if(_this.inp) { _this.inp.blur(); }
						event.preventDefault(); 
						event.target.blur();
						return false;
					})
					.live("dblclick", function (event) { // WHEN DOUBLECLICK ON TEXT OR ICON
						if(_this.locked) {
							event.preventDefault(); 
							event.stopPropagation();
							event.target.blur();
							return _this.error("LOCKED");
						}
						_this.callback("ondblclk", [_this.get_node(event.target).get(0), _this]);
						event.preventDefault(); 
						event.stopPropagation();
						event.target.blur();
					})
					.live("contextmenu", function (event) {
						if(_this.locked) {
							event.target.blur();
							return _this.error("LOCKED");
						}
						return _this.callback("onrgtclk", [_this.get_node(event.target).get(0), _this, event]);
					})
					.live("mouseover", function (event) {
						if(_this.locked) {
							event.preventDefault();
							event.stopPropagation();
							return _this.error("LOCKED");
						}
						if(_this.hovered !== false && (event.target.tagName == "A" || event.target.tagName == "INS")) {
							_this.hovered.children("a").removeClass("hover");
							_this.hovered = false;
						}
						_this.callback("onhover",[_this.get_node(event.target).get(0), _this]);
					})
					.live("mousedown", function (event) {
						if(_this.settings.rules.drag_button == "left" && event.which && event.which != 1)	return true;
						if(_this.settings.rules.drag_button == "right" && event.which && event.which != 3)	return true;
						_this.focus.apply(_this);
						if(_this.locked) return _this.error("LOCKED");
						// SELECT LIST ITEM NODE
						var obj = _this.get_node(event.target);
						// IF ITEM IS DRAGGABLE
						if(_this.settings.rules.multiple != false && _this.selected_arr.length > 1 && obj.children("a:eq(0)").hasClass("clicked")) {
							var counter = 0;
							for(var i in _this.selected_arr) {
								if(!_this.selected_arr.hasOwnProperty(i)) continue;
								if(_this.check("draggable", _this.selected_arr[i])) {
									_this.selected_arr[i].addClass("dragged");
									tree_component.drag_drop.origin_tree = _this;
									counter ++;
								}
							}
							if(counter > 0) {
								if(_this.check("draggable", obj))	tree_component.drag_drop.drag_node = obj;
								else								tree_component.drag_drop.drag_node = _this.container.find("li.dragged:eq(0)");
								tree_component.drag_drop.isdown		= true;
								tree_component.drag_drop.drag_help	= $("<div id='jstree-dragged' class='tree " + ( _this.settings.ui.theme_name != "" ? " tree-" + _this.settings.ui.theme_name : "" ) + "' />").append("<ul class='" + _this.container.children("ul:eq(0)").get(0).className + "' />");
								var tmp = tree_component.drag_drop.drag_node.clone();
								if(_this.settings.languages.length > 0) tmp.find("a").not("." + _this.current_lang).hide();
								tree_component.drag_drop.drag_help.children("ul:eq(0)").append(tmp);
								tree_component.drag_drop.drag_help.find("li:eq(0)").removeClass("last").addClass("last").children("a").html("<ins>&nbsp;</ins>Multiple selection").end().children("ul").remove();

								tree_component.drag_drop.dragged = _this.container.find("li.dragged");
							}
						}
						else {
							if(_this.check("draggable", obj)) {
								tree_component.drag_drop.drag_node	= obj;
								tree_component.drag_drop.drag_help	= $("<div id='jstree-dragged' class='tree " + ( _this.settings.ui.theme_name != "" ? " tree-" + _this.settings.ui.theme_name : "" ) + "' />").append("<ul class='" + _this.container.children("ul:eq(0)").get(0).className + "' />");
								var tmp = obj.clone();
								if(_this.settings.languages.length > 0) tmp.find("a").not("." + _this.current_lang).hide();
								tree_component.drag_drop.drag_help.children("ul:eq(0)").append(tmp);
								tree_component.drag_drop.drag_help.find("li:eq(0)").removeClass("last").addClass("last");
								tree_component.drag_drop.isdown		= true;
								tree_component.drag_drop.foreign	= false;
								tree_component.drag_drop.origin_tree = _this;
								obj.addClass("dragged");

								tree_component.drag_drop.dragged = _this.container.find("li.dragged");
							}
						}
						tree_component.drag_drop.init_x = event.pageX;
						tree_component.drag_drop.init_y = event.pageY;
						obj.blur();
						event.preventDefault(); 
						event.stopPropagation();
						return false;
					});
			},
			focus : function () {
				if(this.locked) return false;
				if(tree_component.focused != this.cntr) {
					tree_component.focused = this.cntr;
					this.callback("onfocus",[this]);
				}
			},

			off_height : function () {
				if(this.offset === false) {
					this.container.css({ position : "relative" });
					this.offset = this.container.offset();
					var tmp = 0;
					tmp = parseInt($.curCSS(this.container.get(0), "paddingTop", true),10);
					if(tmp) this.offset.top += tmp;
					tmp = parseInt($.curCSS(this.container.get(0), "borderTopWidth", true),10);
					if(tmp) this.offset.top += tmp;
					this.container.css({ position : "" });
				}
				if(!this.li_height) {
					var tmp = this.container.find("ul li.closed, ul li.leaf").eq(0);
					this.li_height = tmp.height();
					if(tmp.children("ul:eq(0)").size()) this.li_height -= tmp.children("ul:eq(0)").height();
					if(!this.li_height) this.li_height = 18;
				}
			},
			scroll_check : function (x,y) { 
				var _this = this;
				var cnt = _this.container;
				var off = _this.container.offset();

				var st = cnt.scrollTop();
				var sl = cnt.scrollLeft();
				// DETECT HORIZONTAL SCROLL
				var h_cor = (cnt.get(0).scrollWidth > cnt.width()) ? 40 : 20;

				if(y - off.top < 20)						cnt.scrollTop(Math.max( (st - _this.settings.ui.scroll_spd) ,0));	// NEAR TOP
				if(cnt.height() - (y - off.top) < h_cor)	cnt.scrollTop(st + _this.settings.ui.scroll_spd);					// NEAR BOTTOM
				if(x - off.left < 20)						cnt.scrollLeft(Math.max( (sl - _this.settings.ui.scroll_spd),0));	// NEAR LEFT
				if(cnt.width() - (x - off.left) < 40)		cnt.scrollLeft(sl + _this.settings.ui.scroll_spd);					// NEAR RIGHT

				if(cnt.scrollLeft() != sl || cnt.scrollTop() != st) {
					tree_component.drag_drop.move_type	= false;
					tree_component.drag_drop.ref_node	= false;
					tree_component.drag_drop.marker.hide();
				}
				tree_component.drag_drop.scroll_time = setTimeout( function() { _this.scroll_check(x,y); }, 50);
			},
			scroll_into_view : function (obj) {
				obj = obj ? this.get_node(obj) : this.selected;
				if(!obj) return false;
				var off_t = obj.offset().top;
				var beg_t = this.container.offset().top;
				var end_t = beg_t + this.container.height();
				var h_cor = (this.container.get(0).scrollWidth > this.container.width()) ? 40 : 20;
				if(off_t + 5 < beg_t) this.container.scrollTop(this.container.scrollTop() - (beg_t - off_t + 5) );
				if(off_t + h_cor > end_t) this.container.scrollTop(this.container.scrollTop() + (off_t + h_cor - end_t) );
			},

			get_node : function (obj) {
				return $(obj).closest("li");
			},
			get_type : function (obj) {
				obj = !obj ? this.selected : this.get_node(obj);
				if(!obj) return;
				var tmp = obj.attr(this.settings.rules.type_attr);
				return tmp || "default";
			},
			set_type : function (str, obj) {
				obj = !obj ? this.selected : this.get_node(obj);
				if(!obj || !str) return;
				obj.attr(this.settings.rules.type_attr, str);
			},
			get_text : function (obj, lang) {
				obj = this.get_node(obj);
				if(!obj || obj.size() == 0) return "";
				if(this.settings.languages && this.settings.languages.length) {
					lang = lang ? lang : this.current_lang;
					obj = obj.children("a." + lang);
				}
				else obj = obj.children("a:visible");
				var val = "";
				obj.contents().each(function () {
					if(this.nodeType == 3) { val = this.data; return false; }
				});
				return val;
			},

			check : function (rule, obj) {
				if(this.locked) return false;
				var v = false;
				// if root node
				if(obj === -1) { if(typeof this.settings.rules[rule] != "undefined") v = this.settings.rules[rule]; }
				else {
					obj = !obj ? this.selected : this.get_node(obj);
					if(!obj) return;
					var t = this.get_type(obj);
					if(typeof this.settings.types[t] != "undefined" && typeof this.settings.types[t][rule] != "undefined") v = this.settings.types[t][rule];
					else if(typeof this.settings.types["default"] != "undefined" && typeof this.settings.types["default"][rule] != "undefined") v = this.settings.types["default"][rule];
				}
				if(typeof v == "function") v = v.call(null, obj, this);
				v = this.callback("check", [rule, obj, v, this]);
				return v;
			},
			check_move : function (nod, ref_node, how) {
				if(this.locked) return false;
				if($(ref_node).closest("li.dragged").size()) return false;

				var tree1 = nod.parents(".tree:eq(0)").get(0);
				var tree2 = ref_node.parents(".tree:eq(0)").get(0);
				// if different trees
				if(tree1 && tree1 != tree2) {
					var m = $.tree.reference(tree2.id).settings.rules.multitree;
					if(m == "none" || ($.isArray(m) && $.inArray(tree1.id, m) == -1)) return false;
				}

				var p = (how != "inside") ? this.parent(ref_node) : this.get_node(ref_node);
				nod = this.get_node(nod);
				if(p == false) return false;
				var r = {
					max_depth : this.settings.rules.use_max_depth ? this.check("max_depth", p) : -1,
					max_children : this.settings.rules.use_max_children ? this.check("max_children", p) : -1,
					valid_children : this.check("valid_children", p)
				};
				var nod_type = (typeof nod == "string") ? nod : this.get_type(nod);
				if(typeof r.valid_children != "undefined" && (r.valid_children == "none" || (typeof r.valid_children == "object" && $.inArray(nod_type, $.makeArray(r.valid_children)) == -1))) return false;
				
				if(this.settings.rules.use_max_children) {
					if(typeof r.max_children != "undefined" && r.max_children != -1) {
						if(r.max_children == 0) return false;
						var c_count = 1;
						if(tree_component.drag_drop.moving == true && tree_component.drag_drop.foreign == false) {
							c_count = tree_component.drag_drop.dragged.size();
							c_count = c_count - p.find('> ul > li.dragged').size();
						}
						if(r.max_children < p.find('> ul > li').size() + c_count) return false;
					}
				}

				if(this.settings.rules.use_max_depth) {
					if(typeof r.max_depth != "undefined" && r.max_depth === 0) return this.error("MOVE: MAX-DEPTH REACHED");
					// check for max_depth up the chain
					var mx = (r.max_depth > 0) ? r.max_depth : false;
					var i = 0;
					var t = p;
					while(t !== -1) {
						t = this.parent(t);
						i ++;
						var m = this.check("max_depth",t);
						if(m >= 0) {
							mx = (mx === false) ? (m - i) : Math.min(mx, m - i);
						}
						if(mx !== false && mx <= 0) return this.error("MOVE: MAX-DEPTH REACHED");
					}
					if(mx !== false && mx <= 0) return this.error("MOVE: MAX-DEPTH REACHED");
					if(mx !== false) { 
						var incr = 1;
						if(typeof nod != "string") {
							var t = nod;
							// possible async problem - when nodes are not all loaded down the chain
							while(t.size() > 0) {
								if(mx - incr < 0) return this.error("MOVE: MAX-DEPTH REACHED");
								t = t.children("ul").children("li");
								incr ++;
							}
						}
					}
				}
				if(this.callback("check_move", [nod, ref_node, how, this]) == false) return false;
				return true;
			},

			hover_branch : function (obj) {
				if(this.locked) return this.error("LOCKED");
				var _this = this;
				var obj = _this.get_node(obj);
				if(!obj.size()) return this.error("HOVER: NOT A VALID NODE");
				if(!_this.check("clickable", obj)) return this.error("SELECT: NODE NOT SELECTABLE");
				if(this.hovered) this.hovered.children("A").removeClass("hover");
				this.hovered = obj;
				this.hovered.children("a").addClass("hover");
				this.scroll_into_view(this.hovered);
			},
			select_branch : function (obj, multiple) {
				if(this.locked) return this.error("LOCKED");
				if(!obj && this.hovered !== false) obj = this.hovered;
				var _this = this;
				obj = _this.get_node(obj);
				if(!obj.size()) return this.error("SELECT: NOT A VALID NODE");
				obj.children("a").removeClass("hover");
				// CHECK AGAINST RULES FOR SELECTABLE NODES
				if(!_this.check("clickable", obj)) return this.error("SELECT: NODE NOT SELECTABLE");
				if(_this.callback("beforechange",[obj.get(0),_this]) === false) return this.error("SELECT: STOPPED BY USER");
				// IF multiple AND obj IS ALREADY SELECTED - DESELECT IT
				if(this.settings.rules.multiple != false && multiple && obj.children("a.clicked").size() > 0) {
					return this.deselect_branch(obj);
				}
				if(this.settings.rules.multiple != false && multiple) {
					this.selected_arr.push(obj);
				}
				if(this.settings.rules.multiple != false && !multiple) {
					for(var i in this.selected_arr) {
						if(!this.selected_arr.hasOwnProperty(i)) continue;
						this.selected_arr[i].children("A").removeClass("clicked");
						this.callback("ondeselect", [this.selected_arr[i].get(0), _this]);
					}
					this.selected_arr = [];
					this.selected_arr.push(obj);
					if(this.selected && this.selected.children("A").hasClass("clicked")) {
						this.selected.children("A").removeClass("clicked");
						this.callback("ondeselect", [this.selected.get(0), _this]);
					}
				}
				if(!this.settings.rules.multiple) {
					if(this.selected) {
						this.selected.children("A").removeClass("clicked");
						this.callback("ondeselect", [this.selected.get(0), _this]);
					}
				}
				// SAVE NEWLY SELECTED
				this.selected = obj;
				if(this.hovered !== false) {
					this.hovered.children("A").removeClass("hover");
					this.hovered = obj;
				}

				// FOCUS NEW NODE AND OPEN ALL PARENT NODES IF CLOSED
				this.selected.children("a").addClass("clicked").end().parents("li.closed").each( function () { _this.open_branch(this, true); });

				// SCROLL SELECTED NODE INTO VIEW
				this.scroll_into_view(this.selected);

				this.callback("onselect", [this.selected.get(0), _this]);
				this.callback("onchange", [this.selected.get(0), _this]);
			},
			deselect_branch : function (obj) {
				if(this.locked) return this.error("LOCKED");
				var _this = this;
				var obj = this.get_node(obj);
				if(obj.children("a.clicked").size() == 0) return this.error("DESELECT: NODE NOT SELECTED");

				obj.children("a").removeClass("clicked");
				this.callback("ondeselect", [obj.get(0), _this]);
				if(this.settings.rules.multiple != false && this.selected_arr.length > 1) {
					this.selected_arr = [];
					this.container.find("a.clicked").filter(":first-child").parent().each(function () {
						_this.selected_arr.push($(this));
					});
					if(obj.get(0) == this.selected.get(0)) {
						this.selected = this.selected_arr[0];
					}
				}
				else {
					if(this.settings.rules.multiple != false) this.selected_arr = [];
					this.selected = false;
				}
				this.callback("onchange", [obj.get(0), _this]);
			},
			toggle_branch : function (obj) {
				if(this.locked) return this.error("LOCKED");
				var obj = this.get_node(obj);
				if(obj.hasClass("closed"))	return this.open_branch(obj);
				if(obj.hasClass("open"))	return this.close_branch(obj); 
			},
			open_branch : function (obj, disable_animation, callback) {
				var _this = this;

				if(this.locked) return this.error("LOCKED");
				var obj = this.get_node(obj);
				if(!obj.size()) return this.error("OPEN: NO SUCH NODE");
				if(obj.hasClass("leaf")) return this.error("OPEN: OPENING LEAF NODE");
				if(this.settings.data.async && obj.find("li").size() == 0) {
					
					if(this.callback("beforeopen",[obj.get(0),this]) === false) return this.error("OPEN: STOPPED BY USER");

					obj.children("ul:eq(0)").remove().end().append("<ul><li class='last'><a class='loading' href='#'><ins>&nbsp;</ins>" + (_this.settings.lang.loading || "Loading ...") + "</a></li></ul>");
					obj.removeClass("closed").addClass("open");

					var _datastore = new $.tree.datastores[this.settings.data.type]();
					_datastore.load(this.callback("beforedata",[obj,this]),this,this.settings.data.opts,function(data){
						data = _this.callback("ondata", [data, _this]);
						if(!data || data.length == 0) {
							obj.removeClass("closed").removeClass("open").addClass("leaf").children("ul").remove();
							if(callback) callback.call();
							return;
						}
						_datastore.parse(data,_this,_this.settings.data.opts,function(str){
							str = _this.callback("onparse", [str, _this]);
							// if(obj.children('ul:eq(0)').children('li').size() > 1) obj.children("ul").find('.loaading').parent().replaceWith(str); else 
							obj.children("ul:eq(0)").replaceWith($("<ul>").html(str));
							obj.find("li:last-child").addClass("last").end().find("li:has(ul)").not(".open").addClass("closed");
							obj.find("li").not(".open").not(".closed").addClass("leaf");
							_this.open_branch.apply(_this, [obj]);
							if(callback) callback.call();
						});
					});
					return true;
				}
				else {
					if(!this.settings.data.async) {
						if(this.callback("beforeopen",[obj.get(0),this]) === false) return this.error("OPEN: STOPPED BY USER");
					}
					if(parseInt(this.settings.ui.animation) > 0 && !disable_animation ) {
						obj.children("ul:eq(0)").css("display","none");
						obj.removeClass("closed").addClass("open");
						obj.children("ul:eq(0)").slideDown(parseInt(this.settings.ui.animation), function() {
							$(this).css("display","");
							if(callback) callback.call();
						});
					} else {
						obj.removeClass("closed").addClass("open");
						if(callback) callback.call();
					}
					this.callback("onopen", [obj.get(0), this]);
					return true;
				}
			},
			close_branch : function (obj, disable_animation) {
				if(this.locked) return this.error("LOCKED");
				var _this = this;
				var obj = this.get_node(obj);
				if(!obj.size()) return this.error("CLOSE: NO SUCH NODE");
				if(_this.callback("beforeclose",[obj.get(0),_this]) === false) return this.error("CLOSE: STOPPED BY USER");
				if(parseInt(this.settings.ui.animation) > 0 && !disable_animation && obj.children("ul:eq(0)").size() == 1) {
					obj.children("ul:eq(0)").slideUp(parseInt(this.settings.ui.animation), function() {
						if(obj.hasClass("open")) obj.removeClass("open").addClass("closed");
						$(this).css("display","");
					});
				} 
				else {
					if(obj.hasClass("open")) obj.removeClass("open").addClass("closed");
				}
				if(this.selected && this.settings.ui.selected_parent_close !== false && obj.children("ul:eq(0)").find("a.clicked").size() > 0) {
					obj.find("li:has(a.clicked)").each(function() {
						_this.deselect_branch(this);
					});
					if(this.settings.ui.selected_parent_close == "select_parent" && obj.children("a.clicked").size() == 0) this.select_branch(obj, (this.settings.rules.multiple != false && this.selected_arr.length > 0) );
				}
				this.callback("onclose", [obj.get(0), this]);
			},
			open_all : function (obj, callback) {
				if(this.locked) return this.error("LOCKED");
				var _this = this;
				obj = obj ? this.get_node(obj) : this.container;

				var s = obj.find("li.closed").size();
				if(!callback)	this.cl_count = 0;
				else			this.cl_count --;
				if(s > 0) {
					this.cl_count += s;
					// maybe add .andSelf()
					obj.find("li.closed").each( function () { var __this = this; _this.open_branch.apply(_this, [this, true, function() { _this.open_all.apply(_this, [__this, true]); } ]); });
				}
				else if(this.cl_count == 0) this.callback("onopen_all",[this]);
			},
			close_all : function (obj) {
				if(this.locked) return this.error("LOCKED");
				var _this = this;
				obj = obj ? this.get_node(obj) : this.container;
				// maybe add .andSelf()
				obj.find("li.open").each( function () { _this.close_branch(this, true); });
				this.callback("onclose_all",[this]);
			},

			set_lang : function (i) { 
				if(!$.isArray(this.settings.languages) || this.settings.languages.length == 0) return false;
				if(this.locked) return this.error("LOCKED");
				if(!$.inArray(i,this.settings.languages) && typeof this.settings.languages[i] != "undefined") i = this.settings.languages[i];
				if(typeof i == "undefined") return false;
				if(i == this.current_lang) return true;
				var st = false;
				var id = "#" + this.container.attr("id");
				st = tree_component.get_css(id + " ." + this.current_lang);
				if(st !== false) st.style.display = "none";
				st = tree_component.get_css(id + " ." + i);
				if(st !== false) st.style.display = "";
				this.current_lang = i;
				return true;
			},
			get_lang : function () {
				if(!$.isArray(this.settings.languages) || this.settings.languages.length == 0) return false;
				return this.current_lang;
			},

			create : function (obj, ref_node, position) { 
				if(this.locked) return this.error("LOCKED");
				
				var root = false;
				if(ref_node == -1) { root = true; ref_node = this.container; }
				else ref_node = ref_node ? this.get_node(ref_node) : this.selected;

				if(!root && (!ref_node || !ref_node.size())) return this.error("CREATE: NO NODE SELECTED");

				var pos = position;

				var tmp = ref_node; // for type calculation
				if(position == "before") {
					position = ref_node.parent().children().index(ref_node);
					ref_node = ref_node.parents("li:eq(0)");
				}
				if(position == "after") {
					position = ref_node.parent().children().index(ref_node) + 1;
					ref_node = ref_node.parents("li:eq(0)");
				}
				if(!root && ref_node.size() == 0) { root = true; ref_node = this.container; }

				if(!root) {
					if(!this.check("creatable", ref_node)) return this.error("CREATE: CANNOT CREATE IN NODE");
					if(ref_node.hasClass("closed")) {
						if(this.settings.data.async && ref_node.children("ul").size() == 0) {
							var _this = this;
							return this.open_branch(ref_node, true, function () { _this.create.apply(_this, [obj, ref_node, position]); } );
						}
						else this.open_branch(ref_node, true);
					}
				}

				// creating new object to pass to parseJSON
				var torename = false; 
				if(!obj)	obj = {};
				else		obj = $.extend(true, {}, obj);
				if(!obj.attributes) obj.attributes = {};
				if(!obj.attributes[this.settings.rules.type_attr]) obj.attributes[this.settings.rules.type_attr] = this.get_type(tmp) || "default";
				if(this.settings.languages.length) {
					if(!obj.data) { obj.data = {}; torename = true; }
					for(var i = 0; i < this.settings.languages.length; i++) {
						if(!obj.data[this.settings.languages[i]]) obj.data[this.settings.languages[i]] = ((typeof this.settings.lang.new_node).toLowerCase() != "string" && this.settings.lang.new_node[i]) ? this.settings.lang.new_node[i] : this.settings.lang.new_node;
					}
				}
				else {
					if(!obj.data) { obj.data = this.settings.lang.new_node; torename = true; }
				}

				obj = this.callback("ondata",[obj, this]);
				var obj_s = $.tree.datastores.json().parse(obj,this);
				obj_s = this.callback("onparse", [obj_s, this]);
				var $li = $(obj_s);

				if($li.children("ul").size()) {
					if(!$li.is(".open")) $li.addClass("closed");
				}
				else $li.addClass("leaf");
				$li.find("li:last-child").addClass("last").end().find("li:has(ul)").not(".open").addClass("closed");
				$li.find("li").not(".open").not(".closed").addClass("leaf");

				var r = {
					max_depth : this.settings.rules.use_max_depth ? this.check("max_depth", (root ? -1 : ref_node) ) : -1,
					max_children : this.settings.rules.use_max_children ? this.check("max_children", (root ? -1 : ref_node) ) : -1,
					valid_children : this.check("valid_children", (root ? -1 : ref_node) )
				};
				var nod_type = this.get_type($li);
				if(typeof r.valid_children != "undefined" && (r.valid_children == "none" || ($.isArray(r.valid_children) && $.inArray(nod_type, r.valid_children) == -1))) return this.error("CREATE: NODE NOT A VALID CHILD");

				if(this.settings.rules.use_max_children) {
					if(typeof r.max_children != "undefined" && r.max_children != -1 && r.max_children >= this.children(ref_node).size()) return this.error("CREATE: MAX_CHILDREN REACHED");
				}

				if(this.settings.rules.use_max_depth) {
					if(typeof r.max_depth != "undefined" && r.max_depth === 0) return this.error("CREATE: MAX-DEPTH REACHED");
					// check for max_depth up the chain
					var mx = (r.max_depth > 0) ? r.max_depth : false;
					var i = 0;
					var t = ref_node;

					while(t !== -1 && !root) {
						t = this.parent(t);
						i ++;
						var m = this.check("max_depth",t);
						if(m >= 0) {
							mx = (mx === false) ? (m - i) : Math.min(mx, m - i);
						}
						if(mx !== false && mx <= 0) return this.error("CREATE: MAX-DEPTH REACHED");
					}
					if(mx !== false && mx <= 0) return this.error("CREATE: MAX-DEPTH REACHED");
					if(mx !== false) { 
						var incr = 1;
						var t = $li;
						while(t.size() > 0) {
							if(mx - incr < 0) return this.error("CREATE: MAX-DEPTH REACHED");
							t = t.children("ul").children("li");
							incr ++;
						}
					}
				}

				if((typeof position).toLowerCase() == "undefined" || position == "inside") 
					position = (this.settings.rules.createat == "top") ? 0 : ref_node.children("ul:eq(0)").children("li").size();
				if(ref_node.children("ul").size() == 0 || (root == true && ref_node.children("ul").children("li").size() == 0) ) {
					if(!root)	var a = this.moved($li,ref_node.children("a:eq(0)"),"inside", true);
					else		var a = this.moved($li,this.container.children("ul:eq(0)"),"inside", true);
				}
				else if(pos == "before" && ref_node.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").size())
					var a = this.moved($li,ref_node.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").children("a:eq(0)"),"before", true);
				else if(pos == "after" &&  ref_node.children("ul:eq(0)").children("li:nth-child(" + (position) + ")").size())
					var a = this.moved($li,ref_node.children("ul:eq(0)").children("li:nth-child(" + (position) + ")").children("a:eq(0)"),"after", true);
				else if(ref_node.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").size())
					var a = this.moved($li,ref_node.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").children("a:eq(0)"),"before", true);
				else
					var a = this.moved($li,ref_node.children("ul:eq(0)").children("li:last").children("a:eq(0)"),"after",true);

				if(a === false) return this.error("CREATE: ABORTED");

				if(torename) {
					this.select_branch($li.children("a:eq(0)"));
					this.rename();
				}
				return $li;
			},
			rename : function (obj, new_name) {
				if(this.locked) return this.error("LOCKED");
				obj = obj ? this.get_node(obj) : this.selected;
				var _this = this;
				if(!obj || !obj.size()) return this.error("RENAME: NO NODE SELECTED");
				if(!this.check("renameable", obj)) return this.error("RENAME: NODE NOT RENAMABLE");
				if(!this.callback("beforerename",[obj.get(0), _this.current_lang, _this])) return this.error("RENAME: STOPPED BY USER");

				obj.parents("li.closed").each(function () { _this.open_branch(this) });
				if(this.current_lang)	obj = obj.find("a." + this.current_lang);
				else					obj = obj.find("a:first");

				// Rollback
				var rb = {}; 
				rb[this.container.attr("id")] = this.get_rollback();

				var icn = obj.children("ins").clone();
				if((typeof new_name).toLowerCase() == "string") {
					obj.text(new_name).prepend(icn);
					_this.callback("onrename", [_this.get_node(obj).get(0), _this, rb]);
				}
				else {
					var last_value = "";
					obj.contents().each(function () {
						if(this.nodeType == 3) { last_value = this.data; return false; }
					});
					_this.inp = $("<input type='text' autocomplete='off' />");
					_this.inp
						.val(last_value.replace(/&amp;/g,"&").replace(/&gt;/g,">").replace(/&lt;/g,"<"))
						.bind("mousedown",		function (event) { event.stopPropagation(); })
						.bind("mouseup",		function (event) { event.stopPropagation(); })
						.bind("click",			function (event) { event.stopPropagation(); })
						.bind("keyup",			function (event) { 
								var key = event.keyCode || event.which;
								if(key == 27) { this.value = last_value; this.blur(); return }
								if(key == 13) { this.blur(); return; }
							});
					_this.inp.blur(function(event) {
							if(this.value == "") this.value = last_value; 
							obj.text(this.value).prepend(icn);
							obj.get(0).style.display = ""; 
							obj.prevAll("span").remove(); 
							_this.inp = false;
							_this.callback("onrename", [_this.get_node(obj).get(0), _this, rb]);
						});

					var spn = $("<span />").addClass(obj.attr("class")).append(icn).append(_this.inp);
					obj.get(0).style.display = "none";
					obj.parent().prepend(spn);
					_this.inp.get(0).focus();
					_this.inp.get(0).select();
				}
			},
			remove : function(obj) {
				if(this.locked) return this.error("LOCKED");
				var _this = this;

				// Rollback
				var rb = {}; 
				rb[this.container.attr("id")] = this.get_rollback();

				if(obj && (!this.selected || this.get_node(obj).get(0) != this.selected.get(0) )) {
					obj = this.get_node(obj);
					if(obj.size()) {
						if(!this.check("deletable", obj)) return this.error("DELETE: NODE NOT DELETABLE");
						if(!this.callback("beforedelete",[obj.get(0), _this])) return this.error("DELETE: STOPPED BY USER");
						$parent = obj.parent();
						if(obj.find("a.clicked").size()) {
							var reset_selected = false;
							_this.selected_arr = [];
							this.container.find("a.clicked").filter(":first-child").parent().each(function () {
								if(!reset_selected && this == _this.selected.get(0)) reset_selected = true;
								if($(this).parents().index(obj) != -1) return true;
								_this.selected_arr.push($(this));
							});
							if(reset_selected) this.selected = this.selected_arr[0] || false;
						}
						obj = obj.remove();
						$parent.children("li:last").addClass("last");
						if($parent.children("li").size() == 0) {
							$li = $parent.parents("li:eq(0)");
							$li.removeClass("open").removeClass("closed").addClass("leaf").children("ul").remove();
						}
						this.callback("ondelete", [obj.get(0), this, rb]);
					}
				}
				else if(this.selected) {
					if(!this.check("deletable", this.selected)) return this.error("DELETE: NODE NOT DELETABLE");
					if(!this.callback("beforedelete",[this.selected.get(0), _this])) return this.error("DELETE: STOPPED BY USER");
					$parent = this.selected.parent();
					var obj = this.selected;
					if(this.settings.rules.multiple == false || this.selected_arr.length == 1) {
						var stop = true;
						var tmp = this.settings.ui.selected_delete == "select_previous" ? this.prev(this.selected) : false;
					}
					obj = obj.remove();
					$parent.children("li:last").addClass("last");
					if($parent.children("li").size() == 0) {
						$li = $parent.parents("li:eq(0)");
						$li.removeClass("open").removeClass("closed").addClass("leaf").children("ul").remove();
					}
					if(!stop && this.settings.rules.multiple != false) {
						var _this = this;
						this.selected_arr = [];
						this.container.find("a.clicked").filter(":first-child").parent().each(function () {
							_this.selected_arr.push($(this));
						});
						if(this.selected_arr.length > 0) {
							this.selected = this.selected_arr[0];
							this.remove();
						}
					}
					if(stop && tmp) this.select_branch(tmp); 
					this.callback("ondelete", [obj.get(0), this, rb]);
				}
				else return this.error("DELETE: NO NODE SELECTED");
			},

			next : function (obj, strict) {
				obj = this.get_node(obj);
				if(!obj.size()) return false;
				if(strict) return (obj.nextAll("li").size() > 0) ? obj.nextAll("li:eq(0)") : false;

				if(obj.hasClass("open")) return obj.find("li:eq(0)");
				else if(obj.nextAll("li").size() > 0) return obj.nextAll("li:eq(0)");
				else return obj.parents("li").next("li").eq(0);
			},
			prev : function(obj, strict) {
				obj = this.get_node(obj);
				if(!obj.size()) return false;
				if(strict) return (obj.prevAll("li").size() > 0) ? obj.prevAll("li:eq(0)") : false;

				if(obj.prev("li").size()) {
					var obj = obj.prev("li").eq(0);
					while(obj.hasClass("open")) obj = obj.children("ul:eq(0)").children("li:last");
					return obj;
				}
				else return obj.parents("li:eq(0)").size() ? obj.parents("li:eq(0)") : false;
			},
			parent : function(obj) {
				obj = this.get_node(obj);
				if(!obj.size()) return false;
				return obj.parents("li:eq(0)").size() ? obj.parents("li:eq(0)") : -1;
			},
			children : function(obj) {
				if(obj === -1) return this.container.children("ul:eq(0)").children("li");

				obj = this.get_node(obj);
				if(!obj.size()) return false;
				return obj.children("ul:eq(0)").children("li");
			},

			toggle_dots : function () {
				if(this.settings.ui.dots) {
					this.settings.ui.dots = false;
					this.container.children("ul:eq(0)").addClass("no_dots");
				}
				else {
					this.settings.ui.dots = true;
					this.container.children("ul:eq(0)").removeClass("no_dots");
				}
			},

			callback : function (cb, args) {
				var p = false;
				var r = null;
				for(var i in this.settings.plugins) {
					if(typeof $.tree.plugins[i] != "object") continue;
					p = $.tree.plugins[i];
					if(p.callbacks && typeof p.callbacks[cb] == "function") r = p.callbacks[cb].apply(this, args);
					if(typeof r !== "undefined" && r !== null) {
						if(cb == "ondata" || cb == "onparse") args[0] = r; // keep the chain if data or parse
						else return r;
					}
				}
				p = this.settings.callback[cb];
				if(typeof p == "function") return p.apply(null, args);
			},
			get_rollback : function () {
				var rb = {};
				rb.html = this.container.html();
				rb.selected = this.selected ? this.selected.attr("id") : false;
				return rb;
			},
			moved : function (what, where, how, is_new, is_copy, rb) {
				var what	= $(what);
				var $parent	= $(what).parents("ul:eq(0)");
				var $where	= $(where);
				if($where.is("ins")) $where = $where.parent();

				// Rollback
				if(!rb) {
					var rb = {}; 
					rb[this.container.attr("id")] = this.get_rollback();
					if(!is_new) {
						var tmp = what.size() > 1 ? what.eq(0).parents(".tree:eq(0)") : what.parents(".tree:eq(0)");
						if(tmp.get(0) != this.container.get(0)) {
							tmp = tree_component.inst[tmp.attr("id")];
							rb[tmp.container.attr("id")] = tmp.get_rollback();
						}
						delete tmp;
					}
				}

				if(how == "inside" && this.settings.data.async) {
					var _this = this;
					if(this.get_node($where).hasClass("closed")) {
						return this.open_branch(this.get_node($where), true, function () { _this.moved.apply(_this, [what, where, how, is_new, is_copy, rb]); });
					}
					if(this.get_node($where).find("> ul > li > a.loading").size() == 1) {
						setTimeout(function () { _this.moved.apply(_this, [what, where, how, is_new, is_copy]); }, 200);
						return;
					}
				}


				// IF MULTIPLE
				if(what.size() > 1) {
					var _this = this;
					var tmp = this.moved(what.eq(0), where, how, false, is_copy, rb);
					what.each(function (i) {
						if(i == 0) return;
						if(tmp) { // if tmp is false - the previous move was a no-go
							tmp = _this.moved(this, tmp.children("a:eq(0)"), "after", false, is_copy, rb);
						}
					});
					return what;
				}

				if(is_copy) {
					_what = what.clone();
					_what.each(function (i) {
						this.id = this.id + "_copy";
						$(this).find("li").each(function () {
							this.id = this.id + "_copy";
						});
						$(this).removeClass("dragged").find("a.clicked").removeClass("clicked").end().find("li.dragged").removeClass("dragged");
					});
				}
				else _what = what;
				if(is_new) {
					if(!this.callback("beforecreate", [this.get_node(what).get(0), this.get_node(where).get(0),how,this])) return false;
				}
				else {
					if(!this.callback("beforemove", [this.get_node(what).get(0), this.get_node(where).get(0),how,this])) return false;
				}

				if(!is_new) {
					var tmp = what.parents(".tree:eq(0)");
					// if different trees
					if(tmp.get(0) != this.container.get(0)) {
						tmp = tree_component.inst[tmp.attr("id")];

						// if there are languages - otherwise - no cleanup needed
						if(tmp.settings.languages.length) {
							var res = [];
							// if new tree has no languages - use current visible
							if(this.settings.languages.length == 0) res.push("." + tmp.current_lang);
							else {
								for(var i in this.settings.languages) {
									if(!this.settings.languages.hasOwnProperty(i)) continue;
									for(var j in tmp.settings.languages) {
										if(!tmp.settings.languages.hasOwnProperty(j)) continue;
										if(this.settings.languages[i] == tmp.settings.languages[j]) res.push("." + this.settings.languages[i]);
									}
								}
							}
							if(res.length == 0) return this.error("MOVE: NO COMMON LANGUAGES");
							_what.find("a").not(res.join(",")).remove();
						}
						_what.find("a.clicked").removeClass("clicked");
					}
				}
				what = _what;

				// ADD NODE TO NEW PLACE
				switch(how) {
					case "before":
						$where.parents("ul:eq(0)").children("li.last").removeClass("last");
						$where.parent().before(what.removeClass("last"));
						$where.parents("ul:eq(0)").children("li:last").addClass("last");
						break;
					case "after":
						$where.parents("ul:eq(0)").children("li.last").removeClass("last");
						$where.parent().after(what.removeClass("last"));
						$where.parents("ul:eq(0)").children("li:last").addClass("last");
						break;
					case "inside":
						if($where.parent().children("ul:first").size()) {
							if(this.settings.rules.createat == "top") {
								$where.parent().children("ul:first").prepend(what.removeClass("last")).children("li:last").addClass("last");

								// restored this section
								var tmp_node = $where.parent().children("ul:first").children("li:first");
								if(tmp_node.size()) {
									how = "before";
									where = tmp_node;
								}
							}
							else {
								// restored this section
								var tmp_node = $where.parent().children("ul:first").children(".last");
								if(tmp_node.size()) {
									how = "after";
									where = tmp_node;
								}

								$where.parent().children("ul:first").children(".last").removeClass("last").end().append(what.removeClass("last")).children("li:last").addClass("last");
							}
						}
						else {
							what.addClass("last");
							$where.parent().removeClass("leaf").append("<ul/>");
							if(!$where.parent().hasClass("open")) $where.parent().addClass("closed");
							$where.parent().children("ul:first").prepend(what);
						}
						if($where.parent().hasClass("closed")) { this.open_branch($where); }
						break;
					default:
						break;
				}
				// CLEANUP OLD PARENT
				if($parent.find("li").size() == 0) {
					var $li = $parent.parent();
					$li.removeClass("open").removeClass("closed").addClass("leaf");
					if(!$li.is(".tree")) $li.children("ul").remove();
					$li.parents("ul:eq(0)").children("li.last").removeClass("last").end().children("li:last").addClass("last");
				}
				else {
					$parent.children("li.last").removeClass("last");
					$parent.children("li:last").addClass("last");
				}

				// NO LONGER CORRECT WITH position PARAM - if(is_new && how != "inside") where = this.get_node(where).parents("li:eq(0)");
				if(is_copy)		this.callback("oncopy", [this.get_node(what).get(0), this.get_node(where).get(0), how, this, rb]);
				else if(is_new)	this.callback("oncreate", [this.get_node(what).get(0), ($where.is("ul") ? -1 : this.get_node(where).get(0) ), how, this, rb]);
				else			this.callback("onmove", [this.get_node(what).get(0), this.get_node(where).get(0), how, this, rb]);
				return what;
			},
			error : function (code) {
				this.callback("error",[code,this]);
				return false;
			},
			lock : function (state) {
				this.locked = state;
				if(this.locked)	this.container.children("ul:eq(0)").addClass("locked");
				else			this.container.children("ul:eq(0)").removeClass("locked");
			},
			cut : function (obj) {
				if(this.locked) return this.error("LOCKED");
				obj = obj ? this.get_node(obj) : this.container.find("a.clicked").filter(":first-child").parent();
				if(!obj || !obj.size()) return this.error("CUT: NO NODE SELECTED");
				tree_component.cut_copy.copy_nodes = false;
				tree_component.cut_copy.cut_nodes = obj;
			},
			copy : function (obj) {
				if(this.locked) return this.error("LOCKED");
				obj = obj ? this.get_node(obj) : this.container.find("a.clicked").filter(":first-child").parent();
				if(!obj || !obj.size()) return this.error("COPY: NO NODE SELECTED");
				tree_component.cut_copy.copy_nodes = obj;
				tree_component.cut_copy.cut_nodes = false;
			},
			paste : function (obj, position) {
				if(this.locked) return this.error("LOCKED");

				var root = false;
				if(obj == -1) { root = true; obj = this.container; }
				else obj = obj ? this.get_node(obj) : this.selected;

				if(!root && (!obj || !obj.size())) return this.error("PASTE: NO NODE SELECTED");
				if(!tree_component.cut_copy.copy_nodes && !tree_component.cut_copy.cut_nodes) return this.error("PASTE: NOTHING TO DO");

				var _this = this;

				var pos = position;

				if(position == "before") {
					position = obj.parent().children().index(obj);
					obj = obj.parents("li:eq(0)");
				}
				else if(position == "after") {
					position = obj.parent().children().index(obj) + 1;
					obj = obj.parents("li:eq(0)");
				}
				else if((typeof position).toLowerCase() == "undefined" || position == "inside") {
					position = (this.settings.rules.createat == "top") ? 0 : obj.children("ul:eq(0)").children("li").size();
				}
				if(!root && obj.size() == 0) { root = true; obj = this.container; }

				if(tree_component.cut_copy.copy_nodes && tree_component.cut_copy.copy_nodes.size()) {
					var ok = true;
					if(!root && !this.check_move(tree_component.cut_copy.copy_nodes, obj.children("a:eq(0)"), "inside")) return false;

					if(obj.children("ul").size() == 0 || (root == true && obj.children("ul").children("li").size() == 0) ) {
						if(!root)	var a = this.moved(tree_component.cut_copy.copy_nodes,obj.children("a:eq(0)"),"inside", false, true);
						else		var a = this.moved(tree_component.cut_copy.copy_nodes,this.container.children("ul:eq(0)"),"inside", false, true);
					}
					else if(pos == "before" && obj.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").size())
						var a = this.moved(tree_component.cut_copy.copy_nodes,obj.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").children("a:eq(0)"),"before", false, true);
					else if(pos == "after" && obj.children("ul:eq(0)").children("li:nth-child(" + (position) + ")").size())
						var a = this.moved(tree_component.cut_copy.copy_nodes,obj.children("ul:eq(0)").children("li:nth-child(" + (position) + ")").children("a:eq(0)"),"after", false, true);
					else if(obj.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").size())
						var a = this.moved(tree_component.cut_copy.copy_nodes,obj.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").children("a:eq(0)"),"before", false, true);
					else
						var a = this.moved(tree_component.cut_copy.copy_nodes,obj.children("ul:eq(0)").children("li:last").children("a:eq(0)"),"after", false, true);
					tree_component.cut_copy.copy_nodes = false;
				}
				if(tree_component.cut_copy.cut_nodes && tree_component.cut_copy.cut_nodes.size()) {
					var ok = true;
					obj.parents().andSelf().each(function () {
						if(tree_component.cut_copy.cut_nodes.index(this) != -1) {
							ok = false;
							return false;
						}
					});
					if(!ok) return this.error("Invalid paste");
					if(!root && !this.check_move(tree_component.cut_copy.cut_nodes, obj.children("a:eq(0)"), "inside")) return false;

					if(obj.children("ul").size() == 0 || (root == true && obj.children("ul").children("li").size() == 0) ) {
						if(!root)	var a = this.moved(tree_component.cut_copy.cut_nodes,obj.children("a:eq(0)"),"inside");
						else		var a = this.moved(tree_component.cut_copy.cut_nodes,this.container.children("ul:eq(0)"),"inside");
					}
					else if(pos == "before" && obj.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").size())
						var a = this.moved(tree_component.cut_copy.cut_nodes,obj.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").children("a:eq(0)"),"before");
					else if(pos == "after" && obj.children("ul:eq(0)").children("li:nth-child(" + (position) + ")").size())
						var a = this.moved(tree_component.cut_copy.cut_nodes,obj.children("ul:eq(0)").children("li:nth-child(" + (position) + ")").children("a:eq(0)"),"after");
					else if(obj.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").size())
						var a = this.moved(tree_component.cut_copy.cut_nodes,obj.children("ul:eq(0)").children("li:nth-child(" + (position + 1) + ")").children("a:eq(0)"),"before");
					else
						var a = this.moved(tree_component.cut_copy.cut_nodes,obj.children("ul:eq(0)").children("li:last").children("a:eq(0)"),"after");
					tree_component.cut_copy.cut_nodes = false;
				}
			},
			search : function(str, func) {
				var _this = this;
				if(!str || (this.srch && str != this.srch) ) {
					this.srch = "";
					this.srch_opn = false;
					this.container.find("a.search").removeClass("search");
				}
				this.srch = str;
				if(!str) return;

				if(!func) func = "contains";
				if(this.settings.data.async) {
					if(!this.srch_opn) {
						var dd = $.extend( { "search" : str } , this.callback("beforedata", [false, this] ) );
						$.ajax({
							type		: this.settings.data.opts.method,
							url			: this.settings.data.opts.url, 
							data		: dd, 
							dataType	: "text",
							success		: function (data) {
								_this.srch_opn = $.unique(data.split(","));
								_this.search.apply(_this,[str, func]);
							} 
						});
					}
					else if(this.srch_opn.length) {
						if(this.srch_opn && this.srch_opn.length) {
							var opn = false;
							for(var j = 0; j < this.srch_opn.length; j++) {
								if(this.get_node("#" + this.srch_opn[j]).size() > 0) {
									opn = true;
									var tmp = "#" + this.srch_opn[j];
									delete this.srch_opn[j];
									this.open_branch(tmp, true, function () { _this.search.apply(_this,[str, func]); } );
								}
							}
							if(!opn) {
								this.srch_opn = [];
								 _this.search.apply(_this,[str, func]);
							}
						}
					}
					else {
						this.srch_opn = false;
						var selector = "a";
						// IF LANGUAGE VERSIONS
						if(this.settings.languages.length) selector += "." + this.current_lang;
						this.callback("onsearch", [this.container.find(selector + ":" + func + "('" + str + "')"), this]);
					}
				}
				else {
					var selector = "a";
					// IF LANGUAGE VERSIONS
					if(this.settings.languages.length) selector += "." + this.current_lang;
					var nn = this.container.find(selector + ":" + func + "('" + str + "')");
					nn.parents("li.closed").each( function () { _this.open_branch(this, true); });
					this.callback("onsearch", [nn, this]);
				}
			},
			add_sheet : tree_component.add_sheet,

			destroy : function() {
				this.callback("ondestroy", [this]);

				this.container.unbind(".jstree");
				$("#" + this.container.attr("id")).die("click.jstree").die("dblclick.jstree").die("mouseover.jstree").die("mouseout.jstree").die("mousedown.jstree");
				this.container.removeClass("tree ui-widget ui-widget-content tree-default tree-" + this.settings.ui.theme_name).children("ul").removeClass("no_dots ltr locked").find("li").removeClass("leaf").removeClass("open").removeClass("closed").removeClass("last").children("a").removeClass("clicked hover search");

				if(this.cntr == tree_component.focused) {
					for(var i in tree_component.inst) {
						if(i != this.cntr && i != this.container.attr("id")) {
							tree_component.inst[i].focus();
							break;
						}
					}
				}

				tree_component.inst[this.cntr] = false;
				tree_component.inst[this.container.attr("id")] = false;
				delete tree_component.inst[this.cntr];
				delete tree_component.inst[this.container.attr("id")];
				tree_component.cntr --;
			}
		}
	};

	// instance manager
	tree_component.cntr = 0;
	tree_component.inst = {};

	// themes
	tree_component.themes = [];

	// drag'n'drop stuff
	tree_component.drag_drop = {
		isdown		: false,	// Is there a drag
		drag_node	: false,	// The actual node
		drag_help	: false,	// The helper
		dragged		: false,

		init_x		: false,
		init_y		: false,
		moving		: false,

		origin_tree	: false,
		marker		: false,

		move_type	: false,	// before, after or inside
		ref_node	: false,	// reference node
		appended	: false,	// is helper appended

		foreign		: false,	// Is the dragged node a foreign one
		droppable	: [],		// Array of classes that can be dropped onto the tree

		open_time	: false,	// Timeout for opening nodes
		scroll_time	: false		// Timeout for scrolling
	};
	tree_component.mouseup = function(event) {
		var tmp = tree_component.drag_drop;
		if(tmp.open_time)	clearTimeout(tmp.open_time);
		if(tmp.scroll_time)	clearTimeout(tmp.scroll_time);

		if(tmp.moving && $.tree.drag_end !== false) $.tree.drag_end.call(null, event, tmp);

		if(tmp.foreign === false && tmp.drag_node && tmp.drag_node.size()) {
			tmp.drag_help.remove();
			if(tmp.move_type) {
				var tree1 = tree_component.inst[tmp.ref_node.parents(".tree:eq(0)").attr("id")];
				if(tree1) tree1.moved(tmp.dragged, tmp.ref_node, tmp.move_type, false, (tmp.origin_tree.settings.rules.drag_copy == "on" || (tmp.origin_tree.settings.rules.drag_copy == "ctrl" && event.ctrlKey) ) );
			}
			tmp.move_type	= false;
			tmp.ref_node	= false;
		}
		if(tmp.foreign !== false) {
			if(tmp.drag_help) tmp.drag_help.remove();
			if(tmp.move_type) {
				var tree1 = tree_component.inst[tmp.ref_node.parents(".tree:eq(0)").attr("id")];
				if(tree1) tree1.callback("ondrop",[tmp.f_data, tree1.get_node(tmp.ref_node).get(0), tmp.move_type, tree1]);
			}
			tmp.foreign		= false;
			tmp.move_type	= false;
			tmp.ref_node	= false;
		}
		// RESET EVERYTHING
		if(tree_component.drag_drop.marker) tree_component.drag_drop.marker.hide();
		if(tmp.dragged && tmp.dragged.size()) tmp.dragged.removeClass("dragged");
		tmp.dragged		= false;
		tmp.drag_help	= false;
		tmp.drag_node	= false;
		tmp.f_type		= false;
		tmp.f_data		= false;
		tmp.init_x		= false;
		tmp.init_y		= false;
		tmp.moving		= false;
		tmp.appended	= false;
		tmp.origin_tree	= false;
		if(tmp.isdown) {
			tmp.isdown = false;
			event.preventDefault(); 
			event.stopPropagation();
			return false;
		}
	};
	tree_component.mousemove = function(event) {
		var tmp = tree_component.drag_drop;
		var is_start = false;

		if(tmp.isdown) {
			if(!tmp.moving && Math.abs(tmp.init_x - event.pageX) < 5 && Math.abs(tmp.init_y - event.pageY) < 5) {
				event.preventDefault();
				event.stopPropagation();
				return false;
			}
			else {
				if(!tmp.moving) {
					tree_component.drag_drop.moving = true;
					is_start = true;
				}
			}

			if(tmp.open_time) clearTimeout(tmp.open_time);

			if(tmp.drag_help !== false) {
				if(!tmp.appended) {
					if(tmp.foreign !== false) tmp.origin_tree = $.tree.focused();
					$("body").append(tmp.drag_help);
					tmp.w = tmp.drag_help.width();
					tmp.appended = true;
				}
				tmp.drag_help.css({ "left" : (event.pageX + 5 ), "top" : (event.pageY + 15) });
			}

			if(is_start && $.tree.drag_start !== false) $.tree.drag_start.call(null, event, tmp);
			if($.tree.drag !== false) $.tree.drag.call(null, event, tmp);

			if(event.target.tagName == "DIV" && event.target.id == "jstree-marker") return false;

			var et = $(event.target);
			if(et.is("ins")) et = et.parent();
			var cnt = et.is(".tree") ? et : et.parents(".tree:eq(0)");

			// if not moving over a tree
			if(cnt.size() == 0 || !tree_component.inst[cnt.attr("id")]) {
				if(tmp.scroll_time) clearTimeout(tmp.scroll_time);
				if(tmp.drag_help !== false) tmp.drag_help.find("li:eq(0) ins").addClass("forbidden");
				tmp.move_type	= false;
				tmp.ref_node	= false;
				tree_component.drag_drop.marker.hide();
				return false;
			}

			var tree2 = tree_component.inst[cnt.attr("id")];
			tree2.off_height();

			if(tmp.scroll_time) clearTimeout(tmp.scroll_time);
			tmp.scroll_time = setTimeout( function() { tree2.scroll_check(event.pageX,event.pageY); }, 50);

			var mov = false;
			var st = cnt.scrollTop();

			if(event.target.tagName == "A" || event.target.tagName == "INS") {
				// just in case if hover is over the draggable
				if(et.is("#jstree-dragged")) return false;
				if(tree2.get_node(event.target).hasClass("closed")) {
					tmp.open_time = setTimeout( function () { tree2.open_branch(et); }, 500);
				}

				var et_off = et.offset();
				var goTo = { 
					x : (et_off.left - 1),
					y : (event.pageY - et_off.top)
				};

				var arr = [];
				if(goTo.y < tree2.li_height/3 + 1 )			arr = ["before","inside","after"];
				else if(goTo.y > tree2.li_height*2/3 - 1 )	arr = ["after","inside","before"];
				else {
					if(goTo.y < tree2.li_height/2)			arr = ["inside","before","after"];
					else									arr = ["inside","after","before"];
				}
				var ok = false;
				var nn = (tmp.foreign == false) ? tmp.origin_tree.container.find("li.dragged") : tmp.f_type;
				$.each(arr, function(i, val) {
					if(tree2.check_move(nn, et, val)) {
						mov = val;
						ok = true;
						return false;
					}
				});
				if(ok) {
					switch(mov) {
						case "before":
							goTo.y = et_off.top - 2;
							tree_component.drag_drop.marker.attr("class","marker");
							break;
						case "after":
							goTo.y = et_off.top - 2 + tree2.li_height;
							tree_component.drag_drop.marker.attr("class","marker");
							break;
						case "inside":
							goTo.x -= 2;
							goTo.y = et_off.top - 2 + tree2.li_height/2;
							tree_component.drag_drop.marker.attr("class","marker_plus"); 
							break;
					}
					tmp.move_type	= mov;
					tmp.ref_node	= $(event.target);
					if(tmp.drag_help !== false) tmp.drag_help.find(".forbidden").removeClass("forbidden");
					tree_component.drag_drop.marker.css({ "left" : goTo.x , "top" : goTo.y }).show();
				}
			}

			if( (et.is(".tree") || et.is("ul") ) && et.find("li:eq(0)").size() == 0) {
				var et_off = et.offset();
				tmp.move_type	= "inside";
				tmp.ref_node	= cnt.children("ul:eq(0)");
				if(tmp.drag_help !== false) tmp.drag_help.find(".forbidden").removeClass("forbidden");
				tree_component.drag_drop.marker.attr("class","marker_plus");
				tree_component.drag_drop.marker.css({ "left" : (et_off.left + 10) , "top" : et_off.top + 15 }).show();
			}
			else if( (event.target.tagName != "A" && event.target.tagName != "INS") || !ok) {
				if(tmp.drag_help !== false) tmp.drag_help.find("li:eq(0) ins").addClass("forbidden");
				tmp.move_type	= false;
				tmp.ref_node	= false;
				tree_component.drag_drop.marker.hide();
			}
			event.preventDefault();
			event.stopPropagation();
			return false;
		}
		return true;
	};
	$(function () { 
		$(document).bind("mousemove.jstree",	tree_component.mousemove); 
		$(document).bind("mouseup.jstree",		tree_component.mouseup); 
	});

	// cut, copy, paste stuff
	tree_component.cut_copy = { 
		copy_nodes : false,
		cut_nodes : false
	};

	// css stuff
	tree_component.css = false;
	tree_component.get_css = function(rule_name, delete_flag) {
		rule_name = rule_name.toLowerCase();
		var css_rules = tree_component.css.cssRules || tree_component.css.rules;
		var j = 0;
		do {
			if(css_rules.length && j > css_rules.length + 5) return false;
			if(css_rules[j].selectorText && css_rules[j].selectorText.toLowerCase() == rule_name) {
				if(delete_flag == true) {
					if(tree_component.css.removeRule) document.styleSheets[i].removeRule(j);
					if(tree_component.css.deleteRule) document.styleSheets[i].deleteRule(j);
					return true;
				}
				else return css_rules[j];
			}
		}
		while (css_rules[++j]);
		return false;
	};
	tree_component.add_css = function(rule_name) {
		if(tree_component.get_css(rule_name)) return false;
		(tree_component.css.insertRule) ? tree_component.css.insertRule(rule_name + ' { }', 0) : tree_component.css.addRule(rule_name, null, 0);
		return tree_component.get_css(rule_name);
	};
	tree_component.remove_css = function(rule_name) { 
		return tree_component.get_css(rule_name, true); 
	};
	tree_component.add_sheet = function(opts) {
		if(opts.str) {
			var tmp = document.createElement("style");
			tmp.setAttribute('type',"text/css");
			if(tmp.styleSheet) {
				document.getElementsByTagName("head")[0].appendChild(tmp);
				tmp.styleSheet.cssText = opts.str;
			}
			else {
				tmp.appendChild(document.createTextNode(opts.str));
				document.getElementsByTagName("head")[0].appendChild(tmp);
			}
			return tmp.sheet || tmp.styleSheet;
		}
		if(opts.url) {
			if(document.createStyleSheet) {
				try { document.createStyleSheet(opts.url); } catch (e) { };
			}
			else {
				var newSS	= document.createElement('link');
				newSS.rel	= 'stylesheet';
				newSS.type	= 'text/css';
				newSS.media	= "all";
				newSS.href	= opts.url;
				// var styles	= "@import url(' " + url + " ');";
				// newSS.href	='data:text/css,'+escape(styles);
				document.getElementsByTagName("head")[0].appendChild(newSS);
				return newSS.styleSheet;
			}
		}
	};
	$(function () {
		var u = navigator.userAgent.toLowerCase();
		var v = (u.match( /.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/ ) || [0,'0'])[1];

		var css = '/* TREE LAYOUT */ .tree ul { margin:0 0 0 5px; padding:0 0 0 0; list-style-type:none; } .tree li { display:block; min-height:18px; line-height:18px; padding:0 0 0 15px; margin:0 0 0 0; /* Background fix */ clear:both; } .tree li ul { display:none; } .tree li a, .tree li span { display:inline-block;line-height:16px;height:16px;color:black;white-space:nowrap;text-decoration:none;padding:1px 4px 1px 4px;margin:0; } .tree li a:focus { outline: none; } .tree li a input, .tree li span input { margin:0;padding:0 0;display:inline-block;height:12px !important;border:1px solid white;background:white;font-size:10px;font-family:Verdana; } .tree li a input:not([class="xxx"]), .tree li span input:not([class="xxx"]) { padding:1px 0; } /* FOR DOTS */ .tree .ltr li.last { float:left; } .tree > ul li.last { overflow:visible; } /* OPEN OR CLOSE */ .tree li.open ul { display:block; } .tree li.closed ul { display:none !important; } /* FOR DRAGGING */ #jstree-dragged { position:absolute; top:-10px; left:-10px; margin:0; padding:0; } #jstree-dragged ul ul ul { display:none; } #jstree-marker { padding:0; margin:0; line-height:5px; font-size:1px; overflow:hidden; height:5px; position:absolute; left:-45px; top:-30px; z-index:1000; background-color:transparent; background-repeat:no-repeat; display:none; } #jstree-marker.marker { width:45px; background-position:-32px top; } #jstree-marker.marker_plus { width:5px; background-position:right top; } /* BACKGROUND DOTS */ .tree li li { overflow:hidden; } .tree > .ltr > li { display:table; } /* ICONS */ .tree ul ins { display:inline-block; text-decoration:none; width:16px; height:16px; } .tree .ltr ins { margin:0 4px 0 0px; } ';
		if($.browser.msie) { 
			if($.browser.version == 6) css += '.tree li { height:18px; zoom:1; } .tree li li { overflow:visible; } .tree .ltr li.last { margin-top: expression( (this.previousSibling && /open/.test(this.previousSibling.className) ) ? "-2px" : "0"); } .marker { width:45px; background-position:-32px top; } .marker_plus { width:5px; background-position:right top; }';
			if($.browser.version == 7) css += '.tree li li { overflow:visible; } .tree .ltr li.last { margin-top: expression( (this.previousSibling && /open/.test(this.previousSibling.className) ) ? "-2px" : "0"); }';
		}
		if($.browser.opera) css += '.tree > ul > li.last:after { content:"."; display: block; height:1px; clear:both; visibility:hidden; }';
		if($.browser.mozilla && $.browser.version.indexOf("1.8") == 0) css += '.tree .ltr li a { display:inline; float:left; } .tree li ul { clear:both; }';
		tree_component.css = tree_component.add_sheet({ str : css });
	});
})(jQuery);

// Datastores
// HTML and JSON are included here by default
(function ($) {
	$.extend($.tree.datastores, {
		"html" : function () {
			return {
				get		: function(obj, tree, opts) {
					return obj && $(obj).size() ? $('<div>').append(tree.get_node(obj).clone()).html() : tree.container.children("ul:eq(0)").html();
				},
				parse	: function(data, tree, opts, callback) {
					if(callback) callback.call(null, data);
					return data;
				},
				load	: function(data, tree, opts, callback) {
					if(opts.url) {
						$.ajax({
							'type'		: opts.method,
							'url'		: opts.url, 
							'data'		: data, 
							'dataType'	: "html",
							'success'	: function (d, textStatus) {
								callback.call(null, d);
							},
							'error'		: function (xhttp, textStatus, errorThrown) { 
								callback.call(null, false);
								tree.error(errorThrown + " " + textStatus); 
							}
						});
					}
					else {
						callback.call(null, opts.static || tree.container.children("ul:eq(0)").html());
					}
				}
			};
		},
		"json" : function () {
			return {
				get		: function(obj, tree, opts) { 
					var _this = this;
					if(!obj || $(obj).size() == 0) obj = tree.container.children("ul").children("li");
					else obj = $(obj);

					if(!opts) opts = {};
					if(!opts.outer_attrib) opts.outer_attrib = [ "id", "rel", "class" ];
					if(!opts.inner_attrib) opts.inner_attrib = [ ];

					if(obj.size() > 1) {
						var arr = [];
						obj.each(function () {
							arr.push(_this.get(this, tree, opts));
						});
						return arr;
					}
					if(obj.size() == 0) return [];

					var json = { attributes : {}, data : {} };
					if(obj.hasClass("open")) json.data.state = "open";
					if(obj.hasClass("closed")) json.data.state = "closed";

					for(var i in opts.outer_attrib) {
						if(!opts.outer_attrib.hasOwnProperty(i)) continue;
						var val = (opts.outer_attrib[i] == "class") ? obj.attr(opts.outer_attrib[i]).replace(/(^| )last( |$)/ig," ").replace(/(^| )(leaf|closed|open)( |$)/ig," ") : obj.attr(opts.outer_attrib[i]);
						if(typeof val != "undefined" && val.toString().replace(" ","").length > 0) json.attributes[opts.outer_attrib[i]] = val;
						delete val;
					}
					
					if(tree.settings.languages.length) {
						for(var i in tree.settings.languages) {
							if(!tree.settings.languages.hasOwnProperty(i)) continue;
							var a = obj.children("a." + tree.settings.languages[i]);
							if(opts.force || opts.inner_attrib.length || a.children("ins").get(0).style.backgroundImage.toString().length || a.children("ins").get(0).className.length) {
								json.data[tree.settings.languages[i]] = {};
								json.data[tree.settings.languages[i]].title = tree.get_text(obj,tree.settings.languages[i]);
								if(a.children("ins").get(0).style.className.length) {
									json.data[tree.settings.languages[i]].icon = a.children("ins").get(0).style.className;
								}
								if(a.children("ins").get(0).style.backgroundImage.length) {
									json.data[tree.settings.languages[i]].icon = a.children("ins").get(0).style.backgroundImage.replace("url(","").replace(")","");
								}
								if(opts.inner_attrib.length) {
									json.data[tree.settings.languages[i]].attributes = {};
									for(var j in opts.inner_attrib) {
										if(!opts.inner_attrib.hasOwnProperty(j)) continue;
										var val = a.attr(opts.inner_attrib[j]);
										if(typeof val != "undefined" && val.toString().replace(" ","").length > 0) json.data[tree.settings.languages[i]].attributes[opts.inner_attrib[j]] = val;
										delete val;
									}
								}
							}
							else {
								json.data[tree.settings.languages[i]] = tree.get_text(obj,tree.settings.languages[i]);
							}
						}
					}
					else {
						var a = obj.children("a");
						json.data.title = tree.get_text(obj);

						if(a.children("ins").size() && a.children("ins").get(0).className.length) {
							json.data.icon = a.children("ins").get(0).className;
						}
						if(a.children("ins").size() && a.children("ins").get(0).style.backgroundImage.length) {
							json.data.icon = a.children("ins").get(0).style.backgroundImage.replace("url(","").replace(")","");
						}

						if(opts.inner_attrib.length) {
							json.data.attributes = {};
							for(var j in opts.inner_attrib) {
								if(!opts.inner_attrib.hasOwnProperty(j)) continue;
								var val = a.attr(opts.inner_attrib[j]);
								if(typeof val != "undefined" && val.toString().replace(" ","").length > 0) json.data.attributes[opts.inner_attrib[j]] = val;
								delete val;
							}
						}
					}

					if(obj.children("ul").size() > 0) {
						json.children = [];
						obj.children("ul").children("li").each(function () {
							json.children.push(_this.get(this, tree, opts));
						});
					}
					return json;
				},
				parse	: function(data, tree, opts, callback) { 
					if(Object.prototype.toString.apply(data) === "[object Array]") {
						var str = '';
						for(var i = 0; i < data.length; i ++) {
							if(typeof data[i] == "function") continue;
							str += this.parse(data[i], tree, opts);
						}
						if(callback) callback.call(null, str);
						return str;
					}

					if(!data || !data.data) {
						if(callback) callback.call(null, false);
						return "";
					}

					var str = '';
					str += "<li ";
					var cls = false;
					if(data.attributes) {
						for(var i in data.attributes) {
							if(!data.attributes.hasOwnProperty(i)) continue;
							if(i == "class") {
								str += " class='" + data.attributes[i] + " ";
								if(data.state == "closed" || data.state == "open") str += " " + data.state + " ";
								str += "' ";
								cls = true;
							}
							else str += " " + i + "='" + data.attributes[i] + "' ";
						}
					}
					if(!cls && (data.state == "closed" || data.state == "open")) str += " class='" + data.state + "' ";
					str += ">";

					if(tree.settings.languages.length) {
						for(var i = 0; i < tree.settings.languages.length; i++) {
							var attr = {};
							attr["href"] = "";
							attr["style"] = "";
							attr["class"] = tree.settings.languages[i];
							if(data.data[tree.settings.languages[i]] && (typeof data.data[tree.settings.languages[i]].attributes).toLowerCase() != "undefined") {
								for(var j in data.data[tree.settings.languages[i]].attributes) {
									if(!data.data[tree.settings.languages[i]].attributes.hasOwnProperty(j)) continue;
									if(j == "style" || j == "class")	attr[j] += " " + data.data[tree.settings.languages[i]].attributes[j];
									else								attr[j]  = data.data[tree.settings.languages[i]].attributes[j];
								}
							}
							str += "<a";
							for(var j in attr) {
								if(!attr.hasOwnProperty(j)) continue;
								str += ' ' + j + '="' + attr[j] + '" ';
							}
							str += ">";
							if(data.data[tree.settings.languages[i]] && data.data[tree.settings.languages[i]].icon) {
								str += "<ins " + (data.data[tree.settings.languages[i]].icon.indexOf("/") == -1 ? " class='" + data.data[tree.settings.languages[i]].icon + "' " : " style='background-image:url(\"" + data.data[tree.settings.languages[i]].icon + "\");' " ) + ">&nbsp;</ins>";
							}
							else str += "<ins>&nbsp;</ins>";
							str += ( (typeof data.data[tree.settings.languages[i]].title).toLowerCase() != "undefined" ? data.data[tree.settings.languages[i]].title : data.data[tree.settings.languages[i]] ) + "</a>";
						}
					}
					else {
						var attr = {};
						attr["href"] = "";
						attr["style"] = "";
						attr["class"] = "";
						if((typeof data.data.attributes).toLowerCase() != "undefined") {
							for(var i in data.data.attributes) {
								if(!data.data.attributes.hasOwnProperty(i)) continue;
								if(i == "style" || i == "class")	attr[i] += " " + data.data.attributes[i];
								else								attr[i]  = data.data.attributes[i];
							}
						}
						str += "<a";
						for(var i in attr) {
							if(!attr.hasOwnProperty(i)) continue;
							str += ' ' + i + '="' + attr[i] + '" ';
						}
						str += ">";
						if(data.data.icon) {
							str += "<ins " + (data.data.icon.indexOf("/") == -1 ? " class='" + data.data.icon + "' " : " style='background-image:url(\"" + data.data.icon + "\");' " ) + ">&nbsp;</ins>";
						}
						else str += "<ins>&nbsp;</ins>";
						str += ( (typeof data.data.title).toLowerCase() != "undefined" ? data.data.title : data.data ) + "</a>";
					}
					if(data.children && data.children.length) {
						str += '<ul>';
						for(var i = 0; i < data.children.length; i++) {
							str += this.parse(data.children[i], tree, opts);
						}
						str += '</ul>';
					}
					str += "</li>";
					if(callback) callback.call(null, str);
					return str;
				},
				load	: function(data, tree, opts, callback) {
					if(opts.static) {
						callback.call(null, opts.static);
					} 
					else {
						$.ajax({
							'type'		: opts.method,
							'url'		: opts.url, 
							'data'		: data, 
							'dataType'	: "json",
							'success'	: function (d, textStatus) {
								callback.call(null, d);
							},
							'error'		: function (xhttp, textStatus, errorThrown) { 
								callback.call(null, false);
								tree.error(errorThrown + " " + textStatus); 
							}
						});
					}
				}
			}
		}
	});
})(jQuery);

// jquery.tree.checkbox.js

(function($){$.extend($.tree.plugins,{"checkbox":{defaults:{three_state:true},get_checked:function(t){if(!t)t=$.tree.focused();return t.container.find("a.checked").parent();},get_undeterminded:function(t){if(!t)t=$.tree.focused();return t.container.find("a.undetermined").parent();},get_unchecked:function(t){if(!t)t=$.tree.focused();return t.container.find("a:not(.checked, .undetermined)").parent();},check:function(n){if(!n)return false;var t=$.tree.reference(n);n=t.get_node(n);if(n.children("a").hasClass("checked"))return true;var opts=$.extend(true,{},$.tree.plugins.checkbox.defaults,t.settings.plugins.checkbox);if(opts.three_state){n.find("li").andSelf().children("a").removeClass("unchecked undetermined").addClass("checked");n.parents("li").each(function(){if($(this).children("ul").find("a:not(.checked):eq(0)").size()>0){$(this).parents("li").andSelf().children("a").removeClass("unchecked checked").addClass("undetermined");return false;}
else $(this).children("a").removeClass("unchecked undetermined").addClass("checked");});}
else n.children("a").removeClass("unchecked").addClass("checked");return true;},uncheck:function(n){if(!n)return false;var t=$.tree.reference(n);n=t.get_node(n);if(n.children("a").hasClass("unchecked"))return true;var opts=$.extend(true,{},$.tree.plugins.checkbox.defaults,t.settings.plugins.checkbox);if(opts.three_state){n.find("li").andSelf().children("a").removeClass("checked undetermined").addClass("unchecked");n.parents("li").each(function(){if($(this).find("a.checked, a.undetermined").size()-1>0){$(this).parents("li").andSelf().children("a").removeClass("unchecked checked").addClass("undetermined");return false;}
else $(this).children("a").removeClass("checked undetermined").addClass("unchecked");});}
else n.children("a").removeClass("checked").addClass("unchecked");return true;},toggle:function(n){if(!n)return false;var t=$.tree.reference(n);n=t.get_node(n);if(n.children("a").hasClass("checked"))$.tree.plugins.checkbox.uncheck(n);else $.tree.plugins.checkbox.check(n);},callbacks:{onchange:function(n,t){$.tree.plugins.checkbox.toggle(n);}}}});})(jQuery);

// jquery.tree.contextmenu.js

(function($){$.extend($.tree.plugins,{"contextmenu":{object:$("<ul id='jstree-contextmenu' class='tree-context' />"),data:{t:false,a:false,r:false},defaults:{class_name:"hover",items:{create:{label:"Create",icon:"create",visible:function(NODE,TREE_OBJ){if(NODE.length!=1)return 0;return TREE_OBJ.check("creatable",NODE);},action:function(NODE,TREE_OBJ){TREE_OBJ.create(false,TREE_OBJ.get_node(NODE[0]));},separator_after:true},rename:{label:"Rename",icon:"rename",visible:function(NODE,TREE_OBJ){if(NODE.length!=1)return false;return TREE_OBJ.check("renameable",NODE);},action:function(NODE,TREE_OBJ){TREE_OBJ.rename(NODE);}},remove:{label:"Delete",icon:"remove",visible:function(NODE,TREE_OBJ){var ok=true;$.each(NODE,function(){if(TREE_OBJ.check("deletable",this)==false)ok=false;return false;});return ok;},action:function(NODE,TREE_OBJ){$.each(NODE,function(){TREE_OBJ.remove(this);});}}}},show:function(obj,t){var opts=$.extend(true,{},$.tree.plugins.contextmenu.defaults,t.settings.plugins.contextmenu);obj=$(obj);$.tree.plugins.contextmenu.object.empty();var str="";var cnt=0;for(var i in opts.items){if(!opts.items.hasOwnProperty(i))continue;if(opts.items[i]===false)continue;var r=1;if(typeof opts.items[i].visible=="function")r=opts.items[i].visible.call(null,$.tree.plugins.contextmenu.data.a,t);if(r==-1)continue;else cnt++;if(opts.items[i].separator_before===true)str+="<li class='separator'><span>&nbsp;</span></li>";str+='<li><a href="#" rel="'+i+'" class="'+i+' '+(r==0?'disabled':'')+'">';if(opts.items[i].icon)str+="<ins "+(opts.items[i].icon.indexOf("/")==-1?" class='"+opts.items[i].icon+"' ":" style='background-image:url(\""+opts.items[i].icon+"\");' ")+">&nbsp;</ins>";else str+="<ins>&nbsp;</ins>";str+="<span>"+opts.items[i].label+'</span></a></li>';if(opts.items[i].separator_after===true)str+="<li class='separator'><span>&nbsp;</span></li>";}
var tmp=obj.children("a:visible").offset();$.tree.plugins.contextmenu.object.attr("class","tree-context tree-"+t.settings.ui.theme_name.toString()+"-context").html(str);var h=$.tree.plugins.contextmenu.object.height();var w=$.tree.plugins.contextmenu.object.width();var x=tmp.left;var y=tmp.top+parseInt(obj.children("a:visible").height())+2;var max_y=$(window).height()+$(window).scrollTop();var max_x=$(window).width()+$(window).scrollLeft();if(y+h>max_y)y=Math.max((max_y-h-2),0);if(x+w>max_x)x=Math.max((max_x-w-2),0);$.tree.plugins.contextmenu.object.css({"left":(x),"top":(y)}).fadeIn("fast");},hide:function(){if(!$.tree.plugins.contextmenu.data.t)return;var opts=$.extend(true,{},$.tree.plugins.contextmenu.defaults,$.tree.plugins.contextmenu.data.t.settings.plugins.contextmenu);if($.tree.plugins.contextmenu.data.r&&$.tree.plugins.contextmenu.data.a){$.tree.plugins.contextmenu.data.a.children("a, span").removeClass(opts.class_name);}
$.tree.plugins.contextmenu.data={a:false,r:false,t:false};$.tree.plugins.contextmenu.object.fadeOut("fast");},exec:function(cmd){if($.tree.plugins.contextmenu.data.t==false)return;var opts=$.extend(true,{},$.tree.plugins.contextmenu.defaults,$.tree.plugins.contextmenu.data.t.settings.plugins.contextmenu);try{opts.items[cmd].action.apply(null,[$.tree.plugins.contextmenu.data.a,$.tree.plugins.contextmenu.data.t]);}catch(e){};},callbacks:{oninit:function(){if(!$.tree.plugins.contextmenu.css){var css='#jstree-contextmenu { display:none; position:absolute; z-index:2000; list-style-type:none; margin:0; padding:0; left:-2000px; top:-2000px; } .tree-context { margin:20px; padding:0; width:180px; border:1px solid #979797; padding:2px; background:#f5f5f5; list-style-type:none; }.tree-context li { height:22px; margin:0 0 0 27px; padding:0; background:#ffffff; border-left:1px solid #e0e0e0; }.tree-context li a { position:relative; display:block; height:22px; line-height:22px; margin:0 0 0 -28px; text-decoration:none; color:black; padding:0; }.tree-context li a ins { text-decoration:none; float:left; width:16px; height:16px; margin:0 0 0 0; background-color:#f0f0f0; border:1px solid #f0f0f0; border-width:3px 5px 3px 6px; line-height:16px; }.tree-context li a span { display:block; background:#f0f0f0; margin:0 0 0 29px; padding-left:5px; }.tree-context li.separator { background:#f0f0f0; height:2px; line-height:2px; font-size:1px; border:0; margin:0; padding:0; }.tree-context li.separator span { display:block; margin:0px 0 0px 27px; height:1px; border-top:1px solid #e0e0e0; border-left:1px solid #e0e0e0; line-height:1px; font-size:1px; background:white; }.tree-context li a:hover { border:1px solid #d8f0fa; height:20px; line-height:20px; }.tree-context li a:hover span { background:#e7f4f9; margin-left:28px; }.tree-context li a:hover ins { background-color:#e7f4f9; border-color:#e7f4f9; border-width:2px 5px 2px 5px; }.tree-context li a.disabled { color:gray; }.tree-context li a.disabled ins { }.tree-context li a.disabled:hover { border:0; height:22px; line-height:22px; }.tree-context li a.disabled:hover span { background:#f0f0f0; margin-left:29px; }.tree-context li a.disabled:hover ins { border-color:#f0f0f0; background-color:#f0f0f0; border-width:3px 5px 3px 6px; }';$.tree.plugins.contextmenu.css=this.add_sheet({str:css});}},onrgtclk:function(n,t,e){var opts=$.extend(true,{},$.tree.plugins.contextmenu.defaults,t.settings.plugins.contextmenu);n=$(n);if(n.size()==0)return;$.tree.plugins.contextmenu.data.t=t;if(!n.children("a:eq(0)").hasClass("clicked")){$.tree.plugins.contextmenu.data.a=n;$.tree.plugins.contextmenu.data.r=true;n.children("a").addClass(opts.class_name);e.target.blur();}
else{$.tree.plugins.contextmenu.data.r=false;$.tree.plugins.contextmenu.data.a=(t.selected_arr&&t.selected_arr.length>1)?t.selected_arr:t.selected;}
$.tree.plugins.contextmenu.show(n,t);e.preventDefault();e.stopPropagation();},onchange:function(){$.tree.plugins.contextmenu.hide();},beforedata:function(){$.tree.plugins.contextmenu.hide();},ondestroy:function(){$.tree.plugins.contextmenu.hide();}}}});$(function(){$.tree.plugins.contextmenu.object.hide().appendTo("body");$("#jstree-contextmenu a").live("click",function(event){if(!$(this).hasClass("disabled")){$.tree.plugins.contextmenu.exec.apply(null,[$(this).attr("rel")]);$.tree.plugins.contextmenu.hide();}
event.stopPropagation();event.preventDefault();return false;})
$(document).bind("mousedown",function(event){if($(event.target).parents("#jstree-contextmenu").size()==0)$.tree.plugins.contextmenu.hide();});});})(jQuery);

// jquery.tree.cookie.js

(function($){if(typeof $.cookie=="undefined")throw"jsTree cookie: jQuery cookie plugin not included.";$.extend($.tree.plugins,{"cookie":{defaults:{prefix:"",options:{expires:false,path:false,domain:false,secure:false},types:{selected:true,open:true},keep_selected:false,keep_opened:false},set_cookie:function(type){var opts=$.extend(true,{},$.tree.plugins.cookie.defaults,this.settings.plugins.cookie);if(opts.types[type]!==true)return false;switch(type){case"selected":if(this.settings.rules.multiple!=false&&this.selected_arr.length>1){var val=Array();$.each(this.selected_arr,function(){if(this.attr("id")){val.push(this.attr("id"));}});val=val.join(",");}
else var val=this.selected?this.selected.attr("id"):false;$.cookie(opts.prefix+'selected',val,opts.options);break;case"open":var str="";this.container.find("li.open").each(function(i){if(this.id){str+=this.id+",";}});$.cookie(opts.prefix+'open',str.replace(/,$/ig,""),opts.options);break;}},callbacks:{oninit:function(t){var opts=$.extend(true,{},$.tree.plugins.cookie.defaults,this.settings.plugins.cookie);var tmp=false;tmp=$.cookie(opts.prefix+'open');if(tmp){tmp=tmp.split(",");if(opts.keep_opened)this.settings.opened=$.unique($.merge(tmp,this.settings.opened));else this.settings.opened=tmp;}
tmp=$.cookie(opts.prefix+'selected');if(tmp){tmp=tmp.split(",");if(opts.keep_selected)this.settings.selected=$.unique($.merge(tmp,this.settings.opened));else this.settings.selected=tmp;}},onchange:function(){$.tree.plugins.cookie.set_cookie.apply(this,["selected"]);},onopen:function(){$.tree.plugins.cookie.set_cookie.apply(this,["open"]);},onclose:function(){$.tree.plugins.cookie.set_cookie.apply(this,["open"]);},ondelete:function(){$.tree.plugins.cookie.set_cookie.apply(this,["open"]);},oncopy:function(){$.tree.plugins.cookie.set_cookie.apply(this,["open"]);},oncreate:function(){$.tree.plugins.cookie.set_cookie.apply(this,["open"]);},onmoved:function(){$.tree.plugins.cookie.set_cookie.apply(this,["open"]);}}}});})(jQuery);

// jquery.tree.hotkeys.js

(function($){if(typeof window.hotkeys=="undefined")throw"jsTree hotkeys: jQuery hotkeys plugin not included.";$.extend($.tree.plugins,{"hotkeys":{bound:[],disabled:false,defaults:{hover_mode:false,functions:{"up":function(){$.tree.plugins.hotkeys.get_prev.apply(this);return false;},"down":function(){$.tree.plugins.hotkeys.get_next.apply(this);return false;},"left":function(){$.tree.plugins.hotkeys.get_left.apply(this);return false;},"right":function(){$.tree.plugins.hotkeys.get_right.apply(this);return false;},"f2":function(){if(this.selected)this.rename();return false;},"del":function(){if(this.selected)this.remove();return false;},"ctrl+c":function(){if(this.selected)this.copy();return false;},"ctrl+x":function(){if(this.selected)this.cut();return false;},"ctrl+v":function(){if(this.selected)this.paste();return false;}}},exec:function(key){if($.tree.plugins.hotkeys.disabled)return false;var t=$.tree.focused();if(typeof t.settings.plugins.hotkeys=="undefined")return;var opts=$.extend(true,{},$.tree.plugins.hotkeys.defaults,t.settings.plugins.hotkeys);if(typeof opts.functions[key]=="function")return opts.functions[key].apply(t);},get_next:function(){var opts=$.extend(true,{},$.tree.plugins.hotkeys.defaults,this.settings.plugins.hotkeys);var obj=this.hovered||this.selected;return opts.hover_mode?this.hover_branch(this.next(obj)):this.select_branch(this.next(obj));},get_prev:function(){var opts=$.extend(true,{},$.tree.plugins.hotkeys.defaults,this.settings.plugins.hotkeys);var obj=this.hovered||this.selected;return opts.hover_mode?this.hover_branch(this.prev(obj)):this.select_branch(this.prev(obj));},get_left:function(){var opts=$.extend(true,{},$.tree.plugins.hotkeys.defaults,this.settings.plugins.hotkeys);var obj=this.hovered||this.selected;if(obj){if(obj.hasClass("open"))this.close_branch(obj);else{return opts.hover_mode?this.hover_branch(this.parent(obj)):this.select_branch(this.parent(obj));}}},get_right:function(){var opts=$.extend(true,{},$.tree.plugins.hotkeys.defaults,this.settings.plugins.hotkeys);var obj=this.hovered||this.selected;if(obj){if(obj.hasClass("closed"))this.open_branch(obj);else{return opts.hover_mode?this.hover_branch(obj.find("li:eq(0)")):this.select_branch(obj.find("li:eq(0)"));}}},callbacks:{oninit:function(t){var opts=$.extend(true,{},$.tree.plugins.hotkeys.defaults,this.settings.plugins.hotkeys);for(var i in opts.functions){if(opts.functions.hasOwnProperty(i)&&$.inArray(i,$.tree.plugins.hotkeys.bound)==-1){(function(k){$(document).bind("keydown",{combi:k,disableInInput:true},function(event){return $.tree.plugins.hotkeys.exec(k);});})(i);$.tree.plugins.hotkeys.bound.push(i);}}}}}});})(jQuery);

// jquery.tree.metadata.js

(function($){if(typeof $.metadata=="undefined")throw"jsTree metadata: jQuery metadata plugin not included.";$.extend($.tree.plugins,{"metadata":{defaults:{attribute:"data"},callbacks:{check:function(rule,obj,value,tree){var opts=$.extend(true,{},$.tree.plugins.metadata.defaults,this.settings.plugins.metadata);if(typeof $(obj).metadata({type:"attr",name:opts.attribute})[rule]!="undefined")return $(obj).metadata()[rule];}}}});})(jQuery);

// jquery.tree.themeroller.js

(function($){$.extend($.tree.plugins,{"themeroller":{defaults:{},callbacks:{oninit:function(t){if(this.settings.ui.theme_name!="themeroller")return;var opts=$.extend(true,{},$.tree.plugins.themeroller.defaults,this.settings.plugins.themeroller);this.container.addClass("ui-widget ui-widget-content");$("#"+this.container.attr("id")+" li a").live("mouseover",function(){$(this).addClass("ui-state-hover");});$("#"+this.container.attr("id")+" li a").live("mouseout",function(){$(this).removeClass("ui-state-hover");});},onparse:function(s,t){if(this.settings.ui.theme_name!="themeroller")return;var opts=$.extend(true,{},$.tree.plugins.themeroller.defaults,this.settings.plugins.themeroller);return $(s).find("a").not(".ui-state-default").addClass("ui-state-default").children("ins").addClass("ui-icon").end().end().end();},onselect:function(n,t){if(this.settings.ui.theme_name!="themeroller")return;var opts=$.extend(true,{},$.tree.plugins.themeroller.defaults,this.settings.plugins.themeroller);$(n).children("a").addClass("ui-state-active");},ondeselect:function(n,t){if(this.settings.ui.theme_name!="themeroller")return;var opts=$.extend(true,{},$.tree.plugins.themeroller.defaults,this.settings.plugins.themeroller);$(n).children("a").removeClass("ui-state-active");}}}});})(jQuery);

// jquery.tree.xml_flat.js

(function ($) {
	if(typeof Sarissa == "undefined") throw "jsTree xml_flat: Sarissa is not included.";

	$.extend($.tree.datastores, {
		"xml_flat" : function () {
			return {
				get		: function(obj, t, opts) {
					var str = "";
					if(!obj || $(obj).size() == 0) {
						obj = t.container.children("ul").children("li");
					}
					else obj = $(obj);

					if(obj.size() > 1) {
						var _this = this;
						var str	 = '<root>';
						obj.each(function () {
							opts.callback = true;
							str += _this.get(this, t, opts);
						});
						str		+= '</root>';
						return str;
					}

					if(!opts) var opts = {};
					if(!opts.outer_attrib) opts.outer_attrib = [ "id", "rel", "class" ];
					if(!opts.inner_attrib) opts.inner_attrib = [ ];
					if(!opts.callback) str += '<root>';

					str += '<item ';
					str += ' parent_id="' + (obj.parents("li:eq(0)").size() ? obj.parents("li:eq(0)").attr("id") : 0) + '" ';
					for(var i in opts.outer_attrib) {
						if(!opts.outer_attrib.hasOwnProperty(i)) continue;
						var val = (opts.outer_attrib[i] == "class") ? obj.attr(opts.outer_attrib[i]).toString().replace(/(^| )last( |$)/ig," ").replace(/(^| )(leaf|closed|open)( |$)/ig," ") : obj.attr(opts.outer_attrib[i]);
						if(typeof val != "undefined" && val.toString().replace(" ","").length > 0) str += ' ' + opts.outer_attrib[i] + '="' + val.toString() + '" ';
						delete val;
					}
					str += '>';

					str += '<content>';
					if(t.settings.languages.length) {
						for(var i in t.settings.languages) {
							if(!t.settings.languages.hasOwnProperty(i)) continue;
							str += this.process_inner(obj.children("a." + t.settings.languages[i]), t, opts, t.settings.languages[i]);
						}
					}
					else {
						str += this.process_inner(obj.children("a"), t, opts);
					}
					str += '</content>';
					str += '</item>';

					if(obj.children("ul").size() > 0) {
						var _this = this;
						opts.callback = true;
						obj.children("ul").children("li").each(function () {
							str += _this.get(this, t, opts);
						});
						opts.callback = false;
					}
					if(!opts.callback) str += '</root>';
					return str;
				},
				process_inner : function(obj, t, opts, lang) {
					var str = '<name ';
					if(lang) str += ' lang="' + lang + '" ';
					if(opts.inner_attrib.length || obj.children("ins").get(0).style.backgroundImage.toString().length || obj.children("ins").get(0).className.length) {
						if(obj.children("ins").get(0).style.className.length) {
							str += ' icon="' + obj.children("ins").get(0).style.className + '" ';
						}
						if(obj.children("ins").get(0).style.backgroundImage.length) {
							str += ' icon="' + obj.children("ins").get(0).style.backgroundImage.replace("url(","").replace(")","") + '" ';
						}
						if(opts.inner_attrib.length) {
							for(var j in opts.inner_attrib) {
								if(!opts.inner_attrib.hasOwnProperty(j)) continue;
								var val = obj.attr(opts.inner_attrib[j]);
								if(typeof val != "undefined" && val.toString().replace(" ","").length > 0) str += ' ' + opts.inner_attrib[j] + '="' + val.toString() + '" ';
								delete val;
							}
						}
					}
					str += '><![CDATA[' + t.get_text(obj,lang) + ']]></name>';
					return str;
				},

				parse	: function(data, t, opts, callback) {
					var processor = new XSLTProcessor();
					processor.importStylesheet($.tree.datastores.xml_flat.xsl);

					var result = $((new XMLSerializer()).serializeToString(processor.transformToDocument(data)));
					if(result.is("ul"))	result = result.html();
					else				result = result.find("ul").html();
					if(callback) callback.call(null,result);

					// Disabled because of Chrome issues
					// if(callback) callback.call(null,(new XMLSerializer()).serializeToString(processor.transformToDocument(data)).replace(/^<ul[^>]*>/i,"").replace(/<\/ul>$/i,""));
				},
				load	: function(data, t, opts, callback) {
					if(opts.static) {
						callback.call(null, (new DOMParser()).parseFromString(opts.static,'text/xml'));
					}
					else {
						$.ajax({
							'type'		: opts.method,
							'url'		: opts.url, 
							'data'		: data, 
							'dataType'	: "xml",
							'success'	: function (d, textStatus) {
								callback.call(null, d);
							},
							'error'		: function (xhttp, textStatus, errorThrown) { 
								callback.call(null, false);
								t.error(errorThrown + " " + textStatus); 
							}
						});
					}
				}
			}
		}
	});
	$.tree.datastores.xml_flat.xsl = (new DOMParser()).parseFromString('<?xml version="1.0" encoding="utf-8" ?><xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" ><xsl:output method="html" encoding="utf-8" omit-xml-declaration="yes" standalone="no" indent="no" media-type="text/xml" /><xsl:template match="/"><ul><xsl:for-each select="//item[not(@parent_id) or @parent_id=0]"><xsl:call-template name="nodes"><xsl:with-param name="node" select="." /><xsl:with-param name="is_last" select="number(position() = last())" /></xsl:call-template></xsl:for-each></ul></xsl:template><xsl:template name="nodes"><xsl:param name="node" /><xsl:param name="theme_path" /><xsl:param name="theme_name" /><xsl:param name="is_last" /><xsl:variable name="children" select="count(//item[@parent_id=$node/attribute::id]) &gt; 0" /><li><xsl:attribute name="class"><xsl:if test="$is_last = true()"> last </xsl:if><xsl:choose><xsl:when test="@state = \'open\'"> open </xsl:when><xsl:when test="$children or @hasChildren or @state = \'closed\'"> closed </xsl:when><xsl:otherwise> leaf </xsl:otherwise></xsl:choose><xsl:value-of select="@class" /></xsl:attribute><xsl:for-each select="@*"><xsl:if test="name() != \'parent_id\' and name() != \'hasChildren\' and name() != \'class\' and name() != \'state\'"><xsl:attribute name="{name()}"><xsl:value-of select="." /></xsl:attribute></xsl:if></xsl:for-each><xsl:for-each select="content/name"><a href="#"><xsl:attribute name="class"><xsl:value-of select="@lang" /><xsl:value-of select="@class" /></xsl:attribute><xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute><xsl:for-each select="@*"><xsl:if test="name() != \'style\' and name() != \'class\'"><xsl:attribute name="{name()}"><xsl:value-of select="." /></xsl:attribute></xsl:if></xsl:for-each><ins><xsl:if test="string-length(attribute::icon) > 0"><xsl:choose><xsl:when test="not(contains(@icon,\'/\'))"><xsl:attribute name="class"><xsl:value-of select="@icon" /></xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="style">background-image:url(<xsl:value-of select="@icon" />);</xsl:attribute></xsl:otherwise></xsl:choose></xsl:if><xsl:text>&#xa0;</xsl:text></ins><xsl:value-of select="." /></a></xsl:for-each><xsl:if test="$children or @hasChildren"><ul><xsl:for-each select="//item[@parent_id=$node/attribute::id]"><xsl:call-template name="nodes"><xsl:with-param name="node" select="." /><xsl:with-param name="is_last" select="number(position() = last())" /></xsl:call-template></xsl:for-each></ul></xsl:if></li></xsl:template></xsl:stylesheet>','text/xml');
})(jQuery);

// jquery.tree.xml_nested.js

(function ($) {
	if(typeof Sarissa == "undefined") throw "jsTree xml_nested: Sarissa is not included.";

	$.extend($.tree.datastores, {
		"xml_nested" : function () {
			return {
				get		: function(obj, tree, opts) {
					var str = "";
					if(!obj || $(obj).size() == 0) {
						obj = tree.container.children("ul").children("li");
					}
					else obj = $(obj);

					if(obj.size() > 1) {
						var _this = this;
						var str	 = '<root>';
						obj.each(function () {
							str += _this.get(this, tree, $.extend(true, {}, opts, { callback : true }));
						});
						str		+= '</root>';
						return str;
					}

					if(!opts) var opts = {};
					if(!opts.outer_attrib) opts.outer_attrib = [ "id", "rel", "class" ];
					if(!opts.inner_attrib) opts.inner_attrib = [ ];
					if(!opts.callback) str += '<root>';

					str += '<item ';
					for(var i in opts.outer_attrib) {
						if(!opts.outer_attrib.hasOwnProperty(i)) continue;
						var val = (opts.outer_attrib[i] == "class") ? obj.attr(opts.outer_attrib[i]).replace(/(^| )last( |$)/ig," ").replace(/(^| )(leaf|closed|open)( |$)/ig," ") : obj.attr(opts.outer_attrib[i]);
						if(typeof val != "undefined" && val.toString().replace(" ","").length > 0) str += ' ' + opts.outer_attrib[i] + '="' + val.toString() + '" ';
						delete val;
					}
					str += '>';

					str += '<content>';
					if(tree.settings.languages.length) {
						for(var i in tree.settings.languages) {
							if(!tree.settings.languages.hasOwnProperty(i)) continue;
							str += this.process_inner(obj.children("a." + tree.settings.languages[i]), tree, opts, tree.settings.languages[i]);
						}
					}
					else {
						str += this.process_inner(obj.children("a"), tree, opts);
					}
					str += '</content>';

					if(obj.children("ul").size() > 0) {
						var _this = this;
						obj.children("ul").children("li").each(function () {
							str += _this.get(this, tree, $.extend(true, {}, opts, { callback : true }));
						});
					}
					str += '</item>';

					if(!opts.callback) str += '</root>';
					return str;
				},
				process_inner : function(obj, tree, opts, lang) {
					var str = '<name ';
					if(lang) str += ' lang="' + lang + '" ';
					if(opts.inner_attrib.length || obj.children("ins").get(0).style.backgroundImage.toString().length || obj.children("ins").get(0).className.length) {
						if(obj.children("ins").get(0).style.className.length) {
							str += ' icon="' + obj.children("ins").get(0).style.className + '" ';
						}
						if(obj.children("ins").get(0).style.backgroundImage.length) {
							str += ' icon="' + obj.children("ins").get(0).style.backgroundImage.replace("url(","").replace(")","") + '" ';
						}
						if(opts.inner_attrib.length) {
							for(var j in opts.inner_attrib) {
								if(!opts.inner_attrib.hasOwnProperty(j)) continue;
								var val = obj.attr(opts.inner_attrib[j]);
								if(typeof val != "undefined" && val.toString().replace(" ","").length > 0) str += ' ' + opts.inner_attrib[j] + '="' + val.toString() + '" ';
								delete val;
							}
						}
					}
					str += '><![CDATA[' + tree.get_text(obj,lang) + ']]></name>';
					return str;
				},
				parse	: function(data, tree, opts, callback) { 
					var processor = new XSLTProcessor();
					processor.importStylesheet($.tree.datastores.xml_nested.xsl);

					var result = $((new XMLSerializer()).serializeToString(processor.transformToDocument(data)).replace('<?xml version="1.0"?>',''));
					// Opera bug
					if(result.size() > 1)	result = result.eq(1);
					if(result.is("ul"))		result = result.html();
					else					result = result.find("ul:eq(0)").html();
					if(callback) callback.call(null,result);

					// Disabled because of Chrome issues
					// if(callback) callback.call(null,(new XMLSerializer()).serializeToString(processor.transformToDocument(data)).replace(/^<ul[^>]*>/i,"").replace(/<\/ul>$/i,""));
				},
				load	: function(data, tree, opts, callback) {
					if(opts.staticData) {
						callback.call(null, (new DOMParser()).parseFromString(opts.staticData,'text/xml'));
					}
					else {
						$.ajax({
							'type'		: opts.method,
							'url'		: opts.url, 
							'data'		: data, 
							'dataType'	: "xml",
							'success'	: function (d, textStatus) {
								callback.call(null, d);
							},
							'error'		: function (xhttp, textStatus, errorThrown) { 
								callback.call(null, false);
								tree.error(errorThrown + " " + textStatus); 
							}
						});
					}
				}
			}
		}
	});
	$.tree.datastores.xml_nested.xsl = (new DOMParser()).parseFromString('<?xml version="1.0" encoding="utf-8" ?><xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" ><xsl:output method="html" encoding="utf-8" omit-xml-declaration="yes" standalone="no" indent="no" media-type="text/html" /><xsl:template match="/"><xsl:call-template name="nodes"><xsl:with-param name="node" select="/root" /></xsl:call-template></xsl:template><xsl:template name="nodes"><xsl:param name="node" /><ul><xsl:for-each select="$node/item"><xsl:variable name="children" select="count(./item) &gt; 0" /><li><xsl:attribute name="class"><xsl:if test="position() = last()"> last </xsl:if><xsl:choose><xsl:when test="@state = \'open\'"> open </xsl:when><xsl:when test="$children or @hasChildren"> closed </xsl:when><xsl:otherwise> leaf </xsl:otherwise></xsl:choose><xsl:value-of select="@class" /></xsl:attribute><xsl:for-each select="@*"><xsl:if test="name() != \'class\' and name() != \'state\' and name() != \'hasChildren\'"><xsl:attribute name="{name()}"><xsl:value-of select="." /></xsl:attribute></xsl:if></xsl:for-each><xsl:for-each select="content/name"><a href=""><xsl:attribute name="class"><xsl:value-of select="@lang" /><xsl:value-of select="@class" /></xsl:attribute><xsl:attribute name="style"><xsl:value-of select="@style" /></xsl:attribute><xsl:for-each select="@*"><xsl:if test="name() != \'style\' and name() != \'class\'"><xsl:attribute name="{name()}"><xsl:value-of select="." /></xsl:attribute></xsl:if></xsl:for-each><ins><xsl:if test="string-length(attribute::icon) > 0"><xsl:choose><xsl:when test="not(contains(@icon,\'/\'))"><xsl:attribute name="class"><xsl:value-of select="@icon" /></xsl:attribute></xsl:when><xsl:otherwise><xsl:attribute name="style">background-image:url(<xsl:value-of select="@icon" />);</xsl:attribute></xsl:otherwise></xsl:choose></xsl:if><xsl:text>&#xa0;</xsl:text></ins><xsl:value-of select="current()" /></a></xsl:for-each><xsl:if test="$children or @hasChildren"><xsl:call-template name="nodes"><xsl:with-param name="node" select="current()" /></xsl:call-template></xsl:if></li></xsl:for-each></ul></xsl:template></xsl:stylesheet>','text/xml');
})(jQuery);

// shCore.js

eval(function(p,a,c,k,e,d){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--){d[e(c)]=k[c]||e(c)}k=[function(e){return d[e]}];e=function(){return'\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c])}}return p}('f(!1q.2E){l 2E=h(){l p={77:{"1e-1f":"","79-2P":1,"1I":u,"6V-70":U,"1C-2A":4,"5f":N,"4Z":U,"1z":U,"56":N,"7G-7F":U,"6Z":N,"4S-1m":U},M:{52:u,5P:16,5S:16,8k:N,8l:N,83:"4R",1k:{3Y:"97 1c",41:"9b 1c",5U:"9O 93 7A",6t:"9B I 9E 23 8w 7A 8o",34:"34",6P:"?",1v:"2E\\n\\n",6F:"8p\'t 8I 87 D: ",7X:"8V 8v\'t bD D 2u-2c bf: ",6H:"<!be 2u aV \\"-//9V//6U bz 1.0 bx//bI\\" \\"2g://6D.6v.6m/bi/7c/6U/7c-a4.aw\\"><2u ay=\\"2g://6D.6v.6m/as/8r\\"><6l><8T 2g-92=\\"8P-8L\\" 5B=\\"2d/2u; 8E=8s-8\\" /><3B>8C 2E</3B></6l><2L 1n=\\"3N-8x:8Z,9y,9H,9I-9Q;9S-4v:#9K;4v:#9J;3N-2A:9L;2d-6k:6i;\\"><A 1n=\\"2d-6k:6i;5D-43:99;\\"><A 1n=\\"3N-2A:9p-9o;\\">2E</A><A 1n=\\"3N-2A:.9m;5D-9l:9k;\\"><A>6f 2.0.9j (9n 9s 6n)</A><A><a 2q=\\"2g://6j.4U\\" 9r=\\"57\\" 1n=\\"4v:#9h;2d-9g:98;\\">2g://6j.4U</a></A></A><A>96 I 94 95.</A><A>9f 9e-6n 9c 9t.</A></A></2L></2u>"},6T:N},1t:{4D:u,3k:u,3P:u,5K:{}},2B:{},85:{9u:/\\/\\*[\\s\\S]*?\\*\\//4k,9N:/\\/\\/.*$/4k,9M:/#.*$/4k,9P:/"(?:\\.|(\\\\\\")|[^\\""\\n])*"/g,9T:/\'(?:\\.|(\\\\\\\')|[^\\\'\'\\n])*\'/g,9R:/"(?:\\.|(\\\\\\")|[^\\""])*"/g,9z:/\'(?:\\.|(\\\\\\\')|[^\\\'\'])*\'/g,3p:/\\w+:\\/\\/[\\w-.\\/?%&=]*/g,9x:{E:/(&1F;|<)\\?=?/g,13:/\\?(&2o;|>)/g},9v:{E:/(&1F;|<)%=?/g,13:/%(&2o;|>)/g},9w:{E:/(&1F;|<)\\s*2c.*?(&2o;|>)/4x,13:/(&1F;|<)\\/\\s*2c\\s*(&2o;|>)/4x}},1z:{12:h(3s){l 3y=L.1s("3j"),4o=p.1z.65;3y.J="1z";D(l 2Y 23 4o){l 6o=4o[2Y],4J=T 6o(3s),28=4J.12();3s.5I[2Y]=4J;f(28==u){1H}f(9G(28)=="9F"){28=p.1z.6s(28,3s.1g,2Y)}28.J+="5k "+2Y;3y.1G(28)}q 3y},6s:h(4A,6r,4h){l a=L.1s("a"),4Q=a.1n,4P=p.M,4F=4P.5P,48=4P.5S;a.2q="#"+4h;a.3B=4A;a.5M=6r;a.6q=4h;a.1x=4A;f(55(4F)==N){4Q.1S=4F+"5x"}f(55(48)==N){4Q.2t=48+"5x"}a.8t=h(e){8D{p.1z.6p(c,e||1q.6w,c.5M,c.6q)}8m(e){p.B.1v(e.6u)}q N};q a},6p:h(69,68,6h,6g,67){l 3U=p.1t.5K[6h],3X;f(3U==u||(3X=3U.5I[6g])==u){q u}q 3X.2h(69,68,67)},65:{3Y:h(4b){c.12=h(){f(4b.V("56")!=U){q}q p.M.1k.3Y};c.2h=h(42,8X,91){l A=4b.A;42.7T.5a(42);A.J=A.J.C("51","")}},41:h(66){c.12=h(){q p.M.1k.41};c.2h=h(8R,8Q,8J){l 3Q=p.B.3G(66.4W).C(/</g,"&1F;"),2i=p.B.54("","57",8H,8G,"8K=0, 8O=1, 8N=0, 6O=1");3Q=p.B.2D(3Q);2i.L.3h("<4R>"+3Q+"</4R>");2i.L.5O()}},5U:h(5e){l 3S,8F,5L=5e.1g;c.12=h(){l 2S=p.M;f(2S.52==u){q u}h 1A(5E){l 5s="";D(l 5y 23 5E){5s+="<8S 1f=\'"+5y+"\' 1U=\'"+5E[5y]+"\'/>"}q 5s};h 2v(5t){l 5Q="";D(l 5w 23 5t){5Q+=" "+5w+"=\'"+5t[5w]+"\'"}q 5Q};l 5m={1S:2S.5P,2t:2S.5S,1g:5L+"b8",6N:"b7/x-6a-6b",3B:p.M.1k.5U},5h={b6:"b4",b5:"b9",ba:"5M="+5L,bd:"N"},5g=2S.52,3H;f(/bb/i.1R(5Z.5W)){3H="<6e"+2v({bc:"b3:b2-aU-aT-aS-aQ",aR:"2g://aW.b1.4U/b0/6a/aX/6b/bg.bh#6f=9,0,0,0"})+2v(5m)+">"+1A(5h)+1A({bB:5g})+"</6e>"}F{3H="<bA"+2v(5m)+2v(5h)+2v({bC:5g})+"/>"}3S=L.1s("A");3S.1x=3H;q 3S};c.2h=h(bH,bG,5T){l 6d=5T.bE;6z(6d){2N"7u":l 53=p.B.2D(p.B.3G(5e.4W).C(/&1F;/g,"<").C(/&2o;/g,">").C(/&bw;/g,"&"));f(1q.6c){1q.6c.bm("2d",53)}F{q p.B.2D(53)}2N"bk":p.B.1v(p.M.1k.6t);2m;2N"bj":p.B.1v(5T.6u);2m}}},bo:h(58){c.12=h(){q p.M.1k.34};c.2h=h(bu,bt,bs){l 1W=L.1s("bp"),1O=u;f(p.1t.3P!=u){L.2L.5a(p.1t.3P)}p.1t.3P=1W;1W.1n.bq="aP:aO;1S:6L;2t:6L;E:-6K;43:-6K;";L.2L.1G(1W);1O=1W.5c.L;6J(1O,1q.L);1O.3h("<A 1e=\\""+58.A.J.C("51","")+" ae\\">"+58.A.1x+"</A>");1O.5O();1W.5c.4d();1W.5c.34();h 6J(6M,64){l 2F=64.82("4Y");D(l i=0;i<2F.v;i++){f(2F[i].6R.ac()=="6Q"&&/aa\\.19$/.1R(2F[i].2q)){6M.3h("<4Y 6N=\\"2d/19\\" 6R=\\"6Q\\" 2q=\\""+2F[i].2q+"\\"></4Y>")}}}}},af:h(ag){c.12=h(){q p.M.1k.6P};c.2h=h(aj,ah){l 2i=p.B.54("","57",ai,a9,"6O=0"),1O=2i.L;1O.3h(p.M.1k.6H);1O.5O();2i.4d()}}}},B:{5H:h(6G){q 6G+3J.9Y(3J.9W()*9X).2r()},5o:h(5R,5G){l 3m={},1T;D(1T 23 5R){3m[1T]=5R[1T]}D(1T 23 5G){3m[1T]=5G[1T]}q 3m},8d:h(5z){6z(5z){2N"U":q U;2N"N":q N}q 5z},54:h(3p,6x,44,4c,2J){l x=(6y.1S-44)/2,y=(6y.2t-4c)/2;2J+=", E="+x+", 43="+y+", 1S="+44+", 2t="+4c;2J=2J.C(/^,/,"");l 49=1q.a5(3p,6x,2J);49.4d();q 49},7Q:h(1M,25,24){f(1M.6A){1M["e"+25+24]=24;1M[25+24]=h(){1M["e"+25+24](1q.6w)};1M.6A("an"+25,1M[25+24])}F{1M.aG(25,24,N)}},1v:h(z){1v(p.M.1k.1v+z)},4l:h(4M,6B){l 2k=p.1t.4D,3b=u;f(2k==u){2k={};D(l 4G 23 p.2B){l 37=p.2B[4G].aF;f(37==u){1H}D(l i=0;i<37.v;i++){2k[37[i]]=4G}}p.1t.4D=2k}3b=p.2B[2k[4M]];f(3b==u&&6B!=N){p.B.1v(p.M.1k.6F+4M)}q 3b},4n:h(z,6E){l 2U=z.1P("\\n");D(l i=0;i<2U.v;i++){2U[i]=6E(2U[i])}q 2U.5u("\\n")},74:h(){l A=L.1s("A"),3e=L.1s("A"),6C=10,i=1;29(i<=aD){f(i%6C===0){A.1x+=i;i+=(i+"").v}F{A.1x+="&aI;";i++}}3e.J="5f 2P";3e.1G(A);q 3e},6W:h(z){q z.C(/^[ ]*[\\n]+|[\\n]*[ ]*$/g,"")},84:h(z){l 3d,4u={},4p=T R("^\\\\[(?<4q>(.*?))\\\\]$"),6S=T R("(?<1f>[\\\\w-]+)"+"\\\\s*:\\\\s*"+"(?<1U>"+"[\\\\w-%#]+|"+"\\\\[.*?\\\\]|"+"\\".*?\\"|"+"\'.*?\'"+")\\\\s*;?","g");29((3d=6S.Q(z))!=u){l 2f=3d.1U.C(/^[\'"]|[\'"]$/g,"");f(2f!=u&&4p.1R(2f)){l m=4p.Q(2f);2f=m.4q.v>0?m.4q.1P(/\\s*,\\s*/):[]}4u[3d.1f]=2f}q 4u},7g:h(z,19){f(z==u||z.v==0||z=="\\n"){q z}z=z.C(/</g,"&1F;");z=z.C(/ {2,}/g,h(m){l 4r="";D(l i=0;i<m.v-1;i++){4r+="&1X;"}q 4r+" "});f(19!=u){z=p.B.4n(z,h(2s){f(2s.v==0){q""}l 3c="";2s=2s.C(/^(&1X;| )+/,h(s){3c=s;q""});f(2s.v==0){q 3c}q 3c+"<I 1e=\\""+19+"\\">"+2s+"</I>"})}q z},7a:h(61,62){l 2I=61.2r();29(2I.v<62){2I="0"+2I}q 2I},5p:h(){l 3x=L.1s("A"),35,3r=0,5i=L.2L,1g=p.B.5H("5p"),2Q="<A 1e=\\"",2V="</A>",4H="</1V>";3x.1x=2Q+"7P\\">"+2Q+"1m\\">"+2Q+"2P\\">"+2Q+"5B"+"\\"><1V 1e=\\"7i\\"><1V 1g=\\""+1g+"\\">&1X;"+4H+4H+2V+2V+2V+2V;5i.1G(3x);35=L.ar(1g);f(/aq/i.1R(5Z.5W)){l 63=1q.ao(35,u);3r=7b(63.ap("1S"))}F{3r=35.at}5i.5a(3x);q 3r},76:h(5Y,60){l 1C="";D(l i=0;i<60;i++){1C+=" "}q 5Y.C(/\\t/g,1C)},71:h(2C,4w){l az=2C.1P("\\n"),1C="\\t",40="";D(l i=0;i<50;i++){40+="                    "}h 6I(3z,18,5X){q 3z.1Q(0,18)+40.1Q(0,5X)+3z.1Q(18+1,3z.v)};2C=p.B.4n(2C,h(2a){f(2a.1i(1C)==-1){q 2a}l 18=0;29((18=2a.1i(1C))!=-1){l 7r=4w-18%4w;2a=6I(2a,18,7r)}q 2a});q 2C},3G:h(z){l br=/<br\\s*\\/?>|&1F;br\\s*\\/?&2o;/4x;f(p.M.8k==U){z=z.C(br,"\\n")}f(p.M.8l==U){z=z.C(br,"")}q z},33:h(z){q z.C(/\\s*$/g,"").C(/^\\s*/,"")},2D:h(z){l 21=p.B.3G(z).1P("\\n"),av=T 5V(),8a=/^\\s*/,1Z=ax;D(l i=0;i<21.v&&1Z>0;i++){l 3V=21[i];f(p.B.33(3V).v==0){1H}l 3W=8a.Q(3V);f(3W==u){q z}1Z=3J.1Z(3W[0].v,1Z)}f(1Z>0){D(l i=0;i<21.v;i++){21[i]=21[i].1Q(1Z)}}q 21.5u("\\n")},7d:h(2K,2O){f(2K.G<2O.G){q-1}F{f(2K.G>2O.G){q 1}F{f(2K.v<2O.v){q-1}F{f(2K.v>2O.v){q 1}}}}q 0},30:h(7S,2H){h 7R(4V,7Y){q[T p.4i(4V[0],4V.G,7Y.19)]};l au=0,5N=u,39=[],7Z=2H.4L?2H.4L:7R;29((5N=2H.3q.Q(7S))!=u){39=39.31(7Z(5N,2H))}q 39},7C:h(86){q 86.C(p.85.3p,h(m){q"<a 2q=\\""+m+"\\">"+m+"</a>"})}},1I:h(88,4T){h 81(5j){l 59=[];D(l i=0;i<5j.v;i++){59.K(5j[i])}q 59};l 3g=4T?[4T]:81(L.82(p.M.83)),80="1x",2e=u;f(3g.v===0){q}D(l i=0;i<3g.v;i++){l 2G=3g[i],2l=p.B.84(2G.J),32;2l=p.B.5o(88,2l);32=2l["87"];f(32==u){1H}f(2l["2u-2c"]=="U"){2e=T p.4B(32)}F{l 4O=p.B.4l(32);f(4O){2e=T 4O()}F{1H}}2e.1I(2G[80],2l);l 2p=2e.A;f(p.M.6T){2p=L.1s("aA");2p.1U=2e.A.1x;2p.1n.1S="aB";2p.1n.2t="aK"}2G.7T.aJ(2p,2G)}},aL:h(7U){p.B.7Q(1q,"aM",h(){p.1I(7U)})}};p.4i=h(4j,7V,19){c.1U=4j;c.G=7V;c.v=4j.v;c.19=19};p.4i.Y.2r=h(){q c.1U};p.4B=h(4y){l 1J=p.B.4l(4y),4z=T p.2B.aN(),aH=u;f(1J==u){q}1J=T 1J();c.4E=4z;f(1J.3O==u){p.B.1v(p.M.1k.7X+4y);q}4z.5n.K({3q:1J.3O.I,4L:89});h 3a(4K,7W){D(l j=0;j<4K.v;j++){4K[j].G+=7W}};h 89(17,aC){l 8f=17.I,1L=[],4N=1J.5n,8e=17.G+17.E.v,2Z=1J.3O,1l;D(l i=0;i<4N.v;i++){1l=p.B.30(8f,4N[i]);3a(1l,8e);1L=1L.31(1l)}f(2Z.E!=u&&17.E!=u){1l=p.B.30(17.E,2Z.E);3a(1l,17.G);1L=1L.31(1l)}f(2Z.13!=u&&17.13!=u){1l=p.B.30(17.13,2Z.13);3a(1l,17.G+17[0].aE(17.13));1L=1L.31(1l)}q 1L}};p.4B.Y.1I=h(8h,8i){c.4E.1I(8h,8i);c.A=c.4E.A};p.8b=h(){};p.8b.Y={V:h(8c,8g){l 3Z=c.1A[8c];q p.B.8d(3Z==u?8g:3Z)},12:h(8j){q L.1s(8j)},72:h(38,7O){l 2w=[];f(38!=u){D(l i=0;i<38.v;i++){2w=2w.31(p.B.30(7O,38[i]))}}2w=2w.am(p.B.7d);q 2w},73:h(){l 26=c.2R;D(l i=0;i<26.v;i++){f(26[i]===u){1H}l 2x=26[i],45=2x.G+2x.v;D(l j=i+1;j<26.v&&26[i]!==u;j++){l 20=26[j];f(20===u){1H}F{f(20.G>45){2m}F{f(20.G==2x.G&&20.v>2x.v){c.2R[i]=u}F{f(20.G>=2x.G&&20.G<45){c.2R[j]=u}}}}}}},7m:h(2M){l 36=2M.1P(/\\n/g),3f=7b(c.V("79-2P")),7e=(3f+36.v).2r().v,7f=c.V("1I",[]);2M="";D(l i=0;i<36.v;i++){l 1r=36[i],2y=/^(&1X;|\\s)+/.Q(1r),5A="2P a3"+(i%2==0?1:2),7j=p.B.7a(3f+i,7e),7k=7f.1i((3f+i).2r())!=-1,1E=u;f(2y!=u){1E=2y[0].2r();1r=1r.1Q(1E.v);1E=1E.C(/&1X;/g," ");2y=p.1t.3k*1E.v}F{2y=0}1r=p.B.33(1r);f(1r.v==0){1r="&1X;"}f(7k){5A+=" a6"}2M+="<A 1e=\\""+5A+"\\">"+"<I 1e=\\"a7\\">"+7j+".</I>"+"<1V 1e=\\"5B\\">"+(1E!=u?"<I 1e=\\"a2\\">"+1E.C(/\\s/g,"&1X;")+"</I>":"")+"<1V 1e=\\"7i\\" 1n=\\"5D-E: "+2y+"5x !78;\\">"+1r+"</1V>"+"</1V>"+"</A>"}q 2M},7l:h(5v,5r){l 18=0,3o="",3n=p.B.7g;D(l i=0;i<5r.v;i++){l 1N=5r[i];f(1N===u||1N.v===0){1H}3o+=3n(5v.1Q(18,1N.G-18),"7h")+3n(1N.1U,1N.19);18=1N.G+1N.v}3o+=3n(5v.1Q(18),"7h");q 3o},1I:h(1j,6Y){l a1=p.M,3l=p.1t,A,9Z,3i,a0="78";c.1A={};c.A=u;c.1m=u;c.I=u;c.1h=u;c.5I={};c.1g=p.B.5H("a8");3l.5K[c.1g]=c;f(1j===u){1j=""}f(3l.3k===u){3l.3k=p.B.5p()}c.1A=p.B.5o(p.77,6Y||{});f(c.V("6Z")==U){c.1A.1z=c.1A.4Z=N}c.A=A=c.12("3j");c.1m=c.12("3j");c.1m.J="1m";J="7P";A.1g=c.1g;f(c.V("56")){J+=" 51"}f(c.V("4Z")==N){J+=" ak"}f(c.V("4S-1m")==N){c.1m.J+=" al-4S"}J+=" "+c.V("1e-1f");A.J=J;c.4W=1j;c.I=p.B.6W(1j).C(/\\r/g," ");3i=c.V("1C-2A");c.I=c.V("6V-70")==U?p.B.71(c.I,3i):p.B.76(c.I,3i);c.I=p.B.2D(c.I);f(c.V("1z")){c.1h=c.12("3j");c.1h.J="1h";c.1h.1G(p.1z.12(c));A.1G(c.1h);l 1h=c.1h;h 5d(){1h.J=1h.J.C("75","")};A.ab=h(){5d();1h.J+=" 75"};A.ad=h(){5d()}}f(c.V("5f")){A.1G(p.B.74())}A.1G(c.1m);c.2R=c.72(c.5n,c.I);c.73();1j=c.7l(c.I,c.2R);1j=c.7m(p.B.33(1j));f(c.V("7G-7F")){1j=p.B.7C(1j)}c.1m.1x=1j},bn:h(z){z=z.C(/^\\s+|\\s+$/g,"").C(/\\s+/g,"\\\\b|\\\\b");q"\\\\b"+z+"\\\\b"},bl:h(2W){c.3O={E:{3q:2W.E,19:"2c"},13:{3q:2W.13,19:"2c"},I:T R("(?<E>"+2W.E.1c+")"+"(?<I>.*?)"+"(?<13>"+2W.13.1c+")","bv")}}};q p}()}f(!5V.1i){5V.Y.1i=h(7M,3R){3R=3J.bF(3R||0,0);D(l i=3R;i<c.v;i++){f(c[i]==7M){q i}}q-1}}f(!1q.R){(h(){l 2z={Q:11.Y.Q,7K:5b.Y.7K,C:5b.Y.C,1P:5b.Y.1P},1K={W:/(?:[^\\\\([#\\s.]+|\\\\(?!k<[\\w$]+>|[7B]{[^}]+})[\\S\\s]?|\\((?=\\?(?!#|<[\\w$]+>)))+|(\\()(?:\\?(?:(#)[^)]*\\)|<([$\\w]+)>))?|\\\\(?:k<([\\w$]+)>|[7B]{([^}]+)})|(\\[\\^?)|([\\S\\s])/g,by:/(?:[^$]+|\\$(?![1-9$&`\']|{[$\\w]+}))+|\\$(?:([1-9]\\d*|[$&`\'])|{([$\\w]+)})/g,3M:/^(?:\\s+|#.*)+/,5F:/^(?:[?*+]|{\\d+(?:,\\d*)?})/,7x:/&&\\[\\^?/g,7v:/]/g},7n=h(5l,5k,4X){D(l i=4X||0;i<5l.v;i++){f(5l[i]===5k){q i}}q-1},7y=/()??/.Q("")[1]!==3K,3w={};R=h(1d,1Y){f(1d 3T 11){f(1Y!==3K){3L 7N("4C\'t 4I bJ 7H aY 7J 11 4X aZ")}q 1d.3C()}l 1Y=1Y||"",7w=1Y.1i("s")>-1,6X=1Y.1i("x")>-1,5q=N,3u=[],14=[],W=1K.W,H,3D,3F,3E,3v;W.O=0;29(H=2z.Q.2n(W,1d)){f(H[2]){f(!1K.5F.1R(1d.15(W.O))){14.K("(?:)")}}F{f(H[1]){3u.K(H[3]||u);f(H[3]){5q=U}14.K("(")}F{f(H[4]){3E=7n(3u,H[4]);14.K(3E>-1?"\\\\"+(3E+1)+(55(1d.5J(W.O))?"":"(?:)"):H[0])}F{f(H[5]){14.K(3w.7t?3w.7t.7u(H[5],H[0].5J(1)==="P"):H[0])}F{f(H[6]){f(1d.5J(W.O)==="]"){14.K(H[6]==="["?"(?!)":"[\\\\S\\\\s]");W.O++}F{3D=R.7q("&&"+1d.15(H.G),1K.7x,1K.7v,"",{7s:"\\\\"})[0];14.K(H[6]+3D+"]");W.O+=3D.v+1}}F{f(H[7]){f(7w&&H[7]==="."){14.K("[\\\\S\\\\s]")}F{f(6X&&1K.3M.1R(H[7])){3F=2z.Q.2n(1K.3M,1d.15(W.O-1))[0].v;f(!1K.5F.1R(1d.15(W.O-1+3F))){14.K("(?:)")}W.O+=3F-1}F{14.K(H[7])}}}F{14.K(H[0])}}}}}}}3v=11(14.5u(""),2z.C.2n(1Y,/[8y]+/g,""));3v.1u={1c:1d,2j:5q?3u:u};q 3v};R.8B=h(1f,o){3w[1f]=o};11.Y.Q=h(z){l 1b=2z.Q.2n(c,z),1f,i,5C;f(1b){f(7y&&1b.v>1){5C=T 11("^"+c.1c+"$(?!\\\\s)",c.4a());2z.C.2n(1b[0],5C,h(){D(i=1;i<7z.v-2;i++){f(7z[i]===3K){1b[i]=3K}}})}f(c.1u&&c.1u.2j){D(i=1;i<1b.v;i++){1f=c.1u.2j[i-1];f(1f){1b[1f]=1b[i]}}}f(c.3A&&c.O>(1b.G+1b[0].v)){c.O--}}q 1b}})()}11.Y.4a=h(){q(c.3A?"g":"")+(c.8M?"i":"")+(c.7D?"m":"")+(c.3M?"x":"")+(c.8Y?"y":"")};11.Y.3C=h(7o){l 4g=T R(c.1c,(7o||"")+c.4a());f(c.1u){4g.1u={1c:c.1u.1c,2j:c.1u.2j?c.1u.2j.15(0):u}}q 4g};11.Y.2n=h(90,z){q c.Q(z)};11.Y.8W=h(8U,7p){q c.Q(7p[0])};R.47=h(4f,4e){l 46="/"+4f+"/"+(4e||"");q R.47[46]||(R.47[46]=T R(4f,4e))};R.3t=h(z){q z.C(/[-[\\]{}()*+?.\\\\^$|,#\\s]/g,"\\\\$&")};R.7q=h(z,E,Z,1a,2T){l 2T=2T||{},2X=2T.7s,X=2T.8A,1a=1a||"",4s=1a.1i("g")>-1,7E=1a.1i("i")>-1,7L=1a.1i("m")>-1,4t=1a.1i("y")>-1,1a=1a.C(/y/g,""),E=E 3T 11?(E.3A?E:E.3C("g")):T R(E,"g"+1a),Z=Z 3T 11?(Z.3A?Z:Z.3C("g")):T R(Z,"g"+1a),1D=[],2b=0,1o=0,1p=0,1y=0,27,22,1w,1B,3I,4m;f(2X){f(2X.v>1){3L 8n("4C\'t 4I 8q 8z 7J 3t 7I")}f(7L){3L 7N("4C\'t 4I 3t 7I 7H 9U 9D 7D 9C")}3I=R.3t(2X);4m=T 11("^(?:"+3I+"[\\\\S\\\\s]|(?:(?!"+E.1c+"|"+Z.1c+")[^"+3I+"])+)+",7E?"i":"")}29(U){E.O=Z.O=1p+(2X?(4m.Q(z.15(1p))||[""])[0].v:0);1w=E.Q(z);1B=Z.Q(z);f(1w&&1B){f(1w.G<=1B.G){1B=u}F{1w=u}}f(1w||1B){1o=(1w||1B).G;1p=(1w?E:Z).O}F{f(!2b){2m}}f(4t&&!2b&&1o>1y){2m}f(1w){f(!2b++){27=1o;22=1p}}F{f(1B&&2b){f(!--2b){f(X){f(X[0]&&27>1y){1D.K([X[0],z.15(1y,27),1y,27])}f(X[1]){1D.K([X[1],z.15(27,22),27,22])}f(X[2]){1D.K([X[2],z.15(22,1o),22,1o])}f(X[3]){1D.K([X[3],z.15(1o,1p),1o,1p])}}F{1D.K(z.15(22,1o))}1y=1p;f(!4s){2m}}}F{E.O=Z.O=0;3L 9q("9A 9i 9a 9d 8u")}}f(1o===1p){1p++}}f(4s&&!4t&&X&&X[0]&&z.v>1y){1D.K([X[0],z.15(1y),1y,z.v])}E.O=Z.O=0;q 1D};',62,728,'||||||||||||this|||if||function||||var||||sh|return||||null|length||||str|div|utils|replace|for|left|else|index|_10f|code|className|push|document|config|false|lastIndex||exec|XRegExp||new|true|getParam|part|vN|prototype|_127||RegExp|create|right|_10d|slice||_c4|pos|css|_128|_117|source|_107|class|name|id|bar|indexOf|_f0|strings|_cb|lines|style|_132|_133|window|_e3|createElement|vars|_x|alert|_137|innerHTML|_134|toolbar|params|_138|tab|_130|_e8|lt|appendChild|continue|highlight|_be|lib|_c7|obj|_ef|doc|split|substr|test|width|_4b|value|span|_3c|nbsp|_108|min|_dc|_98|_136|in|_57|_56|_d7|_135|_8|while|_91|_131|script|text|_b2|_6e|http|execute|wnd|captureNames|_5b|_b5|break|call|gt|_b8|href|toString|_75|height|html|attributes|_d5|_d9|_e4|_fe|size|brushes|_88|unindent|SyntaxHighlighter|_40|_b4|_a2|_7a|_51|m1|body|_dd|case|m2|line|_80|matches|_28|_129|_62|_81|_fa|_12a|_5|_ca|getMatches|concat|_b6|trim|print|_7c|_de|_5e|_d3|_a7|offsetMatches|_5c|_76|_6a|_65|_df|_b0|write|_f6|DIV|spaceWidth|_f3|_4a|_ed|_ec|url|regex|_7d|_2|escape|_10c|_113|_106|_7b|_3|_8e|global|title|addFlags|cc|_112|len|fixInputString|_32|_139|Math|undefined|throw|extended|font|htmlScript|printFrame|_22|_fc|_25|instanceof|_17|_9d|_9e|_18|expandSource|_d1|_8c|viewSource|_1a|top|_4f|_da|key|cache|_10|win|getNativeFlags|_19|_50|focus|_122|_121|_11c|_b|Match|_ba|gm|findBrush|esc|eachLine|_4|_6c|values|_73|_12c|_12f|_6b|color|_89|gi|_bd|_bf|_9|HtmlScript|can|discoveredBrushes|xmlBrush|_f|_5d|_82|supply|_7|_c1|func|_59|_c8|_b7|_e|_d|pre|wrap|_ac|com|_a3|originalCode|from|link|gutter||collapsed|clipboardSwf|_37|popup|isNaN|collapse|_blank|_38|_ae|removeChild|String|contentWindow|hide|_24|ruler|swf|_30|_7e|_ad|item|_101|_2f|regexList|merge|measureSpace|_10b|_ea|_2a|_2c|join|_e9|_2e|px|_2b|_4c|_e5|content|r2|margin|_29|quantifier|_49|guid|toolbarCommands|charAt|highlighters|_27|highlighterId|_a6|close|toolbarItemWidth|_2d|_48|toolbarItemHeight|_35|copyToClipboard|Array|userAgent|_90|_84|navigator|_85|_78|_79|_83|_3f|items|_1e|_16|_13|_12|shockwave|flash|clipboardData|_36|object|version|_15|_14|center|alexgorbatchev|align|head|org|2009|_6|executeCommand|commandName|_a|createButton|copyToClipboardConfirmation|message|w3|event|_4e|screen|switch|attachEvent|_5a|_66|www|_61|noBrush|_47|aboutDialog|insertSpaces|copyStyles|500px|0px|_3e|type|scrollbars|help|stylesheet|rel|_6d|debug|DTD|smart|trimFirstAndLastLines|_10a|_f1|light|tabs|processSmartTabs|findMatches|removeNestedMatches|createRuler|show|processTabs|defaults|important|first|padNumber|parseInt|xhtml1|matchesSortCallback|_e0|_e1|decorate|plain|block|_e6|_e7|processMatches|createDisplayLines|_100|_11b|args|matchRecursive|_93|escapeChar|unicode|get|classRight|_109|classLeft|_105|arguments|clipboard|pP|processUrls|multiline|_12d|links|auto|when|character|one|match|_12e|_fb|TypeError|_d4|syntaxhighlighter|addEvent|defaultAdd|_a1|parentNode|_b9|_bb|_c2|brushNotHtmlScript|_a4|_a8|_b1|toArray|getElementsByTagName|tagName|parseParams|regexLib|_a9|brush|_ab|process|_9a|Highlighter|_cf|toBoolean|_c9|_c6|_d0|_cd|_ce|_d2|bloggerMode|stripBrs|catch|SyntaxError|now|Can|more|xhtml|utf|onclick|delimiters|wasn|your|family|sx|than|valueNames|addPlugin|About|try|charset|_26|400|750|find|_21|location|Type|ignoreCase|menubar|resizable|Content|_20|_1f|param|meta|_11f|Brush|apply|_1b|sticky|Geneva|_11d|_1c|equiv|to|syntax|highlighter|JavaScript|expand|none|3em|contains|view|Alex|unbalanced|2004|Copyright|decoration|0099FF|data|320|4em|bottom|75em|May|large|xx|Error|target|03|Gorbatchev|multiLineCComments|aspScriptTags|scriptScriptTags|phpScriptTags|Arial|multiLineSingleQuotedString|subject|The|flag|the|is|string|typeof|Helvetica|sans|000|fff|1em|singleLinePerlComments|singleLineCComments|copy|doubleQuotedString|serif|multiLineDoubleQuotedString|background|singleQuotedString|using|W3C|random|1000000|round|_f5|_f7|_f2|spaces|alt|transitional|open|highlighted|number|highlighter_|250|shCore|onmouseover|toLowerCase|onmouseout|printing|about|_42|_44|500|_43|nogutter|no|sort|on|getComputedStyle|getPropertyValue|opera|getElementById|1999|offsetWidth|_a5|_99|dtd|1000|xmlns|_8a|textarea|70em|_c5|150|lastIndexOf|aliases|addEventListener|_c0|middot|replaceChild|30em|all|load|Xml|absolute|position|444553540000|codebase|96b8|11cf|ae6d|PUBLIC|download|cabs|constructing|another|pub|macromedia|d27cdb6e|clsid|always|wmode|allowScriptAccess|application|_clipboard|transparent|flashVars|msie|classid|menu|DOCTYPE|option|swflash|cab|TR|error|ok|forHtmlScript|setData|getKeywords|printSource|IFRAME|cssText||_3b|_3a|_39|sgi|amp|Transitional|replaceVar|XHTML|embed|movie|src|configured|command|max|_34|_33|EN|flags'.split('|'),0,{}))

// shBrushJScript.js

SyntaxHighlighter.brushes.JScript=function()
{var keywords='break case catch continue '+'default delete do else false  '+'for function if in instanceof '+'new null return super switch '+'this throw true try typeof var while with';this.regexList=[{regex:SyntaxHighlighter.regexLib.singleLineCComments,css:'comments'},{regex:SyntaxHighlighter.regexLib.multiLineCComments,css:'comments'},{regex:SyntaxHighlighter.regexLib.doubleQuotedString,css:'string'},{regex:SyntaxHighlighter.regexLib.singleQuotedString,css:'string'},{regex:/\s*#.*/gm,css:'preprocessor'},{regex:new RegExp(this.getKeywords(keywords),'gm'),css:'keyword'}];this.forHtmlScript(SyntaxHighlighter.regexLib.scriptScriptTags);};SyntaxHighlighter.brushes.JScript.prototype=new SyntaxHighlighter.Highlighter();SyntaxHighlighter.brushes.JScript.aliases=['js','jscript','javascript'];

// shBrushXml.js

SyntaxHighlighter.brushes.Xml=function()
{function process(match,regexInfo)
{var constructor=SyntaxHighlighter.Match,code=match[0],tag=new XRegExp('(&lt;|<)[\\s\\/\\?]*(?<name>[:\\w-\\.]+)','xg').exec(code),result=[];if(match.attributes!=null)
{var attributes,regex=new XRegExp('(?<name> [\\w:\\-\\.]+)'+'\\s*=\\s*'+'(?<value> ".*?"|\'.*?\'|\\w+)','xg');while((attributes=regex.exec(code))!=null)
{result.push(new constructor(attributes.name,match.index+attributes.index,'color1'));result.push(new constructor(attributes.value,match.index+attributes.index+attributes[0].indexOf(attributes.value),'string'));}}
if(tag!=null)
result.push(new constructor(tag.name,match.index+tag[0].indexOf(tag.name),'keyword'));return result;}
this.regexList=[{regex:new XRegExp('(\\&lt;|<)\\!\\[[\\w\\s]*?\\[(.|\\s)*?\\]\\](\\&gt;|>)','gm'),css:'color2'},{regex:new XRegExp('(\\&lt;|<)!--\\s*.*?\\s*--(\\&gt;|>)','gm'),css:'comments'},{regex:new XRegExp('(&lt;|<)[\\s\\/\\?]*(\\w+)(?<attributes>.*?)[\\s\\/\\?]*(&gt;|>)','sg'),func:process}];};SyntaxHighlighter.brushes.Xml.prototype=new SyntaxHighlighter.Highlighter();SyntaxHighlighter.brushes.Xml.aliases=['xml','xhtml','xslt','html','xhtml'];

$(function () {
	var h = 0;
	$("#container .source").each(function () {
		var code = $(this).html().replace(/</g,'&lt;').replace(/>/g,'&gt;');
		var div = $('<div class="code"><pre class="brush:' + ( $(this).is("script") ? 'js' : 'xml' ) + ';">' + code + '</pre></div>');
		$(this).after(div);
	});
	SyntaxHighlighter.all();
});