package fr.up.xlim.sic.ig.jerboa.jme.script.language.generated;
import java_cup.runtime.Symbol; 

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSOperatorKind;


%%
%public
%class MyLexer
%cup
%line
%{
public int getLine() { return yyline+1; }
public int getColumn() { return yycolumn; }
%}
%column

%{
	StringBuffer string = new StringBuffer();
	int countCloseBrace = 0;
	
	protected StringBuffer m_String = new StringBuffer();
	protected ComplexSymbolFactory factory;
	
	
	public MyLexer(java.io.Reader in, ComplexSymbolFactory sf){
		this(in);
		factory = sf;
    }
	 
	void setSymbolFactory(ComplexSymbolFactory sf) {
		factory = sf;
	}
	
	private Symbol symbol(String name, int sym, Object val,int buflength) {
	      Location left = new Location(yyline+1,yycolumn+yylength()-buflength,yychar+yylength()-buflength);
	      Location right= new Location(yyline+1,yycolumn+yylength(), yychar+yylength());
	      return factory.newSymbol(name, sym, left, right,val);
	  }
	  
	  
	public Symbol symbol(String plainname, int terminalcode, String lexem){
		return factory.newSymbol(plainname, terminalcode, new Location(yyline+1, yycolumn +1), 
				new Location(yyline+1,yycolumn+yylength()), lexem);
	}

	public Symbol symbol(String plainname, int terminalcode){
		return factory.newSymbol(plainname, terminalcode, new Location(yyline+1, yycolumn +1), 
				new Location(yyline+1,yycolumn+yylength()));
	}
	
	private Symbol symbol(String name, int sym, Object val) {
	      Location left = new Location(yyline+1,yycolumn+1,yychar);
	      Location right= new Location(yyline+1,yycolumn+yylength(), yychar+yylength());
	      return factory.newSymbol(name, sym, left, right,val);
	  }

%}


%eofval{
    return factory.newSymbol("EOF",sym.EOF);
%eofval}

DIGIT=[0-9]
ID=[a-zA-Z][a-zA-Z_0-9]* // on ne permet pas de commencer par un [_]* car sinon on ne peut pas parser <0,1,2>_point(n)
EXPONENT=[eE][+-]?{DIGIT}+
BoolLiteral = true | false

%state STRING
%state LANGCODE
%state LANGHEADER

%%

