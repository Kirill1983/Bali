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

import java.awt.Menu;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocaleSupport;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;





/**
 * Tag used to render the navbar for the Bali application.
 * @author Baldeep Hira, Kirill Konoplev
 */
public class NavbarBaliTag extends SimpleTagSupport {

    public  static final String SELECTED_MENU       = "InfSelectedMenu";
    	
    private static final String NAV_PAD             = "tests";
    private static final String 	NAV_PAD_TESTS   = "testssub";
    
    private static final String NAV_SUITES      	= "suites";
    private static final String 	NAV_FUNC_SUITES = "suitessub";

    private static final String 	   NAV_STATUS       = "status";
    private static final String 	NAV_ACTIVITY_STATUS = "activitystatus";
    private static final String 	 NAV_RESULTS_STATUS = "resultsstatus";
    private static final String 	NAV_EXEC_STATUS     = "executorstatus";
    private static final String NAV_RESOURCES_STATUS    = "resourcesstatus";
    private static final String 	NAV_CRON_STATUS     = "cronstatus";
    private static final String    NAV_TARGET_STATUS    = "targetgridstatus";
    private static final String    NAV_PROJECTS_STATUS  = "projectsstatus";
    
    
    private static final String NAV_SUITE_RUN       = "suiteRun";


    private static final Logger LOG = Logger.getLogger(NavbarBaliTag.class);
    private String _selectedMenu;
    private boolean _hideTabs;


    /**
     * Display the navigation menus for the application.
     * @throws JspException in case there is an error writing to page.
     */
    public void doTag() throws JspException {
        PageContext page = (PageContext)getJspContext();
        JspWriter out = page.getOut();
        MenuAc[] menus = getMenus(_selectedMenu);
       
        try {
            StringWriter writer = new StringWriter();
            StringBuffer buf = writer.getBuffer();
    
            String mainMenu = getSelectedMainMenu(menus, _selectedMenu);
            List<MenuAc> children = null;
            StringBuilder secBar = new StringBuilder();
            String refreshUrl;
                 
            
            //Show the primary & secondary menu, if needed
            if (!_hideTabs) {
            	//first level menu
                buf.append("  <table cellpadding=\"0\" cellspacing=\"0\">\n");
                buf.append("  <tr>\n");
	            for (MenuAc menu : menus) {
	                buf.append("            <td");
	                if (menu.getId().equals(mainMenu)) {
	                    buf.append(" class=\"selectedtab\"");
	                    buf.append(" onmouseover=\"this.className='hovertab';\"");
	                    buf.append(" onmouseout=\"this.className='selectedtab';\"");
	                } else {
	                    buf.append(" class=\"tab\"");
	                    buf.append(" onmouseover=\"this.className='hovertab';\"");
	                    buf.append(" onmouseout=\"this.className='tab';\"");
	                }
	                buf.append(" onclick=\"");
	                buf.append(menu.getAction());
	                buf.append("\"><span>");
	                buf.append(LocaleSupport.getLocalizedMessage(page, menu.getNameKey()));
	                buf.append("</span></td>\n");
	                buf.append("            <td class=\"spacer\"><img src=\"");
	                buf.append("/bali/data/images");
	                buf.append("/spacer.gif\" height=\"1\"/></td>\n");
	                
	                //second level menu
	                children = menu.getChildren();
	                if (menu.getId().equals(mainMenu) && children != null && !children.isEmpty()) {
	                    secBar.append("  <tr>\n");
	                    secBar.append("    <td class=\"InfSecNavBar\">\n");
	                    secBar.append("      <table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n");
	                    secBar.append("      <tr>\n");
	                    secBar.append("        <td>\n");
	                    
                        secBar.append("          <table cellpadding=\"0\" cellspacing=\"0\">\n");
                        secBar.append("          <tr>\n");
	                    for (int j = 0; children != null && j < children.size(); j++) {
	                        MenuAc m = children.get(j);
	         
	                        secBar.append("            <td");
	                        if (m.getId().equals(_selectedMenu)) {
	                            secBar.append(" class=\"sel\"");
	                            refreshUrl = m.getAction().split("'")[1];
	                        } else {
	                        	secBar.append(" class=\"sn\"");
	                            secBar.append(" onmouseover=\"this.className='sel';\"");
	                            secBar.append(" onmouseout=\"this.className='sn';\"");
	                        }
	                        secBar.append(" onclick=\"");
	                        secBar.append(m.getAction());
	                        secBar.append("\"><span>");
	                        secBar.append(LocaleSupport.getLocalizedMessage(page, m.getNameKey()));
	                        secBar.append("</span></td>\n");
	                        secBar.append("            <td class=\"spacer\"><img src=\"");
	                        secBar.append("/bali/data/images");
	                        secBar.append("/spacer.gif\" height=\"1\" width=\"9\"/></td>\n");
	                    }
                        secBar.append("          </tr>\n");
                        secBar.append("          </table>\n");
                        
                        //append the secondary menu row
                        secBar.append("        </td>\n");
                        secBar.append("        <td width=\"100%\">&nbsp;</td>\n");
                        secBar.append("        <td class=\"InfHelpSect\">\n");
                        secBar.append("          <img src=\"");
                        secBar.append("/bali/data/images");
                        secBar.append("/spacer.gif\" height=\"18\" width=\"1\"/>\n");
                        secBar.append("          <a href=\"/bali/doc/html/index.htm\" title=\"");
                        secBar.append(LocaleSupport.getLocalizedMessage(page, "header.help.alt")).append("\">");
                        secBar.append(LocaleSupport.getLocalizedMessage(page, "header.help"));
                        secBar.append("</a>\n");
                        secBar.append("        </td>\n");
                        secBar.append("      </tr>\n");
                        
                        secBar.append("      </table>\n");
                        secBar.append("      </tr>\n");
	                	
	                }
	            }
	            
                buf.append("</tr>\n");
                buf.append("</table>\n");
                buf.append("</td>");
                buf.append("</tr>");
                buf.append("</table>");
                buf.append("</td>");
                buf.append("</tr"); 
                buf.append(secBar);
                
               
                
            }
            
           
            
            out.println(writer);
        } catch(Exception ex) {
            LOG.warn(ex, ex);
        }
    }

