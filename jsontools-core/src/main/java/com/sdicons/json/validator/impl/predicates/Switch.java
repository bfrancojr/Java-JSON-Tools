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
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;

import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;

public class Switch
extends Predicate
{
    private List<Case> rules = new LinkedList<Case>();
    private String key;

    private static class Case
    {
        private List<JSONValue> values;
        private Validator validator;

        public Case(Validator validator, List<JSONValue> values)
        {
            this.validator = validator;
            this.values = values;
        }

        public Validator getValidator()
        {
            return validator;
        }

        public boolean isApplicable(JSONValue aVal)
        {
            return values.contains(aVal);
        }
    }


    public Switch(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);

        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_CASE, JSONArray.class);
        List<JSONValue> lCases = ((JSONArray) aRule.get(ValidatorUtil.PARAM_CASE)).getValue();
        for (JSONValue lCase : lCases)
        {
            if(!lCase.isObject()) fail("A case in a swicht should be an object type.", lCase);
            JSONObject lObjCase = (JSONObject) lCase;

            ValidatorUtil.requiresAttribute(lObjCase, ValidatorUtil.PARAM_RULE, JSONObject.class);
            JSONObject lRule = (JSONObject) lObjCase.get(ValidatorUtil.PARAM_RULE);
            Validator lValidator = ValidatorUtil.buildValidator(lRule, aRuleset);

            ValidatorUtil.requiresAttribute(lObjCase, ValidatorUtil.PARAM_VALUES, JSONArray.class);
            JSONArray lVals = (JSONArray) lObjCase.get(ValidatorUtil.PARAM_VALUES);

            Case lNewCase = new Case(lValidator, lVals.getValue());
            rules.add(lNewCase);
        }

        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_KEY, JSONString.class);
        key = ((JSONString) aRule.get(ValidatorUtil.PARAM_KEY)).getValue();
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        if(!aValue.isObject()) fail("The value is not a JSONObject.", aValue);
        JSONObject lObj = (JSONObject) aValue;

        if(!lObj.containsKey(key)) fail("The object does not contain the key: \"" + key + "\".", lObj);
        JSONValue lVal = lObj.get(key);

        for (Case aCase : rules)
        {
            if(aCase.isApplicable(lVal))
            {
                aCase.getValidator().validate(lObj);
                return;
            }
        }
        fail("No applicable rule found for key: \"" + key + "\", value: " + lVal.toString(), aValue);
    }
}
