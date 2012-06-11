package com.sdicons.json.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

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

/**
 * Base class for all JSON representations.
 */
public abstract class JSONValue implements Serializable
{
    private String streamName;
    private int line = 0;
    private int col = 0;
    private Object data = null;

    /**
     * Get the line number in the textual representation where this JSON value was encountered.
     * Can be handy for post processing tools which want to give an indication tot he user
     * where in the representation some condition occurred.
     * @return   The line number.
     */
    public int getLine()
    {
        return line;
    }

    /**
     * Set The position where this JSON value occurred during parsing. This method is called
     * by the parser. Probably no need to call this yourself.
     * @param line
     * @param col
     */
    public void setLineCol(int line, int col)
    {
        this.line = line;
        this.col = col;
    }

    /**
     * Get information about the stream in which the value occurred.
     * Its purpose is to identify in which stream the error occurred if multiple streams are being parsed
     * and the error reports are being collected in a single report.
     * @return The name of the stream.
     */
    public String getStreamName()
    {
        return streamName;
    }

    /**
     * Fill in information about the stream.
     * @param streamName
     */
    public void setStreamName(String streamName)
    {
        this.streamName = streamName;
    }

    /**
     * Get the column number in the textual representation where this JSON value was encountered.
     * Can be handy for post processing tools which want to give an indication tot he user
     * where in the representation some condition occurred.
     * @return   The line number.
     */
    public int getCol()
    {
        return col;
    }

    /**
     * Get user data.
     * @return  The user data.
     */
    public Object getData()
    {
        return data;
    }

    /**
     * Set user data. The user of the library can link whatever information is useful in the
     * user context to a JSON object in order to track back to the original JSON data.
     * The JSON tools do not use this field, it is for the user of the library.
     * @param data
     */
    public void setData(Object data)
    {
        this.data = data;
    }

    /**
     * Check if this value represents a "simple" value, meaning:
     * a boolean, a number, a string or null.
     * @return  An assertion that the value is "simple".
     */
    public boolean isSimple()
    {
        return this instanceof JSONSimple;
    }

    /**
     * Check if this value represents a "complex" value, meaning:
     * an array, an object.
     * @return An assertion that the value is "complex".
     */
    public boolean isComplex()
    {
        return this instanceof JSONComplex;
    }

    /**
     * Check if this value represents an array.
     * @return An assertion that the value is an array.
     */
    public boolean isArray()
    {
        return this instanceof JSONArray;
    }

    /**
     * Check if this value represents a JSON object.
     * @return An assertion that the value is a JSON object.
     */
    public boolean isObject()
    {
        return this instanceof JSONObject;
    }

    /**
     * Check if this value is a number, meaning:
     * an integer or a decimal.
     * @return An assertion that the value is a number.
     */
    public boolean isNumber()
    {
        return this instanceof JSONNumber;
    }

    /**
     * Check if this value is a decimal.
     * @return An assertion that the value is a decimal.
     */
    public boolean isDecimal()
    {
        return this instanceof JSONDecimal;
    }

    /**
     * Check if this value is an integer.
     * @return An assertion that the value is an integer.
     */
    public boolean isInteger()
    {
        return this instanceof JSONInteger;
    }

    /**
     * Check if this value represents a JSON null value.
     * @return An assertion that the value is the JSON null value.
     */
    public boolean isNull()
    {
        return this instanceof JSONNull;
    }

    /**
     * Check if this value represents a JSON boolean value.
     * @return An assertion that the value is a JSON boolean.
     */
    public boolean isBoolean()
    {
        return this instanceof JSONBoolean;
    }

    /**
     * Check if this value represents a JSON string.
     * @return An assertion that the value is a JSON string.
     */
    public boolean isString()
    {
        return this instanceof JSONString;
    }

    /**
     * Convert the JSON value into a string representation (JSON representation).
     * @param pretty Indicating if the print should be made pretty (human readers) or compact (transmission or storage).
     * @return A JSON representation.
     */
    public String render(boolean pretty)
    {
        return render(pretty, "");
    }

    /**
     * Convert the JSON value into a string representation (JSON representation).
     * @param pretty Indicating if the print should be made pretty (human readers) or compact (transmission or storage).
     * @param indent Starting indent.
     * @return A JSON representation.
     */
    protected abstract String render(boolean pretty, String indent);

    /**
     * This method strips all JSON related information and returns pure Java objects.
     * It can be handy if you know in advance which objects to expect or if you already did validation.
     * @return Pure Java object, stripped of all JSON information.
     */
    public abstract Object strip();

    /**
     * This method is the reverse of a strip, it converts a construction of Java objects to a JSON decorated
     * composition.
     * @param anObject
     * @return A JSONValue representing the object.
     * @throws IllegalArgumentException If a conversion cannot be done.
     */
    public static JSONValue decorate(Object anObject)
    {
        if(anObject == null)
        {
            return new JSONNull();
        }
        else if(anObject instanceof Boolean)
        {
            return anObject.equals(Boolean.TRUE)?JSONBoolean.TRUE:JSONBoolean.FALSE;
        }
        else if(anObject instanceof BigDecimal)
        {
            return new JSONDecimal((BigDecimal) anObject);
        }
        else if(anObject instanceof BigInteger)
        {
            return new JSONInteger((BigInteger) anObject);
        }
        else if(anObject instanceof String)
        {
            return new JSONString((String) anObject);
        }
        else if(anObject instanceof List)
        {
            final JSONArray lArray = new JSONArray();
            for(Object lElement : ((List) anObject))
            {
                lArray.getValue().add(decorate(lElement));
            }
            return lArray;
        }
        else if(anObject instanceof Map)
        {
            final JSONObject lObj = new JSONObject();
            for(Object lKey: ((Map)anObject).keySet())
            {
                if(lKey instanceof String)
                {
                    lObj.getValue().put((String) lKey, decorate(((Map)anObject).get(lKey)));
                }
                else throw new IllegalArgumentException("HashMap contains a key that is not a String: " + lKey);
            }
            return lObj;
        }
        else
        {
            throw new IllegalArgumentException("Cannot convert this object to a JSONValue: " + anObject);
        }
    }
}
