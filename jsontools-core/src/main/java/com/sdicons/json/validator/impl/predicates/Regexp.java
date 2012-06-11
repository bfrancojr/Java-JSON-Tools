package com.sdicons.json.validator.impl.predicates;

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
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.impl.ValidatorUtil;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.regex.Matcher;

public class Regexp
extends Predicate
{
    private Pattern pattern;
    private String representation;

    public Regexp(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName, aRule);

        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_PATTERN, JSONString.class);
        String lPattern = ((JSONString) aRule.get(ValidatorUtil.PARAM_PATTERN)).getValue();

        try
        {
            pattern = Pattern.compile(lPattern);
            representation = lPattern;
        }
        catch (PatternSyntaxException e)
        {
           fail("Error while compiling the pattern: " + e.getMessage(), aRule);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isString()) fail("The value is not a JSONString." ,aValue);
        final String lString = ((JSONString) aValue).getValue();

        Matcher lMatcher = pattern.matcher(lString);
        if(!lMatcher.matches())
        {
            fail("The string: \"" + lString + "\" does not match the pattern: \"" + representation + "\".", aValue);
        }
    }
}
