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

package org.kkonoplev.bali.suiteexec.resource;

import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import org.kkonoplev.bali.suiteexec.resource.ResourcePoolFactory;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

public class ResourceMarkerFactory  implements  ResourcePoolFactory {

	protected static final Logger log = LogManager.getLogger(ResourceMarkerFactory.class);
	
	public Class<? extends TestExecResource> getResourceClass(){
		return EnvResource.class;
	}
	

	public ArrayList<TestExecResource> load(String cmd) {
		
		String[] tags = cmd.split(", ");
		
		ArrayList<TestExecResource> resources = new ArrayList<TestExecResource>();
		for (String tag: tags){
			if (tag.endsWith(EnvResource.ENVRESOURCE)){
				String shortTag = tag.substring(0, tag.length()-EnvResource.ENVRESOURCE.length());
				resources.add(new EnvResource(shortTag, "UAT2"));
			} else
			
			if (tag.endsWith(EnvResource.RESOURCE)){
				String shortTag = tag.substring(0, tag.length()-EnvResource.RESOURCE.length());
				resources.add(new EnvResource(shortTag, EnvResource.ALLENV));
			}
	
		}
		
		return resources;
	}


	@Override
	public String getLoadCmdHelp() {
		return "<resource_name> ResourceEnv|Resource";
	}

}
