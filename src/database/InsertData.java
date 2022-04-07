package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InsertData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String filepath = "D:\\country.txt";
		String encoding = "utf-8";
		
		List<Country> list = readToList(filepath, encoding);
		//insertData(list);
		readAllCountry();
	}
	
	public static void insertData(List<Country> list) {
		if (list.size() < 1) {
			return;
		}
		
		Connection conn = null;
		int count = 0;
		try {
			conn = DBLinker.getLinker();
			if (conn == null) {
				System.out.println("获取数据库链接失败");
				return;
			}
			
			String sqlselect = "select * from country where countryid = ? and engname = ? and chsname = ?";
			String sqlinsert = "insert into country (countryid, engname, chsname) values (?, ?, ?)";
			for (Country country : list) {
				PreparedStatement stmt1 = conn.prepareStatement(sqlselect);
				stmt1.setString(1, country.id);
				stmt1.setString(2, country.engname);
				stmt1.setString(3, country.chsname);
				ResultSet rs = stmt1.executeQuery();
				if (rs == null || !rs.next()) {
					PreparedStatement stmt = conn.prepareStatement(sqlinsert);
					stmt.setString(1, country.id);
					stmt.setString(2, country.engname);
					stmt.setString(3, country.chsname);
					stmt.execute();
					stmt.close();
					count++;
				}
				stmt1.close();
			}
			
			System.out.println("共写入" + count + "条数据");
		} catch (Exception exp) {
			System.out.println("sql执行失败");
			System.out.println(exp.getMessage());
		} finally {
			try {
				conn.close();
			} catch (Exception exp) {
			}
		}
	}
	
	public static class Country {
		public String id = "";
		public String engname = "";
		public String chsname = "";
		
		public Country(String id, String chsname, String engname) {
			this.id = id;
			this.engname = engname;
			this.chsname = chsname;
		}
	}

	public static List<Country> readToList(String filepath, String encoding) {
		List<Country> result = new ArrayList<Country>();
		InputStreamReader reader = null;
		BufferedReader breader = null;
		
		try {
			File file = new File(filepath);
			if (!file.exists()) {
				return result;
			}
			reader = new InputStreamReader(new FileInputStream(file), encoding);
			breader = new BufferedReader(reader);
			String line = null;
			while ((line = breader.readLine()) != null) {
				String[] info = line.split("-\\+-");
				Country item = new Country(info[0], info[1], info[2]);
				result.add(item);
			}
		} catch (IOException exp) {
			
		} finally {
			try {
				breader.close();
				reader.close();
			} catch (Exception exp) {
				
			}
		}
		return result;
	}
	
	public static void readAllCountry() {
		Connection conn = null;

		try {
			int count = 0;
			conn = DBLinker.getLinker();
			if (conn == null) {
				System.out.println("数据库链接获取失败");
				return;
			}
			String sql = "select * from country";
			PreparedStatement stmt =
				conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery();
			if (rs == null) {
				System.out.println("查询结果集为空");
				return;
			}
			rs.beforeFirst();
			while(rs.next()) {
				String out = rs.getString("countryid") + "\t";
				out += rs.getString("chsname") + "\t";
				out += rs.getString("engname") + "\t";
				System.out.println(out);
				count++;
			}
			
			System.out.println("-----共" + count + "条数据");
		} catch (Exception exp) {
			System.out.println("sql执行失败");
			System.out.println(exp.getMessage());
		} finally {
			try {
				conn.close();
			} catch (Exception exp) {
				
			}
		}
	}
}
