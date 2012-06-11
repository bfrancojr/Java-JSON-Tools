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

import com.sdicons.json.helper.JSONConstruct;
import com.sdicons.json.helper.JSONSerialize;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.serializer.helper.MarshallHelper;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ObjectHelperDirect
implements MarshallHelper
{
    private Map<Class, AnnotatedMethods> annotatedPool = new HashMap<Class, AnnotatedMethods>();

    private static class AnnotatedMethods
    {
        public Constructor cons;
        public Method serialize;

        public AnnotatedMethods(Constructor aCons, Method aSerialize)
        {
            cons = aCons;
            serialize = aSerialize;
        }
    }

    // Accessing a shared object should be synced.
    protected synchronized AnnotatedMethods getAnnotatedMethods(Class aClass)
    throws MarshallException
    {
        AnnotatedMethods lResult = annotatedPool.get(aClass);
        if(lResult == null)
        {
            final Constructor lCons = getAnnotatedConstructor(aClass);
            final Method lMeth = getAnnotatedSerializingMethod(aClass);

            if((lMeth == null && lCons != null) || (lMeth != null && lCons == null))
                throw new MarshallException(String.format("ObjectHelperDirect found inconsistency in class: '%1$s'. If annotated methods are used, it should contain both @HessianConstruct and @HessianSerialize together.", aClass.getClass().getName()));

            lResult = new AnnotatedMethods(lCons, lMeth);
            annotatedPool.put(aClass, lResult);
        }
        return lResult;
    }

    protected List<Field> getFieldInfo(Class aClass)
    {
        final List<Field> lJavaFields = new LinkedList<Field>();
        Class lClassWalker = aClass;
        while (lClassWalker != null)
        {
            final Field[] lClassFields = lClassWalker.getDeclaredFields();
            for (Field lFld : lClassFields)
            {
                int lModif = lFld.getModifiers();
                if (!Modifier.isTransient(lModif) &&
                        !Modifier.isAbstract(lModif) &&
                        !Modifier.isStatic(lModif) &&
                        !Modifier.isFinal(lModif))
                {
                    lFld.setAccessible(true);
                    lJavaFields.add(lFld);
                }
            }
            lClassWalker = lClassWalker.getSuperclass();
        }
        return lJavaFields;
    }

    protected Method getAnnotatedSerializingMethod(Class aClass)
    {
        // Check if we have an annotated class.
        for(Method lMethod : aClass.getDeclaredMethods())
        {
            if(lMethod.isAnnotationPresent(JSONSerialize.class))
            {
                lMethod.setAccessible(true);
                return lMethod;
            }
        }
        return null;
    }

    protected Constructor getAnnotatedConstructor(Class aClass)
    {
        //Check if we have a class with an annotated constructor
        final Constructor[] lConstructors = aClass.getDeclaredConstructors();
        for(Constructor lCons : lConstructors)
            if(lCons.isAnnotationPresent(JSONConstruct.class))
            {
                // Found the constructor we are
                // looking for.
                lCons.setAccessible(true);
                return lCons;
            }
        return null;
    }

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        // First we have to cope with the special case of "writeReplace" objects.
        // It is described in the official specs of the serializable interface.
        ////////////////////////////////////////////////////////////////////////////
        if(aObj instanceof Serializable)
        {
            try
            {
                final Method lWriteReplace = aObj.getClass().getDeclaredMethod("writeReplace");
                if(lWriteReplace != null)
                {
                    lWriteReplace.setAccessible(true);
                    final JSONObject lOele = aMarshall.marshallImpl(lWriteReplace.invoke(aObj), aPool);
                    aObjectElement.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, lOele);
                    return;
                }
            }
            catch(NoSuchMethodException e)
            {
                // Do nothing, just continue normal operation.
            }
            catch(Exception e)
            {
                throw new MarshallException(String.format("ObjectHelperDirect error while trying to invoke 'writeReplace' on instance of class: '%1$s'.", aObj.getClass().getName()));
            }
        }
        ////////////////////////////////////////////////////////////////////////////

        // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();
        aObjectElement.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, lElements);

        Class lClass = aObj.getClass();
        // Fetch the field  information, we need this in several places.
        final List<Field> lJavaFields = getFieldInfo(lClass);
        // Find info about the annotated methods.
        final AnnotatedMethods lAnnotated = getAnnotatedMethods(lClass);

        // Check if we have an annotated class.
        if(lAnnotated.serialize != null)
        {
            Object[] lVals;
            try
            {
                lVals = (Object[]) lAnnotated.serialize.invoke(aObj);
            }
            catch(Exception e)
            {
                throw new MarshallException(String.format("ObjectHelperDirect error while serializing. Error while invoking the @HessianSerialize method called '%1$s(...)' on an instance of class: '%2$s'.", lAnnotated.serialize.getName(), lClass.getName()));
            }

            int i = 0;
            try
            {
                for(Object lVal : lVals)
                {
                    lElements.getValue().put("cons-" + i, aMarshall.marshallImpl(lVal, aPool));
                    i++;
                }
            }
            catch(MarshallException e)
            {
                throw new MarshallException(String.format("ObjectHelperDirect error while serializing. Error while serializing element nr %1$d from the @HessianSerialize method: '%2$s(...)' on instance of class: '%3$s'.", i, lAnnotated.serialize.getName(), lClass.getName()));
            }
        }

        for(Field lFld : lJavaFields)
        {
            try
            {
                lFld.setAccessible(true);
                lElements.getValue().put(lFld.getName(), aMarshall.marshallImpl(lFld.get(aObj), aPool));
            }
            catch(MarshallException e)
            {
                throw new MarshallException(String.format("ObjectHelperDirect error while serializing. Error while serializing field: '%1$s' from instance of class: '%2$s'.", lFld.getName(), lClass.getName()));
            }
            catch(Exception e)
            {
                throw new MarshallException(String.format("ObjectHelperDirect error while serializing. Error while reading field: '%1$s' from instance of class: '%2$s'.", lFld.getName(), lClass.getName()));
            }
        }        
    }

    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_CLASS);
        final String lBeanClassName = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_CLASS)).getValue();

        String lId = null;
        try
        {
            JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_ID);
            lId = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_ID)).getValue();
        }
        catch(Exception eIgnore){}

        try
        {
            final Class lBeanClass = Class.forName(lBeanClassName);
            final List<Field> lJavaFields = getFieldInfo(lBeanClass);
            final AnnotatedMethods lAnnotated = getAnnotatedMethods(lBeanClass);
            final JSONObject lProperties = (JSONObject) aObjectElement.get(JSONMarshall.RNDR_ATTR_VALUE);

            Object lBean;

            if (lAnnotated.cons != null)
            {
                // Let's get the field values to pass to the constructor
                int lCnt = lAnnotated.cons.getParameterTypes().length;
                final Object[] lAttrs = new Object[lCnt];
                for (int i = 0; i < lCnt; i++)
                {
                    final String lFldName = "cons-" + i;
                    // Fetch subelement information.
                    JSONObject lSubEl = (JSONObject) lProperties.get(lFldName);

                    try
                    {
                        lAttrs[i] = aMarshall.unmarshallImpl(lSubEl, aPool);
                    }
                    catch(MarshallException e)
                    {
                        throw new MarshallException(String.format("ObjectHelperDirect error while deserializing. Error while calling the @JSONConstruct constructor in class: '%1$s' on parameter nr: %2$d with a value of class: '%3$s'.", lBeanClass.getName(), i, lSubEl.getClass().getName()));
                    }
                }

                // Create a new instance using the annotated constructor.
                try
                {
                    lBean = lAnnotated.cons.newInstance(lAttrs);
                }
                catch(Exception e)
                {
                    throw new MarshallException(String.format("ObjectHelperDirect error while deserializing. Tried to instantiate an object (using annotated constructor) of class: '%1$s'.", lBeanClass.getName()));
                }
            }
            else
            {
                // Create a new instance using the default constructor
                lBean = lBeanClass.newInstance();
            }

            if (lId != null) aPool.put(lId, lBean);

            int i = 0;
            for(String lPropname : lProperties.getValue().keySet())
            {
                for (Field lFld : lJavaFields)
                {
                    // Write the property.
                    if (lFld.getName().equals(lPropname))
                    {
                        JSONObject lSubEl = (JSONObject) lProperties.get(lPropname);
                        final Object lFldValue = aMarshall.unmarshallImpl(lSubEl, aPool);

                        try
                        {
                            lFld.setAccessible(true);
                            lFld.set(lBean, lFldValue);
                            i++;
                            break;
                        }
                        catch (Exception e)
                        {
                            throw new MarshallException(String.format("ObjectHelperDirect error while deserializing. Type error while trying to set the field: '%1$s' in class: '%2$s' with a value of class: '%3$s'.", lFld.getName(), lBeanClass.getName(), lFldValue.getClass().getName()));
                        }
                    }
                }
            }

            // First we have to cope with the special case of "readResolve" objects.
            // It is described in the official specs of the serializable interface.
            ////////////////////////////////////////////////////////////////////////////
            if (lBean instanceof Serializable)
            {
                try
                {
                    final Method lReadResolve = lBean.getClass().getDeclaredMethod("readResolve");
                    if (lReadResolve != null)
                    {
                        lReadResolve.setAccessible(true);
                        lBean = lReadResolve.invoke(lBean);
                        // Replace previous instance of the stored reference.
                        aPool.put(aObjectElement, lBean);
                    }
                }
                catch (NoSuchMethodException e)
                {
                    // Do nothing, just continue normal operation.
                }
                catch (Exception e)
                {
                    throw new MarshallException(String.format("ObjectHelperDirect error while deserializing. Tried to invoke 'readResolve' on instance of class: '%1$s'.", lBean.getClass().getName()));
                }
            }
            ////////////////////////////////////////////////////////////////////////////

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
    }

    public Class getHelpedClass()
    {
        return Object.class;
    }
}
