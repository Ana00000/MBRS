package ${class.typePackage};

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.${class.name};

@Repository
public interface ${class.name}Repository extends JpaRepository<${class.name}, Long> {

	<#list properties as property>
			<#if property.upper == 1>
			List<${class.name}> findBy<#if property.name != "">${property.name?cap_first}<#else>${property.type}</#if>(Long id);
			</#if>
	</#list>
}
