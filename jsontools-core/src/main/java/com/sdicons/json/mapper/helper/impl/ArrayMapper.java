package com.sdicons.json.mapper.helper.impl;

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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

public class ArrayMapper
implements SimpleMapperHelper
{
    public JSONValue toJSON(Object aObj)
    throws MapperException
    {
    	final Class lClass = aObj.getClass();
        final String lObjClassName = lClass.getName();

    	 String lComponentName = "unknown";
         if(lObjClassName.startsWith("[L"))
             // Array of objects.
        	 lComponentName = lObjClassName.substring(2, lObjClassName.length() - 1);
         else
             // Array of array; Array of primitive types.
        	 lComponentName = lObjClassName.substring(1);
         
        final JSONArray lElements = new JSONArray();

        if(isPrimitiveArray(lComponentName))
        {
            if("I".equals(lComponentName))
            {
                int[] lArr = (int[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                    lElements.getValue().add(JSONMapper.toJSON(lArr[i]));
            }
            if("C".equals(lComponentName))
            {
                char[] lArr = (char[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(JSONMapper.toJSON(lArr[i]));
            }
            else if("Z".equals(lComponentName))
            {
                boolean[] lArr = (boolean[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(JSONMapper.toJSON(lArr[i]));
            }
            else if("S".equals(lComponentName))
            {
                short[] lArr = (short[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(JSONMapper.toJSON(lArr[i]));
            }
            else if("B".equals(lComponentName))
            {
                byte[] lArr = (byte[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(JSONMapper.toJSON(lArr[i]));
            }
            else if("J".equals(lComponentName))
            {
                long[] lArr = (long[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(JSONMapper.toJSON(lArr[i]));
            }
            else if("F".equals(lComponentName))
            {
                float[] lArr = (float[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(JSONMapper.toJSON(lArr[i]));
            }
            else if("D".equals(lComponentName))
            {
                double[] lArr = (double[]) aObj;
                for(int i = 0; i < lArr.length; i++)
                	lElements.getValue().add(JSONMapper.toJSON(lArr[i]));
            }
        }
        else
        {
            Iterator lIter = Arrays.asList((Object[]) aObj).iterator();
            while(lIter.hasNext())
            {
                Object lArrEl = lIter.next();
                lElements.getValue().add(JSONMapper.toJSON(lArrEl));
            }
        }
        return lElements;
    }


    public Class getHelpedClass()
    {
        return null;
    }

    private boolean isPrimitiveArray(String aClassName)
    {
        return ("I".equals(aClassName) || "Z".equals(aClassName) || "S".equals(aClassName) ||
                "B".equals(aClassName) || "J".equals(aClassName) || "F".equals(aClassName) ||
                "D".equals(aClassName) || "C".equals(aClassName));
    }

	public Object toJava(JSONValue aValue, Class aRequestedClass) throws MapperException {
		if(!aValue.isArray()) throw new MapperException("ArrayMapper cannot map: " + aValue.getClass().getName());
		
        // First we fetch all array elements.
        JSONArray lValues = (JSONArray)aValue;
        
        final String lObjClassName = aRequestedClass.getName();

    	 String lArrClassName = "unknown";
         if(lObjClassName.startsWith("[L"))
             // Array of objects.
        	 lArrClassName = lObjClassName.substring(2, lObjClassName.length() - 1);
         else
             // Array of array; Array of primitive types.
        	 lArrClassName = lObjClassName.substring(1);

        final List<Object> lElements = new LinkedList<Object>();
        for (JSONValue jsonValue : lValues.getValue())
        {
            try {
            	if(isPrimitiveArray(lArrClassName)){
            		Class primitiveClass=null;
            		if("I".equals(lArrClassName)) primitiveClass=Integer.class;
            		else  if("C".equals(lArrClassName))	primitiveClass=Character.class;
                    else if("Z".equals(lArrClassName)) 	primitiveClass=Boolean.class;
                    else if("S".equals(lArrClassName)) 	primitiveClass=Short.class;
                    else if("B".equals(lArrClassName)) 	primitiveClass=Byte.class;
                    else if("J".equals(lArrClassName)) 	primitiveClass=Long.class;
                    else if("F".equals(lArrClassName)) 	primitiveClass=Float.class;
                    else if("D".equals(lArrClassName))	primitiveClass=Double.class;
                    else {
                        final String lMsg = "Unknown primitive array type: " + lArrClassName;
                        throw new  MapperException(lMsg);
                    }
            		lElements.add(JSONMapper.toJava(jsonValue,primitiveClass));
            	}else{
            		lElements.add(JSONMapper.toJava(jsonValue,Class.forName(lArrClassName)));	
            	}
            	
			}
            catch (ClassNotFoundException e)
            {
				throw new MapperException("No Class Found: " + lArrClassName);
			}
        }
        final int lArrSize = lElements.size();

        if(isPrimitiveArray(lArrClassName))
        {
            if("I".equals(lArrClassName))
            {
                int[] lArr = new int[lArrSize];
                Iterator lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Integer) lIter.next()).intValue();
                    i++;
                }
                return lArr;
            }
            if("C".equals(lArrClassName))
            {
                char[] lArr = new char[lArrSize];
                Iterator lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Character) lIter.next()).charValue();
                    i++;
                }
                return lArr;
            }
            else if("Z".equals(lArrClassName))
            {
                boolean[] lArr = new boolean[lArrSize];
                Iterator lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Boolean) lIter.next()).booleanValue();
                    i++;
                }
                return lArr;
            }
            else if("S".equals(lArrClassName))
            {
                short[] lArr = new short[lArrSize];
                Iterator lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Short) lIter.next()).shortValue();
                    i++;
                }
                return lArr;
            }
            else if("B".equals(lArrClassName))
            {
                byte[] lArr = new byte[lArrSize];
                Iterator lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Byte) lIter.next()).byteValue();
                    i++;
                }
                return lArr;
            }
            else if("J".equals(lArrClassName))
            {
                long[] lArr = new long[lArrSize];
                Iterator lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Long) lIter.next()).longValue();
                    i++;
                }
                return lArr;
            }
            else if("F".equals(lArrClassName))
            {
                float[] lArr = new float[lArrSize];
                Iterator lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Float) lIter.next()).floatValue();
                    i++;
                }
                return lArr;
            }
            else if("D".equals(lArrClassName))
            {
                double[] lArr = new double[lArrSize];
                Iterator lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    lArr[i] = ((Double) lIter.next()).doubleValue();
                    i++;
                }
                return lArr;
            }
            else
            {
                final String lMsg = "Unknown primitive array type: " + lArrClassName;
                throw new  MapperException(lMsg);
            }
        }
        else
        {
            try
            {
                Class lComponentClass = Class.forName(lArrClassName);
                Object lArr = Array.newInstance(lComponentClass, lArrSize);
                Iterator lIter = lElements.iterator();
                int i = 0;
                while(lIter.hasNext())
                {
                    Array.set(lArr, i, lIter.next());
                    i++;
                }
                return lArr;
            }
            catch(ClassNotFoundException e)
            {
                final String lMsg = "Exception while trying to unmarshall an array of JavaObjects: " + lArrClassName;
                throw new  MapperException(lMsg);
            }
        }
	}
}
