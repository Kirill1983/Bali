package org.kkonoplev.bali.treelogger;

import java.io.File;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.common.utils.Util;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.treelogger.LogNode.State;

public class TreeLogHTMLBuilder {
	
	private static final Logger log = Logger.getLogger(TreeLogHTMLBuilder.class);
	private TreeLog treeLog;
	private String id = "";
	private String treeId = "treeId";
	private boolean expand = false;
	private String jquerysrc = "tree/jquery.js";
	private String jquerytreesrc = "tree/jquery.tree.js";
	private String css = "tree/demo.css";
		
	public TreeLogHTMLBuilder(TreeLog treeLog) {
		super();
		this.treeLog = treeLog;
	}
	
	
	public void setJquerysrc(String jquerysrc) {
		this.jquerysrc = jquerysrc;
	}


	public void setJquerytreesrc(String jquerytreesrc) {
		this.jquerytreesrc = jquerytreesrc;
	}


	public void saveFile(String filename){
		
	      try {
	            String text = buildHTML();
	            Util.saveTextToFile(text, new File(filename), false);
	         
	        } catch (Exception e) {
	        	log.warn(e, e);
	            log.warn("TreeLog::saveFile");
	        }

	}
	
	public String getHeaders(){
		return 
		"<link rel=\"alternate\" type=\"application/rss+xml\" title=\"RSS 2.0\" href=\"http://www.jstree.com/feed\" /> \n"+
		"<link rel=\"stylesheet\" type=\"text/css\" href=\""+css+"\" /> \n"+
		(jquerysrc.equals("") ? "" : "<script type=\"text/javascript\" src=\""+jquerysrc+"\"></script> \n")+
		(jquerytreesrc.equals("") ? "" : "<script type=\"text/javascript\" src=\""+jquerytreesrc+"\"></script> \n")+
		"<script type=\"text/javascript\">SyntaxHighlighter.config.clipboardSwf = 'http://static.jstree.com/v.0.9.9a/clipboard.swf';\n"+
		
		" function setValue(id){ \n"+
		"  	tags=document.body.getElementsByTagName('a'); \n"+
		"   launch = \"\"; \n"+
		"  	for(var j=0; j<tags.length; j++) \n"+
		" 		if ((tags[j].className == 'checked') || (tags[j].className == 'clicked checked')) \n"+
		"	 		if ((tags[j].id != '') && (tags[j].name == id)) \n" +
		"				if (document.getElementById('loadmode').checked == true) \n"+
		" 					launch += tags[j].id+'#'+document.getElementById(tags[j].id+'.threads').value+\",\"; \n" +
		"				else \n" +
		" 					launch += tags[j].id+'#1,'; \n" +
		"	\n"+									
		" 	document.getElementById(id).value = launch; \n"+
		"   return false; "+
		" } \n\n"+
		
		" function modifyState(testid, classname){ " +
		"	return false;\n" +
		"   if (document.getElementById('loadmode').checked != true)\n" +
		"		return false; \n" +
		"   				  \n"+	
		"   				  \n"+	
		
		"   if (classname != 'clicked checked' || classname != 'checked')	" +
		"		document.getElementById(testid+'.threads').type='text'; \n" +
		"   else \n"+
		"       document.getElementById(testid+'.threads').type='hidden'; \n" +
		"    " +
		"	return false;\n"+
		" } \n\n"+
		
		" function updateAllTreeStates(id){ \n" +
		"	return false;"+
		"   if (document.getElementById('loadmode').checked != true)\n" +
		"		return false; \n" +
		"   				  \n"+		
		"  	tags=document.body.getElementsByTagName('a'); \n"+
		"   launch = \"\"; \n"+
		"  	for(var j=0; j<tags.length; j++) \n"+
		"		if ((tags[j].id != '') && (tags[j].name == id)) \n"+
		" 			if ((tags[j].className == 'checked') || (tags[j].className == 'clicked checked')) \n"+
		" 				document.getElementById(tags[j].id+'.threads').type='text'; \n" +
		"			else \n" +
		" 				document.getElementById(tags[j].id+'.threads').type='hidden'; \n" +
	
		"   return false; \n "+		
		" } \n"+
		" </script>";
		
		
	}
	
	String header(){
		String page = "";
		
		page +=
		"<script type=\"text/javascript\" class=\"source\"> \n"+
		"$(function () { \n"+
		"	$(\"#"+treeId+"\").tree({ \n"+
		"		ui : { \n"+
		"            theme_name : \"checkbox\" \n"+
		"		}, \n"+
		"       plugins : {  \n"+
		"	 	  	 checkbox : { } \n"+
		"		} \n"+
		"	}); \n"+
		"}); \n"+
		"</script> \n";
		
		return page;
	
	}
	
