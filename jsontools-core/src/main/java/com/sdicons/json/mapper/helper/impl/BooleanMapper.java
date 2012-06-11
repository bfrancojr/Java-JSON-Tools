package com.sdicons.json.mapper.helper.impl;

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

import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONBoolean;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;

public class BooleanMapper
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return Boolean.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException
    {
        if(aValue.isString())
        {
            return Boolean.parseBoolean(((JSONString)aValue).getValue());
        }
        else if(aValue.isBoolean())
        {
            return ((JSONBoolean) aValue).getValue();
        }
        else throw new MapperException("BooleanMapper cannot map: " + aValue.getClass().getName());        
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        if(!Boolean.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("BooleanMapper cannot map: " + aPojo.getClass().getName());
        return new JSONBoolean((Boolean) aPojo);
    }
}