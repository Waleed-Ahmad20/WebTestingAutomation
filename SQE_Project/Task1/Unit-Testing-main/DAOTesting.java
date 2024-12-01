//package DAL;

import DTO.DTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import DAL.DAO;
import DAL.DatabaseUtility;

import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DAOTesting {

    private DAO dao;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        dao = new DAO();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        DatabaseUtility.setConnection(mockConnection);
    }

    @Test
    void testCreateFile() throws SQLException {
        DTO dto = new DTO(1, "testFile", "content", LocalDate.now(), LocalDate.now(), "hash");
        List<String> pages = Arrays.asList("page1", "page2");

        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        boolean result = dao.createFile(dto, pages);

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
    

//    @Test
//    void testGetPages() throws SQLException {
//        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
//        when(mockResultSet.next()).thenReturn(true, true, false);
//        when(mockResultSet.getString("page_content")).thenReturn("page1", "page2");
//
//        List<String> pages = dao.getPages(1);
//
//        assertEquals(2, pages.size());
//        assertEquals("page1", pages.get(0));
//        assertEquals("page2", pages.get(1));
//    }

//    @Test
//    void testGetPages() throws SQLException {
//        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
//        when(mockResultSet.next()).thenReturn(true, true, false);
//        when(mockResultSet.getString("page_content")).thenReturn("page1", "page2");
//
//        List<String> pages = dao.getPages(1);
//
//          System.out.println("Pages size: " + pages.size());
//          for (String page : pages) {
//              System.out.println("Page content: " + page);
//          }
//
//        assertEquals(2, pages.size());
//        assertEquals("page1", pages.get(0));
//        assertEquals("page2", pages.get(1));
//    }
    
    @Test
    void testGetPages() throws SQLException {
    	when(DatabaseUtility.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getString("page_content")).thenReturn("page1", "page2");

        List<String> pages = dao.getPages(1);
        
        assertEquals(2, pages.size());
        assertEquals("page1", pages.get(0));
        assertEquals("page2", pages.get(1));
    }
    
    @Test
    void testDeleteFile() throws SQLException {
        DTO dto = new DTO(1, "testFile", "content", LocalDate.now(), LocalDate.now(), "hash");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = dao.deleteFile(dto);

        assertTrue(result);
        verify(mockPreparedStatement, times(2)).executeUpdate();
    }

    
    @Test
    void testUpdateFile() throws SQLException {
        DTO dto = new DTO(1, "testFile", "updatedContent", LocalDate.now(), LocalDate.now(), "hash");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = dao.updateFile(dto);

        assertTrue(result);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
    
    
    @Test
    void testOpenFile() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("file_id")).thenReturn(1);
        when(mockResultSet.getString("file_name")).thenReturn("testFile");
        when(mockResultSet.getString("file_content")).thenReturn("content");
        when(mockResultSet.getDate("created_at")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getDate("last_modified")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getString("file_hash")).thenReturn("hash");

        DTO dto = dao.openFile("testFile");

        assertNotNull(dto);
        assertEquals("testFile", dto.getFileName());
    }

    @Test
    void testGetAllFiles() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("file_id")).thenReturn(1);
        when(mockResultSet.getString("file_name")).thenReturn("testFile");
        when(mockResultSet.getString("file_content")).thenReturn("content");
        when(mockResultSet.getDate("created_at")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getDate("last_modified")).thenReturn(Date.valueOf(LocalDate.now()));
        when(mockResultSet.getString("file_hash")).thenReturn("hash");

        List<DTO> files = dao.getAllFiles();

        assertEquals(1, files.size());
        assertEquals("testFile", files.get(0).getFileName());
    }

    
    @Test
    void testIsDuplicateContent() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        boolean result = dao.isDuplicateContent("hash");

        assertTrue(result);
    }

    @Test
    void testFindFilesContainingTerm() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("file_id")).thenReturn(1);
        when(mockResultSet.getString("file_name")).thenReturn("testFile");
        when(mockResultSet.getString("file_content")).thenReturn("content");

        List<DTO> files = dao.findFilesContainingTerm("content");

        assertEquals(1, files.size());
        assertEquals("testFile", files.get(0).getFileName());
    }
}