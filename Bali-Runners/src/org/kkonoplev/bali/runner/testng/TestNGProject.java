/* 
 * Copyright � 2015 Kirill Konoplev
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

package org.kkonoplev.bali.runner.testng;

import org.kkonoplev.bali.project.BaseJavaProject;

public class TestNGProject extends BaseJavaProject {
	
	private String binfolder;
	private String libfolder;
	private String prepath;
	
	public TestNGProject(){		
	}
	
	public TestNGProject(String name_){
		this.name = name_;	
	}

	public String getBinfolder() {
		return binfolder;
	}

	public void setBinfolder(String binfolder) {
		this.binfolder = binfolder;
	}

	public String getLibfolder() {
		return libfolder;
	}

	public void setLibfolder(String libfolder) {
		this.libfolder = libfolder;
	}


	public String getPrepath() {
		return prepath;
	}

	public void setPrepath(String prepath) {
		this.prepath = prepath;
	}


}
