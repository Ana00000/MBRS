package myplugin.analyzer;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import myplugin.generator.fmmodel.FMClass;
import myplugin.generator.fmmodel.FMEnumeration;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.fmmodel.FMProperty;
import myplugin.generator.fmmodel.FMPersistentProperty;
import myplugin.generator.fmmodel.FMReferencedProperty;
import myplugin.generator.fmmodel.FMMethod;
import myplugin.generator.fmmodel.FMParameter;
import myplugin.generator.fmmodel.FMType;
import myplugin.generator.fmmodel.Strategy;
import myplugin.generator.fmmodel.FetchType;
import myplugin.generator.fmmodel.CascadeType;

import com.nomagic.uml2.ext.jmi.helpers.ModelHelper;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Enumeration;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Operation;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;


/** Model Analyzer takes necessary metadata from the MagicDraw model and puts it in 
 * the intermediate data structure (@see myplugin.generator.fmmodel.FMModel) optimized
 * for code generation using freemarker. Model Analyzer now takes metadata only for ejb code 
 * generation
 * */ 

public class ModelAnalyzer {	
	//root model package
	private Package root;
	
	//java root package for generated code
	private String filePackage;
	
	public ModelAnalyzer(Package root, String filePackage) {
		super();
		this.root = root;
		this.filePackage = filePackage;
	}
	
	public void prepareModel() throws AnalyzeException {
		FMModel.getInstance().getClasses().clear();
		FMModel.getInstance().getEnumerations().clear();
		processPackage(root, filePackage);
	}
	
	private void processPackage(Package pack, String packageOwner) throws AnalyzeException {
		//Recursive procedure that extracts data from package elements and stores it in the intermediate data structure
		if (pack.getName() == null) {
			throw new AnalyzeException("Packages must have names!");  
		}
		
		String packageName = packageOwner;
		if (pack != root) {
			packageName += "." + pack.getName();
		}
		
		if (pack.hasOwnedElement()) {
			for (Iterator<Element> it = pack.getOwnedElement().iterator(); it.hasNext();) {
				Element ownedElement = it.next();
				if (ownedElement instanceof Class) {
					FMClass fmClass = getClassData((Class)ownedElement, packageName);
					FMModel.getInstance().getClasses().add(fmClass);
				}
				if (ownedElement instanceof Enumeration) {
					FMEnumeration fmEnumeration = getEnumerationData((Enumeration)ownedElement, packageName);
					FMModel.getInstance().getEnumerations().add(fmEnumeration);
				}								
			}
			
			for (Iterator<Element> it = pack.getOwnedElement().iterator(); it.hasNext();) {
				Element ownedElement = it.next();
				if (ownedElement instanceof Package) {					
					Package ownedPackage = (Package)ownedElement;
					if (StereotypesHelper.getAppliedStereotypeByString(ownedPackage, "BusinessApp") != null)
						//only packages with stereotype BusinessApp are candidates for metadata extraction and code generation:
						processPackage(ownedPackage, packageName);
				}
			}
		}
	}
	
	private FMClass getClassData(Class cl, String packageName) throws AnalyzeException {
		if (cl.getName() == null) {
			throw new AnalyzeException("Classes must have names!");
		}
		
		FMClass fmClass = new FMClass(cl.getName(), packageName, cl.getVisibility().toString());
		Iterator<Property> it = ModelHelper.attributes(cl);
		while (it.hasNext()) {
			Property p = it.next();
			
			if (p.getOpposite() != null) {
				FMReferencedProperty referencedProperty = getReferencedPropertyData(p, cl);
				fmClass.addReferencedProperty(referencedProperty);
			} else {
				FMProperty prop = getPropertyData(p, cl);
				fmClass.addProperty(prop);
			}
		}
		
		Stereotype entityStereotype = StereotypesHelper.getAppliedStereotypeByString(cl, "Entity");
		if (entityStereotype != null) {
			List<?> values = StereotypesHelper.getStereotypePropertyValue(cl, entityStereotype, "tableName");
			if (!((values == null) || (values.size() == 0))) {
				fmClass.setTableName(values.get(0).toString());
			}
		}
		
		Iterator<Operation> op = ModelHelper.operations(cl);
		while (op.hasNext()) {
			Operation o = op.next();
			FMMethod met = getMethodData(o, cl);
			
			fmClass.addMethod(met);
		}
		
		return fmClass;
	}
	
