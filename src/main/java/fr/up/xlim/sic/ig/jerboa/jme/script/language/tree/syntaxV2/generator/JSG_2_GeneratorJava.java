package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.generator;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEGraph;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.GeneratedLanguage;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageState;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue.LanguageType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSOperatorKind;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JSG_2_Entity;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Alpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_ApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_AssocParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Choice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Collect;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_CollectEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_EbdParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_FreeMarker;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_GetEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_GetMarker;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_IsMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_IsNotMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_JerboaKeyword;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_JerboaKeyword.KeywordType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Mark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_Rule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_RuleArg;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_RuleNodeId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.JerboaOperators.JSG_2_UnMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_InScope;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_Index;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.access.JSG_2_IndexInJerboaType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_AtLang;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Block;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Boolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Break;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Cast;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Catch;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Comment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Continue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Delete;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Double;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Float;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Header;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_If;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Indirection;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Integer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Literal;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Long;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Map;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_NOP;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_New;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Not;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Null;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Operator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Orbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Print;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Return;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_String;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Throw;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Try;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Unreference;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.JSG_2_Variable;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.declaration.JSG_2_Assignment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.declaration.JSG_2_Declare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.declaration.JSG_2_DeclareMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_AddInHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_AddInList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_Call;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_CallListSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_CallRuleResHeight;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_CallRuleResWidth;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_Constructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_DeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GMapSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetDartId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetEbdId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetEbdName;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.functions.JSG_2_GetEbdOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_PackagedType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeBoolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeDart;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeEmbedding;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeGmap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypePointer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeRuleResult;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeString;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.entity.type.JSG_2_TypeTemplate;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_DoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_For;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_ForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_ForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.loops.JSG_2_While;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.TranslatorContextV2;

/**
 * @author Valentin
 *
 */
public class JSG_2_GeneratorJava implements Generator_G_V2 {

	private static String nameToProperty(String name) {
		String s = name.substring(0, 1).toUpperCase() + name.substring(1);
		return s;
	}

	private static String getCategoryFolder(final String s) {
		String res = new String(s);
		if (s.length() > 0)
			return res.replaceAll("[/|:|;|,|\\\\]", ".");
		return "";
	}
	private static String getModelerModulePath(JMEModeler modeler, String endStringToAdd) {
		String module = modeler.getModule().replaceAll("[/|:]+", ".");
		if (!module.endsWith("/") && !module.endsWith("\\") && module.length() > 0) {
			module += endStringToAdd;
		}
		return module;
	}
	private static String getRulePath(JMERule r) {
		String path = getModelerModulePath(r.getModeler(), "/");
		path += getCategoryFolder(r.getCategory());
		if (path.length() > 0 && !path.endsWith("/"))
			path += "/";
		return path.replaceAll("/", ".") + r.getName();
	}

	private GeneratedLanguage genLanguage;
	private LanguageGlue glue;

	private TranslatorContextV2 context;

	private int nbSpaceIndent;

	private String errorParseMsg = "error in parsing";

	private boolean lastEntityCouldNeedSemicolon = false;

	private String exceptionNamePrefix = "_exep";

	public JSG_2_GeneratorJava(LanguageGlue _glue, TranslatorContextV2 context, GeneratedLanguage genLang, JMEModeler modeler) {
		nbSpaceIndent = 0;
		glue = _glue;
		this.context = context;
		genLanguage = new GeneratedLanguage(genLang);
	}

