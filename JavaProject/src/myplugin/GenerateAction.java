package myplugin;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import myplugin.analyzer.AnalyzeException;
import myplugin.analyzer.ModelAnalyzer;
import myplugin.generator.ControllerGenerator;
import myplugin.generator.EJBGenerator;
import myplugin.generator.EnumGenerator;
import myplugin.generator.MainGenerator;
import myplugin.generator.RepositoryGenerator;
import myplugin.generator.ServiceGenerator;
import myplugin.generator.ServiceImplGenerator;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.options.GeneratorOptions;
import myplugin.generator.options.ProjectOptions;

@SuppressWarnings("serial")
class GenerateAction extends MDAction{
	public GenerateAction(String name) {			
		super("", name, null, null);		
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent evt) {
		if (Application.getInstance().getProject() == null || Application.getInstance().getProject().getModel() == null) {
			return;
		}
		
		try {
			new ModelAnalyzer(Application.getInstance().getProject().getModel(), "ejb").prepareModel();	
			
			Map<String, GeneratorOptions> generatorOptions = ProjectOptions.getProjectOptions().getGeneratorOptions();
			GeneratorOptions ejbGeneratorOptions = generatorOptions.get("EJBGenerator");
			new EJBGenerator(ejbGeneratorOptions).generate();
			showGeneratedDialog(ejbGeneratorOptions);
			
			GeneratorOptions controllerGeneratorOptions = generatorOptions.get("ControllerGenerator");
			new ControllerGenerator(controllerGeneratorOptions).generate();
			showGeneratedDialog(controllerGeneratorOptions);
			
			GeneratorOptions enumGeneratorOptions = generatorOptions.get("EnumGenerator");
			new EnumGenerator(enumGeneratorOptions).generate();
			showGeneratedDialog(enumGeneratorOptions);
			
			GeneratorOptions mainGeneratorOptions = generatorOptions.get("MainGenerator");
			new MainGenerator(mainGeneratorOptions).generate();
			showGeneratedDialog(mainGeneratorOptions);
			
			GeneratorOptions repositoryGeneratorOptions = generatorOptions.get("RepositoryGenerator");
			new RepositoryGenerator(repositoryGeneratorOptions).generate();
			showGeneratedDialog(repositoryGeneratorOptions);
			
			GeneratorOptions serviceGeneratorOptions = generatorOptions.get("ServiceGenerator");
			new ServiceGenerator(serviceGeneratorOptions).generate();
			showGeneratedDialog(serviceGeneratorOptions);
			
			GeneratorOptions serviceImplGeneratorOptions = generatorOptions.get("ServiceImplGenerator");
			new ServiceImplGenerator(serviceImplGeneratorOptions).generate();
			showGeneratedDialog(serviceImplGeneratorOptions);
			
			GeneratorOptions pomGeneratorOptions = generatorOptions.get("PomGenerator");
			//new PomGenerator(pomGeneratorOptions).generate();
			showGeneratedDialog(pomGeneratorOptions);
			
			GeneratorOptions applicationPropertiesGeneratorOptions = generatorOptions.get("ApplicationPropertiesGenerator");
			//new ApplicationPropertiesGenerator(applicationPropertiesGeneratorOptions).generate();
			showGeneratedDialog(applicationPropertiesGeneratorOptions);
			
			exportToXml();
		} catch (AnalyzeException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} 			
	}
	
	private void showGeneratedDialog(GeneratorOptions generatorOptions) {
		JOptionPane.showMessageDialog(null, "Code is successfully generated! Generated code is in folder: " + generatorOptions.getOutputPath() 
										+ ", package: " + generatorOptions.getFilePackage());
	}
	
	private void exportToXml() {
		if (JOptionPane.showConfirmDialog(null, "Do you want to save FM Model?") != JOptionPane.OK_OPTION 
			|| new JFileChooser().showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		BufferedWriter out;		
		XStream xstream = new XStream(new DomDriver());
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
					new JFileChooser().getSelectedFile().getAbsolutePath()), "UTF8"));					
			xstream.toXML(FMModel.getInstance().getClasses(), out);
			xstream.toXML(FMModel.getInstance().getEnumerations(), out);
		} catch (UnsupportedEncodingException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());				
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());				
		}
	}
}