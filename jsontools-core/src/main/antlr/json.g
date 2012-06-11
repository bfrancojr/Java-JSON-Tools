header
{
package com.sdicons.json.parser.impl;
import com.sdicons.json.model.*;
import java.math.BigDecimal;
import java.math.BigInteger;
}

class JSONParserAntlr extends Parser;

options
{

   defaultErrorHandler = false;
}

value[String aStreamName] returns [JSONValue val=JSONNull.NULL] :
     val=object[aStreamName]
   | val=array[aStreamName]
   | val=atomic[aStreamName]
   ;

atomic[String aStreamName] returns[JSONValue val=JSONNull.NULL] :
     t:TRUE         {val = new JSONBoolean(true); val.setLineCol(t.getLine(), t.getColumn()); val.setStreamName(aStreamName);}
   | f:FALSE        {val = new JSONBoolean(false);val.setLineCol(f.getLine(), f.getColumn()); val.setStreamName(aStreamName);}
   | n:NULL         {val.setLineCol(n.getLine(), n.getColumn()); val.setStreamName(aStreamName);}
   | str:STRING     {val = new JSONString(str.getText());val.setLineCol(str.getLine(), str.getColumn()); val.setStreamName(aStreamName);}
   | num:NUMBER     { String lTxt = num.getText().toLowerCase();
                      if(lTxt.indexOf('e')>=0 || lTxt.indexOf('.')>=0) val = new JSONDecimal(new BigDecimal(lTxt));
                      else val = new JSONInteger(new BigInteger(lTxt));
                      val.setLineCol(num.getLine(), num.getColumn());
                      val.setStreamName(aStreamName); }
   ;

object[String aStreamName] returns [JSONObject lResult= new JSONObject()] :
{JSONValue lVal;}
   begin:LCURLY                                {lResult.setLineCol(begin.getLine(), begin.getColumn()); lResult.setStreamName(aStreamName);}
   (
        i:STRING COLON lVal=value[aStreamName]              {lResult.getValue().put(i.getText(), lVal);}
        (
            COMMA j:STRING COLON lVal=value[aStreamName]    {lResult.getValue().put(j.getText(), lVal);}
        )*
   )?
   RCURLY
   ;

array[String aStreamName]  returns [JSONArray lResult=new JSONArray()] :
{JSONValue lVal;}
   begin:LBRACK                                 {lResult.setLineCol(begin.getLine(), begin.getColumn()); lResult.setStreamName(aStreamName);}
   (
        lVal=value[aStreamName]                              {lResult.getValue().add(lVal);}
        (
            COMMA lVal=value[aStreamName]                    {lResult.getValue().add(lVal);}
        )*
   )?
   RBRACK
   ;

class JSONLexer extends Lexer;

options {
    k=2; // needed for newline junk
    charVocabulary='\u0003'..'\uFFFF';   
}

LPAREN: '(' ;
RPAREN: ')' ;
LCURLY: '{' ;
RCURLY: '}' ;
LBRACK: '[' ;
RBRACK: ']' ;
COMMA:  ',' ;
QUOTES: '"' ;
COLON:  ':' ;
TRUE:   "true";
FALSE:  "false";
NULL:   "null";

// string literals
STRING
{StringBuilder lBuf = new StringBuilder();}
	:	'"' (escaped:ESC {lBuf.append(escaped.getText());}| normal:~('"'|'\\'|'\n'|'\r') {lBuf.append(normal);} )* '"' {$setText(lBuf.toString());}
	;

protected
ESC
	:	'\\'
		(	'n'    {$setText("\n");}
		|	'r'    {$setText("\r");}
		|	't'    {$setText("\t");}
		|	'b'    {$setText("\b");}
		|	'f'    {$setText("\f");}
		|	'"'    {$setText("\"");}
		|	'\''   {$setText("\'");}
		|	'/'   {$setText("/");}
		|	'\\'   {$setText("\\");}
		|	('u')+ i:HEX_DIGIT j:HEX_DIGIT k:HEX_DIGIT l:HEX_DIGIT   {$setText(ParserUtil.hexToChar(i.getText(),j.getText(),k.getText(),l.getText()));}

		)
	;

protected
HEX_DIGIT
	:	('0'..'9'|'A'..'F'|'a'..'f')
	;

protected
ZERO: '0' ;

protected
NONZERO: '1'..'9';

protected
DIGIT: ZERO | NONZERO;

protected
INTEGER: ZERO | NONZERO (DIGIT)* ;

NUMBER
	:	('-')?
	    INTEGER
		(	(
				'.'
				(DIGIT)+ (EXPONENT)?
			)?
		|	EXPONENT 	// 'E' means we are float
		)
	;

protected
EXPONENT
	:	('e') ('+'|'-')? INTEGER
	;

// Whitespace -- ignored
WS	:	(	' '
		|	'\t'
		|	'\f'
			// handle newlines
		|	(	options {generateAmbigWarnings=false;}
			:	"\r\n"  // Evil DOS
			|	'\r'    // Macintosh
			|	'\n'    // Unix (the right way)
			)
			{ newline(); }
		)+
		{ _ttype = Token.SKIP; }
	;

// Single-line comments
SL_COMMENT
	:	"#"
		(~('\n'|'\r'))* ('\n'|'\r'('\n')?)
		{$setType(Token.SKIP); newline();}
	;