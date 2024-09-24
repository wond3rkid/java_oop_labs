package nsu_laboratory.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static nsu_laboratory.utils.Utility.SIZE;

public class BitsetMapper {
    Logger LOGGER = LogManager.getLogger("BitsetMapper");
    private final Map<Integer, List<Integer>> piecePortMap;

    public BitsetMapper() {
        this.piecePortMap = new HashMap<>();
    }

    public void add(BitSet bitSet, int port) {
        for (int i = 0; i < SIZE; i++) {
            if (bitSet.get(i)) {
                List<Integer> ports = piecePortMap.get(port);
                if (ports == null) {
                    ports = new ArrayList<>();
                }
                ports.add(port);
                piecePortMap.put(i, ports);
            }
        }
    }

    public List<Integer> getPortForPieceIndex(int index) {
        return piecePortMap.get(index);
    }

    private void removePort(int port) {
        Iterator<Map.Entry<Integer, List<Integer>>> iterator = piecePortMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, List<Integer>> entry = iterator.next();
            List<Integer> ports = entry.getValue();
            ports.removeIf(existingPort -> existingPort == port);
            if (ports.isEmpty()) {
                iterator.remove();
            }
        }
        LOGGER.info("Removed port {} from piecePortMap", port);
    }

    @Override
    public String toString() {
        return piecePortMap.toString();
    }
}