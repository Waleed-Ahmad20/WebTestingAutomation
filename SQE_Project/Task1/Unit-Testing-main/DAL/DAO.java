package DAL;

import DTO.DTO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAO implements DAOFacade {

    @Override
    public boolean createFile(DTO DTO, List<String> pages) throws SQLIntegrityConstraintViolationException {
        String query = "INSERT INTO files (file_name, file_content, created_at, last_modified, file_hash) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, DTO.getFileName());
            stmt.setString(2, DTO.getContent());
            stmt.setDate(3, Date.valueOf(DTO.getCreatedAt()));
            stmt.setDate(4, Date.valueOf(DTO.getLastModified()));
            stmt.setString(5, DTO.getFileHash());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int fileId = generatedKeys.getInt(1);
                    insertPages(conn, fileId, pages);
                    return true;
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException("Duplicate file entry.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void insertPages(Connection conn, int fileId, List<String> pages) throws SQLException {
        String pageQuery = "INSERT INTO file_pages (file_id, page_number, page_content) VALUES (?, ?, ?)";
        try (PreparedStatement pageStmt = conn.prepareStatement(pageQuery)) {
            for (int i = 0; i < pages.size(); i++) {
                pageStmt.setInt(1, fileId);
                pageStmt.setInt(2, i + 1);
                pageStmt.setString(3, pages.get(i));
                pageStmt.addBatch();
            }
            pageStmt.executeBatch();
        }
    }
    
//    @Override
//    public List<String> getPages(int fileId) {
//        List<String> pages = new ArrayList<>();
//        String query = "SELECT page_content FROM file_pages WHERE file_id = ? ORDER BY page_number";
//        try (Connection conn = DatabaseUtility.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//            stmt.setInt(1, fileId);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                pages.add(rs.getString("page_content"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return pages;
//    }
    
    @Override
    public List<String> getPages(int fileId) {
        List<String> pages = new ArrayList<>();
        String query = "SELECT page_content FROM file_pages WHERE file_id = ? ORDER BY page_number";
        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, fileId);
            ResultSet rs = stmt.executeQuery();
            System.out.println("yoyo");
            while (rs.next()) {
                System.out.println("yoyo1");
                String pageContent = rs.getString("page_content");
                System.out.println("Fetched page content: " + pageContent); 
                pages.add(pageContent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pages;
    }

    @Override
public boolean deleteFile(DTO DTO) {
    String deletePagesQuery = "DELETE FROM file_pages WHERE file_id = ?";
    String deleteFileQuery = "DELETE FROM files WHERE file_name = ?";
    try (Connection conn = DatabaseUtility.getConnection();
         PreparedStatement deletePagesStmt = conn.prepareStatement(deletePagesQuery);
         PreparedStatement deleteFileStmt = conn.prepareStatement(deleteFileQuery)) {
        
        deletePagesStmt.setInt(1, DTO.getId());
        deletePagesStmt.executeUpdate();
        
        deleteFileStmt.setString(1, DTO.getFileName());
        return deleteFileStmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    @Override
    public boolean updateFile(DTO DTO) {
        String query = "UPDATE files SET file_content = ?, last_modified = ?, file_hash = ? WHERE file_name = ?";
        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, DTO.getContent());
            stmt.setDate(2, Date.valueOf(DTO.getLastModified()));
            stmt.setString(3, DTO.getFileHash());
            stmt.setString(4, DTO.getFileName());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public DTO openFile(String fileName) {
        String query = "SELECT * FROM files WHERE file_name = ?";
        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fileName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int fileId = rs.getInt("file_id");
                String content = rs.getString("file_content");
                LocalDate createdAt = rs.getDate("created_at").toLocalDate();
                LocalDate lastModified = rs.getDate("last_modified").toLocalDate();
                String fileHash = rs.getString("file_hash");
                return new DTO(fileId, fileName, content, createdAt, lastModified, fileHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DTO> getAllFiles() {
        List<DTO> files = new ArrayList<>();
        String query = "SELECT * FROM files";
        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int fileId = rs.getInt("file_id");
                String fileName = rs.getString("file_name");
                String content = rs.getString("file_content");
                LocalDate createdAt = rs.getDate("created_at").toLocalDate();
                LocalDate lastModified = rs.getDate("last_modified").toLocalDate();
                String fileHash = rs.getString("file_hash");
                files.add(new DTO(fileId, fileName, content, createdAt, lastModified, fileHash));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }

    @Override
    public boolean isDuplicateContent(String fileHash) {
        String query = "SELECT COUNT(*) FROM files WHERE file_hash = ?";
        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fileHash);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<DTO> findFilesContainingTerm(String searchTerm) {
        List<DTO> matchedFiles = new ArrayList<>();
        
        try (Connection conn = DatabaseUtility.getConnection()) {
            String query = "SELECT file_id, file_name, file_content FROM files WHERE file_content LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int Id = rs.getInt("file_id");
                String fileName = rs.getString("file_name");
                String content = rs.getString("file_content");
                DTO file = new DTO(Id, fileName, content); // Use the new constructor
                matchedFiles.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return matchedFiles;
    }


    @Override
    public void updatePages(int fileId, List<String> pages) throws SQLException {
        String deleteQuery = "DELETE FROM file_pages WHERE file_id = ?";
        try (Connection conn = DatabaseUtility.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, fileId);
            deleteStmt.executeUpdate();
        }
        insertPages(DatabaseUtility.getConnection(), fileId, pages);
    }
}