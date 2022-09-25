package ${class.typePackage};

import java.util.*;

import mbrs.team6.model.${class.name};
import org.springframework.stereotype.Service;

@Service
public interface ${class.name}Service {
<#list methods as method>
	<#if method.returnType.name == "Collection" || method.returnType.name == "Set" || method.returnType.name == "List" >
    ${method.returnType.name}<${class.name}> ${method.name}(<#list method.parameters as parameter>${parameter.type.name} ${parameter.name}<#sep>, </#sep></#list>);
    
	<#else>
    ${method.returnType.name} ${method.name}(<#list method.parameters as parameter>${parameter.type.name?cap_first} ${parameter.name}<#sep>, </#sep></#list>);
	
	</#if>
</#list>
}