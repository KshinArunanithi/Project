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
@Table(name="books")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="book_id")
	private Integer bookId;
	
	@Column(name="book_name")
	private String bookName;
	
	@Column(name="author_name")
	private String authorName;
	
	@Column(name="category")
	private String category;
	
	@Column(name="available_copies")
	private Integer availableCopies;
	
	@Column(name="price")
	private String price;

	 @Column(name = "deleted", nullable = false)
	 private boolean deleted = false;
}