	@Override
	public Boolean accept(JSG_2_AddInHookList jsg_2_AddInHookList) {
		final List<JSG_2_Entity> args = jsg_2_AddInHookList.getArgs();
		if (args.size() > 1) { // si on passe un entier ET la valeur c'est pour
			// ajouter a  une place specifique
			genLanguage.appendContent("addRow(");
		} else // sinon ajout normal
			genLanguage.appendContent("addCol(");

		nbSpaceIndent+=2;
		boolean presJump = false;
		for (int i = 0; i < args.size(); i++) {
			final JSG_2_Entity e = args.get(i);
			if(presJump)
				indent();
			presJump = e.visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		nbSpaceIndent-=2;
		if(presJump)
			indent();
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_AddInList jsg_2_AddInList) {
		genLanguage.appendContent("add(");

		// Cast sur le contenu de la liste :

		JSG_2_Type typeList = context.getType(jsg_2_AddInList.getList(), glue);
		if(typeList!=null && typeList instanceof JSG_2_TypeList) {
			genLanguage.appendContent("(");
			((JSG_2_TypeList)typeList).getListContentType().visit(this);
			genLanguage.appendContent(")");
		}
		// FIN de cast

		nbSpaceIndent+=2;
		final List<JSG_2_Entity> args = jsg_2_AddInList.getArgs();

		boolean presJump = false;
		for (int i = 0; i < args.size(); i++) {
			final JSG_2_Entity e = args.get(i);
			if(presJump)
				indent();
			presJump = e.visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		nbSpaceIndent-=2;
		if(presJump)
			indent();
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Alpha jsg_2_Alpha) {
		jsg_2_Alpha.getNode().visit(this);
		genLanguage.appendContent(".alpha(");
		jsg_2_Alpha.getDim().visit(this);
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_ApplyRule jsg_2_ApplyRule) {
		jsg_2_ApplyRule.getRuleExpr().visit(this);
		genLanguage.appendContent(".applyRule(gmap");
		final List<JSG_2_Entity> args = jsg_2_ApplyRule.getArgs();
		if(args.size()>0) {
			genLanguage.appendContent(",");
		}

		nbSpaceIndent+=2;
		boolean presJump = false;
		for (int i = 0; i < args.size(); i++) {
			JSG_2_Entity ra = args.get(i);
			if(presJump)
				indent();
			presJump = ra.visit(this);
		}
		if(presJump)
			indent();
		nbSpaceIndent-=2;
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Assignment jsg_2_Assignment) {
		int l = jsg_2_Assignment.getLine();
		int c = jsg_2_Assignment.getColumn();

		jsg_2_Assignment.getVariable().visit(this);
		genLanguage.appendContent(" = ");

		JSG_2_Entity entityValue = jsg_2_Assignment.getValue();

		if(jsg_2_Assignment.getValue() instanceof JSG_2_Variable
				&& jsg_2_Assignment.getValue() instanceof JSG_2_Constructor) {
			JSG_2_Variable var = (JSG_2_Variable) jsg_2_Assignment.getVariable();
			JSG_2_Entity varConstruct = jsg_2_Assignment.getValue();

			if(context.varExists(var.getName())) {
				JSG_2_Type varType = context.getType(var, glue);
				JSG_2_Type constructType = context.getType(varConstruct, glue);
				if(varType!=null && varType.getTypeName().compareTo(constructType.getTypeName())==0) {
					entityValue = (new JSG_2_New(varConstruct, l, c));
				}
			}
		}
		lastEntityCouldNeedSemicolon = true;
		return entityValue.visit(this);
	}

	@Override
	public Boolean accept(JSG_2_AssocParam jsg_2_AssocParam) {
		jsg_2_AssocParam.getRule().visit(this);
		genLanguage.appendContent(".set");
		genLanguage.appendContent(nameToProperty(jsg_2_AssocParam.getParamName()));
		genLanguage.appendContent("(");
		jsg_2_AssocParam.getParamValue().visit(this);
		genLanguage.appendContent(")");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_AtLang jsg_2_AtLang) {
		if (jsg_2_AtLang.getLanguage() == null 
				|| "java".equalsIgnoreCase(jsg_2_AtLang.getLanguage())
				|| "j".equalsIgnoreCase(jsg_2_AtLang.getLanguage())) {

			genLanguage.appendContent("\n");
			indent();
			genLanguage.appendContent("// >> BEGIN RAW CODE\n");
			genLanguage.appendContentln(jsg_2_AtLang.getCode());
			//			for(String line : jsg_2_AtLang.getCode().split("\n")) {
			//				if(line.length()>0) {
			//					indent();
			//					genLanguage.appendContentln(line);
			//				}
			//			}
			indent();
			genLanguage.appendContent("// << END RAW CODE\n");

			lastEntityCouldNeedSemicolon = false;
			return true;
		}else {
			System.err.println("#C++ generator : @atLang : language not supported '" + jsg_2_AtLang.getLanguage()+"'");
		}

		lastEntityCouldNeedSemicolon = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Block jsg_2_Block) {
		genLanguage.appendContentln("{");

		nbSpaceIndent+=4;

		if (jsg_2_Block.getBody() != null) {
			if (!(jsg_2_Block.getBody() instanceof JSG_2_Sequence))
				indent(); // on laisse la sequence gerer toute son indentation
			try {
				jsg_2_Block.getBody().visit(this);
			} catch (Exception e) {
				System.err.println(e);
			}
			if (!(jsg_2_Block.getBody() instanceof JSG_2_Sequence) && lastEntityCouldNeedSemicolon) {
				genLanguage.appendContentln(";"); // la sequence fait tout les jumpLine
			}
		}		

		nbSpaceIndent-=4;
		indent();
		genLanguage.appendContentln("}");

		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_Boolean jsg_2_Boolean) {
		genLanguage.appendContent(jsg_2_Boolean.getValue());
		//		lastEntityCouldNeedSemicolon = false; // on laisse le precedent replir ce champs donc on laisse le cache tel quel
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Break jsg_2_Break) {
		genLanguage.appendContent("break");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Call jsg_2_Call) {
		genLanguage.appendContent(jsg_2_Call.getName());
		genLanguage.appendContent("(");
		final List<JSG_2_Entity> args = jsg_2_Call.getArguments();
		for (int i = 0; i < args.size(); i++) {
			final JSG_2_Entity e = args.get(i);
			e.visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		genLanguage.appendContent(")");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_CallListSize jsg_2_CallListSize) {
		genLanguage.appendContent("size()");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_CallRuleResHeight jsg_2_CallRuleResHeight) {
		genLanguage.appendContent("getHeight()");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_CallRuleResWidth jsg_2_CallRuleResWidth) {
		genLanguage.appendContent("getWidth()");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Cast jsg_2_Cast) {
		genLanguage.appendContent("( (");
		jsg_2_Cast.getType().visit(this);
		genLanguage.appendContent(") (");
		jsg_2_Cast.getExpr().visit(this);
		genLanguage.appendContent(") )");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Catch jsg_2_Catch) {
		genLanguage.appendContent("catch(");
		if (jsg_2_Catch.getDeclar() != null) {
			jsg_2_Catch.getDeclar().visit(this);
		} else {
			genLanguage.appendContent("Exception __excp");
		}
		genLanguage.appendContent(")");
		if (jsg_2_Catch.getBlock() != null && !(jsg_2_Catch.getBlock() instanceof JSG_2_NOP))
			jsg_2_Catch.getBlock().visit(this);
		else
			genLanguage.appendContentln("{}");

		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_Choice jsg_2_Choice) {
		List<String> exceptionName = new ArrayList<>();
		for (int i = 0; i < jsg_2_Choice.getOptions().size(); i++) {
			genLanguage.appendContentln("try{");
			nbSpaceIndent+=4;
			indent();

			if (jsg_2_Choice.getVarResult() != null) {
				genLanguage.appendContent(jsg_2_Choice.getVarResult() + " = ");
			}
			jsg_2_Choice.getOptions().get(i).visit(this);
			genLanguage.appendContentln("");
			nbSpaceIndent-=4;
			indent();
			Integer exepId = 0;
			while(exceptionName.contains(exceptionNamePrefix+exepId)) {
				exepId++;
			}
			exceptionName.add(exceptionNamePrefix+exepId);
			genLanguage.appendContentln("}catch(JerboaException "+ exceptionNamePrefix+exepId +"){");
			nbSpaceIndent+=4;
		}
		for (int i = 0; i < jsg_2_Choice.getOptions().size(); i++) {
			indent();
			genLanguage.appendContentln("throw "+exceptionName.get(exceptionName.size()-1-i)+";");
			nbSpaceIndent-=4;
			indent();
			genLanguage.appendContentln("}");

		}
		if (jsg_2_Choice.getOptions().size() <= 0) {
			genLanguage.appendContentln("}");
		}
		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_Collect jsg_2_Collect) {
		genLanguage.appendContent("gmap.collect(");
		jsg_2_Collect.getNode().visit(this);
		genLanguage.appendContent(",");
		jsg_2_Collect.getOrbit().visit(this);
		genLanguage.appendContent(",");
		jsg_2_Collect.getSubOrbit().visit(this);
		genLanguage.appendContent(")");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_CollectEbd jsg_2_CollectEbd) {
		genLanguage.appendContent("gmap.");
		genLanguage.appendContent("<");
		genLanguage.appendContent(context.getModeler().getEmbedding(jsg_2_CollectEbd.getEmbedding()).getType());
		genLanguage.appendContent(">");
		genLanguage.appendContent("collect(");
		jsg_2_CollectEbd.getNode().visit(this);
		genLanguage.appendContent(",");
		jsg_2_CollectEbd.getOrbit().visit(this);
		genLanguage.appendContent(",");
		int cpt = 0;
		for (JMEEmbeddingInfo e : glue.getModeler().getEmbeddings()) {
			if (e.getName().compareToIgnoreCase(jsg_2_CollectEbd.getEmbedding()) == 0)
				break;
			cpt++;
		}
		genLanguage.appendContent(cpt);
		genLanguage.appendContent("");
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Comment jsg_2_Comment) {
		int lastLineColIndex = genLanguage.getLastContentLine().length();
		String [] lines = jsg_2_Comment.getComment().split("\n");
		for(int i=0;i<lines.length;i++) {
			String line = lines[i];
			if(i>0) {
				for(int spaceI=0; spaceI<lastLineColIndex; spaceI++) {
					genLanguage.appendContent(" ");
				}
			}
			genLanguage.appendContent("// " + line);
		}
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Constructor jsg_2_Constructor) {
		JSG_2_Entity name = jsg_2_Constructor.getName();
		if(isTypeThatIsNewedInConstructor(name)) {
			genLanguage.appendContent("new ");
		}else {
			System.out.println("not new : " + name + " " + name.getClass().getCanonicalName());
		}
		jsg_2_Constructor.getName().visit(this);
		genLanguage.appendContent("(");
		nbSpaceIndent += 2;
		final List<JSG_2_Entity> args = jsg_2_Constructor.getArguments();
		if(args.size()>0) {
			for (int i = 0; i < args.size(); i++) {
				final JSG_2_Entity e = args.get(i);
				e.visit(this);
				if (i < args.size() - 1) {
					genLanguage.appendContent(",");
				}
			}
		}else if(name instanceof JSG_2_TypeRuleResult){
			genLanguage.appendContent("this");
		}
		nbSpaceIndent -= 2;
		genLanguage.appendContent(")");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Continue jsg_2_Continue) {
		genLanguage.appendContent("continue");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Declare jsg_2_Declare) {
		JSG_2_Type varType = jsg_2_Declare.getType();
		if(varType instanceof JSG_2_TypeList) {
			genLanguage.appendContent("List< ");
			((JSG_2_TypeList) varType).getListContentType().visit(this);
			genLanguage.appendContent("> ");			
		}else {
			varType.visit(this);
		}
		
		genLanguage.appendContent(" " + jsg_2_Declare.getName());

		if (jsg_2_Declare.getValue() != null ) {
			if(jsg_2_Declare.getValue() instanceof JSG_2_Constructor) {
				genLanguage.appendContent(" = ");
				JSG_2_Constructor constructor = (JSG_2_Constructor) jsg_2_Declare.getValue();
				constructor.visit(this);
			}else{
				genLanguage.appendContent(" = ");
				jsg_2_Declare.getValue().visit(this);
			}
		}else if(varType instanceof JSG_2_TypeList
				|| varType instanceof JSG_2_TypeHookList) {//isTypeThatIsNewedInConstructor(varType)){
			genLanguage.appendContent(" = new ");
			jsg_2_Declare.getType().visit(this);
			genLanguage.appendContent("()");
		}else if(!context.isPrimitvType(varType)) {
//			genLanguage.appendContent("= null"); // NON ! sinon ça va le faire pour les exceptions dans les catch
		}

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_DeclareFunction jsg_2_DeclareFunction) {
		if(jsg_2_DeclareFunction.getReturnType()!=null) {
			jsg_2_DeclareFunction.getReturnType().visit(this);
		}else {
			genLanguage.appendContent("void");
		}
		genLanguage.appendContent(" ");
		genLanguage.appendContent(jsg_2_DeclareFunction.getName());
		genLanguage.appendContent("(");
		nbSpaceIndent+=2;
		final List<JSG_2_Entity> args = jsg_2_DeclareFunction.getArguments();
		for (int i = 0; i < args.size(); i++) {
			args.get(i).visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(",");
			}
		}
		nbSpaceIndent-=2;
		genLanguage.appendContent(")");
		boolean jumped = jsg_2_DeclareFunction.getBlock().visit(this);

		lastEntityCouldNeedSemicolon = false;
		return jumped;
	}

	@Override
	public Boolean accept(JSG_2_DeclareMark jsg_2_DeclareMark) {
		genLanguage.appendContent("int ");
		genLanguage.appendContent(jsg_2_DeclareMark.getName());
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Delete jsg_2_Delete) {
		//		if(context.getType(jsg_2_Delete.getName(), glue) instanceof JSG_2_TypeMark){
		//			(new JSG_2_JerboaKeyword(KeywordType.GMAP, jsg_2_Delete.getLine(), jsg_2_Delete.getColumn())).visit(this);
		//			genLanguage.appendContent(".freeMarker(");
		//			jsg_2_Delete.getName().visit(this);
		//			genLanguage.appendContent(")");
		//		}else {
		//			genLanguage.appendContent("delete ");
		//			jsg_2_Delete.getName().visit(this);
		//		}

		lastEntityCouldNeedSemicolon = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Double jsg_2_Double) {
		genLanguage.appendContent(jsg_2_Double.getValue());
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_DoWhile jsg_2_DoWhile) {
		genLanguage.appendContent("do");
		if(jsg_2_DoWhile.getBody().visit(this)) {
			indent();
		}
		genLanguage.appendContent("while(");
		nbSpaceIndent+=2;
		jsg_2_DoWhile.getCondition().visit(this);
		nbSpaceIndent-=2;
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_EbdParam jsg_2_Variable) {
		// TODO : pas vrai dans les process ni les scripts
		//		if(glue.getLangageType()==LanguageType.EMBEDDING)
		//			genLanguage.appendContent("parentRule->");
		genLanguage.appendContent(jsg_2_Variable.getName());
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Float jsg_2_Float) {
		genLanguage.appendContent(jsg_2_Float.getValue());
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_For jsg_2_For) {
		genLanguage.appendContent("for(");
		if (jsg_2_For.getType() != null)
			genLanguage.appendContent(jsg_2_For.getType().getTypeName().replaceAll("\\.", "::"));
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent(" ");
		if (jsg_2_For.getVariable() != null)
			genLanguage.appendContent(jsg_2_For.getVariable());
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent("=");
		if (jsg_2_For.getStart() != null)
			jsg_2_For.getStart().visit(this);
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent("; ");
		genLanguage.appendContent(jsg_2_For.getVariable());
		genLanguage.appendContent("<=");
		if (jsg_2_For.getEnd() != null)
			jsg_2_For.getEnd().visit(this);
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent("; ");
		if (jsg_2_For.getVariable() != null)
			genLanguage.appendContent(jsg_2_For.getVariable());
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent("+=");
		if (jsg_2_For.getStep() != null)
			jsg_2_For.getStep().visit(this);
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent(")");

		if (jsg_2_For.getBody() != null)
			jsg_2_For.getBody().visit(this);

		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_ForEach jsg_2_ForEach) {
		genLanguage.appendContent("for(");
		jsg_2_ForEach.getType().visit(this);
		genLanguage.appendContent(" ");
		genLanguage.appendContent(jsg_2_ForEach.getName());
		genLanguage.appendContent(": ");
		jsg_2_ForEach.getColl().visit(this);
		genLanguage.appendContent(")");
		if (jsg_2_ForEach.getBody() != null)
			jsg_2_ForEach.getBody().visit(this);
		else
			genLanguage.appendContent("{}");

		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_ForLoop jsg_2_ForLoop) {
		genLanguage.appendContent("for(");
		jsg_2_ForLoop.getInit().visit(this);
		genLanguage.appendContent("; ");
		jsg_2_ForLoop.getCond().visit(this);
		genLanguage.appendContent("; ");
		jsg_2_ForLoop.getStep().visit(this);
		genLanguage.appendContent(")");

		jsg_2_ForLoop.getBody().visit(this);

		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_FreeMarker jsg_2_FreeMarker) {
		genLanguage.appendContent("gmap.freeMarker(");
		jsg_2_FreeMarker.getMarker().visit(this);
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_GetDartId jsg_2_GetNodeId) {
		genLanguage.appendContent("getID()");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_GetEbd jsg_2_GetEbd) {
		// TODO : penser à faire les CAST sur les type dans le parcours précédent la génération dessus !
		jsg_2_GetEbd.getLeft().visit(this);
		genLanguage.appendContent(".<");
		genLanguage.appendContent(jsg_2_GetEbd.getEbdInfo().getType());
		genLanguage.appendContent(">ebd(");
		int cpt = 0;
		for (JMEEmbeddingInfo e : glue.getModeler().getEmbeddings()) {
			if (e.getName().compareToIgnoreCase(jsg_2_GetEbd.getEbdInfo().getName()) == 0)
				break;
			cpt++;
		}
		genLanguage.appendContent(cpt);
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_GetEbdId jsg_2_GetEbdId) {
		int cpt = -1;
		for (JMEEmbeddingInfo e : glue.getModeler().getEmbeddings()) {
			if (e.getName().compareToIgnoreCase(jsg_2_GetEbdId.getEbdInfo().getName()) == 0)
				break;
			cpt++;
		}
		genLanguage.appendContent(cpt); // on remplace directement par l'id
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_GetEbdName jsg_2_GetEbdName) {
		genLanguage.appendContent("\"" + jsg_2_GetEbdName.getEbdInfo().getName() + "\"");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_GetEbdOrbit jsg_2_GetEbdOrbit) {
		genLanguage.appendContent("modeler.getEmbedding(\"" + jsg_2_GetEbdOrbit.getEbdInfo().getName() + "\")->orbit()");
		// TODO : changer ça pour y acceder directement dans la generation
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_GetMarker jsg_2_GetMarker) {
		genLanguage.appendContent("gmap.creatFreeMarker()");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_GMapSize jsg_2_GMapSize) {
		genLanguage.appendContent("gmap.size()");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Header jsg_2_Header) {
		if (jsg_2_Header.getLanguage() == null 
				|| "java".equalsIgnoreCase(jsg_2_Header.getLanguage())
				|| "j".equalsIgnoreCase(jsg_2_Header.getLanguage())) {

			genLanguage.appendInclude("\n");
			genLanguage.appendInclude("// >> BEGIN HEADER CODE\n");
			genLanguage.appendInclude(jsg_2_Header.getCode());
			genLanguage.appendInclude("// << END HEADER CODE\n");

			lastEntityCouldNeedSemicolon = false;
			return true;
		}

		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_If jsg_2_If) {
		genLanguage.appendContent("if(");
		if (jsg_2_If.getCondition() != null)
			jsg_2_If.getCondition().visit(this);
		else
			genLanguage.appendContent(errorParseMsg);
		genLanguage.appendContent(") ");
		if (jsg_2_If.getConsequence() != null)
			jsg_2_If.getConsequence().visit(this);
		else {
			genLanguage.appendContent("{}");
		}
		if (jsg_2_If.getAlternant() != null && !(jsg_2_If.getAlternant() instanceof JSG_2_NOP)) {
			indent();
			genLanguage.appendContent("else ");
			// TODO : SI IF on ne crée pas de block superflu !
			JSG_2_Entity alt = jsg_2_If.getAlternant();
			if(alt instanceof JSG_2_Block
					&& ((JSG_2_Block)alt).getBody() instanceof JSG_2_If) {
				((JSG_2_Block)alt).getBody().visit(this);
			}else {
				alt.visit(this);
			}
		}

		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_Index jsg_2_Index) {
		JSG_2_Type leftType = context.getType(jsg_2_Index.getVariable(), glue);
		jsg_2_Index.getVariable().visit(this);
		if(leftType instanceof JSG_2_TypeList) {
			genLanguage.appendContent(".get(");
			jsg_2_Index.getIndex().visit(this);
			genLanguage.appendContent(")");

		}else {
			genLanguage.appendContent("[");
			jsg_2_Index.getIndex().visit(this);
			genLanguage.appendContent("]");
		}
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_IndexInJerboaType jsg_2_IndexInJerboaType) {
		List<JSG_2_Entity> argList = jsg_2_IndexInJerboaType.getArgList();

		int nbArgMax = 0;
		int argumentConsumed = 0;
		switch (jsg_2_IndexInJerboaType.getType()) {
		case GMAP:
			jsg_2_IndexInJerboaType.getLeft().visit(this);
			genLanguage.appendContent(".getNode(");
			nbArgMax=1;

			break;
		case SCRIPT_HOOK:
			genLanguage.appendContent("( (JerboaInputHooksGeneric) ");
			jsg_2_IndexInJerboaType.getLeft().visit(this);
			genLanguage.appendContent(" ).");
			if(jsg_2_IndexInJerboaType.getArgList().size()==1) {
				genLanguage.appendContent("getCol(");
			}else {
				genLanguage.appendContent("dart(");
			}
			nbArgMax=2;

			break;
		case LEFT_PATTERN:
			jsg_2_IndexInJerboaType.getLeft().visit(this);
			nbArgMax=1;
			if(argList.size()>0) {
				if(glue.getLangagesState() == LanguageState.MIDPROCESS) {
					genLanguage.appendContent(".get(");
					argList.get(0).visit(this);
					genLanguage.appendContent(").get(");
					argumentConsumed=1;

				}else if(glue.getLangagesState() == LanguageState.PRECONDITION
						|| glue.getLangagesState() == LanguageState.PREPROCESS) {
					genLanguage.appendContent(".dart(");
					nbArgMax=2;
				}else if(glue.getLangageType() == LanguageType.SCRIPT && glue.getLangagesState() == LanguageState.CLASSICAL){
					genLanguage.appendContent(".get(");
					nbArgMax=2;
				}else {
					genLanguage.appendContent(".get(");
				}
			}
			break;
		case RIGHT_PATTERN:
			genLanguage.appendContent("// TODO : JSG_2_IndexInJerboaType : no translation for RIGHT PATTERN");
			break;
		case RULE_RESULT:
			// ici on ne décrit pas le membre gauche qui n'est pas static et qui dépend d'une autre expression
			jsg_2_IndexInJerboaType.getLeft().visit(this);
			genLanguage.appendContent(".get(");
			//			genLanguage.appendContent("/*"+argList.size()+" : " + argumentConsumed + " */");

			nbArgMax=2;
			break;
		default:
			break;
		}


		for(int i=0; (i<nbArgMax) && (i<argList.size()); i++) {
			argList.get(argumentConsumed).visit(this);
			argumentConsumed++;
			if(i < nbArgMax - 1 && (i<argList.size()-1)) {
				genLanguage.appendContent(",");
			}
		}

		genLanguage.appendContent(")");

		for(int i=argumentConsumed;i<argList.size();i++) {
			genLanguage.appendContent(".get(");
			argList.get(i).visit(this);
			genLanguage.appendContent(")");
		}
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Indirection jsg_2_Indirection) {
		jsg_2_Indirection.getExp().visit(this);
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_InScope jsg_2_InScope) {
		jsg_2_InScope.getLeft().visit(this);
		genLanguage.appendContent(".");
		return jsg_2_InScope.getRight().visit(this);
	}

	@Override
	public Boolean accept(JSG_2_Integer jsg_2_Integer) {
		genLanguage.appendContent(jsg_2_Integer.getValue());
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_IsMarked jsg_2_IsMarked) {
		jsg_2_IsMarked.getLeft().visit(this);
		genLanguage.appendContent(".isMarked(");
		jsg_2_IsMarked.getMark().visit(this);
		genLanguage.appendContent(")");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_IsNotMarked jsg_2_IsNotMarked) {
		jsg_2_IsNotMarked.getLeft().visit(this);
		genLanguage.appendContent(".isNotMarked(");
		jsg_2_IsNotMarked.getMark().visit(this);
		genLanguage.appendContent(")");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_JerboaKeyword jsg_2_JerboaKeyword) {
		switch (jsg_2_JerboaKeyword.getType()) {
		case DIMENSION:
			genLanguage.appendContent(glue.getModeler().getDimension());
			break;
		case GMAP:
			genLanguage.appendContent("gmap");
			break;
		case HOOK:
			genLanguage.appendContent("hooks");
			break;

		case LEFTPATTERN:
			if (glue.getLangageType() == LanguageType.SCRIPT && glue.getLangagesState() == LanguageState.CLASSICAL) {
				genLanguage.appendContent("hooks");
			}else {
				genLanguage.appendContent("leftPattern");
			}
			break;
		case MODELER:
			JMEModeler modeler = glue.getModeler();
			if (modeler != null) {
				genLanguage.appendContent("((");
				genLanguage.appendContent(modeler.getModule());
				if (modeler.getModule().replaceAll("\\s", "").length() > 0) {
					genLanguage.appendContent(".");
				}
				genLanguage.appendContent(modeler.getName());
				genLanguage.appendContent(")");
			}
			genLanguage.appendContent("modeler");
			if (modeler != null) {
				genLanguage.appendContent(")");
			}
			break;
		case RIGHTPATTERN:
			genLanguage.appendContent("rightPattern");//"// JSG_2_JerboaKeyword : no translation for RIGHTPATTER type ");

			break;
		default:
			break;
		}


		lastEntityCouldNeedSemicolon = true;
		return false;
	}


	@Override
	public Boolean accept(@SuppressWarnings("rawtypes") JSG_2_Literal jsg_2_Literal) {
		genLanguage.appendContent(jsg_2_Literal.getValue());
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Long jsg_2_Long) {
		genLanguage.appendContent(jsg_2_Long.getValue());
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Map jsg_2_Map) {
		// TODO Auto-generated method stub
		System.err.println("map has not been yet implemented");
		lastEntityCouldNeedSemicolon = true;
		return null;
	}

	@Override
	public Boolean accept(JSG_2_Mark jsg_2_Mark) {
		(new JSG_2_JerboaKeyword(KeywordType.GMAP, jsg_2_Mark.getLine(), jsg_2_Mark.getColumn())).visit(this);
		genLanguage.appendContent(".");
		if (jsg_2_Mark.getLeft() instanceof JSG_2_Collect) {
			genLanguage.appendContent("markOrbit(");
			((JSG_2_Collect) jsg_2_Mark.getLeft()).getNode().visit(this);
			genLanguage.appendContent(",");
			((JSG_2_Collect) jsg_2_Mark.getLeft()).getOrbit().visit(this);
			genLanguage.appendContent(", ");
			jsg_2_Mark.getMark().visit(this);
		} else {
			genLanguage.appendContent("mark(");
			jsg_2_Mark.getMark().visit(this);
			genLanguage.appendContent(", ");
			jsg_2_Mark.getLeft().visit(this);
		}
		genLanguage.appendContent(")");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_New jsg_2_New) {
		JSG_2_Entity exp = jsg_2_New.getExp();
		if(exp instanceof JSG_2_New
				|| (exp instanceof JSG_2_Constructor && isTypeThatIsNewedInConstructor(((JSG_2_Constructor)exp).getName()) ) ) {
			lastEntityCouldNeedSemicolon = true;
			return jsg_2_New.getExp().visit(this);
		}else {
			genLanguage.appendContent("new ");
			jsg_2_New.getExp().visit(this);
			lastEntityCouldNeedSemicolon = true;
		}
		return false;
	}

	@Override
	public Boolean accept(JSG_2_NOP jsg_2_NOP) {
		genLanguage.appendContent("// NOP\n");
		lastEntityCouldNeedSemicolon = false;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Not jsg_2_Not) {
		genLanguage.appendContent("!");
		jsg_2_Not.getExpr().visit(this);
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Null jsg_2_Null) {
		genLanguage.appendContent("null");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Operator jsg_2_Operator) {
		if (!(jsg_2_Operator.getOperator() == JSOperatorKind.INC || jsg_2_Operator.getOperator() == JSOperatorKind.DEC)) {
			genLanguage.appendContent("(");
		}
		// TODO : faire des sauts de ligne pour ne pas mettre des operation sur une seule ligne
		boolean first = true;
		for (final JSG_2_Entity e : jsg_2_Operator) {
			if (first) {
				first = false;
			} else {
				genLanguage.appendContent(" " + jsg_2_Operator.getOperator().toCode() + " ");
			}
			if (e != null)
				e.visit(this);
		}
		if (!(jsg_2_Operator.getOperator() == JSOperatorKind.INC || jsg_2_Operator.getOperator() == JSOperatorKind.DEC)) {
			genLanguage.appendContent(")");
		}

		lastEntityCouldNeedSemicolon = true;		
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Orbit jsg_2_Orbit) {
		genLanguage.appendContent("JerboaOrbit.orbit(");
		final List<JSG_2_Entity> dims = jsg_2_Orbit.getDimensions();
		if (dims.size() > 0) {
			for (int i = 0; i < dims.size(); i++) {
				final JSG_2_Entity e = dims.get(i);
				e.visit(this);
				if (i < dims.size() - 1) {
					genLanguage.appendContent(",");
				}
			}
		}
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_PackagedType jsg_2_PackagedType) {
		jsg_2_PackagedType.getLeft().visit(this);
		genLanguage.appendContent(".");
		return jsg_2_PackagedType.getRight().visit(this);
	}

	@Override
	public Boolean accept(JSG_2_Print jsg_2_Print) { 
		// TODO : il faudait pouvoir expliciter le type de sorte : err ou out
		// on pourrait peut etre même proposer une sortie "pop" pour afficher un popup ?
		genLanguage.appendContent("System.out.print(");
		final List<JSG_2_Entity> args = jsg_2_Print.getArguments();
		for (int i = 0; i < args.size(); i++) {
			final JSG_2_Entity e = args.get(i);
			e.visit(this);
			if (i < args.size() - 1) {
				genLanguage.appendContent(" + ");
			}
		}
		genLanguage.appendContent(")");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Return jsg_2_Return) {
		genLanguage.appendContent("return ");
		jsg_2_Return.getExpression().visit(this);
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Rule jsg_2_Rule) {
		String ruleName = "";
		if(jsg_2_Rule.getName()==null) {
			ruleName = glue.getCurrentRule().getName().replaceAll(" ", "_");
		}else {
			ruleName = jsg_2_Rule.getName().replaceAll(" ", "_");
		}
		genLanguage.appendContent("((" + ruleName + ")modeler.getRule(\"" + ruleName + "\"))");

		if(jsg_2_Rule.getRule()!=null && !genLanguage.getInclude().contains("."+ruleName+";")) {

			genLanguage.appendInclude("import "+getRulePath(jsg_2_Rule.getRule())+";\n");
		}


		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_RuleArg jsg_2_RuleArg) {
		if(jsg_2_RuleArg.getArgName()==null || jsg_2_RuleArg.getArgName().length()==0) {
			jsg_2_RuleArg.getArgValue().visit(this);
		}
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_RuleNodeId jsg_2_RuleNode) {
		int index = -1;

		JMEGraph graph = null;
		switch (jsg_2_RuleNode.getGraphside()) {
		case RIGHT:
			graph = jsg_2_RuleNode.getRule().getRight();
			break;
		case LEFT:
		default:
			graph = jsg_2_RuleNode.getRule().getLeft();
			break;
		}


		List<JMENode> nodes = graph.getNodes();
		for(int i=0; i<nodes.size(); i++) {
			JMENode n = nodes.get(i);
			if(n.getName().compareTo(jsg_2_RuleNode.getNodeName())==0) {
				index = i;
			}
		}
		genLanguage.appendContent(index+"");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Sequence jsg_2_Sequence) {
		boolean r = true;
		for (final JSG_2_Entity inst : jsg_2_Sequence) {
			if (inst != null) {
				if (r && !(inst instanceof JSG_2_Sequence)) {
					indent();
				}
				r = inst.visit(this);
				if (lastEntityCouldNeedSemicolon) {
					genLanguage.appendContentln(";");
					r = true;
				}
			}
		}
		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_String jsg_2_String) {

		int lastLineColIndex = genLanguage.getLastContentLine().length();
		String[] lines = jsg_2_String.getValue().split("\n");
		for(int i=0; i<lines.length; i++) {
			String l = lines[i];
			if(i>0) {
				for(int spaceI=0; spaceI<lastLineColIndex; spaceI++) {
					genLanguage.appendContent(" ");
				}
				genLanguage.appendContent("+ ");
			}
			genLanguage.appendContent("\"");
			genLanguage.appendContent(l);
			genLanguage.appendContent("\"");
		}

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Throw jsg_2_Throw) {
		genLanguage.appendContent("throw ");
		jsg_2_Throw.getExpr().visit(this);
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Try jsg_2_Try) {
		genLanguage.appendContent("try");
		if(jsg_2_Try.getTryBlock().visit(this)) {
			indent();
		}
		if (jsg_2_Try.getCatchList() != null) {
			for (JSG_2_Catch c : jsg_2_Try.getCatchList()) {
				c.visit(this);
			}
		}
		if (jsg_2_Try.getFinallyBlock() != null) {
			indent();
			genLanguage.appendContent("finally");
			if (!(jsg_2_Try.getFinallyBlock() instanceof JSG_2_NOP))
				jsg_2_Try.getFinallyBlock().visit(this);
			else
				genLanguage.appendContent("{}");

		}
		genLanguage.appendContentln("");
		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public Boolean accept(JSG_2_Type jsg_2_Type) {
		//		genLanguage.appendContent("/*-- "+jsg_2_Type.getClass().getCanonicalName()  +"*/");
		genLanguage.appendContent(jsg_2_Type.getTypeName());
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeBoolean jsg_2_TypeBoolean) {
		genLanguage.appendContent("Boolean ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeDart jsg_2_Type) {
		genLanguage.appendContent("JerboaDart ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeEmbedding jsg_2_Type) {
		if(jsg_2_Type.getEbdName()==null || jsg_2_Type.getEbdName().length()==0) {
			genLanguage.appendContent("Object ");
		}else {
			genLanguage.appendContent(context.getModeler().getEmbedding(jsg_2_Type.getEbdName()).getType());
		}
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeGmap jsg_2_Type) {
		genLanguage.appendContent("JerboaGMap ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeHookList jsg_2_TypeHookList) {
		genLanguage.appendContent("JerboaInputHooksGeneric ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeList jsg_2_Type) {
		genLanguage.appendContent("ArrayList<");
		if(jsg_2_Type.getListContentType()!=null)
			jsg_2_Type.getListContentType().visit(this);
		genLanguage.appendContent("> ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeMark jsg_2_Type) {
		genLanguage.appendContent("JerboaMark ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeModeler jsg_2_Type) {
		genLanguage.appendContent("JerboaModeler ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeOrbit jsg_2_Type) {
		genLanguage.appendContent("JerboaOrbit ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypePointer jsg_2_TypePointer) {
		JSG_2_Type type = jsg_2_TypePointer.getType();
		type.visit(this);
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeRule jsg_2_Type) {
		genLanguage.appendContent("JerboaOperation ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeRuleResult jsg_2_Type) {
		genLanguage.appendContent("JerboaRuleResult ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeString jsg_2_TypeString) {
		genLanguage.appendContent("String ");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_TypeTemplate jsg_2_Type) {
		genLanguage.appendContent(jsg_2_Type.getTypeName());
		genLanguage.appendContent("<");
		for (int i = 0; i < jsg_2_Type.getTemplateTypeList().size(); i++) {
			jsg_2_Type.getTemplateTypeList().get(i).visit(this);
			if (i < jsg_2_Type.getTemplateTypeList().size() - 1) {
				genLanguage.appendContent(", ");
			}
		}
		genLanguage.appendContent(">");
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_UnMark jsg_2_UnMark) {
		(new JSG_2_JerboaKeyword(KeywordType.GMAP, jsg_2_UnMark.getLine(), jsg_2_UnMark.getColumn()))
		.visit(this);
		genLanguage.appendContent(".");
		if (jsg_2_UnMark.getLeft() instanceof JSG_2_Collect) {
			genLanguage.appendContent("unmarkOrbit(");
			((JSG_2_Collect) jsg_2_UnMark.getLeft()).getNode().visit(this);
			genLanguage.appendContent(", ");
			((JSG_2_Collect) jsg_2_UnMark.getLeft()).getOrbit().visit(this);
			genLanguage.appendContent(",");
			jsg_2_UnMark.getMark().visit(this);
		} else {
			genLanguage.appendContent("unmark(");
			jsg_2_UnMark.getMark().visit(this);
			genLanguage.appendContent(", ");
			jsg_2_UnMark.getLeft().visit(this);
		}
		genLanguage.appendContent(")");

		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Unreference jsg_2_Unreference) {
		jsg_2_Unreference.getExp().visit(this);
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_Variable jsg_2_Variable) {
		//TODO : faire une classe pour les parametres topo
		// TODO : vérifier si on est dans un endroit ou les hooks sont accessibles !! LanguageState
		//		switch (glue.getLangageType()) {
		//		case MODELER:	
		//			genLanguage.appendContent(jsg_2_Variable.getName());		
		//			break;
		//		default:
		//			boolean hookFound = false;
		//			JMERule rule = glue.getCurrentRule();
		//			if(rule!=null) {
		//				for(JMENode hook : rule.getHooks()) {
		//					if(hook.getName().compareTo(jsg_2_Variable.getName())==0) {
		//						hookFound = true;
		//						genLanguage.appendContent(jsg_2_Variable.getName()+"()");
		//					}
		//				}
		//			}
		//			if(!hookFound)
		//				genLanguage.appendContent(jsg_2_Variable.getName());
		//			break;
		//		}
		genLanguage.appendContent(jsg_2_Variable.getName());
		lastEntityCouldNeedSemicolon = true;
		return false;
	}

	@Override
	public Boolean accept(JSG_2_While jsg_2_While) {
		genLanguage.appendContent("while(");
		jsg_2_While.getCondition().visit(this);
		genLanguage.appendContent(")");
		jsg_2_While.getBody().visit(this);
		genLanguage.appendContentln("");
		lastEntityCouldNeedSemicolon = false;
		return true;
	}

	@Override
	public void beginGeneration(JSG_2_Entity js) {
		genLanguage = new GeneratedLanguage();
		js.visit(this);
	}

	@Override
	public GeneratedLanguage getResult() {
		return genLanguage;
	}

	private void indent() {
//		genLanguage.appendContent("/*");
		for (int i = 0; i < nbSpaceIndent; i++) {
			genLanguage.appendContent(" ");
		}
//		genLanguage.appendContent("*/");
	}

	public boolean isTypeThatIsNewedInConstructor(JSG_2_Entity constructorName) {
		boolean needNewInConstructeur = 
				constructorName instanceof JSG_2_TypeList
				|| 	constructorName instanceof JSG_2_TypeEmbedding
				|| 	constructorName instanceof JSG_2_TypeHookList
				||  constructorName instanceof JSG_2_TypeRuleResult; 
				;
		if(constructorName instanceof JSG_2_TypeTemplate) {
			JSG_2_TypeTemplate template = (JSG_2_TypeTemplate) constructorName;
//			System.err.println("try template on  " + template.getTemplateTypeList());
			return isTypeThatIsNewedInConstructor(template.getTypeTemplate());
		}
		return needNewInConstructeur;
	}

	//	public static void main(String[] argv) {
	//		List<JSG_2_Type> typeList = new ArrayList<>();
	//		typeList.add(new JSG_2_TypeDart(0, 0));
	//		typeList.add(new JSG_2_TypeDart(0, 0));
	//		
	//		JSG_2_TypeTemplate t = new JSG_2_TypeTemplate("ArrayList", typeList, 0, 0);
	//		
	//		GeneratedLanguage res = new GeneratedLanguage();
	//		JSG_2_GeneratorJava gen = new JSG_2_GeneratorJava(null, null, res, null);
	//		t.visit(gen);
	//		System.out.println(gen.getResult().getContent());
	//
	//		System.out.println(t.toString());
	//	}

}
