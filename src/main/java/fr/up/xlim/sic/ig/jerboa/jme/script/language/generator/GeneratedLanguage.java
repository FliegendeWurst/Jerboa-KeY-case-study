package fr.up.xlim.sic.ig.jerboa.jme.script.language.generator;

public class GeneratedLanguage {
	private StringBuilder include, inClassPrivate, inClassPublic, inClassConstructor, beginOfApplyRule, content;

	public GeneratedLanguage() {
		include = new StringBuilder();
		inClassPrivate = new StringBuilder();
		inClassPublic = new StringBuilder();
		inClassConstructor = new StringBuilder();
		content = new StringBuilder();
	}

	public GeneratedLanguage(GeneratedLanguage gl) {
		include = gl.include;
		inClassPrivate = gl.inClassPrivate;
		inClassPublic = gl.inClassPublic;
		inClassConstructor = gl.inClassConstructor;
		content = gl.content;
	}

	public GeneratedLanguage append(String separator, GeneratedLanguage gl) {
		include.append(separator).append(gl.include);
		inClassPrivate.append(separator).append(gl.inClassPrivate);
		inClassPublic.append(separator).append(gl.inClassPublic);
		inClassConstructor.append(separator).append(gl.inClassConstructor);
		content.append(separator).append(gl.content);
		return this;
	}

	public String getContent() {
		return content.toString();
	}

	public String getInClassConstructor() {
		return inClassConstructor.toString();
	}

	public String getInClassPrivate() {
		return inClassPrivate.toString();
	}

	public String getInClassPublic() {
		return inClassPublic.toString();
	}

	public String getInclude() {
		return include.toString();
	}

	public void appendContent(Object content) {
		this.content.append(content);
	}

	public void appendContentln(Object content) {
		this.content.append(content).append("\n");
	}
	
	public String getLastContentLine() {
		String contentStr = content.toString();
		if(contentStr.length()>0) {
			String [] lines = contentStr.split("\n");
			return lines[lines.length-1];
		}
		return "";
	}

	public void appendInClassConstructor(Object inClassConstructor) {
		this.inClassConstructor.append(inClassConstructor);
	}

	public void appendInClassPrivate(Object inClassPrivate) {
		this.inClassPrivate.append(inClassPrivate);
	}

	public void appendInClassPublic(Object inClassPublic) {
		this.inClassPublic.append(inClassPublic);
	}

	public void appendInclude(Object include) {
		this.include.append(include);
	}

}
