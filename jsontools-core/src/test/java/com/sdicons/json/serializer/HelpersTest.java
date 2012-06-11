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

import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.Marshall;
import com.sdicons.json.serializer.marshall.MarshallValue;
import com.sdicons.json.serializer.marshall.MarshallException;
import junit.framework.Assert;
import junit.framework.TestCase;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.awt.*;

@SuppressWarnings({"unchecked"})
public class HelpersTest
extends TestCase
{

    public HelpersTest(String lName)
    {
        super(lName);
    }

    Marshall marshall;

    public void setUp()
    throws Exception
    {
        marshall = new JSONMarshall();
    }

    public void testByte()
    {
        try
        {
            Byte lByte = (byte) 5;
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lByte));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Byte lByte2 = (Byte) lResult.getReference();
            Assert.assertTrue(lByte2.equals(lByte));
        }
        catch(Exception e)
        {
            Assert.fail();
        }

    }

    public void testShort()
    {
        try
        {
            Short lShort = (short) 3;
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lShort));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Short lShort2 = (Short) lResult.getReference();
            Assert.assertTrue(lShort2.equals(lShort));
        }
        catch(Exception e)
        {
            Assert.fail();
        }

    }

    public void testChar()
    {
        try
        {
            Character lchar = 'x';
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lchar));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Character lChar2 = (Character) lResult.getReference();
            Assert.assertTrue(lChar2.equals(lchar));
        }
        catch(Exception e)
        {
            Assert.fail();
        }

    }

    public void testInteger()
    {
        try
        {
            Integer lInt = 17;
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lInt));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Integer lInt2 = (Integer) lResult.getReference();
            Assert.assertTrue(lInt2.equals(lInt));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testLong()
    {
        try
        {
            Long lLong = (long) 21;
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lLong));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Long lLong2 = (Long) lResult.getReference();
            Assert.assertTrue(lLong2.equals(lLong));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testFloat()
    {
        try
        {
            Float lFloat = new Float(21.331);
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lFloat));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Float lFloat2 = (Float) lResult.getReference();
            Assert.assertTrue(lFloat2.equals(lFloat));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testDouble()
    {
        try
        {
            Double lDouble = 21.156331;
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lDouble));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            Double lDouble2 = (Double) lResult.getReference();
            Assert.assertTrue(lDouble2.equals(lDouble));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testBigDecimal()
    {
        try
        {
            BigDecimal lDecimal = new BigDecimal(21.331);
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lDecimal));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            BigDecimal lDecimal2 = (BigDecimal) lResult.getReference();
            Assert.assertTrue(lDecimal2.equals(lDecimal));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testBigInteger()
    {
        try
        {
            BigInteger lBigInt = new BigInteger("123456789123456789123456789123456789");
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lBigInt));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            BigInteger lBigInt2 = (BigInteger) lResult.getReference();
            Assert.assertTrue(lBigInt2.equals(lBigInt));
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testDate()
    {
        try
        {
            final Date lDate = new Date();
            MarshallValue lResult = marshall.unmarshall(marshall.marshall(lDate));
            Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
            final Date lDate2 = (Date) lResult.getReference();
            Assert.assertTrue(lDate2.getTime() == lDate.getTime());
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testCollections()
    {
        try
        {
            {
                // Linked lists.
                ////////////////
                final List lLinkedList = new LinkedList();
                lLinkedList.add("uno");
                lLinkedList.add("duo");
                lLinkedList.add("tres");

                final List lNestedLinkedList = new LinkedList();
                lNestedLinkedList.add("one");
                lNestedLinkedList.add("two");
                lNestedLinkedList.add("three");
                lLinkedList.add(lNestedLinkedList);

                // This will not work! See java bug 4756334.
                // A container cannot contain itself when  the hashcode is calculated, this generates a
                // StackOverflow. According to Sun this behaviour is wanted.
                //lLinkedList.add(lLinkedList);

                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lLinkedList));
                Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
                final List lLinkedList2 = (LinkedList) lResult.getReference();
                Assert.assertTrue(lLinkedList.size() == lLinkedList2.size());
            }

            // Array lists.
            ///////////////
            {
                final List lArrayList = new ArrayList();
                lArrayList.add("uno");
                lArrayList.add("duo");
                lArrayList.add("tres");

                final List lNestedArrayList = new ArrayList();
                lNestedArrayList.add("one");
                lNestedArrayList.add("two");
                lNestedArrayList.add("three");
                lArrayList.add(lNestedArrayList);

                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lArrayList));
                Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
                final List lArrayList2 = (ArrayList) lResult.getReference();
                Assert.assertTrue(lArrayList.size() == lArrayList2.size());
            }

            // TreeSet.
            ///////////
            {
                final Set lTreeSet = new TreeSet();
                lTreeSet.add("uno");
                lTreeSet.add("duo");
                lTreeSet.add("tres");

                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lTreeSet));
                Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
                final Set lTreeSet2 = (TreeSet) lResult.getReference();
                Assert.assertTrue(lTreeSet.size() == lTreeSet2.size());
            }
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testMaps()
    {
        try
        {
            {
                // HashMap.
                ////////////////
                final Map lHashMap = new HashMap();
                lHashMap.put("uno", "one");
                lHashMap.put("duo", "two");
                lHashMap.put("tres", "three");

                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lHashMap));
                Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
                final Map lHashMap2 = (HashMap) lResult.getReference();
                Assert.assertTrue(lHashMap.size() == lHashMap2.size());
            }

            {
                // TreeMap.
                ////////////////
                final Map lTreeMap = new TreeMap();
                lTreeMap.put("uno", "one");
                lTreeMap.put("duo", "two");
                lTreeMap.put("tres", "three");

                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lTreeMap));
                Assert.assertTrue(MarshallValue.REFERENCE == lResult.getType());
                final Map lTreeMap2 = (TreeMap) lResult.getReference();
                Assert.assertTrue(lTreeMap.size() == lTreeMap2.size());
            }
        }
        catch(Exception e)
        {
            Assert.fail();
        }
    }

    public void testColors()
    {
        try
        {
            {
                final Color lColor = Color.WHITE;
                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lColor));
                Assert.assertTrue(lResult.getReference().equals(lColor));
            }

            {
                final Color lColor = Color.RED;
                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lColor));
                Assert.assertTrue(lResult.getReference().equals(lColor));
            }

             {
                final Color lColor = Color.GREEN;
                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lColor));
                Assert.assertTrue(lResult.getReference().equals(lColor));
            }

             {
                final Color lColor = Color.BLUE;
                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lColor));
                Assert.assertTrue(lResult.getReference().equals(lColor));
            }
        }
        catch (MarshallException e)
        {
            Assert.fail();
        }
    }

    public void testFonts()
    {
        try
        {
            {
                final Font lFont = new JPanel().getFont();
                MarshallValue lResult = marshall.unmarshall(marshall.marshall(lFont));
                Assert.assertTrue(lResult.getReference().equals(lFont));
            }
        }
        catch (MarshallException e)
        {
            Assert.fail();
        }
    }

    static enum SimpsonEnum {HOMER, MARGE, LISA, BART, MAGGY}

    public void testEnums()
    {
        try
        {
            {
                for(SimpsonEnum lSimpson: SimpsonEnum.values())
                {
                    MarshallValue lResult = marshall.unmarshall(marshall.marshall(lSimpson));
                    Assert.assertTrue(lResult.getReference() == lSimpson);
                }
            }
        }
        catch (MarshallException e)
        {
            Assert.fail();
        }
    }
}