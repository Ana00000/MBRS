package myplugin;

import java.io.File;
import java.util.Map;

import javax.swing.JOptionPane;

import myplugin.generator.options.GeneratorOptions;
import myplugin.generator.options.ProjectOptions;

import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.actions.ActionsConfiguratorsManager;
import com.nomagic.magicdraw.plugins.Plugin;

/** MagicDraw plugin that performes code generation */
public class MyPlugin extends Plugin {
	
	String pluginDir = null; 
	
	public void init() {
		JOptionPane.showMessageDialog( null, "My Plugin initialization.");
		// Creating submenu in the MagicDraw main menu 		
		ActionsConfiguratorsManager.getInstance().addMainMenuConfigurator(new MainMenuConfigurator(getSubmenuActions()));
		
		Map<String, GeneratorOptions> generatorOptions = ProjectOptions.getProjectOptions().getGeneratorOptions();
		pluginDir = getDescriptor().getPluginDirectory().getPath();
		String rootDir = System.getProperty("user.home") + File.separator + "PluginDevelopment";
		String javaDir = rootDir + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + "com" + File.separator + "example" + File.separator + "demo";
		
		//for test purpose only:
		GeneratorOptions ejbOptions = new GeneratorOptions(javaDir + File.separator + "model", "ejbclass", "templates", "{0}.java", true, "com.example.demo.model"); 				
		generatorOptions.put("EJBGenerator", ejbOptions);			
		ejbOptions.setTemplateDir(pluginDir + File.separator + ejbOptions.getTemplateDir());
		
		GeneratorOptions enumOptions = new GeneratorOptions(javaDir, "enum", "templates", "{0}.java", true, "enums"); 
		generatorOptions.put("EnumGenerator", enumOptions); 
		enumOptions.setTemplateDir(pluginDir + File.separator +  enumOptions.getTemplateDir());
		
		GeneratorOptions repositoryOptions = new GeneratorOptions(javaDir + File.separator + "repository", "repositoryclass", "templates", "{0}Repository.java", true, "com.example.demo.repository");
		generatorOptions.put("RepositoryGenerator", repositoryOptions);
		repositoryOptions.setTemplateDir(pluginDir + File.separator + repositoryOptions.getTemplateDir());
		
		
		GeneratorOptions serviceOptions = new GeneratorOptions(javaDir + File.separator + "service", "serviceClass", "templates", "{0}Service.java", true, "com.example.demo.service");
		generatorOptions.put("ServiceGenerator", serviceOptions);
		serviceOptions.setTemplateDir(pluginDir + File.separator + serviceOptions.getTemplateDir());
		
		
		GeneratorOptions serviceImplOptions = new GeneratorOptions(javaDir + File.separator + "service" + File.separator + "impl", "serviceImplClass", "templates", "{0}ServiceImpl.java", true, "com.example.demo.service.impl");
		generatorOptions.put("ServiceImplGenerator", serviceImplOptions);
		serviceImplOptions.setTemplateDir(pluginDir + File.separator + serviceImplOptions.getTemplateDir());
		
		
		GeneratorOptions controllerOptions = new GeneratorOptions(javaDir + File.separator + "controller", "controllerClass", "templates", "{0}Controller.java", true, "com.example.demo.controller");
		generatorOptions.put("ControllerGenerator", controllerOptions);
		controllerOptions.setTemplateDir(pluginDir + File.separator + controllerOptions.getTemplateDir());
		
		
		GeneratorOptions pomOptions = new GeneratorOptions(rootDir, "pomFile", "templates", "pom.xml", true, "");
		generatorOptions.put("PomGenerator", pomOptions);
		pomOptions.setTemplateDir(pluginDir + File.separator + pomOptions.getTemplateDir());

		
		GeneratorOptions appPropOptions = new GeneratorOptions(rootDir + File.separator + "src" + File.separator + "main" + File.separator + "resources", "appPropertiesFile", "templates", "application.properties", true, "");
		generatorOptions.put("ApplicationPropertiesGenerator", appPropOptions);
		appPropOptions.setTemplateDir(pluginDir + File.separator + appPropOptions.getTemplateDir());
		
		
		GeneratorOptions mainOptions = new GeneratorOptions(javaDir, "mainClass", "templates", "{0}.java", true, "");
		generatorOptions.put("MainGenerator", mainOptions);
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


