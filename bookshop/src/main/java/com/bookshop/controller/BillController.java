package com.bookshop.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.bookshop.dto.BillDTO;
import com.bookshop.model.BillPdf;
import com.bookshop.model.Book;
import com.bookshop.service.BillService;
import com.bookshop.service.BookService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class BillController {

	private static final Logger logger = LoggerFactory.getLogger(BillController.class);
	@Autowired
	BillService billService;

	@Autowired
	BookService bookService;

	@GetMapping("bills/{userId}")
	public String getCustomerBill(@PathVariable("userId") String userId, Model model,HttpSession session) {
		logger.info("Entry point of bills {}", userId);
		try {			
			Integer id = Integer.parseInt(userId);
			List<BillDTO> bills = billService.getBillByCustomerId(id);
			model.addAttribute("bills", bills);
			logger.info("Exit point #1 of bill");
			return "customerbilldetails.html";			
		} catch (Exception e) {
			logger.error("Error while get bills ", e);
			logger.info("Exit point #2 of bills");
			return "error.html";
		}
	}

	@GetMapping("/viewbills/{billNo}")
	public String getBillDetails(@PathVariable("billNo") String billNo, Model model) {
		logger.info("Entry point of view bills {} ", billNo);
		try {			
			Integer bill = Integer.parseInt(billNo);
			List<BillDTO> bills = billService.getBillByBillNo(bill);
			double grandTotal = bills.stream().mapToDouble(BillDTO::getTotalAmount).sum();
			model.addAttribute("bills", bills);
			model.addAttribute("grandTotal", grandTotal);
			logger.info("Exit point #1 of view bills ");
			return "customerviewbill.html";
		} catch (Exception e) {
			logger.error("Error while view bills", e);
			logger.info("Exit point #2 of view bills");
			return "error.html";
		}
	}

	@GetMapping("/allbill")
	public String getAllBills(Model model,HttpSession session) {
		logger.info("Entry point of allbill");
		String rolename = (String) session.getAttribute("rolename");
		
		try {	
			if(rolename.equals("Shopkeeper")) {
			List<BillDTO> bills = billService.getAllBills();
			model.addAttribute("bills", bills);
			logger.info("Exit point #1 of allbill");
			return "allbills.html";
		}else {
				logger.error("Exit point #2 of get bills ");
				return "login.html";
			}
		}catch (Exception e) {
			logger.error("Error while getAllBills ", e);
			logger.info("Exit point #2 of getAllBills");
			return "error.html";
		}
	}

	@GetMapping("/sellerbill/{userId}")
	public String getSeller(@PathVariable("userId") String userId, Model model) {
		logger.info("Entry point of seller bills {}", userId);
		try {		
			Integer id = Integer.parseInt(userId);
			List<BillDTO> bills = billService.getBillBySellerId(id);
			model.addAttribute("bills", bills);
			logger.info("Exit point #1 seller of bill");
			return "allbills.html";
		} catch (Exception e) {
			logger.error("Error while get bills ", e);
			logger.info("Exit point #2 of seller bills");
			return "error.html";
		}
	}

	
	@GetMapping("/newbill")
	public String getNewBill(Model model) {
		logger.info("Entry point of newbill");
		try {			
			List<Book> books = bookService.getAllBooks();
			List<String> uniqueCategories = books.stream().map(Book::getCategory).distinct()
					.collect(Collectors.toList());

			model.addAttribute("categories", uniqueCategories);
			model.addAttribute("books", books);
			logger.info("Exit point #1 of new bill");
			return "newbill.html";
		} catch (Exception e) {
			logger.error("Error while get newBill", e);
			logger.info("Exit point #2 of newBill");
			return "error.html";
		}
	}

	@PostMapping("/addtolist")
	public String addItemsToList(@RequestParam("category") String category, @RequestParam("bookName") String bookName,
			@RequestParam("quantity") String quantity, Model model, HttpSession session) {
		logger.info("Entry point of addtolist , category={}, bookName={}",category,bookName);
		try {
			List<Book> books = bookService.getAllBooks();
			model.addAttribute("books", books);
			int bookId = getIdForBook(bookName, books);
			
			String price = getPriceForBook(bookName, books);			
			Integer quantityNum = Integer.parseInt(quantity);
			Integer totalPrice = Integer.parseInt(price) * Integer.parseInt(quantity);			

			BillDTO selectedItem = new BillDTO(bookName, category, quantityNum, price, totalPrice, bookId);

			List<BillDTO> selectedItemsList = (List<BillDTO>) session.getAttribute("selectedItems");
			if (selectedItemsList == null) {
				selectedItemsList = new ArrayList<>();
			}

			selectedItemsList.add(selectedItem);
			session.setAttribute("selectedItems", selectedItemsList);
			logger.info("Exit point #1 of addtolist");
			return "redirect:/newbill";
		} catch (Exception e) {
			logger.error("Error while addtolist ", e);
			logger.info("Exit point #2 of addtolist");
			return "error.html";
		}
	}

	private String getPriceForBook(String bookName, List<Book> books) {
		for (Book book : books) {
			if (book.getBookName().equals(bookName)) {
				return book.getPrice();
			}
		}
		return "0";
	}

	private int getIdForBook(String bookName, List<Book> books) {
		for (Book book : books) {
			if (book.getBookName().equals(bookName)) {
				return book.getBookId();
			}
		}
		return 0;
	}

	@PostMapping("/removerow")
	public String removeRow(@RequestParam("index") Integer index, HttpSession session) {
        logger.info("Entry point of removerow with index: {}", index);
		try {

	        List<BillDTO> selectedItems = (List<BillDTO>) session.getAttribute("selectedItems");

	        if (selectedItems != null && index.intValue() >= 0 && index.intValue() < selectedItems.size()) {
	            selectedItems.remove(index.intValue());
	            session.setAttribute("selectedItems", selectedItems);
	            
	        }
	        logger.info("Exit point #1 of removerow");
	        return "redirect:/newbill";
	    } catch (Exception e) {
	        logger.error("Error while remove row" , e);
	        logger.info("Exit point #2 of remove row");
	        return "error.html";
	    } 
	}

	@PostMapping("/storebilldata")
	public String storeBill(@RequestParam("customer") Integer customer, HttpServletRequest request, HttpSession session)
			throws DocumentException {
		logger.info("Entry point of storebilldata {}", customer);
		try {
			
			String useridentity = session.getAttribute("userId") + "";			
			Integer sellerId = Integer.parseInt(useridentity);
			try {
				List<BillDTO> selectedItemsList = (List<BillDTO>) request.getSession().getAttribute("selectedItems");
				if (selectedItemsList != null && !selectedItemsList.isEmpty()) {
					insertintoDatabase(customer, sellerId, selectedItemsList);
					byte[] pdfBytes = generatePDF(customer, sellerId, selectedItemsList);
					saveBillToDatabase(customer, pdfBytes);
				}
				request.getSession().removeAttribute("selectedItems");
				logger.info("Exit point #1 of storebilldata");
				return "redirect:/newbill";
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		} catch (Exception e) {
			logger.info("Error while storebilldata ", e);
			logger.info("Exit point #2 of store bill date");
			return "error.html";
		}

	}

	private void insertintoDatabase(Integer customerName, Integer sellerName, List<BillDTO> items) throws SQLException {
		logger.info("Entry point of insertintoDatabase customerName={},sellerName={}", customerName,sellerName);
		try {

			billService.addBill(items, customerName, sellerName);
			logger.info("Entry point #1 of insertintodatabase");
		} catch (Exception e) {
			logger.error("Error while insertinto database");
			logger.info("Exit point #2 of insertinto database");
		}
	}

	private byte[] generatePDF(Integer customerName, Integer sellerName, List<BillDTO> items)
			throws DocumentException, FileNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = new Document();
		logger.info("Entry point of generatePDF customerName={},sellerName={}", customerName, sellerName);
		try {
			
			PdfWriter.getInstance(document, baos);
			document.open();
			document.add(new Paragraph("Bill Details"));
			document.add(new Paragraph("Customer Name: " + customerName));
			document.add(new Paragraph("Seller Name: " + sellerName));

			for (BillDTO item : items) {

				document.add(new Paragraph("Number of books: " + item.getQuantity()));
				document.add(new Paragraph("Total Price: " + item.getTotalPrice()));
			}

			document.close();
			logger.info("Exit point #1 of generatePDF");
		} catch (DocumentException e) {
			logger.error("Error while generatePDF ", e);
			logger.info("Exit point #2 of generatePDF");
		} catch (Exception e) {
			logger.error("Error while generatePDF ", e);
			logger.info("Exit point #3 of generatePDF");
		}
		return baos.toByteArray();
	}

	private void saveBillToDatabase(Integer customer, byte[] pdfBytes) {
		logger.info("Entry point of saveBillToDatabase {}", customer);
		try {

			
			BillPdf billEntity = new BillPdf();
			billEntity.setCustomerName(customer);
			billEntity.setBillPdf(pdfBytes);
			billService.saveBill(billEntity);
			logger.info("Entry point #1 of saveBillIntoDatabase");
		} catch (Exception e) {
			logger.error("Error while saveBillToDatabse", e);
			logger.info("Exit point #2 of saveBillToPDF");
		}
	}

	@GetMapping("/billsort")
	public String getAllBillSortBill( @RequestParam("sortField") String sortField,
            @RequestParam("sortDir") String sortDir,Model model) {
		logger.info("Entry point of  sort bill");

		try {			
			List<BillDTO> bills = billService.getBillSort(sortField,sortDir);
			model.addAttribute("bills", bills);	
						logger.info("Exit point #1 of billsort");
			return "allbills.html";
		} catch (Exception e) {
			logger.error("Error while Billsort ", e);
			logger.info("Exit point #2 of Billsort");
			return "error.html";
		}
	}

	@GetMapping("/searchbill")
	public String getSearchBill( @RequestParam("searchValue") String searchValue,Model model) {
		logger.info("Entry point of  search bill");

		try {			
			List<BillDTO> bills = billService.searchBills(searchValue);
			model.addAttribute("bills", bills);	
			logger.info("Exit point #1 of bill search");
			return "allbills.html";
		} catch (Exception e) {
			logger.error("Error while Bill search ", e);
			logger.info("Exit point #2 of Bill search");
			return "error.html";
		}
	}

}
