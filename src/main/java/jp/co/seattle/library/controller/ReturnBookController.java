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
	public String deleteBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
		logger.info("Welcome rent! The client locale is {}.", locale);
		int rentalId = rentservice.rentBook(bookId);

		if (rentalId == 0) {
			model.addAttribute("error", "貸出されていません。");
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		} else {
			rentservice.returnBook(bookId);
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		}

		return "details";
	}
}
