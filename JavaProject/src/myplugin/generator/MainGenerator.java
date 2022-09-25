package myplugin.generator;

import com.nomagic.magicdraw.core.Application;
import freemarker.template.TemplateException;
import myplugin.generator.options.GeneratorOptions;

import javax.swing.*;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class MainGenerator extends BasicGenerator {

	public final String PACKAGE_NAME = "java_project_placement";
	public final String PROJECT_NAME = Application.getInstance().getProject().getName();

	public MainGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
	}

	public void generate() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		Map<String, Object> context = new HashMap<String, Object>();
		Writer out;
		try {
			out = getWriter(PROJECT_NAME, PACKAGE_NAME);
			if (out != null) {
				context.clear();
				context.put("project_name", PROJECT_NAME);
				context.put("package", PACKAGE_NAME);
				getTemplate().process(context, out);
				out.flush();
			}
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}