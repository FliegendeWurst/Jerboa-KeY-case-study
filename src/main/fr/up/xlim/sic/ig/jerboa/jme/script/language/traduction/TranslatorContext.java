package fr.up.xlim.sic.ig.jerboa.jme.script.language.traduction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Expression;
import fr.up.xlim.sic.ig.jerboa.jme.script.language.tree.semantic.expr.JSG_Type;
import up.jerboa.core.util.Pair;

public class TranslatorContext {

	HashMap<String, ArrayList<Pair<JSG_Type, JSG_Expression>>> mapVariableToTypeNValue;

	Stack<ArrayList<String>> varDeclaredPerBlock;

	public TranslatorContext() {
		mapVariableToTypeNValue = new HashMap<>();
		varDeclaredPerBlock = new Stack<ArrayList<String>>();
		varDeclaredPerBlock.push(new ArrayList<>()); // On en mets un par défaut pour le block courant
	}
	
	public TranslatorContext(TranslatorContext context) {
		
	}

	public ArrayList<Pair<JSG_Type, JSG_Expression>> var(String name) {
		if (mapVariableToTypeNValue.containsKey(name)) {
			return mapVariableToTypeNValue.get(name);
		}
		return null;
	}

	public Pair<JSG_Type, JSG_Expression> varLastValue(String name) {
		if (mapVariableToTypeNValue.containsKey(name)) {
			return mapVariableToTypeNValue.get(name).get(mapVariableToTypeNValue.get(name).size() - 1);
		}
		return null;
	}

	public void beginBlock() {
		varDeclaredPerBlock.push(new ArrayList<>());
	}

	public void endBlock(){
		varDeclaredPerBlock.pop();
	}

	public Stack<ArrayList<String>> getAccessibleVariable(){
		Stack<ArrayList<String>> stack = new Stack<>();
		for (ArrayList<String> s : varDeclaredPerBlock) {
			ArrayList<String> tmp = new ArrayList<>();
			tmp.addAll(s);
			stack.push(tmp);
		}
		return stack;
	}

	public void declareVar(String name, JSG_Type type, JSG_Expression value) {
		varDeclaredPerBlock.peek().add(name);
		if(!mapVariableToTypeNValue.containsKey(name)){
			mapVariableToTypeNValue.put(name, new ArrayList<>());
		}
		mapVariableToTypeNValue.get(name).add(new Pair<JSG_Type, JSG_Expression>(type, value));
	}

	public boolean varExists(String name) {
		return mapVariableToTypeNValue.containsKey(name);
	}

	public static void main(String[] s) {

		// test pour voir si la copie était inversée
		TranslatorContext tc = new TranslatorContext();
		tc.beginBlock();
		tc.declareVar("test1", null, null);
		tc.declareVar("test2", null, null);
		tc.beginBlock();
		tc.declareVar("test3", null, null);
		tc.declareVar("test4", null, null);
		System.err.println("## " + tc.getAccessibleVariable());
		System.err.println("#> " + tc.varDeclaredPerBlock);

		tc.endBlock();
		System.err.println("## " + tc.getAccessibleVariable());
		System.err.println("#> " + tc.varDeclaredPerBlock);
		tc.endBlock();
	}

}
