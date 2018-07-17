package MP3;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnect {
	private Connection con;
	private Statement st;
	
	
	public DBConnect() {
		
	}
	
	public Connection connectDB() {
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mp3_list?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			st = con.createStatement();
		
		
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		return con;
	}
	
	public Statement listConnect() {
		
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mp3_list?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
			st = con.createStatement();
		
		
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
		return st;
	}
}