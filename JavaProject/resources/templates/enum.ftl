package ${enum.typePackage};

public enum ${enum.name} {
	
<#list values as value>
    ${value?upper_case}<#sep>,</#sep>
</#list>
<#lt>

}