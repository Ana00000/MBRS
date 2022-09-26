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