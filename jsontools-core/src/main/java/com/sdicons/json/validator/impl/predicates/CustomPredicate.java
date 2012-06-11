package com.sdicons.json.validator.impl.predicates;

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

import com.sdicons.json.model.JSONObject;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.model.JSONString;
import com.sdicons.json.validator.ValidationException;
import com.sdicons.json.validator.Validator;
import com.sdicons.json.validator.impl.ValidatorUtil;

import java.util.HashMap;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CustomPredicate
extends Predicate
{
    private Validator validator;

    public CustomPredicate(String aName, JSONObject aRule, HashMap<String, Validator> aRuleset)
    throws ValidationException
    {
        super(aName, aRule);
        ValidatorUtil.requiresAttribute(aRule, ValidatorUtil.PARAM_CLASS, JSONString.class);
        String lClassname = ((JSONString) aRule.get(ValidatorUtil.PARAM_CLASS)).getValue();

        try
        {
            Class lCustomClass = Class.forName(lClassname);
            if(!CustomValidator.class.isAssignableFrom(lCustomClass))
            {
                // Problem, not derived from CustomValidator.
                throw new ValidationException("The custom class is not derived from CustomValidator: " + lClassname, aRule, aName);
            }
            else
            {
                Constructor lConstructor = lCustomClass.getConstructor(String.class,JSONObject.class,HashMap.class);
                validator = (Validator) lConstructor.newInstance(aName, aRule, aRuleset);
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new ValidationException("The custom class was not found: " + lClassname, aRule, aName);
        }
        catch (NoSuchMethodException e)
        {
            throw new ValidationException("Constructor method not found on custom class: " + lClassname, aRule, aName);
        }
        catch (InstantiationException e)
        {
            throw new ValidationException("Error during construction of validtor of class: " + lClassname, aRule, aName);
        }
        catch (IllegalAccessException e)
        {
            throw new ValidationException("Access rights problem during construction of validator of class: " + lClassname, aRule, aName);
        }
        catch (InvocationTargetException e)
        {
            throw new ValidationException("Access rights problem during construction of validator of class: " + lClassname, aRule, aName);
        }
    }

    public void validate(JSONValue aValue)
    throws ValidationException
    {
        validator.validate(aValue);
    }
}
