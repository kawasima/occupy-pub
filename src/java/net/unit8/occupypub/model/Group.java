package net.unit8.occupypub.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author kawasima
 */
@Entity
@Table(name = "GROUPS")
public class Group implements Serializable {
    @Id
    @GeneratedValue
    @Column(insertable = true, updatable = true)
    private Long id;

    private String name;

    private String description;

    @ManyToMany
    @JoinTable(name="MEMBERSHIP",
            joinColumns = {@JoinColumn(name = "GROUP_ID")},
            inverseJoinColumns = {@JoinColumn(name = "USER_ID")})
    private List<User> users;

    @OneToMany(mappedBy = "group")
    private List<Membership> memberships;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }
}
