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

import java.io.Serializable;
import com.sdicons.json.validator.impl.ValidatorUtil;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONObject;

/**
 * A validator that accepts a validator description in JSON format.
 */
public class JSONValidator
implements Serializable, Validator
{
    private Validator validator;

    /**
     * Construct the validator based on the JSON description.
     * @param aValidation   The JSON description of the validator.
     * @throws ValidationException If the JSON description did not represent a validator.
     */
    public JSONValidator(JSONObject aValidation)
    throws ValidationException
    {
        validator = ValidatorUtil.buildValidator(aValidation);
    }

    /**
     * Validate a JSON value according to the rules described in the
     * validator rules.
     * @param aValue
     * @throws ValidationException
     */
    public void validate(JSONValue aValue)
    throws ValidationException
    {
        validator.validate(aValue);
    }
}
