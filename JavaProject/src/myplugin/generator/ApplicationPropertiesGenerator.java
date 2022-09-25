package myplugin.generator;

import com.nomagic.magicdraw.core.Application;
import freemarker.template.TemplateException;
import myplugin.generator.options.GeneratorOptions;

import javax.swing.*;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ApplicationPropertiesGenerator extends BasicGenerator {
	
	public final String PROJECT_NAME = Application.getInstance().getProject().getName();

	public ApplicationPropertiesGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
	}

	public void generate() {
		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		Writer out;
		Map<String, Object> context = new HashMap<String, Object>();
		try {
			out = getWriter("application.properties", "");
			context.clear();
			context.put("app_name", PROJECT_NAME);
			getTemplate().process(context, out);
			out.flush();
		} catch (TemplateException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}