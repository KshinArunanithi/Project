package com.bookshop.repository;

import java.util.List;

public interface SortRepository {
    List<Object[]> findBillSort(String query);
}
