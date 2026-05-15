package in.org.rebit.dto;

import lombok.Data;

import java.util.List;

@Data
public class KavachUserGroups {

    private Long groupId;
    private String groupName;
    private String groupDesc;
    private List<String> roles;
    private List<String> users;
    private String status;
    private String createdDate;
    private Object makerComments;
    private Object workTime;

}
