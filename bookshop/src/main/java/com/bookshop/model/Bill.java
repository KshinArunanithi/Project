package com.bookshop.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="bill")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_no")
	private Integer billNo;
	
	@Column(name="billing_date")
	private Date billingDate;
	
	@Column(name="customer_id")
	private Integer customerId;
	
	@Column(name="seller_id")
	private Integer sellerId;
}
