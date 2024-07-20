package com.bookshop.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {

	@Override
	public String toString() {
		return "BillDTO [billNo=" + billNo + ", billingDate=" + billingDate + ", bookId=" + bookId + ", bookName="
				+ bookName + ", authorName=" + authorName + ", category=" + category + ", availableCopies="
				+ availableCopies + ", price=" + price + ", quantity=" + quantity + ", orderCopies=" + orderCopies
				+ ", customerName=" + customerName + ", sellerName=" + sellerName + ", totalAmount=" + totalAmount
				+ ", total=" + total + ", totalPrice=" + totalPrice + "]";
	}

	private int billNo;
	private Date billingDate;
	private int bookId;
	private String bookName;
	private String authorName;
	private String category;
	private int availableCopies;
	private String price;
	private int quantity;
	private int orderCopies;
	private String customerName;
	private String sellerName;
	private Double totalAmount;
	private int total;
	private int totalPrice;

	public BillDTO(String bookName, String category, int quantity, String price, int totalPrice, int bookId) {
		this.bookId=bookId;
		this.bookName=bookName;
		this.category=category;
		this.quantity=quantity;
		this.price=price;
		this.totalPrice=totalPrice;
	}
}
