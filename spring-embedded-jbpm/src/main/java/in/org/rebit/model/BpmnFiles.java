package in.org.rebit.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "BPMN_FILES")
@SequenceGenerator(
        name = "bpmnFilesIdSeq",
        sequenceName = "BPMN_FILES_ID_SEQ",
        allocationSize = 1
)
public class BpmnFiles {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPMN_FILES_ID_SEQ")
    @SequenceGenerator(
            name = "bpmnFilesIdSeq",
            sequenceName = "BPMN_FILES_ID_SEQ",
            allocationSize = 1
    )
    private Long id;

    @Lob
    @Column(name = "FILE_CONTENT")
    private String fileContent;

    @Column(name = "PROCESS_ID")
    private String processId;

}
