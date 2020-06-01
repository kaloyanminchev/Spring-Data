package com.softuni.springautomappingdemo.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    private User user;
    private List<Game> orderedGames;

    public Order() {
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "orders_ordered_games",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ordered_games_id", referencedColumnName = "id"))
    public List<Game> getOrderedGames() {
        return orderedGames;
    }

    public void setOrderedGames(List<Game> orderedGames) {
        this.orderedGames = orderedGames;
    }
}
