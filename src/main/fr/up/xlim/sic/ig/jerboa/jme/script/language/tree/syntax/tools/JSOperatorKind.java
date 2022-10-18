/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools;

/**
 * @author Hakim
 *
 */
public enum JSOperatorKind {
	// Arith
	PLUS, MINUS, MULT, DIV, MOD, DEC, INC,
	QM, DOT, COMMA, 

	// Assignement
	AFFECT, ASS_ADD, ASS_SUB, ASS_MUL, ASS_DIV, ASS_MOD, ASS_XOR, ASS_AND, ASS_OR, ASS_SHR, ASS_SHL,

	// REL
	EQUAL, DIFF, LT, LE, GE, GT,

	// LOG
	AND, OR, XOR, SHL, SHR, NOT,

	// OTHER
	ELLIPSIS, ARROW, 
	SEMICOLON, COLON, STATICOP, AMPERSAND, 
	LPAR, RPAR, LBRACE, RBRACE, LBRACKET, RBRACKET, 

	// Jerboa 
	ALPHA, DOLLAR, PIPE, BIG_ARROW, UNDER

	;

	public String toCode() {
		switch (this) {
		case AFFECT :		return "=";
		case AMPERSAND :	return "&";
		case AND:			return "&&";
		case ARROW:			return "->";
		case ASS_ADD:		return "+=";
		case ASS_AND:		return "&=";
		case ASS_DIV:		return "/=";
		case ASS_MOD:		return "%=";
		case ASS_MUL:		return "*=";
		case ASS_OR:		return "|=";
		case ASS_SHL:		return "<<=";
		case ASS_SHR:		return ">>=";
		case ASS_SUB:		return "-=";
		case ASS_XOR:		return "^=";
		case COLON :		return ":";
		case COMMA :		return ",";
		case DEC:			return "--";
		case DIFF:			return "!=";
		case DIV:			return "/";
		case DOT :			return ".";
		case ELLIPSIS:		return "...";
		case EQUAL:			return "==";
		case GE:			return ">=";
		case GT:			return ">";
		case INC:			return "++";
		case LBRACE :		return "{";
		case LBRACKET :		return "[";
		case LE:			return "<=";
		case LPAR :			return "(";
		case LT:			return "<";
		case MINUS:			return "-";
		case MOD:			return "%";
		case MULT:			return "*";
		case NOT:			return "!";
		case OR:			return "||";
		case PLUS:			return "+";
		case QM :			return "?";
		case RBRACE :		return "}";
		case RBRACKET :		return "]";
		case RPAR :			return ")";
		case SEMICOLON :	return ";";
		case SHL:			return "<<";
		case SHR:			return ">>";
		case STATICOP :		return "::";
		case XOR:			return "^";
		case ALPHA : 		return "@";
		case DOLLAR : 		return "$";
		case PIPE : 		return "|";
		case BIG_ARROW : 	return "=>";
		case UNDER : 		return "_";
		default:			return "<unknown>";

		}
	}
}
