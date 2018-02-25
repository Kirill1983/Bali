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

package org.kkonoplev.bali.project;

import org.kkonoplev.bali.common.utils.MailUtil;

public class SendMail {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		String DEFAULT_HOST = "172.23.10.20";
		String html = "hello world!";
		MailUtil.sendHTML("balitest@trs-it.ru", "kirill.konoplev@trs-it.ru", "", "Bali Suite Execution Results ", "Hello, world!", DEFAULT_HOST, null, null, true);


	}

}
