package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.dto.LendingHistoryInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentService;

/*
 * * リターンコントローラー
 */

@Controller // APIの入り口

public class ReturnBookController {

	final static Logger logger = LoggerFactory.getLogger(RentBookController.class);

	@Autowired
	private RentService rentservice;

	@Autowired
	private BooksService booksService;

	@RequestMapping(value = "/returnBook", method = RequestMethod.POST) // value＝actionで指定したパラメータ
	public String deleteBook(Locale locale, @RequestParam("bookId") Integer bookId, @RequestParam("title") String title,
			Model model) {
		logger.info("Welcome rent! The client locale is {}.", locale);
		LendingHistoryInfo lendingHistoryInfo = new LendingHistoryInfo();
		lendingHistoryInfo.setBookId(bookId);

		LendingHistoryInfo rentdate = rentservice.rentBook(bookId);
		if ((rentdate == null)||(rentdate.getRentDate() == null)) {
			model.addAttribute("error", "貸出されていません。");
		} else {
			rentservice.returnBook(lendingHistoryInfo);
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}
}
