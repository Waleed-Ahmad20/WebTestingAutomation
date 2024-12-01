
import BL.BOFacade;
import DTO.DTO;
import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class POTesting {

    private FrameFixture window;
    private BOFacade mockBOFacade;
    private Robot robot;

    @BeforeEach
    void setUp() {
        mockBOFacade = mock(BOFacade.class);
        robot = BasicRobot.robotWithNewAwtHierarchy();
        PO frame = GuiActionRunner.execute(() -> new PO(mockBOFacade));
        window = new FrameFixture(robot, frame);
        window.show(); 
    }

    @AfterEach
    void tearDown() {
        window.cleanUp();
    }

    @Test
    void testCreateFileAction() throws SQLIntegrityConstraintViolationException {
        when(mockBOFacade.isDuplicateContent(anyString())).thenReturn(false);
        doNothing().when(mockBOFacade).createFile(anyString(), anyString());

        window.comboBox("menuOptions").selectItem("Create .txt File");
        window.dialog().textBox().enterText("testFile");
        window.dialog().button("OK").click();

        verify(mockBOFacade, times(1)).createFile(eq("testFile"), eq(""));
    }

    @Test
    void testDeleteFileAction() {
        DTO fileDTO = new DTO(1, "testFile", "content", LocalDate.now(), LocalDate.now(), "hash");
        when(mockBOFacade.openFile("testFile")).thenReturn(fileDTO);
        when(mockBOFacade.deleteFile(fileDTO)).thenReturn(true);

        window.comboBox("menuOptions").selectItem("Delete .txt File");

        verify(mockBOFacade, times(1)).deleteFile(fileDTO);
    }

    @Test
    void testUpdateFileAction() throws SQLException {
        DTO fileDTO = new DTO(1, "testFile", "content", LocalDate.now(), LocalDate.now(), "hash");
        when(mockBOFacade.openFile("testFile")).thenReturn(fileDTO);
        when(mockBOFacade.isDuplicateContent(anyString())).thenReturn(false);
        doNothing().when(mockBOFacade).updateFile(any(DTO.class));
        doNothing().when(mockBOFacade).updatePages(anyInt(), anyList());

        window.comboBox("menuOptions").selectItem("Update .txt File");
        window.textBox().enterText("Updated content.");
        window.button("OK").click();

        verify(mockBOFacade, times(1)).updateFile(any(DTO.class));
        verify(mockBOFacade, times(1)).updatePages(anyInt(), anyList());
    }

    @Test
    void testOpenFileAction() {
        DTO fileDTO = new DTO(1, "testFile", "content", LocalDate.now(), LocalDate.now(), "hash");
        when(mockBOFacade.openFile("testFile")).thenReturn(fileDTO);

        window.comboBox("viewMenu").selectItem("Open .txt file");
        window.dialog().textBox().enterText("testFile");
        window.dialog().button("OK").click();

        verify(mockBOFacade, times(1)).openFile("testFile");
    }

    @Test
    void testViewAllFilesAction() {
        DTO fileDTO1 = new DTO(1, "file1", "content1", LocalDate.now(), LocalDate.now(), "hash1");
        DTO fileDTO2 = new DTO(2, "file2", "content2", LocalDate.now(), LocalDate.now(), "hash2");

        when(mockBOFacade.getAllFiles()).thenReturn(Arrays.asList(fileDTO1, fileDTO2));

        window.comboBox("viewMenu").selectItem("View all .txt file");

        verify(mockBOFacade, times(1)).getAllFiles();
    }

    @Test
    void testSearchWordAction() {
        String searchTerm = "test";
        DTO fileDTO1 = new DTO(1, "file1", "content1", LocalDate.now(), LocalDate.now(), "hash1");
        DTO fileDTO2 = new DTO(2, "file2", "content2", LocalDate.now(), LocalDate.now(), "hash2");

        when(mockBOFacade.findFilesContainingTerm(searchTerm)).thenReturn(Arrays.asList(fileDTO1, fileDTO2));

        window.button("Search Word").click();
        window.dialog().textBox().enterText(searchTerm);
        window.dialog().button("OK").click();

        verify(mockBOFacade, times(1)).findFilesContainingTerm(searchTerm);
    }

    @Test
    void testTransliterateFileAction() throws SQLException {
        String currentFile = "testFile";
        String fileContent = "content";
        DTO fileDTO = new DTO(1, currentFile, fileContent, LocalDate.now(), LocalDate.now(), "hash");

        when(mockBOFacade.openFile(currentFile)).thenReturn(fileDTO);
        doNothing().when(mockBOFacade).updateFile(any(DTO.class));
        doNothing().when(mockBOFacade).updatePages(anyInt(), anyList());

        window.comboBox("menuOptions").selectItem("Transliterate .txt File");

        verify(mockBOFacade, times(1)).transliterateFileContent(eq(currentFile), eq(fileContent));
    }
}