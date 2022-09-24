package myplugin.generator;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import com.nomagic.magicdraw.core.Application;

import freemarker.template.TemplateException;
import myplugin.generator.fmmodel.FMEnumeration;
import myplugin.generator.fmmodel.FMModel;
import myplugin.generator.options.GeneratorOptions;

public class EnumGenerator extends BasicGenerator {

	public final String PROJECT_NAME = Application.getInstance().getProject().getName();

	public EnumGenerator(GeneratorOptions generatorOptions) {
		super(generatorOptions);
	}

	public void generate() {

		try {
			super.generate();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}

		List<FMEnumeration> enums = FMModel.getInstance().getEnumerations();
		
		for(FMEnumeration en : enums) {
			Writer out;
			Map<String, Object> context = new HashMap<String, Object>();
			try {
				out = getWriter(en.getName(), en.getTypePackage());
				if (out != null) {
					context.clear();
					context.put("enum", en);
					context.put("app_name", PROJECT_NAME);
					context.put("values", en.getValues());
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
}