package com.sdicons.json.serializer.helper.impl;

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
import com.sdicons.json.serializer.helper.MarshallHelper;
import com.sdicons.json.serializer.marshall.MarshallException;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;

import java.util.*;
import java.beans.*;
import java.lang.reflect.*;

public class ObjectHelper
implements MarshallHelper
{
    public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();
        aObjectElement.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, lElements);

        try
        {
            Class lClass = aObj.getClass();
            PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(lClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
            for (PropertyDescriptor aLPropDesc : lPropDesc)
            {
                Method lReader = aLPropDesc.getReadMethod();
                Method lWriter = aLPropDesc.getWriteMethod();
                String lPropName = aLPropDesc.getName();

                // Only serialize if the property is READ-WRITE.
                if (lReader != null && lWriter != null)
                {
                    lElements.getValue().put(lPropName, aMarshall.marshallImpl(lReader.invoke(aObj), aPool));
                }
            }
        }
        catch(IntrospectionException e)
        {
            final String lMsg = "Error while introspecting JavaBean.";
            throw new MarshallException(lMsg);
        }
        catch(IllegalAccessException e)
        {
            final String lMsg = "Illegal access while trying to fetch a bean property (1).";
            throw new MarshallException(lMsg);
        }
        catch(InvocationTargetException e)
        {
            final String lMsg = "Illegal access while trying to fetch a bean property (2).";
            throw new MarshallException(lMsg);
        }
    }

    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_CLASS);
        String lBeanClassName = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_CLASS)).getValue();

       String lId = null;
        try
        {
            JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_ID);
            lId = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_ID)).getValue();
        }
        catch(Exception eIgnore){}

        try
        {
            Class lBeanClass = Class.forName(lBeanClassName);
            Object lBean;

            lBean = lBeanClass.newInstance();
            if (lId != null) aPool.put(lId, lBean);

            JSONObject lProperties = (JSONObject) aObjectElement.get(JSONMarshall.RNDR_ATTR_VALUE);

            for(String lPropname : lProperties.getValue().keySet())
            {
                // Fetch subelement information.
                JSONObject lSubEl = (JSONObject) lProperties.get(lPropname);
                Object lProp = aMarshall.unmarshallImpl(lSubEl, aPool);

                // Put the property in the bean.
                boolean lFoundWriter = false;
                PropertyDescriptor[] lPropDesc = Introspector.getBeanInfo(lBeanClass, Introspector.USE_ALL_BEANINFO).getPropertyDescriptors();
                for(PropertyDescriptor aLPropDesc : lPropDesc)
                {
                    if(aLPropDesc.getName().equals(lPropname))
                    {
                        lFoundWriter = true;
                        Method lWriter = aLPropDesc.getWriteMethod();
                        if(lWriter == null)
                        {
                            final String lMsg = "Could not find a setter for prop: " + lPropname + " in class: " + lBeanClassName;
                            throw new MarshallException(lMsg);
                        }
                        lWriter.invoke(lBean, lProp);
                        break;
                    }
                }

                if(!lFoundWriter)
                {
                    final String lMsg = "Could not find a setter for prop: " + lPropname + " in class: " + lBeanClassName;
                    throw new MarshallException(lMsg);
                }
            }

            return lBean;
        }
        catch (ClassNotFoundException e)
        {
            final String lMsg = "Could not find JavaBean class: " + lBeanClassName;
            throw new MarshallException(lMsg);
        }
        catch (IllegalAccessException e)
        {
            final String lMsg = "IllegalAccessException while trying to instantiate bean: " + lBeanClassName;
            throw new MarshallException(lMsg);
        }
        catch (InstantiationException e)
        {
            final String lMsg = "InstantiationException while trying to instantiate bean: " + lBeanClassName;
            throw new MarshallException(lMsg);
        }
        catch (IntrospectionException e)
        {
            final String lMsg = "IntrospectionException while trying to fill bean: " + lBeanClassName;
            throw new MarshallException(lMsg);
        }
        catch (InvocationTargetException e)
        {
            final String lMsg = "InvocationTargetException while trying to fill bean: " + lBeanClassName;
            throw new MarshallException(lMsg);
        }
    }

    public Class getHelpedClass()
    {
        return Object.class;
    }
}
