package BL;

import DTO.DTO;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface BOFacade {
    void createFile(String fileName, String content) throws SQLIntegrityConstraintViolationException;
    List<String> getPages(String fileName);
    boolean deleteFile(String fileName);
    void updateFile(String fileName, String updatedContent) throws SQLException;
    DTO openFile(String fileName);
    List<DTO> getAllFiles();
    public List<DTO> searchFilesByTerm(String searchTerm);
    void transliterateFileContent(String currentFile, String fileContent) throws SQLException;

}