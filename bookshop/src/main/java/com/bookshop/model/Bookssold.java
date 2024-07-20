package com.bookshop.model;



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
@Table(name="bookssold")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bookssold {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sl_no")
	private Integer slNo;
	
	@Column(name="book_id")
	private Integer bookId;
	
	@Column(name="order_copies")
	private Integer orderCopies;
	
	@Column(name="bill_no")
	private Integer billNo;
}
