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

import com.sdicons.json.helper.HelperRepository;
import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.serializer.helper.MarshallHelper;
import com.sdicons.json.serializer.helper.impl.*;

import java.util.HashMap;

public class JSONMarshall
implements Marshall
{
    private static final String IDPREFIX = "id";
    private long idCounter = 0;

    public static final String RNDR_NULL = "null";
    public static final String RNDR_OBJ = "O";
    public static final String RNDR_OBJREF = "R";
    public static final String RNDR_PRIM = "P";
    public static final String RNDR_ARR = "A";

    public static final String RNDR_ATTR_ID = "&";
    public static final String RNDR_ATTR_KIND = ">";
    public static final String RNDR_ATTR_TYPE = "t";
    public static final String RNDR_ATTR_VALUE = "=";
    public static final String RNDR_ATTR_CLASS = "c";
    public static final String RNDR_ATTR_REF = "*";

    public static final String RNDR_PRTITYP_BOOLEAN = "boolean";
    public static final String RNDR_PRTITYP_BYTE = "byte";
    public static final String RNDR_PRTITYP_CHAR = "char";
    public static final String RNDR_PRTITYP_SHORT = "short";
    public static final String RNDR_PRTITYP_INT = "int";
    public static final String RNDR_PRTITYP_LONG = "long";
    public static final String RNDR_PRTITYP_FLOAT = "float";
    public static final String RNDR_PRTITYP_DOUBLE = "double";

    public static final String ERR_MISSINGATTR = "Attribute is missing: ";
    public static final String ERR_MISSINGATTRVAL = "Attribute value is missing: ";
    public static final String ERR_MISSINGSTRING = "Attribute is not a string value: ";

    private HelperRepository<MarshallHelper> repo = new HelperRepository<MarshallHelper>();

    {
        repo.addHelper(new ObjectHelper());
//        repo.addHelper(new ObjectHelperDirect());
        repo.addHelper(new StringHelper());
        repo.addHelper(new BooleanHelper());
        repo.addHelper(new ByteHelper());
        repo.addHelper(new ShortHelper());
        repo.addHelper(new IntegerHelper());
        repo.addHelper(new LongHelper());
        repo.addHelper(new FloatHelper());
        repo.addHelper(new DoubleHelper());
        repo.addHelper(new BigIntegerHelper());
        repo.addHelper(new BigDecimalHelper());
        repo.addHelper(new CharacterHelper());
        repo.addHelper(new DateHelper());
        repo.addHelper(new CollectionHelper());
        repo.addHelper(new MapHelper());
        repo.addHelper(new ColorHelper());
        repo.addHelper(new FontHelper());
        repo.addHelper(new EnumHelper());
    }

    public JSONObject marshall(boolean aValue)
    {
        return marshallPrimitive(RNDR_PRTITYP_BOOLEAN, "" + aValue);
    }

    public JSONObject marshall(byte aValue)
    {
        return marshallPrimitive(RNDR_PRTITYP_BYTE, "" + aValue);
    }

    public JSONObject marshall(short aValue)
    {
        return marshallPrimitive(RNDR_PRTITYP_SHORT, "" + aValue);
    }

    public JSONObject marshall(char aValue)
    {
        return marshallPrimitive(RNDR_PRTITYP_CHAR, "" + aValue);
    }

    public JSONObject marshall(int aValue)
    {
        return marshallPrimitive(RNDR_PRTITYP_INT, "" + aValue);
    }

    public JSONObject marshall(long aValue)
    {
        return marshallPrimitive(RNDR_PRTITYP_LONG, "" + aValue);
    }

    public JSONObject marshall(float aValue)
    {
        return marshallPrimitive(RNDR_PRTITYP_FLOAT, "" + aValue);
    }

    public JSONObject marshall(double aValue)
    {
        return marshallPrimitive(RNDR_PRTITYP_DOUBLE, "" + aValue);
    }

    private JSONObject marshallPrimitive(String aType, String aValue)
    {
        final JSONObject lElement = new JSONObject();
        lElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_PRIM));
        lElement.getValue().put(RNDR_ATTR_TYPE, new JSONString(aType));
        lElement.getValue().put(RNDR_ATTR_VALUE, new JSONString(aValue));
        return lElement;
    }

    public JSONObject marshall(Object aObj)
    throws MarshallException
    {
        return marshallImpl(aObj, new HashMap());
    }

    public JSONObject marshallImpl(Object aObj, HashMap aPool)
    throws MarshallException
    {
        // Handle null references quickly.
        if(aObj == null)
        {
            final JSONObject lElement = new JSONObject();
            lElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_NULL));
            return lElement;
        }
        else
        {
            final Class lObjectClass = aObj.getClass();
            final String lObjectClassName = lObjectClass.getName();
            // A reference to the object will be used for storage as a key in the
            // hashtable for identity reasons. Two objects are different if they
            // are different in memory, even if they are equal().
            // Serialization should not change the original layout of the objects.
            final Reference lRef = new Reference(aObj);

            if(aPool.containsKey(lRef))
            {
                // We already rendered this object in the past.
                // We have to render to a reference.
                final JSONObject lElement = new JSONObject();
                lElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_OBJREF));
                lElement.getValue().put(RNDR_ATTR_REF, new JSONString((String) aPool.get(lRef)));
                return lElement;
            }
            else
            {
                // We did not encounter this object before.
                // We generate a new key and associate it with the object.
                final String lObjectId = generateId();
                aPool.put(lRef, lObjectId);

                if(lObjectClass.isArray()) return marshallImplArray(aObj, aPool);
                else return marshallImplObject(aObj, lObjectId, lObjectClass, lObjectClassName, aPool);
            }
        }
    }

    private JSONObject marshallImplArray(Object aObj, HashMap aPool)
    throws MarshallException
    {
        final Class lClass = aObj.getClass();
        final String lObjClassName = lClass.getName();

        // Construct the component class name.
        String lComponentClassName = "unknown";
        if(lObjClassName.startsWith("[L"))
            // Array of objects.
            lComponentClassName = lObjClassName.substring(2, lObjClassName.length() - 1);
        else
            // Array of array; Array of primitive types.
            lComponentClassName = lObjClassName.substring(1);

        final JSONObject lArrElement = new JSONObject();
        lArrElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_ARR));
        lArrElement.getValue().put(RNDR_ATTR_CLASS, new JSONString(lComponentClassName));

        final ArrayHelper lAh = new ArrayHelper();
        lAh.renderValue(aObj, lArrElement, this, aPool);

        return lArrElement;
    }

    private JSONObject marshallImplObject(Object aObj, String aObjId, Class aObjClass, String aObjClassName, HashMap aPool)
    throws MarshallException
    {
        final JSONObject lObjElement = new JSONObject();
        lObjElement.getValue().put(RNDR_ATTR_KIND, new JSONString(RNDR_OBJ));
        lObjElement.getValue().put(RNDR_ATTR_ID, new JSONString(aObjId));
        lObjElement.getValue().put(RNDR_ATTR_CLASS, new JSONString(aObjClassName));

        final MarshallHelper lHelper = repo.findHelper(aObjClass);
        lHelper.renderValue(aObj, lObjElement, this, aPool);
        return lObjElement;
    }

    private String generateId()
    {
        String lId = IDPREFIX + idCounter;
        idCounter++;
        return lId;
    }

    public MarshallValue unmarshall(JSONObject aElement)
    throws MarshallException
    {
        requireStringAttribute(aElement, RNDR_ATTR_KIND);
        final String lElementKind = ((JSONString) aElement.get(RNDR_ATTR_KIND)).getValue();
        final Object lUnmarshalled = unmarshallImpl(aElement, new HashMap());

        // Put primitive types in corresponding instance vars.
        if (RNDR_PRIM.equals(lElementKind))
        {
            if (lUnmarshalled instanceof Boolean)
                return new MarshallValueImpl(((Boolean) lUnmarshalled).booleanValue());
            else if (lUnmarshalled instanceof Byte)
                return new MarshallValueImpl(((Byte) lUnmarshalled).byteValue());
            else if (lUnmarshalled instanceof Short)
                return new MarshallValueImpl(((Short) lUnmarshalled).shortValue());
            else if (lUnmarshalled instanceof Character)
                return new MarshallValueImpl(((Character) lUnmarshalled).charValue());
            else if (lUnmarshalled instanceof Integer)
                return new MarshallValueImpl(((Integer) lUnmarshalled).intValue());
            else if (lUnmarshalled instanceof Long)
                return new MarshallValueImpl(((Long) lUnmarshalled).longValue());
            else if (lUnmarshalled instanceof Float)
                return new MarshallValueImpl(((Float) lUnmarshalled).floatValue());
            else if (lUnmarshalled instanceof Double)
                return new MarshallValueImpl(((Double) lUnmarshalled).doubleValue());
            else
            {
                final String lMsg = "Unknown primitive type encountered: " + lUnmarshalled.getClass().getName() + aElement.getLine() + ":" + aElement.getCol() + ".";
                throw new MarshallException(lMsg);
            }
        }
        else
        {
            return new MarshallValueImpl(lUnmarshalled);
        }
    }

   // Internal implementation. Always uses return objects, never primitives.
    public Object unmarshallImpl(JSONObject aElement, HashMap aObjectPool)
    throws MarshallException
   {
       requireStringAttribute(aElement, RNDR_ATTR_KIND);
       final String lElementKind = ((JSONString) aElement.get(RNDR_ATTR_KIND)).getValue();
       if (RNDR_OBJREF.equals(lElementKind))
       {
           requireStringAttribute(aElement, RNDR_ATTR_REF);
           final String lRef = ((JSONString) aElement.get(RNDR_ATTR_REF)).getValue();
           //noinspection SuspiciousMethodCalls
           final Object lObjFromPool = aObjectPool.get(lRef);
           if (lObjFromPool == null)
           {
               final String lMsg = "Unknown reference: " + lRef;
               throw new MarshallException(lMsg);
           }
           return lObjFromPool;
       }
       else
       {
           if (RNDR_PRIM.equals(lElementKind))
           {
               return unmarshallImplPrimitive(aElement);
           }
           else if (RNDR_NULL.equals(lElementKind))
           {
               return null;
           }
           else
           {
               if (RNDR_ARR.equals(lElementKind))
               {
                   final ArrayHelper lAh = new ArrayHelper();
                   return lAh.parseValue(aElement, this, aObjectPool);
               }
               else if (RNDR_OBJ.equals(lElementKind))
               {
                   try
                   {
                       requireStringAttribute(aElement, RNDR_ATTR_CLASS);
                       final String lBeanClassName = ((JSONString) aElement.get(RNDR_ATTR_CLASS)).getValue();
                       if (lBeanClassName == null)
                       {
                           final String lMsg = ERR_MISSINGATTRVAL + RNDR_ATTR_CLASS;
                           throw new MarshallException(lMsg);
                       }

                       String lId = null;
                       try
                       {
                           JSONMarshall.requireStringAttribute(aElement, JSONMarshall.RNDR_ATTR_ID);
                           lId = ((JSONString) aElement.get(JSONMarshall.RNDR_ATTR_ID)).getValue();
                       }
                       catch (Exception eIgnore)
                       {
                       }

                       final Class lBeanClass = Class.forName(lBeanClassName);
                       MarshallHelper lHelper = repo.findHelper(lBeanClass);
                       Object lResult =  lHelper.parseValue(aElement, this, aObjectPool);
                       if(lId != null) aObjectPool.put(lId, lResult);
                       return lResult;                       
                   }
                   catch (ClassNotFoundException e)
                   {
                       final String lMsg = "Tried to unmarshall unknown class.";
                       throw new MarshallException(lMsg);
                   }
               }
               else
               {
                   final String lMsg = "Unknown type encountered: " + lElementKind;
                   throw new MarshallException(lMsg);
               }
           }
       }
   }

    private Object unmarshallImplPrimitive(JSONObject aElement)
    throws MarshallException
    {
        requireStringAttribute(aElement, RNDR_ATTR_TYPE);
        requireStringAttribute(aElement, RNDR_ATTR_VALUE);


        final String lType = ((JSONString) aElement.get(RNDR_ATTR_TYPE)).getValue();
        final String lValue = ((JSONString) aElement.get(RNDR_ATTR_VALUE)).getValue();

        try
        {
            if("boolean".equals(lType)) return new Boolean(lValue);
            else if("byte".equals(lType)) return new Byte(lValue);
            else if("short".equals(lType)) return new Short(lValue);
            else if("char".equals(lType)) return new Character(lValue.charAt(0));
            else if("int".equals(lType)) return new Integer(lValue);
            else if("long".equals(lType)) return new Long(lValue);
            else if("float".equals(lType)) return new Float(lValue);
            else if("double".equals(lType)) return new Double(lValue);
            else
            {
                final String lMsg = "Unknown primitive type encountered: " + lType;
                throw new MarshallException(lMsg);
            }
        }
        catch(MarshallException passtrough)
        {
            throw passtrough;
        }
        catch(Exception e)
        {
            final String lMsg = "Error while unmarshalling primitive type: " + lType + ", value: " + lValue;
            throw new MarshallException(lMsg);
        }
    }

    public static void requireStringAttribute(JSONObject aElement, String anAttribute)
    throws MarshallException
    {
        if(!aElement.containsKey(anAttribute))
        {
             final String lMsg = ERR_MISSINGATTRVAL + anAttribute + " for object at location " + aElement.getLine() + ":" + aElement.getCol() + ".";
             throw new MarshallException(lMsg);
        }

        if(!(aElement.get(anAttribute) instanceof JSONString))
        {
            final String lMsg = ERR_MISSINGSTRING + anAttribute + " for object at location " + aElement.getLine() + ":" + aElement.getCol() + ".";
            throw new MarshallException(lMsg);
        }
    }

    /**
     * Add custom helper class.
     *
     * @param aHelper the custom helper you want to add to the serializer.
     */
    public void addHelper(MarshallHelper aHelper)
    {
        repo.addHelper(aHelper);
    }

    /**
     * The objects that fall back on the general object helper will be serialized by
     * using their fields directly. Without further annotations, the default
     * constructor without arguments will be used in the POJO. If this is not sufficient,
     * the @JSONConstruct and @JSONSerialize annotations can be used as well in the  POJO to
     * indicate which constructor has to be used.
     */
    public void usePojoAccess()
    {
        addHelper(new ObjectHelperDirect());
    }

    /**
     * The objects that fall back on the general object helper will be serialized by
     * using their JavaBean properties. The  JavaBean always needs a
     * default constructor without arguments.
     */
    public void useJavaBeanAccess()
    {
        addHelper(new ObjectHelper());
    }
}