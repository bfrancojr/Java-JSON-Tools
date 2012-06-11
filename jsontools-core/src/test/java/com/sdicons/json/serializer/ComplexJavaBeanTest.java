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

public class ComplexJavaBeanTest
extends TestCase
{

    public ComplexJavaBeanTest(String lName)
    {
        super(lName);
    }

    Marshall marshall;

    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    public void testSelfReference()
    {
        try
        {
            MyBean lTest1 = new MyBean();
            lTest1.setId(113);
            lTest1.setName("SELF-POINTING");
            lTest1.setPtr(lTest1);

            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lTest1));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            MyBean lTest2 = (MyBean) lResult.getReference();
            Assert.assertNotNull(lTest2);
            Assert.assertEquals(lTest2.getName(), "SELF-POINTING");
            Assert.assertEquals(lTest2.getId(), 113);
            Assert.assertTrue(lTest2 == lTest2.getPtr());
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testCycle()
    {
        try
        {
            MyBean[] lTest1 = new MyBean[10];
            for(int i = 0; i < 10; i++)
            {
                lTest1[i] = new MyBean();
                lTest1[i].setName("CYCLE-" + i);
                lTest1[i].setId(i);
            }

            lTest1[0].setPtr(lTest1[9]);
            for(int i = 1; i < 10; i++)
                lTest1[i].setPtr(lTest1[i-1]);

            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lTest1));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            MyBean[] lTest2 = (MyBean[]) lResult.getReference();

            Assert.assertTrue(lTest2[0].getPtr() == lTest2[9]);
            for(int i = 1; i < 10; i++)
                Assert.assertTrue(lTest2[i].getPtr() == lTest2[i-1]);
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }
}
