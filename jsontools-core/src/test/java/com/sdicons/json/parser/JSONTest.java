package com.sdicons.json.parser;

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

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import junit.framework.TestCase;

import java.util.Iterator;
import java.io.StringReader;

public class JSONTest
extends TestCase
{
    public void testExamples()
    {
        try
        {
            final JSONParser lParser = new JSONParser(JSONTest.class.getResourceAsStream("/data/config.json"));
            final JSONValue lConfig = lParser.nextValue();
            assertTrue(lConfig.isArray());
            final JSONArray lConfigArray = (JSONArray) lConfig;
            final Iterator<JSONValue> lIter = lConfigArray.getValue().iterator();

            while (lIter.hasNext())
            {
                final JSONValue lEntry = lIter.next();
                TestCase.assertTrue(lEntry.isString());
                final JSONString lEntryResource = (JSONString) lEntry;

                try
                {
                    System.out.println(" --------- " + lEntryResource.getValue());
                    final JSONParser lExampleParser = new JSONParser(JSONTest.class.getResourceAsStream(lEntryResource.getValue()));
                    JSONValue lExampleVal = lExampleParser.nextValue();
                    System.out.println(lExampleVal.render(true));
                    JSONValue.decorate(lExampleVal.strip());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    TestCase.fail(e.getMessage());
                }
            }
        }
        catch (TokenStreamException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (RecognitionException e)
        {
           TestCase.fail(e.getMessage());
        }
    }

    public void testAntiExamples()
    {
        try
        {
            final String lErrorExample = "{ \"fld1\" : \"val1\" ["; // This string contains an error.
            final String lErrorExample2 = "[1, 2, 3 {";

            final JSONParser lParser = new JSONParser(new StringReader(lErrorExample2));
            final JSONValue lConfig = lParser.nextValue();
            System.out.println(lConfig);
            TestCase.fail("Anb exception should be thrown when an error is found.");

        }
        catch(Exception e)
        {
            // This is correct, an exception should be thrown.
            // This test has to arrive here.
        }
    }

}

