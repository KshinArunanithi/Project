package com.bookshop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.bookshop.dto.BillDTO;
import com.bookshop.model.Bill;
import com.bookshop.model.BillPdf;
import com.bookshop.model.Bookssold;
import com.bookshop.repository.BillRepository;
import com.bookshop.repository.BookSoldRepository;
import com.bookshop.service.BillService;

import jakarta.transaction.Transactional;

@Service
public class BillServiceImpl implements BillService{

	private static final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);
	
	@Autowired
	BillRepository billRepository;

	@Autowired
	BookSoldRepository bookSoldRepository;
	
	@Override
	public List<BillDTO> getBillByCustomerId(Integer userId) {
		logger.info("Entry point of getBillByCustomerId {}", userId);		
		try {		
		List<Object[]> resultList = billRepository.findBillsByCustomerId(userId);
		
		List<BillDTO> billDTOList = new ArrayList<BillDTO>();
		for(Object[] result : resultList) {
			BillDTO billDTO = new BillDTO();
		    billDTO.setBillNo((Integer) result[0]);
		    billDTO.setCustomerName((String) result[1]);
		    billDTO.setSellerName((String) result[2]);
		    billDTO.setTotalAmount((Double) result[3]);
		    billDTOList.add(billDTO);
		}
		
		logger.info("Exit point #1 of getBillByCustomerId");
		return billDTOList;
	}catch(Exception e) {
		logger.error("Error while getBillByCustomerId",e );
		logger.info("Exit point #2 of getBillByCustomerId");
		return null;
	}
	}
	@Override
	public List<BillDTO> getBillByBillNo(Integer billNo) {
		logger.info("Entry point of getBillByBillNo");
		try {
			
		List<Object[]> resultList = billRepository.findBillByBillNo(billNo);
		
		List<BillDTO> billDTOList = new ArrayList<BillDTO>();
		for(Object[] result : resultList) {
			BillDTO billDTO = new BillDTO();
		    billDTO.setBillNo((Integer) result[0]);
		    billDTO.setCustomerName((String) result[1]);
		    billDTO.setSellerName((String) result[2]);
		    billDTO.setBillingDate((Date) result[3]);
		    billDTO.setBookName((String) result[4]);
		    billDTO.setOrderCopies((Integer) result[5]);
		    billDTO.setPrice((String) result[6]);
		    billDTO.setTotalAmount((Double) result[7]);
		    billDTOList.add(billDTO);
		}

		logger.info("Exit point #1 of getBillByBillNo");
		return billDTOList;
	}catch(Exception e) {
		logger.error("Error while getBillByBillNo",e);
		logger.info("Exit point #2 of getBillByBillNo");
		return null;
	}	
}
	@Override
	public List<BillDTO> getBillBySellerId(Integer userId) {
		logger.info("Entry point of getBillBySellerId");
		try {
		List<Object[]> resultList = billRepository.findBillBySellerId(userId);
		
		List<BillDTO> billDTOList = new ArrayList<BillDTO>();
		
		for(Object[] result:resultList) {
			BillDTO billDTO = new BillDTO();
		    billDTO.setBillNo((Integer) result[0]);
		    billDTO.setCustomerName((String) result[1]);
		    billDTO.setSellerName((String) result[2]);		 
		    billDTO.setTotalAmount((Double) result[3]);
		    billDTOList.add(billDTO);
		}
		logger.info("Exit point #1 of getBillBySellerId");
		return billDTOList;
	}catch(Exception e) {
		logger.error("Error while getBillBySellerId",e);
		logger.info("Exit point #2 of getBillBySellerId");
		return null;
	}
		
	}
	
	@Override
	public List<BillDTO> getAllBills() {
		logger.info("Entry point of getAllBills");
		try {
		List<Object[]> resultList = billRepository.findAllBills();
		
		List<BillDTO> billDTOList = new ArrayList<BillDTO>();
		
		for(Object[] result:resultList) {
			BillDTO billDTO = new BillDTO();
		    billDTO.setBillNo((Integer) result[0]);
		    billDTO.setCustomerName((String) result[1]);
		    billDTO.setSellerName((String) result[2]);		 
		    billDTO.setTotalAmount((Double) result[3]);
		    billDTOList.add(billDTO);
		}
		logger.info("Exit ppint #1 of getAllBills");
		return billDTOList;
	}catch(Exception e) {
		logger.error("Error while getAllBills",e);
		logger.info("Exit point #2 of getAllBills");
		return null;
	}
		
	}
		@Override
		@Transactional
		public void addBill(List<BillDTO> books, int customerName, int sellerName) {
			logger.info("Entry point of addBill");
			try {
		        Bill bill = new Bill();
		        bill.setCustomerId(customerName);
		        bill.setSellerId(sellerName);
		        bill.setBillingDate(new Date());

		        Bill savedBill = billRepository.save(bill);

		        for (BillDTO book : books) {
		        	
		        	Bookssold bookSold = new Bookssold();
		        	int billNo = savedBill.getBillNo();		          
		            bookSold.setBillNo(billNo);
		            bookSold.setBookId(book.getBookId());
		            bookSold.setOrderCopies(book.getQuantity());
		            logger.info("Exit point #1 of addBill");
		            bookSoldRepository.save(bookSold);
		        }
	}catch(Exception e) {
		logger.error("Error while addBill ",e);
		logger.info("Exit point #2 of add Bill");
	}
}
		@Override
		public void saveBill(BillPdf billEntity) {
			logger.info("Entry point of saveBill");
			try {
				logger.info("Exit point #1 of saveBill");
			   billRepository.save(billEntity);
			}catch(Exception e) {
				logger.error("Error while saveBill",e);
				logger.info("Exit point #2 of save bill");
			}
		}
		
		@Override
	    public List<BillDTO> getBillSort(String sortField, String sortDir) {
	        logger.info("Entry point of getAllBills sort");
	        logger.info("Sorting " + sortField + " " + sortDir);

	        String baseQuery = "SELECT b.bill_no, u.username AS customername, s.username AS sellername, " +
	                           "SUM(bk.price * bs.order_copies) AS totalamount " +
	                           "FROM bill b " +
	                           "JOIN users u ON b.customer_id = u.user_id " +
	                           "JOIN users s ON b.seller_id = s.user_id " +
	                           "JOIN bookssold bs ON b.bill_no = bs.bill_no " +
	                           "JOIN books bk ON bs.book_id = bk.book_id " +
	                           "GROUP BY b.bill_no, u.username, s.username ";

	        String orderByClause = "ORDER BY " + sortField + " " + sortDir;
	        String finalQuery = baseQuery + orderByClause;

	        try {
	            List<Object[]> resultList = billRepository.findBillSort(finalQuery);

	            List<BillDTO> billDTOList = new ArrayList<>();

	            for (Object[] result : resultList) {
	                BillDTO billDTO = new BillDTO();
	                billDTO.setBillNo((Integer) result[0]);
	                billDTO.setCustomerName((String) result[1]);
	                billDTO.setSellerName((String) result[2]);
	                billDTO.setTotalAmount((Double) result[3]);
	                billDTOList.add(billDTO);
	            }

	            logger.info("Exit point #1 of getAllBills sort");
	            return billDTOList;
	        } catch (Exception e) {
	            logger.error("Error while getAllBills sort", e);
	            logger.info("Exit point #2 of getAllBills sort");
	            return null;
	        }
	    }
		
		@Override
		public List<BillDTO> searchBills(String searchValue) {
			logger.info("Entry point of getAllBills");
			try {
			List<Object[]> resultList = billRepository.findAllBills(searchValue);
			
			List<BillDTO> billDTOList = new ArrayList<BillDTO>();
			
			for(Object[] result:resultList) {
				BillDTO billDTO = new BillDTO();
			    billDTO.setBillNo((Integer) result[0]);
			    billDTO.setCustomerName((String) result[1]);
			    billDTO.setSellerName((String) result[2]);		 
			    billDTO.setTotalAmount((Double) result[3]);
			    billDTOList.add(billDTO);
			}
			logger.info("Exit ppint #1 of getAllBills");
			return billDTOList;
		}catch(Exception e) {
			logger.error("Error while getAllBills",e);
			logger.info("Exit point #2 of getAllBills");
			return null;
		}
			
		}
}
