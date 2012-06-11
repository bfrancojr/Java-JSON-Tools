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

import com.sdicons.json.model.JSONValue;

/**
 * The exception is thrown when a validation fails. 
 */
public class ValidationException
extends Exception
{
    private JSONValue culprit;
    private String validationRule;

    public ValidationException(String aComments, JSONValue aCulprit, String aRule)
    {
        super(aComments);
        culprit = aCulprit;
        validationRule = aRule;
    }

    public ValidationException(JSONValue aCulprit, String aRule)
    {
        super();
        culprit = aCulprit;
        validationRule = aRule;
    }

    public JSONValue getCulprit()
    {
        return culprit;
    }

    public String getValidationRule()
    {
        return validationRule;
    }

    public String getMessage()
    {
        return "Validation failed on rule \"" + validationRule + "\": " + super.getMessage() + " Offending part: " + culprit;
    }
}