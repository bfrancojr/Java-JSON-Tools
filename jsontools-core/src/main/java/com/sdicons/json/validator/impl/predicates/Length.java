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
import com.sdicons.json.validator.impl.ValidatorUtil;

public class Length
extends Predicate
{
    private Integer minLength = null;
    private Integer maxLength = null;

    public Length(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName, aRule);

        if (aRule.containsKey(ValidatorUtil.PARAM_MIN))
        {
            JSONValue lMin = aRule.get(ValidatorUtil.PARAM_MIN);
            if (!lMin.isInteger())
            {
                final String lMsg = "Minimum length should be specified using an integer.";
                throw new ValidationException(lMsg, aRule, "WRONG TYPE");
            }
            else minLength = ((JSONInteger) lMin).getValue().intValue();
        }

        if (aRule.containsKey(ValidatorUtil.PARAM_MAX))
        {
            JSONValue lMax = aRule.get(ValidatorUtil.PARAM_MAX);
            if (!lMax.isInteger())
            {
                final String lMsg = "Maximum length should be specified using an integer.";
                throw new ValidationException(lMsg, aRule, "WRONG TYPE");
            }
            else maxLength = ((JSONInteger) lMax).getValue().intValue();
        }

    }

    public void validate(JSONValue aValue) throws ValidationException
    {
        int lSize = 0;
        if(aValue.isArray()) lSize = ((JSONArray) aValue).getValue().size();
        else if(aValue.isString()) lSize = ((JSONString) aValue).getValue().length();
        else if(aValue.isObject()) lSize = ((JSONObject) aValue).getValue().size();
        else fail("The value is not a JSONArray, JSONString or JSONObject.", aValue);

        // If there are lenght specs, we check them.
        if(minLength != null)
        {
            if(lSize < minLength) fail("The size (" + lSize +") is smaller then allowed (" + minLength + ").", aValue);
        }
        if(maxLength != null)
        {
            if( lSize > maxLength ) fail("The size (" + lSize +") is larger then allowed (" + maxLength + ").", aValue);
        }
    }
}
