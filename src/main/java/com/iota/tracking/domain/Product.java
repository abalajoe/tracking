package com.iota.tracking.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.FetchType.EAGER;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "product_tbl")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int quantity;
    private String recipient;
    private String deliveryaddress;
    private int cost;
    private String custodian;
    private String location;
    @JsonFormat(pattern="dd MMMM yyyy")
    private Timestamp createddate;
    @JsonFormat(pattern="dd MMMM yyyy")
    private Timestamp updateddate;
    @ManyToOne(cascade = CascadeType.ALL, fetch = EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "recipient", referencedColumnName = "email", insertable = false, updatable = false)
    private User user;
}