<YYINITIAL> {
					/* Comments */
"//".*		{}
"/*" ~ "*/"	{}
					/* Operators */

"?" 		{return symbol("INTERROGATION", sym.INTERROGATION);}
"^" 		{return symbol("XOR"		, sym.XOR,  JSOperatorKind.XOR);}
"+" 		{return symbol("PLUS"		, sym.PLUS,  JSOperatorKind.PLUS);}
"-" 		{return symbol("MINUS"		, sym.MINUS, JSOperatorKind.MINUS);}
"*" 		{return symbol("MULT"		, sym.MULT,  JSOperatorKind.MULT);}
"/" 		{return symbol("DIV"		, sym.DIV, 	 JSOperatorKind.DIV);}
"%" 		{return symbol("MOD"		, sym.MOD, 	 JSOperatorKind.MOD);}
"and"		{return symbol("AND"		, sym.AND, 	 JSOperatorKind.AND);}
"&&"		{return symbol("AND"		, sym.AND, 	 JSOperatorKind.AND);}
"or"		{return symbol("OR"			, sym.OR, 	 JSOperatorKind.OR);}
"||"		{return symbol("OR"			, sym.OR, 	 JSOperatorKind.OR);}
"++" 		{return symbol("INC"		, sym.INC, 	 JSOperatorKind.INC);}
"--" 		{return symbol("DEC"		, sym.DEC, 	 JSOperatorKind.DEC);}
"<"			{return symbol("LT"			, sym.LT);}
"<="		{return symbol("LE"			, sym.COMP , JSOperatorKind.LE);}
">"			{return symbol("GT"			, sym.GT);}
">="		{return symbol("GE"			, sym.COMP , JSOperatorKind.GE);}
"=="		{return symbol("EQ"			, sym.COMP , JSOperatorKind.EQUAL);} // juste mettre le "=" ?
"not"		{return symbol("NOT"		, sym.NOT);}
"!"			{return symbol("NOT"		, sym.NOT);}
"!="		{return symbol("DIFF"		, sym.COMP , JSOperatorKind.DIFF);}
[nN][uU][lL][lL](ptr)?		{return symbol("NULL", sym.NULL);}
"@"			{return symbol("ALPHA"		, sym.ALPHA);}
"$"			{return symbol("DOLLAR"		, sym.DOLLAR);}
"|"			{return symbol("PIPE"		, sym.PIPE);}
"=>"		{return symbol("ARROW"		, sym.ASSIGN);}
"new"		{return symbol("NEW"		, sym.NEW);}
"delete"	{return symbol("DELETE"		, sym.DELETE);}
"try"		{return symbol("TRY"		, sym.TRY);}
"catch"		{return symbol("CATCH"		, sym.CATCH);}
"finally"	{return symbol("FINALLY"	, sym.FINALLY);}
"throw"		{return symbol("THROW"	    , sym.THROW);}
"&"			{return symbol("AMPERSAND"	, sym.AMPERSAND);}
"continue"	{return symbol("CONTINUE"	, sym.CONTINUE);}

[Tt][Rr][Uu][Ee]		{return symbol("TRUE"	, sym.TRUE);}
[Ff][Aa][Ll][Ss][Ee]	{return symbol("FALSE"	, sym.FALSE);}


\"			{ yybegin(STRING); }

					/* instructions */
[Bb][Rr][Ee][Aa][Kk] {return symbol("BREAK"		, sym.BREAK);}

[Ii][Ff] 				{return symbol("IF"			, sym.IF);}
[Ee][Ll][Ss][Ee]		{return symbol("ELSE"		, sym.ELSE);}
[Ww][Hh][Ii][Ll][Ee]	{return symbol("WHILE"		, sym.WHILE);}
[Dd][Oo]				{return symbol("DO"			, sym.DO);}
[Ff][Oo][Rr]			{return symbol("FOR"		, sym.FOR);}
"foreach" 				{return symbol("FOREACH"	, sym.FOREACH);}
"step"					{return symbol("STEP"		, sym.STEP);}
"(" 					{return symbol("LPAR"		, sym.LPAR);}
")" 					{return symbol("RPAR"		, sym.RPAR);}
"{" 					{return symbol("LBRACE"		, sym.LBRACE);}
"}" 					{return symbol("RBRACE"		, sym.RBRACE);}
"=" 					{return symbol("AFFECT"		, sym.AFFECT);}
"[" 					{return symbol("LBRACKET"	, sym.LBRACKET);}
"]" 					{return symbol("RBRACKET"	, sym.RBRACKET);}
";" 					{return symbol("END_INSTR" 	, sym.SEMICOLON);}
":" 					{return symbol("COLON" 		, sym.COLON);}
"::" 					{return symbol("STATICOP" 	, sym.STATICOP);}
"return"				{return symbol("RETURN"		, sym.RETURN);}
"in"					{return symbol("IN"			, sym.IN);}
"."						{return symbol("DOT"		, sym.DOT);}
","						{return symbol("COMMA"		, sym.COMMA);}
"_"						{return symbol("UNDER"		, sym.UNDER);}
"@print"				{return symbol(" PRINT "	, sym.PRINT);}


[vV][oO][iI][dD]	{return symbol(" VOID "	, sym.VOID);}


[jJ][eE][rR][bB][oO][aA][Ll][iI][sS][tT]	{return symbol(" LIST "	, sym.LIST);}

					/* Topology */

"#"				{return symbol(" # "   		, sym.SHARP);}
"@ebd"			{return symbol(" EBD " 		, sym.EBD);}
"@"[gG]"map"	{return symbol(" GMAP "		, sym.GMAP);}
"@rule"			{return symbol(" RULE "		, sym.RULE);}
"@op"			{return symbol(" RULE "		, sym.RULE);}
"@collect"		{return symbol(" COLLECT "	, sym.COLLECT);}
"@modeler"		{return symbol(" MODELER "	, sym.MODELER);}
"@dimension"	{return symbol(" DIMENSION ", sym.DIMENSION);}
"@"[lL]"eft"[pP]"attern"	{return symbol(" LEFTPATTERN ", sym.LEFTPATTERN);}
"@"[rR]"ight"[pP]"attern"	{return symbol(" RIGHTPATTERN ", sym.RIGHTPATTERN);}

/* Specification des marquages */
"@mark"						{return symbol(" MARK "			, sym.MARK);}
"@unmark"					{return symbol(" UNMARK "		, sym.UNMARK);}
"@is"[mM]"arked"			{return symbol(" ISMARKED "		, sym.ISMARKED);}
"@is"[nN]"ot"[mM]"arked"	{return symbol(" ISNOTMARKED "	, sym.ISNOTMARKED);}


/* opérateur de cheat */
"@lang"			{ yybegin(LANGHEADER); 	return symbol("LANG", sym.LANG);}
"@header"		{ yybegin(LANGHEADER); 	return symbol("HEADER", sym.HEADER);}




\'.\'			{return symbol(" Char:"+yytext(), sym.CHAR,yytext().substring(1,yytext().length()-1));}
			/* Variables or Numbers */
{DIGIT}+ 								{return symbol("INT"	, sym.INT,yytext());}
{DIGIT}+("."{DIGIT}+)+?{EXPONENT}?"f" 	{return symbol("FLOAT"	, sym.FLOAT,yytext());}
{DIGIT}+("."{DIGIT}+)?{EXPONENT}? 		{return symbol("DOUBLE"	, sym.DOUBLE,yytext());}
{ID}									{return symbol("ID"		, sym.IDENT,yytext());}
} /* END YYINITIAL STATE */

