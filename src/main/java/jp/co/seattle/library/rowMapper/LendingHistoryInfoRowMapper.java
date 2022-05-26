package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.LendingHistoryInfo;

@Configuration
public class LendingHistoryInfoRowMapper implements RowMapper<LendingHistoryInfo> {

	@Override
	public LendingHistoryInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		// Query結果（ResultSet rs）を、オブジェクトに格納する実装
		LendingHistoryInfo lendingHistoryInfo = new LendingHistoryInfo();

		// bookInfoの項目と、取得した結果(rs)のカラムをマッピングする
		lendingHistoryInfo.setBookId(rs.getInt("bookid"));
		lendingHistoryInfo.setTitle(rs.getString("title"));
		lendingHistoryInfo.setRentDate(rs.getString("rent_date"));
		lendingHistoryInfo.setReturnDate(rs.getString("return_date"));

		return lendingHistoryInfo;
	}

}
