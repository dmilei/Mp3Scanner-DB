package MP3;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
	private static Database dataBase = new Database();
	private ResultSet rs;
	private int countInserts;
	
	private JFrame frame;
	private JTextField searchField;
	private JLabel lblSaves;
	private JTable table;
	private JTextField textFieldSearch;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}
	
	private void printTree(int depth, File file) throws IOException, SAXException, TikaException, SQLException { 
        StringBuilder indent = new StringBuilder();
        String name = file.getName();
        String path = file.getAbsolutePath();
        
        
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "Error: The selected folder does not exists!");
            return;
        }
        
        for (int i = 0; i < depth; i++) {
            indent.append(".");
        }

        
        //Print file name, we can choose file extension
        if(name.contains(".mp3") == true) {
            String dbAnswer = dataBase.storeMetadata(path);
            if(dbAnswer!="Data already exists") {
            	countInserts++;
            }
            
        }
        //Recurse children
        if (file.isDirectory()) { 
            File[] files = file.listFiles(); 
            for (int i = 0; i < files.length; i++){
                printTree(depth + 4, files[i]);
            } 
        }
        
        
    }
	
	public void refreshTable() {
		try {
			rs = dataBase.listAllSongs();
			table.setModel(DbUtils.resultSetToTableModel(rs));
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 742, 584);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblMain = new JLabel("Welcome to my MP3 Finder & DataBase");
		lblMain.setBounds(184, 11, 409, 50);
		lblMain.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		frame.getContentPane().add(lblMain);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 75, 706, 460);
		frame.getContentPane().add(tabbedPane);
		
		JPanel scanPanel = new JPanel();
		tabbedPane.addTab("Scan my PC for MP3 songs", null, scanPanel, null);
		scanPanel.setLayout(null);
		
		JLabel lblPath = new JLabel("Please enter the path of the directory you want to scan");
		lblPath.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPath.setBounds(36, 25, 389, 31);
		scanPanel.add(lblPath);
		
		JLabel lblPathline = new JLabel("Path:");
		lblPathline.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPathline.setBounds(10, 67, 46, 14);
		scanPanel.add(lblPathline);
		
		searchField = new JTextField();
		searchField.setBounds(55, 64, 366, 20);
		scanPanel.add(searchField);
		searchField.setColumns(10);
		
		JLabel ImageLabel = new JLabel("");
		ImageLabel.setBounds(491, 11, 200, 200);
		Image img = new ImageIcon(this.getClass().getResource("/search.png")).getImage();
		ImageLabel.setIcon(new ImageIcon(img));
		scanPanel.add(ImageLabel);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String path = searchField.getText();
				try {
					JOptionPane.showMessageDialog(null, "The scanning of the directory and subdirectories will start after pressing OK. Wait for the process summary to come up.");
					printTree(0, new File(path));
					lblSaves.setText("After scanning the chosen directory and subdirectories we found " + countInserts + " songs which we saved to the DB.");
					countInserts = 0;
				} catch (IOException | SAXException | TikaException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSearch.setBounds(313, 114, 108, 23);
		scanPanel.add(btnSearch);
		
		lblSaves = new JLabel("");
		lblSaves.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblSaves.setBounds(36, 268, 644, 20);
		scanPanel.add(lblSaves);
		
		JPanel listPanel = new JPanel();
		tabbedPane.addTab("List & Search MP3 songs", null, listPanel, null);
		listPanel.setLayout(null);
		
		JLabel lblImage = new JLabel("");
		lblImage.setBounds(491, 11, 200, 200);
		Image img2 = new ImageIcon(this.getClass().getResource("/list-icon.png")).getImage();
		lblImage.setIcon(new ImageIcon(img2));
		listPanel.add(lblImage);
		
		JLabel lblList = new JLabel("List of songs in Database");
		lblList.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblList.setBounds(165, 11, 189, 21);
		listPanel.add(lblList);
		
		
		JButton btnListAll = new JButton("List All");
		btnListAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs = dataBase.listAllSongs();
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnListAll.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnListAll.setBounds(539, 222, 114, 23);
		listPanel.add(btnListAll);
		
		JScrollPane scrollPane = new JScrollPane();
		
		scrollPane.setBounds(25, 49, 456, 333);
		listPanel.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		
		JComboBox comboBoxSearchCriteria = new JComboBox();
		comboBoxSearchCriteria.setFont(new Font("Tahoma", Font.BOLD, 11));
		comboBoxSearchCriteria.setModel(new DefaultComboBoxModel(new String[] {"Search Criteria", "Title", "Artist", "Genre", "Album"}));
		comboBoxSearchCriteria.setBounds(539, 303, 114, 22);
		listPanel.add(comboBoxSearchCriteria);
		
		textFieldSearch = new JTextField();
		textFieldSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					String criteria = comboBoxSearchCriteria.getSelectedItem().toString();
					String keyword = textFieldSearch.getText();
					rs = dataBase.searchByCriteria(criteria, keyword);
					table.setModel(DbUtils.resultSetToTableModel(rs));
					
							
				}catch(Exception error){
					error.printStackTrace();
				}
			}
		});
		textFieldSearch.setBounds(491, 336, 200, 20);
		listPanel.add(textFieldSearch);
		textFieldSearch.setColumns(10);
		
		JLabel lblDynamicSearch = new JLabel("Dynamic search");
		lblDynamicSearch.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblDynamicSearch.setBounds(552, 271, 101, 21);
		listPanel.add(lblDynamicSearch);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row[] = table.getSelectedRows();
				
				if(row.length>0) {
					int deleteId [] = new int[row.length];
					for(int i=0; i<row.length; i++) {
						deleteId [i] = (int) table.getModel().getValueAt(row[i], 0);
					}
					
					int action = JOptionPane.showConfirmDialog(null, "Do you want to delete the selected row(s)?", "Delete", JOptionPane.YES_NO_OPTION);
					if(action==0) {
						try {
							dataBase.deleteById(deleteId);
							refreshTable();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Select at least one row to delete!");
				}
			}
		});
		btnDelete.setBounds(392, 398, 89, 23);
		listPanel.add(btnDelete);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row!=-1) {
					int updateId = (int) table.getModel().getValueAt(row, 0);
					Update updateWindow = new Update(updateId);
					updateWindow.setVisible(true);
				}
				else{
					JOptionPane.showMessageDialog(null, "Select one row to update!");
				}
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnUpdate.setBounds(25, 398, 89, 23);
		listPanel.add(btnUpdate);
		
		
	}
}
