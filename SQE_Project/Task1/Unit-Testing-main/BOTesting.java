
import DAL.DAOFacade;
import DTO.DTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import BL.BO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BOTesting {

    private BO bo;
    private DAOFacade mockDAOFacade;

    @BeforeEach
    void setUp() {
        mockDAOFacade = mock(DAOFacade.class);
        bo = new BO(mockDAOFacade);
    }

    @Test
    void testCreateFile() throws SQLIntegrityConstraintViolationException {
        String fileName = "testFile";
        String content = "This is a test content.";
        String fileHash = bo.hashContent(content);

        when(mockDAOFacade.isDuplicateContent(fileHash)).thenReturn(false);
        doNothing().when(mockDAOFacade).createFile(any(DTO.class), anyList());

        bo.createFile(fileName, content);

        verify(mockDAOFacade, times(1)).createFile(any(DTO.class), anyList());
    }

    @Test
    void testCreateFileDuplicateContent() {
        String fileName = "testFile";
        String content = "This is a test content.";
        String fileHash = bo.hashContent(content);

        when(mockDAOFacade.isDuplicateContent(fileHash)).thenReturn(true);

        assertThrows(SQLIntegrityConstraintViolationException.class, () -> {
            bo.createFile(fileName, content);
        });
    }

    @Test
    void testGetPages() {
        String fileName = "testFile";
        DTO fileDTO = new DTO(1, fileName, "content", LocalDate.now(), LocalDate.now(), "hash");

        when(mockDAOFacade.openFile(fileName)).thenReturn(fileDTO);
        when(mockDAOFacade.getPages(fileDTO.getId())).thenReturn(Arrays.asList("page1", "page2"));

        List<String> pages = bo.getPages(fileName);

        assertEquals(2, pages.size());
        assertEquals("page1", pages.get(0));
        assertEquals("page2", pages.get(1));
    }

    @Test
    void testGetPagesFileNotFound() {
        String fileName = "testFile";

        when(mockDAOFacade.openFile(fileName)).thenReturn(null);

        List<String> pages = bo.getPages(fileName);

        assertTrue(pages.isEmpty());
    }

    @Test
    void testDeleteFile() {
        String fileName = "testFile";
        DTO fileDTO = new DTO(1, fileName, "content", LocalDate.now(), LocalDate.now(), "hash");

        when(mockDAOFacade.openFile(fileName)).thenReturn(fileDTO);
        when(mockDAOFacade.deleteFile(fileDTO)).thenReturn(true);

        boolean result = bo.deleteFile(fileName);

        assertTrue(result);
        verify(mockDAOFacade, times(1)).deleteFile(fileDTO);
    }

    @Test
    void testDeleteFileNotFound() {
        String fileName = "testFile";

        when(mockDAOFacade.openFile(fileName)).thenReturn(null);

        boolean result = bo.deleteFile(fileName);

        assertFalse(result);
    }

    @Test
    void testUpdateFile() throws SQLException {
        String fileName = "testFile";
        String updatedContent = "Updated content.";
        String fileHash = bo.hashContent(updatedContent);
        DTO fileDTO = new DTO(1, fileName, "content", LocalDate.now(), LocalDate.now(), "hash");

        when(mockDAOFacade.openFile(fileName)).thenReturn(fileDTO);
        when(mockDAOFacade.isDuplicateContent(fileHash)).thenReturn(false);
        doNothing().when(mockDAOFacade).updateFile(any(DTO.class));
        doNothing().when(mockDAOFacade).updatePages(anyInt(), anyList());

        bo.updateFile(fileName, updatedContent);

        verify(mockDAOFacade, times(1)).updateFile(any(DTO.class));
        verify(mockDAOFacade, times(1)).updatePages(anyInt(), anyList());
    }

    @Test
    void testUpdateFileDuplicateContent() throws SQLException {
        String fileName = "testFile";
        String updatedContent = "Updated content.";
        String fileHash = bo.hashContent(updatedContent);
        DTO fileDTO = new DTO(1, fileName, "content", LocalDate.now(), LocalDate.now(), "hash");

        when(mockDAOFacade.openFile(fileName)).thenReturn(fileDTO);
        when(mockDAOFacade.isDuplicateContent(fileHash)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> {
            bo.updateFile(fileName, updatedContent);
        });
    }

    @Test
    void testOpenFile() {
        String fileName = "testFile";
        DTO fileDTO = new DTO(1, fileName, "content", LocalDate.now(), LocalDate.now(), "hash");

        when(mockDAOFacade.openFile(fileName)).thenReturn(fileDTO);

        DTO result = bo.openFile(fileName);

        assertNotNull(result);
        assertEquals(fileName, result.getFileName());
    }

    @Test
    void testGetAllFiles() {
        DTO fileDTO1 = new DTO(1, "file1", "content1", LocalDate.now(), LocalDate.now(), "hash1");
        DTO fileDTO2 = new DTO(2, "file2", "content2", LocalDate.now(), LocalDate.now(), "hash2");

        when(mockDAOFacade.getAllFiles()).thenReturn(Arrays.asList(fileDTO1, fileDTO2));

        List<DTO> files = bo.getAllFiles();

        assertEquals(2, files.size());
        assertEquals("file1", files.get(0).getFileName());
        assertEquals("file2", files.get(1).getFileName());
    }

    @Test
    void testSearchFilesByTerm() {
        String searchTerm = "test";
        DTO fileDTO1 = new DTO(1, "file1", "content1", LocalDate.now(), LocalDate.now(), "hash1");
        DTO fileDTO2 = new DTO(2, "file2", "content2", LocalDate.now(), LocalDate.now(), "hash2");

        when(mockDAOFacade.findFilesContainingTerm(searchTerm)).thenReturn(Arrays.asList(fileDTO1, fileDTO2));

        List<DTO> files = bo.searchFilesByTerm(searchTerm);

        assertEquals(2, files.size());
        assertEquals("file1", files.get(0).getFileName());
        assertEquals("file2", files.get(1).getFileName());
    }

    @Test
    void testTransliterateFileContent() throws SQLException {
        String currentFile = "testFile";
        String fileContent = "content";
        String transliteratedContent = "transliteratedContent";
        DTO fileDTO = new DTO(1, currentFile, fileContent, LocalDate.now(), LocalDate.now(), "hash");

        when(mockDAOFacade.openFile(currentFile)).thenReturn(fileDTO);
        doNothing().when(mockDAOFacade).updateFile(any(DTO.class));
        doNothing().when(mockDAOFacade).updatePages(anyInt(), anyList());

        bo.transliterateFileContent(currentFile, fileContent);

        verify(mockDAOFacade, times(1)).updateFile(any(DTO.class));
        verify(mockDAOFacade, times(1)).updatePages(anyInt(), anyList());
    }
}