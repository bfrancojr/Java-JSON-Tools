package com.sdicons.json.log4j;

/*
    JSONTools - Java JSON Tools
    Copyright (C) 2006-2008 S.D.I.-Consulting BVBA
    http://www.sdi-consulting.com
    mailto://nospam@sdi-consulting.com

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.ISO8601DateFormat;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONInteger;

import java.util.Date;
import java.text.DateFormat;
import java.math.BigInteger;

public class JSONLayout
extends Layout
{
    // Requested options.
    private boolean prettyPrint = true;
    private boolean ignoreThrowable = false;

    // Active options.
    private boolean activePrettyPrint = prettyPrint;
    private boolean activeIgnoreThrowable = ignoreThrowable;

    public java.lang.String format(LoggingEvent loggingEvent)
    {
        DateFormat lFormat = new ISO8601DateFormat();

        // First we create a JSON logging object.
        JSONObject lLogObj = new JSONObject();
        // Now we can fill the object with the event attributes.
        lLogObj.getValue().put("level", new JSONString(loggingEvent.getLevel().toString()));
        lLogObj.getValue().put("message", new JSONString(loggingEvent.getRenderedMessage()));
        lLogObj.getValue().put("logger", new JSONString(loggingEvent.getLoggerName()));
        lLogObj.getValue().put("thread", new JSONString(loggingEvent.getThreadName()));

        JSONArray lLogTimes = new JSONArray();
        lLogTimes.getValue().add(new JSONString(lFormat.format(new Date(loggingEvent.timeStamp))));
        lLogTimes.getValue().add(new JSONInteger(new BigInteger(Long.toString(loggingEvent.timeStamp))));
        lLogObj.getValue().put("timestamp", lLogTimes);

        ThrowableInformation lTi = loggingEvent.getThrowableInformation();
        if(lTi != null && !activeIgnoreThrowable)
        {
            JSONObject lExcObj = new JSONObject();
            lLogObj.getValue().put("exception", lExcObj);

            JSONArray lExcMsgs = new JSONArray();
            String[] lThrRep = loggingEvent.getThrowableStrRep();
            if(lThrRep != null && lThrRep.length > 0)
            {
                for (String lMsg : lThrRep)
                {
                    lExcMsgs.getValue().add(new JSONString(lMsg));
                }

            }

            lExcObj.getValue().put("class", new JSONString(lTi.getThrowable().getClass().getCanonicalName()));
            lExcObj.getValue().put("stacktrace", lExcMsgs);
            lExcObj.getValue().put("message", new JSONString(lTi.getThrowable().getMessage()));
        }

        return lLogObj.render(activePrettyPrint) + "\n";
    }

    public void activateOptions()
    {
        activePrettyPrint = prettyPrint;
        activeIgnoreThrowable = ignoreThrowable;
    }

    public boolean ignoresThrowable()
    {
        return ignoreThrowable;
    }

    public boolean isPrettyPrint()
    {
        return prettyPrint;
    }

    public void setPrettyPrint(String prettyPrint)
    {
        this.prettyPrint = Boolean.parseBoolean(prettyPrint);
    }

    public boolean isIgnoreThrowable()
    {
        return ignoreThrowable;
    }

    public void setIgnoreThrowable(String ignoreThrowable)
    {
        this.ignoreThrowable = Boolean.parseBoolean(ignoreThrowable);
    }
}
