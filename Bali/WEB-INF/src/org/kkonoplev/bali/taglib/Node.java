/* 
 * Copyright © 2011 Kirill Konoplev
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
 * 
 */

package org.kkonoplev.bali.taglib;

import java.util.ArrayList;

public class Node {
	
	State state = State.UNCHECKED;
	String name="";
	String id="";
	String value="";
	ArrayList<Node> children = null;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Node> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<Node> children) {
		this.children = children;
	}

	public enum State {
		CHECKED("checked"),
		UNDERTERMINED("undetermined"),
		UNCHECKED("");
		
		String name;
		
		State(String name_){
			name = name_;
		}
		
		public String getName(){
			return name;
		}
	};
	
	public Node(){	
	}
	
	public void setState(State st){
		state = st;
	}
	
	public State getState(){
		return state;
	}
	
	boolean isParent(){
		return (children != null);		
	}

	public String getTags() {
		// TODO Auto-generated method stub
		String mark = "";
		if (id.length() > 0)
			mark += " id='"+id+"' ";
		
		if (name.length() > 0)
			mark += " name='"+name+"' ";
		
		if (!state.equals(State.UNCHECKED))
			mark += " class='"+state.getName()+"' ";
		
		return mark;
	}

	public String getHead() {
		// TODO Auto-generated method stub
		return "<a "+getTags()+" href=\"#\"><ins>&nbsp;</ins> "+(isParent() ? value.toUpperCase(): value)+" </a> \n";
	}

}
