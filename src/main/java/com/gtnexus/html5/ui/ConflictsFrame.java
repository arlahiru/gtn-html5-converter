package com.gtnexus.html5.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;

public class ConflictsFrame extends JFrame {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MainUI mainUi;
	private JTable table;
	private JTextField searchTag;
	private JScrollPane scrollPane = new JScrollPane();
	private JButton btnSearch = new JButton("Search Usage");
	private JButton btnListAll;
	private JButton btnListAdminPages= new JButton("Conflicting Admin Pages");
	
	public ConflictsFrame(MainUI parentFrame) {
		this.mainUi = parentFrame;
		this.setTitle("View Conflicts");
		loadAll();
		addContent();
		setActionListeners();
	}
	private void refresh(){
		scrollPane.setViewportView(table);
		getContentPane().add(scrollPane);
	}
	private void addContent(){
		setResizable(false);
		getContentPane().setLayout(null);
		table.setBounds(50, 75, 1100, 500);	
		scrollPane.setBounds(50, 75, 1100, 500);
		refresh();		
		searchTag = new JTextField();
		searchTag.setBounds(50, 23, 318, 20);
		getContentPane().add(searchTag);
		searchTag.setColumns(10);		
		btnSearch.setBounds(401, 22, 138, 23);
		getContentPane().add(btnSearch);
		{
			btnListAll = new JButton("List All");
			
			btnListAll.setBounds(559, 22, 138, 23);
			getContentPane().add(btnListAll);
		}
		btnListAdminPages.setBounds(725, 22, 160, 23);
		getContentPane().add(btnListAdminPages);
		this.setBounds(0, 0, 1200, 650);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);
		this.setVisible(true);
		this.setResizable(false);
	}
	
	private void setActionListeners(){
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
		btnListAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadAll();
			}
		});
		btnListAdminPages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadCommonAdminPageList();
			}
		});
	}
	private void loadAll(){
		loadData(dbLogger.getAllConflicts(),new String[]{"Admin Page","TradePage"});
	}
	private void search(){
		loadData(dbLogger.searchConflicts(searchTag.getText().toString()),new String[]{"Usages"});
	}
	private void loadCommonAdminPageList(){
		loadData(dbLogger.getCommonAdminPageList(),new String[]{"Common Admin Page List"});
	}
	private void loadData(ArrayList<String> list,String[] headers){
		int iterations = list.size()/headers.length;
		String[][] tableData = new String[iterations][headers.length];
		int row =0;
		for(int i=0;i<list.size();i+=headers.length){
			for(int j=0;j<headers.length;j++){
				tableData[row][j]=list.get(i+j);
			}
			row++;
		}
		table = new JTable(tableData, headers);
		refresh();
	}

}
