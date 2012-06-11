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
 * A validator inspects a JSONValue. If everything is fine the validator does nothing, and
 * if an error is encounterd the validator throws an exception. Different validators can look
 * at different aspects of JSONValues. 
 */
public interface Validator
{
    /**
     * Validate a JSONValue.
     * @param aValue        The JSONValue that has to be validated.
     * @throws ValidationException If the validation fails.
     */
    public void validate(JSONValue aValue)
    throws ValidationException;
}
