package myplugin;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.nomagic.magicdraw.actions.MDAction;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import myplugin.analyzer.AnalyzeException;
import myplugin.analyzer.ModelAnalyzer;
import myplugin.generator.ApplicationPropertiesGenerator;
import myplugin.generator.ControllerGenerator;
import myplugin.generator.EJBGenerator;
import myplugin.generator.EnumGenerator;
import myplugin.generator.MainGenerator;
import myplugin.generator.PomGenerator;
import myplugin.generator.RepositoryGenerator;
import myplugin.generator.ServiceGenerator;
import myplugin.generator.ServiceImplGenerator;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.options.GeneratorOptions;
import myplugin.generator.options.ProjectOptions;
	
/** Action that activate code generation */
@SuppressWarnings("serial")
class GenerateAction extends MDAction{
	
	public GenerateAction(String name) {			
		super("", name, null, null);		
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent evt) {
		Project project = Application.getInstance().getProject();
		if (project == null || project.getModel() == null) {
			return;
		}
		
		try {
			new ModelAnalyzer(project.getModel(), "ejb").prepareModel();
			
			GeneratorOptions ejbGeneratorOptions = getGeneratorOptions("EJBGenerator");
			new EJBGenerator(ejbGeneratorOptions).generate();
			showGeneratedDialog(ejbGeneratorOptions);
			
			GeneratorOptions applicationPropertiesGeneratorOptions = getGeneratorOptions("ApplicationPropertiesGenerator");
			new ApplicationPropertiesGenerator(applicationPropertiesGeneratorOptions).generate();
			showGeneratedDialog(applicationPropertiesGeneratorOptions);
			
			GeneratorOptions controllerGeneratorOptions = getGeneratorOptions("ControllerGenerator");
			new ControllerGenerator(controllerGeneratorOptions).generate();
			showGeneratedDialog(controllerGeneratorOptions);
			
			GeneratorOptions enumGeneratorOptions = getGeneratorOptions("EnumGenerator");
			new EnumGenerator(enumGeneratorOptions).generate();
			showGeneratedDialog(enumGeneratorOptions);
			
			GeneratorOptions mainGeneratorOptions = getGeneratorOptions("MainGenerator");
			new MainGenerator(mainGeneratorOptions).generate();
			showGeneratedDialog(mainGeneratorOptions);
			
			GeneratorOptions pomGeneratorOptions = getGeneratorOptions("PomGenerator");
			new PomGenerator(pomGeneratorOptions).generate();
			showGeneratedDialog(pomGeneratorOptions);
			
			GeneratorOptions repositoryGeneratorOptions = getGeneratorOptions("RepositoryGenerator");
			new RepositoryGenerator(repositoryGeneratorOptions).generate();
			showGeneratedDialog(repositoryGeneratorOptions);
			
			GeneratorOptions serviceGeneratorOptions = getGeneratorOptions("ServiceGenerator");
			new ServiceGenerator(serviceGeneratorOptions).generate();
			showGeneratedDialog(serviceGeneratorOptions);
			
			GeneratorOptions serviceImplGeneratorOptions = getGeneratorOptions("ServiceImplGenerator");
			new ServiceImplGenerator(serviceImplGeneratorOptions).generate();
			showGeneratedDialog(serviceImplGeneratorOptions);
			
			exportToXml();
		} catch (AnalyzeException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} 			
	}
	
	private GeneratorOptions getGeneratorOptions(String generator) {
		return ProjectOptions.getProjectOptions().getGeneratorOptions().get(generator);
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