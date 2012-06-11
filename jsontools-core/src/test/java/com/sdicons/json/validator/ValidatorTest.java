package com.sdicons.json.validator;

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
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import junit.framework.TestCase;

public class ValidatorTest
extends TestCase
{
    public void testSimple()
    {
        try
        {
            // This is a good illustration of the use of a validator.
            // The code is kept as simple and complete as possible.
            final JSONParser lParser = new JSONParser(ValidatorTest.class.getResourceAsStream("/rules/simple.json"));
            final Validator lChecker = new JSONValidator((JSONObject) lParser.nextValue());
            lChecker.validate(new JSONObject());                  
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
        catch (ValidationException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
    }

    public void testValidations()
    {
        try
        {
            final JSONParser lParser1 = new JSONParser(ValidatorTest.class.getResourceAsStream("/validator-validator.json"));
            final JSONObject lValidatorObject = (JSONObject) lParser1.nextValue();
            final Validator lValidatorValidator = new JSONValidator(lValidatorObject);

            final JSONParser lParser = new JSONParser(ValidatorTest.class.getResourceAsStream("/rules/config.json"));
            final JSONValue lConfig = lParser.nextValue();

            final JSONArray lConfigArray = (JSONArray) lConfig;

            for (final JSONValue lEntry : lConfigArray.getValue())
            {
                final JSONString lEntryResource = (JSONString) lEntry;

                try
                {
                    System.out.println(" --------- " + lEntryResource.getValue());
                    testCase(lEntryResource.getValue(), lValidatorValidator);
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
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
        catch (ValidationException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
    }


    private void testCase(String aResource, Validator aValidatorValidator)
    {
        try
        {
            final JSONParser lParser = new JSONParser(ValidatorTest.class.getResourceAsStream(aResource));
            final JSONObject lTestData = (JSONObject) lParser.nextValue();

            final JSONObject lCheckerObject = (JSONObject) lTestData.get("validator");

            // Note that in practice it is not necessary to validate a validator. The validator code
            // is independent of the validator mechanism, otherwise we would have a chicken-egg problem.
            // The check is here to test the validator-validator definition.
            aValidatorValidator.validate(lCheckerObject);

            final Validator lChecker = new JSONValidator(lCheckerObject);

            final JSONArray lGoods = (JSONArray) lTestData.get("good");
            final JSONArray lBads = (JSONArray) lTestData.get("bad");

            for (JSONValue jsonValue : lGoods.getValue())
            {
                try
                {
                    lChecker.validate(jsonValue);
                }
                catch (ValidationException e)
                {
                    // Failure if exception.
                    e.printStackTrace(System.out);
                    TestCase.fail("Should have succeeded: " + jsonValue.toString());
                }
            }

            for (JSONValue jsonValue1 : lBads.getValue())
            {
                try
                {
                    lChecker.validate(jsonValue1);
                    // Failure if success.
                    TestCase.fail("Should have failed: " + jsonValue1.toString());
                }
                catch (ValidationException eIgnore)
                {
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
        catch (ValidationException e)
        {
            e.printStackTrace();
            TestCase.fail(e.getMessage());
        }
    }
}
