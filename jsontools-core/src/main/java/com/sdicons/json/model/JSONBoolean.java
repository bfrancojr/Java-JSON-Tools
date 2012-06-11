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

/**
 * Represents a JSON boolean value.
 * Examples are: <code>true, false.</code>
 */
public class JSONBoolean
extends JSONSimple
{
    public static final JSONBoolean TRUE = new JSONBoolean(true);
    public static final JSONBoolean FALSE = new JSONBoolean(false);

    private boolean value;

    public JSONBoolean(boolean value)
    {
        this.value = value;
    }

    public boolean getValue()
    {
        return value;
    }

    public String toString()
    {
        return "JSONBoolean(" + getLine() + ":" + getCol() + ")[" + value +"]";
    }

    protected String render(boolean pretty, String indent)
    {
        if(pretty) return indent + value;
        else return "" + value;
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final JSONBoolean that = (JSONBoolean) o;

        return value == that.value;
    }

    public int hashCode()
    {
        return (value ? 1 : 0);
    }

    /**
     * Get the Java object, remove all JSON information. In the case of a JSONBoolean, this
     * is a Java Boolean object.
     * @return Boolean.TRUE or Boolean.FALSE depending on the value of the JSONBoolean.
     */
    public Object strip()
    {
        return value?Boolean.TRUE:Boolean.FALSE;
    }
}
