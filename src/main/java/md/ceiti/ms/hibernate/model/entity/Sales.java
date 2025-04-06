package md.ceiti.ms.hibernate.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Sales", schema = "public")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ToysName", nullable = false)
    private String toysName;

    @Column(name = "Cantity", nullable = false)
    private int cantity;

    @Column(name = "Transaction_date", nullable = false)
    private Timestamp transaction_date;

    @Column(name = "Total", nullable = false)
    private float total;

    public Sales(String toysName, int cantity, Timestamp transaction_date, float total) {
        this.toysName = toysName;
        this.cantity = cantity;
        this.transaction_date = transaction_date;
        this.total = total;
    }
}
