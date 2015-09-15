package net.unit8.occupypub.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author kawasima
 */
@Entity
public class Membership implements Serializable {
    @Id
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "USER_ID")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    private GroupRole groupRole;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public GroupRole getGroupRole() {
        return groupRole;
    }

    public void setGroupRole(GroupRole groupRole) {
        this.groupRole = groupRole;
    }
}
