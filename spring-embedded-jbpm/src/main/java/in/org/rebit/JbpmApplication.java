package in.org.rebit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan( basePackages = {
        "org.drools.persistence.info",
        "org.jbpm.persistence.processinstance",
        "org.jbpm.kie.services.impl.query.persistence",
        "org.jbpm.persistence.processinstance",
        "org.jbpm.persistence.correlation",
        "org.jbpm.runtime.manager.impl.jpa",
        "org.jbpm.process.audit",
        "org.jbpm.executor.entities",
        "org.jbpm.casemgmt.impl.generator",
        "org.jbpm.runtime.manager.impl.jpa",
        "org.jbpm.kie.services.impl.store",
        "org.jbpm.shared.services.impl",
        "org.jbpm.services.task.impl.model",
        "org.jbpm.services.task.audit.impl.model",
        "org.jbpm.services.task.identity",
        "in.org.rebit"
})
public class JbpmApplication {
    public static void main(String[] args) {
        SpringApplication.run(JbpmApplication.class, args);
    }

}