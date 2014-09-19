package com.gtnexus.html5.ui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.util.ArrayList;

import javax.swing.JTable;

import com.gtnexus.html5.ui.MainUI;
import com.gtnexus.html5.util.Error;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
public class ErrorsFrame extends JFrame {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private static ErrorsFrame instance = null;
	
	private JScrollPane scrollPane = new JScrollPane();
	private JTable table;
	private JButton btnNotepad = new JButton("Open with Notepad++");
	private JButton btnDreamweaver = new JButton("Open with Dreamweaver");
	private JButton btnConvert = new JButton("Convert");
	private MainUI parent;
	
	private ArrayList<Error> errorList;
	private String[] COLUMNS = new String[]{ "Page Id", "Error Type", "Error Message" , "Last Converted Line"};
	
	private ErrorsFrame(MainUI parentFrame) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				parent.checkForPreviousErrors();
			}
		});
		parent = parentFrame;
		populateList();
		addActionListeners();
	}
	
	static synchronized ErrorsFrame getInstance(MainUI parentFrame){
		if(instance==null) return new ErrorsFrame(parentFrame);
		else return instance;
	}
	private void addContent(String[][] errorsList){
		getContentPane().setLayout(null);
		table = new JTable(errorsList,COLUMNS);
		
		table.setBounds(10, 30, 623, 321);
		scrollPane.setBounds(10, 47, 760, 354);
		scrollPane.setViewportView(table);		
		getContentPane().add(scrollPane);
				
		btnConvert.setBounds(780, 118, 180, 23);
		getContentPane().add(btnConvert);		
		
		btnDreamweaver.setBounds(780, 50, 180, 23);
		getContentPane().add(btnDreamweaver);

		
		btnNotepad.setBounds(780, 84, 180, 23);
		getContentPane().add(btnNotepad);
		this.setBounds(0, 0, 1000, 450);;
		this.setVisible(true);
	}

	private void addActionListeners(){
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				convert();
			}				
		});
		btnDreamweaver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					String path = getPath();
					parent.openDreamweaver(path);
				}catch(NullPointerException ex){
					
				}
			}
		});
		btnNotepad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
	}
	private void convert(){
		try{
			parent.convertToHTML5(getPath());
			removeRow();
		}catch(Exception ex){
			
		}
	}
	private void removeRow(){
		table.remove(table.getSelectedRow());
	}
	private String getPath(){
		try{
			return dbLogger.getPath(errorList.get(table.getSelectedRow()).getPageId());
		}catch(ArrayIndexOutOfBoundsException e){
			parent.printWarning("No file has been selected!");
			return null;
		}
	}
	private String[] getRow(Error errorObject){
		String id = errorObject.getPageId() +"";
		String type = errorObject.getErrorType();
		String message = errorObject.getErrorMessage();
		String line = errorObject.getLastConvertedLine() +"";
		
		return new String[]{id,type,message,line};
	}
	
    private void populateList(){
    	errorList = dbLogger.getErrors();

    	String[][] errorArray = new String[errorList.size()][4];
    	for(int i = 0; i< errorList.size();i++){
    		errorArray[i] = getRow(errorList.get(i));
    	}
    	addContent(errorArray);
    }
}
