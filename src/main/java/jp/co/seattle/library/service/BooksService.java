package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"select id,title,author,publisher,publish_date,thumbnail_url from books order by title;",
				new BookInfoRowMapper());

		return getedBookList;
	}

	/**
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * @return 書籍情報
	 */
	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "select *,case when rentalbooks.id isnull  then '貸し出し可'else '貸し出し中'end as status from books left join rentalbooks on books.id = rentalbooks.bookid where books.id ="
				+ bookId;
		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}

	/**
	 * 新規登録した書籍の情報を取得する
	 */
	public BookDetailsInfo getnewBookInfo() {

		// JSPに渡すデータを設定する
		String sql = "SELECT *,case when rentalbooks.id isnull  then '貸し出し可'else '貸し出し中'end as status FROM books left join rentalbooks on books.id = rentalbooks.bookid where books.id =(select max(id) from books);";

		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

		return bookDetailsInfo;
	}

	/**
	 * 書籍を登録する
	 *
	 * @param bookInfo 書籍情報
	 */
	public void registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher,publish_date,thumbnail_name,thumbnail_url,reg_date,upd_date,texts,isbn) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getPublishDate() + "','" + bookInfo.getThumbnailName() + "','" + bookInfo.getThumbnailUrl()
				+ "','" + "now()," + "','" + "now()," + "','" + bookInfo.getTexts() + "','" + bookInfo.getIsbn()
				+ "');";

		jdbcTemplate.update(sql);
	}

	/*
	 * 書籍情報のバリデーションチェック
	 * 
	 * 
	 */
	public String validationcheck(String title, String author, String publisher, String publishdate, String isbn,
			Model model) {
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
		return error;

	}

	/**
	 * 書籍を削除する
	 * 
	 * @param bookId 書籍
	 */
	public void deleteBook(Integer bookId) {

		String sql = "delete from books where id = '" + bookId + "';";
		jdbcTemplate.update(sql);
	}

	/*
	 * 書籍を編集する
	 * 
	 */

	public void editBook(BookDetailsInfo bookInfo) {

		String sql;
		if (bookInfo.getThumbnailUrl() == null) {

			sql = "update books set title = '" + bookInfo.getTitle() + "',author ='" + bookInfo.getAuthor()
					+ "',publisher = '" + bookInfo.getPublisher() + "',publish_date = '" + bookInfo.getPublishDate()
					+ "',upd_date = 'now()" + "',texts ='" + bookInfo.getTexts() + "',isbn ='" + bookInfo.getIsbn()
					+ "'where  id = " + bookInfo.getBookId() + ";";

		} else {

			sql = "update books set title = '" + bookInfo.getTitle() + "',author ='" + bookInfo.getAuthor()
					+ "',publisher = '" + bookInfo.getPublisher() + "',publish_date = '" + bookInfo.getPublishDate()
					+ "',thumbnail_url = '" + bookInfo.getThumbnailUrl() + "',thumbnail_name = '"
					+ bookInfo.getThumbnailName() + "',upd_date = 'now()" + "',texts ='" + bookInfo.getTexts()
					+ "',isbn ='" + bookInfo.getIsbn() + "'where  id = " + bookInfo.getBookId() + ";";

		}
		jdbcTemplate.update(sql);
	}

}
