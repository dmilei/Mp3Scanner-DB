package MP3;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Update extends JFrame {
	private static Database dataBase = new Database();
	private ResultSet rs;
	private int updateID;
	
	private JPanel contentPane;
	private JTextField textFieldTitle;
	private JTextField textFieldArtist;
	private JTextField textFieldGenre;
	private JTextField textFieldAlbum;
	private JTextField textFieldPath;
	
	private void fillForm() throws SQLException {
		try {
			rs = dataBase.searchById(updateID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(rs.next()) {
			textFieldTitle.setText(rs.getString("title"));
			textFieldArtist.setText(rs.getString("artist"));
			textFieldGenre.setText(rs.getString("genre"));
			textFieldAlbum.setText(rs.getString("album"));
			textFieldPath.setText(rs.getString("path"));
		}
	}
	
	private void closeWindow(ActionEvent e) {
		JComponent comp = (JComponent) e.getSource();
		Window win = SwingUtilities.getWindowAncestor(comp);
		win.dispose();
	}
	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public Update(int updateId){
		updateID = updateId;
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 492, 317);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblImageLabel = new JLabel("");
		lblImageLabel.setBounds(338, 11, 128, 128);
		Image img = new ImageIcon(this.getClass().getResource("/edit-icon.png")).getImage();
		lblImageLabel.setIcon(new ImageIcon(img));
		contentPane.add(lblImageLabel);
		
		JLabel lblMain = new JLabel("Update Song Info");
		lblMain.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		lblMain.setBounds(112, 11, 154, 22);
		contentPane.add(lblMain);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTitle.setBounds(10, 50, 46, 14);
		contentPane.add(lblTitle);
		
		JLabel lblArtist = new JLabel("Artist:");
		lblArtist.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblArtist.setBounds(10, 80, 51, 14);
		contentPane.add(lblArtist);
		
		JLabel lblGenre = new JLabel("Genre:");
		lblGenre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblGenre.setBounds(10, 110, 51, 14);
		contentPane.add(lblGenre);
		
		JLabel lblAlbum = new JLabel("Album:");
		lblAlbum.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblAlbum.setBounds(10, 140, 51, 14);
		contentPane.add(lblAlbum);
		
		JLabel lblDir = new JLabel("Path:");
		lblDir.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDir.setBounds(10, 170, 51, 14);
		contentPane.add(lblDir);
		
		textFieldTitle = new JTextField();
		textFieldTitle.setBounds(66, 48, 262, 20);
		contentPane.add(textFieldTitle);
		textFieldTitle.setColumns(10);
		
		textFieldArtist = new JTextField();
		textFieldArtist.setColumns(10);
		textFieldArtist.setBounds(66, 78, 262, 20);
		contentPane.add(textFieldArtist);
		
		textFieldGenre = new JTextField();
		textFieldGenre.setColumns(10);
		textFieldGenre.setBounds(66, 105, 262, 20);
		contentPane.add(textFieldGenre);
		
		textFieldAlbum = new JTextField();
		textFieldAlbum.setColumns(10);
		textFieldAlbum.setBounds(66, 135, 262, 20);
		contentPane.add(textFieldAlbum);
		
		textFieldPath = new JTextField();
		textFieldPath.setColumns(10);
		textFieldPath.setBounds(66, 168, 262, 20);
		contentPane.add(textFieldPath);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = textFieldTitle.getText();
				String artist = textFieldArtist.getText();
				String genre = textFieldGenre.getText();
				String album = textFieldAlbum.getText();
				String path = textFieldPath.getText();
				
				try {
					int action = JOptionPane.showConfirmDialog(null, "Do you want to update the selected song with ID of "+updateID+"?", "Update", JOptionPane.YES_NO_OPTION);
					if(action == 0) {
						dataBase.updateById(updateID, title, artist, genre, album, path);
						closeWindow(e);
						
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnUpdate.setBounds(239, 210, 89, 23);
		contentPane.add(btnUpdate);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeWindow(e);
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnCancel.setBounds(10, 211, 89, 23);
		contentPane.add(btnCancel);
		
		try {
			fillForm();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
