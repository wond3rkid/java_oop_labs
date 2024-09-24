import nsu_laboratory.utils.BencodeParser;
import nsu_laboratory.utils.Utility;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BencoderTest {
    private File file = new File("src/main/resources/DieTriffids.txt.torrent").getAbsoluteFile();
    BencodeParser parser = new BencodeParser(file);

    @Test
    public void testFileName() {
        assertEquals("DieTriffids.txt", parser.getFileName());
    }

    @Test
    public void testFileSize() {
        long expected = 445580;
        assertEquals(expected, parser.getFileLength());
    }

    @Test
    public void testPieceLength() {
        int expected = 16 * 1024;
        assertEquals(expected, parser.getPieceLength());
    }

    @Test
    public void testLastPieceLength() {
        long expected = 3212;
        assertEquals(expected, parser.getCurrentPieceLength(27));
    }

    @Test
    public void testPieceCount() {
        int expected = 28;
        assertEquals(expected, parser.getNumberOfPieces());
    }

    @Test
    public void testCreator() {
        String expected = "kimbatt.github.io/torrent-creator";
        assertEquals(expected, parser.getCreatedBy());
    }

    @Test
    public void testComment() {
        String expected = null;
        assertEquals(expected, parser.getComment());
    }

    @Test
    public void testHashesSize() {
        int expected = 28;
        assertEquals(expected, parser.getHashesSize());
    }

    @Test
    public void test() {
        for (int i = 0; i< parser.getNumberOfPieces(); i++) {
            System.out.printf("Current ind:%d, %s", i, Arrays.toString(parser.getPieceHashes(i)));
        }
    }
}
