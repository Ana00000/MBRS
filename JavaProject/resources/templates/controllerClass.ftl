package ${class.typePackage};

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

<#assign mylist=class.typePackage?split(".")>


<#assign x=mylist?size-2>
<#assign baseDir=mylist[0]>

<#list 1..x as i>
	<#assign baseDir = baseDir+"."+mylist[i]>
</#list>

import ${baseDir}.model.*;
import ${baseDir}.service.*;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/${class.name?uncap_first}")
${class.visibility} class ${class.name}Controller {  

	@Autowired
	@Qualifier("${class.name?uncap_first}ServiceImpl")
	private ${class.name}Service ${class.name?uncap_first}Service;
	
	<#list referencedProperties as property>
	@Autowired
	@Qualifier("${property.type?uncap_first}ServiceImpl")
	private ${property.type?cap_first}Service ${property.type?uncap_first}Service;
	</#list>
	
	@GetMapping
	public ResponseEntity<Collection<${class.name}>> getAll() {
		return new ResponseEntity<Collection<${class.name}>>(${class.name?uncap_first}Service.getAll(), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
	public ResponseEntity<${class.name}> getById(@PathVariable("id") Long id) {
		return new ResponseEntity<${class.name}>(${class.name?uncap_first}Service.getById(id), HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ${class.name} ${class.name?uncap_first}) {
        try {
        	${class.name} ${class.name?uncap_first}Updated = ${class.name?uncap_first}Service.save(${class.name?uncap_first});
            return new ResponseEntity<${class.name}>(${class.name?uncap_first}Updated, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.BAD_REQUEST);
        }
    }
      
    @PostMapping
    public ResponseEntity<${class.name}> save(@RequestBody ${class.name} ${class.name?uncap_first}){
    		${class.name} ${class.name?uncap_first}Saved = ${class.name?uncap_first}Service.save(${class.name?uncap_first});
            return new ResponseEntity<${class.name}>(${class.name?uncap_first}Saved, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
    	
    	try {
    		${class.name?uncap_first}Service.delete(id);
    		return new ResponseEntity<>(HttpStatus.OK);
    	}
    	catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}
    }
 
}