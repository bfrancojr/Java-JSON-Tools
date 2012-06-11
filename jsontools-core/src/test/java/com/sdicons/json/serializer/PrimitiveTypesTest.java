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

public class PrimitiveTypesTest
extends TestCase
{

    public PrimitiveTypesTest(String lName)
    {
        super(lName);
    }

    Marshall marshall;

    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    public void testBoolean1()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(false));
            Assert.assertTrue(MarshallValue.BOOLEAN == lResult.getType());
            Assert.assertTrue(!lResult.getBoolean());
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testBoolean2()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(true));
            Assert.assertTrue(MarshallValue.BOOLEAN == lResult.getType());
            Assert.assertTrue(lResult.getBoolean());
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
            MarshallValue lResult = marshall.unmarshall(marshall.marshall((byte) 7));
            Assert.assertTrue(MarshallValue.BYTE == lResult.getType());
            Assert.assertTrue(lResult.getByte() == 7);
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
            MarshallValue lResult = marshall.unmarshall(marshall.marshall((short) -13));
            Assert.assertTrue(MarshallValue.SHORT == lResult.getType());
            Assert.assertTrue(lResult.getShort() == -13);
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
            MarshallValue lResult = marshall.unmarshall(marshall.marshall('q'));
            Assert.assertTrue(MarshallValue.CHAR == lResult.getType());
            Assert.assertTrue(lResult.getChar() == 'q');
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }

    public void testInteger()
    {
        try
        {
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(177));
            Assert.assertTrue(MarshallValue.INT == lResult.getType());
            Assert.assertTrue(lResult.getInt() == 177);
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
            MarshallValue lResult = marshall.unmarshall(marshall.marshall((long) 117));
            Assert.assertTrue(MarshallValue.LONG == lResult.getType());
            Assert.assertTrue(lResult.getLong() == 117);
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
            MarshallValue lResult = marshall.unmarshall(marshall.marshall((float) 3.14));
            Assert.assertTrue(MarshallValue.FLOAT == lResult.getType());
            Assert.assertTrue(lResult.getFloat() == (float) 3.14);
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
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(3.141567));
            Assert.assertTrue(MarshallValue.DOUBLE == lResult.getType());
            Assert.assertTrue(lResult.getDouble() == 3.141567);
        }
        catch(Exception e)
        {
             e.printStackTrace(System.out);
            Assert.fail();
        }
    }
}
