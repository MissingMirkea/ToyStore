package md.ceiti.ms.hibernate.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ToysOutOfStock", schema = "public")

public class ToysOutOfStock  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "Country", nullable = false, length = 3)
    private String country;

    @Column(name = "ToyType", nullable = false, length = 100)
    private String toyType;

    @Column(name = "Age", nullable = false)
    private int age;

    @Column(name = "cantity",nullable = false)
    private int cantity;

    @Column(name = "Price", nullable = false)
    private float price;

    public ToysOutOfStock(String name, String country, String toyType, int age,int cantity, float price) {
        this.name = name;
        this.country = country;
        this.toyType = toyType;
        this.age = age;
        this.cantity = cantity;
        this.price = price;
    }


}