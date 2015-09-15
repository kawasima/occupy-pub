package net.unit8.occupypub.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kawasima
 */
@Entity
@NamedQuery(name = "findAllWithCert", query ="SELECT u FROM User u")
public class User {
    @Id
    @GeneratedValue
    @Column(insertable = true, updatable = true)
    private Long id;

    private String uid;

    private String sirName;
    private String givenName;

    private String countryName;
    private String provinceName;
    private String localityName;

    private String organizationName;
    private String organizationUnitName;
    private String commonName;

    private boolean admin;

    private String emailAddress;
    private byte[] privateKey;

    @OneToMany(mappedBy = "user")
    private List<Cert> certList;

    @OneToMany(mappedBy = "user")
    private List<Membership> membershipList;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "MEMBERSHIP",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "GROUP_ID")}
    )
    private List<Group> groups;

    public User() {
        certList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationUnitName() {
        return organizationUnitName;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSirName() {
        return sirName;
    }

    public void setSirName(String sirName) {
        this.sirName = sirName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public byte[] getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(byte[] privateKey) {
        this.privateKey = privateKey;
    }

    public List<Cert> getCertList() {
        return certList;
    }

    public void setCertList(List<Cert> certList) {
        this.certList = certList;
    }

    public List<Membership> getMembershipList() {
        return membershipList;
    }

    public void setMembershipList(List<Membership> membershipList) {
        this.membershipList = membershipList;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
