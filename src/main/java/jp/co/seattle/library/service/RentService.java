package jp.co.seattle.library.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

import jp.co.seattle.library.dto.LendingHistoryInfo;
import jp.co.seattle.library.rowMapper.LendingHistoryInfoRowMapper;

@Controller
public class RentService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍の貸し出しを新規登録する
	 * 
	 * @param bookId 書籍ID
	 */

	public void rentalBook(LendingHistoryInfo lendingHistoryInfo) {

		String sql = "INSERT INTO rentalbooks (bookid,title,rent_date) VALUES(?,?,now());";
		jdbcTemplate.update(sql, lendingHistoryInfo.getBookId(), lendingHistoryInfo.getTitle());

	}

	/**
	 * 書籍の貸し出し登録を更新する
	 * 
	 * @param bookId 書籍ID
	 */

	public void updaterentalBook(LendingHistoryInfo lendingHistoryInfo) {

		String sql = "update rentalbooks set title = ?,rent_date = now(),return_date =null where bookid = ? ;";
		jdbcTemplate.update(sql, lendingHistoryInfo.getTitle(), lendingHistoryInfo.getBookId());
	}

	/**
	 * 書籍の返却登録する
	 * 
	 * @param bookId 書籍ID
	 */

	public void returnBook(LendingHistoryInfo lendingHistoryInfo) {

		String sql = "update rentalbooks set title = ?,rent_date =null,return_date = now() where bookid = ?;";

		jdbcTemplate.update(sql, lendingHistoryInfo.getTitle(), lendingHistoryInfo.getBookId());
	}

	/*
	 * 書籍IDに紐づく書籍詳細情報を取得する
	 *
	 * @param bookId 書籍ID
	 * 
	 * @return 貸し出し
	 */

	public LendingHistoryInfo rentBook(int bookId) {

		try {
			String sql = "select * from rentalbooks where bookid =" + bookId;

			LendingHistoryInfo rentdate = jdbcTemplate.queryForObject(sql, new LendingHistoryInfoRowMapper());
			return rentdate;

		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 貸出リストを取得する
	 *
	 * @return 書籍リスト
	 */
	public List<LendingHistoryInfo> getrentalList() {

		List<LendingHistoryInfo> getedrentalList = jdbcTemplate.query(
				"select bookid,title,rent_date ,return_date from rentalbooks;", new LendingHistoryInfoRowMapper());

		return getedrentalList;
	}

}
