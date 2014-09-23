package com.gtnexus.html5.ui;

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

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.gtnexus.html5.exception.HTML5ParserException;
import com.gtnexus.html5.main.JerichoJspParserUtil;
import com.gtnexus.html5.main.RevertBackChanges;

import static com.gtnexus.html5.main.JerichoJspParserUtil.dbLogger;

public class MainUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List preHTML5List = new List();
	private List html5List = new List();
	private JTextArea notificationArea = new JTextArea();

	private JProgressBar progressBar = new JProgressBar(0, 100);

	private final JLabel lblFile = new JLabel("URL Source                   ");
	private final JScrollPane scrollPane = new JScrollPane();
	private JTextField percentageTextArea = new JTextField("0%");
	private JTextField backupLocationField = new JTextField();
	private final JButton convertButton = new JButton("Convert All");
	private final JButton openWithAraxis = new JButton("Open with Araxis");
	private final JButton revertButton = new JButton("Rollback All");
	private final JButton convertSelectedButton = new JButton(
			"Convert Selected");
	private final JButton revertSelectedButton = new JButton(
			"Rollback Selected");
	private final JButton clearNotificationArea = new JButton("Clear Console");
	private final JButton browseSourceLocation = new JButton(
			"Browse URL Source");
	private final JButton setBackupLocation = new JButton(
			"Change Backup Location");
	private TextField locationDirectory = new TextField();
	private final JButton btnOpenWithDreamweaver = new JButton(
			"Open with Dreamweaver");
	private final JButton btnOpenWithFireFox = new JButton("Open with Firefox");
	private JButton btnErrorsRecorded = new JButton("Errors Recorded");
	private final JButton btnRemove = new JButton("Remove");
	private final JButton btnOpenWithIE = new JButton("Open with IE");
	private String sourcePath;
	private String backupPath;

	private String araxis_path = "C:/Program Files/Araxis/Araxis Merge/merge.exe";
	private String dreamweaver_path = "C:/Program Files (x86)/Macromedia/Dreamweaver 8/Dreamweaver.exe";
	private String firefox_path = "C:/Program Files (x86)/Mozilla Firefox/firefox.exe";
	private String internetExplorer_path = "C:/Program Files (x86)/Internet Explorer/iexplorer.exe";
	private final String DEFAULT_BACKUP_PATH = "C:/TcardWebBackup";
	private final String basePath = "C:/code/gtnexus/development/modules/main/tcard";
	private final String CONFIG_FILE = "config.ini";
	private final String LOCALHOST = "http://localhost:8080/";
	private final String QA2HOST = "http://commerce.qa2.tradecard.com/";
	private boolean isBackupValid = false;

	public MainUI() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addContent();
		addActionListners();
		JerichoJspParserUtil.initialize();
		backupPath = DEFAULT_BACKUP_PATH;
		// dbLogger.initialize();
		setBackupPath(backupPath);
		// checkForPreviousErrors();
		printOnConsole(JerichoJspParserUtil.getDebuggerOutput());
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

		locationDirectory.setBounds(150, 18, 425, 22);

		getContentPane().setLayout(null);

		getContentPane().add(locationDirectory);
		preHTML5List.setMultipleMode(true);
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
		revertButton.setHorizontalAlignment(SwingConstants.LEFT);

		revertButton.setBounds(609, 423, 109, 23);
		getContentPane().add(revertButton);

		openWithAraxis.setHorizontalAlignment(SwingConstants.LEFT);

		revertButton.setHorizontalAlignment(SwingConstants.LEFT);

		revertButton.setBounds(609, 423, 109, 23);
		getContentPane().add(revertButton);

		openWithAraxis.setBounds(872, 423, 138, 23);
		getContentPane().add(openWithAraxis);

		progressBar.setBounds(10, 480, 956, 14);
		getContentPane().add(progressBar);

		scrollPane.setBounds(10, 505, 1174, 160);
		getContentPane().add(scrollPane);
		scrollPane.setViewportView(notificationArea);
		notificationArea.setEditable(false);

		percentageTextArea.setEditable(false);
		percentageTextArea.setBounds(976, 474, 53, 20);
		getContentPane().add(percentageTextArea);
		percentageTextArea.setColumns(10);
		convertSelectedButton.setHorizontalAlignment(SwingConstants.LEFT);

		convertSelectedButton.setBounds(130, 423, 138, 22);

		getContentPane().add(convertSelectedButton);
		revertSelectedButton.setHorizontalAlignment(SwingConstants.LEFT);

		revertSelectedButton.setBounds(729, 423, 140, 22);

		getContentPane().add(revertSelectedButton);

		clearNotificationArea.setBounds(1054, 472, 132, 22);

		getContentPane().add(clearNotificationArea);
		browseSourceLocation.setHorizontalAlignment(SwingConstants.LEFT);

		browseSourceLocation.setBounds(581, 17, 179, 23);
		getContentPane().add(browseSourceLocation);
		setBackupLocation.setHorizontalAlignment(SwingConstants.LEFT);

		setBackupLocation.setBounds(581, 54, 179, 23);
		getContentPane().add(setBackupLocation);

		JLabel backupLocationLabel = new JLabel("Backup Directory ");
		backupLocationLabel.setBounds(10, 58, 130, 14);
		getContentPane().add(backupLocationLabel);

		backupLocationField.setEditable(false);
		backupLocationField.setBounds(150, 55, 425, 20);
		getContentPane().add(backupLocationField);
		backupLocationField.setColumns(10);
		backupLocationField.setText(DEFAULT_BACKUP_PATH);

		btnOpenWithDreamweaver.setBounds(1020, 423, 164, 23);
		getContentPane().add(btnOpenWithDreamweaver);

		btnErrorsRecorded.setEnabled(false);
		btnErrorsRecorded.setHorizontalAlignment(SwingConstants.LEFT);
		btnErrorsRecorded.setBounds(1016, 17, 154, 23);
		getContentPane().add(btnErrorsRecorded);

		btnOpenWithFireFox.setBounds(835, 54, 154, 23);
		getContentPane().add(btnOpenWithFireFox);
		
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i = 0;i <= preHTML5List.getSelectedIndexes().length;i++)
				preHTML5List.remove(preHTML5List.getSelectedIndexes()[i]);
			}
		});
		btnRemove.setBounds(278, 423, 89, 23);
		getContentPane().add(btnRemove);
		
		
		
		btnOpenWithIE.setBounds(1016, 54, 154, 23);
		getContentPane().add(btnOpenWithIE);

		this.setBounds(0, 0, 1200, 700);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height
				/ 2 - this.getSize().height / 2);
		this.setVisible(true);
		this.setResizable(false);
		hideProgressBar();
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
				list.add(sCurrentLine);
			}
			br.close();

		} catch (FileNotFoundException e) {
			notificationArea
					.append("File Not Found! Please check file path.\n");

		} catch (IOException e2) {
			notificationArea.setText(e2.getMessage());
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

				SwingWorker<Integer, Integer> convertFiles = new SwingWorker<Integer, Integer>() {
					@Override
					protected Integer doInBackground() {

						String[] files = preHTML5List.getItems();

						for (int i = 0; i < files.length; i++) {

							convertToHTML5(files[i]);

							setProgressBarValue(files.length, i);
						}
						return 0;
					}
				};
				convertFiles.execute();
				printHtml5Count();

			}
		});
		/*
		 * converts the selected file/files.
		 */
		convertSelectedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Integer, Integer> convertFiles = new SwingWorker<Integer, Integer>() {
					@Override
					protected Integer doInBackground() throws Exception {
						String[] selectedItems = preHTML5List
								.getSelectedItems();

						for (int i = 0; i < selectedItems.length; i++) {
							convertToHTML5(selectedItems[i]);
							setProgressBarValue(selectedItems.length, i);
						}

						return 0;
					}
				};
				convertFiles.execute();
				printHtml5Count();
			}
		});
		/*
		 * Revert all converted files
		 */
		revertButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Integer, Integer> revertFiles = new SwingWorker<Integer, Integer>() {
					@Override
					protected Integer doInBackground() throws Exception {
						String[] files = html5List.getItems();

						for (int i = 0; i < files.length; i++) {
							revertBack(files[i]);
							setProgressBarValue(files.length, i);
						}

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
		revertSelectedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				SwingWorker<Integer, Integer> revertSelectedFiles = new SwingWorker<Integer, Integer>() {
					@Override
					protected Integer doInBackground() throws Exception {
						String[] selectedItems = html5List.getSelectedItems();
						System.out.println(selectedItems.length + " "
								+ selectedItems[0]);
						for (int i = 0; i < selectedItems.length; i++) {
							revertBack(selectedItems[i]);
							setProgressBarValue(selectedItems.length, i);
						}

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
		clearNotificationArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notificationArea.setText("");
			}
		});

		/*
		 * Opens the selected file using notepad++;
		 */
		openWithAraxis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ProcessBuilder pb = new ProcessBuilder(NOTEPAD_PATH,
				// html5List.getSelectedItem());
				ProcessBuilder pb = new ProcessBuilder(araxis_path, backupPath
						+ html5List.getSelectedItem(), formatFilePath(html5List
						.getSelectedItem()));
				try {
					pb.start();
				} catch (IOException ex) {
					printOnConsole(ex.getMessage());
					printOnConsole(printStacktrace(ex));

				}
			}
		});
		/*
		 * Sets the filepath textfield using a file chooser. Add the commented
		 * out lines to load files from the source text file instantly.
		 */
		browseSourceLocation.addActionListener(new ActionListener() {
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
					}
				} catch (Exception ex) {

				}

			}
		});
		/*
		 * Sets the backup location.
		 */
		setBackupLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					JFileChooser chooser = new JFileChooser();

					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					chooser.setAcceptAllFileFilterUsed(false);
					int returnVal = chooser.showOpenDialog(new Frame());
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						setBackupPath(chooser.getCurrentDirectory()
								.getAbsolutePath());
						printOnConsole(backupPath);
					}
				} catch (Exception ex) {
					printOnConsole(printStacktrace(ex));
				}

			}
		});
		/*
		 * Opens the selected converted file and its preHTML5 version(taken from
		 * the corresponding directory in back) in Araxis Merge
		 */
		openWithAraxis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openAraxis();
			}
		});
		/*
		 * 
		 */
		btnOpenWithDreamweaver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openDreamweaver(formatFilePath(html5List.getSelectedItem()));
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
					firefox_path = openWithBrowser("FIREFOX", firefox_path,
							LOCALHOST, html5List.getSelectedItem().substring(1));
					openWithBrowser("FIREFOX", firefox_path, QA2HOST, html5List
							.getSelectedItem().substring(15));
				} catch (NullPointerException e2) {
					printWarning("Please select a file");
				}
			}
		});
		btnOpenWithIE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					firefox_path = openWithBrowser("IE", internetExplorer_path,
							LOCALHOST, html5List.getSelectedItem().substring(1));
					openWithBrowser("IE", internetExplorer_path, QA2HOST, html5List
							.getSelectedItem().substring(15));
				} catch (NullPointerException e2) {
					printWarning("Please select a file");
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

	public void setBackupPath(String backupPath) {

		File loginFile = new File(backupPath
				+ "\\web\\tradecard\\en\\administration\\login.jsp");
		try {

			if (loginFile.exists()) {
				this.backupPath = backupPath;
				backupLocationField.setText(backupPath);
				isBackupValid = true;
				printOnConsole("Backup path changed.");
			} else {
				printOnConsole("Error: Backup directory file path seems to be incorrect.\n Resetting back to default path.\n Consider moving the files to : "
						+ DEFAULT_BACKUP_PATH);
				isBackupValid = false;
			}
		} catch (Exception e1) {
			printOnConsole(e1.getMessage());
			printOnConsole(printStacktrace(e1));
		}
	}

	public void loadToListFromFile() {

		// clear list
		preHTML5List.removeAll();
		html5List.removeAll();

		ArrayList<String> fileList = readCSV();
		showProgressBar();
		int fileCount = 0;
		for (int i = 0; i < fileList.size(); i++) {
			if (checkFile(fileList.get(i))) {
				preHTML5List.add(fileList.get(i));
				fileCount++;
			} else
				printWarning(fileList.get(i) + " File Doesn't exist.");
			setProgressBarValue(fileList.size(), i);

		}

		printOnConsole(fileCount + " new files Loaded.");
		printPreHtml5Count();
	}

	public void printPreHtml5Count() {
		printOnConsole("Total of " + preHTML5List.getItemCount()
				+ " pre-HTML5 files exist.");
	}

	public void printHtml5Count() {
		printOnConsole("Total of " + html5List.getItemCount()
				+ " HTML5 files exist.");
	}

	public void printWarning(String message) {
		printOnConsole("Warning! : " + message);
	}

	public boolean checkFile(String filePathString) {

		File f = new File(basePath + filePathString);
		return (f.exists() && !f.isDirectory());
	}

	public boolean checkBackup(String filePathString) {

		File f = new File(backupPath + filePathString);
		printOnConsole(backupPath + filePathString);
		return (f.exists() && !f.isDirectory());
	}

	public void convertToHTML5(String sourceFile) {

		try {
			if (isBackupValid && checkBackup(sourceFile)) {
				JerichoJspParserUtil.clearConsoleWriter();
				JerichoJspParserUtil.convertToHTML5(
							formatFilePath(sourceFile), false);
				printOnConsole(JerichoJspParserUtil.getDebuggerOutput());
				html4ToHtml5(sourceFile);
			} else {
				printWarning("Backup path is not correct!");
				printOnConsole("Conversion aborted.");
			}

		} catch(HTML5ParserException ex){
			ex.printStackTrace();
			printWarning(ex.getType());
			printOnConsole(ex.getMessage());
			printOnConsole(ex.getTagInfo());
			dbLogger.logError(formatFilePath(sourceFile), ex.getType(), ex.getMessage(), ex.getTagInfo());
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	public void revertBack(String convertedFilePath) {

		try {
			if (isBackupValid && checkBackup(convertedFilePath)) {
				JerichoJspParserUtil.clearConsoleWriter();
				RevertBackChanges.revertChanges(
						formatFilePath(convertedFilePath), backupPath);
				printOnConsole(JerichoJspParserUtil.getDebuggerOutput());
				html5ToHtml4(convertedFilePath);

			}
		} catch (Exception e) {
			printOnConsole(e.getMessage());
			printOnConsole(printStacktrace(e));

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

	public void printOnConsole(String message) {
		notificationArea.append(message + "\n");
	}

	public static String printStacktrace(Exception e) {
		StringWriter w = new StringWriter();
		e.printStackTrace(new PrintWriter(w));
		return w.toString();
	}

	public String formatFilePath(String fileName) {
		return basePath + fileName;
	}

	public String getBackupFilePath(String fileName) {
		return backupPath + fileName;
	}

	/*
	 * returns the absolute file path of an executable file. provides a file
	 * chooser to select the file.
	 */
	public String readFilePaths() {
		try {
			JFileChooser chooser = new JFileChooser();

			chooser.addChoosableFileFilter(new FileNameExtensionFilter(
					"Executable Files", "exe"));
			chooser.setAcceptAllFileFilterUsed(false);
			int returnVal = chooser.showOpenDialog(new Frame());
			if (returnVal == JFileChooser.FILES_ONLY) {

				return chooser.getSelectedFile().getAbsolutePath();
			}
		} catch (Exception ex) {

		}
		return null;
	}

	public String getFileName(String path) {
		return path.substring(path.lastIndexOf("\\"));
	}

	/*
	 * Replace/Append a value in/to the config file. elements are in xml format.
	 * If the tag doesn't exist, program will append it to the file. else will
	 * replace the value.
	 * 
	 * @param-newPath : the new value that should replace/should be appended.
	 * 
	 * @param-tag : tag name
	 */
	public void appendPathToConfigFile(String newPath, String tag) {
		String fileContent = readFromConfigFile();
		try {

			String currentLine = fileContent.substring(
					fileContent.indexOf(tag),
					fileContent.indexOf(getEndingTag(tag)) + tag.length() + 1);
			fileContent = fileContent.substring(0,
					fileContent.indexOf(currentLine))
					+ getAsXmlValue(newPath, tag)
					+ fileContent.substring(fileContent.indexOf(currentLine)
							+ currentLine.length(), fileContent.length());

		} catch (StringIndexOutOfBoundsException e) {
			fileContent = readFromConfigFile();
			fileContent += getAsXmlValue(newPath, tag);
		}
		try {
			PrintWriter writer = new PrintWriter(CONFIG_FILE);
			writer.println(fileContent);
			writer.close();

		} catch (FileNotFoundException e) {

			printOnConsole("Creating Config file.");
		}
	}

	public String getAsXmlValue(String value, String tag) {
		return tag + value + getEndingTag(tag);
	}

	public String getEndingTag(String tag) {
		return tag.substring(0, 1) + "/" + tag.substring(1, tag.length());
	}

	/*
	 * Opens a file using Dreamweaver
	 */
	public void openDreamweaver(String path) {
		ProcessBuilder pb = new ProcessBuilder(dreamweaver_path, path);
		try {
			pb.start();
		} catch (IOException ex) {
			printOnConsole(ex.getMessage());
			if (ex.getMessage().contains(
					"The system cannot find the file specified")) {

				String directoryInConfigFile = extractValue("<DREAMWEAVER>",
						"</DREAMWEAVER>");
				try {
					if (directoryInConfigFile != null
							&& !directoryInConfigFile.equals(dreamweaver_path))
						dreamweaver_path = directoryInConfigFile;
					else {
						dreamweaver_path = readFilePaths();
						appendPathToConfigFile(dreamweaver_path,
								"<DREAMWEAVER>");
					}
					openDreamweaver(path);
				} catch (NullPointerException ex2) {
					dreamweaver_path = readFilePaths();
					appendPathToConfigFile(dreamweaver_path, "<DREAMWEAVER>");
					openDreamweaver(path);
				}
			}

		}
	}

	public void openAraxis() {

		ProcessBuilder pb = new ProcessBuilder(araxis_path, backupPath
				+ html5List.getSelectedItem(),
				formatFilePath(html5List.getSelectedItem()));
		try {
			pb.start();
		} catch (IOException ex) {
			printOnConsole(ex.getMessage());
			if (ex.getMessage().contains(
					"The system cannot find the file specified")) {

				String directoryInConfigFile = extractValue("<ARAXIS>",
						"</ARAXIS>");
				try {
					if (directoryInConfigFile != null
							&& !directoryInConfigFile.equals(araxis_path))
						araxis_path = directoryInConfigFile;
					else {
						araxis_path = readFilePaths();
						appendPathToConfigFile(araxis_path, "<ARAXIS>");
					}
					openAraxis();
				} catch (NullPointerException ex2) {
					araxis_path = readFilePaths();
					appendPathToConfigFile(araxis_path, "<ARAXIS>");
					openAraxis();
				}
			}

		}
	}

	public String extractValue(String key, String ending) {
		try {
			return readFromConfigFile().substring(
					readFromConfigFile().indexOf(key) + key.length(),
					readFromConfigFile().indexOf(ending));
		} catch (StringIndexOutOfBoundsException e) {
			return null;
		}
	}

	public String readFromConfigFile() {
		StringBuilder fileContent = new StringBuilder();
		FileReader configFile = null;
		try {
			configFile = new FileReader(CONFIG_FILE);
		} catch (FileNotFoundException e) {
			File newConfigFile = new File(CONFIG_FILE);
			newConfigFile.mkdir();
			try {
				newConfigFile.createNewFile();
			} catch (IOException e2) {
				printOnConsole("ERROR: Config file not found. Creating a new config file failed. Consider creating it manually.");
			}
		}
		BufferedReader br = null;
		try {

			if (configFile != null) {

				br = new BufferedReader(configFile);

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					fileContent.append(sCurrentLine);
				}

				configFile.close();

			}

		} catch (IOException e2) {

			e2.printStackTrace();

		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
		}
		return fileContent.toString();

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

	public String openWithBrowser(String browser, String browserPath,
			String host, String file) {
		ProcessBuilder pb = new ProcessBuilder(browserPath, host + file);
		try {
			pb.start();
		} catch (IOException ex) {
			printOnConsole(ex.getMessage());
			if (ex.getMessage().contains(
					"The system cannot find the file specified")) {

				String directoryInConfigFile = extractValue(
						"<" + browser + ">", "</" + browser + ">");
				try {
					if (directoryInConfigFile != null
							&& !directoryInConfigFile.equals(browserPath))
						browserPath = directoryInConfigFile;
					else {
						browserPath = readFilePaths();
						appendPathToConfigFile(browserPath, "<" + browser + ">");
						openWithBrowser(browser, browserPath, host, file);
					}

				} catch (NullPointerException ex2) {
					browserPath = readFilePaths();
					appendPathToConfigFile(browserPath, "<" + browser + ">");
					openWithBrowser(browser, browserPath, host, file);
				}
			}

		}
		return browserPath;
	}
}
