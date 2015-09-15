package net.unit8.occupypub.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author kawasima
 */
@Entity
public class AccountLock implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String unlockCode;
    private LocalDateTime expires;
    private AccountLockReason reason;

    private LocalDateTime createdAt;
}
