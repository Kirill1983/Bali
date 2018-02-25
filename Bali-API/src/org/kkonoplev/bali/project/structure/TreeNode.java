/* 
 * Copyright � 2011 Kirill Konoplev
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

package org.kkonoplev.bali.project.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kkonoplev.bali.runner.RunnableItem;

public class TreeNode implements Serializable {
	
	protected String label;
	protected String name;
	protected String color = "black";
	protected String title = "";
	protected String path = "";
	
	protected TreeNode dependsOnNode = null;
	protected String dependsOnNodeClass = "";
	protected String lineViewLabel = "";

	protected transient RunnableItem runnableItem;

	protected List<String> tags = new ArrayList<String>();
	protected ArrayList<TreeNode> childs = new ArrayList<TreeNode>();
	
	private static final Logger log = Logger.getLogger(TreeNode.class);
	
	public TreeNode(){
	}

	public TreeNode(String name_){
		name = name_;		
		label = name_;
	}
	
	public TreeNode(String name, String label){
		this.name = name;		
		this.label = label;
	}
	
	public TreeNode(String name, String label, String title){
		this.name = name;		
		this.label = label;
		this.title = title;
	}
	
	
	public void setLabel(String label) {
		this.label = label;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFolder(){
		
		if (childs.size() > 0)
			return true;
		
		return false;
	}
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName(){
		return name;
	}
	
	public String getLabel(){
		return label;
	}

	
	public String getFullName(){
		return path;
	}

	public ArrayList<TreeNode> getChilds() {
		return childs;
	}

	public void setChilds(ArrayList<TreeNode> childs) {
		this.childs = childs;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public RunnableItem getRunnableItem() {
		return runnableItem;
	}

	public void setRunnableItem(RunnableItem runnableItem) {
		this.runnableItem = runnableItem;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public TreeNode getNodeByName(String leafName) {
			
		
		for (TreeNode child: this.childs){
			if (leafName.equalsIgnoreCase(child.getName()))
				return child;					
		}
		
		log.error("Unable find leaf "+leafName+" for node "+this.name);
		return new TreeNode();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLineViewLabel() {
		return lineViewLabel;
	}

	public void setLineViewLabel(String lineViewLabel) {
		this.lineViewLabel = lineViewLabel;
	}

	
	public TreeNode getDependsOnNode() {
		return dependsOnNode;
	}

	public void setDependsOnNode(TreeNode dependsOnNode) {
		this.dependsOnNode = dependsOnNode;
	}
	
	public String getDependsOnNodeClass() {
		return dependsOnNodeClass;
	}

	public void setDependsOnNodeClass(String dependsOnNodeClass) {
		this.dependsOnNodeClass = dependsOnNodeClass;
	}

	public boolean isLeaf() {
		
		return childs.size() == 0 ? true: false;
	}

	public void addAllTags(List<String> allTags) {
		
		for (String tag: tags){
			if (!allTags.contains(tag))
				allTags.add(tag);
		}
		
		for (TreeNode childNode: childs){
			childNode.addAllTags(allTags);
		}
		
	}
	
	public String toString(){
		return name+":"+childs.size();
	}

	
	
}
