import nsu_laboratory.utils.Utility;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UtilityTest {
    @Test
    public void testFileTorrent() {
        File file = new File(Utility.TORRENT_PATH);
        System.out.println(file.getAbsolutePath());
        assertNotEquals(file, null);
    }

    @Test
    public void testSize() {
        int expected = 28;
        assertEquals(expected, Utility.SIZE);
    }
}
