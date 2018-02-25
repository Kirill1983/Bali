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

package org.kkonoplev.bali.common.logger;

import java.util.Date;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

public class BaliHTMLLayout extends Layout {
    public static String NO_ESC_PREFIX = "DO_NOT_ESCAPE::>";

    /**  */
    protected final int BUF_SIZE = 256;

    /**. */
    protected final int MAX_CAPACITY = 1024;

    /**
     * 
     * 
     */
    private StringBuffer sbuf = new StringBuffer(BUF_SIZE);

    /**  **/
    private boolean isAquaRunning = null != System.getProperty("aqua.server.url");

    /**  **/
    private long errorCounter = 1;

    public String getContentType() {
        return "text/html";
    }

    public void activateOptions() {
    }

    public String getHeader() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" + Layout.LINE_SEP);
        sbuf.append("<html>" + Layout.LINE_SEP);
        sbuf.append("<head>" + Layout.LINE_SEP);
        sbuf.append("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">" + Layout.LINE_SEP);
        if (isAquaRunning) {
            sbuf.append("<script type=\"text/javascript\" language=\"JavaScript\" charset=\"UTF-8\" src=\"/aqua/js/common.js\"></script>" + Layout.LINE_SEP);
            sbuf.append("<script type=\"text/javascript\" language=\"JavaScript\" charset=\"UTF-8\" src=\"/aqua/js/prototype.js\"></script>" + Layout.LINE_SEP);
            sbuf.append("<script type=\"text/javascript\" language=\"JavaScript\" charset=\"UTF-8\" src=\"/aqua/js/cookie.js\"></script>" + Layout.LINE_SEP);
            sbuf.append("<script type=\"text/javascript\" language=\"JavaScript\" charset=\"UTF-8\" src=\"/aqua/js/report.js\"></script>" + Layout.LINE_SEP);
            sbuf.append("<link rel=\"stylesheet\" charset=\"UTF-8\" href=\"/aqua/css/main.css\">" + Layout.LINE_SEP);
            sbuf.append("<link rel=\"stylesheet\" charset=\"UTF-8\" href=\"/aqua/css/report.css\">" + Layout.LINE_SEP);
            sbuf.append("<link rel=\"shortcut icon\" href=\"/aqua/img/favicon.ico\">" + Layout.LINE_SEP);
        } else {
            sbuf.append("<style type=\"text/css\">" + Layout.LINE_SEP);
            sbuf.append("<!--" + Layout.LINE_SEP);
            sbuf.append("body {font-family:arial,sans-serif;font-size:13px;}" + Layout.LINE_SEP);
            sbuf.append("table.details {border-bottom:#888888 1px solid;border-right:#888888 1px solid;}" + Layout.LINE_SEP);
            sbuf.append("table.details th {border-top:#888888 1px solid;border-left:#888888 1px solid;background:#E0E0FF;text-align:left;}" + Layout.LINE_SEP);
            sbuf.append("table.details td {border-top:#888888 1px solid;border-left:#888888 1px solid;}" + Layout.LINE_SEP);
            sbuf.append(".trace {color:#000000;}" + Layout.LINE_SEP);
            sbuf.append(".debug {color:#000000;}" + Layout.LINE_SEP);
            sbuf.append(".info {color:#000000;}" + Layout.LINE_SEP);
            sbuf.append(".warn {color:#809CE4;}" + Layout.LINE_SEP);
            sbuf.append(".error {color:#A800DE;}" + Layout.LINE_SEP);
            sbuf.append(".fatal {color:#FF0000;}" + Layout.LINE_SEP);
            sbuf.append("a {color:#5656A3;text-decoration:underline;}" + Layout.LINE_SEP);
            sbuf.append("a:hover {color:#5656A3;text-decoration:none;}" + Layout.LINE_SEP);
            sbuf.append("a:visited {color:#A3568E;}" + Layout.LINE_SEP);
            sbuf.append("a:active {color:#5656A3;}" + Layout.LINE_SEP);
            sbuf.append("-->" + Layout.LINE_SEP);
            sbuf.append("</style>" + Layout.LINE_SEP);
        }
        sbuf.append("</head>" + Layout.LINE_SEP);
        sbuf.append("<body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\" onload=\"init();\">" + Layout.LINE_SEP);
        sbuf.append("<form name=\"levelControl\">" + Layout.LINE_SEP);
        sbuf.append("<table border=\"0\">" + Layout.LINE_SEP);
        sbuf.append("<tr><td class=\"trace\">" + Layout.LINE_SEP);
        
        sbuf.append("<input type=\"checkbox\" name=\"trace\" id=\"trace\" value=\"32\" onclick=\"toggleLevel('trace');\"> <label for=\"trace\"><img src=\"/bali/data/images/report/trace.gif\" width=\"12\" height=\"12\"> trace</label>" + Layout.LINE_SEP);
        sbuf.append("</td>" + Layout.LINE_SEP);
        sbuf.append("<td rowspan=\"3\">&nbsp;</td><td class=\"warn\">" + Layout.LINE_SEP);
        sbuf.append("<input type=\"checkbox\" name=\"warn\" id=\"warn\" value=\"4\" onclick=\"toggleLevel('warn');\"> <label for=\"warn\"><img src=\"/bali/data/images/report/warn.gif\" width=\"12\" height=\"12\"> warn</label>" + Layout.LINE_SEP);
        sbuf.append("</td><td rowspan=\"3\">" + Layout.LINE_SEP);
        sbuf.append("<span style=\"margin-left:20px;\">" + Layout.LINE_SEP);
        sbuf.append("Logging level Log4j" + Layout.LINE_SEP);
        sbuf.append("<select name=\"selector\" onchange=\"setSelectedLevels(this.value.split(COOKIE_DELIMITER));\">" + Layout.LINE_SEP);
        sbuf.append("<option value=\"\"></option>" + Layout.LINE_SEP);
        sbuf.append("<option value=\"trace|debug|info|warn|error|fatal\" class=\"trace\">trace</option>" + Layout.LINE_SEP);
        sbuf.append("<option value=\"debug|info|warn|error|fatal\" class=\"debug\">debug</option>" + Layout.LINE_SEP);
        sbuf.append("<option value=\"info|warn|error|fatal\" class=\"info\">info</option>" + Layout.LINE_SEP);
        sbuf.append("<option value=\"warn|error|fatal\" class=\"warn\">warn</option>" + Layout.LINE_SEP);
        sbuf.append("<option value=\"error|fatal\" class=\"error\">error</option>" + Layout.LINE_SEP);
        sbuf.append("<option value=\"fatal\" class=\"fatal\">fatal</option>" + Layout.LINE_SEP);
        sbuf.append("</select>" + Layout.LINE_SEP);
        sbuf.append("</span>" + Layout.LINE_SEP);
        sbuf.append("</td></tr><tr><td class=\"debug\">" + Layout.LINE_SEP);
        sbuf.append("<input type=\"checkbox\" name=\"debug\" id=\"debug\" value=\"16\" onclick=\"toggleLevel('debug');\"> <label for=\"debug\"><img src=\"/bali/data/images/report/debug.gif\" width=\"12\" height=\"12\"> debug</label>" + Layout.LINE_SEP);
        sbuf.append("</td><td class=\"error\">" + Layout.LINE_SEP);
        sbuf.append("<input type=\"checkbox\" name=\"error\" id=\"error\" value=\"2\" onclick=\"toggleLevel('error');\"> <label for=\"error\"><img src=\"/bali/data/images/report/error.gif\" width=\"12\" height=\"12\"> error</label>" + Layout.LINE_SEP);
        sbuf.append("</td></tr><tr><td class=\"info\">" + Layout.LINE_SEP);
        sbuf.append("<input type=\"checkbox\" name=\"info\" id=\"info\" value=\"8\" onclick=\"toggleLevel('info');\"> <label for=\"info\"><img src=\"/bali/data/images/report/info.gif\" width=\"12\" height=\"12\"> info</label>" + Layout.LINE_SEP);
        sbuf.append("</td><td class=\"fatal\">" + Layout.LINE_SEP);
        sbuf.append("<input type=\"checkbox\" name=\"fatal\" id=\"fatal\" value=\"1\" onclick=\"toggleLevel('fatal')\"> <label for=\"fatal\"><img src=\"/bali/data/images/report/fatal.gif\" width=\"12\" height=\"12\"> fatal</label>" + Layout.LINE_SEP);
        sbuf.append("</td></tr>" + Layout.LINE_SEP);
        sbuf.append("</table>" + Layout.LINE_SEP);
        sbuf.append("</form>" + Layout.LINE_SEP);
        sbuf.append("<br>Start logging: " + String.format("%1$td.%1$tm.%1$tY %1$tT (%1$tA)", new java.util.Date()) + "<br><br>" + Layout.LINE_SEP);
        sbuf.append(getHeaderTable());
        sbuf.append("<tr>" + Layout.LINE_SEP);
        sbuf.append(getFormattedTimeHeader());
        sbuf.append(getFormattedThreadHeader());
        sbuf.append(getFormattedLevelHeader());
        sbuf.append(getFormattedLoggerHeader());
        sbuf.append(getFormattedMessageHeader());
        sbuf.append("</tr>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    /**
     * Возвращает начало таблицы лога.
     *
     * @return HTML-код начала таблицы.
     */
    protected String getHeaderTable() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<table class=\"details\" id=\"details\" cellspacing=\"0\" cellpadding=\"4\" border=\"0\" width=\"100%\">" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedTimeHeader() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<th>Time</th>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedThreadHeader() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<th>Thread</th>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedLevelHeader() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<th>&nbsp;</th>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedLoggerHeader() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<th>Class</th>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedMessageHeader() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<th>Message</th>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedTime(LoggingEvent event) {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<td>" + Layout.LINE_SEP);
        sbuf.append(Transform.escapeTags(String.format("%1$tT.%1$tL", new Date(event.timeStamp))) + Layout.LINE_SEP);
        sbuf.append("</td>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedThread(LoggingEvent event) {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<td>" + Layout.LINE_SEP);
        sbuf.append(Transform.escapeTags(event.getThreadName()));
        sbuf.append("</td>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedLevel(LoggingEvent event) {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<td>" + Layout.LINE_SEP);
        String levelName = event.getLevel().toString().toLowerCase();
        
        sbuf.append("<img src=\"/bali/data/images/report/" + levelName + ".gif\" alt=\"" + levelName + "\" width=\"12\" height=\"12\">" + Layout.LINE_SEP);
        sbuf.append("</td>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedLogger(LoggingEvent event) {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<td>" + Layout.LINE_SEP);
        sbuf.append(Transform.escapeTags(event.getLoggerName()) + Layout.LINE_SEP);
        sbuf.append("</td>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedMessage(LoggingEvent event) {        
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("<td>" + Layout.LINE_SEP);
        ThrowableInformation throwableInfo = event.getThrowableInformation();
        if (throwableInfo != null) {
            Throwable t = throwableInfo.getThrowable();
            //sbuf.append("<a name='error" + errorCounter + "'>"+errorCounter+".</a>");
            //errorCounter++;
            sbuf.append("Exception: <span class=\"exception hand\" onclick=\"toggleExceptionVisibility('" + event.toString() + "');\">" + Transform.escapeTags(t.getClass().getName()) + "</span>, description: " + Transform.escapeTags(t.getMessage()) + "<br>" + Layout.LINE_SEP);
        }
        
        String escapeTags;
        String addHtml = "#addhtml#";
        if (event.getRenderedMessage().startsWith(addHtml)){
        	escapeTags = event.getRenderedMessage().substring(addHtml.length());
        } else 
        	escapeTags = Transform.escapeTags(event.getRenderedMessage());
        
        if (null != escapeTags) {
            sbuf.append(escapeTags.replaceAll("\\[\\[", "<").replaceAll("\\]\\]", ">").replaceAll("\\{q\\}", "\"") + Layout.LINE_SEP);
        } else {
            sbuf.append("No message detected");            
        }
        sbuf.append("</td>" + Layout.LINE_SEP);
        return sbuf.toString();
    }

    protected String getFormattedThrowable(LoggingEvent event) {
        StringBuffer sbuf = new StringBuffer();
        String[] s = event.getThrowableStrRep();
        if (s != null) {
            sbuf.append("<tr class=\"" + event.getLevel().toString().toLowerCase() + " invisible\" id=\"" + event.toString() + "\"><td colspan=\"6\">" + Layout.LINE_SEP);
            appendThrowableAsHTML(s, sbuf);
            sbuf.append("</td></tr>" + Layout.LINE_SEP);
        }
        return sbuf.toString();
    }

    public String format(LoggingEvent event) {
        if (sbuf.capacity() > MAX_CAPACITY) {
            sbuf = new StringBuffer(BUF_SIZE);
        } else {
            sbuf.setLength(0);
        }

        sbuf.append("<tr class=\"" + event.getLevel().toString().toLowerCase() + "\">" + Layout.LINE_SEP);

        sbuf.append(getFormattedTime(event));
        sbuf.append(getFormattedThread(event));
        sbuf.append(getFormattedLevel(event));
        sbuf.append(getFormattedLogger(event));
        sbuf.append(getFormattedMessage(event));
        sbuf.append("</tr>" + Layout.LINE_SEP);

        sbuf.append(getFormattedThrowable(event));

        return sbuf.toString();
    }

    /**
     * convert Throwable в HTML .
     *
     * @param s    str Throwable.
     * @param sbuf where to put result
     */
    protected void appendThrowableAsHTML(String[] s, StringBuffer sbuf) {
         if (s != null && s.length != 0) {
            sbuf.append(Transform.escapeTags(s[0]) + Layout.LINE_SEP);
            for (int i = 1; i < s.length; i++) {
                if(s[i].startsWith(NO_ESC_PREFIX)) {
                    sbuf.append("<br>");
                    sbuf.append(s[i].substring(NO_ESC_PREFIX.length()));
                    sbuf.append(Layout.LINE_SEP);
                } else {
                    sbuf.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;" + Transform.escapeTags(s[i]) + Layout.LINE_SEP);
                }
            }
        }
    }

    public String getFooter() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("</table>" + Layout.LINE_SEP);
        sbuf.append("<br>" + Layout.LINE_SEP);
        sbuf.append("</body></html>");
        return sbuf.toString();
    }

    public boolean ignoresThrowable() {
        return false;
    }
}
