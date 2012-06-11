package com.sdicons.json.mapper.helper.impl;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.helper.JSONMap;
import com.sdicons.json.helper.JSONConstruct;
import com.sdicons.json.mapper.helper.SimpleMapperHelper;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ObjectMapperDirect
implements SimpleMapperHelper
{
    public Class getHelpedClass()
    {
        return Object.class;
    }

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

    protected Method getAnnotatedSerializingMethod(Class aClass)
    {
        // Check if we have an annotated class.
        for(Method lMethod : aClass.getDeclaredMethods())
        {
            if(lMethod.isAnnotationPresent(JSONMap.class))
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

    // Accessing a shared object should be synced.
    protected synchronized AnnotatedMethods getAnnotatedMethods(Class aClass)
    throws MapperException
    {
        AnnotatedMethods lResult = annotatedPool.get(aClass);
        if(lResult == null)
        {
            final Constructor lCons = getAnnotatedConstructor(aClass);
            final Method lMeth = getAnnotatedSerializingMethod(aClass);

            if((lMeth == null && lCons != null) || (lMeth != null && lCons == null))
                throw new MapperException(String.format("ObjectMapperDirect found inconsistency in class: '%1$s'. If annotated methods are used, it should contain both @JSONConstruct and @JSONMap together.", aClass.getClass().getName()));

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

    public Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {
        if(!aValue.isObject()) throw new MapperException("ObjectMapperDirect cannot map: " + aValue.getClass().getName());
        JSONObject aObject = (JSONObject) aValue;

        try
        {            
            // The result can be constructed in two ways, an annotated constructor
            // or the default constructor. At this point we don't know which of the two
            // methods will be used.
            Object lResult;

            // Find info about the annotated methods.
            final AnnotatedMethods lAnnotated = getAnnotatedMethods(aRequestedClass);
            if (lAnnotated.cons != null)
            {
                // Let's get the field values to pass to the constructor
                int lCnt = lAnnotated.cons.getParameterTypes().length;
                final Object[] lAttrs = new Object[lCnt];
                for (int i = 0; i < lCnt; i++)
                {
                    final String lFldName = "cons-" + i;
                    final JSONValue lSubEl = aObject.get(lFldName);

                    try
                    {
                        lAttrs[i] = JSONMapper.toJava(lSubEl, lAnnotated.cons.getParameterTypes()[i]);
                    }
                    catch(MapperException e)
                    {
                        throw new MapperException(String.format("ObjectMapperDirect error while deserializing. Error while calling the @JSONConstruct constructor in class: '%1$s' on parameter nr: %2$d with a value of class: '%3$s'.", aRequestedClass.getName(), i, lAnnotated.cons.getParameterTypes()[i].getName()), e);
                    }
                }

                // Create a new instance using the annotated constructor.
                try
                {
                    lResult = lAnnotated.cons.newInstance(lAttrs);
                }
                catch(Exception e)
                {
                    throw new MapperException(String.format("ObjectMapperDirect error while deserializing. Tried to instantiate an object (using annotated constructor) of class: '%1$s'.", aRequestedClass.getName()), e);
                }
            }
            else
            {
                // Just use the default constructor.
                lResult = aRequestedClass.newInstance();
            }

            final List<Field> lJavaFields = getFieldInfo(aRequestedClass);
            for (String lPropname : aObject.getValue().keySet())
            {
                // Fetch subelement information.
                final JSONValue lSubEl = aObject.get(lPropname);
                for (Field lFld : lJavaFields)
                {
                    // Write the property.
                    if (lFld.getName().equals(lPropname))
                    {
                        Object lFldValue;
                        final Type lGenType = lFld.getGenericType();
                        if(lGenType instanceof ParameterizedType)
                        {
                            lFldValue = JSONMapper.toJava(lSubEl, (ParameterizedType) lGenType);                            
                        }
                        else
                        {
                            lFldValue = JSONMapper.toJava(lSubEl, lFld.getType());
                        }

                        try
                        {
                            lFld.setAccessible(true);
                            lFld.set(lResult, lFldValue);
                            break;
                        }
                        catch (Exception e)
                        {
                            throw new MapperException(String.format("ObjectMapperDirect error while deserializing. Type error while trying to set the field: '%1$s' in class: '%2$s' with a value of class: '%3$s'.", lFld.getName(), aRequestedClass.getName(), lFldValue.getClass().getName()), e);
                        }
                    }
                }
            }

            // First we have to cope with the special case of "readResolve" objects.
            // It is described in the official specs of the serializable interface.
            ////////////////////////////////////////////////////////////////////////////
            if (lResult instanceof Serializable)
            {
                try
                {
                    final Method lReadResolve = lResult.getClass().getDeclaredMethod("readResolve");
                    if (lReadResolve != null)
                    {
                        lReadResolve.setAccessible(true);
                        lResult = lReadResolve.invoke(lResult);
                    }
                }
                catch (NoSuchMethodException e)
                {
                    // Do nothing, just continue normal operation.
                }
                catch (Exception e)
                {
                    throw new MapperException(String.format("ObjectMapperDirect error while creating java object. Tried to invoke 'readResolve' on instance of class: '%1$s'.", lResult.getClass().getName()), e);
                }
            }
            ////////////////////////////////////////////////////////////////////////////

            return lResult;
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
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        // We will render the bean properties as the elements of a JSON object.
        final JSONObject lElements = new JSONObject();

        // First we have to cope with the special case of "writeReplace" objects.
        // It is described in the official specs of the serializable interface.
        ////////////////////////////////////////////////////////////////////////////
        if(aPojo instanceof Serializable)
        {
            try
            {
                final Method lWriteReplace = aPojo.getClass().getDeclaredMethod("writeReplace");
                if(lWriteReplace != null)
                {
                    lWriteReplace.setAccessible(true);
                    return JSONMapper.toJSON(lWriteReplace.invoke(aPojo));
                }
            }
            catch(NoSuchMethodException e)
            {
                // Do nothing, just continue normal operation.
            }
            catch(Exception e)
            {
                throw new MapperException(String.format("ObjectMapperDirect error while trying to invoke 'writeReplace' on instance of class: '%1$s'.", aPojo.getClass().getName()), e);
            }
        }
        ////////////////////////////////////////////////////////////////////////////

        Class lJavaClass = aPojo.getClass();
        final List<Field> lJavaFields = getFieldInfo(lJavaClass);
        // Find info about the annotated methods.
        final AnnotatedMethods lAnnotated = getAnnotatedMethods(lJavaClass);
        
        for(Field lFld : lJavaFields)
        {
            try
            {
                lFld.setAccessible(true);
                final JSONValue lFieldVal = JSONMapper.toJSON(lFld.get(aPojo));
                lElements.getValue().put(lFld.getName(), lFieldVal);
            }
            catch(Exception e)
            {
                throw new MapperException(String.format("ObjectMapperDirect error while serializing. Error while reading field: '%1$s' from instance of class: '%2$s'.", lFld.getName(), lJavaClass.getName()), e);
            }
        }

        // Check if we have an annotated class.
        if (lAnnotated.serialize != null)
        {
            Object[] lVals;
            try
            {
                lVals = (Object[]) lAnnotated.serialize.invoke(aPojo);
            }
            catch(Exception e)
            {
                throw new MapperException(String.format("ObjectMapperDirect error while serializing. Error while invoking the @JSONMap method called '%1$s(...)' on an instance of class: '%2$s'.", lAnnotated.serialize.getName(), lJavaClass.getName()), e);
            }

            int i = 0;
            try
            {
                for (Object lVal : lVals)
                {
                    final JSONValue lFieldVal = JSONMapper.toJSON(lVal);
                    lElements.getValue().put("cons-" + i, lFieldVal);
                    i++;
                }
            }
            catch(MapperException e)
            {
                throw new MapperException(String.format("ObjectMapperDirect error while serializing. Error while serializing element nr %1$d from the @JSONMap method: '%2$s(...)' on instance of class: '%3$s'.", i, lAnnotated.serialize.getName(), lJavaClass.getName()), e);
            }
        }

        return lElements;
    }
}
