package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
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

@Controller // APIの入り口
public class BulkregistController {
	final static Logger logger = LoggerFactory.getLogger(EditBooksController.class);
	@Autowired
	private BooksService booksService;

	/**
	 * 一括登録ボタンから一括登録ページへ遷移
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bulkregist", method = RequestMethod.GET)
	public String test(Locale locale) {
		return "bulkregist";
	}

	/*
	 * 書籍を一括登録する
	 * 
	 * 
	 */
	@Transactional
	@RequestMapping(value = "/bulkregistraction", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String bulkregistraction(Locale locale, @RequestParam("upload_file") MultipartFile uploadFile, Model model) {
		logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

		// パラメータで受け取った書籍情報をDtoに格納する。
		BookDetailsInfo bookInfo = new BookDetailsInfo();

		String line = null;
		boolean bline = true;
		int count = 0;

		List<String[]> bookslist = new ArrayList<String[]>();
		List<String> errorlist = new ArrayList<String>();

		try {
			InputStream stream = uploadFile.getInputStream();
			Reader reader = new InputStreamReader(stream);
			BufferedReader buf = new BufferedReader(reader);

			if (!buf.ready()) { // CSVファイルにデータがあるか if（空だったら）
				model.addAttribute("errorMessage", "csvに書籍情報がありません");
				return "bulkregist";
			}

			while ((line = buf.readLine()) != null) { // １つずつデータを読み込む
				count++;
				final String[] split = line.split(",", -1);
				for (int i = 0; i < split.length; i++) {
				}

				// バリデーションチェック
				String error = booksService.validationcheck(split[0], split[1], split[2], split[3], split[4], model);

				// 書籍情報をセットする
				bookslist.add(split);

				// もしどれかしらのエラーが発生していたらエラー表示、一括登録画面に戻る
				if (!(error.equals(""))) {
					errorlist.add(count + "行目の書籍登録でエラーが起きました。<br>");
				}
			}
			if (errorlist.size() > 0) {
				model.addAttribute("errorMessage", errorlist);
				return "bulkregist";
			}
		} catch (IOException e) {
			throw new RuntimeException("ファイルが読み込めません", e);
		}

		// 書籍を登録する（１つずつ
		for (int i = 0; i < bookslist.size(); i++) {
			String[] booklist = bookslist.get(i);
			bookInfo.setTitle(booklist[0]);
			bookInfo.setAuthor(booklist[1]);
			bookInfo.setPublisher(booklist[2]);
			bookInfo.setPublishDate(booklist[3]);
			bookInfo.setIsbn(booklist[4]);
			bookInfo.setTexts(booklist[5]);
			model.addAttribute("bookDetailsInfo", booklist);
			booksService.registBook(bookInfo);
		}
		return "redirect:/home";
	}
}