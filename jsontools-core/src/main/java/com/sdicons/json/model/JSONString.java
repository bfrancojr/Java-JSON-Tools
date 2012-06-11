package com.sdicons.json.model;

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

import com.sdicons.json.parser.impl.ParserUtil;

/**
 * Represents a JSON delimited string.
 * Examples are: <code>"Hello" and "World"</code>; <code>"Hello\nWorld"</code> contains a newline.
 * Strings are always delimited with double quotes.
 */
public class JSONString
extends JSONSimple
{
    private String value;

    public JSONString(String value)
    {
        if(value == null) throw new IllegalArgumentException();
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public String toString()
    {
        return "JSONString(" + getLine() + ":" + getCol() + ")[" + ParserUtil.render(value, false, "") + "]";
    }

    protected String render(boolean pretty, String indent)
    {
        return ParserUtil.render(value, pretty, indent);
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JSONString that = (JSONString) o;
        return value.equals(that.value);
    }

    public int hashCode()
    {
        return value.hashCode();
    }

    /**
     * A pure Java object, all JSON information is removed. A JSONString
     * trivially maps to a Java String.
     * @return A Java String representing the contents of a JSONString.
     */
    public Object strip()
    {
        return value;
    }
}
