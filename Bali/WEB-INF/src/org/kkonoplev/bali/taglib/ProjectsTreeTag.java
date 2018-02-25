/* 
 * Copyright � 2011 Konoplev Kirill
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kkonoplev.bali.taglib;


import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.kkonoplev.bali.project.BaseProject;
import org.kkonoplev.bali.project.structure.TreeNode;
import org.kkonoplev.bali.suiteexec.SuiteMdl;
import org.kkonoplev.bali.taglib.Node.State;




/**
 * Tag used to render the Projects Tests checkbox tree 
 * @author Kirill Konoplev
 */
public class ProjectsTreeTag extends SimpleTagSupport {

   
    private ArrayList<BaseProject> projects;
    private SuiteMdl suiteMdl; 
    String[] testlist;
    String treeId;
    
	public static String getHeaders(){
		return 
		"<link rel=\"shortcut icon\" href=\"/favicon.ico\" /> \n"+
		"<link rel=\"alternate\" type=\"application/rss+xml\" title=\"RSS 2.0\" href=\"http://www.jstree.com/feed\" /> \n"+
		"<script type=\"text/javascript\" src=\"/bali/data/tree/jquery.js\"></script> \n"+
		"<link rel=\"stylesheet\" type=\"text/css\" href=\"/bali/v145/tree/demo.css\" /> \n"+
		"<script type=\"text/javascript\" src=\"/bali/data/tree/jquery.tree.js\"></script> \n"+
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


    /**
     * Display the navigation menus for the application.
     * @throws JspException in case there is an error writing to page.
     */
    public void doTag() throws JspException {
        PageContext page = (PageContext)getJspContext();
        JspWriter out = page.getOut();
       
        StringWriter writer = new StringWriter();
        StringBuffer buf = writer.getBuffer();
        
    	String checked_idstr="";	
        
        if (suiteMdl != null){
        	testlist = suiteMdl.getTestList();
        	checked_idstr=suiteMdl.getTests();
        }
        
    
    	
        try {
        	String onEvent ="onMouseOut=\"setValue('"+getID()+"');\" ";
    		buf.append(getHeaders());
    		buf.append(header());
    		buf.append("<input type=hidden name=\""+treeId+"\" id=\""+treeId+"Values\" value='"+checked_idstr+"' >\n");
    		buf.append("<div "+onEvent+" class=\"demo\" id=\""+treeId+"\">\n");
        	
        	//add subtree for each project
    		buf.append(" <ul> \n");
        	for (BaseProject proj: projects){
        		
        		renderProjectTree(proj, buf);    
        	}
        	buf.append(" </ul> \n");
        	
        	buf.append("</div>");
        	
        	
        
        	out.println(writer);
    	} catch(Exception ex) {
    		ex.printStackTrace();
    	}            
        
        
    }
    
    

    String len(int n){
		String l = "";
		for (int i = 0; i < 3*n; i++)
			l += "  ";
		return l;
	}

	private void renderProjectTree(BaseProject proj, StringBuffer buf) {		
		try {
			if (proj.getRootFolderNode() == null)
				return;
			
			
			buf.append(" <li class='last open'> "+proj.getName()+"\n");
			renderFolderStructure(proj.getRootFolderNode(), buf, proj.getName(), 0);
			
			//buf.append(" </ul> \n");		
		} catch (Exception e){
			e.printStackTrace();
		}
	}


	private void renderFolderStructure(TreeNode folderNode, StringBuffer buf, String projName, int rootline) {
		if (folderNode == null)
			return;
		
	
		buf.append(len(rootline)+" <ul> \n");
		
		for (TreeNode childNode: folderNode.getChilds()) 			
			if (childNode.isFolder()){								
				buf.append(len(rootline+1)+" <li> "+renderNode(childNode, projName));
				renderFolderStructure(childNode, buf, projName, rootline+1);
				buf.append(len(rootline+1)+"</li>\n");				
			} else {								
				buf.append(len(rootline+1)+" <li> "+ renderNode(childNode, projName));
				buf.append(len(rootline+1)+"</li>\n");
			}
			
		buf.append(len(rootline)+" </ul>\n");		

	}
	
	private int threadsForTestSelected(String testinfo, String[] testinfos){
		
		if (testinfos == null)
			return 0;
		
		for (String testinf: testinfos){
			if (testinf.startsWith(testinfo)){
				String[] args = testinf.split("#");
				int threads = Integer.valueOf(args[2]);
				return threads;
			}
		}
		
		return 0;		
	}
	
	public String getNodeTags(TreeNode node, String projName) {
		// TODO Auto-generated method stub
		
		String mark = "";
		
		// if is test
		if (!node.isFolder()){
			String testinfo = projName+"#"+node.getFullName();
			mark += " id='"+testinfo+"' ";	
	
			mark += " name='"+getID()+"' ";
			
			mark += " onclick=\"modifyState('"+testinfo+"', this.className);\" ";
		} else {
			
			mark += " onclick=\"updateAllTreeStates('"+getID()+"');\" ";
			
		}
		
		State state = getNodeState(node, projName);
		
		if (!state.equals(State.UNCHECKED))
			mark += " class='"+state.getName()+"' ";
		
		
		return mark;
	}


	private State getNodeState(TreeNode node, String projName) {
		
		// if folder		
		if (node.isFolder()){
			
			boolean hasChecked = false;
			boolean hasUndertermined = false;
			boolean allChecked = true;
			
			for (TreeNode child: node.getChilds()){
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
			
		} else {
			// test selected
			String testinfo = projName+"#"+node.getFullName();
			
			if (threadsForTestSelected(testinfo, testlist) > 0)
				return State.CHECKED;
			else
				return State.UNCHECKED;
				
		}
	}

	private String renderNode(TreeNode node, String projName) {
		
		//for tests create input box
		String threadinput = "";
		if (!node.isFolder()){
			
			
			
			String testinfo = projName+"#"+node.getFullName();
			
			int threads = threadsForTestSelected(testinfo, testlist);
			
			threadinput = "<input STYLE='background-color: #72A4D2;' id='"+projName+"#"+node.getFullName()+".threads' type=hidden size=2 value="+threads+" >";
		}
		
		
		String nd = "<a "+getNodeTags(node, projName)+" title='"+node.getTitle()+"' href=\"#\"><ins>&nbsp;</ins> "+getNodeHeader(node)+" </a> " 
				+ threadinput +	"\n";
		
		return nd;
	}



	private String getNodeHeader(TreeNode node) {
		String leaf = "<span style='color:"+node.getColor()+"'>"+ node.getLabel()+" </span> ";
		return leaf;
	}

	public ArrayList<BaseProject> getProjects() {
		return projects;
	}


	public void setProjects(ArrayList<BaseProject> projects) {
		this.projects = projects;
	}

	public SuiteMdl getSuiteMdl() {
		return suiteMdl;
	}

	public void setSuiteMdl(SuiteMdl suiteMdl) {
		this.suiteMdl = suiteMdl;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}
	
	
	
	




  
    
}
