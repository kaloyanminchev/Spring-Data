package demos.springdata.advanced.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "ingredients")
public class Ingredient extends BaseEntity {
    private String name;
    private BigDecimal price;
    private Set<Shampoo> shampoos;

    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @ManyToMany(mappedBy = "ingredients", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public Set<Shampoo> getShampoos() {
        return shampoos;
    }

    public void setShampoos(Set<Shampoo> shampoos) {
        this.shampoos = shampoos;
    }
}
