package com.bookshop.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.bookshop.model.Book;
import com.bookshop.service.BookService;

@Controller
public class BookController {

	private static final Logger logger = LoggerFactory.getLogger(BookController.class);

	@Autowired
	private BookService bookService;

	@GetMapping("/books")
	public String getAllBook(Model model) {
		logger.info("Entry point of getAllBook");
		try {
			
			List<Book> books = bookService.getAllBooks();
			model.addAttribute("books", books);
			logger.info("Exit point #1 of get Allbook");
			return "customerbookdetails.html";
		} catch (Exception e) {
			logger.error("Error while getAllBook", e);
			logger.info("Exit point #2 of getAllBook");
			return "error.html";
		}
	}

	@GetMapping("/allbook")
	public String getAllBooks(Model model) {
		logger.info("Entry point of getAllBooks");
		try {
		
		List<Book> books = bookService.getAllBooks();
		model.addAttribute("books",books);
		logger.info("Exit point #1 of getAllBooks");
		return "allbooks.html";
		}catch(Exception e) {
			logger.error("Error while getAllBooks");
			logger.info("Exit point #2 of getAllBooks");
			return "error.html";
		}
	}

	@GetMapping("/getbook/{bookId}")
	public String editBook(@PathVariable Integer bookId, Model model) {
		logger.info("Entry point of getBook {}", bookId);
		try {		
		Optional<Book> books = bookService.getBook(bookId);
		if (books.isPresent()) {
			model.addAttribute("books", books.get());
			logger.info("Exit point #1 of editbook");
			return "editbook.html";
		} else {
			return "error.html";
		}
	}catch(Exception e) {		
		logger.error("Error while getBook " ,e);
		logger.info("Exit point #2 of getBook");
		return "error.html";
	}
	}
	
	@PostMapping("/editbook")
	public String editBook(@ModelAttribute("book") Book book) {
		logger.info("Entry point of edit book");
		try {				
		bookService.save(book);
		logger.info("Exit point #1 of edit book");
		return "redirect:/allbook";
	}catch(Exception e) {
		logger.error("Error while editbook ",e );
		logger.info("Exit point #2 of editbook");
		return "error.html";
	}
	}

	@GetMapping("/addbook")
	
	public String addBook(Model model) {
		logger.info("Entry point of addbook");
		try {		
		model.addAttribute("book", new Book());
		logger.info("Exit point #1 of addbook");
		return "addbook.html";
	}catch(Exception e) {
		logger.error("Error while addbook ",e);
		logger.info("Exit point #2 of addbook");
		return "error.html";
	}
		
	}
	@PostMapping("/addbook")
	public String addBook(@ModelAttribute("book") Book book) {
		logger.info("Entry point of addbook redirect");
		try {		
		bookService.save(book);
		logger.info("Exit point #1 of add book redirect");
		return "redirect:/allbook";
	}catch(Exception e) {
		logger.error("Error while addbook redirect",e);
		logger.info("Exit point #2 of addbook redirect");
		return "error.html";
	}
	}
	
	@GetMapping("/deletebook/{bookId}")
	public String deleteBook(@PathVariable("bookId") Integer bookId) {
		logger.info("Entry point of delete book {}",bookId);
		try {		
		System.out.println("BookId" + bookId);
		bookService.softDeleteById(bookId);
		logger.info("Exit point #1 of bookId");
		return "redirect:/allbook";
	}catch(Exception e) {
		logger.error("Error while deletebook ", e);
		logger.info("Exit point #2 of delete book");
		return "error.html";
	}
}
}