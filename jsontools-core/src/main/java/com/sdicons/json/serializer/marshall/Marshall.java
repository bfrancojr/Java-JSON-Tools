package com.sdicons.json.serializer.marshall;

import com.sdicons.json.model.JSONObject;

/*
    JSONTools - Java JSON Tools
    Copyright (C) 2006 S.D.I.-Consulting BVBA
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
 * This interface represents the functionality to convert a JSON representation to/from a
 * Java representation. The serializer's goal is to preserve as much Java structure as
 * possible: primitive types, arrays, object clusters containing recursive references and so on.
 * As a consequence, the generated JSON is rather terse. The serializer is the best choice in a
 * Java centric application where you want to write some object structures to JSON in a readable form.
 * If the emphasis is on clean JSON, take a look at the Mapper tool, which does not support all Java constructs
 * but which emits nice JSON which can easily be parsed in e.g. Javascript.
 *
 * * The main difference between the serializer and the mapper is that the serializer keeps as much
 * type information and structure information in the JSON data where the mapper uses the type information
 * in the provided Java classes to interprete the JSON data.
 */
public interface Marshall
{
    /**
     * Convert a boolean primitive to JSON.
     * @param aValue
     * @return An JSON representation of the boolean primitive.
     */
    JSONObject marshall(boolean aValue);

    /**
     * Convert a byte primitive to JSON.
     * @param aValue
     * @return An JSON representation of the byte primitive.
     */
    JSONObject marshall(byte aValue);

    /**
     * Convert a short primitive to JSON.
     * @param aValue
     * @return An JSON representation of the short primitive.
     */
    JSONObject marshall(short aValue);

    /**
     * Convert a char primitive to JSON.
     * @param aValue
     * @return An JSON representation of the char primitive.
     */
    JSONObject marshall(char aValue);

    /**
     * Convert an int primitive to JSON.
     * @param aValue
     * @return An JSON representation of the int primitive.
     */
    JSONObject marshall(int aValue);

    /**
     * Convert a long primitive to JSON.
     * @param aValue
     * @return An JSON representation of the long primitive.
     */
    JSONObject marshall(long aValue);

    /**
     * Convert a float primitive to JSON.
     * @param aValue
     * @return An JSON representation of the float primitive.
     */
    JSONObject marshall(float aValue);

    /**
     * Convert a double primitive to JSON.
     * @param aValue
     * @return An JSON representation of the double primitive.
     */
    JSONObject marshall(double aValue);

    /**
     * Convert a Java object to JSON.
     * @param aObj
     * @return The JSON representation of the Java object.
     * @throws MarshallException An error occured while converting the Java object to JSON.
     */
    JSONObject marshall(final Object aObj)
    throws MarshallException;

    /**
     * Convert a JSON representation to the Java primitive or reference.
     * @param aElement
     * @return The Java representation of the JSON. This value can represent a Java primitive value or
     *         it can represent a Java reference.
     * @throws MarshallException An error occured while trying to convert the JSON representation into a
     *         Java representation.
     */
    MarshallValue unmarshall(final JSONObject aElement)
    throws MarshallException;
}