	private FMMethod getMethodData(Operation o, Class cl) throws AnalyzeException {
		String methodName = o.getName();
		if (methodName == null) 
			throw new AnalyzeException("Operations of the class: " + cl.getName() +
					" must have names!");
		
		String methodVisibility = o.getVisibility().toString();
		if (methodVisibility == null) 
			throw new AnalyzeException("Operations of the class: " + cl.getName() +
					" must have visibility!");
		
		Type methodType = o.getType();
		if (methodType == null) 
			throw new AnalyzeException("Operations of the class: " + cl.getName() +
					" must have a return type!");
		
		String typeName = methodType.getName();
		if (typeName == null) 
			throw new AnalyzeException("Operations of the class: " + cl.getName() +
					" must have a return type name!");
		
		FMType retType = new FMType(typeName, "");
		
		FMMethod met = new FMMethod(methodVisibility, retType, methodName);
		
		List<FMParameter> parameters = new ArrayList<>();
		
		Iterator<Parameter> it = o.getOwnedParameter().iterator();	
		while (it.hasNext()) {
			Parameter p = it.next();
			if (p.getDirection().toString().equals("in")) {
				FMParameter par = getParameterData(p, met);
				parameters.add(par);
			}
		}
		
		met.setParameters(parameters);
		
		return met;
	}
	
	private FMParameter getParameterData(Parameter p, FMMethod met) throws AnalyzeException {
		String parName = p.getName();
		if (parName == null) 
			throw new AnalyzeException("Parameters of the operation: " + met.getName() +
					" must have names!");
		
		String parType = p.getType().getName();
		if (parType == null) 
			throw new AnalyzeException("Parameters of the operation: " + met.getName() +
					" must have types!");
		
		FMType parameterType = new FMType(parType, "");
		
		FMParameter par = new FMParameter(parameterType, parName);
		
		return par;
	}
	
	private FMProperty getPropertyData(Property property, Class cl) throws AnalyzeException {
		Type attType = property.getType();
		if (property.getName() == null) {
			throw new AnalyzeException("Properties of the class: " + cl.getName() + " must have names!");
		} else if (attType == null) {
			throw new AnalyzeException("Property " + cl.getName() + "." + property.getName() + " must have type!");
		} else if (attType.getName() == null) {
			throw new AnalyzeException("Type ot the property " + cl.getName() + "." + property.getName() + " must have name!");
		}
		
		FMProperty fmProperty = new FMProperty(property.getName(), attType.getName(), property.getVisibility().toString(),
												property.getLower(), property.getUpper());
		
		Stereotype persistentStereotype = StereotypesHelper.getAppliedStereotypeByString(property, "PersistentProperty");
		if (persistentStereotype != null) {
			fmProperty = createPersistentProperty(fmProperty, property, persistentStereotype);
		}
		
		if (StereotypesHelper.getAppliedStereotypeByString(property, "ManyToOne") != null) {
			fmProperty = createReferencedProperty(fmProperty, property, StereotypesHelper.getAppliedStereotypeByString(property, "ManyToOne"));
		} else if (StereotypesHelper.getAppliedStereotypeByString(property, "ManyToMany") != null) {
			fmProperty = createReferencedProperty(fmProperty, property, StereotypesHelper.getAppliedStereotypeByString(property, "ManyToMany"));
		} else if (StereotypesHelper.getAppliedStereotypeByString(property, "OneToMany") != null) {
			fmProperty = createReferencedProperty(fmProperty, property, StereotypesHelper.getAppliedStereotypeByString(property, "OneToMany"));
		} else if (StereotypesHelper.getAppliedStereotypeByString(property, "OneToOne") != null) {
			fmProperty = createReferencedProperty(fmProperty, property, StereotypesHelper.getAppliedStereotypeByString(property, "OneToOne"));
		}
		
		return fmProperty;		
	}
	
	private FMPersistentProperty createPersistentProperty(FMProperty fmProperty, Property property, Stereotype stereotype) {
		String columnName = null;
		Integer length = null;
		Integer precision = null;
		Strategy strategy = null;
		Boolean isKey = null;
		Boolean isNullable = null;
		Boolean isUnique = null;
		
		List<Property> tags = stereotype.getOwnedAttribute();
		for (int i = 0; i < tags.size(); i++) {
			List<?> stereotypePropertyValues = StereotypesHelper.getStereotypePropertyValue(property, stereotype, tags.get(i).getName());
			if (stereotypePropertyValues.size() > 0) {
				switch(tags.get(i).getName()) {
					case "columnName":
						columnName = (String) stereotypePropertyValues.get(0);
						break;
					case "length":
						length = (Integer) stereotypePropertyValues.get(0);
						break;
					case "precision":
						precision = (Integer) stereotypePropertyValues.get(0);
						break;
					case "strategy":
						if (stereotypePropertyValues.get(0) instanceof EnumerationLiteral) {
							strategy = Strategy.valueOf(getEnumerationString((EnumerationLiteral)stereotypePropertyValues.get(0)));
						}
						break;
					case "isKey":
						isKey = (Boolean) stereotypePropertyValues.get(0);
						break;
					case "isNullable":
						isNullable = (Boolean) stereotypePropertyValues.get(0);
						break;
					case "isUnique":
						isUnique = (Boolean) stereotypePropertyValues.get(0);
						break;
				}
			}
		}
		return new FMPersistentProperty(fmProperty, columnName, length, precision, strategy, isKey, isNullable, isUnique);
	}
	
