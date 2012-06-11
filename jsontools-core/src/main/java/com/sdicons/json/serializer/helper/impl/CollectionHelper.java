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

import com.sdicons.json.model.JSONArray;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.serializer.helper.MarshallHelper;
import com.sdicons.json.serializer.marshall.JSONMarshall;
import com.sdicons.json.serializer.marshall.MarshallException;

import java.util.Collection;
import java.util.HashMap;

public class CollectionHelper
implements MarshallHelper
{
    public CollectionHelper()
    {
    }

    public void renderValue(Object aObj, JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        // We create a new JSON array where we will collect the elements of the
        // collection. We attach this new array as the parent object value.
        final JSONArray lArray = new JSONArray();
        aObjectElement.getValue().put(JSONMarshall.RNDR_ATTR_VALUE, lArray);

        // We iterate through the elements of the collection, render these as
        // JSON values and put these values in the array created above.
        final Collection lCollection = (Collection) aObj;
        for(Object lColEl : lCollection)
        {
            lArray.getValue().add(aMarshall.marshallImpl(lColEl, aPool));
        }
    }

    public Object parseValue(JSONObject aObjectElement, JSONMarshall aMarshall, HashMap aPool)
    throws MarshallException
    {
        final JSONArray lArray = (JSONArray) aObjectElement.getValue().get(JSONMarshall.RNDR_ATTR_VALUE);

        JSONMarshall.requireStringAttribute(aObjectElement, JSONMarshall.RNDR_ATTR_CLASS);
        String lCollectionClassName = ((JSONString) aObjectElement.get(JSONMarshall.RNDR_ATTR_CLASS)).getValue();

        try
        {
            Class lCollectionClass = Class.forName(lCollectionClassName);
            Collection lCollection;

            lCollection = (Collection) lCollectionClass.newInstance();
//            if (lId != null) aPool.put(lId, lCollection);

            for(JSONValue lVal : lArray.getValue())
            {
                lCollection.add(aMarshall.unmarshallImpl((JSONObject)lVal, aPool));
            }
            return lCollection;
        }
        catch (IllegalAccessException e)
        {
            final String lMsg = "IllegalAccessException while trying to instantiate collection: " + lCollectionClassName;
            throw new MarshallException(lMsg);
        }
        catch (InstantiationException e)
        {
            final String lMsg = "InstantiationException while trying to instantiate collection: " + lCollectionClassName;
            throw new MarshallException(lMsg);
        }
        catch (ClassNotFoundException e)
        {
            final String lMsg = "ClassNotFoundException while trying to instantiate collection: " + lCollectionClassName;
            throw new MarshallException(lMsg);
        }
    }

    public Class getHelpedClass()
    {
        return Collection.class;
    }
}
