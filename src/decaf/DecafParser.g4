
/*
 * A VERY minimal skeleton for your parser, provided by Emma Norling.
 *
 * Your parser should use the tokens provided by your lexer in rules.
 * Even if your lexer appeared to be working perfectly for stage 1,
 * you might need to adjust some of those rules when you implement
 * your parser.
 *
 * Remember to provide documentation too (including replacing this
 * documentation).
 *
 */
parser grammar DecafParser;
options { tokenVocab = DecafLexer; }

program: CLASS ID LCURLY (field_decl)* (method_decl)* RCURLY EOF;



field_decl: type field_name (COMMA field_name)* SEMI;
field_name: (ID | ID SQBRACKETLEFT (INT_LITERAL) SQBRACKETRIGHT);

method_decl: ((type) | VOID ) (ID) LBRACKET (type ID (COMMA type ID)*)? RBRACKET (block);

block: LCURLY (var_decl)*(statement)* RCURLY;

var_decl: (type) ID (COMMA ID)* SEMI;

type: INT | BOOLEAN;

statement: (location)(assign_op)(expr) SEMI 
| (method_call) SEMI 
| IF LBRACKET (expr) RBRACKET (block) ((ELSE) (block))?
| FOR (ID) EQUALS (expr) COMMA (expr)(block)
| RETURN ((expr))? SEMI
| BREAK SEMI
| CONTINUE SEMI
| (block);

assign_op: EQUALS | INC | DEC;

method_call: method_name LBRACKET (expr (COMMA expr)*)? RBRACKET
| CALLOUT LBRACKET STRING (COMMA callout_arg(COMMA callout_arg)* )? RBRACKET;

method_name: (ID);	

location: (ID) | (ID) SQBRACKETLEFT (expr) SQBRACKETRIGHT;

expr: MINUS(expr) 
	| NOT(expr) 
	| expr (TIMES | DIVIDE | MOD) expr 
	| expr (PLUS | MINUS) expr 
	| expr rel_op expr 
	| expr eq_op expr 
	| expr AND expr 
	| expr OR expr 
	| (location)
	| (method_call) 
	| (literal) 	
	| LBRACKET (expr) RBRACKET;

callout_arg: (expr) | STRING;

rel_op: GREATER | LESS | EQUALLESS | EQUALGREAT;

eq_op:EQUALTOO | NOTEQUAL;

literal: (INT_LITERAL) | (CHAR) | (BOOLEAN);







 