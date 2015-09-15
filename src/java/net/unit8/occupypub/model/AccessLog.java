package net.unit8.occupypub.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author kawasima
 */
@Entity
public class AccessLog implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate sessionStartedOn;
    private String userName;
    private String deviceName;
    private String purpose;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSessionStartedOn() {
        return sessionStartedOn;
    }

    public void setSessionStartedOn(LocalDate sessionStartedOn) {
        this.sessionStartedOn = sessionStartedOn;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
