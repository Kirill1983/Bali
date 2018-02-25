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

package org.kkonoplev.bali.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.kkonoplev.bali.suiteexec.annotation.UseResources;
import org.kkonoplev.bali.suiteexec.resource.TestExecResource;

/** @author kkono */
public class Util {
    private static final Logger log = LogManager.getLogger(Util.class);

    private static Map<String, String> xmlEscapeSymbols = new LinkedHashMap<String,String>();

    static {
        xmlEscapeSymbols.put("&", "&amp;");
        xmlEscapeSymbols.put("<", "&lt;");
        xmlEscapeSymbols.put(">", "&gt;");
        xmlEscapeSymbols.put("\"", "&quot;");
    }

    /**
     *
     *
     * @param is 
     * @return
     */
    public static Document getXmlDocument(InputSource is) throws ParserConfigurationException, IOException, SAXException {
        if (is == null) {
            throw new IllegalArgumentException("null!");
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // 
        factory.setValidating(true);

        // 
        factory.setIgnoringElementContentWhitespace(true);

        // 
        factory.setIgnoringComments(true);

        DocumentBuilder documentBuilder = factory.newDocumentBuilder();

        return documentBuilder.parse(is);
    }

    /**
     *
     * @param file null.
     * @return
     */
    public static Document getXmlDocument(File file) throws IOException, ParserConfigurationException, SAXException {
        if (file == null) {
            throw new IllegalArgumentException(" null!");
        }

        String uri = "file:" + file.getAbsolutePath();
        if (File.separatorChar == '\\') {
            uri = uri.replace('\\', '/');
        }

        InputSource in = new InputSource(uri);

        return getXmlDocument(in);
    }

    /**
     
     *
     * @param is
     * @return .
     */
    public static Document getXmlDocument(InputStream is) throws IOException, ParserConfigurationException, SAXException {
        if (is == null) {
            throw new IllegalArgumentException(" null!");
        }

        InputSource in = new InputSource(is);
        return getXmlDocument(in);
    }

    /**
     * 
     * @param uri U
     * @return 
     */
    public static Document getXmlDocument(String uri) throws IOException, ParserConfigurationException, SAXException {
        if (uri == null) {
            throw new IllegalArgumentException("URI null!");
        }

        InputSource in = new InputSource(uri);
        return getXmlDocument(in);
    }

    /**
     * Saving text in specified encoding to file. If file isn't exists it will be created.
     *
     * @param text    text string, if null then empty file will be created.
     * @param file    file, must not be null.
     * @param charset charset, if null then UTF-8 will be used.
     * @param append  if true, then bytes will be written to the end of the file rather than the
     *                beginning.
     * @throws IllegalArgumentException if argument 'file' is null.
     * @throws IOException if some problems with 'file'
     */
    public static void saveTextToFile(String text, File file, String charset, boolean append) throws IllegalArgumentException, IOException {
    	if (text == null) {
            text = "";
        }
        if (file == null) {
            throw new IllegalArgumentException("Argument 'file' must not be null!");
        }
        if (charset == null) {
            charset = "UTF-8";
        }

        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file, append);
            fos.write(text.getBytes(charset));
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * Saving text in default encoding (UTF-8) to file. If file isn't exists it will be created.
     *
     * @param text   text string.
     * @param file   file.
     * @param append if true, then bytes will be written to the end of the file rather than the
     *               beginning.
     * @throws IOException if some problem with 'file'
     */
    public static void saveTextToFile(String text, File file, boolean append) throws IOException {
        saveTextToFile(text, file, "UTF-8", append);
    }

    public static void copyfile(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage() + " in the specified directory." + ex);
        } catch (IOException e) {
            log.error(e.getMessage() + e);
        }
    }

    /**
     * Prepare string to save in xml.
     *
     * @param string String to prepare
     * @return result String
     */
    public static String prepareString4Xml(String string) {
        if (null == string) {
            return "";
        }

        String preparedString = string;
        for (Map.Entry<String, String> escape: xmlEscapeSymbols.entrySet()) {
            preparedString = preparedString.replaceAll(escape.getKey(), escape.getValue());
        }

        return preparedString;
    }

    /**
     * Matches regexp reg in string cells and returns results
     *
     * @param reg  Regexp
     * @param data Matched string
     * @return Matches
     */
    public static List<String> getMatches(String reg, String data) {
        List<String> answers = new ArrayList<String>();
        Matcher matcher = Pattern.compile(reg).matcher(data);
        while (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++)
                answers.add(matcher.group(i));
        }
        return answers;
    }

    public static void addPath2ClassPath(String path) throws Exception {
	  File f = new File(path);
	  URL pathUrl = f.toURI().toURL();
	  URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
	  Class urlClass = URLClassLoader.class;
	  Method method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
	  method.setAccessible(true);
	  method.invoke(urlClassLoader, new Object[]{pathUrl});
	}
    
    public static void fillResourcesList(Class testclass, ArrayList<Class<? extends TestExecResource>> resources) {
		
		if (testclass == null)
			return;
		
		UseResources useResources = (UseResources) testclass.getAnnotation(UseResources.class);
		if (useResources != null){
			for (int i = 0; i < useResources.list().length; i++){
				resources.add(useResources.list()[i]);				
			}
		}
		
		fillResourcesList(testclass.getSuperclass(), resources);	
		
	}
}