/**
 *
 */
package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.tools;

import java.util.ArrayList;

import fr.up.xlim.sic.ig.jerboa.jme.model.JMEModeler;
import fr.up.xlim.sic.ig.jerboa.jme.model.JMERule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Type;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Assignment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Block;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Catch;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Declare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Delete;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_DoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ExprInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_For;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_ForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_If;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Instruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_NOP;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_Sequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.instr.JSG_While;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSAlpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSBoolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSCall;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSChoice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSCollect;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSCollectEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSComment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSConstructor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSDouble;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSFloat;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSGMapSize;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSInScope;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSInScopeStatic;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIndex;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIndirection;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSInteger;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIsMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIsNotMarked;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordDimension;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordEbd;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordGmap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordLeftFilter;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordModeler;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSKeywordRightFilter;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSList;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSLong;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSNew;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSNot;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSNull;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSOperator;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSOrbit;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSPackagedType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSRuleArg;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSString;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSType;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSTypeTemplate;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSUnreference;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSVariable;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSAssignment;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSAtLang;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSBlock;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSBreak;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSCatch;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDeclare;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDeclareFunction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDelete;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSDoWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSExprInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSFor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSForEach;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSForLoop;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSHeader;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSHookCall;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSIf;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSInstruction;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSMap;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSNOP;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSPrint;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSReturn;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSSequence;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSTry;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSUnMark;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.instr.JSWhile;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSExprVisitor;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools.JSInstVisitor;

/**
 * @author Valentin
 *
 */

