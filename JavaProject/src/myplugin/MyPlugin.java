package myplugin;

import java.io.File;

import javax.swing.JOptionPane;

import myplugin.generator.options.GeneratorOptions;
import myplugin.generator.options.ProjectOptions;


import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;

/** MagicDraw plugin that performes code generation */
public class MyPlugin extends com.nomagic.magicdraw.plugins.Plugin {
	
	String pluginDir = null; 
	
	public void init() {
		JOptionPane.showMessageDialog( null, "My Plugin init");
		
		pluginDir = getDescriptor().getPluginDirectory().getPath();
		
		// Creating submenu in the MagicDraw main menu 	
		ActionsConfiguratorsManager manager = ActionsConfiguratorsManager.getInstance();		
		manager.addMainMenuConfigurator(new MainMenuConfigurator(getSubmenuActions()));
		
		/** @Todo: load project options (@see myplugin.generator.options.ProjectOptions) from 
		 * ProjectOptions.xml and take ejb generator options */
		
		String rootDir = System.getProperty("user.home") + File.separator + "naziv_projekta";
		String resourcesDir = System.getProperty("user.home") + File.separator + "naziv_projekta" + File.separator + "src" + File.separator + "main" + File.separator + "resources";
		String javaDir = System.getProperty("user.home") + File.separator + "nayiv_projekta" + File.separator + "src" + File.separator + "main" + File.separator + "java";
		
		
		//for test purpose only:
		GeneratorOptions ejbOptions = new GeneratorOptions(javaDir, "ejbclass", "templates", "{0}.java", true, "ejb"); 				
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("EJBGenerator", ejbOptions);			
		ejbOptions.setTemplateDir(pluginDir + File.separator + ejbOptions.getTemplateDir()); //apsolutna putanja
		
		
		GeneratorOptions enumOptions = new GeneratorOptions(javaDir, "enum", "templates", "{0}.java", true, "enums"); 
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("EnumGenerator", enumOptions); 
		enumOptions.setTemplateDir(pluginDir + File.separator +  enumOptions.getTemplateDir());
		
		GeneratorOptions repositoryOptions = new GeneratorOptions(javaDir, "repositoryclass", "templates", "{0}Repository.java", true, "repository");
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("RepositoryGenerator", repositoryOptions);
		repositoryOptions.setTemplateDir(pluginDir + File.separator + repositoryOptions.getTemplateDir());
		
		
		GeneratorOptions serviceOptions = new GeneratorOptions(javaDir, "serviceClass", "templates", "{0}Service.java", true, "service");
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ServiceGenerator", serviceOptions);
		serviceOptions.setTemplateDir(pluginDir + File.separator + serviceOptions.getTemplateDir());
		
		
		GeneratorOptions serviceImplOptions = new GeneratorOptions(javaDir, "serviceImplClass", "templates", "{0}ServiceImpl.java", true, "service.impl");
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ServiceImplGenerator", serviceImplOptions);
		serviceImplOptions.setTemplateDir(pluginDir + File.separator + serviceImplOptions.getTemplateDir());
		
		
		GeneratorOptions controllerOptions = new GeneratorOptions(javaDir, "controllerClass", "templates", "{0}Controller.java", true, "controller");
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ControllerGenerator", controllerOptions);
		controllerOptions.setTemplateDir(pluginDir + File.separator + controllerOptions.getTemplateDir());
		
		
		GeneratorOptions pomOptions = new GeneratorOptions(rootDir, "pomFile", "templates", "pom.xml", true, "");
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("PomGenerator", pomOptions);
		pomOptions.setTemplateDir(pluginDir + File.separator + pomOptions.getTemplateDir());

		
		GeneratorOptions appPropOptions = new GeneratorOptions(resourcesDir, "appPropertiesFile", "templates", "application.properties", true, "");
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("ApplicationPropertiesGenerator", appPropOptions);
		appPropOptions.setTemplateDir(pluginDir + File.separator + appPropOptions.getTemplateDir());
		
		
		GeneratorOptions mainOptions = new GeneratorOptions(javaDir, "mainClass", "templates", "{0}.java", true, "");
		ProjectOptions.getProjectOptions().getGeneratorOptions().put("MainGenerator", mainOptions);
		mainOptions.setTemplateDir(pluginDir + File.separator + mainOptions.getTemplateDir());
		
	}

	private NMAction[] getSubmenuActions()
	{
	   return new NMAction[]{
			new GenerateAction("Generate"),
	   };
	}
	
	public boolean close() {
		return true;
	}
	
	public boolean isSupported() {				
		return true;
	}
}