	public String getID(){
		return treeId+"Values";		
	}
	
	
	public String buildHTML() throws Exception {
	
	    StringBuffer buf = new StringBuffer();

	    String onEvent ="onMouseOut=\"setValue('"+getID()+"');\" ";
		buf.append(getHeaders());
		buf.append(header());
		buf.append("<input type=hidden name=\""+treeId+"\" id=\""+treeId+"Values\" value='' >\n");
		buf.append("<div "+onEvent+" class=\"demo\" id=\""+treeId+"\">\n");
	    	
	    //add subtree for each project
		buf.append(" <ul> \n");
	    		
	    renderLogTree(treeLog, buf);    
	    buf.append(" </ul> \n");
	    	
	    buf.append("</div>");
         
	    return buf.toString();
	    
	}
	
	
	
	String len(int n){
		String l = "";
		for (int i = 0; i < 3*n; i++)
			l += "  ";
		return l;
	}
	
	private void renderLogTree(TreeLog treeLog, StringBuffer buf) {		
		try {
			
			if (treeLog == null)
				return;
			
			
			buf.append(" <li class='last open'> "+treeLog.getName()+"\n");
			renderTreeLogStructure(treeLog.getRoot(), buf, treeLog.getName(), 0);
			
			//buf.append(" </ul> \n");		
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void renderTreeLogStructure(LogNode logNode, StringBuffer buf, String projName, int rootline) {
		if (logNode == null)
			return;
		
	
		buf.append(len(rootline)+" <ul> \n");
		
		for (LogNode childNode: logNode.getChilds()) 			
			if (childNode.isFolder()){		
				String cl = ""; 
				if (expand && (getNodeState(childNode, projName) != State.UNCHECKED))
					cl = "class='last open'";
				buf.append(len(rootline+1)+" <li "+cl+"> "+renderNode(childNode, projName));
				renderTreeLogStructure(childNode, buf, projName, rootline+1);
				buf.append(len(rootline+1)+"</li>\n");				
			} else {								
				buf.append(len(rootline+1)+" <li> "+ renderNode(childNode, projName));
				buf.append(len(rootline+1)+"</li>\n");
			}
			
		buf.append(len(rootline)+" </ul>\n");		
	
	}
	
	
	public String getNodeTags(LogNode node, String projName) {
		
		String mark = "";	
		State state = getNodeState(node, projName);

		if (!state.equals(State.UNCHECKED))
			mark += " class='"+state.getName()+"' ";
		
		return mark;
		
	}
	
	
	private State getNodeState(LogNode node, String projName) {
		
		// if folder		
		if (node.isFolder()){
					
			boolean hasChecked = false;
			boolean hasUndertermined = false;
			boolean allChecked = true;
					
			for (LogNode child: node.getChilds()){
				
				State childstate = getNodeState(child, projName);
			
				if (childstate == State.UNDERTERMINED){
						hasUndertermined = true;
				}
						
				if (childstate == State.CHECKED)
						hasChecked = true;
					else
						allChecked = false;
			}
			
			if (hasChecked || hasUndertermined){
						
				if (allChecked)
					return State.CHECKED;
				else 
					return State.UNDERTERMINED;
						
			} else 
					return State.UNCHECKED;
			
					
		}	else { 	
		
			if (id.equals("")) {
				if (node.isFailed())
					return State.CHECKED;
				else
					return State.UNCHECKED;
			} else {
				if ((node.getId()+"").equals(id))
					return State.CHECKED;
				else
					return State.UNCHECKED;
			}
			
		}

		
	}
	
	private String renderNode(LogNode node, String projName) {
		
		//for tests create input box
		String threadinput = "";
		if (!node.isFolder()){
			int threads = 1;
			threadinput = "<input STYLE='background-color: #72A4D2;' id='"+33+".threads' type=hidden size=2 value="+threads+" >";
		}
		String title = "log title";
		
		String nd = "<a "+getNodeTags(node, projName)+" title='"+title+"' href=\"#\"><ins>&nbsp;</ins> "+getNodeHeader(node)+" </a> " 
				+ threadinput +	"\n";
		
		return nd;
	}
	
	
	
	private String getNodeHeader(LogNode node) {
		String color = "black";
		if (node.isFailed())
			color = "red";
		
		String leaf = "<span style='color:"+color+"'>"+ node.getText()+" </span> ";
		return leaf;
	}


	public String getCss() {
		return css;
	}


	public void setCss(String css) {
		this.css = css;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public boolean isExpand() {
		return expand;
	}


	public void setExpand(boolean expand) {
		this.expand = expand;
	}
	
	
	
}
