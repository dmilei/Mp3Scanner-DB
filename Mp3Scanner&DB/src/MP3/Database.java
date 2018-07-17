package MP3;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Database {
	static DBConnect connect = new DBConnect();
	static Connection con = connect.connectDB();
	static ResultSet rs;
	
	public String storeMetadata(String filepath) throws IOException, SAXException, TikaException, SQLException {
    	
    	String dbAnswer="";
    	String fileLocation = filepath;
    	int lastIndex = filepath.lastIndexOf(92);
        String path = filepath.substring(0,lastIndex);
        path = path.replace("\\", "\\\\");
       
    	
    	InputStream input = new FileInputStream(new File(fileLocation));
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
       
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);
        input.close();
    	
    	int id =0;
    	String title = metadata.get("title") == null ? "N/A" : metadata.get("title");
    	title = title.replace("'", "''");
    	String artist = metadata.get("xmpDM:artist") == null ? "N/A" : metadata.get("xmpDM:artist");
    	artist = artist.replace("'", "''");
    	String composer = metadata.get("xmpDM:composer") == null ? "N/A" : metadata.get("xmpDM:composer");
    	String genre = metadata.get("xmpDM:genre") == null ? "N/A" : metadata.get("xmpDM:genre");
    	String album = metadata.get("xmpDM:album") == null ? "N/A" : metadata.get("xmpDM:album");
    	album = album.replace("'", "''");
    	
    	
    	
    	if(checkIfDataExists(title, artist, path) == -1) {
	    	PreparedStatement prep;
			try {
				prep = con.prepareStatement("INSERT INTO `songs` (`id`, `title`, `artist`, `composer`, `genre`, `album`, `path`) "
						+ "VALUES('" + id +"', '" + title +"', '" + artist +"', '" + composer +"', '" +  genre +"', '" + album +"', '"+ path +"');");
				
				prep.execute();
				
				dbAnswer = "The metadata of the following song has been added to the database: " + title;
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e);;
			}
    	}else {
    		
    		dbAnswer ="Data already exists";
    	}
    	
		return dbAnswer;
    }
     
    private static int checkIfDataExists(String title, String artist, String path) throws SQLException {
			
			Statement st = con.createStatement();
			int result = -1;
			title = "'" + title + "'";
			artist = "'" + artist + "'";
			path = "'" + path + "'";
					
			try {
				
				String query = "SELECT id from songs WHERE title = " + title + " AND artist = " + artist + " AND path = " + path;
				rs = st.executeQuery(query);
				
				while(rs.next()) {
					result = rs.getInt("id");
					
				}
				
				
			}catch(Exception ex) {
				System.out.println("Error: " + ex);
			}
			
			return result;
			
			
		}
     
    public ResultSet listAllSongs() throws SQLException {
			
			rs = null;		
			try {
				
				String query = "SELECT id, title, artist, genre, album, path from songs";
				PreparedStatement prep = con.prepareStatement(query);
				rs = prep.executeQuery(query);
				
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "Couldn't connect to DataBase. Error: " + ex);
				
			}
			
			return rs;
	}
     
    public ResultSet searchByCriteria(String criteria, String keyword) throws SQLException {
		rs = null;
		keyword = "'%" + keyword + "%'";
			
		try {
			
			String query = "SELECT id, title, artist, genre, album, path from songs WHERE "+criteria+" LIKE " + keyword;
			PreparedStatement prep = con.prepareStatement(query);
			rs = prep.executeQuery(query);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return rs;
    }
    
    public void deleteById(int id[]) throws SQLException {
		
			
		try {
			for(int i=0; i<id.length; i++) {
				String query = "DELETE FROM songs WHERE id=" + id[i];
				PreparedStatement prep = con.prepareStatement(query);
				prep.execute();
				
				prep.close();
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		
		JOptionPane.showMessageDialog(null, "Data Deleted");
	}
     
    public void updateById(int id, String title, String artist, String genre, String album, String path) throws SQLException {
 		
    	title = title.replace("'", "''");
    	artist = artist.replace("'", "''");
    	album = album.replace("'", "''");
    	path = path.replace("\\", "\\\\");
    	String ID= Integer.toString(id);
 		try {
 			
 			String query = "UPDATE songs SET title=?, artist=?, genre=?, album=?, path=? WHERE id=?";
 			PreparedStatement prep = con.prepareStatement(query);
 			prep.setString(1, title);
 			prep.setString(2, artist);
 			prep.setString(3, genre);
 			prep.setString(4, album);
 			prep.setString(5, path);
 			prep.setString(6, ID);
 			prep.execute();
 			JOptionPane.showMessageDialog(null, "Data Updated");
 			prep.close();
 			
 		}catch(Exception ex) {
 			ex.printStackTrace();
 			
 		}
 	}
     
     public ResultSet searchById(int id) throws SQLException {
 		
	 rs = null;		
 		try {
 			
 			String query = "SELECT * FROM songs WHERE id=" + id;
 			PreparedStatement prep = con.prepareStatement(query);
 			rs = prep.executeQuery(query);
 		
 		}catch(Exception ex) {
 			JOptionPane.showMessageDialog(null, "Error: " + ex);
 			ex.printStackTrace();
 			
 		}
 	
 	return rs;	
 	}
}
