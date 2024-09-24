package nsu_laboratory.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class PeerInfoHandler {
    static private final Logger LOGGER = LogManager.getLogger("PEER_HANDLER");

    public static ArrayList<PeerInfo> peerHandle() {
        ArrayList<PeerInfo> peers = new ArrayList<>();
        PeerInfo peer1 = new PeerInfo("localhost", 8080);
        PeerInfo peer2 = new PeerInfo("localhost", 8081);
        PeerInfo peer3 = new PeerInfo("localhost", 8082);
        int piecesPerPeer = Utility.SIZE / 2;
        int remainingPieces = Utility.SIZE % 2;
        for (int i = 0; i < piecesPerPeer; i++) {
            peer2.setBitByIndex(i);
            peer3.setBitByIndex(i + piecesPerPeer);
        }
        for (int i = 0; i < remainingPieces; i++) {
            peer3.setBitByIndex(Utility.SIZE - remainingPieces + i);
        }
        LOGGER.debug("PeerInfo - pieces: 1 - {} | 2 - {} | 3 - {}", peer1.getCountOfPieces(), peer2.getCountOfPieces(), peer3.getCountOfPieces());
        LOGGER.debug("PeerInfo - bitset: 1 - {} | 2 - {} | 3 - {}", peer1.getBitfield(), peer2.getBitfield(), peer3.getBitfield());
        LOGGER.debug("PeerInfo - hash: \n1 - {}\n2 - {}\n3 - {}", peer1.getPeerId(), peer2.getPeerId(), peer3.getPeerId());
        peers.add(peer1);
        peers.add(peer2);
        peers.add(peer3);
        for (PeerInfo PeerInfo : peers) {
            if (PeerInfo.getPort() == Utility.PORT) {
                LOGGER.debug("Bitfield for {} is {}", Utility.PORT, PeerInfo.getBitfield());
                Utility.setPeerHash(PeerInfo.getPeerId());
                Utility.updateBitfield(PeerInfo.getBitfield());
                PeerInfo.setCondition(PeerCondition.UNUSED);
                try {
                    Utility.downloadOurPieces();
                } catch (IOException e) {
                    LOGGER.error("Couldnt download for myself");
                }
                break;
            }
        }
        return peers;
    }
}
