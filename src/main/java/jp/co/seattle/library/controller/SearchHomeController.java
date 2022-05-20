package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
@Controller //APIの入り口
public class SearchHomeController {
	final static Logger logger = LoggerFactory.getLogger(SearchHomeController.class);

    @Autowired
    private BooksService booksService;

    /**
     * 検索ボタンからホーム画面に戻るページ
     * @param model
     * @return
     */
    @RequestMapping(value = "/searchBook", method = RequestMethod.GET)
    public String serchHome(
    		@RequestParam("input") String input,
    		@RequestParam("searchtype") String searchtype,
    		Model model) {
    	
    	model.addAttribute("bookList", booksService.getsearchBookList(input, searchtype));
        return "home";
    }

}
