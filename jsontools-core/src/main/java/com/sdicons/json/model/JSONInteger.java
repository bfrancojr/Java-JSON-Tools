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

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Represents a JSON int. JSON only defines "numbers" but during parsing a difference is
 * made between integral types and floating types.
 */
public class JSONInteger
extends JSONNumber implements Serializable
{
    private BigInteger value;

    public JSONInteger(BigInteger value)
    {
        if(value == null) throw new IllegalArgumentException();
        this.value = value;
    }

    public BigInteger getValue()
    {
        return value;
    }

    public String toString()
    {
        return "JSONInteger(" + getLine() + ":" + getCol() + ")[" + value.toString() + "]";
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

        final JSONInteger that = (JSONInteger) o;
        return value.equals(that.value);
    }

    public int hashCode()
    {
        return value.hashCode();
    }

    /**
     * Remove all JSON information, in the case of a JSONInteger this means a BigInteger.
     * @return A BigInteger representing the value of the JSONInteger object.
     */
    public Object strip()
    {
        return value;
    }
}
