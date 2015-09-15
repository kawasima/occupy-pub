package net.unit8.occupypub.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author kawasima
 */
@Entity
public class SerialNumber implements Serializable {
    @Id
    private Long id;
    private BigInteger value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigInteger getValue() {
        return value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }
}
