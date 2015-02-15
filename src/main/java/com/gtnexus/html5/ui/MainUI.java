package com.gtnexus.html5.ui;

import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.List;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.main.RevertBackChanges;
import com.gtnexus.html5.util.ProgramLauncher;
import com.gtnexus.html5.util.UsageScanner;

public class MainUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List preHTML5List = new List();
	private List html5List = new List();

	private JTextPane consoleArea = createConsole();

	private JProgressBar progressBar = new JProgressBar(0, 100);

	private final JLabel lblFile = new JLabel("URL Source                   ");
	private final JScrollPane scrollPane = new JScrollPane();
	private JTextField percentageTextArea = new JTextField("0%");
	private JTextField backupLocationField = new JTextField();
	private final JButton convertButton = new JButton("Convert All");
	private final JButton btnOpenWithAraxis = new JButton("Open with Araxis");
	private final JButton btnRollbackAll = new JButton("Rollback All");
	private final JButton btnConvertSelected = new JButton("Convert Selected");
	private final JButton btnRollbackSelected = new JButton("Rollback Selected");
	private final JButton btnClearConsole = new JButton("Clear Console");
	private final JButton btnBrowseSourceLocation = new JButton(
			"Browse URL Source");
	private final JButton btnSetBackupLocation = new JButton(
			"Change Backup Location");
	private TextField textFieldlocationDirectory = new TextField();
	private final JButton btnOpenWithDreamweaver = new JButton(
			"Open with Dreamweaver");
	private final JButton btnOpenWithFireFox = new JButton("Open with Firefox");
	private JButton btnErrorsRecorded = new JButton("Errors Recorded");
	private final JButton btnRemove = new JButton("Remove");
	private final JButton btnOpenWithIE = new JButton("Open with IE");
	private final JButton btnScan = new JButton("Scan");
	private final JButton btnStop = new JButton("Stop");
	private final JButton btnOpenScannedPages = new JButton("View Conflicts");
	
	private String sourcePath;
	private SwingWorker currentThread;
	private Executor current;
	
	private ProgramLauncher launcher = new ProgramLauncher(this);
	private UsageScanner scanner = new UsageScanner(this);
	
	public ProgramLauncher getProgramLauncher() {
		return this.launcher;
	}

	

	public MainUI() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addContent();
		addActionListners();
		JerichoJspParserUtil.initialize();
		dbLogger.initialize();
		printOnConsole(JerichoJspParserUtil.getDebuggerOutput(), "log");

		checkForPreviousErrors();

	}

	public static void main(String[] args) {

		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		new MainUI();

	}

	public void addContent() {

		setTitle("HTML5 Converter");

		textFieldlocationDirectory.setBounds(150, 18, 425, 22);

		getContentPane().setLayout(null);

		getContentPane().add(textFieldlocationDirectory);

		preHTML5List.setMultipleSelections(true);
		// preHTML5List.setMultipleMode(true);

		preHTML5List.setFont(new Font("Dialog", Font.PLAIN, 13));
		html5List.setFont(new Font("Dialog", Font.PLAIN, 13));

		preHTML5List.setBounds(10, 117, 575, 300);
		getContentPane().add(preHTML5List);
		html5List.setMultipleSelections(true);
		// html5List.setMultipleMode(true);

		html5List.setBounds(609, 117, 575, 300);
		getContentPane().add(html5List);

		Label preHTML5ListLable = new Label("Loaded Files");
		preHTML5ListLable.setFont(new Font("Cambria", Font.BOLD, 13));
		preHTML5ListLable.setBounds(234, 89, 99, 22);
		getContentPane().add(preHTML5ListLable);

		Label html5ListLable = new Label("Converted Files");
		html5ListLable.setFont(new Font("Cambria", Font.BOLD, 13));
		html5ListLable.setBounds(843, 89, 146, 22);
		getContentPane().add(html5ListLable);
		lblFile.setBounds(10, 18, 130, 18);

		getContentPane().add(lblFile);
		convertButton.setHorizontalAlignment(SwingConstants.LEFT);

		convertButton.setBounds(10, 423, 109, 23);
		getContentPane().add(convertButton);
		btnRollbackAll.setHorizontalAlignment(SwingConstants.LEFT);

		btnRollbackAll.setBounds(609, 423, 109, 23);
		getContentPane().add(btnRollbackAll);

		btnOpenWithAraxis.setHorizontalAlignment(SwingConstants.LEFT);

		btnRollbackAll.setHorizontalAlignment(SwingConstants.LEFT);

		btnRollbackAll.setBounds(609, 423, 109, 23);
		getContentPane().add(btnRollbackAll);

		btnOpenWithAraxis.setBounds(872, 423, 138, 23);
		getContentPane().add(btnOpenWithAraxis);

		progressBar.setBounds(10, 474, 956, 20);
		getContentPane().add(progressBar);

		scrollPane.setBounds(10, 505, 1174, 160);
		getContentPane().add(scrollPane);
		scrollPane.setViewportView(consoleArea);
		consoleArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) consoleArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		percentageTextArea.setEditable(false);
		percentageTextArea.setBounds(976, 474, 53, 20);
		getContentPane().add(percentageTextArea);
		percentageTextArea.setColumns(10);
		btnConvertSelected.setHorizontalAlignment(SwingConstants.LEFT);

		btnConvertSelected.setBounds(129, 423, 138, 22);

		getContentPane().add(btnConvertSelected);
		btnRollbackSelected.setHorizontalAlignment(SwingConstants.LEFT);

		btnRollbackSelected.setBounds(729, 423, 140, 22);

		getContentPane().add(btnRollbackSelected);

		btnClearConsole.setBounds(1054, 472, 132, 22);

		getContentPane().add(btnClearConsole);
		btnBrowseSourceLocation.setHorizontalAlignment(SwingConstants.LEFT);

		btnBrowseSourceLocation.setBounds(581, 17, 179, 23);
		getContentPane().add(btnBrowseSourceLocation);
		btnSetBackupLocation.setHorizontalAlignment(SwingConstants.LEFT);

		btnSetBackupLocation.setBounds(581, 54, 179, 23);
		getContentPane().add(btnSetBackupLocation);

		JLabel backupLocationLabel = new JLabel("Backup Directory ");
		backupLocationLabel.setBounds(10, 58, 130, 14);
		getContentPane().add(backupLocationLabel);

		getBackupLocationField().setEditable(false);
		getBackupLocationField().setBounds(150, 55, 425, 20);
		getContentPane().add(getBackupLocationField());
		getBackupLocationField().setColumns(10);
		getBackupLocationField().setText(launcher.DEFAULT_BACKUP_PATH);

		btnOpenWithDreamweaver.setBounds(1020, 423, 164, 23);
		getContentPane().add(btnOpenWithDreamweaver);

		btnErrorsRecorded.setEnabled(true);
		btnErrorsRecorded.setHorizontalAlignment(SwingConstants.LEFT);
		btnErrorsRecorded.setBounds(1030, 17, 154, 23);
		getContentPane().add(btnErrorsRecorded);

		btnOpenWithFireFox.setBounds(820, 17, 154, 23);
		getContentPane().add(btnOpenWithFireFox);

		
		btnRemove.setBounds(376, 423, 89, 23);
		getContentPane().add(btnRemove);

		btnOpenWithIE.setBounds(820, 54, 154, 23);
		getContentPane().add(btnOpenWithIE);
		
		
		
		btnStop.setEnabled(true);
		btnStop.setBounds(475, 423, 89, 23);
		getContentPane().add(btnStop);
		
		
		btnScan.setBounds(277, 423, 89, 23);
		getContentPane().add(btnScan);
		
		
		
		btnOpenScannedPages.setBounds(1030, 54, 154, 23);
		getContentPane().add(btnOpenScannedPages);

		this.setBounds(0, 0, 1200, 700);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);
		this.setVisible(true);
		this.setResizable(false);
		hideProgressBar();
	}

	private JTextPane createConsole() {

		JTextPane textPane = new JTextPane();
		StyledDocument doc = textPane.getStyledDocument();

		// Initialize some styles.
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "Dialog");
		StyleConstants.setFontSize(def, 12);

		Style s = doc.addStyle("error", regular);
		StyleConstants.setForeground(s, Color.RED);

		s = doc.addStyle("warning", regular);
		StyleConstants.setForeground(s, Color.ORANGE);

		s = doc.addStyle("info", regular);
		StyleConstants.setForeground(s, Color.GREEN);

		s = doc.addStyle("log", regular);
		StyleConstants.setForeground(s, Color.blue);

		return textPane;
	}

	/*
	 * Reads the target csv and store each line in a list. Each line contains a
	 * filepath.
	 */
	public ArrayList<String> readCSV() {

		ArrayList<String> list = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(sourcePath));
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				try {
					list.add(sCurrentLine.substring(sCurrentLine
							.lastIndexOf("/en/") + 3));
				} catch (StringIndexOutOfBoundsException e) {
					try {
						list.add(sCurrentLine.substring(sCurrentLine
								.lastIndexOf("\\en\\") + 3));
					} catch (StringIndexOutOfBoundsException e2) {
						list.add(sCurrentLine);
					}
				}
			}
			br.close();

		} catch (FileNotFoundException e) {
			printOnConsole("File Not Found! Please check file path.\n", "error");

		} catch (IOException e2) {
			consoleArea.setText(e2.getMessage());
		} catch (Exception e3) {

		}
		return list;
	}

	public void addActionListners() {
		/*
		 * converts the whole list.
		 */
		convertButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				currentThread = new SwingWorker<Integer, Integer>() {
					@Override
					protected Integer doInBackground() {

						String[] files = preHTML5List.getItems();
						
							disableButtons();
						
						for (int i = 0; i < files.length; i++) {
							if(!this.isCancelled()){
								progressBar.setIndeterminate(true);
	
								convertToHTML5(files[i]);
	
								progressBar.setIndeterminate(false);
	
								setProgressBarValue(files.length, i);
							}
						}
						enableButtons();
		
						return 0;
					}

				};

				currentThread.execute();
				printHtml5Count();
				
			}
		});
		/*
		 * converts the selected file/files.
		 */
		btnConvertSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				currentThread = new SwingWorker<Integer, Integer>() {
					@Override
					protected Integer doInBackground() throws Exception {
						
						String[] selectedItems = preHTML5List
								.getSelectedItems();
						disableButtons();
						for (int i = 0; i < selectedItems.length; i++) {
							if(!isCancelled()){
								progressBar.setIndeterminate(true);
								convertToHTML5(selectedItems[i]);
								progressBar.setIndeterminate(false);
								setProgressBarValue(selectedItems.length, i);

						}
						
					}
						enableButtons();	
						return 0;
					}

				};
				currentThread.execute();
				printHtml5Count();
			}
		});
		/*
		 * Revert all converted files
		 */
		btnRollbackAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Integer, Integer> revertFiles = new SwingWorker<Integer, Integer>() {
					@Override
					protected Integer doInBackground() throws Exception {
						disableButtons();
						String[] files = html5List.getItems();

						for (int i = 0; i < files.length; i++) {
							revertBack(files[i]);
							setProgressBarValue(files.length, i);
						}
						
						enableButtons();
						return 0;
					}
				};
				revertFiles.execute();
				printHtml5Count();
			}
		});
		/*
		 * Reverts only the selected file/files.
		 */
		btnRollbackSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Integer, Integer> revertSelectedFiles = new SwingWorker<Integer, Integer>() {
					@Override
					protected Integer doInBackground() throws Exception {
						disableButtons();
						String[] selectedItems = html5List.getSelectedItems();
						System.out.println(selectedItems.length + " "
								+ selectedItems[0]);
						for (int i = 0; i < selectedItems.length; i++) {
							revertBack(selectedItems[i]);
							setProgressBarValue(selectedItems.length, i);
						}
						enableButtons();
						return 0;
					}
				};
				revertSelectedFiles.execute();
				printHtml5Count();
			}
		});
		/*
		 * Clears the console
		 */
		btnClearConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				consoleArea.setText("");
			}
		});
		/*
		 * Stops the current working thread
		 */
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopCurrentThread();
			}
			
		});

		/*
		 * Sets the filepath textfield using a file chooser. Add the commented
		 * out lines to load files from the source text file instantly.
		 */
		btnBrowseSourceLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser();

					chooser.addChoosableFileFilter(new FileNameExtensionFilter(
							"Text files", "txt", "csv", "ini"));
					chooser.setAcceptAllFileFilterUsed(false);
					int returnVal = chooser.showOpenDialog(new Frame());
					if (returnVal == JFileChooser.FILES_ONLY) {

						sourcePath = chooser.getSelectedFile()
								.getAbsolutePath();
						loadToListFromFile();
						textFieldlocationDirectory.setText(sourcePath);
					}
				} catch (Exception ex) {

				}

			}
		});
		/*
		 * Sets the backup location.
		 */
		btnSetBackupLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					JFileChooser chooser = new JFileChooser();

					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setAcceptAllFileFilterUsed(false);
					int returnVal = chooser.showOpenDialog(new Frame());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						launcher.setBackupPath(chooser.getCurrentDirectory()
								.getAbsolutePath());
						printOnConsole(launcher.getBackupPath(), "info");
					}
				} catch (Exception ex) {
					printOnConsole(printStacktrace(ex), "error");
				}

			}
		});
		/*
		 * Opens the selected converted file and its preHTML5 version(taken from
		 * the corresponding directory in back) in Araxis Merge
		 */
		btnOpenWithAraxis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (html5List.getSelectedIndex() != -1) {
					launcher.openAraxis(html5List.getSelectedItem().toString());
				} else {
					printOnConsole("Please select a specific file to compare!",
							"warning");
				}
			}

		});
		/*
		 * 
		 */
		btnOpenWithDreamweaver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (html5List.getSelectedIndex() != -1)
					launcher.openDreamweaver(formatFilePath(html5List
							.getSelectedItem()));
				else if (preHTML5List.getSelectedIndex() != -1)
					launcher.openDreamweaver(formatFilePath(preHTML5List
							.getSelectedItem()));
				else
					printOnConsole("Please select a specific file to open!",
							"warning");
			}
		});

		btnErrorsRecorded.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createErrorsFrame();
			}
		});
		btnOpenWithFireFox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					launcher.openWithBrowser("FIREFOX", launcher.LOCALHOST,
							html5List.getSelectedItem().substring(1));

					launcher.openWithBrowser("FIREFOX", launcher.QA2HOST,
							html5List.getSelectedItem().substring(15));
				} catch (NullPointerException e2) {
					printOnConsole("Please select a file", "error");
				} catch (Exception e3) {
					printOnConsole(e3.getMessage(), "error");
				}
			}
		});
		btnOpenWithIE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					launcher.openWithBrowser("IE", launcher.LOCALHOST,
							html5List.getSelectedItem().substring(1));
					launcher.openWithBrowser("IE", launcher.QA2HOST, html5List
							.getSelectedItem().substring(15));
				} catch (NullPointerException e2) {
					printOnConsole("Please select a file", "error");
				}
			}
		});

		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingWorker<Integer, Integer> scan = new SwingWorker<Integer, Integer>() {
					@Override
					protected Integer doInBackground() throws Exception {
						disableButtons();
						setProgressBarValue(preHTML5List.getItemCount(),0);
						int i=0;
						for(String file : preHTML5List.getItems()){
							scan(file);
							setProgressBarValue(preHTML5List.getItemCount(),i);
							i++;
						}
						enableButtons();
						return 0;
					}
				};
				scan.execute();
			}
		});
		
		btnOpenScannedPages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openConflictsFrame();
				
			}
		});
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] selectedIndexes = preHTML5List.getSelectedIndexes();
				for (int i = selectedIndexes.length - 1; i >= 0; i--) {
				
					preHTML5List.remove(selectedIndexes[i]);
				}
			}
		});
	}

	/*
	 * Sets the progress bar value for an iteration. progress is calculated
	 * using progress = (current iteration/ length of array) * 100
	 */
	public void setProgressBarValue(int max, int iteration) {
		double progress;

		if (max == 1)
			progress = 100;
		else
			progress = (((double) iteration) / ((double) max - 1)) * 100;

		progressBar.setValue((int) progress);

		updateProgressTextArea(progress);
	}

	public void loadToListFromFile() {

		// clear list
		preHTML5List.removeAll();
		html5List.removeAll();

		ArrayList<String> fileList = readCSV();
		showProgressBar();
		int fileCount = 0;
		for (int i = 0; i < fileList.size(); i++) {
			if (launcher.checkFile(
					fileList.get(i),
					launcher.adminBasePath.substring(0,
							launcher.adminBasePath.indexOf("\\en\\") + 3))) {
				preHTML5List.add(fileList.get(i));
				fileCount++;
			} else
				printOnConsole(fileList.get(i) + " File Doesn't exist.",
						"error");
			setProgressBarValue(fileList.size(), i);

		}

		printOnConsole(fileCount + " new files Loaded.", "info");
		printPreHtml5Count();
	}

	public void printPreHtml5Count() {
		printOnConsole("Total of " + preHTML5List.getItemCount()
				+ " pre-HTML5 files exist.", "info");
	}

	public void printHtml5Count() {
		printOnConsole("Total of " + html5List.getItemCount()
				+ " HTML5 files exist.", "info");
	}

	public void convertToHTML5(String sourceFile) {
		try {

			if (launcher.isBackupValid()
					&& launcher.checkFile(sourceFile,
							launcher.adminBasePath
									.substring(0, launcher.adminBasePath
											.indexOf("\\en\\") + 4))) {
				JerichoJspParserUtil.clearConsoleWriter();
				JerichoJspParserUtil.convertToHTML5(formatFilePath(sourceFile),
						false, sourcePath);
				printOnConsole(JerichoJspParserUtil.getDebuggerOutput(), "log");
				html4ToHtml5(sourceFile);

			} else {
				printOnConsole("Backup path is not correct!", "error");
				printOnConsole("Conversion aborted.", "error");
			}

		} catch (HTML5ParserException ex) {
			ex.printStackTrace();
			printOnConsole(ex.getType(), "error");
			printOnConsole(ex.getMessage(), "error");
			printOnConsole(ex.getTagInfo(), "error");

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public void revertBack(String convertedFilePath) {

		try {
			if (launcher.isBackupValid()
					&& launcher.checkFile(convertedFilePath,
							launcher.adminBasePath
									.substring(0, launcher.adminBasePath
											.indexOf("\\en\\") + 4))) { // ////
				JerichoJspParserUtil.clearConsoleWriter();
				RevertBackChanges.revertChanges(
						formatFilePath(convertedFilePath),
						launcher.getBackupPath());
				printOnConsole(JerichoJspParserUtil.getDebuggerOutput(), "log");
				html5ToHtml4(convertedFilePath);

			}
		} catch (Exception e) {
			printOnConsole(e.getMessage(), "error");
			printOnConsole(printStacktrace(e), "error");

		}
	}

	public void html4ToHtml5(String entry) {

		preHTML5List.remove(entry);
		html5List.add(entry);
	}

	public void html5ToHtml4(String entry) {
		html5List.remove(entry);
		preHTML5List.add(entry);
	}

	public void hideProgressBar() {
		progressBar.setVisible(false);
		percentageTextArea.setText("");
	}

	public void showProgressBar() {
		progressBar.setVisible(true);
		progressBar.setValue(0);
	}

	public void updateProgressTextArea(double displayValue) {
		percentageTextArea.setText(Math.rint(displayValue) + "%");
	}

	public void printOnConsole(String message, String style) {

		StyledDocument doc = consoleArea.getStyledDocument();
		try {
			consoleArea.getStyledDocument().insertString(doc.getLength(),
					message + "\n", doc.getStyle(style));
			consoleArea
					.setCaretPosition(consoleArea.getDocument().getLength() - 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

	}

	public static String printStacktrace(Exception e) {
		StringWriter w = new StringWriter();
		e.printStackTrace(new PrintWriter(w));
		return w.toString();
	}

	public String formatFilePath(String fileName) {
		return launcher.adminBasePath.substring(0,
				launcher.adminBasePath.indexOf("\\en\\") + 3)
				+ fileName;
	}

	public String getBackupFilePath(String fileName) {
		return launcher.getBackupPath() + fileName;
	}

	public void checkForPreviousErrors() {
		int numberOfErrors = dbLogger.getNumberOfErrors();
		if (numberOfErrors == 0)
			disableErrorsRecordedButton();
		else
			enableErrorsRecordedButton(numberOfErrors);
	}

	public void enableErrorsRecordedButton(int numberOfErrors) {

		btnErrorsRecorded.setText(numberOfErrors + " Error(s) Exist.");
		btnErrorsRecorded.setEnabled(true);
	}

	public void disableErrorsRecordedButton() {
		btnErrorsRecorded.setText("");
		btnErrorsRecorded.setEnabled(false);
	}

	public void createErrorsFrame() {
		ErrorsFrame.getInstance(this);
		disableErrorsRecordedButton();

	}

	/**
	 * @return the backupLocationField
	 */
	public JTextField getBackupLocationField() {
		return backupLocationField;
	}

	/**
	 * @param backupLocationField
	 *            the backupLocationField to set
	 */
	public void setBackupLocationField(JTextField backupLocationField) {
		this.backupLocationField = backupLocationField;
	}
	
	public void stopCurrentThread(){
		currentThread.cancel(true);
		enableButtons();
	}
	public void enableButtons(){
		this.btnConvertSelected.setEnabled(true);
		this.convertButton.setEnabled(true);
		this.btnRollbackSelected.setEnabled(true);
		this.btnRollbackAll.setEnabled(true);
		this.btnBrowseSourceLocation.setEnabled(true);
		this.btnSetBackupLocation.setEnabled(true);
		this.btnRemove.setEnabled(true);
		this.btnScan.setEnabled(true);
	}
	public void disableButtons(){
		this.btnConvertSelected.setEnabled(false);
		this.convertButton.setEnabled(false);
		this.btnRollbackSelected.setEnabled(false);
		this.btnRollbackAll.setEnabled(false);
		this.btnBrowseSourceLocation.setEnabled(false);
		this.btnSetBackupLocation.setEnabled(false);
		this.btnRemove.setEnabled(false);
		this.btnScan.setEnabled(false);
	}
	
	private void openConflictsFrame(){
		ConflictsFrame conflicts = new ConflictsFrame(this);
		conflicts.show();
	}
	
	private void scan(String filename){
		printOnConsole("Scanning "+filename,"info");
		scanner.performScan(formatFilePath(filename),sourcePath);
		printOnConsole(filename+ "Completed.","info");
	}
}