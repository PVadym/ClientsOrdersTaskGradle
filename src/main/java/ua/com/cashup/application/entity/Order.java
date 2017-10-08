package ua.com.cashup.application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.com.cashup.application.enums.Currency;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by Вадим on 04.10.2017.
 */
@Entity
@Table(name = "orders")
@XmlRootElement
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date createDate;

    @Column
    private String status;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column
    private boolean confirmation;


    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    public Order(Date createDate, String status, double amount, Currency currency, boolean confirmation) {
        this.createDate = createDate;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.confirmation = confirmation;
    }

    public Order() {
        this.confirmation = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", confirmation=" + confirmation +
                '}';
    }
}
