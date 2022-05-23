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
 * レンタルコントローラー
 */
@Controller // APIの入り口
public class RentBookController {
	final static Logger logger = LoggerFactory.getLogger(RentBookController.class);

	@Autowired
	private RentService rentservice;

	@Autowired
	private BooksService booksService;

	@RequestMapping(value = "/rentBook", method = RequestMethod.POST) // value＝actionで指定したパラメータ
	public String deleteBook(Locale locale, @RequestParam("bookId") Integer bookId, 
			Model model) {

		logger.info("Welcome rent! The client locale is {}.", locale);
		LendingHistoryInfo lendingHistoryInfo = new LendingHistoryInfo();
		lendingHistoryInfo.setBookId(bookId);

		LendingHistoryInfo rentdate = rentservice.rentBook(bookId);
		if (rentdate == null) {
			rentservice.rentalBook(lendingHistoryInfo);

		} else {

			if (rentdate.getRentDate() == null) {
				rentservice.updaterentalBook(lendingHistoryInfo);

			} else {
				model.addAttribute("error", "貸し出し済みです。");
			}
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}

}
