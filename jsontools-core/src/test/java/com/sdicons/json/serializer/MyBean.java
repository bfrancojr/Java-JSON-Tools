package com.sdicons.json.serializer;

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

public class MyBean
{
    private int id;
    private String name;
    private MyBean ptr;

    private Integer int1, int2;

    public int getId()
    {
        return id;
    }

    public void setId(int aId)
    {
        id = aId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String aName)
    {
        name = aName;
    }

    public MyBean getPtr()
    {
        return ptr;
    }

    public void setPtr(MyBean aPtr)
    {
        ptr = aPtr;
    }

    public Integer getInt1()
    {
        return int1;
    }

    public void setInt1(Integer aInt1)
    {
        int1 = aInt1;
    }

    public Integer getInt2()
    {
        return int2;
    }

    public void setInt2(Integer aInt2)
    {
        int2 = aInt2;
    }
}
