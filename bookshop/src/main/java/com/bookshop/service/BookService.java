package com.bookshop.service;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bookshop.model.Book;

@Service
public interface BookService{
	
	public List<Book> getAllBooks();
	public Optional<Book> getBook(int bookId);
	public void save(Book book);
	public void softDeleteById(Integer bookId);
}