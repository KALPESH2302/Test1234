package in.org.rebit.jbpm.services;

import in.org.rebit.model.BpmnFiles;
import in.org.rebit.repository.BpmnFileRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.EnvironmentName;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.*;
import org.kie.api.task.TaskService;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.manager.RuntimeManagerRegistry;
import org.kie.internal.runtime.manager.context.EmptyContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
public class BpmnInitializerService {

    private static final Logger logger = LoggerFactory.getLogger(BpmnInitializerService.class);
    private final EntityManagerFactory emf;
    private final RebitUserGroupCallBack rebitUserGroupCallBack;
    private final BpmnFileRepository bpmnFileRepository;
    private final PlatformTransactionManager transactionManager;
    private RuntimeManager runtimeManager;

    public BpmnInitializerService(EntityManagerFactory emf,
                                  RebitUserGroupCallBack rebitUserGroupCallBack,
                                  BpmnFileRepository bpmnFileRepository,
                                  PlatformTransactionManager transactionManager) {
        this.emf = emf;
        this.rebitUserGroupCallBack = rebitUserGroupCallBack;
        this.bpmnFileRepository = bpmnFileRepository;
        this.transactionManager = transactionManager;
    }

    @PostConstruct
    public synchronized void initRuntimeManager() {
        this.runtimeManager = createRuntimeManager();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    private RuntimeManager createRuntimeManager() {
        RuntimeEnvironmentBuilder builder = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder()
                .entityManagerFactory(emf)
                .userGroupCallback(rebitUserGroupCallBack)
                .addEnvironmentEntry(EnvironmentName.TRANSACTION_MANAGER, transactionManager)
                .addEnvironmentEntry(EnvironmentName.ENTITY_MANAGER_FACTORY, emf)
                .persistence(true);

        List<BpmnFiles> bpmnFiles = bpmnFileRepository.findAll();
        for (BpmnFiles bpmnFile : bpmnFiles) {
            builder.addAsset(
                    ResourceFactory.newByteArrayResource(bpmnFile.getFileContent().getBytes(StandardCharsets.UTF_8)),
                    ResourceType.BPMN2
            );
        }

        RuntimeEnvironment env = builder.get();
        return RuntimeManagerFactory.Factory.get().newPerRequestRuntimeManager(env);
    }

    public TaskService getTaskService(RuntimeEngine engine) {
        return engine.getTaskService();
    }

    public KieSession getKieSession(RuntimeEngine engine) {
        return engine.getKieSession();
    }

    public RuntimeEngine getRuntimeEngine(){
        return runtimeManager.getRuntimeEngine(EmptyContext.get());
    }

    public void disposeRuntimeEngine(RuntimeEngine engine){
        runtimeManager.disposeRuntimeEngine(engine);
    }

    public void closeRuntimeManager() {
        if (runtimeManager != null) {
            logger.info("Closing RuntimeManager");

            runtimeManager.close();
            RuntimeManagerRegistry.get().remove(runtimeManager);

            logger.info("RuntimeManager closed and deregistered.");
        }
    }
}