package net.unit8.occupypub.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * @author kawasima
 */
public class Signup implements Serializable {
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

    private String emailAddress;

    @ManyToOne
    private Coupon coupon;
}
