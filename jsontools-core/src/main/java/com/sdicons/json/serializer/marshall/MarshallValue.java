package com.sdicons.json.serializer.marshall;

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
 * This interface represents an object that is the result of converting a JSON structure
 * into a java structure. A dedicated interface is supplied because you cannot know
 * in advance whether the result will be a primitive type or a reference type.
 * This interface lets you investigate which kind of value is returned before you use it.
 */
public interface MarshallValue
{
    final int BOOLEAN=0;
    final int BYTE=1;
    final int SHORT=2;
    final int CHAR=3;
    final int INT=4;
    final int LONG=5;
    final int FLOAT=6;
    final int DOUBLE=7;
    final int REFERENCE=8;

    /**
     * Get the primitive boolean value.
     * @return The unmarshalled boolean value.
     * @throws MarshallException If it is not a boolean representation.
     */
    boolean getBoolean()
    throws MarshallException;

    /**
     * Get the primitive byte value.
     * @return The unmarshalled byte value.
     * @throws MarshallException  If it is not a byte representation.
     */
    byte getByte()
    throws MarshallException;

    /**
     * Get the primitive short value.
     * @return The unmarshalled short value.
     * @throws MarshallException If it is not a short representation.
     */
    short getShort()
    throws MarshallException;

    /**
     * Get the primitive char value.
     * @return The unmarshalled char value.
     * @throws MarshallException If it is not a char representation.
     */
    char getChar()
    throws MarshallException;

    /**
     * Get the primitive int value.
     * @return The unmarshalled int value.
     * @throws MarshallException If it is not an int representation.
     */
    int getInt()
    throws MarshallException;

    /**
     * Get the primitive long value.
     * @return The unmarshalled long value.
     * @throws MarshallException If it is not a long representation.
     */
    long getLong()
    throws MarshallException;

    /**
     * Get the primitive float value.
     * @return The unmarshalled float value.
     * @throws MarshallException If it is not a float representation.
     */
    float getFloat()
    throws MarshallException;

    /**
     * Get the primitive double value.
     * @return The unmarshalled primitive value.
     * @throws MarshallException If it is not a double representation.
     */
    double getDouble()
    throws MarshallException;

    /**
     * Get the reference to a Java object.
     * @return The unmarshalled reference to the Java object.
     * @throws MarshallException If it is not a reference representation.
     */
    Object getReference()
    throws MarshallException;

    /**
     * Get the type of the value so that you can access its value safely.
     * @return One of the getValues BOOLEAN, BYTE, SHORT CHAR, INT, LONG, FLOAT, DOUBLE, REFERENCE.
     */
    int getType();
}