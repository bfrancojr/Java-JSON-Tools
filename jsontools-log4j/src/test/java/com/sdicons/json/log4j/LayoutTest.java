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

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.JSONValidator;
import com.sdicons.json.validator.ValidationException;
import junit.framework.TestCase;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import antlr.TokenStreamException;
import antlr.RecognitionException;


public class LayoutTest
extends TestCase
{
    public void testLayout()
    {
        File lTempLog = null;

        try
        {
            lTempLog = File.createTempFile("json-log-test", ".txt");
            lTempLog.createNewFile();

            final Logger lLog = Logger.getLogger(LayoutTest.class);

            final Layout lLayout = new JSONLayout();
            final FileAppender lAppender = new FileAppender();
            lAppender.setFile(lTempLog.getAbsolutePath());
            lAppender.setLayout(lLayout);
            lAppender.activateOptions();

            final Logger lRootLogger = Logger.getRootLogger();
            lRootLogger.addAppender(new ConsoleAppender(lLayout));
            lRootLogger.addAppender(lAppender);

            // Do some logging from the main thread.
            lLog.info("This is an info message.");
            lLog.debug("This is a debug message.");
            lLog.error("This is an error message.");
            lLog.fatal("This is a fatal message.", new IllegalArgumentException("This is an illegal argument."));

            // Do some logging from helper threads.
            for(int i = 0; i < 10; i++)
            {
                new Thread(new Runnable()
                {
                    public void run()
                    {
                        lLog.info("This is an info message from a thread.");
                        lLog.debug("This is a debug message from a thread.");
                        lLog.error("This is an error message from a tread.");
                    }
                }).start();
            }

            // Wait some time for the threads to end ...
            try { Thread.sleep(2000); } catch (InterruptedException e) { }

            // Open the JSON log file.
            FileInputStream lLogStream = new FileInputStream(lTempLog);
            JSONParser lLogParser = new JSONParser(lLogStream);

            // Initialize the validator of the log file contents.
            final JSONParser lLogValidatorParser = new JSONParser(LayoutTest.class.getResourceAsStream("/log4j-validator.json"));
            final Validator lLogValidator = new JSONValidator((JSONObject) lLogValidatorParser.nextValue());

            // Initialize statistics.
            int lCount = 0;
            HashMap<String,int[]> lStats = new HashMap<String, int[]>();
            lStats.put("INFO", new int[]{0});
            lStats.put("ERROR", new int[]{0});
            lStats.put("DEBUG", new int[]{0});
            lStats.put("FATAL", new int[]{0});

            // While info available in the stream (we take 1 because of the trailing newline).
            while(lLogStream.available() > 1)
            {
                try
                {
                    JSONValue lVal = lLogParser.nextValue();
                    lLogValidator.validate(lVal);
                    
                    JSONObject lLogObj = (JSONObject) lVal;
                    String lLevel = ((JSONString) lLogObj.getValue().get("level")).getValue();
                    if(lStats.containsKey(lLevel))
                    {
                        int[] lLevelCount = lStats.get(lLevel);
                        lLevelCount[0]++;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    TestCase.fail();
                }

                lCount++;
            }

            // Test some stattistics.
            TestCase.assertEquals(lCount, 34);
            TestCase.assertEquals((lStats.get("ERROR"))[0], 11);
            TestCase.assertEquals((lStats.get("INFO"))[0], 11);
            TestCase.assertEquals((lStats.get("DEBUG"))[0], 11);
            TestCase.assertEquals((lStats.get("FATAL"))[0], 1);

        }
        catch (IOException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (ValidationException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (TokenStreamException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (RecognitionException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        finally
        {
            // Don't forget to clean up the test file.
            if(lTempLog != null) lTempLog.delete();
        }
    }
}
