
var $=function(_4){
return document.getElementById(_4);
};

function openUrl(_5){

if(_5){
document.location.href=_5;
}

}

var pnHelp=new function(){
this.helpContext="Bali_User_Guide";
this.helpTopic=null;
this.prevHelpTopic=null;
this.getHelpUrl=function(){
var url="/bali/help.htm";
if(this.helpTopic&&this.helpTopic.length>0){
url+="&topic="+this.helpTopic;
}
return url;
};

this.show=function(){
var w=740;
var h=540;
var pos=pnCommon.getPopupLocation(w,h);
var _3b=pos+",width="+w+",height="+h+",location=0,menubar=0";
_3b+=",resizable=1,scrollbars=1,status=0,titlebar=0,toolbar=0";
var _3c=window.open(this.getHelpUrl(),"HelpWindow",_3b);
_3c.focus();
};
this.setTopic=function(_3d){
if(_3d&&_3d.length>0){
this.prevHelpTopic=this.helpTopic;
this.helpTopic=_3d;
}else{
this.prevHelpTopic=this.helpTopic;
this.helpTopic=null;
}
};
this.resetHelp=function(){
if(this.prevHelpTopic&&this.prevHelpTopic.length>0){
this.helpTopic=this.prevHelpTopic;
this.prevHelpTopic=null;
}else{
this.helpTopic=null;
}
};
};





var pnBtn=new function(){

this.state=null;

this.resetState=function(){
this.state=null;
};

this.preSubmit=function(_132,_133,_134){
var _135=_134?"disabledbtnSmall":"disabledbtn";
this.state="submit";
this.setStyle(_132,_135);
this.showAnimation(_133);
};

this.postSubmit=function(_136,_137){
var _138=_137?"btnSmall":"btn";
this.resetState();
this.setStyle(_136,_138);
this.showAnimation(false);
};

this.active=function(){
return (this.state==null||this.state!="submit");
};

this.setClass=function(elem,_13a){
if(this.active()&&elem&&_13a){
elem.className=_13a;
}
};

this.showAnimation=function(show){

var e=$("pnAniGif");

if(show){
if(!e){
var _13d=$("InfHtmlBody");
if(_13d){
e=document.createElement("div");
e.setAttribute("id","pnAniGif");
e.innerHTML="<img src=\""+pnvars.imagePath+"/Loading.gif\"/> <span style=\"padding-left:15px;\">Loading...</span>";
e.className="InfAniGif";
_13d.appendChild(e);
}
}

if(e){
e.style.display="";
}
}else{
if(e){
e.style.display="none";
}
}
};

this.setStyle=function(_13e,_13f){
if(_13e&&_13f){
for(var i=0;i<_13e.length;i++){
var elem=$(_13e[i]+"Td");
if(elem){
elem.className=_13f;
}
}
}
};
};

function pnButton(id,name,_144,_145,_146,_147){
this.id=id;
this.name=name;
this.title=_144;
this.action=_145;
this.small=_146;
this.disabled=_147;
}

pnButton.prototype.getId=function(){
return this.id;
};
pnButton.prototype.getName=function(){
return this.name;
};
pnButton.prototype.getTitle=function(){
return (this.title?this.title:this.name);
};
pnButton.prototype.getAction=function(){
return this.action;
};
pnButton.prototype.isSmall=function(){
return this.small;
};
pnButton.prototype.isDisabled=function(){
return this.disabled;
};
pnButton.prototype.setId=function(id){
this.id=id;
};
pnButton.prototype.setName=function(name){
this.name=name;
};
pnButton.prototype.setTitle=function(_14a){
this.title=_14a;
};
pnButton.prototype.setAction=function(_14b){
this.action=_14b;
};
pnButton.prototype.setSmall=function(_14c){
this.small=_14c;
};
pnButton.prototype.setDisabled=function(_14d){
this.disabled=_14d;
};
pnButton.prototype.refresh=function(){
var td=$(this.id+"Td");
if(!td){
return;
}
if(this.disabled){
td.className="disabledbtn";
td.onmouseover="";
td.onmouseout="";
td.onclick="";
td.innerHTML=this.name;
}else{
td.className="btn";
td.onmouseover=function(){
pnBtn.setClass(this,"hoverbtn");
};

td.onmouseout=function(){
pnBtn.setClass(this,"btn");
};

td.onclick=function(){
var btn=pnButtons.get(this.id.substring(0,this.id.length-2));
if(btn){
eval(btn.getAction());
}
};
td.innerHTML="<span>"+this.name+"</span>";
}
};

pnButton.prototype.print=function(){
var s="pnButton DETAILS";
s+="\n  id    = "+this.id;
s+="\n  name  = "+this.name;
s+="\n  title   = "+this.title;
s+="\n  action  = "+this.action;
s+="\n  small   = "+this.small;
s+="\n  disabled  = "+this.disabled;
alert(s);
};
pnButton.prototype.draw=function(){
var html="<td id=\""+this.id+"Td\" class=\"";
if(this.disabled){
html+="disabledbtn";
html+="\" title=\""+this.title+"\">"+this.name;
}else{
html+="btn";
html+="\" onmouseover=\"pnBtn.setClass(this,'hoverbtn')\" onmouseout=\"pnBtn.setClass(this,'";
html+="btn')\" title=\""+this.title+"\" onclick=\""+this.action+"\">";
html+="<span>"+this.name+"</span>";
}
html+="</td>";
return html;
};


var pnButtons=new function(){

this.array=new Array();

this.add=function(btn){
if(btn){
this.array.push(btn);
}
};

this.get=function(id){
if(this.array){
for(var i=0;i<this.array.length;i++){
if(this.array[i].id==id){
return this.array[i];
}
}
}
return null;
};

this.draw=function(btns){
if(!btns||btns.length==0){
return "";
}
var html="<table cellpadding=0 cellspacing=0 class='InfButton'><tr>";
for(var i=0;i<btns.length;i++){
if(i!=0){
html+="<td class=spacer>&nbsp;</td>";
}
html+=btns[i].draw();
}
html+="</tr></table>";
return html;
};
};
