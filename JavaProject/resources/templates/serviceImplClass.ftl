package ${class.typePackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.Collection;

import com.example.demo.repository.${class.name}Repository;
import com.example.demo.service.${class.name}Service;
import com.example.demo.model.${class.name};

@Service
@Qualifier("${class.name?uncap_first}ServiceImpl")
public class ${class.name}ServiceImpl implements ${class.name}Service {
	@Autowired
    private ${class.name}Repository ${class.name?uncap_first}Repository;

    <#list properties as property>
    @Autowired
    private ${property.type?cap_first}Repository ${property.type?uncap_first}Reposiroty;

    </#list>
    @Override
    public ${class.name} findOne(Long id){
        return ${class.name?uncap_first}Repository.findById(id).orElse(null);
    }

    @Override
    public List<${class.name}> findAll(){
        return ${class.name?uncap_first}Repository.findAll();
	}
	
	<#list properties as property>
		<#if property.name != "id" && property.upper == 1>
	@Override
	public List<${class.name}> findBy${property.name?cap_first}(${property.type} ${property.name}){
		return ${class.name?uncap_first}Repository.findBy${property.name?cap_first}(${property.name});
	}
		</#if>
	</#list>
	
	@Override
	public ${class.name} save(${class.name} ${class.name?uncap_first}){
		return ${class.name?uncap_first}Repository.save(${class.name?uncap_first});
	}
	
	@Override
	public ${class.name} update(${class.name} ${class.name?uncap_first}) throws Exception{
		${class.name} ${class.name?uncap_first}ToSave = ${class.name?uncap_first}Repository.findById(id).get();
		<#list properties as property>${class.name?uncap_first}ToSave.set${property.name?cap_first}(${class.name?uncap_first}.get${property.name?cap_first}())<#sep>;
    	</#sep></#list>;
        return ${class.name?uncap_first}Repository.save(${class.name?uncap_first}ToSave);
	}
	
	@Override
	public ${class.name} delete(Long id)  throws Exception{
		${class.name?uncap_first}Repository.deleteById(id);
	}
	
}