package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.translator;

import java.util.ArrayList;
import java.util.List;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEEmbeddingInfo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
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
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.JSG_2_EntityVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntaxV2.tools.TranslatorContextV2;

public class SpecificOperatorRemover_V2 implements JSG_2_EntityVisitor<ExpressionTraductionV2, RuntimeException> {

	TranslatorContextV2 context;
	LanguageGlue glue;

	public SpecificOperatorRemover_V2(LanguageGlue _glue, TranslatorContextV2 context) {
		this.context = context;
		glue = _glue;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_AddInHookList jsg_2_AddInHookList) {
		return new ExpressionTraductionV2(jsg_2_AddInHookList);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_AddInList jsg_2_AddInList) {
		return new ExpressionTraductionV2(jsg_2_AddInList);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Alpha jsg_2_Alpha) {
		return new ExpressionTraductionV2(jsg_2_Alpha);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_ApplyRule jsg_2_ApplyRule) {
		final int l = jsg_2_ApplyRule.getLine();
		final int c = jsg_2_ApplyRule.getColumn();
		JSG_2_Sequence seq = new JSG_2_Sequence(jsg_2_ApplyRule.getLine(), jsg_2_ApplyRule.getColumn());

		String hookListName = getFreshVariableName("hook");
		JSG_2_TypeHookList typeHook = new JSG_2_TypeHookList(jsg_2_ApplyRule.getRule(), l, c);
		seq.add(new JSG_2_Declare(typeHook, hookListName, null, l, c));

		ArrayList<JSG_2_Entity> args = new ArrayList<>();
		JSG_2_Variable hookVar = new JSG_2_Variable(hookListName, l, c);
		args.add(hookVar);

		//		boolean namedParameters = false;
		//		if (jsg_2_ApplyRule.getArgs().size() > 0 && jsg_2_ApplyRule.getArgs().get(0) instanceof JSG_2_AssocParam) {
		//			namedParameters = true;
		//		}
		JMERule rule = jsg_2_ApplyRule.getRule();
		if (rule == null) {
			System.err.println("No rule found for expression apply rule : (" + jsg_2_ApplyRule.getLine() + ","
					+ jsg_2_ApplyRule.getColumn() + ") " + jsg_2_ApplyRule.getRuleExpr());
			return new ExpressionTraductionV2(new JSG_2_ApplyRule(jsg_2_ApplyRule.getRule(), 
					jsg_2_ApplyRule.getRuleExpr(), args,
					jsg_2_ApplyRule.getReturnType(), 
					jsg_2_ApplyRule.getLine(), 
					jsg_2_ApplyRule.getColumn()));

		}
		int nbTopoParam = 0;
		if (rule.getParamsTopo() != null)
			nbTopoParam = rule.getParamsTopo().size();

		int nbEbdParam = 0;
		if (rule.getParamsEbd() != null)
			nbEbdParam = rule.getParamsEbd().size();

		int cptParam = 0;
		for (JSG_2_Entity e : jsg_2_ApplyRule.getArgs()) {
			ExpressionTraductionV2 et = e.visit(this);
			for (JSG_2_Entity i : et.getInstructions()) {
				seq.add(i);
			}
			//			if (namedParameters) {
			//				if (e instanceof JSG_AssocParam) {
			//					if (ruleHasTopoParam(rule, ((JSG_AssocParam) e).getParamName())) {
			//						ArrayList<JSG_Expression> listTmp = new ArrayList<JSG_Expression>();
			//						listTmp.add(new JSG_IndexRuleNode(SIDE.LEFT, rule, ((JSG_AssocParam) e).getParamName(),
			//								jsg_2_ApplyRule.getLine(), jsg_2_ApplyRule.getColumn()));
			//						listTmp.add(e);
			//						seq.add(new JSG_ExprInstruction(new JSG_InScope(hookVar, new JSG_AddInHookList(listTmp))));
			//					} else {
			//						seq.add(new JSG_ExprInstruction(e)); // TODO: vÃ©rifier
			//						// Ã§a
			//					}
			//				} else {
			//
			//				}
			//			} else {
			if (nbTopoParam > cptParam) {
				ArrayList<JSG_2_Entity> listTmp = new ArrayList<>();
				listTmp.add(e);
				seq.add(new JSG_2_InScope(hookVar, new JSG_2_AddInHookList(listTmp, hookVar.getLine(), hookVar.getColumn()), false, hookVar.getLine(), hookVar.getColumn()));
			} else if (nbTopoParam + nbEbdParam > cptParam) {
				JSG_2_Entity expParam = e;
				if (expParam instanceof JSG_2_AssocParam) {
					expParam = ((JSG_2_AssocParam) expParam).getParamValue();
				}
				seq.add(new JSG_2_AssocParam(
						new JSG_2_Rule(rule.getName(), jsg_2_ApplyRule.getRule(), jsg_2_ApplyRule.getLine(),
								jsg_2_ApplyRule.getColumn()),
						expParam, rule.getParamsEbd().get(cptParam - nbTopoParam).getName(), l, c));
			}
			cptParam++;
		}

		ExpressionTraductionV2 exprTrad = new ExpressionTraductionV2(
				new JSG_2_ApplyRule(jsg_2_ApplyRule.getRule(), jsg_2_ApplyRule.getRuleExpr(), args, jsg_2_ApplyRule.getReturnType(),
						jsg_2_ApplyRule.getLine(), jsg_2_ApplyRule.getColumn()));

		for (JSG_2_Entity i : seq) {
			exprTrad.addInstruction(i);
		}

		context.assignVar(hookListName, typeHook, typeHook); 
		// TODO: bizarre de mettre le type en valeur et vis versa

		return exprTrad;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Assignment jsg_2_Assignment) {
		int l = jsg_2_Assignment.getLine();
		int c = jsg_2_Assignment.getColumn();

		ExpressionTraductionV2 result = jsg_2_Assignment.getValue().visit(this);
		
		ExpressionTraductionV2 expTrad = jsg_2_Assignment.getValue().visit(this);
		result.addInstruction(expTrad.getInstructions());
		
		if ( (jsg_2_Assignment.getValue() instanceof JSG_2_Choice) ) {
			result.setExpression(new JSG_2_Comment("Probleme de traduction de choice ? ", l, c));
		} else {
			result.setExpression(new JSG_2_Assignment(jsg_2_Assignment.getVariable(), expTrad.getExpression(), l, c));
		}
		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_AssocParam jsg_2_AssocParam) {
		int l = jsg_2_AssocParam.getLine();
		int c = jsg_2_AssocParam.getColumn();
		// TODO Auto-generated method stub
		ExpressionTraductionV2 val = jsg_2_AssocParam.getParamValue().visit(this);
		ExpressionTraductionV2 rule = jsg_2_AssocParam.getRule().visit(this);
		
		JSG_2_AssocParam assoc = new JSG_2_AssocParam((JSG_2_Rule)rule.getExpression(), val.getExpression(), jsg_2_AssocParam.getParamName(), l, c);
		List<JSG_2_Entity> prev = new ArrayList<>();
		prev.addAll(rule.getInstructions());
		prev.addAll(val.getInstructions());
		return new ExpressionTraductionV2(prev, assoc);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_AtLang jsg_2_AtLang) {
		return new ExpressionTraductionV2(jsg_2_AtLang);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Block jsg_2_Block) {
		context.beginBlock();
		ExpressionTraductionV2 content = jsg_2_Block.getBody().visit(this);
		context.endBlock();
		ExpressionTraductionV2 res = new ExpressionTraductionV2(new JSG_2_Block(content.getExpression(), jsg_2_Block.hasBracket(), jsg_2_Block.getLine(), jsg_2_Block.getColumn()));
		res.addInstruction(content.getInstructions());
		return res;				
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Boolean jsg_2_Boolean) {
		return new ExpressionTraductionV2(jsg_2_Boolean);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Break jsg_2_Break) {
		return new ExpressionTraductionV2(jsg_2_Break);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Call jsg_2_Call) {
		ArrayList<JSG_2_Entity> listInstruction = new ArrayList<>();
		ArrayList<JSG_2_Entity> listexprArg = new ArrayList<>();
		for (JSG_2_Entity e : jsg_2_Call.getArguments()) {
			if(e != null) {
				ExpressionTraductionV2 eET = e.visit(this);
				listInstruction.addAll(eET.getInstructions());
				listexprArg.add(eET.getExpression());
			} else {
				ExpressionTraductionV2 eET = new ExpressionTraductionV2(new JSG_2_Null(jsg_2_Call.getLine(), jsg_2_Call.getColumn()));
				listInstruction.addAll(eET.getInstructions());
				listexprArg.add(eET.getExpression());
			}
		}
		ExpressionTraductionV2 res = new ExpressionTraductionV2(new JSG_2_Call(jsg_2_Call.getName(), listexprArg, jsg_2_Call.getLine(), jsg_2_Call.getColumn()));
		res.addInstruction(listInstruction);
		return res;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_CallListSize jsg_2_CallListSize) {
		return new ExpressionTraductionV2(jsg_2_CallListSize);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_CallRuleResHeight jsg_2_CallRuleResHeight) {
		return new ExpressionTraductionV2(jsg_2_CallRuleResHeight);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_CallRuleResWidth jsg_2_CallRuleResWidth) {
		return new ExpressionTraductionV2(jsg_2_CallRuleResWidth);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Cast jsg_2_Cast) {
		return new ExpressionTraductionV2(jsg_2_Cast);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Catch jsg_2_Catch) {
		ExpressionTraductionV2 block = jsg_2_Catch.getBlock().visit(this);
		ExpressionTraductionV2 excepDeclaration = jsg_2_Catch.getDeclar().visit(this);

		ExpressionTraductionV2 res = new ExpressionTraductionV2(new JSG_2_Catch(block.getExpression(), 
				excepDeclaration.getExpression(),
				jsg_2_Catch.getLine(), jsg_2_Catch.getColumn()) );
		res.addInstruction(block.getInstructions());
		res.addInstruction(excepDeclaration.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Choice jsg_2_Choice) {
		int l = jsg_2_Choice.getLine();
		int c = jsg_2_Choice.getColumn();
		ArrayList<ExpressionTraductionV2> listResult = new ArrayList<>();
		for (JSG_2_Entity e : jsg_2_Choice.getOptions()) {
			listResult.add(e.visit(this));
		}
		// On suppose qu'il y a au moins une expression sinon c'est pas un choice (meme
		// pour 1ere l'est pas en fait).
		JSG_2_Sequence seqTry = new JSG_2_Sequence(l, c);
		seqTry.addAll(listResult.get(0).getInstructions());
		if (jsg_2_Choice.getVarResult() != null && jsg_2_Choice.getVarResult().length() > 0) {
			seqTry.add(new JSG_2_Assignment(new JSG_2_Variable(jsg_2_Choice.getVarResult(), l, c),
					listResult.get(0).getExpression(), l, c));
		} else {
			seqTry.add(listResult.get(0).getExpression());
		}
		JSG_2_Entity last = null;

		for (int i = listResult.size() - 1; i >= 0; i--) {
			JSG_2_Sequence seqCatch = new JSG_2_Sequence(jsg_2_Choice.getLine(), jsg_2_Choice.getColumn());
			seqCatch.addAll(listResult.get(i).getInstructions());
			if (jsg_2_Choice.getVarResult() != null && jsg_2_Choice.getVarResult().length() > 0) {
				seqCatch.add(new JSG_2_Assignment(
						new JSG_2_Variable(jsg_2_Choice.getVarResult(), jsg_2_Choice.getLine(), jsg_2_Choice.getColumn()),
						listResult.get(i).getExpression(), jsg_2_Choice.getLine(), jsg_2_Choice.getColumn()));
			} else {
				seqCatch.add(listResult.get(i).getExpression());
			}
			JSG_2_Catch blockCatch = new JSG_2_Catch(new JSG_2_Block(last != null ? last : new JSG_2_NOP(l, c), true, l, c),
					new JSG_2_Declare(new JSG_2_Type("JerboaException", l, c), getFreshVariableName("_exeption"),
							null, l, c),
					l, c);
			ArrayList<JSG_2_Catch> catcherList = new ArrayList<>();
			catcherList.add(blockCatch);
			last = new JSG_2_Try(new JSG_2_Block(seqCatch, true, l, c), catcherList, null, l, c);
		}
		return new ExpressionTraductionV2(last, null);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Collect jsg_2_Collect) {
		ExpressionTraductionV2 orb = jsg_2_Collect.getOrbit().visit(this);
		ExpressionTraductionV2 sorb = jsg_2_Collect.getSubOrbit().visit(this);
		ExpressionTraductionV2 node = jsg_2_Collect.getNode().visit(this);
		JSG_2_Collect collect = new JSG_2_Collect((JSG_2_Orbit) orb.getExpression(), 
				(JSG_2_Orbit) sorb.getExpression(),
				node.getExpression(), 
				jsg_2_Collect.gmapHasDirectAccess(), 
				jsg_2_Collect.getLine(), jsg_2_Collect.getColumn());
		ExpressionTraductionV2 res = new ExpressionTraductionV2(collect);
		res.addInstruction(orb.getInstructions());
		res.addInstruction(sorb.getInstructions());
		res.addInstruction(node.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_CollectEbd jsg_2_CollectEbd) {
		ExpressionTraductionV2 orb = jsg_2_CollectEbd.getOrbit().visit(this);
		ExpressionTraductionV2 node = jsg_2_CollectEbd.getNode().visit(this);
		JSG_2_CollectEbd collect = new JSG_2_CollectEbd((JSG_2_Orbit)orb.getExpression(), 
				jsg_2_CollectEbd.getEmbedding(),
				node.getExpression(), 
				jsg_2_CollectEbd.gmapHasDirectAccess(), 
				jsg_2_CollectEbd.getLine(), jsg_2_CollectEbd.getColumn());
		ExpressionTraductionV2 res = new ExpressionTraductionV2(collect);
		res.addInstruction(orb.getInstructions());
		res.addInstruction(node.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Comment jsg_2_Comment) {
		return new ExpressionTraductionV2(jsg_2_Comment);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Constructor jsg_2_Constructor) {
		ArrayList<JSG_2_Entity> listInstr = new ArrayList<>();
		ArrayList<JSG_2_Entity> param = new ArrayList<>();
		for (JSG_2_Entity e : jsg_2_Constructor.getArguments()) {
			ExpressionTraductionV2 re = e.visit(this);
			listInstr.addAll(re.getInstructions());
			param.add(re.getExpression());
		}
		ExpressionTraductionV2 res = new ExpressionTraductionV2(new JSG_2_Constructor(jsg_2_Constructor.getName(), param, jsg_2_Constructor.getLine(), jsg_2_Constructor.getColumn()));
		res.addInstruction(listInstr);
		return res;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Continue jsg_2_Continue) {
		return new ExpressionTraductionV2(jsg_2_Continue);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Declare jsg_2_Declare) {
		int l = jsg_2_Declare.getLine();
		int c = jsg_2_Declare.getColumn();
		
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 expTrad = new ExpressionTraductionV2(null);
		
		if (jsg_2_Declare.getValue() != null) {
			expTrad = jsg_2_Declare.getValue().visit(this);
		}else if(jsg_2_Declare.getType() instanceof JSG_2_TypeMark) {
			// Si la marque n'est pas allouee on l'alloue par défaut. Une information sera donnee dans lors des verifs
			expTrad.setExpression(new JSG_2_GetMarker(context.isGmapHasDirectAccess(), l, c));
		} 
		
		if (jsg_2_Declare.getValue() instanceof JSG_2_Choice) {
			result.addInstruction(new JSG_2_Declare(jsg_2_Declare.getType(), jsg_2_Declare.getName(), null, l, c));
			result.addInstruction(expTrad.getInstructions());
			context.assignVar(jsg_2_Declare.getName(), jsg_2_Declare.getType(), null);
			result.setExpression(expTrad.getExpression());
		} else {
			for (JSG_2_Entity i : expTrad.getInstructions()) {
				result.addInstruction(i);
			}
			
			/**** VAl : 2 solution : 1) on indirecte la value  -  2) on pointerise le type
			 * Je choisis finalement d'indirecter car a l'usage, si on fait : 
			 * 		Ebd<point> var = dart.point;
			 * 		var = var + Ebd<point>(0,1,0);
			 * l'operateur indirecte a gauche et on a donc plus de pointeur. 
			 * si rien est pointeur ici c'est mieux
			 * --> du coup il faut aussi le faire dans l'assignement
			 */
			JSG_2_Type type = jsg_2_Declare.getType();
			
			// DEBUT DE  1)
			if(context.isAPointerType(jsg_2_Declare.getValue(), glue) && !context.isAPointerType(type, glue)) {
				expTrad.setExpression(new JSG_2_Indirection(expTrad.getExpression(), l, c));
			}
			// FIN DE    1)
			
			// DEBUT DE  2)
			// if(context.isAPointerType(jsg_2_Declare.getValue()) && !context.isAPointerType(type)) {
			// 	type = new JSG_2_TypePointer(type, l, c);
			// }
			// FIN DE    2)
			result.setExpression(new JSG_2_Declare(type, jsg_2_Declare.getName(), expTrad.getExpression(), l, c));
			context.assignVar(jsg_2_Declare.getName(), type, expTrad.getExpression());
		}

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_DeclareFunction jsg_2_DeclareFunction) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		List<JSG_2_Entity> params = new ArrayList<>();

		for(JSG_2_Entity e : jsg_2_DeclareFunction.getArguments()) {
			ExpressionTraductionV2 eTrad = e.visit(this);
			params.add(eTrad.getExpression());
			result.addInstruction(eTrad.getInstructions());
		}

		ExpressionTraductionV2 block = jsg_2_DeclareFunction.getBlock().visit(this); 
		result.addInstruction(block.getInstructions());

		result.setExpression(new JSG_2_DeclareFunction(jsg_2_DeclareFunction.getReturnType(), 
				jsg_2_DeclareFunction.getName(),
				params,
				(JSG_2_Block) block.getExpression(), 
				jsg_2_DeclareFunction.getLine(), jsg_2_DeclareFunction.getColumn()));

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_DeclareMark jsg_2_DeclareMark) {
		return new ExpressionTraductionV2(jsg_2_DeclareMark);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Delete jsg_2_Delete) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 expTrad = jsg_2_Delete.getName().visit(this);
		for (JSG_2_Entity i : expTrad.getInstructions()) {
			result.addInstruction(i);
		}
		if(context.getType(jsg_2_Delete.getName(), glue) instanceof JSG_2_TypeMark) {
			result.setExpression(new JSG_2_FreeMarker(context.isGmapHasDirectAccess(), jsg_2_Delete.getName(), 
					jsg_2_Delete.getLine(), jsg_2_Delete.getColumn()));
		}else {
			result.setExpression(new JSG_2_Delete(expTrad.getExpression(), jsg_2_Delete.getLine(), jsg_2_Delete.getColumn()));
		}
		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Double jsg_2_Double) {
		return new ExpressionTraductionV2(jsg_2_Double);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_DoWhile jsg_2_DoWhile) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 block = jsg_2_DoWhile.getBody().visit(this);
		result.addInstruction(block.getInstructions());

		ExpressionTraductionV2 condition = jsg_2_DoWhile.getCondition().visit(this);
		result.addInstruction(condition.getInstructions());

		result.setExpression(new JSG_2_DoWhile(condition.getExpression(), block.getExpression(), 
				jsg_2_DoWhile.getLine(), jsg_2_DoWhile.getColumn()));

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_EbdParam jsg_2_Variable) {
		return new ExpressionTraductionV2(jsg_2_Variable);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Float jsg_2_Float) {
		return new ExpressionTraductionV2(jsg_2_Float);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_For jsg_2_For) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 block = jsg_2_For.getBody().visit(this);
		result.addInstruction(block.getInstructions());

