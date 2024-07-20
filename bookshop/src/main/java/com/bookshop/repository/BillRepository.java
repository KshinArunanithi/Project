package com.bookshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bookshop.model.Bill;
import com.bookshop.model.BillPdf;
import com.bookshop.model.User;

@Repository
public interface BillRepository extends JpaRepository<User,Integer>,SortRepository{
	
	@Query(value="SELECT "
			+ "    b.bill_no,"
			+ "    u.username AS customerName,"
			+ "    s.username AS sellerName,"
			+ "    SUM(bk.price * bs.order_copies) AS totalAmount "
			+ "FROM "
			+ "    bill b "
			+ "        JOIN "
			+ "    users u ON b.customer_id = u.user_id "
			+ "        JOIN "
			+ "    users s ON b.seller_id = s.user_id "
			+ "        JOIN "
			+ "    bookssold bs ON b.bill_no = bs.bill_no "
			+ "        JOIN "
			+ "    books bk ON bs.book_id = bk.book_id "
			+ "WHERE"
			+ "    u.user_id = :userId "
			+ "GROUP BY b.bill_no , u.username , s.username "
			+ "ORDER BY b.bill_no",nativeQuery = true)
	

	List<Object[]> findBillsByCustomerId(@Param("userId") Integer userId);
	
	@Query (value = 
			 "	SELECT  "
			+ "b.bill_no,  "
			+ "u1.username AS customerName,  "
			+ "u2.username AS sellerName,  "
			+ "b.billing_date,  "
			+ "				   bk.book_name,  "
			+ "				   bs.order_copies,  "
			+ "				   bk.price AS book_price,  "
			+ "				   (bs.order_copies * bk.price) AS totalAmount  "
			+ "				   FROM  "
			+ "				   bill b  "
			+ "				   JOIN  "
			+ "				   users u1 ON b.customer_id = u1.user_id  "
			+ "				   JOIN  "
			+ "				   users u2 ON b.seller_id = u2.user_id  "
			+ "				   JOIN  "
			+ "				   bookssold bs ON b.bill_no = bs.bill_no  "
			+ "				   JOIN  "
			+ "				   books bk ON bs.book_id = bk.book_id  "
			+ "				   WHERE  "
			+ "				   b.bill_no = :billNo " , nativeQuery = true)
	List<Object[]> findBillByBillNo(int billNo);

	@Query (value = "SELECT "
			+ "    b.bill_no,"
			+ "    u.username AS customer_name,"
			+ "    s.username AS seller_name,"
			+ "    SUM(bk.price * bs.order_copies) AS total_amount "
			+ "FROM "
			+ "    bill b "
			+ "        JOIN "
			+ "    users u ON b.customer_id = u.user_id "
			+ "        JOIN "
			+ "    users s ON b.seller_id = s.user_id "
			+ "        JOIN "
			+ "    bookssold bs ON b.bill_no = bs.bill_no "
			+ "        JOIN "
			+ "    books bk ON bs.book_id = bk.book_id "
			+ "GROUP BY b.bill_no , u.username , s.username "
			+ "ORDER BY b.bill_no " , nativeQuery = true
			)
	List<Object[]> findAllBills();
	
	Bill save(Bill bill);

	BillPdf save(BillPdf billEntity);

	@Query(value="SELECT "
			+ "			    b.bill_no,"
			+ "			    u.username AS customerName,"
			+ "			    s.username AS sellerName,"
			+ "			    SUM(bk.price * bs.order_copies) AS totalAmount "
			+ "			FROM "
			+ "			    bill b "
			+ "			        JOIN "
			+ "			    users u ON b.customer_id = u.user_id "
			+ "			        JOIN "
			+ "			    users s ON b.seller_id = s.user_id "
			+ "			        JOIN "
			+ "			    bookssold bs ON b.bill_no = bs.bill_no "
			+ "			        JOIN "
			+ "			    books bk ON bs.book_id = bk.book_id "
			+ "			WHERE"
			+ "			    s.user_id = :userId"
			+ "			GROUP BY b.bill_no , u.username , s.username "
			+ "			ORDER BY b.bill_no",nativeQuery = true)
	List<Object[]> findBillBySellerId(Integer userId);
	

    @Query(value = "SELECT "
            + "    b.bill_no, "
            + "    u.username AS customer_name, "
            + "    s.username AS seller_name, "
            + "    SUM(bk.price * bs.order_copies) AS total_amount "
            + "FROM "
            + "    bill b "
            + "        JOIN "
            + "    users u ON b.customer_id = u.user_id "
            + "        JOIN "
            + "    users s ON b.seller_id = s.user_id "
            + "        JOIN "
            + "    bookssold bs ON b.bill_no = bs.bill_no "
            + "        JOIN "
            + "    books bk ON bs.book_id = bk.book_id "
            + "GROUP BY b.bill_no, u.username, s.username "
            + "ORDER BY B.bill_no desc",
      nativeQuery = true)
    List<Object[]> findBillSort(String sortField, String sortDir);
    
    @Query(value = "SELECT "
            + "    b.bill_no, "
            + "    u.username AS customerName, "
            + "    s.username AS sellerName, "
            + "    SUM(bk.price * bs.order_copies) AS totalAmount "
            + "FROM "
            + "    bill b "
            + "    JOIN users u ON b.customer_id = u.user_id "
            + "    JOIN users s ON b.seller_id = s.user_id "
            + "    JOIN bookssold bs ON b.bill_no = bs.bill_no "
            + "    JOIN books bk ON bs.book_id = bk.book_id "
            + "GROUP BY b.bill_no, u.username, s.username "
            + "HAVING u.username LIKE %:searchValue% "
            + "    OR b.bill_no LIKE %:searchValue% "
            + "    OR s.username LIKE %:searchValue% "
            + "    OR SUM(bk.price * bs.order_copies) LIKE %:searchValue% "
            + "ORDER BY b.bill_no", nativeQuery = true)
    List<Object[]> findAllBills(String searchValue);
	
}
