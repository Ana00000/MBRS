package ${class.typePackage};

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

<#assign mylist=class.typePackage?split(".")>


<#assign x=mylist?size-2>
<#assign baseDir=mylist[0]>

<#list 1..x as i>
	<#assign baseDir = baseDir+"."+mylist[i]>
</#list>

import ${baseDir}.model.*;

@Repository
public interface ${class.name}Repository extends JpaRepository<${class.name}, Long> {
}
