package DAL;

import DTO.DTO;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface DAOFacade {
    boolean createFile(DTO DTO, List<String> pages) throws SQLIntegrityConstraintViolationException;
    boolean deleteFile(DTO DTO);
    boolean updateFile(DTO DTO);
    DTO openFile(String fileName);
    List<DTO> getAllFiles();
    boolean isDuplicateContent(String fileHash);
    List<String> getPages(int fileId);
    void updatePages(int fileId, List<String> pages) throws SQLException; 
    public List<DTO> findFilesContainingTerm(String searchTerm);
}