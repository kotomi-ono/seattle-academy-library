package jp.co.seattle.library.controller;


import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/*
 * 編集コントローラー
 */
@Controller // APIの入り口
public class EditBooksController {
	final static Logger logger = LoggerFactory.getLogger(EditBooksController.class);

	// booksService book = new booksService();
	//@Autowired
	// private BooksService booksService;
	 @Autowired
	    private BooksService booksService;

	    @Autowired
	    private ThumbnailService thumbnailService;
	/**
	 * 編集ボタンから編集画面に戻るページ
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editBook", method = RequestMethod.GET)
	public String editBook(Locale locale, Model model,int bookId) {
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		return "edit";
	}

	/*
	 * 対象書籍を編集する
	 * 
	 * 
	 */
	@Transactional
	@RequestMapping(value = "/updateBook", method = RequestMethod.POST)
	public String updateBook(Locale locale, 
			@RequestParam("bookId") int bookId, 
			@RequestParam("title") String title, 
			@RequestParam("author") String author,
			@RequestParam("publisher") String publisher, 
			@RequestParam("publishdate") String publishdate,
			@RequestParam("texts") String texts,
			@RequestParam("isbn") String isbn,
			@RequestParam("thumbnail") MultipartFile file,

			Model model) {
		logger.info("Welcome delete! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
		BookDetailsInfo bookInfo = new BookDetailsInfo();

		bookInfo.setBookId(bookId);
		bookInfo.setTitle(title);
		bookInfo.setAuthor(author);
		bookInfo.setPublisher(publisher);
		bookInfo.setPublishDate(publishdate);
		bookInfo.setTexts(texts);
		bookInfo.setIsbn(isbn);
		
		String thumbnail = file.getOriginalFilename();

        if (!file.isEmpty()) {
            try {
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookDetailsInfo", bookInfo);
                return "edit";
            }
        }
     // 書籍情報を編集し更新する
     		String error = "";

     		if (title.equals("") || author.equals("") || publisher.equals("") || publishdate.equals("")) {
     			error += "必須項目が入力されていません<br>";
     		}

     		if (((!(isbn.length() == 13) && !(isbn.length() == 10) || (!isbn.matches("^[0-9]+$")))) && !(isbn.equals(""))) {
     			error += "SBNの桁数または半角数字が正しくありません。<br>";
     		}

     		if (!publishdate.matches("^[0-9]{4}[0-9]{2}[0-9]{2}$")) {
     			error += "出版日は半角数字のYYYYMMDD形式で入力してください。<br>";
     		}

     		if (!(error.equals(""))) {// もしどれかしらのエラーが発生していたらエラー表示、登録画面に戻る
     			model.addAttribute("error", error);
     			model.addAttribute("bookDetailsInfo", bookInfo);
     			return "edit";

     		}
     		
    		// TODO 登録した書籍の詳細情報を表示するように実装
    		booksService.editBook(bookInfo);
   
    		// 詳細画面に遷移する
    		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
    		return "details";

     		
		
		

	}

}
