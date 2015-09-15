package net.unit8.occupypub.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author kawasima
 */
@Entity
public class Coupon implements Serializable {
    @Id
    @GeneratedValue
    @Column(insertable = true, updatable = true)
    private Long id;

    private String code;

    @ManyToOne
    private Group group;


}
