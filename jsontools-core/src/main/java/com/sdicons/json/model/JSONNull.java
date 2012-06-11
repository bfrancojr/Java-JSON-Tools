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
 * Represents a JSON null value.
 * The only valid example is: <code>null.</code>
 */
public class JSONNull
extends JSONSimple
{
    public static final JSONNull NULL = new JSONNull();

    public JSONNull()
    {
    }

    public String toString()
    {
        return "JSONNull(" + getLine() + ":" + getCol() + ")";
    }

    protected String render(boolean pretty, String indent)
    {
        if(pretty) return indent + "null";
        else return "null";
    }

    public boolean equals(Object obj)
    {
        return obj != null && obj instanceof JSONNull;
    }

    /**
     * Strip all JSON information. In the case of a JSONNull object, only null remains...
     * @return null.
     */
    public Object strip()
    {
        return null;
    }
}
