package com.bookshop.repository;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

public class SortRepositoryImpl implements SortRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Object[]> findBillSort(String query) {
        Query nativeQuery = entityManager.createNativeQuery(query);
        return nativeQuery.getResultList();
    }
}
