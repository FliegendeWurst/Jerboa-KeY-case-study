package fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction;

import java.util.ArrayList;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMENode;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMEParamTopo;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.generator.LanguageGlue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_AddInHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_AddInList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Alpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_ApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Boolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Call;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CallListSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CallRuleResHeight;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CallRuleResWidth;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Cast;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Choice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Collect;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_CollectEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Comment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Constructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Double;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_EbdParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Float;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_FreeMarker;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GMapSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetEbdId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetEbdName;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetEbdOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetMarker;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetNodeId;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_GetTopoParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_InScope;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_InScopeStatic;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Index;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexInLeftPattern;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexInRuleResult;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexNodeInGmap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexRuleNode;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IndexRuleNode.SIDE;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Indirection;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Integer;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IsMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_IsNotMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordDimension;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordGmap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordHook;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordLeftFilter;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_KeywordRightFilter;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_LeftRuleNode;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_List;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Long;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_New;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Not;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Null;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Operator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Orbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_PackagedType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Rule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_RuleArg;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_RuleNode;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Size;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_String;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeBoolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaDart;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeJerboaRuleResult;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypePrimitive;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeString;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_TypeTemplate;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Unreference;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_UserType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Variable;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Assignment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_AssocParam;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_AtLang;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Block;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Break;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Catch;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ClearHookList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Continue;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Declare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DeclareMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Delete;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ExprInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_For;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Header;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_HookCall;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_If;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Map;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Mark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_NOP;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Print;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Return;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Throw;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Try;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_UnMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_While;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_ExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools.JSG_InstVisitor;

