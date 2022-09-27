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
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Type;
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
			FMProperty prop = getPropertyData(p, cl);
			fmClass.addProperty(prop);
			
		}
		
		Stereotype entityStereotype = StereotypesHelper.getAppliedStereotypeByString(cl, "Entity");
		if (entityStereotype != null) {
			List<?> values = StereotypesHelper.getStereotypePropertyValue(cl, entityStereotype, "tableName");
			if (!((values == null) || (values.size() == 0))) {
				fmClass.setTableName(values.get(0).toString());
			}
		}
		
		
		return fmClass;
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
			fmProperty = createReferencedProperty(fmProperty, property, StereotypesHelper.getAppliedStereotypeByString(property, "ManyToOne"), "ManyToOne");
		} else if (StereotypesHelper.getAppliedStereotypeByString(property, "ManyToMany") != null) {
			fmProperty = createReferencedProperty(fmProperty, property, StereotypesHelper.getAppliedStereotypeByString(property, "ManyToMany"), "ManyToMany");
		} else if (StereotypesHelper.getAppliedStereotypeByString(property, "OneToMany") != null) {
			fmProperty = createReferencedProperty(fmProperty, property, StereotypesHelper.getAppliedStereotypeByString(property, "OneToMany"), "OneToMany");
		} else if (StereotypesHelper.getAppliedStereotypeByString(property, "OneToOne") != null) {
			fmProperty = createReferencedProperty(fmProperty, property, StereotypesHelper.getAppliedStereotypeByString(property, "OneToOne"), "OneToOne");
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
	
	private FMReferencedProperty createReferencedProperty(FMProperty fmProperty, Property property, Stereotype stereotype, String relationship) {
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
		
		FMReferencedProperty retVal = new FMReferencedProperty(fmProperty, fetchType, cascadeType, columnName, joinTable, oppositeEnd);
		
		retVal.setRelationship(relationship);
		
		return retVal;
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
