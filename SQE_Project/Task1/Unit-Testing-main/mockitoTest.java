import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class mockitoTest {
    @Test
    public void testMockito() {
        // Create a mock list
        List<String> mockList = mock(List.class);

        // Define behavior
        when(mockList.get(0)).thenReturn("Hello, Mockito!");

        // Use the mock object
        System.out.println(mockList.get(0)); // Should print "Hello, Mockito!"
    }
}