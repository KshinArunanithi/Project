package com.bookshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookshop.model.Book;

import jakarta.transaction.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer>{
	
	  @Transactional
	    @Modifying
	    @Query("UPDATE Book b SET b.deleted = true WHERE b.id = :bookId")
	    void softDeleteById(@Param("bookId") Integer bookId);
	  
	  @Query("SELECT b FROM Book b WHERE b.deleted = false")
	  List<Book> findAllActiveBooks();
}