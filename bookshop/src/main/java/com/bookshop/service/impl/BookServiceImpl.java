package com.bookshop.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookshop.model.Book;
import com.bookshop.repository.BookRepository;
import com.bookshop.service.BookService;

import jakarta.transaction.Transactional;

@Service
public class BookServiceImpl implements BookService{
	
	private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);
	
	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public List<Book> getAllBooks(){
		logger.info("Entry point of get All Books");
		try {
			logger.info("Exit point #1 of get All Books");
		return bookRepository.findAllActiveBooks();
	}catch(Exception e) {
		logger.error("Error while getAllBooks",e);
		logger.info("Exit point #2 of get All Books");
		return null;
	}
	}
	@Override
	public Optional<Book> getBook(int bookId) {
		logger.info("Entry point of get Book");
		try {
			logger.info("Exit point #1 of getBook");
		return bookRepository.findById(bookId);
		}catch(Exception e) {
			logger.error("Error while getBook",e);
			logger.info("Exit point #2 of get Book");
			return null;
		}
	}
	
	@Override
	public void save(Book book) {
			logger.info("Entry point of save book");
			try {
				logger.info("Exit point #1 of save book");
				bookRepository.save(book);
	    }catch(Exception e) {
	    	logger.error("Error while save book",e);
	    	logger.info("Exit point #2 of save book");
	    }
	}
	
	@Override
	@Transactional
	public void softDeleteById(Integer bookId) {
		logger.info("Entry point of softdeletebybookid");
		try {
		logger.info("Exit point #1 of softdeletebybookid");
		bookRepository.softDeleteById(bookId);		
	}catch(Exception e) {		
		logger.error("Error while softdeletebybookid",e);
		logger.info("Exit point #2 of softdeletebybookid");
	}
}
	}