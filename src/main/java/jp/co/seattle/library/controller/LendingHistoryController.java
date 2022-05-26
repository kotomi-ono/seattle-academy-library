package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.RentService;

@Controller //APIの入り口
public class LendingHistoryController {
		final static Logger logger = LoggerFactory.getLogger(HomeController.class);
		
		@Autowired
		private RentService rentservice;
		/**
	     * 貸出履歴ボタンから貸出履歴画面へ遷移
	     * @param model
	     * @return
	     */
	    @RequestMapping(value = "/history", method = RequestMethod.GET)
	    public String LendingHistory(Model model) {				
	        model.addAttribute("rentalList", rentservice.getrentalList());

	        return "history";
	    }

}
