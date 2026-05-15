package in.org.rebit.jbpm.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.org.rebit.dto.KavachUserGroups;
import in.org.rebit.service.KavachRestImpl;
import org.jbpm.services.task.impl.model.GroupImpl;
import org.jbpm.services.task.impl.model.UserImpl;
import org.kie.api.task.UserGroupCallback;
import org.kie.api.task.model.Group;
import org.kie.api.task.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RebitUserGroupCallBack implements UserGroupCallback {

    @Autowired
    KavachRestImpl kavachRestImpl;

    private Map<User, List<Group>> userGroupMapping;

    public RebitUserGroupCallBack() {
        userGroupMapping = new HashMap<>();
        this.userGroupMapping.put(new UserImpl("jbpm-admin"), List.of(new GroupImpl("Administrators")));
        this.userGroupMapping.put(new UserImpl("admin"), List.of(new GroupImpl("Administrators")));
    }

//    public RebitUserGroupCallBack(@Value("${kavach.retrieve-users.url}") String retrieveUsersUrl,
//                                  @Value("${kavach.retrieve-groups.url}") String retrieveGroupsUrl,
//                                  @Value("${kavach.token}") String token,
//                                  KavachRestImpl kavachRestImpl) throws JsonProcessingException {
//
//        this.kavachRestImpl = kavachRestImpl;
//        this.userGroupMapping = new HashMap<>();
//
//        List<KavachUserGroups> groupList = parseKavachGroupResponse(fetchGroupData(retrieveGroupsUrl, token));
//        Map<String, List<String>> userToGroups = buildUserToGroupMap(groupList);
//        this.userGroupMapping = convertToUserGroupMapping(userToGroups);
//        this.userGroupMapping.put(new UserImpl("admin"), List.of(new GroupImpl("Administrators")));
//
//    }

    public boolean existsUser(String userId) {
        for (User user : this.userGroupMapping.keySet()) {
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsGroup(String groupId) {
        for (User user : this.userGroupMapping.keySet()) {
            List<Group> groups = this.userGroupMapping.get(user);
            for (Group group : groups) {
                if (group.getId().equals(groupId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getGroupsForUser(String userId) {
        return this.userGroupMapping.entrySet().stream()
                .filter(entry -> entry.getKey().getId().equals(userId))
                .findFirst()
                .map(entry -> entry.getValue().stream()
                        .map(Group::getId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private String fetchGroupData(String retrieveGroupsUrl, String token) {
        return kavachRestImpl.fetchKavachDetailsAPI(retrieveGroupsUrl, token);
    }

    private List<KavachUserGroups> parseKavachGroupResponse(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(json, new TypeReference<List<KavachUserGroups>>() {});
    }

    private Map<String, List<String>> buildUserToGroupMap(List<KavachUserGroups> groups) {
        return groups.stream()
                .flatMap(g -> g.getUsers()
                        .stream()
                        .map(u -> Map.entry(u, g.getGroupName())))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));
    }

    private Map<User, List<Group>> convertToUserGroupMapping(Map<String, List<String>> userToGroups) {
        Map<User, List<Group>> result = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : userToGroups.entrySet()) {
            UserImpl user = new UserImpl(entry.getKey());
            List<Group> groups = entry.getValue()
                    .stream()
                    .map(GroupImpl::new)
                    .collect(Collectors.toList());
            result.put(user, groups);
        }
        return result;
    }

}
