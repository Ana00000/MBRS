package myplugin.generator.fmmodel;

import java.util.ArrayList;
import java.util.List;

public class FMMethod extends FMElement {

	private String visibility;

	private FMType returnType;

	private String name;

	private List<FMParameter> parameters = new ArrayList<FMParameter>();

	public FMMethod(String name) {
		super(name);
	}

	public FMMethod(String visibility, FMType returnType, String name) {
		super(name);
		this.visibility = visibility;
		this.returnType = returnType;
		this.name = name;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public FMType getReturnType() {
		return returnType;
	}

	public void setReturnType(FMType returnType) {
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FMParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<FMParameter> parameters) {
		this.parameters = parameters;
	}
}
