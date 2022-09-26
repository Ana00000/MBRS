package myplugin.generator.fmmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FMClass extends FMType {	
	
	private String visibility;
	private String tableName;
	private List<FMProperty> FMProperties = new ArrayList<FMProperty>();
	private List<String> importedPackages = new ArrayList<String>();
	private List<FMReferencedProperty> referencedProperties = new ArrayList<>();
	private String label;
	/** @ToDo: add list of methods */
	private List<FMMethod> methods = new ArrayList<FMMethod>();

	public FMClass(String name, String classPackage, String visibility) {
		super(name, classPackage);		
		this.visibility = visibility;
	}	
	
	public FMClass(String name, String typePackage, String visibility, List<FMProperty> fMProperties,
			List<String> importedPackages, List<FMReferencedProperty> referencedProperties) {
		super(name, typePackage);
		this.visibility = visibility;
		FMProperties = fMProperties;
		this.importedPackages = importedPackages;
		this.referencedProperties = referencedProperties;
	}

	public List<FMReferencedProperty> getReferencedProperties() {
		return referencedProperties;
	}
		
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public List<FMProperty> getProperties(){
		return FMProperties;
	}
	
	public Iterator<FMProperty> getPropertyIterator(){
		return FMProperties.iterator();
	}
	
	public void addProperty(FMProperty property){
		FMProperties.add(property);		
	}
	
	public int getPropertyCount(){
		return FMProperties.size();
	}
	
	public List<String> getImportedPackages(){
		return importedPackages;
	}

	public Iterator<String> getImportedIterator(){
		return importedPackages.iterator();
	}
	
	public void addImportedPackage(String importedPackage){
		importedPackages.add(importedPackage);		
	}
	
	public int getImportedCount(){
		return FMProperties.size();
	}
	
	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}	
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void addReferencedProperty(FMReferencedProperty property) {
		referencedProperties.add(property);
	}
	
	public List<FMMethod> getMethods() {
		return methods;
	}

	public Iterator<FMMethod> getMethodIterator() {
		return methods.iterator();
	}

	public void addMethod(FMMethod method) {
		methods.add(method);
	}

	public int getMethodCount() {
		return methods.size();
	}
}
