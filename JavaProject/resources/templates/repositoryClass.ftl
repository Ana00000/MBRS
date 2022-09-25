package ${class.typePackage};

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import mbrs.team6.model.${class.name};

@Repository
public interface ${class.name}Repository extends JpaRepository<${class.name}, Long> {
}