	private FMReferencedProperty createReferencedProperty(FMProperty fmProperty, Property property, Stereotype stereotype) {
		FetchType fetchType = null;
		CascadeType cascadeType = null;
		String columnName = null;
		String joinTable = null;
		
		List<Property> tags = stereotype.getOwnedAttribute();
		for (Iterator<?> iterator = stereotype.getInheritedMember().iterator(); iterator.hasNext();) {
			tags.add((Property)iterator.next());
		}	
		
		for (int i = 0; i < tags.size(); i++) {
			List<?> values = StereotypesHelper.getStereotypePropertyValue(property, stereotype, tags.get(i).getName());
			if(values.size() > 0) {
				switch(tags.get(i).getName()) {
					case "columnName":
						columnName = (String) values.get(0);
						break;
					case "fetch":
						if(values.get(0) instanceof EnumerationLiteral) {
							fetchType = FetchType.valueOf(getEnumerationString((EnumerationLiteral)values.get(0)));
						}
						break;
					case "cascade":
						if(values.get(0) instanceof EnumerationLiteral) {
							cascadeType = CascadeType.valueOf(getEnumerationString((EnumerationLiteral)values.get(0)));
						}
						break;
					case "joinTable":
						joinTable = (String) values.get(0);
						break;
				}
			}
		}
		
		Property opposite = property.getOpposite();
		FMReferencedProperty oppositeEnd = new FMReferencedProperty(new FMProperty(opposite.getName(), opposite.getType().toString(),
				opposite.getVisibility().toString(), (Integer)opposite.getLower(), (Integer)opposite.getUpper()));
		
		return new FMReferencedProperty(fmProperty, fetchType, cascadeType, columnName, joinTable, oppositeEnd);
	}
	
	private FMReferencedProperty getReferencedPropertyData(Property p, Class cl) throws AnalyzeException {
		Stereotype referencedPropertyStereotype = null;
		
		String cascade = null;
		String fetchType = null;
		String mappedBy = null;
		String joinTable = null;
		String columnName = null;
		int upper = p.getUpper();
		Integer oppositeEnd = p.getOpposite().getUpper();
		if(upper == -1 && oppositeEnd == -1) {
			referencedPropertyStereotype = StereotypesHelper.getAppliedStereotypeByString(p, "ManyToMany");
		} else if(upper == -1 && oppositeEnd == 1) {
			referencedPropertyStereotype = StereotypesHelper.getAppliedStereotypeByString(p, "OneToMany");
		} else if(upper == 1 && oppositeEnd == -1) {
			referencedPropertyStereotype = StereotypesHelper.getAppliedStereotypeByString(p, "ManyToOne");
		} else {
			referencedPropertyStereotype = StereotypesHelper.getAppliedStereotypeByString(p, "OneToOne");
		}
		
		if(referencedPropertyStereotype != null) {
			List<Property> tags = referencedPropertyStereotype.getOwnedAttribute();
			for (int j = 0; j < tags.size(); ++j) {
				Property tagDef = tags.get(j);
				String tagName = tagDef.getName();
				String value = getTagValue(p, referencedPropertyStereotype, tagName);
			
				switch (tagName) {
					case "cascade":
						cascade = value;
						break;
					case "fetch":
						fetchType = value;
						break;
					case "joinTable":
						joinTable = value;
						break;
					case "mappedBy":
						mappedBy = value;
						break;
					case "columnName":
						columnName = value;
						break;
				}
			}
		}
		
		String attName = p.getName();
		if (attName == null) 
			throw new AnalyzeException("Properties of the class: " + cl.getName() +
					" must have names!");
		Type attType = p.getType();
		if (attType == null)
			throw new AnalyzeException("Property " + cl.getName() + "." +
			p.getName() + " must have type!");
		String typeName = attType.getName();
		if (typeName == null)
			throw new AnalyzeException("Type ot the property " + cl.getName() + "." +
			p.getName() + " must have name!");		
		FMReferencedProperty prop = new FMReferencedProperty(attName, typeName, p.getVisibility().toString(),
				p.getLower(), p.getUpper(), cascade, fetchType, joinTable, columnName, oppositeEnd);
		return prop;
	}
	
	private String getTagValue(Element el, Stereotype s, String tagName) {
		List<String> value = StereotypesHelper.getStereotypePropertyValueAsString(el, s, tagName);
		if(value == null)
			return null;
		if(value.size() == 0)
			return null;
		return value.get(0);
		
	}
	
	
	private String getEnumerationString(EnumerationLiteral enumerationLiteral) {
		return enumerationLiteral.getName();
	}
	
	private FMEnumeration getEnumerationData(Enumeration enumeration, String packageName) throws AnalyzeException {
		FMEnumeration fmEnum = new FMEnumeration(enumeration.getName(), packageName);
		for (int i = 0; i < enumeration.getOwnedLiteral().size() - 1; i++) {
			String literalName = enumeration.getOwnedLiteral().get(i).getName();
			if (literalName == null) {
				throw new AnalyzeException("Items of the enumeration " + enumeration.getName() + " must have names!");
			}
			fmEnum.addValue(literalName);
		}
		return fmEnum;
	}
}
