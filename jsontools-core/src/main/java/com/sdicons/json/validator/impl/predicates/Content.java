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

import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.impl.ValidatorUtil;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONArray;

import java.util.HashMap;
import java.util.Iterator;

public class Content
extends Predicate
{
    private Validator rule;

    public Content(String aName, JSONObject aRule, HashMap<String, Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);

        rule = new True(aName, aRule);
        if (aRule.containsKey(ValidatorUtil.PARAM_RULE))
        {
            ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_RULE, JSONObject.class);
            rule = ValidatorUtil.buildValidator(aRule.get(ValidatorUtil.PARAM_RULE), aRuleset);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        Iterator<JSONValue> lIter = null;
        if(aValue.isArray()) lIter = ((JSONArray) aValue).getValue().iterator();
        else if(aValue.isObject()) lIter = ((JSONObject) aValue).getValue().values().iterator();
        else fail("The value is not a JSONComplex, it has no content.", aValue);

        // Finally we apply the internal rule to all elements.
        if(lIter != null)
        {
            while(lIter.hasNext())
            {
                JSONValue lVal = (JSONValue) lIter.next();
                rule.validate(lVal);
            }
        }
    }
}