		ExpressionTraductionV2 start = jsg_2_For.getStart().visit(this);
		result.addInstruction(start.getInstructions());

		ExpressionTraductionV2 step = jsg_2_For.getStep().visit(this);
		result.addInstruction(step.getInstructions());

		ExpressionTraductionV2 end = jsg_2_For.getEnd().visit(this);
		result.addInstruction(end.getInstructions());

		result.setExpression(new JSG_2_For(
				jsg_2_For.getType(), 
				jsg_2_For.getVariable(),
				start.getExpression(),
				end.getExpression(),
				step.getExpression(),
				block.getExpression(),
				jsg_2_For.getLine(), jsg_2_For.getColumn()));

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_ForEach jsg_2_ForEach) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		

		ExpressionTraductionV2 collection = jsg_2_ForEach.getColl().visit(this);
		result.addInstruction(collection.getInstructions());
		
		if(jsg_2_ForEach.getType() != null && jsg_2_ForEach.getName() != null) {
			context.beginBlock();
			context.assignVar(jsg_2_ForEach.getName(), jsg_2_ForEach.getType() , null);
		}
		
		ExpressionTraductionV2 block = jsg_2_ForEach.getBody().visit(this);
		result.addInstruction(block.getInstructions());
		
		if(jsg_2_ForEach.getType() != null && jsg_2_ForEach.getName() != null) {
			context.endBlock();
		}
		

		result.setExpression(new JSG_2_ForEach(
				jsg_2_ForEach.getType(), 
				jsg_2_ForEach.getName(),
				collection.getExpression(),
				block.getExpression(),
				jsg_2_ForEach.getLine(), jsg_2_ForEach.getColumn()));

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_ForLoop jsg_2_ForLoop) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 block = jsg_2_ForLoop.getBody().visit(this);
		result.addInstruction(block.getInstructions());

		ExpressionTraductionV2 init = jsg_2_ForLoop.getInit().visit(this);
		result.addInstruction(init.getInstructions());

		ExpressionTraductionV2 step = jsg_2_ForLoop.getStep().visit(this);
		result.addInstruction(step.getInstructions());

		ExpressionTraductionV2 cond = jsg_2_ForLoop.getCond().visit(this);
		result.addInstruction(cond.getInstructions());

		result.setExpression(new JSG_2_ForLoop(
				init.getExpression(),
				cond.getExpression(),
				step.getExpression(),
				block.getExpression(),
				jsg_2_ForLoop.getLine(), jsg_2_ForLoop.getColumn()));

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_FreeMarker jsg_2_FreeMarker) {
		return new ExpressionTraductionV2(jsg_2_FreeMarker);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_GetDartId jsg_2_GetNodeId) {
		return new ExpressionTraductionV2(jsg_2_GetNodeId);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_GetEbd jsg_2_GetEbd) {
		int l = jsg_2_GetEbd.getLine();
		int c = jsg_2_GetEbd.getColumn();

		ExpressionTraductionV2 leftTrad = jsg_2_GetEbd.getLeft().visit(this);

		JSG_2_GetEbd ebdGet = new JSG_2_GetEbd(leftTrad.getExpression(), jsg_2_GetEbd.getEbdInfo(), l,c);
		
		return new ExpressionTraductionV2(leftTrad.getInstructions(),
				new JSG_2_Cast(new JSG_2_TypePointer(new JSG_2_TypeEmbedding(jsg_2_GetEbd.getEbdInfo().getName(), l, c), l, c), ebdGet, l, c));
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_GetEbdId jsg_2_GetEbdId) {
		return new ExpressionTraductionV2(jsg_2_GetEbdId);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_GetEbdName jsg_2_GetEbdName) {
		return new ExpressionTraductionV2(jsg_2_GetEbdName);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_GetEbdOrbit jsg_2_GetEbdOrbit) {
		return new ExpressionTraductionV2(jsg_2_GetEbdOrbit);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_GetMarker jsg_2_GetMarker) {
		return new ExpressionTraductionV2(jsg_2_GetMarker);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_GMapSize jsg_2_GMapSize) {
		return new ExpressionTraductionV2(jsg_2_GMapSize);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Header jsg_2_Header) {
		return new ExpressionTraductionV2(jsg_2_Header);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_If jsg_2_If) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 condition = jsg_2_If.getCondition().visit(this);
		result.addInstruction(condition.getInstructions());

		ExpressionTraductionV2 cons = jsg_2_If.getConsequence().visit(this);
		result.addInstruction(cons.getInstructions());

		ExpressionTraductionV2 alt = new ExpressionTraductionV2(new JSG_2_NOP(jsg_2_If.getLine(), jsg_2_If.getColumn()));
		if(jsg_2_If.getAlternant()!=null) {
			alt = jsg_2_If.getAlternant().visit(this);
			result.addInstruction(alt.getInstructions());
		}

		result.setExpression(new JSG_2_If(
				condition.getExpression(),
				cons.getExpression(),
				alt.getExpression(),
				jsg_2_If.getLine(), jsg_2_If.getColumn()));

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Index jsg_2_Index) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 left = jsg_2_Index.getVariable().visit(this);
		result.addInstruction(left.getInstructions());

		ExpressionTraductionV2 right = jsg_2_Index.getIndex().visit(this);
		result.addInstruction(right.getInstructions());


		result.setExpression(new JSG_2_Index(
				left.getExpression(),
				right.getExpression(),
				jsg_2_Index.getLine(), jsg_2_Index.getColumn()));

		return result;
	}


	@Override
	public ExpressionTraductionV2 accept(JSG_2_IndexInJerboaType jsg_2_IndexInJerboaType) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		List<JSG_2_Entity> params = new ArrayList<>();
		for(JSG_2_Entity e : jsg_2_IndexInJerboaType.getArgList()) {
			ExpressionTraductionV2 eTrad = e.visit(this);
			result.addInstruction(eTrad.getInstructions());
			params.add(eTrad.getExpression());
		}

		
		ExpressionTraductionV2 leftTrad = null;
		if(jsg_2_IndexInJerboaType.getLeft()!=null) {
			leftTrad = jsg_2_IndexInJerboaType.getLeft().visit(this);
			result.addInstruction(leftTrad.getInstructions());
		}
		
		result.setExpression(new JSG_2_IndexInJerboaType(jsg_2_IndexInJerboaType.getType(), leftTrad.getExpression(), params, 
				jsg_2_IndexInJerboaType.getLine(), jsg_2_IndexInJerboaType.getColumn()));

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Indirection jsg_2_Indirection) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 exp = jsg_2_Indirection.getExp().visit(this);
		result.addInstruction(exp.getInstructions());
		result.setExpression(new JSG_2_Indirection(exp.getExpression(), jsg_2_Indirection.getLine(), jsg_2_Indirection.getColumn()));

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_InScope jsg_2_InScope) {
		int l = jsg_2_InScope.getLine();
		int c = jsg_2_InScope.getColumn();

		// RIGHT 

		ExpressionTraductionV2 rightTrad = jsg_2_InScope.getRight().visit(this);

		// LEFT

		ExpressionTraductionV2 leftTrad = jsg_2_InScope.getLeft().visit(this);

		JSG_2_Type leftType = context.getType(leftTrad.getExpression(), glue);

		if(!jsg_2_InScope.isStatic() && !(jsg_2_InScope.getLeft() instanceof JSG_2_Type) ) {
			if(leftType instanceof JSG_2_TypeEmbedding && !(leftTrad.getExpression() instanceof JSG_2_Cast)) {
				if(context.isAPointerType(leftTrad.getExpression(), glue) ) {
					leftTrad.setExpression(new JSG_2_Indirection(leftTrad.getExpression(), l, c) );
				}else {
					leftTrad.setExpression(new JSG_2_Cast(context.getType(leftTrad.getExpression(), glue), 
							leftTrad.getExpression(), l, c) );
				}
			}else if(context.isAPointerType(leftTrad.getExpression(), glue)){
				leftTrad.setExpression(new JSG_2_Indirection(leftTrad.getExpression(), l, c) );
				
			}else if(jsg_2_InScope.getRight() instanceof JSG_2_Variable){ 
				// SI on a : f(a).NomDuPlongement : on ne peut pas determiner le type de f() mais dans le doute on met un acces plongement : 
				// On leve du coup un warning dans les verification pour demander un cast si ce n'est pas le bon type
				JSG_2_Variable var = (JSG_2_Variable) jsg_2_InScope.getRight();
				for(JMEEmbeddingInfo einf : context.getModeler().getEmbeddings()) {
					if(einf.getName().compareTo(var.getName())==0) {
						JSG_2_GetEbd getEbd = new JSG_2_GetEbd(leftTrad.getExpression(), einf, jsg_2_InScope.getLeft().getLine(), jsg_2_InScope.getLeft().getColumn());
						
						ExpressionTraductionV2 exprTrad = new ExpressionTraductionV2(getEbd);
						exprTrad.addInstruction(leftTrad.getInstructions());
						exprTrad.addInstruction(rightTrad.getInstructions());
						return exprTrad;
					}
				}
			}
		}


		JSG_2_InScope resultScope = new JSG_2_InScope(leftTrad.getExpression(), rightTrad.getExpression(), jsg_2_InScope.isStatic(), l, c);
		ExpressionTraductionV2 result = new ExpressionTraductionV2(resultScope);
		result.addInstruction(leftTrad.getInstructions());
		result.addInstruction(rightTrad.getInstructions());

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Integer jsg_2_Integer) {
		return new ExpressionTraductionV2(jsg_2_Integer);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_IsMarked jsg_2_IsMarked) {
		return new ExpressionTraductionV2(jsg_2_IsMarked);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_IsNotMarked jsg_2_IsNotMarked) {
		return new ExpressionTraductionV2(jsg_2_IsNotMarked);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_JerboaKeyword jsg_2_JerboaKeyword) {
		return new ExpressionTraductionV2(jsg_2_JerboaKeyword);
	}

	@Override
	public ExpressionTraductionV2 accept(@SuppressWarnings("rawtypes") JSG_2_Literal jsg_2_Literal) {
		return new ExpressionTraductionV2(jsg_2_Literal);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Long jsg_2_Long) {
		return new ExpressionTraductionV2(jsg_2_Long);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Map jsg_2_Map) {
		return new ExpressionTraductionV2(jsg_2_Map);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Mark jsg_2_Mark) {
		return new ExpressionTraductionV2(jsg_2_Mark);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_New jsg_2_New) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 exp = jsg_2_New.getExp().visit(this);
		result.addInstruction(exp.getInstructions());

		return new ExpressionTraductionV2(new JSG_2_New(exp.getExpression(), jsg_2_New.getLine(), jsg_2_New.getColumn()));
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_NOP jsg_2_NOP) {
		return new ExpressionTraductionV2(jsg_2_NOP);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Not jsg_2_Not) {
		int l = jsg_2_Not.getLine();
		int c = jsg_2_Not.getColumn();
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 exp = jsg_2_Not.getExpr().visit(this);
		result.addInstruction(exp.getInstructions());
		
		JSG_2_Entity notExpr = exp.getExpression();
		if(context.isAPointerType(jsg_2_Not.getExpr(), glue)) { 
//			System.out.println("IS a pointer type : " + exp + " - " + notExpr);
			JSG_2_Indirection indirecter_2_these = new JSG_2_Indirection(notExpr, l, c);
			notExpr = indirecter_2_these;
		}

		return new ExpressionTraductionV2(new JSG_2_Not(notExpr, l, c));
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Null jsg_2_Null) {
		return new ExpressionTraductionV2(jsg_2_Null);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Operator jsg_2_Operator) {
		int l = jsg_2_Operator.getLine();
		int c = jsg_2_Operator.getColumn();
		ArrayList<JSG_2_Entity> instrList = new ArrayList<>();
		ArrayList<JSG_2_Entity> exprList = new ArrayList<>();
		
		// SI null est une operade, on va surement tester la validite d'un pointeur
		// alors on ne voudra pas faire d'indirection
		boolean isNullPresentAsOperand = false;
		for (JSG_2_Entity exp : jsg_2_Operator.getOperands()) {
			isNullPresentAsOperand = isNullPresentAsOperand || (exp instanceof JSG_2_Null);// || exp == null;
		}

//		System.out.println("OPERATOR : " + isNullPresentAsOperand );
		for (JSG_2_Entity exp : jsg_2_Operator.getOperands()) {
			if (exp != null) {
				ExpressionTraductionV2 rexp = exp.visit(this);
				instrList.addAll(rexp.getInstructions());

//				if(!isNullPresentAsOperand && context.isAPointerType(rexp.getExpression(), glue)) { 
				if(!isNullPresentAsOperand && context.isAPointerType(exp, glue)) { 
//					System.out.println("IS a pointer type : " + exp + " - " + rexp.getExpression());
					JSG_2_Indirection indirecter_2_these = new JSG_2_Indirection(rexp.getExpression(), l, c);
					exprList.add(indirecter_2_these);
					
				}else if(rexp!=null){
//					System.out.println("Not a pointer type : " + exp + " - " + rexp.getExpression());
					exprList.add(rexp.getExpression());
				}
			} else
				exprList.add(exp);
		}
		return new ExpressionTraductionV2(instrList,
				new JSG_2_Operator(jsg_2_Operator.getOperator(), l, c, exprList));
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Orbit jsg_2_Orbit) {
		return new ExpressionTraductionV2(jsg_2_Orbit);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_PackagedType jsg_2_PackagedType) {
		return new ExpressionTraductionV2(jsg_2_PackagedType);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Print jsg_2_Print) {
		ArrayList<JSG_2_Entity> instrList = new ArrayList<>();
		ArrayList<JSG_2_Entity> args = new ArrayList<>();
		for (JSG_2_Entity exp : jsg_2_Print.getArguments()){
			if (exp != null) {
				ExpressionTraductionV2 rexp = exp.visit(this);
				instrList.addAll(rexp.getInstructions());
				args.add(rexp.getExpression());
			} else
				args.add(exp);
		}
		return new ExpressionTraductionV2(instrList,
				new JSG_2_Print(args, jsg_2_Print.getLine(), jsg_2_Print.getColumn()));
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Return jsg_2_Return) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 exp = jsg_2_Return.getExpression().visit(this);
		result.addInstruction(exp.getInstructions());
		
		result.setExpression(new JSG_2_Return(exp.getExpression(), jsg_2_Return.getLine(), jsg_2_Return.getColumn()));

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Rule jsg_2_Rule) {
		return new ExpressionTraductionV2(jsg_2_Rule);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_RuleArg jsg_2_RuleArg) {
		return new ExpressionTraductionV2(jsg_2_RuleArg);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_RuleNodeId jsg_2_RuleNode) {
		return new ExpressionTraductionV2(jsg_2_RuleNode);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Sequence jsg_2_Sequence) {
		JSG_2_Sequence seq = new JSG_2_Sequence(jsg_2_Sequence.getLine(), jsg_2_Sequence.getColumn());
		for (JSG_2_Entity i : jsg_2_Sequence) {
			ExpressionTraductionV2 iTrad = i.visit(this);
			seq.addAll(iTrad.getInstructions());
			seq.add(iTrad.getExpression());
		}
		return new ExpressionTraductionV2(seq);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_String jsg_2_String) {
		return new ExpressionTraductionV2(jsg_2_String);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Throw jsg_2_Throw) {
		return new ExpressionTraductionV2(jsg_2_Throw);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Try jsg_2_Try) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 block = jsg_2_Try.getTryBlock().visit(this);
		result.addInstruction(block.getInstructions());

		ExpressionTraductionV2 finallyBlock = new ExpressionTraductionV2(new JSG_2_NOP(jsg_2_Try.getLine(), jsg_2_Try.getColumn() ));
		if(jsg_2_Try.getFinallyBlock()!=null) {
			finallyBlock = jsg_2_Try.getFinallyBlock().visit(this);
			result.addInstruction(finallyBlock.getInstructions());
		}

		List<JSG_2_Catch> catchList = new ArrayList<>();
		for(JSG_2_Catch catcher : jsg_2_Try.getCatchList()) {
			ExpressionTraductionV2 catcherTrad = catcher.visit(this);
			result.addInstruction(catcherTrad.getInstructions());
			catchList.add((JSG_2_Catch)catcherTrad.getExpression());
		}

		result.setExpression(new JSG_2_Try(block.getExpression(), catchList, finallyBlock.getExpression(),
				jsg_2_Try.getLine(), jsg_2_Try.getColumn()) );

		return result;
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Type jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeBoolean jsg_2_TypeBoolean) {
		return new ExpressionTraductionV2(jsg_2_TypeBoolean);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeDart jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeEmbedding jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeGmap jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeHookList jsg_2_TypeHookList) {
		return new ExpressionTraductionV2(jsg_2_TypeHookList);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeList jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeMark jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeModeler jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeOrbit jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypePointer jsg_2_TypePointer) {
		return new ExpressionTraductionV2(jsg_2_TypePointer);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeRule jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeRuleResult jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeString jsg_2_TypeString) {
		return new ExpressionTraductionV2(jsg_2_TypeString);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_TypeTemplate jsg_2_Type) {
		return new ExpressionTraductionV2(jsg_2_Type);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_UnMark jsg_2_UnMark) {
		return new ExpressionTraductionV2(jsg_2_UnMark);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Unreference jsg_2_Unreference) {
		return new ExpressionTraductionV2(jsg_2_Unreference);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_Variable jsg_2_Variable) {
		return new ExpressionTraductionV2(jsg_2_Variable);
	}

	@Override
	public ExpressionTraductionV2 accept(JSG_2_While jsg_2_While) {
		ExpressionTraductionV2 result = new ExpressionTraductionV2(null);

		ExpressionTraductionV2 block = jsg_2_While.getBody().visit(this);
		result.addInstruction(block.getInstructions());

		ExpressionTraductionV2 condition = jsg_2_While.getCondition().visit(this);
		result.addInstruction(condition.getInstructions());

		result.setExpression(new JSG_2_While(condition.getExpression(), block.getExpression(), 
				jsg_2_While.getLine(), jsg_2_While.getColumn()));

		return result;
	}

	private String getFreshVariableName(String base) {
		String prefix = "_v_";
		int cpt = 0;
		while (context.varExists(prefix + base + cpt)) {
			cpt++;
		}
		return prefix + base + cpt;
	}

}
