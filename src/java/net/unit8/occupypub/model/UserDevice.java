package net.unit8.occupypub.model;

import javax.persistence.*;

/**
 * @author kawasima
 */
@Entity
public class UserDevice {
    @Id
    @Column(insertable = true, updatable = true)
    @GeneratedValue
    private Long id;

    private String name;

    private String token;

    private String userAgent;

    @ManyToOne
    private User user;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
