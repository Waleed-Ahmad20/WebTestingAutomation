package BL;

import DAL.DAOFacade;
import DTO.DTO;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.ibm.icu.text.Transliterator;

public class BO implements BOFacade {

    private DAOFacade DAOFacade;

    public BO(DAOFacade DAOFacade) {
        this.DAOFacade = DAOFacade;
    }

    @Override
    public void createFile(String fileName, String content) throws SQLIntegrityConstraintViolationException {
        String fileHash = hashContent(content);
        if (DAOFacade.isDuplicateContent(fileHash)) {
            throw new SQLIntegrityConstraintViolationException("Duplicate file content.");
        }
        DTO fileDTO = new DTO(fileName, content, LocalDate.now(), LocalDate.now(), fileHash);
        List<String> pages = paginateContent(content);
        DAOFacade.createFile(fileDTO, pages);
    }

    @Override
    public List<String> getPages(String fileName) {
        DTO fileDTO = DAOFacade.openFile(fileName);
        if (fileDTO != null) {
            return DAOFacade.getPages(fileDTO.getId());
        }
        return new ArrayList<>();
    }

    @Override
    public boolean deleteFile(String fileName) {
        DTO fileDTO = DAOFacade.openFile(fileName);
        if (fileDTO != null) {
            return DAOFacade.deleteFile(fileDTO);
        }
        return false;
    }

    @Override
    public void updateFile(String fileName, String updatedContent) throws SQLException {
        DTO fileDTO = DAOFacade.openFile(fileName);
        if (fileDTO != null) {
            String fileHash = hashContent(updatedContent);
            if (DAOFacade.isDuplicateContent(fileHash)) {
                throw new RuntimeException("Duplicate file content.");
            }
            fileDTO.setContent(updatedContent);
            fileDTO.setLastModified(LocalDate.now());
            fileDTO.setFileHash(fileHash);
            List<String> pages = paginateContent(updatedContent);
            DAOFacade.updateFile(fileDTO);
            DAOFacade.updatePages(fileDTO.getId(), pages);
        }
    }

    @Override
    public DTO openFile(String fileName) {
        return DAOFacade.openFile(fileName);
    }

    @Override
    public List<DTO> getAllFiles() {
        return DAOFacade.getAllFiles();
    }

    private List<String> paginateContent(String content) {
        int pageSize = 500; 
        List<String> pages = new ArrayList<>();
        for (int start = 0; start < content.length(); start += pageSize) {
            pages.add(content.substring(start, Math.min(content.length(), start + pageSize)));
        }
        return pages;
    }
    public List<DTO> searchFilesByTerm(String searchTerm) {
        return DAOFacade.findFilesContainingTerm(searchTerm);
    }


    public String hashContent(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing content", e);
        }
    }
    
    public void transliterateFileContent(String currentFile, String fileContent) throws SQLException
    {
    	
    	String customRules = "ا > a;" + "ب > b;" + "ت > t;" + "ث > th;" + "ج > j;" + "ح > H;" +
                "خ > kh;" + "د > d;" + "ذ > dh;" + "ر > r;" + "ز > z;" + "س > s;" + "ش > sh;" + "ص > S;" +
	              "ض > D;" + "ط > T;" + "ظ > DH;" +"ع > a;" + "غ > gh;" + "ف > f;" + "ق > q;" + "ك > k;" + "ل > l;" +
	              "م > m;" + "ن > n;" + "ه > h;" + "\\0629 > h;" + "و > w;" + "ي > y;" + "ى > y;"+ "ء > ‘;"  + "\\u064E > a;" + "\\u064F > u;" + 
	              "\\u0650 > i;" + "\\u0653 > aa;" + "\\u0670 > aa;" + "أ > A";
    	Transliterator arabicToRomanEng = Transliterator.createFromRules("customTransliteration", customRules, Transliterator.FORWARD);
    	
    	String diacriticsToRemove = "[\u0651\u0652]";
    	
    	
		String newFileContent = fileContent.replaceAll(diacriticsToRemove,"");
    		String updatedContent = arabicToRomanEng.transliterate(newFileContent);
    		updateFile(currentFile, updatedContent);
    	}
}