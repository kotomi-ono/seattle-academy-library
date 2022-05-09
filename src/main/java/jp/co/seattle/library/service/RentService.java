package jp.co.seattle.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class RentService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍の貸し出し登録する
	 * @param bookId 書籍ID
	 */

	public void rentalBook(int bookId) {

		String sql = "INSERT INTO rentalbooks (bookid) VALUES (?);";

		jdbcTemplate.update(sql, bookId);

	}

	/*
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * 
	 * @return 貸し出し
	 */

	public int rentBook(int bookId) {

		String sql = "SELECT count (*) FROM rentalbooks where bookid = " + bookId;

		int rentalId = jdbcTemplate.queryForObject(sql, int.class);

		return rentalId;

	}

}
