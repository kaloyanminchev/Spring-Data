package softuni.xmllab.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "phones")
public class Phone extends BaseEntity {

    private String phoneNumber;
    private User user;

    public Phone() {
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
