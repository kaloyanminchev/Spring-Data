package com.jsonexdemo.models.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    private String firstName;
    private String lastName;
    private int age;
    private List<Product> boughtProducts;
    private List<Product> soldProducts;
    private Set<User> friends;

    public User() {
    }

    @Column(name = "first_name", nullable = true)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = false)
    @Length(min = 3, message = "Last name is not valid!")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @OneToMany(mappedBy = "buyer")
    public List<Product> getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(List<Product> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
    public List<Product> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(List<Product> soldProducts) {
        this.soldProducts = soldProducts;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_friends",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public void addFriend(User newFriend) {
        if (this.getId() == newFriend.getId()) {
            return;
        }
        boolean incorrectUser = false;
        for (User currentFriend : this.friends) {
            if (currentFriend.getId() == newFriend.getId()) {
                incorrectUser = true;
                break;
            }
        }
        if (!incorrectUser) {
            this.friends.add(newFriend);
        }

    }
}
