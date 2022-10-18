package fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.tools;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSAlpha;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSChoice;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSApplyRule;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSBoolean;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSCall;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSCast;
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
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.syntax.expr.JSIndexInLeftPattern;
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

public interface JSExprVisitor<T, E extends Exception> {

	T accept(JSCall jsCall) throws E;

	T accept(JSVariable jsVariable) throws E;

	T accept(JSOrbit jsOrbit) throws E;

	T accept(JSInScope jsInScope) throws E;

	T accept(JSCollectEbd jsCollect) throws E;

	T accept(JSCollect jsCollect) throws E;

	T accept(JSIndex jsIndex) throws E;

	T accept(JSAlpha jsAlpha) throws E;

	T accept(JSInteger jsInteger) throws E;

	T accept(JSFloat jsFloat) throws E;

	T accept(JSDouble jsDouble) throws E;

	T accept(JSBoolean jsBoolean) throws E;

	T accept(JSLong jsLong) throws E;

	T accept(JSString jsString) throws E;

	T accept(JSApplyRule jsApplyRule) throws E;

	T accept(JSOperator jsOperator) throws E;

	T accept(JSNot jsNot) throws E;

	T accept(JSKeywordGmap jsGmapKeyword) throws E;

	T accept(JSType jsType);

	T accept(JSChoice jsAlternativ);

	T accept(JSKeywordEbd jsKeywordEbd);

	T accept(JSList jsList);

	T accept(JSTypeTemplate jsTypeTemplate);

	T accept(JSRule jsRule);

	T accept(JSKeywordDimension jsKeywordDimension);

	T accept(JSKeywordModeler jsKeywordModeler);

	T accept(JSUnreference jsUnreference);

	T accept(JSIndirection jsIndirection);

	T accept(JSComment jsComment);

	T accept(JSConstructor jsConstructor);

	T accept(JSNew jsNew);

	T accept(JSInScopeStatic jsInScopeStatic);

	T accept(JSNull jsNull);

	T accept(JSRuleArg jsRuleArg);

	T accept(JSIsNotMarked jsIsNotMarked);

	T accept(JSIsMarked jsIsMarked);

	T accept(JSGMapSize jsgMapSize);

	T accept(JSKeywordLeftFilter jsKeywordLeftFilter);

	T accept(JSKeywordRightFilter jsKeywordRightFilter);

	T accept(JSPackagedType jsType);

	T accept(JSCast jsCast);

	T accept(JSIndexInLeftPattern jsIndexInLeftPattern);
}
