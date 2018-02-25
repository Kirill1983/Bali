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

package org.kkonoplev.bali.common.logger;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

public class LogHTMLLayout extends BaliHTMLLayout {
    protected String getHeaderTable() {
        StringBuffer sbuf = new StringBuffer();

        sbuf.append("<script type=\"text/javascript\" language=\"JavaScript\" charset=\"UTF-8\">" + Layout.LINE_SEP);
        sbuf.append("var COOKIE_NAME = 'aqua_log_level';" + Layout.LINE_SEP);


        sbuf.append("</script>" + Layout.LINE_SEP);
        sbuf.append(super.getHeaderTable());
        return sbuf.toString();
    }

    protected String getFormattedThreadHeader() {
        //no need this column.
        return "";
    }

    protected String getFormattedThread(LoggingEvent event) {
        // no need this column.
        return "";
    }
}