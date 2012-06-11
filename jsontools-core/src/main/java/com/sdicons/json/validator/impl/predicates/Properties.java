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

import com.sdicons.json.model.*;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.impl.ValidatorUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Properties
extends Predicate
{
    private static class PropRule implements Serializable
    {
        private String key;
        private Validator rule;
        private boolean optional;

        public PropRule(String key, Validator rule, boolean optional)
        {
            this.key = key;
            this.rule = rule;
            this.optional = optional;
        }

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public Validator getRule()
        {
            return rule;
        }

        public void setRule(Validator rule)
        {
            this.rule = rule;
        }

        public boolean isOptional()
        {
            return optional;
        }

        public void setOptional(boolean optional)
        {
            this.optional = optional;
        }
    }

    private List<PropRule> required = new LinkedList<PropRule>();
    private HashMap<String, PropRule> all = new HashMap<String, PropRule>();
    private boolean allFields = true;

    public Properties(String aName, JSONObject aRule, HashMap<String,Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_PAIRS, JSONArray.class);
        List<JSONValue> lPairs = ((JSONArray) aRule.get(ValidatorUtil.PARAM_PAIRS)).getValue();

	if (aRule.containsKey(ValidatorUtil.PARAM_ALL))
	{
		ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_ALL, JSONBoolean.class);
		allFields = ((JSONBoolean) aRule.get(ValidatorUtil.PARAM_ALL)).getValue();
	}
        for (JSONValue lPair : lPairs)
        {
            if(!lPair.isObject())
            {
                final String lMsg = "A pair should be described by a JSONObject.";
                throw new ValidationException(lMsg, lPair, "JSONObject EXPECTED");
            }

            final JSONObject lObj = (JSONObject) lPair;

            ValidatorUtil.requiresAttribute(lObj, ValidatorUtil.PARAM_KEY, JSONString.class);
            final String lKeyname = ((JSONString) lObj.get(ValidatorUtil.PARAM_KEY)).getValue();

            Validator lValrule = new True(ValidatorUtil.ANONYMOUS_RULE, aRule);
            if (lObj.containsKey(ValidatorUtil.PARAM_RULE))
            {
                ValidatorUtil.requiresAttribute(lObj, ValidatorUtil.PARAM_RULE, JSONObject.class);
                lValrule = ValidatorUtil.buildValidator(lObj.get(ValidatorUtil.PARAM_RULE), aRuleset);
            }

            boolean lOptional = false;
            if (lObj.containsKey(ValidatorUtil.PARAM_OPTIONAL))
            {
                ValidatorUtil.requiresAttribute(lObj, ValidatorUtil.PARAM_OPTIONAL, JSONBoolean.class);
                lOptional = ((JSONBoolean) lObj.get(ValidatorUtil.PARAM_OPTIONAL)).getValue();
            }

            PropRule lRule = new PropRule(lKeyname, lValrule, lOptional);
            all.put(lKeyname, lRule);
            if(!lOptional) required.add(lRule);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        // Only for objects.
        if(!aValue.isObject()) fail("The value is not a JSONObject.", aValue);
        JSONObject lObj = (JSONObject) aValue;

        // First we check if required keys are there.
        for(PropRule aRequired : required)
        {
            if(!lObj.containsKey(aRequired.getKey())) fail("The object lacks a required key: \"" + aRequired.getKey() + "\".", aValue);
        }

        // Now we iterate over all keys in the object and lookup the spec.
        for(String lKey : lObj.getValue().keySet())
        {
            if (!all.containsKey(lKey)) {
		if (allFields) fail("The object contains an unspecified key: \"" + lKey + "\".", aValue);
		continue;
	    }
            PropRule lRule = all.get(lKey);
            try
            {
                lRule.getRule().validate(lObj.get(lKey));
            }
            catch(ValidationException e)
            {
                fail("The object property: \"" + lKey + "\" has invalid content. Internal message: " + e.getMessage(), aValue);
            }
        }
    }
}
