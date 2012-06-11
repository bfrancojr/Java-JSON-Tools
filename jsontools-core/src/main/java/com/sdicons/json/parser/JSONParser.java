package com.sdicons.json.parser;

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

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.impl.JSONLexer;

import java.io.InputStream;
import java.io.Reader;

/**
 * Reads JSON text and convert it into a Java model for further handling.
 */
public class JSONParser
{
    private com.sdicons.json.parser.impl.JSONParserAntlr parser;
    private String streamName = "[unknown]";

    /**
     * Construct a parser using a stream.
     * @param aStream A stream containing JSON text.
     * @param aStreamName A String that describes the stream, it will be attached to
     * all JSON objects in the model which are generated from this parser. This makes it
     * possible to identify the stream where the object came from.
     */
    public JSONParser(InputStream aStream, String aStreamName)
    {
         JSONLexer lexer = new JSONLexer(aStream);
         parser = new com.sdicons.json.parser.impl.JSONParserAntlr(lexer);
         if(aStreamName != null) streamName = aStreamName;
    }

    /**
     * Construct a parser using a stream.
     * @param aStream A stream containing JSON text.
     */
    public JSONParser(InputStream aStream)
    {
        this(aStream, null);
    }

    /**
     * Construct a parser using a reader.
     * @param aReader A reader containing JSON text.
     * @param aStreamName A String that describes the stream, it will be attached to
     * all JSON objects in the model which are generated from this parser. This makes it
     * possible to identify the stream where the object came from.
     */
    public JSONParser(Reader aReader, String aStreamName)
    {
         JSONLexer lexer = new JSONLexer(aReader);
         parser = new com.sdicons.json.parser.impl.JSONParserAntlr(lexer);
         if(aStreamName != null) streamName = aStreamName;
    }

    /**
     * Construct a parser using a reader.
     * @param aReader A reader containing JSON text.
     */
    public JSONParser(Reader aReader)
    {
        this(aReader, null);
    }

    /**
     * Read the next JSON structure from the stream and convert it into a
     * Java model.
     * @return    A Java object representing the object in the stream.
     * @throws TokenStreamException A syntax error is encountered.
     * @throws RecognitionException When a token could not be formed.
     */
    public JSONValue nextValue()
    throws TokenStreamException, RecognitionException
    {
        return parser.value(streamName);
    }
}
