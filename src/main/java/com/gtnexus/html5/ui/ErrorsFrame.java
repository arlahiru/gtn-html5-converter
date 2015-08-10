package com.gtnexus.html5.ui;

import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.gtnexus.html5.util.Error;
import com.gtnexus.html5.util.ProgramLauncher;
public class ErrorsFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final String BASE_PATH = "C:/code/gtnexus/devl/modules/main/tcard/web/tradecard/en/";
	private static final long serialVersionUID = 1L;
	
	private static ErrorsFrame instance = null;
	
	private JScrollPane scrollPane = new JScrollPane();
	private JTable table;
	private JButton btnNotepad = new JButton("Open with Notepad++");
	private JButton btnDreamweaver = new JButton("Open with Dreamweaver");
	private JButton btnConvert = new JButton("Convert");
	private MainUI parent;
	private ProgramLauncher launcher; 
	private ArrayList<Error> errorList;
	private String[] COLUMNS = new String[]{ "Page Id", "Path", "Error Type", "Error Message" , "Last Converted Line"};
	
	private ErrorsFrame(MainUI parentFrame) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				parent.checkForPreviousErrors();
			}
		});
		parent = parentFrame;
		launcher = parent.getProgramLauncher();
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
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(450);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(250);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		
		
		table.setBounds(10, 30, 960, 521);
		scrollPane.setBounds(10, 47, 980, 554);
		scrollPane.setViewportView(table);		
		getContentPane().add(scrollPane);
				
		btnConvert.setBounds(1000, 118, 180, 23);
		getContentPane().add(btnConvert);		
		
		btnDreamweaver.setBounds(1000, 50, 180, 23);
		getContentPane().add(btnDreamweaver);

		
		btnNotepad.setBounds(1000, 84, 180, 23);
		getContentPane().add(btnNotepad);
		this.setBounds(0, 0, 1200, 640);
		this.setTitle("Errors Recorded");;
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
				
					launcher.openDreamweaver(getPath());
				}catch(NullPointerException ex){
					
				}
			}
		});
		btnNotepad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				launcher.openNotePad(getPath());
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
			parent.printOnConsole("No file has been selected!","error");
			return null;
		}
	}
	private String[] getRow(Error errorObject){
		String id = errorObject.getPageId() +"";
		String type = errorObject.getErrorType();
		String message = errorObject.getErrorMessage();
		String line = errorObject.getLastConvertedLine() +"";
		String path ="";
		if(errorObject.getPath().contains("en/"))
			path = errorObject.getPath().substring(errorObject.getPath().indexOf("en/")+2);
		else
			path = errorObject.getPath().substring(errorObject.getPath().indexOf("en\\")+2);
		return new String[]{id,path,type,message,line};
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
