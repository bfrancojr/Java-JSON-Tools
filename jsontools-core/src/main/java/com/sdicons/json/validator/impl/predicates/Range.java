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

import java.math.BigDecimal;

public class Range
extends Predicate
{
    private BigDecimal minValue = null;
    private BigDecimal maxValue = null;

    public Range(String aName, JSONObject aRule)
    throws ValidationException
    {
        super(aName, aRule);

        if (aRule.containsKey(ValidatorUtil.PARAM_MIN))
        {
            JSONValue lMin = aRule.get(ValidatorUtil.PARAM_MIN);
            if (!lMin.isNumber())
            {
                final String lMsg = "Minimum length should be specified using a number.";
                throw new ValidationException(lMsg, aRule, "WRONG TYPE");
            }
            else minValue = cvtNumber((JSONNumber) lMin);
        }

        if (aRule.containsKey(ValidatorUtil.PARAM_MAX))
        {
            JSONValue lMax = aRule.get(ValidatorUtil.PARAM_MAX);
            if (!lMax.isInteger())
            {
                final String lMsg = "Maximum length should be specified using an integer.";
                throw new ValidationException(lMsg, aRule, "WRONG TYPE");
            }
            else maxValue = cvtNumber((JSONNumber) lMax);
        }
    }

    public void validate(JSONValue aValue) throws ValidationException
    {
        if(!aValue.isNumber()) fail("The value is not a JSONNumber.", aValue);
        BigDecimal lSize = cvtNumber((JSONNumber) aValue);

        // If there are length specs, we check them.
        if(minValue != null)
        {

            if(lSize.compareTo(minValue) < 0) fail("The size (" + lSize +") is smaller then allowed (" + minValue + ").", aValue);
        }
        if(maxValue != null)
        {
            if(lSize.compareTo(maxValue) > 0 ) fail("The size (" + lSize +") is larger then allowed (" + maxValue + ").", aValue);
        }
    }

    private BigDecimal cvtNumber(JSONNumber aNum)
    {
        if (aNum.isInteger()) return new BigDecimal(((JSONInteger) aNum).getValue());
        else return ((JSONDecimal) aNum).getValue();
    }
}
