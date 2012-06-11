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

import junit.framework.*;

import com.sdicons.json.serializer.marshall.Marshall;
import com.sdicons.json.serializer.marshall.MarshallValue;
import com.sdicons.json.serializer.marshall.JSONMarshall;

public class PrimitiveArrTest
extends TestCase
{

    public PrimitiveArrTest(String lName)
    {
        super(lName);
    }

    Marshall marshall;

    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    public void testBoolean()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new boolean[]{true, false, true, false}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Assert.assertTrue(java.util.Arrays.equals((boolean[])lResult.getReference(), new boolean[]{true, false, true, false}));
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testByte()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new byte[]{-1, 0, 1, 2}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Assert.assertTrue(java.util.Arrays.equals((byte[])lResult.getReference(), new byte[]{-1, 0, 1, 2}));
        }
        catch(Exception e)
        {
            e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testShort()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new short[]{-1, 0, 11, 2}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Assert.assertTrue(java.util.Arrays.equals((short[])lResult.getReference(), new short[]{-1, 0, 11, 2}));
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testChar()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new char[]{'a', 'b', 'A', 'Z'}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Assert.assertTrue(java.util.Arrays.equals((char[])lResult.getReference(), new char[]{'a', 'b', 'A', 'Z'}));
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testInt()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new int[]{-1003, 0, 1003, 2310}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Assert.assertTrue(java.util.Arrays.equals((int[])lResult.getReference(), new int[]{-1003, 0, 1003, 2310}));
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testLong()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new long[]{-1003, 0, 1003, 2311}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Assert.assertTrue(java.util.Arrays.equals((long[])lResult.getReference(), new long[]{-1003, 0, 1003, 2311}));
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testFloat()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new float[]{-1.13f, 0.0f, 1.13f, 2310.1f}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Assert.assertTrue(java.util.Arrays.equals((float[])lResult.getReference(), new float[]{-1.13f, 0.0f, 1.13f, 2310.1f}));
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testDouble()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(new double[]{-1.14, 0.0, 1.13, 2310.2}));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Assert.assertTrue(java.util.Arrays.equals((double[])lResult.getReference(), new double[]{-1.14, 0.0, 1.13, 2310.2}));
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }
}