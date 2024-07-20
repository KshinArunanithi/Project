package com.bookshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookshop.dto.BillDTO;
import com.bookshop.model.BillPdf;

@Service
public interface BillService {
	
	public List<BillDTO> getBillByCustomerId(Integer userId);
	public List<BillDTO> getBillByBillNo(Integer billNo);
	public List<BillDTO> getAllBills();
	public void addBill(List<BillDTO> books, int customerName, int sellerName) ;
	public void saveBill(BillPdf billEntity);
	public List<BillDTO> getBillBySellerId(Integer id);
	public List<BillDTO> getBillSort(String sortField, String sortDir);
	public List<BillDTO> searchBills(String searchValue);
	
}
