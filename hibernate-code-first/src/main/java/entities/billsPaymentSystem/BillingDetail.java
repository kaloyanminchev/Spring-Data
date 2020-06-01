package entities.billsPaymentSystem;

import entities.BaseEntity;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "billing_details")
public abstract class BillingDetail extends BaseEntity {
    private long number;
    private User user;

    public BillingDetail() {
    }

    @Column(name = "number")
    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
