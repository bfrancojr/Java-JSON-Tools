package com.sdicons.json.serializer.helper.impl;

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
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;

import java.util.HashMap;

public class EnumHelper
extends AbstractHelper
{
    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_CLASS);
        final String lEnumClassName = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_CLASS)).getValue();

        final Class lEnumClass;
        try
        {
            lEnumClass = Class.forName(lEnumClassName);
        }
        catch (ClassNotFoundException e)
        {
            final String lMsg = "The class cannot be instantiated, it cannot be found in the classpath: " + lEnumClassName;
            throw new MarshallException(lMsg);
        }

        if(lEnumClass.isEnum())
        {
            Object[] lEnumVals = lEnumClass.getEnumConstants();
            for(Object lEnumVal: lEnumVals)
            {
                JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_VALUE);
                final String lVal = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_VALUE)).getValue();
                if(lEnumVal.toString().equals(lVal)) return lEnumVal;
            }
        }
        else
        {
            final String lMsg = "Enum helper tried to handle a non-enum class: " + lEnumClassName;
            throw new MarshallException(lMsg);
        }

        final String lMsg = "The enum class *is found* but no matching value could be found." + lEnumClassName;
        throw new MarshallException(lMsg);
    }

    public Class getHelpedClass()
    {
        return Enum.class;
    }
}
