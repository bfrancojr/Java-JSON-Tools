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
import com.sdicons.json.mapper.helper.ComplexMapperHelper;
import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONValue;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CollectionMapper
implements ComplexMapperHelper
{
    public Class getHelpedClass()
    {
        return Collection.class;
    }

    public Object toJava(JSONValue aValue, Class aRequestedClass)
    throws MapperException
    {
        return this.toJava(aValue, aRequestedClass, new Type[0]);
    }

    public Object toJava(JSONValue aValue, Class aRawClass, Type[] aTypes)
    throws MapperException
    {
        if (!aValue.isArray()) throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        if (!Collection.class.isAssignableFrom(aRawClass))
            throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        JSONArray aObject = (JSONArray) aValue;

        Collection lCollObj;

        try
        {
            // First we try to instantiate the correct
            // collection class.
            if(aRawClass.isInterface()){
            	//we still can't deal with some unusual interfaces. 
            	if(aRawClass==Set.class){
            		lCollObj = new HashSet();
            	}else if(aRawClass==SortedSet.class){
            		lCollObj = new TreeSet();
            	}else{
            		lCollObj = new LinkedList();
            	}
            }else{
            	lCollObj = (Collection) aRawClass.newInstance();	
            }
        }
        catch (Exception e)
        {
            // If the requested class cannot create an instance because
            // it is abstract, or an interface, we use a default fallback class.
            // This solution is far from perfect, but we try to make the mapper
            // as convenient as possible.
            lCollObj = new LinkedList();
        }

        if(aTypes.length == 0)
        {
            // Simple, raw collection.
            for (JSONValue lVal : aObject.getValue())
            {
                lCollObj.add(JSONMapper.toJava(lVal));
            }
        }
        else if(aTypes.length == 1)
        {
            // Generic collection, we can make use of the type of the elements.            
            for (JSONValue lVal : aObject.getValue())
            {
                
                if(aTypes[0] instanceof Class)
                	lCollObj.add(JSONMapper.toJava(lVal, (Class) aTypes[0]));
                else
                	lCollObj.add(JSONMapper.toJava(lVal, (ParameterizedType) aTypes[0]));                	               
            }
        }
        else
        {
            // Not possible, a collection cannot have more than two types for
            // its contents.
            throw new MapperException("CollectionMapper cannot map: " + aValue.getClass().getName());
        }

        return lCollObj;
    }

    public JSONValue toJSON(Object aPojo)
    throws MapperException
    {
        JSONArray lArray = new JSONArray();
        if(! Collection.class.isAssignableFrom(aPojo.getClass())) throw new MapperException("CollectionMapper cannot map: " + aPojo.getClass().getName());

        Collection lColl = (Collection) aPojo;
        for(Object lEl : lColl)
        {
            lArray.getValue().add(JSONMapper.toJSON(lEl));
        }
        return lArray;
    }
}