<STRING> {
	  \"                             	{ yybegin(YYINITIAL); String res = string.toString(); string = new StringBuffer();
	  return symbol("StringConst",sym.STRING,res,res.length()); }
	  [^\n\r\"\\]+                    	{ string.append( yytext() ); }
	  "\\t"                            	{ string.append("\\t"); }
	  "\\n"                            	{ string.append("\\n"); }

	  "\\r"                            	{ string.append("\\r"); }
	  "\\\""                           	{ string.append("\\\""); }
	  "\\"                             	{ string.append('\\'); }
	} /* END STRING STATE */

<LANGHEADER> {
"(" 			{return symbol("LPAR"		, sym.LPAR);}
")"	 			{return symbol("RPAR"		, sym.RPAR);}
"{" 			{ yybegin(LANGCODE); countCloseBrace = 1;}
[a-zA-Z+#]+			{  return symbol("LANGDEF"	, sym.LANGDEF, yytext());} /* LANGDEF:l */
  
  } /* END LANGHEADER STATE */

<LANGCODE> {
"{"		{ countCloseBrace++; string.append(yytext()); }
"}"		{ countCloseBrace--;
			if(countCloseBrace == 0) {
				yybegin(YYINITIAL);
				String res = string.toString();
				string = new StringBuffer();
	  			return symbol("PLAINCODE",sym.PLAINCODE,res,res.length()); 
	  		}
	  		else {
	  			string.append(yytext()); 
	  		}
	  	}
[^}{]+	{  string.append( yytext() ); }

} /* END LANGCODE STATE */


[^]|[\n]|[\r\t\f\ ]+           {  }