    public void setSelectedMenu(String value) {
        _selectedMenu = value;
    }
    public void setHideTabs(boolean value) {
    	_hideTabs = value;
    }

    
    /**
     * Get the list of all the navigation menus in the system.
     * @return an array of navigation menus as {@link Menu}.
     */
    private static MenuAc[] getMenus(String currentMenu) {
    	String prefix = "openUrl('/bali";
        
        //TESTS TAB
        MenuAc pad = new MenuAc(NAV_PAD, "navbar.tests",
                "navbar.tests.alt", prefix+"/form/pad/tests')");
       
        	pad.addChild(new MenuAc(NAV_PAD_TESTS, "navbar.testssub",
        			"navbar.testssub.alt", prefix+"/form/pad/tests')"));

    	
      
        //SUITES
        MenuAc suites = new MenuAc(NAV_SUITES, "navbar.suites",
                "navbar.suites.alt", prefix+"/form/pad/suites')");
        
        suites.addChild(new MenuAc(NAV_FUNC_SUITES, "navbar.suitessub",
    			"navbar.suitessub.alt", prefix+"/form/pad/suites')"));
    
        
        //CONFIGURATION
        MenuAc status = new MenuAc(NAV_STATUS, "navbar.status",
                "navbar.status.alt", prefix+"/form/status/results')");
        
        status.addChild(new MenuAc(NAV_RESULTS_STATUS, "navbar.resultsstatus",
    			"navbar.resultsstatus.alt", prefix+"/form/status/results')"));
                
        status.addChild(new MenuAc(NAV_ACTIVITY_STATUS, "navbar.activitystatus",
    			"navbar.activitystatus.alt", prefix+"/form/status/activity')"));
        
        status.addChild(new MenuAc(NAV_EXEC_STATUS, "navbar.executorsstatus",
    			"navbar.executorsstatus.alt", prefix+"/form/status/executors')"));
       
        
        status.addChild(new MenuAc(NAV_RESOURCES_STATUS, "navbar.resourcesstatus",
    			"navbar.resourcesstatus.alt", prefix+"/form/status/resources')"));
        
        status.addChild(new MenuAc(NAV_CRON_STATUS, "navbar.cronstatus",
    			"navbar.cronstatus.alt", prefix+"/form/status/cron')"));
        
        status.addChild(new MenuAc(NAV_TARGET_STATUS, "navbar.targetgridstatus",
    			"navbar.targetgridstatus.alt", prefix+"/form/status/targetgrid')"));
        
        
        status.addChild(new MenuAc(NAV_PROJECTS_STATUS, "navbar.projectsstatus",
    			"navbar.projectsstatus.alt", prefix+"/form/status/projects')"));
        
   
 
       
        int size=3;
        MenuAc[] menus = new MenuAc[size];
        menus[0] = pad;
        menus[1] = suites;
        // menus[2] = results;
        menus[2] = status;
         
        
        return menus;
       
    }
    
    private String getSelectedMainMenu(MenuAc[] menuBali, String item) {
    	
    	for (MenuAc menu: menuBali){
    		
    		List<MenuAc> children = menu.getChildren();
    		for (MenuAc child: children){
    			if (child.getId().equalsIgnoreCase(item))
    				return menu.getId();
    		}
    		
    	}
    	
    	return item;
    }
   

}