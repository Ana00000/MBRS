package ${class.typePackage};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.Collection;

import mbrs.team6.repository.${class.name}Repository;
import mbrs.team6.service.${class.name}Service;
import mbrs.team6.model.${class.name};

@Service
@Qualifier("${class.name?uncap_first}ServiceImpl")
public class ${class.name}ServiceImpl implements ${class.name}Service {

    @Autowired
    private ${class.name}Repository ${class.name?uncap_first}Repository;
    
<#list methods as method>
	<#if method.returnType.name == "Collection" || method.returnType.name == "Set" || method.returnType.name == "List" >
		<#if method.name == "getAll">
    @Override
    ${method.visibility} ${method.returnType.name}<${class.name}> ${method.name}(<#list method.parameters as parameter>${parameter.type.name} ${parameter.name}<#sep>, </#sep></#list>){
        return ${class.name?uncap_first}Repository.findAll();
    }
    
	    </#if>
	<#else>
		<#if method.name == "save">
 	@Override
    ${method.visibility} ${method.returnType.name} ${method.name}(<#list method.parameters as parameter>${parameter.type.name} ${parameter.name}<#sep>, </#sep></#list>){
        return ${class.name?uncap_first}Repository.save(${class.name?uncap_first});
    }
    
		</#if>
		<#if method.name == "update">
 	@Override
    ${method.visibility} ${method.returnType.name} ${method.name}(<#list method.parameters as parameter>${parameter.type.name?cap_first} ${parameter.name}<#sep>, </#sep></#list>){
    	${class.name} ${class.name?uncap_first}ToSave = ${class.name?uncap_first}Repository.findById(id).get();
    	
    	<#list properties as property>${class.name?uncap_first}ToSave.set${property.name?cap_first}(${class.name?uncap_first}.get${property.name?cap_first}())<#sep>;
    	</#sep></#list>;
        return ${class.name?uncap_first}Repository.save(${class.name?uncap_first}ToSave);
    }
    
		</#if>	
		<#if method.name == "delete">
 	@Override
    ${method.visibility} ${method.returnType.name} ${method.name}(Long id){
        ${class.name?uncap_first}Repository.deleteById(id);
    }
    
		</#if>		
		<#if method.name == "getById">
 	@Override
    ${method.visibility} ${method.returnType.name} ${method.name}(<#list method.parameters as parameter>${parameter.type.name?cap_first} ${parameter.name?uncap_first}<#sep>, </#sep></#list>){
        return ${class.name?uncap_first}Repository.findById(id).get();
    }
    
		</#if>
	</#if>
</#list>
}