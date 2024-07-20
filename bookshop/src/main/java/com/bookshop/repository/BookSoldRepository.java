package com.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookshop.model.Bookssold;

public interface BookSoldRepository extends JpaRepository<Bookssold, Integer> {
}
