package com.sdicons.json.parser.impl;

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

public class ParserUtil
{
    public static String hexToChar(String i, String j, String k, String l)
    {
         return Character.toString((char)Integer.parseInt("" + i + j + k + l, 16));
    }

    public static String render(String value, boolean pretty, String indent)
    {
        final StringBuilder lBuf = new StringBuilder();
        if(pretty) lBuf.append(indent);
        lBuf.append("\"");
        for(int i = 0; i < value.length(); i++)
        {
            final char lChar = value.charAt(i);
            if(lChar == '\n') lBuf.append("\\n");
            else if(lChar == '\r') lBuf.append("\\r");
            else if(lChar == '\t') lBuf.append("\\t");
            else if(lChar == '\b') lBuf.append("\\b");
            else if(lChar == '\f') lBuf.append("\\f");
//            else if(lChar == '/') lBuf.append("\\/");
            else if(lChar == '\"') lBuf.append("\\\"");
            else if(lChar == '\\') lBuf.append("\\\\");
            else lBuf.append(lChar);
        }

        lBuf.append("\"");
        return lBuf.toString();
    }
}