public abstract class ConvertToSemantic_common implements JSExprVisitor<JSGResult<JSG_Expression>, RuntimeException>,
		JSInstVisitor<JSGResult<JSG_Instruction>, RuntimeException> {

	protected JMEModeler modeler;
	protected JMERule rule;
	protected boolean isRule;

	public ConvertToSemantic_common(JMEModeler _modeler, JMERule _rule) {
		this.modeler = _modeler;
		this.rule = _rule;
		this.isRule = (_rule != null);
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSWhile jsWhile) throws RuntimeException {
		JSGResult<JSG_Instruction> res = new JSGResult<>();

		JSGResult<JSG_Expression> cond = jsWhile.getCondition().visit(this);
		JSGResult<JSG_Instruction> body = jsWhile.getCorps().visit(this);

		JSG_Sequence corps = new JSG_Sequence(0, 0);
		corps.addAll(body.getPreSequence());
		corps.add(body.getResult());
		corps.addAll(cond.getPreSequence());

		res.add(cond.getPreSequence());
		res.setResult(new JSG_While(cond.getResult(), corps, jsWhile.getLine(), jsWhile.getColumn()));

		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSAssignment jsAssignment) throws RuntimeException {

		JSGResult<JSG_Expression> lhs = jsAssignment.getVariable().visit(this);
		JSGResult<JSG_Expression> rhs = jsAssignment.getValue().visit(this);

		JSGResult<JSG_Instruction> res = new JSGResult<>();
		JSG_Assignment ass = new JSG_Assignment(lhs.getResult(), rhs.getResult(), jsAssignment.getLine(),
				jsAssignment.getColumn());

		res.add(lhs.getPreSequence());
		res.add(rhs.getPreSequence());

		res.setResult(ass);
		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSBlock jsBlock) throws RuntimeException {

		JSGResult<JSG_Instruction> res = new JSGResult<>();

		JSGResult<JSG_Instruction> block = jsBlock.getBody().visit(this);

		JSG_Sequence seq = new JSG_Sequence(0, 0);
		seq.add(block.getPreSequence());
		seq.add(block.getResult());

		res.setResult(new JSG_Block(seq, true, 0, 0));

		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSFor jsFor) throws RuntimeException {

		JSGResult<JSG_Expression> etype = jsFor.getType().visit(this);
		JSG_Type type = (JSG_Type) etype.getResult();

		if (!etype.getPreSequence().isEmpty())
			throw new RuntimeException("For: (" + jsFor.getLine() + ":" + jsFor.getColumn() + ")");

		JSGResult<JSG_Expression> start = jsFor.getStart().visit(this);
		JSGResult<JSG_Expression> end = jsFor.getEnd().visit(this);
		JSGResult<JSG_Instruction> step = jsFor.getStep().visit(this);

		JSGResult<JSG_Instruction> res = jsFor.getBody().visit(this);

		JSG_For ifor = new JSG_For(type, jsFor.getVariable(), start.getResult(), end.getResult(), step.getResult(),
				res.getResult(), jsFor.getLine(), jsFor.getColumn());

		JSGResult<JSG_Instruction> resFor = new JSGResult<>();
		resFor.setResult(ifor);
		return resFor;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSForEach jsForEach) throws RuntimeException {
		JSGResult<JSG_Expression> coll = jsForEach.getColl().visit(this);

		JSGResult<JSG_Expression> etype = jsForEach.getType().visit(this);
		JSG_Type type = (JSG_Type) etype.getResult();

		if (!etype.getPreSequence().isEmpty())
			throw new RuntimeException("For: (" + jsForEach.getLine() + ":" + jsForEach.getColumn() + ")");

		JSGResult<JSG_Instruction> body = jsForEach.getBody().visit(this);
		JSG_Sequence newbody = new JSG_Sequence(0, 0);

		newbody.add(body.getPreSequence());
		newbody.add(body.getResult());

		JSG_ForEach foreach = new JSG_ForEach(type, jsForEach.getName(), coll.getResult(), newbody, 0, 0);
		JSGResult<JSG_Instruction> res = new JSGResult<>();
		res.setResult(foreach);
		res.add(coll.getPreSequence());
		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSForLoop jsForLoop) throws RuntimeException {

		JSGResult<JSG_Instruction> init = jsForLoop.getInit().visit(this);
		JSGResult<JSG_Expression> cond = jsForLoop.getCond().visit(this);
		JSGResult<JSG_Instruction> step = jsForLoop.getStep().visit(this);

		JSGResult<JSG_Instruction> body = jsForLoop.getBody().visit(this);

		JSGResult<JSG_Instruction> res = new JSGResult<>();

		JSG_Sequence seq = new JSG_Sequence(0, 0);
		seq.addAll(body.getPreSequence());
		seq.add(body.getResult());

		JSG_ForLoop loop = new JSG_ForLoop(init.getResult(), cond.getResult(), step.getResult(), seq,
				jsForLoop.getLine(), jsForLoop.getColumn());

		res.add(init.getPreSequence());
		res.setResult(loop);
		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSIf jsIf) throws RuntimeException {

		JSGResult<JSG_Expression> cond = jsIf.getCondition().visit(this);
		JSGResult<JSG_Instruction> cons = jsIf.getConsequence().visit(this);
		JSGResult<JSG_Instruction> alt = jsIf.getAlternant().visit(this);

		JSGResult<JSG_Instruction> res = new JSGResult<>();
		res.add(cond.getPreSequence());

		JSG_If iif = new JSG_If(cond.getResult(), cons.toSequence(), alt.toSequence(), 0, 0);
		res.setResult(iif);

		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSSequence jsSequence) throws RuntimeException {

		ArrayList<JSG_Instruction> list = new ArrayList<>();
		JSGResult<JSG_Instruction> res = new JSGResult<>();
		for (JSInstruction inst : jsSequence) {
			list.add(inst.visit(this).toSequence());
		}
		res.setResult(new JSG_Sequence(0, 0, list));
		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSDoWhile jsDoWhile) throws RuntimeException {

		JSGResult<JSG_Expression> cond = jsDoWhile.getCondition().visit(this);
		JSGResult<JSG_Instruction> body = jsDoWhile.getBody().visit(this);

		JSGResult<JSG_Instruction> res = new JSGResult<>();
		res.add(cond.getPreSequence());
		res.setResult(new JSG_DoWhile(cond.getResult(), body.toSequence(), 0, 0));

		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSExprInstruction jsExprInstruction) throws RuntimeException {
		JSGResult<JSG_Expression> expr = jsExprInstruction.getExpr().visit(this);
		JSGResult<JSG_Instruction> res = new JSGResult<>();

		res.add(expr.getPreSequence());
		res.setResult(new JSG_ExprInstruction(expr.getResult()));
		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSDeclare jsDeclare) throws RuntimeException {
		JSGResult<JSG_Expression> etype = jsDeclare.getType().visit(this);
		JSG_Type type = (JSG_Type) etype.getResult();

		JSGResult<JSG_Expression> value = jsDeclare.getValue().visit(this);

		JSGResult<JSG_Instruction> res = new JSGResult<>();

		res.add(value.getPreSequence());

		res.setResult(new JSG_Declare(type, jsDeclare.getName(), value.getResult(), 0, 0));

		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSMap jsMap) {
		// JSGResult<JSG_Expression> etype = jsMap.getType().visit(this);
		// JSG_Type type = (JSG_Type)etype.getResult();
		//
		// JSGResult<JSG_Instruction> res = new JSGResult<>();

		throw new RuntimeException("BON ON VIRE CA. IL FAUT MIEUX LE PENSER");

	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSNOP jsEmpty) {
		JSGResult<JSG_Instruction> res = new JSGResult<>();
		res.setResult(new JSG_NOP());
		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSDelete jsDelete) {
		JSGResult<JSG_Expression> target = jsDelete.getName().visit(this);
		JSGResult<JSG_Instruction> res = new JSGResult<>(new JSG_Delete(target.getResult(), 0, 0));
		res.add(res.getPreSequence());
		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSCatch jsCatch) {
		JSGResult<JSG_Instruction> body = jsCatch.getBlock().visit(this);

		JSGResult<JSG_Expression> etype = jsCatch.getTypeExep().visit(this);
		JSG_Type type = (JSG_Type) etype.getResult();

		JSG_Catch cat = new JSG_Catch(body.toSequence(), new JSG_Declare(type, jsCatch.getNameExep(), null, 0, 0), 0,
				0);
		JSGResult<JSG_Instruction> res = new JSGResult<>(cat);
		return res;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSTry jsTry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSDeclareFunction jsDeclareFunction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSPrint jsPrint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSHookCall jsHookCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSAtLang jsAtLang) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSBreak jsBreak) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSHeader jsHeader) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSReturn jsReturn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSMark jsMark) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Instruction> accept(JSUnMark jsUnMark) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSCall jsCall) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSVariable jsVariable) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSOrbit jsOrbit) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSInScope jsInScope) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSCollectEbd jsCollect) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSCollect jsCollect) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSIndex jsIndex) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSAlpha jsAlpha) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSInteger jsInteger) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSFloat jsFloat) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSDouble jsDouble) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSBoolean jsBoolean) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSLong jsLong) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSString jsString) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSApplyRule jsApplyRule) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSOperator jsOperator) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSNot jsNot) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSKeywordGmap jsGmapKeyword) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSType jsType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSChoice jsAlternativ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSKeywordEbd jsKeywordEbd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSList jsList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSTypeTemplate jsTypeTemplate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSRule jsRule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSKeywordDimension jsKeywordDimension) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSKeywordModeler jsKeywordModeler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSUnreference jsUnreference) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSIndirection jsIndirection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSComment jsComment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSConstructor jsConstructor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSNew jsNew) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSInScopeStatic jsInScopeStatic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSNull jsNull) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSRuleArg jsRuleArg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSIsNotMarked jsIsNotMarked) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSIsMarked jsIsMarked) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSGMapSize jsgMapSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSKeywordLeftFilter jsKeywordLeftFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSKeywordRightFilter jsKeywordRightFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSGResult<JSG_Expression> accept(JSPackagedType jsType) {
		// TODO Auto-generated method stub
		return null;
	}

}