public class SpecificOperatorRemover implements JSG_ExprVisitor<ExpressionTraduction, RuntimeException>,
JSG_InstVisitor<JSG_Instruction, RuntimeException> {

	TranslatorContext context;
	JMEModeler modeler;
	LanguageGlue glue;

	public SpecificOperatorRemover(JMEModeler _modeler, LanguageGlue _glue) {
		context = new TranslatorContext();
		modeler = _modeler;
		glue = _glue;
		// TODO: Val : dsl je sais que tu n'aimes pas la glue mais la j'en ai
		// besoin. Je chercherai une autre solution ult�rieurement
	}

	@Override
	public ExpressionTraduction accept(JSG_AddInHookList jsg_AddInHookList) {
		return new ExpressionTraduction(jsg_AddInHookList);
	}

	@Override
	public ExpressionTraduction accept(JSG_AddInList jsAddInListSem) {
		return new ExpressionTraduction(jsAddInListSem);
	}

	@Override
	public ExpressionTraduction accept(JSG_Alpha jsAlpha) throws RuntimeException {
		return new ExpressionTraduction(jsAlpha);
	}

	@Override
	public ExpressionTraduction accept(JSG_ApplyRule jsApplyRule) throws RuntimeException {
		final int l = jsApplyRule.getLine();
		final int c = jsApplyRule.getColumn();
		JSG_Sequence seq = new JSG_Sequence(jsApplyRule.getLine(), jsApplyRule.getColumn());

		String hookListName = getFreshVariableName("hook");
		JSG_TypeJerboaHookList typeHook = new JSG_TypeJerboaHookList(jsApplyRule.getRule(), l, c);
		seq.add(new JSG_Declare(typeHook, hookListName, new JSG_Constructor(typeHook, new ArrayList<>()), l, c));

		ArrayList<JSG_Expression> args = new ArrayList<>();
		JSG_Variable hookVar = new JSG_Variable(hookListName, l, c);
		args.add(hookVar);

		boolean namedParameters = false;
		if (jsApplyRule.getArgs().size() > 0 && jsApplyRule.getArgs().get(0) instanceof JSG_AssocParam) {
			namedParameters = true;
		}
		JMERule rule = jsApplyRule.getRule();
		if (rule == null) {
			System.err.println("No rule found for expression apply rule : (" + jsApplyRule.getLine() + ","
					+ jsApplyRule.getColumn() + ") " + jsApplyRule.getRuleExpr());
			return new ExpressionTraduction(new JSG_ApplyRule(jsApplyRule.getRule(), jsApplyRule.getRuleExpr(), args,
					jsApplyRule.getReturnType(), jsApplyRule.getLine(), jsApplyRule.getColumn()));

		}
		int nbTopoParam = 0;
		if (rule.getParamsTopo() != null)
			nbTopoParam = rule.getParamsTopo().size();

		int nbEbdParam = 0;
		if (rule.getParamsEbd() != null)
			nbEbdParam = rule.getParamsEbd().size();

		int cptParam = 0;
		for (JSG_Expression e : jsApplyRule.getArgs()) {
			ExpressionTraduction et = e.visit(this);
			for (JSG_Instruction i : et.getInstructions()) {
				seq.add(i);
			}
			if (namedParameters) {
				if (e instanceof JSG_AssocParam) {
					if (ruleHasTopoParam(rule, ((JSG_AssocParam) e).getParamName())) {
						ArrayList<JSG_Expression> listTmp = new ArrayList<JSG_Expression>();
						listTmp.add(new JSG_IndexRuleNode(SIDE.LEFT, rule, ((JSG_AssocParam) e).getParamName(),
								jsApplyRule.getLine(), jsApplyRule.getColumn()));
						listTmp.add(e);
						seq.add(new JSG_ExprInstruction(new JSG_InScope(hookVar, new JSG_AddInHookList(listTmp))));
					} else {
						seq.add(new JSG_ExprInstruction(e)); // TODO: vérifier
						// ça
					}
				} else {

				}
			} else {
				if (nbTopoParam > cptParam) {
					ArrayList<JSG_Expression> listTmp = new ArrayList<JSG_Expression>();
					listTmp.add(e);
					seq.add(new JSG_ExprInstruction(new JSG_InScope(hookVar, new JSG_AddInHookList(listTmp))));
				} else if (nbTopoParam + nbEbdParam > cptParam) {
					JSG_Expression expParam = e;
					if (expParam instanceof JSG_AssocParam) {
						expParam = ((JSG_AssocParam) expParam).getParamValue();
					}
					seq.add(new JSG_AssocParam(
							new JSG_Rule(rule.getName(), jsApplyRule.getRule(), jsApplyRule.getLine(),
									jsApplyRule.getColumn()),
							expParam, rule.getParamsEbd().get(cptParam - nbTopoParam).getName(), l, c));
				}
			}
			cptParam++;
		}

		ExpressionTraduction exprTrad = new ExpressionTraduction(
				new JSG_ApplyRule(jsApplyRule.getRule(), jsApplyRule.getRuleExpr(), args, jsApplyRule.getReturnType(),
						jsApplyRule.getLine(), jsApplyRule.getColumn()));

		for (JSG_Instruction i : seq) {
			exprTrad.addInstruction(i);
		}

		context.declareVar(hookListName, typeHook, typeHook); 
		// TODO: bizarre de mettre le type en valeur et vis versa

		return exprTrad;
	}

	@Override
	public JSG_Instruction accept(JSG_Assignment jsAssignment) throws RuntimeException {
		ExpressionTraduction expTrad = jsAssignment.getValue().visit(this);
		JSG_Sequence seq = new JSG_Sequence(jsAssignment.getLine(), jsAssignment.getColumn());

		if (jsAssignment.getValue() instanceof JSG_Choice) {
			seq.addAll(jsAssignment.getValue().visit(this).getInstructions());
			// JSG_Try tryExp = null;
			// for (int i = ((JSG_Choice) jsAssignment.getValue()).getOptions().size() - 1;
			// i >= 0; i--) {
			// ArrayList<JSG_Catch> listCatch = new ArrayList<>();
			// if (tryExp != null) {
			// listCatch.add(new JSG_Catch(
			// new JSG_Block(tryExp, true, jsAssignment.getLine(),
			// jsAssignment.getColumn()),
			// new JSG_Declare(
			// new JSG_TypePrimitive("JerboaException", jsAssignment.getLine(),
			// jsAssignment.getColumn()),
			// "e" + i, null, jsAssignment.getLine(), jsAssignment.getColumn()),
			// jsAssignment.getLine(), jsAssignment.getColumn()));
			// } else {
			// listCatch.add(new JSG_Catch(
			// new JSG_Block(new JSG_NOP(), true, jsAssignment.getLine(),
			// jsAssignment.getColumn()),
			// new JSG_Declare(
			// new JSG_TypePrimitive("JerboaException", jsAssignment.getLine(),
			// jsAssignment.getColumn()),
			// "e" + i, null, jsAssignment.getLine(), jsAssignment.getColumn()),
			// jsAssignment.getLine(), jsAssignment.getColumn()));
			// }
			// JSG_Block b = new JSG_Block(new JSG_Assignment(jsAssignment.getVariable(),
			// ((JSG_Choice) jsAssignment.getValue()).getOptions().get(i),
			// jsAssignment.getLine(),
			// jsAssignment.getColumn()), true, jsAssignment.getLine(),
			// jsAssignment.getColumn());
			// tryExp = new JSG_Try(b, listCatch, null, jsAssignment.getLine(),
			// jsAssignment.getColumn());
			// }
			// seq.add(tryExp.visit(this));
		} else {
			for (JSG_Instruction i : expTrad.getInstructions()) {
				seq.add(i);
			}
			seq.add(new JSG_Assignment(jsAssignment.getVariable(), expTrad.getExpression(), jsAssignment.getLine(),
					jsAssignment.getColumn()));
		}
		return seq;
	}

	@Override
	public JSG_Instruction accept(JSG_AssocParam jsg_AssocParam) {
		return jsg_AssocParam;
	}

	@Override
	public JSG_Instruction accept(JSG_AtLang jsg_AtLang) {
		return jsg_AtLang;
	}

	@Override
	public JSG_Block accept(JSG_Block jsBlock) throws RuntimeException {
		context.beginBlock();
		JSG_Block res = new JSG_Block(jsBlock.getBody().visit(this), jsBlock.hasBracket(), jsBlock.getLine(),
				jsBlock.getColumn());
		context.endBlock();
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_Boolean jsBoolean) throws RuntimeException {
		return new ExpressionTraduction(jsBoolean);
	}

	@Override
	public JSG_Instruction accept(JSG_Break jsg_Break) {
		return jsg_Break;
	}

	@Override
	public ExpressionTraduction accept(JSG_Call jsCall) throws RuntimeException {
		ArrayList<JSG_Instruction> listInstruction = new ArrayList<>();
		ArrayList<JSG_Expression> listexprArg = new ArrayList<>();
		for (JSG_Expression e : jsCall.getArguments()) {
			if(e != null) {
				ExpressionTraduction eET = e.visit(this);
				listInstruction.addAll(eET.getInstructions());
				listexprArg.add(eET.getExpression());
			} else {
				ExpressionTraduction eET = new ExpressionTraduction(new JSG_Null());
				listInstruction.addAll(eET.getInstructions());
				listexprArg.add(eET.getExpression());
			}
		}
		ExpressionTraduction res = new ExpressionTraduction(new JSG_Call(jsCall.getName(), listexprArg, jsCall.getLine(), jsCall.getColumn()));
		res.addInstruction(listInstruction);
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_CallListSize jsg_CallListSize) {
		return new ExpressionTraduction(jsg_CallListSize);
	}

	@Override
	public ExpressionTraduction accept(JSG_CallRuleResHeight jsg_CallRuleResHeight) {
		return new ExpressionTraduction(jsg_CallRuleResHeight);
	}

	@Override
	public ExpressionTraduction accept(JSG_CallRuleResWidth jsg_CallRuleResWidth) {
		return new ExpressionTraduction(jsg_CallRuleResWidth);
	}

	@Override
	public ExpressionTraduction accept(JSG_Cast jsg_Cast) {
		return new ExpressionTraduction(jsg_Cast);
	}

	@Override
	public JSG_Instruction accept(JSG_Catch jsg_Catch) {
		return new JSG_Catch(jsg_Catch.getBlock().visit(this), jsg_Catch.getDeclar(),
				// TODO: ici je ne fais pas de visite, car sinon on a : "catch
				// (Exception e; ){" a cause
				// de la mise en sequence de l'instruction de déclaration
				jsg_Catch.getLine(), jsg_Catch.getColumn());
	}

	@Override
	public ExpressionTraduction accept(JSG_Choice jsChoice) {
		// if (true)
		// return new ExpressionTraduction(jsChoice);
		int l = jsChoice.getLine();
		int c = jsChoice.getColumn();
		ArrayList<ExpressionTraduction> listResult = new ArrayList<>();
		for (JSG_Expression e : jsChoice.getOptions()) {
			listResult.add(e.visit(this));
		}
		// On suppose qu'il y a au moins une expression sinon c'est pas un choice (m�me
		// pour 1 �a l'est pas en fait).
		JSG_Sequence seqTry = new JSG_Sequence(l, c);
		seqTry.addAll(listResult.get(0).getInstructions());
		if (jsChoice.getVarResult() != null && jsChoice.getVarResult().length() > 0) {
			seqTry.add(new JSG_Assignment(new JSG_Variable(jsChoice.getVarResult(), l, c),
					listResult.get(0).getExpression(), l, c));
		} else {
			seqTry.add(new JSG_ExprInstruction(listResult.get(0).getExpression()));
		}
		/*
		 * ArrayList<JSG_Expression> choices = new ArrayList<JSG_Expression>();
		 * ArrayList<ExpressionTraduction> listResult = new ArrayList<>(); for
		 * (JSG_Expression e : jsChoice.getOptions()) { listResult.add(e.visit(this)); }
		 * // On suppose qu'il y a au moins une expression sinon c'est pas un choice
		 * (m�me // pour 1 �a l'est pas en fait). JSG_Sequence seqTry = new
		 * JSG_Sequence(l, c); seqTry.addAll(listResult.get(0).getInstructions()); if
		 * (jsChoice.getVarResult() != null && jsChoice.getVarResult().length() > 0) {
		 * seqTry.add(new JSG_Assignment(new JSG_Variable(jsChoice.getVarResult(), l,
		 * c), listResult.get(0).getExpression(), l, c)); } else { seqTry.add(new
		 * JSG_ExprInstruction(listResult.get(0).getExpression())); }
		 * 
		 * ArrayList<JSG_Catch> catchList = new ArrayList<>(); JSG_Try lastTry = seqTry;
		 * for (int i = 0; i < listResult.size(); i++) { JSG_Sequence seqCatch = new
		 * JSG_Sequence(jsChoice.getLine(), jsChoice.getColumn());
		 * seqCatch.addAll(listResult.get(i).getInstructions()); if
		 * (jsChoice.getVarResult() != null && jsChoice.getVarResult().length() > 0) {
		 * seqCatch.add(new JSG_Assignment( new JSG_Variable(jsChoice.getVarResult(),
		 * jsChoice.getLine(), jsChoice.getColumn()), listResult.get(i).getExpression(),
		 * jsChoice.getLine(), jsChoice.getColumn())); } else { seqCatch.add(new
		 * JSG_ExprInstruction(listResult.get(i).getExpression())); } catchList.add(new
		 * JSG_Catch(new JSG_Block(seqCatch, true, l, c), new JSG_Declare(new
		 * JSG_TypePrimitive("JerboaException", l, c),
		 * getFreshVariableName("_exeption"), null, l, c), l, c)); }
		 * 
		 * // VAL: Je n'ai pas mis de finally. Peut etre en faudrait-il un.
		 * 
		 * JSG_Try tryer = new JSG_Try(new JSG_Block(seqTry, true, l, c), catchList,
		 * null, jsChoice.getLine(), jsChoice.getColumn());
		 *
		 * return new ExpressionTraduction(tryer, null);
		 */
		JSG_Instruction last = null;

		for (int i = listResult.size() - 1; i >= 0; i--) {
			JSG_Sequence seqCatch = new JSG_Sequence(jsChoice.getLine(), jsChoice.getColumn());
			seqCatch.addAll(listResult.get(i).getInstructions());
			if (jsChoice.getVarResult() != null && jsChoice.getVarResult().length() > 0) {
				seqCatch.add(new JSG_Assignment(
						new JSG_Variable(jsChoice.getVarResult(), jsChoice.getLine(), jsChoice.getColumn()),
						listResult.get(i).getExpression(), jsChoice.getLine(), jsChoice.getColumn()));
			} else {
				seqCatch.add(new JSG_ExprInstruction(listResult.get(i).getExpression()));
			}
			JSG_Catch blockCatch = new JSG_Catch(new JSG_Block(last != null ? last : new JSG_NOP(), true, l, c),
					new JSG_Declare(new JSG_TypePrimitive("JerboaException", l, c), getFreshVariableName("_exeption"),
							null, l, c),
					l, c);
			ArrayList<JSG_Catch> catcherList = new ArrayList<>();
			catcherList.add(blockCatch);
			last = new JSG_Try(new JSG_Block(seqCatch, true, l, c), catcherList, null, l, c);
		}
		return new ExpressionTraduction(last, null);
		/*
		 * TODO: VAL : je change pour faire directement un try plutot que re transformer
		 * en choice qui n'est pa aussi mal�able ArrayList<JSG_Expression> choices = new
		 * ArrayList<JSG_Expression>(); ArrayList<JSG_Instruction> instructions = new
		 * ArrayList<>(); for (JSG_Expression e : jsChoice.getOptions()) {
		 * ExpressionTraduction et = e.visit(this);
		 * instructions.addAll(et.getInstructions()); choices.add(et.getExpression()); }
		 * // TODO: il y a qqch a faire ici! return new
		 * ExpressionTraduction(instructions, new JSG_Choice(choices,
		 * jsChoice.getVarResult(), jsChoice.getLine(), jsChoice.getColumn()));
		 */
	}

	@Override
	public JSG_Instruction accept(JSG_ClearHookList jsg_ClearHookList) {
		return jsg_ClearHookList;
	}

	@Override
	public ExpressionTraduction accept(JSG_Collect jsCollect) throws RuntimeException {
		ExpressionTraduction orb = jsCollect.getOrbit().visit(this);
		ExpressionTraduction sorb = jsCollect.getSubOrbit().visit(this);
		ExpressionTraduction node = jsCollect.getNode().visit(this);
		JSG_Collect collect = new JSG_Collect((JSG_Orbit) orb.getExpression(), (JSG_Orbit) sorb.getExpression(),
				node.getExpression(), jsCollect.gmapHasDirectAccess(), jsCollect.getLine(), jsCollect.getColumn());
		ExpressionTraduction res = new ExpressionTraduction(collect);
		res.addInstruction(orb.getInstructions());
		res.addInstruction(sorb.getInstructions());
		res.addInstruction(node.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_CollectEbd jsCollect) throws RuntimeException {
		ExpressionTraduction orb = jsCollect.getOrbit().visit(this);
		ExpressionTraduction node = jsCollect.getNode().visit(this);
		JSG_CollectEbd collect = new JSG_CollectEbd((JSG_Orbit) orb.getExpression(), jsCollect.getEmbedding(),
				node.getExpression(), jsCollect.getEbdType(), jsCollect.gmapHasDirectAccess(), jsCollect.getLine(),
				jsCollect.getColumn());
		ExpressionTraduction res = new ExpressionTraduction(collect);
		res.addInstruction(orb.getInstructions());
		res.addInstruction(node.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_Comment jsg_Comment) {
		return new ExpressionTraduction(jsg_Comment);
	}

	@Override
	public ExpressionTraduction accept(JSG_Constructor jsg_Constructor) {
		ArrayList<JSG_Instruction> listInstr = new ArrayList<>();
		ArrayList<JSG_Expression> param = new ArrayList<>();
		for (JSG_Expression e : jsg_Constructor.getArguments()) {
			ExpressionTraduction re = e.visit(this);
			listInstr.addAll(re.getInstructions());
			param.add(re.getExpression());
		}
		ExpressionTraduction res = new ExpressionTraduction(new JSG_Constructor(jsg_Constructor.getName(), param));
		res.addInstruction(listInstr);
		return res;
	}

	@Override
	public JSG_Instruction accept(JSG_Continue jsg_Continue) {
		return jsg_Continue;
	}

	@Override
	public JSG_Instruction accept(JSG_Declare jsDeclare) throws RuntimeException {

		ExpressionTraduction expTrad = new ExpressionTraduction(null);
		if (jsDeclare.getValue() != null)
			expTrad = jsDeclare.getValue().visit(this);
		JSG_Sequence seq = new JSG_Sequence(jsDeclare.getLine(), jsDeclare.getColumn());

		if (jsDeclare.getValue() instanceof JSG_Choice) {
			// System.err.println("passe dans alternant du declar");
			// JSG_Try tryExp = null;
			// for (int i = ((JSG_Choice) jsDeclare.getValue()).getOptions().size() - 1; i
			// >= 0; i--) {
			// ArrayList<JSG_Catch> listCatch = new ArrayList<>();
			// if (tryExp != null) {
			// listCatch.add(new JSG_Catch(new JSG_Block(tryExp, true, jsDeclare.getLine(),
			// jsDeclare.getColumn()),
			// new JSG_Declare(
			// new JSG_TypePrimitive("JerboaException", jsDeclare.getLine(),
			// jsDeclare.getColumn()),
			// "e" + i, null, jsDeclare.getLine(), jsDeclare.getColumn()),
			// jsDeclare.getLine(), jsDeclare.getColumn()));
			// } else {
			// listCatch.add(new JSG_Catch(
			// new JSG_Block(new JSG_NOP(), true, jsDeclare.getLine(),
			// jsDeclare.getColumn()),
			// new JSG_Declare(
			// new JSG_TypePrimitive("JerboaException", jsDeclare.getLine(),
			// jsDeclare.getColumn()),
			// "e" + i, null, jsDeclare.getLine(), jsDeclare.getColumn()),
			// jsDeclare.getLine(), jsDeclare.getColumn()));
			// }
			// JSG_Block b = new JSG_Block(new JSG_Assignment(
			// new JSG_Variable(jsDeclare.getName(), jsDeclare.getLine(),
			// jsDeclare.getColumn()),
			// ((JSG_Choice) jsDeclare.getValue()).getOptions().get(i), jsDeclare.getLine(),
			// jsDeclare.getColumn()), true, jsDeclare.getLine(), jsDeclare.getColumn());
			// tryExp = new JSG_Try(b, listCatch, null, jsDeclare.getLine(),
			// jsDeclare.getColumn());
			// }
			seq.add(new JSG_Declare(jsDeclare.getType(), jsDeclare.getName(), null, jsDeclare.getLine(),
					jsDeclare.getColumn()));
			seq.addAll(jsDeclare.getValue().visit(this).getInstructions());
			context.declareVar(jsDeclare.getName(), jsDeclare.getType(), null);
			return seq;
		} else {
			for (JSG_Instruction i : expTrad.getInstructions()) {
				seq.add(i);
			}

			seq.add(new JSG_Declare(jsDeclare.getType(), jsDeclare.getName(), expTrad.getExpression(),
					jsDeclare.getLine(), jsDeclare.getColumn()));
		}

		context.declareVar(jsDeclare.getName(), jsDeclare.getType(), expTrad.getExpression());
		return seq;
	}

	@Override
	public JSG_Instruction accept(JSG_DeclareFunction jsg_DeclareFunction) {
		return new JSG_DeclareFunction(jsg_DeclareFunction.getReturnType(), jsg_DeclareFunction.getName(),
				jsg_DeclareFunction.getArguments(), (JSG_Block) jsg_DeclareFunction.getBlock().visit(this),
				jsg_DeclareFunction.getLine(), jsg_DeclareFunction.getColumn());
	}

	@Override
	public JSG_Instruction accept(JSG_DeclareMark jsg_DeclareMark) {
		return jsg_DeclareMark;
	}

	@Override
	public JSG_Instruction accept(JSG_Delete jsg_Delete) {
		ExpressionTraduction expTrad = jsg_Delete.getName().visit(this);
		JSG_Sequence seq = new JSG_Sequence(jsg_Delete.getLine(), jsg_Delete.getColumn());
		for (JSG_Instruction i : expTrad.getInstructions()) {
			seq.add(i);
		}
		seq.add(new JSG_Delete(expTrad.getExpression(), jsg_Delete.getLine(), jsg_Delete.getColumn()));
		return seq;
	}

	@Override
	public ExpressionTraduction accept(JSG_Double jsDouble) throws RuntimeException {
		return new ExpressionTraduction(jsDouble);
	}

	@Override
	public JSG_Instruction accept(JSG_DoWhile jsDoWhile) throws RuntimeException {
		return new JSG_DoWhile(jsDoWhile.getCondition(), jsDoWhile.getBody().visit(this), jsDoWhile.getLine(),
				jsDoWhile.getColumn());
	}

	@Override
	public ExpressionTraduction accept(JSG_EbdParam jsg_EbdParam) {
		return new ExpressionTraduction(jsg_EbdParam);
	}

	@Override
	public JSG_Instruction accept(JSG_ExprInstruction jsExprInstruction) throws RuntimeException {
		ExpressionTraduction expTrad = jsExprInstruction.getExpr().visit(this);
		if (jsExprInstruction.getExpr() instanceof JSG_Choice) {
			//			System.err.println("^^^^^^^passe exprInstr pour Choice " + expTrad.getInstructions());
			return new JSG_Sequence(jsExprInstruction.getLine(), jsExprInstruction.getColumn(),
					expTrad.getInstructions());
			// VAL : Pas d'expression dans les choices
		} else if (expTrad.getInstructions().size() == 0)
			return new JSG_ExprInstruction(expTrad.getExpression());
		JSG_Sequence seq = new JSG_Sequence(jsExprInstruction.getLine(), jsExprInstruction.getColumn());
		for (JSG_Instruction i : expTrad.getInstructions()) {
			seq.add(i);
		}
		seq.add(new JSG_ExprInstruction(expTrad.getExpression()));
		return seq;
	}

	@Override
	public ExpressionTraduction accept(JSG_Float jsFloat) throws RuntimeException {
		return new ExpressionTraduction(jsFloat);
	}

	@Override
	public JSG_Instruction accept(JSG_For jsFor) throws RuntimeException {
		return new JSG_For(jsFor.getType(), jsFor.getVariable(), jsFor.getStart(), jsFor.getEnd(), jsFor.getStep(),
				jsFor.getBody().visit(this), jsFor.getLine(), jsFor.getColumn());
	}

	@Override
	public JSG_Instruction accept(JSG_ForEach jsForEach) throws RuntimeException {
		return new JSG_ForEach(jsForEach.getType(), jsForEach.getName(), jsForEach.getColl(),
				jsForEach.getBody().visit(this), jsForEach.getLine(), jsForEach.getColumn());
	}

	@Override
	public JSG_Instruction accept(JSG_ForLoop jsForLoop) throws RuntimeException {
		return new JSG_ForLoop(jsForLoop.getInit(), jsForLoop.getCond(), jsForLoop.getStep(),
				jsForLoop.getBody().visit(this), jsForLoop.getLine(), jsForLoop.getColumn());
	}

	@Override
	public JSG_Instruction accept(JSG_FreeMarker jsg_FreeMarker) {
		return jsg_FreeMarker;
	}

	@Override
	public ExpressionTraduction accept(JSG_GetEbd jsg_GetEbd) {
		ExpressionTraduction rexp = jsg_GetEbd.getLeft().visit(this);
		return new ExpressionTraduction(rexp.getInstructions(),
				new JSG_GetEbd(rexp.getExpression(), jsg_GetEbd.getEbdInfo()));
	}

	@Override
	public ExpressionTraduction accept(JSG_GetEbdId jsg_GetEbdId) {
		return new ExpressionTraduction(jsg_GetEbdId);
	}

	@Override
	public ExpressionTraduction accept(JSG_GetEbdName jsg_GetEbdName) {
		return new ExpressionTraduction(jsg_GetEbdName);
	}

	@Override
	public ExpressionTraduction accept(JSG_GetEbdOrbit jsg_GetEbdOrbit) {
		return new ExpressionTraduction(jsg_GetEbdOrbit);
	}

	@Override
	public ExpressionTraduction accept(JSG_GetMarker jsg_GetFreeMarker) {
		return new ExpressionTraduction(jsg_GetFreeMarker);
	}

	@Override
	public ExpressionTraduction accept(JSG_GetNodeId jsg_GetId) {
		return new ExpressionTraduction(jsg_GetId);
	}

	@Override
	public ExpressionTraduction accept(JSG_GetTopoParam jsg_GetTopoParam) {
		return new ExpressionTraduction(jsg_GetTopoParam);
	}

	@Override
	public ExpressionTraduction accept(JSG_GMapSize jsg_GMapSize) {
		return new ExpressionTraduction(jsg_GMapSize);
	}

	@Override
	public JSG_Instruction accept(JSG_Header jsg_Header) {
		return jsg_Header;
	}

	@Override
	public JSG_Instruction accept(JSG_HookCall jsg_HookCall) {
		return jsg_HookCall;
	}

	@Override
	public JSG_Instruction accept(JSG_If jsIf) throws RuntimeException {
		JSG_Sequence seq = new JSG_Sequence(jsIf.getLine(), jsIf.getColumn());

		JSG_Instruction cons = jsIf.getConsequence() != null ? jsIf.getConsequence().visit(this) : null;
		JSG_Instruction alt = jsIf.getAlternant() != null ? jsIf.getAlternant().visit(this) : null;

		ExpressionTraduction rexp = null;
		if(jsIf.getCondition() != null) {
			rexp = jsIf.getCondition().visit(this);
			seq.addAll(rexp.getInstructions());
		}
		seq.add(new JSG_If(rexp!=null?rexp.getExpression():null, jsIf.getConsequence().visit(this), jsIf.getAlternant().visit(this),
				jsIf.getLine(), jsIf.getColumn()));
		return seq;
	}

	@Override
	public ExpressionTraduction accept(JSG_Index jsIndex) throws RuntimeException {
		// return new ExpressionTraduction(jsIndex);

		ExpressionTraduction expTradVar = jsIndex.getVariable().visit(this);
		ExpressionTraduction expTradIndex = jsIndex.getIndex().visit(this);

		ExpressionTraduction res = new ExpressionTraduction(
				new JSG_Index(expTradVar.getExpression(), expTradIndex.getExpression()));
		res.addInstruction(expTradVar.getInstructions());
		res.addInstruction(expTradIndex.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_IndexInLeftPattern jsg_IndexInLeftPattern) {
		JSG_Expression hookExp = jsg_IndexInLeftPattern.getHookIndex();
		//System.err.println("---> " + hookExp);
		if (hookExp instanceof JSG_Variable) {
			// si c'est une variable on regarde si le hook existe pour le
			// traduire en son num�ro d'index.
			for (JMENode tp : glue.getCurrentRule().getLeft().getNodes()) {
				if (tp.getName().compareToIgnoreCase(((JSG_Variable) hookExp).getName()) == 0) {
					hookExp = new JSG_Integer(tp.getID(), ((JSG_Variable) hookExp).getLine(),
							((JSG_Variable) hookExp).getColumn());
					// TODO: le getID a remplacer le getOrdeer des topoParam car
					// on veut parfois prendre un noeud qui n'est pas un hook !
					// Mais peut �tre que �a ne marche pas, a voir !
					// TODO : j'en remet un autre pour bien montrer que c'est un
					// point critique a v�rifier rapidement !
					break;
				}
			}
		} else if (hookExp instanceof JSG_LeftRuleNode) {
			for (JMEParamTopo tp : glue.getCurrentRule().getParamsTopo()) {
				if (tp.getName().compareToIgnoreCase(((JSG_LeftRuleNode) hookExp).getName()) == 0) {
					hookExp = new JSG_Integer(tp.getOrder(), ((JSG_LeftRuleNode) hookExp).getLine(),
							((JSG_LeftRuleNode) hookExp).getColumn());
					break;
				}
			}
		}
		return new ExpressionTraduction(new JSG_IndexInLeftPattern(hookExp, jsg_IndexInLeftPattern.getIndexInDartList(),
				jsg_IndexInLeftPattern.getLine(), jsg_IndexInLeftPattern.getColumn()));
	}

	@Override
	public ExpressionTraduction accept(JSG_IndexInRuleResult jsIndexInRuleResultSem) {
		// return new ExpressionTraduction(jsIndexInRuleResultSem);
		ExpressionTraduction expTradVar = jsIndexInRuleResultSem.getVariable().visit(this);
		ExpressionTraduction expTradIndexFirst = null;
		ExpressionTraduction expTradIndexSecond = null;
		
		if(jsIndexInRuleResultSem.getIndexFirst()!=null) {
			expTradIndexFirst = jsIndexInRuleResultSem.getIndexFirst().visit(this);
		}

		if(jsIndexInRuleResultSem.getIndexSecond()!=null) {
			expTradIndexSecond = jsIndexInRuleResultSem.getIndexSecond().visit(this);
		}

		ExpressionTraduction res = new ExpressionTraduction(
				new JSG_IndexInRuleResult(expTradVar.getExpression(), 
						expTradIndexFirst != null ? expTradIndexFirst.getExpression() : null, 
								expTradIndexSecond != null ?expTradIndexSecond.getExpression() : null));
		res.addInstruction(expTradVar.getInstructions());
		
		if(expTradIndexFirst!=null)
			res.addInstruction(expTradIndexFirst.getInstructions());
		if(expTradIndexSecond!=null)
			res.addInstruction(expTradIndexSecond.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_IndexNodeInGmap jsIndexNodeInGmapSem) {
		// return new ExpressionTraduction(jsIndexNodeInGmapSem);
		ExpressionTraduction expTradIndex = jsIndexNodeInGmapSem.getIndex().visit(this);

		ExpressionTraduction res = new ExpressionTraduction(new JSG_IndexNodeInGmap(expTradIndex.getExpression()));
		res.addInstruction(expTradIndex.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_IndexRuleNode jsg_IndexRuleNode) {
		return new ExpressionTraduction(jsg_IndexRuleNode);
	}

	@Override
	public ExpressionTraduction accept(JSG_Indirection jsg_Indirection) {
		ExpressionTraduction paramET = jsg_Indirection.getExp().visit(this);
		ExpressionTraduction res = new ExpressionTraduction(new JSG_Indirection(paramET.getExpression()));
		res.addInstruction(paramET.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_InScope jsInScope) throws RuntimeException {
		ExpressionTraduction leftET = jsInScope.getLeft().visit(this);
		ExpressionTraduction rightET = jsInScope.getRight().visit(this);
		ExpressionTraduction res = new ExpressionTraduction(
				new JSG_InScope(leftET.getExpression(), rightET.getExpression()));
		res.addInstruction(leftET.getInstructions());
		res.addInstruction(rightET.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_InScopeStatic jsg_InScopeStatic) {
		ExpressionTraduction leftET = jsg_InScopeStatic.getLeft().visit(this);
		ExpressionTraduction rightET = jsg_InScopeStatic.getRight().visit(this);
		ExpressionTraduction res = new ExpressionTraduction(
				new JSG_InScopeStatic((JSG_Type) leftET.getExpression(), rightET.getExpression()));
		res.addInstruction(leftET.getInstructions());
		res.addInstruction(rightET.getInstructions());
		return res;
	}

	@Override
	public ExpressionTraduction accept(JSG_Integer jsInteger) throws RuntimeException {
		return new ExpressionTraduction(jsInteger);
	}

	@Override
	public ExpressionTraduction accept(JSG_IsMarked jsg_IsMarked) {
		return new ExpressionTraduction(jsg_IsMarked);
	}

	@Override
	public ExpressionTraduction accept(JSG_IsNotMarked jsg_IsNotMarked) {
		return new ExpressionTraduction(jsg_IsNotMarked);
	}

	@Override
	public ExpressionTraduction accept(JSG_KeywordDimension jsKeywordDimension) {
		return new ExpressionTraduction(jsKeywordDimension);
	}

	@Override
	public ExpressionTraduction accept(JSG_KeywordEbd jsKeywordEbd) {
		return new ExpressionTraduction(jsKeywordEbd);
	}

	@Override
	public ExpressionTraduction accept(JSG_KeywordGmap jsGmapKeyword) throws RuntimeException {
		return new ExpressionTraduction(jsGmapKeyword);
	}

	@Override
	public ExpressionTraduction accept(JSG_KeywordHook jsg_KeywordHook) {
		return new ExpressionTraduction(jsg_KeywordHook);
	}

	@Override
	public ExpressionTraduction accept(JSG_KeywordLeftFilter jsg_KeywordLeftFilter) {
		return new ExpressionTraduction(jsg_KeywordLeftFilter);
	}

	@Override
	public ExpressionTraduction accept(JSG_KeywordModeler jsKeywordModeler) {
		return new ExpressionTraduction(jsKeywordModeler);
	}

	@Override
	public ExpressionTraduction accept(JSG_KeywordRightFilter jsg_KeywordRightFilter) {
		return new ExpressionTraduction(jsg_KeywordRightFilter);
	}

	@Override
	public ExpressionTraduction accept(JSG_LeftRuleNode jsVariable) {
		return new ExpressionTraduction(jsVariable);
	}

	@Override
	public ExpressionTraduction accept(JSG_List jsList) {
		return new ExpressionTraduction(jsList);
	}

	@Override
	public ExpressionTraduction accept(JSG_Long jsLong) throws RuntimeException {
		return new ExpressionTraduction(jsLong);
	}

	@Override
	public JSG_Instruction accept(JSG_Map jsMap) {
		return jsMap;
	}

	@Override
	public JSG_Instruction accept(JSG_Mark jsg_Mark) {
		return jsg_Mark;
	}

	@Override
	public ExpressionTraduction accept(JSG_New jsg_New) {
		return new ExpressionTraduction(jsg_New);
	}

	@Override
	public JSG_Instruction accept(JSG_NOP jsEmpty) {
		return jsEmpty;
	}

	@Override
	public ExpressionTraduction accept(JSG_Not jsNot) throws RuntimeException {
		return new ExpressionTraduction(jsNot);
	}

	@Override
	public ExpressionTraduction accept(JSG_Null jsg_Null) {
		return new ExpressionTraduction(jsg_Null);
	}

	@Override
	public ExpressionTraduction accept(JSG_Operator jsOperator) throws RuntimeException {
		ArrayList<JSG_Instruction> instrList = new ArrayList<>();
		ArrayList<JSG_Expression> exprList = new ArrayList<>();
		for (JSG_Expression exp : jsOperator.getOperands()) {
			if (exp != null) {
				ExpressionTraduction rexp = exp.visit(this);
				instrList.addAll(rexp.getInstructions());
				exprList.add(rexp.getExpression());
			} else
				exprList.add(exp);
		}
		return new ExpressionTraduction(instrList,
				new JSG_Operator(jsOperator.getLine(), jsOperator.getColumn(), jsOperator.getOperator(), exprList));
	}

	@Override
	public ExpressionTraduction accept(JSG_Orbit jsOrbit) throws RuntimeException {
		return new ExpressionTraduction(jsOrbit);
	}

	@Override
	public ExpressionTraduction accept(JSG_PackagedType jsType) {
		return new ExpressionTraduction(jsType);
	}

	@Override
	public JSG_Instruction accept(JSG_Print jsg_Print) {
		return jsg_Print;
	}

	@Override
	public JSG_Instruction accept(JSG_Return jsg_Return) {
		ExpressionTraduction expTrad = jsg_Return.getExpression().visit(this);
		JSG_Sequence seq = new JSG_Sequence(jsg_Return.getLine(), jsg_Return.getColumn());
		for (JSG_Instruction instr : expTrad.getInstructions()) {
			seq.add(instr);
		}
		seq.add(new JSG_Return(jsg_Return.getLine(), jsg_Return.getColumn(), expTrad.getExpression()));
		return seq;
	}

	@Override
	public ExpressionTraduction accept(JSG_Rule jsRule) {
		return new ExpressionTraduction(jsRule);
	}

	@Override
	public ExpressionTraduction accept(JSG_RuleArg jsg_RuleArg) {
		return new ExpressionTraduction(jsg_RuleArg);
		// TODO: refaire un visit dans l'expression !
	}

	@Override
	public ExpressionTraduction accept(JSG_RuleNode jsRuleNodeSem) {
		return new ExpressionTraduction(jsRuleNodeSem);
	}

	@Override
	public JSG_Instruction accept(JSG_Sequence jsSequence) throws RuntimeException {
		JSG_Sequence seq = new JSG_Sequence(jsSequence.getLine(), jsSequence.getColumn());
		for (JSG_Instruction i : jsSequence) {
			seq.add(i.visit(this));
		}
		return seq;
	}

	@Override
	public ExpressionTraduction accept(JSG_Size jsSizeSem) {
		return new ExpressionTraduction(jsSizeSem);
	}

	@Override
	public ExpressionTraduction accept(JSG_String jsString) throws RuntimeException {
		return new ExpressionTraduction(jsString);
	}

	@Override
	public JSG_Instruction accept(JSG_Throw jsg_Throw) {
		return jsg_Throw;
	}

	@Override
	public JSG_Instruction accept(JSG_Try jsg_Try) {
		ArrayList<JSG_Catch> catchList = new ArrayList<>();
		for (JSG_Catch c : jsg_Try.getCatchList()) {
			catchList.add((JSG_Catch) c.visit(this));
		}
		return new JSG_Try(jsg_Try.getTryBlock().visit(this), catchList,
				jsg_Try.getFinallyBlock() != null ? jsg_Try.getFinallyBlock().visit(this) : null, jsg_Try.getLine(),
						jsg_Try.getColumn());
	}

	@Override
	public ExpressionTraduction accept(JSG_Type jsType) {
		return new ExpressionTraduction(jsType);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypeBoolean jsg_TypeBoolean) {
		return new ExpressionTraduction(jsg_TypeBoolean);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypeJerboaDart jsTypeJerboaNodeSem) {
		return new ExpressionTraduction(jsTypeJerboaNodeSem);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypeJerboaHookList jsType) {
		return new ExpressionTraduction(jsType);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypeJerboaRule jsTypeJerboaRuleSem) {
		return new ExpressionTraduction(jsTypeJerboaRuleSem);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypeJerboaRuleResult jsTypeJerboaRuleResultSem) {
		return new ExpressionTraduction(jsTypeJerboaRuleResultSem);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypeMark jsTypeMark) {
		return new ExpressionTraduction(jsTypeMark);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypeOrbit jsTypeOrbit) {
		return new ExpressionTraduction(jsTypeOrbit);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypePrimitive jsg_TypePrimitive) {
		return new ExpressionTraduction(jsg_TypePrimitive);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypeString jsg_TypePrimitive) {
		return new ExpressionTraduction(jsg_TypePrimitive);
	}

	@Override
	public ExpressionTraduction accept(JSG_TypeTemplate jsTypeTemplate) {
		return new ExpressionTraduction(jsTypeTemplate);
	}

	@Override
	public JSG_Instruction accept(JSG_UnMark jsg_UnMark) {
		return jsg_UnMark;
	}

	@Override
	public ExpressionTraduction accept(JSG_Unreference jsg_Unreference) {
		return new ExpressionTraduction(jsg_Unreference);
	}

	@Override
	public ExpressionTraduction accept(JSG_UserType jsUserTypeSem) {
		return new ExpressionTraduction(jsUserTypeSem);
	}

	@Override
	public ExpressionTraduction accept(JSG_Variable jsVariable) throws RuntimeException {
		return new ExpressionTraduction(jsVariable);
	}

	@Override
	public JSG_While accept(JSG_While jsWhile) throws RuntimeException {
		return new JSG_While(jsWhile.getCondition(), jsWhile.getCorps().visit(this), jsWhile.getLine(),
				jsWhile.getColumn());
	}

	private String getFreshVariableName(String base) {
		String prefix = "_v_";
		int cpt = 0;
		while (context.varExists(prefix + base + cpt)) {
			cpt++;
		}
		return prefix + base + cpt;
	}

	private boolean ruleHasTopoParam(JMERule r, String name) {
		for (JMEParamTopo pto : r.getParamsTopo()) {
			if (pto.getName().compareTo(name) == 0) {
				return true;
			}
		}
		return false;
	}

}
