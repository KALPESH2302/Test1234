package in.org.rebit.repository;

import in.org.rebit.model.BpmnFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BpmnFileRepository extends JpaRepository<BpmnFiles, Long> {


}
