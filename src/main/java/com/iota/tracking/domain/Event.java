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
@Table(name = "event_tbl")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int productfk;
    private String custodian;
    private String location;
    @JsonFormat(pattern="dd MMMM yyyy")
    private Timestamp createddate;
    @ManyToOne(cascade = CascadeType.ALL, fetch = EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "productfk", referencedColumnName = "id", insertable = false, updatable = false)
    private Product product;
}
