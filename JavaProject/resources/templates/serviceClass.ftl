package ${class.typePackage};

import java.util.*;

import mbrs.team6.model.${class.name};
import org.springframework.stereotype.Service;

@Service
public interface ${class.name}Service {
	${class.name} findOne(Long id);
	List<${class.name}> findAll();
	<#list properties as property>
	        <#if property.name != "id" && property.upper == 1>
	    List<${class.name}> findBy${property.name?cap_first}(${property.type} ${property.name});
	        </#if>
	</#list>
	${class.name} save(${class.name} ${class.name?uncap_first});
	${class.name} update(${class.name} ${class.name?uncap_first}) throws Exception;
	${class.name} delete(Long id)  throws Exception;
}