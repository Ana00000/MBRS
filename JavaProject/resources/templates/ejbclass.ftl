package ${class.typePackage};

import javax.persistence.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table
${class.visibility} class ${class.name} {
<#list properties as property>
   <#if property.name == "id" >
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    ${property.visibility} ${property.type?cap_first} ${property.name?uncap_first};
    
    <#elseif property.type?lower_case?contains("enum")>
    @Enumerated(EnumType.STRING)
    ${property.visibility} ${property.type?cap_first} ${property.name?uncap_first};

   <#elseif property.type == "date" >
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    ${property.visibility} ${property.type?cap_first} ${property.name?uncap_first};
   
   <#elseif property.type == "long">
    @Column
    ${property.visibility} ${property.type?cap_first} ${property.name?uncap_first};
   
   <#elseif property.upper == 1 >
    @Column
    ${property.visibility} ${property.type} ${property.name?uncap_first};
   
   <#else>
       <#list 1..property.upper as i>
    ${property.visibility} ${property.type} ${property.name?uncap_first}${i};
       </#list>
   </#if>
</#list>

<#list referencedProperties as property>
	<#if property.upper == -1 && property.oppositeEnd == -1>
    @ManyToMany
	<#elseif property.upper == -1 && property.oppositeEnd == 1>
    @OneToMany
	<#elseif property.upper == 1 && property.oppositeEnd== -1>
    @ManyToOne
	<#else>
    @OneToOne
	</#if>
    <#if (property.fetch)?? || (property.cascade)?? || (property.mappedBy)?? || (property.optional)?? >
    (
		<#if (property.cascade)??>
        cascade = CascadeType.${property.cascade}
		</#if>
		<#if (property.fetch)??>
			<#lt><#if (property.cascade)??>,
			</#if>
        fetch = FetchType.${property.fetch}
			</#if>
		<#if (property.mappedBy)??>
			<#lt><#if (property.cascade)?? || (property.fetch)??>,
			</#if>
        mappedBy = "${property.mappedBy}"
		</#if>
    )
	</#if>
	<#if (property.joinTable)??>
    @JoinTable(name="${property.joinTable}")
	</#if>
	<#if (property.columnName)??>
    @JoinColumn(name="${property.columnName}")
	</#if>
    ${property.visibility} <#if property.upper == -1>Set<</#if>${property.type?cap_first}<#if property.upper == -1>></#if> ${property.name?uncap_first};

</#list>
<#list properties as property>
	<#if property.upper == 1 >
    <#if property.type == "date" || property.type == "long" >
    public ${property.type?cap_first} get${property.name?cap_first}(){
        return ${property.name?uncap_first};
    }
    <#else>
    public ${property.type} get${property.name?cap_first}(){
        return ${property.name?uncap_first};
    }
    </#if>
    <#if property.type == "date" || property.type == "long" >
    public void set${property.name?cap_first}(${property.type?cap_first} ${property.name?uncap_first}){
        this.${property.name?uncap_first} = ${property.name?uncap_first};
    }
    <#else>
    public void set${property.name?cap_first}(${property.type} ${property.name?uncap_first}){
        this.${property.name?uncap_first} = ${property.name?uncap_first};
    }
    </#if>
	<#elseif property.upper == -1 >
    public Set<${property.type}> get${property.name?cap_first}(){
        return ${property.name?uncap_first};
    }

    public void set${property.name?cap_first}( Set<${property.type}> ${property.name?uncap_first}){
        ${property.name?uncap_first} = ${property.name?uncap_first};
    }
	<#else>
    <#list 1..property.upper as i>
    public ${property.type} get${property.name?cap_first}${i}(){
        return ${property.name?uncap_first}${i};
    }

    public void set${property.name?cap_first}${i}(${property.type} ${property.name?uncap_first}${i}){
        ${property.name?uncap_first}${i} = ${property.name?uncap_first}${i};
    }
    </#list>
	</#if>
</#list>
<#list  referencedProperties as property>
    <#if property.upper == -1>
    public Set<${property.type}> get${property.name?cap_first}(){
        return ${property.name?uncap_first};
    }
    public void set${property.name?cap_first}(Set<${property.type}> ${property.name?uncap_first}){
        this.${property.name?uncap_first} = ${property.name?uncap_first};
    }
    <#elseif property.upper == 1 && property.oppositeEnd== -1>
    public ${property.type} get${property.name?cap_first}(){
        return ${property.name?uncap_first};
    }
    public void set${property.name?cap_first}(${property.type} ${property.name?uncap_first}){
        this.${property.name?uncap_first} = ${property.name?uncap_first};
    }
    </#if>
</#list>

}
