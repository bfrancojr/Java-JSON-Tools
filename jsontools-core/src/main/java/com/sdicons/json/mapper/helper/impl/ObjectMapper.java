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

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ObjectMapper
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return Object.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {
        if(!aValue.isObject()) throw new MapperException("ObjectMapper cannot map: " + aValue.getClass().getName());
        JSONObject aObject = (JSONObject) aValue;

        try
        {
            final Object lBean = aRequestedClass.newInstance();

            for (String lPropname : aObject.getValue().keySet())
            {
                // Fetch subelement information.
                final JSONValue lSubEl = aObject.get(lPropname);

                // Put the property in the bean.
                boolean lFoundWriter = false;
                PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(aRequestedClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
                for (PropertyDescriptor aLPropDesc : lPropDesc)
                {
                    if (aLPropDesc.getName().equals(lPropname))
                    {
                        lFoundWriter = true;
                        final Method lWriter = aLPropDesc.getWriteMethod();
                        if (lWriter == null)
                        {
                        	//Ignore the case of no setter                        	
                            // final String lMsg = "Could not find a setter for prop: " + lPropname + " in class: " + aRequestedClass;
                            // System.out.println("WARNING:"+lMsg);
                            break;
                            //throw new MapperException(lMsg);
                        }
                        else
                        {
                            Object lProp;
                            Type[] lTypes = lWriter.getGenericParameterTypes();
                            if (lTypes.length == 1 && (lTypes[0] instanceof ParameterizedType))
                            {
                                // We can make use of the extra type information of the parameter of the
                                // seter. This extra type information can be exploited by the mapper
                                // to produce a more fine grained mapping.
                                lProp = JSONMapper.toJava(lSubEl, (ParameterizedType) lTypes[0]);
                            }
                            else
                            {
                                // We cannot use extra type information, we fall back on the
                                // raw class information.
                                lProp = JSONMapper.toJava(lSubEl, aLPropDesc.getPropertyType());
                            }

                            lWriter.invoke(lBean, lProp);
                        }
                        break;
                    }
                }

                if (!lFoundWriter)
                {
                    final String lMsg = "Could not find a setter for prop: " + lPropname + " in class: " + aRequestedClass;
                    throw new MapperException(lMsg);
                }
            }
            return lBean;
        }
        catch (IllegalAccessException e)
        {
            final String lMsg = "IllegalAccessException while trying to instantiate bean: " + aRequestedClass;
            throw new MapperException(lMsg);
        }
        catch (InstantiationException e)
        {
            final String lMsg = "InstantiationException while trying to instantiate bean: " + aRequestedClass;
            throw new MapperException(lMsg);
        }
        catch (IntrospectionException e)
        {
            final String lMsg = "IntrospectionException while trying to fill bean: " + aRequestedClass;
            throw new MapperException(lMsg);
        }
        catch (InvocationTargetException e)
        {
            final String lMsg = "InvocationTargetException while trying to fill bean: " + aRequestedClass;
            throw new MapperException(lMsg);
        }
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
         // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();
        String lPropName="";
        try
        {
            Class lClass = aPojo.getClass();
            PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(lClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
            for (PropertyDescriptor aLPropDesc : lPropDesc)
            {
                final Method lReader = aLPropDesc.getReadMethod();
                lPropName = aLPropDesc.getName();                
                // Only serialize if the property is READABLE
                // ignore the properties created by a proxy from Hibernate
                if(lReader!=null&&
                		(lReader.getReturnType().toString().contains("net.sf.cglib.proxy.Callback")||
                		 lReader.getReturnType().toString().contains("org.hibernate.proxy.LazyInitializer"))){
                	continue;
                }
                // Ignore the getClass() for any objects   
                if(lReader!=null&&lPropName.equals("class")){
                	continue;
                }
                
                if (lReader != null)
                {
                    lElements.getValue().put(lPropName, JSONMapper.toJSON(lReader.invoke(aPojo)));
                }
            }
            
            return lElements;
        }
        catch(IntrospectionException e)
        {
            final String lMsg = "Error while introspecting JavaBean." + " Class: "+ aPojo.getClass();
            throw new MapperException(lMsg);
        }
        catch(IllegalAccessException e)
        {
            final String lMsg = "Illegal access while trying to fetch a bean property (1).Property: " + lPropName + " Object: " + aPojo;
            throw new MapperException(lMsg);
        }
        catch(InvocationTargetException e)
        {
            final String lMsg = "Illegal access while trying to fetch a bean property (2).Property: " + lPropName + " Object: " + aPojo;
            throw new MapperException(lMsg);
        }
    }
}