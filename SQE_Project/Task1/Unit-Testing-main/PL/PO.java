package PL;
import BL.BOFacade;
import DTO.DTO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class PO extends JFrame {
    private JComboBox<String> menuOptions;
    private JComboBox<String> viewMenu;
    private JTextArea text;
    private BOFacade BO;
    private String current_file_name;
    private File selectedFile;
    private int currentPage = 1;
    private List<String> pages = new ArrayList<>();

    public PO(BOFacade BO) {
        this.BO = BO;
        this.current_file_name = null;

        setTitle("Simple Text Editor");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        menuOptions = new JComboBox<>();
        menuOptions.addItem("File");
        menuOptions.addItem("Import .txt File");
        menuOptions.addItem("Create .txt File");
        menuOptions.addItem("Delete .txt File");
        menuOptions.addItem("Update .txt File");
        menuOptions.addItem("Transliterate .txt File");
        
        viewMenu = new JComboBox<>();
        viewMenu.addItem("View");
        viewMenu.addItem("Open .txt file");
        viewMenu.addItem("View all .txt file");
        
        JButton searchWord = new JButton("Search Word");
         
        menuOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) menuOptions.getSelectedItem();
                switch (selected) {
                    case "Import .txt File":
                        importFileAction();
                        break;
                    case "Create .txt File":
                        try {
                            createFileAction();
                        } catch (SQLIntegrityConstraintViolationException ex) {
                            JOptionPane.showMessageDialog(null, "Duplicate File Names Not Allowed!");
                        } catch (RuntimeException ex) {
                            JOptionPane.showMessageDialog(null, "Duplicate File Content Not Allowed!");
                        }
                        break;
                    case "Delete .txt File":
                        deleteFileAction();
                        break;
                    case "Update .txt File":
                        try {
                            updateFileAction();
                        } catch (RuntimeException ex) {
                            JOptionPane.showMessageDialog(null, "Duplicate File Content Not Allowed!");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error updating file: " + ex.getMessage());
                        }
                        break;
                    case "Transliterate .txt File":
                    	try {
                             transliterateFileAction();
                        } catch (RuntimeException ex) {
                             JOptionPane.showMessageDialog(null, "Duplicate File Content Not Allowed!");
                        } catch (SQLException ex) {
                             JOptionPane.showMessageDialog(null, "Error Transliterating File Content: " + ex.getMessage());
                        }	
                }
            }
        });
        
        viewMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) viewMenu.getSelectedItem();
                if ("Open .txt file".equals(selected)) {
                    openFileAction();
                } else if ("View all .txt file".equals(selected)) {
                    viewAllFilesAction();
                }
            }
        });
        
        searchWord.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				searchWordAction();
			}
        	
        });

        topPanel.add(menuOptions);
        topPanel.add(viewMenu);
        topPanel.add(searchWord);
        
        text = new JTextArea();
        text.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(text);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addPaginationButtons(bottomPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void importFileAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            if (!selectedFile.getName().endsWith(".txt")) {
                JOptionPane.showMessageDialog(this, "Only .txt files are allowed!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String fileContent = new String(java.nio.file.Files.readAllBytes(selectedFile.toPath()));

                text.setEnabled(false);
                try {
                    BO.createFile(selectedFile.getName(), fileContent);
                    JOptionPane.showMessageDialog(this, "File Imported Successfully: " + selectedFile.getName());
                } catch (SQLIntegrityConstraintViolationException ex) {
                    JOptionPane.showMessageDialog(this, "Duplicate File Content Not Allowed!");
                } catch (RuntimeException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createFileAction() throws SQLIntegrityConstraintViolationException {
        String fileName = JOptionPane.showInputDialog(this, "File Name:");
        if (fileName == null) {
            return;
        }

        if (fileName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "File Name Cannot Be Empty!");
        } else {
            try {
                BO.createFile(fileName, "");
                JOptionPane.showMessageDialog(this, "File Created Successfully: " + fileName);
                openFile(fileName);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "Duplicate File Content Not Allowed!");
            }
        }
    }

    private void deleteFileAction() {
        String fileName = current_file_name;
        if (fileName != null) {
            boolean isDeleted = BO.deleteFile(fileName);
            if (isDeleted) {
                if (fileName.equals(current_file_name)) {
                    current_file_name = null;
                    text.setText("");
                    text.setEnabled(false);
                }
                JOptionPane.showMessageDialog(this, "File Deleted: " + fileName);
            } else {
                JOptionPane.showMessageDialog(this, "File not found: " + fileName);
            }
        }
        else {
        	JOptionPane.showMessageDialog(this, "No Opened File to Delete!");
        }	
    }

    private void updateFileAction() throws SQLException {
        if (current_file_name != null) {
            String updatedContent = text.getText();
            try {
                BO.updateFile(current_file_name, updatedContent);
                JOptionPane.showMessageDialog(this, "File Updated Successfully!");
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "Duplicate File Content Not Allowed!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No Opened File to Update!");
        }
    }

    private void openFileAction() {
        String fileName = JOptionPane.showInputDialog(this, "Enter File Name to Open:");
        if (fileName == null) {
            return;
        }
        if (fileName != null && !fileName.trim().isEmpty()) {
            boolean fileExists = openFile(fileName);
            if (fileExists) {
            	if(fileName.equals(current_file_name)){
           		 JOptionPane.showMessageDialog(this, "File is Already Opened: " + fileName);
           	}else {
           		current_file_name = fileName;
           		pages = BO.getPages(fileName);
                   currentPage = 1;
                   displayPage(currentPage);
                   JOptionPane.showMessageDialog(this, "File Opened Successfully: " + fileName);
           	}
            } else {
                JOptionPane.showMessageDialog(this, "File does not Exist: " + fileName);
            }
        } else if (fileName != null) {
            JOptionPane.showMessageDialog(this, "File Name Cannot Be Empty!");
        }
    }

    private void viewAllFilesAction() {
        List<DTO> files = BO.getAllFiles();
        if (files.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No files available.");
            return;
        }

        String[] columnNames = { "File Name", "Created At", "Last Modified" };
        Object[][] data = new Object[files.size()][3];
        for (int i = 0; i < files.size(); i++) {
            data[i][0] = files.get(i).getFileName();
            data[i][1] = files.get(i).getCreatedAt().toString();
            data[i][2] = files.get(i).getLastModified().toString();
        }

        JDialog dialog = new JDialog(this, "All Files", true);
        dialog.setBounds(350, 300, 500, 300);

        JTable allFilesTable = new JTable(data, columnNames) {
            public boolean isTableEditable() {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(allFilesTable);
        dialog.add(scrollPane);

        allFilesTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = allFilesTable.getSelectedRow();
                if (selectedRow != -1) {
                    String fileName = (String) allFilesTable.getValueAt(selectedRow, 0);
                    boolean fileExists = openFile(fileName);
                    if (fileExists) {
                    	if(fileName.equals(current_file_name)){
                    		 JOptionPane.showMessageDialog(this, "File is Already Opened: " + fileName);
                    	}else {
                    		current_file_name = fileName;
                    		pages = BO.getPages(fileName);
                            currentPage = 1;
                            displayPage(currentPage);
                            JOptionPane.showMessageDialog(this, "File Opened Successfully: " + fileName);
                    	}
                    } else {
                        JOptionPane.showMessageDialog(this, "File does not Exist: " + fileName);
                    }
                    dialog.dispose();
                }
            }
        });

        dialog.setVisible(true);
    }

    private String getSnippetAroundTerm(String content, String term) {
        int index = content.toLowerCase().indexOf(term.toLowerCase());
        if (index == -1) {
            return "";
        }
        int start = Math.max(0, index - 20);
        int end = Math.min(content.length(), index + term.length() + 20);
        return "..." + content.substring(start, end) + "...";

}
    private void addPaginationButtons(JPanel panel) {
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("   Next   ");
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                displayPage(currentPage);
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPage < pages.size()) {
                currentPage++;
                displayPage(currentPage);
            }
        });
        
        panel.add(prevButton);
        panel.add(nextButton);
    }

    private void displayPage(int pageNumber) {
        if (pageNumber > 0 && pageNumber <= pages.size()) {
            text.setText(pages.get(pageNumber - 1));
            text.setEnabled(true);
        }
    }

    public boolean openFile(String fileName) {
        DTO file = BO.openFile(fileName);
        if (file != null) {
            text.setText(file.getContent());
            text.setEnabled(true);
            return true;
        } else {
            return false;
        }
    }
    
    public void transliterateFileAction() throws SQLException
    {
    	if(current_file_name != null) {
    		BO.transliterateFileContent(current_file_name, text.getText());
      	  pages = BO.getPages(current_file_name);
            currentPage = 1;
            displayPage(currentPage);
    	}else {
    		JOptionPane.showMessageDialog(this, "No Opened File to Transliterate!");
    	}
    	
    }
    
    private void searchWordAction() {
        String searchTerm = JOptionPane.showInputDialog(this, "Enter word to search:");
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            List<DTO> matchedFiles = BO.searchFilesByTerm(searchTerm);
            displayResultsInJTable(matchedFiles, searchTerm);
        } else if(searchTerm != null) {
        	JOptionPane.showMessageDialog(this, "Search Term Cannot be Empty!");
        }
    }
    
    private void displayResultsInJTable(List<DTO> files, String searchTerm) {
        if (files.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Files Found Containing the Term: " + searchTerm);
            return;
        }

        String[] columnNames = { "File ID", "File Name", "Snippet" };
        Object[][] data = new Object[files.size()][3];
        for (int i = 0; i < files.size(); i++) {
            DTO file = files.get(i);
            data[i][0] = file.getId();
            data[i][1] = file.getFileName();
            data[i][2] = getSnippetAroundTerm(file.getContent(), searchTerm);
        }

        JDialog dialog = new JDialog(this, "Search Results", true);
        dialog.setBounds(350, 300, 600, 300);

        JTable resultsTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        dialog.add(scrollPane);
        dialog.setVisible(true);
    }
}