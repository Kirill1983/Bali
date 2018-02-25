/* 
 * Copyright © 2011 Konoplev Kirill
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

package org.kkonoplev.bali.init;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.apache.log4j.PropertyConfigurator;


public class BaliLog4j {

    public static void init() {
        try {
            StringBuilder str = new StringBuilder();
            str.append(System.getProperty("catalina.base"));
            str.append(File.separator).append("conf");
            str.append(File.separator).append("log4j.properties");
            File file = new File(str.toString());
            Properties props = new Properties();
            props.load(new FileInputStream(file.getCanonicalPath()));
            
            //load lo4j configuration properties
            PropertyConfigurator.configure(props);
            System.out.println("custom log4j initialization complete");
        } catch (Exception ex) {
            System.out.println("error initializing log4j");
            ex.printStackTrace();
        }
    }
}