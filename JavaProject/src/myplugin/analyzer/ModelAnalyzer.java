package myplugin.analyzer;

import java.util.Iterator;
import java.util.List;

import myplugin.generator.fmmodel.FMClass;
import myplugin.generator.fmmodel.FMEnumeration;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.fmmodel.FMProperty;
import myplugin.generator.fmmodel.FMPersistentProperty;
import myplugin.generator.fmmodel.FMReferencedProperty;
import myplugin.generator.fmmodel.Strategy;

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

 * @ToDo: Enhance (or completely rewrite) myplugin.generator.fmmodel classes and  
 * Model Analyzer methods in order to support GUI generation. */ 


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

	public Package getRoot() {
		return root;
	}
	
	public void prepareModel() throws AnalyzeException {
		FMModel.getInstance().getClasses().clear();
		FMModel.getInstance().getEnumerations().clear();
		processPackage(root, filePackage);
	}
	
	private void processPackage(Package pack, String packageOwner) throws AnalyzeException {
		//Recursive procedure that extracts data from package elements and stores it in the 
		// intermediate data structure
		
		if (pack.getName() == null) throw  
			new AnalyzeException("Packages must have names!");
		
		String packageName = packageOwner;
		if (pack != root) {
			packageName += "." + pack.getName();
		}
		
		if (pack.hasOwnedElement()) {
			
			for (Iterator<Element> it = pack.getOwnedElement().iterator(); it.hasNext();) {
				Element ownedElement = it.next();
				if (ownedElement instanceof Class) {
					Class cl = (Class)ownedElement;
					FMClass fmClass = getClassData(cl, packageName);
					FMModel.getInstance().getClasses().add(fmClass);
				}
				
				if (ownedElement instanceof Enumeration) {
					Enumeration en = (Enumeration)ownedElement;
					FMEnumeration fmEnumeration = getEnumerationData(en, packageName);
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
			
			/** @ToDo:
			  * Process other package elements, as needed */ 
		}
	}
	
	private FMClass getClassData(Class cl, String packageName) throws AnalyzeException {
		if (cl.getName() == null) 
			throw new AnalyzeException("Classes must have names!");
		
		FMClass fmClass = new FMClass(cl.getName(), packageName, cl.getVisibility().toString());
		Iterator<Property> it = ModelHelper.attributes(cl);
		while (it.hasNext()) {
			Property p = it.next();
			FMProperty prop = getPropertyData(p, cl);
			fmClass.addProperty(prop);	
		}
		
		Stereotype s = StereotypesHelper.getAppliedStereotypeByString(cl, "Entity");
		
		if(s != null) {
			List<?> values = StereotypesHelper.getStereotypePropertyValue(cl, s, "tableName");
			
			if(!((values == null) || (values.size() == 0))) {
				fmClass.setTableName(values.get(0).toString());
			}
		}
		
		/** @ToDo:
		 * Add import declarations etc. */		
		return fmClass;
	}
	
	private FMProperty getPropertyData(Property p, Class cl) throws AnalyzeException {
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
			
		int lower = p.getLower();
		int upper = p.getUpper();
		
		FMProperty prop = new FMProperty(attName, typeName, p.getVisibility().toString(), 
				lower, upper);
		
		Stereotype persistentStereotype = StereotypesHelper.getAppliedStereotypeByString(p, "PersistentProperty");
		
		if(persistentStereotype != null) {
			prop = createPersistentProperty(prop, p, persistentStereotype);
		}
		
		
		return prop;		
	}	
	
	private FMPersistentProperty createPersistentProperty(FMProperty prop, Property p, Stereotype s) {
		
		String columnName = null;
		Integer length = null;
		Integer precision = null;
		Strategy strategy = null;
		Boolean isKey = null;
		Boolean isUnique = null;
		
		List<Property> tags = s.getOwnedAttribute();
		for(int i=0; i<tags.size(); i++) {
			Property tag = tags.get(i);
			String tagName = tag.getName();
			List<?> values = StereotypesHelper.getStereotypePropertyValue(p, s, tagName);
			
			if(values.size() > 0) {
				switch(tagName) {
					case "columnName":
						columnName = (String) values.get(0);
						break;
					case "length":
						length = (Integer) values.get(0);
						break;
					case "precision":
						precision = (Integer) values.get(0);
						break;
					case "strategy":
						strategy = (Strategy) values.get(0);
						break;
					case "isKey":
						isKey = (Boolean) values.get(0);
						break;
					case "isUnique":
						isUnique = (Boolean) values.get(0);
						break;
					
				}
			}
		}
		
		
		FMPersistentProperty newProp = new FMPersistentProperty(prop, columnName, length, precision, strategy, isKey, isUnique);
		
		return newProp;
	}
	
	private FMEnumeration getEnumerationData(Enumeration enumeration, String packageName) throws AnalyzeException {
		FMEnumeration fmEnum = new FMEnumeration(enumeration.getName(), packageName);
		List<EnumerationLiteral> list = enumeration.getOwnedLiteral();
		for (int i = 0; i < list.size() - 1; i++) {
			EnumerationLiteral literal = list.get(i);
			if (literal.getName() == null)  
				throw new AnalyzeException("Items of the enumeration " + enumeration.getName() +
				" must have names!");
			fmEnum.addValue(literal.getName());
		}
		return fmEnum;
	}	
	
	
}
