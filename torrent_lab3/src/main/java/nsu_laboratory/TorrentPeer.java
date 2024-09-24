package nsu_laboratory;

import nsu_laboratory.app.PeerClient;
import nsu_laboratory.app.PeerServer;
import nsu_laboratory.utils.Utility;
import nsu_laboratory.utils.PeerInfo;
import nsu_laboratory.utils.PeerInfoHandler;
import nsu_laboratory.utils.TorrentException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.cli.*;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class TorrentPeer {
    static Logger LOGGER = LogManager.getLogger("TorrentMain");

    private final PeerClient torrentClient;
    private final PeerServer torrentServer;

    public TorrentPeer(int port) {
        Utility.setPort(port);
        ArrayList<PeerInfo> peers = PeerInfoHandler.peerHandle();
        torrentServer = new PeerServer();
        torrentClient = new PeerClient(peers);
    }

    private void startWork() throws InterruptedException {
        Thread server = new Thread(torrentServer);
        server.start();
        sleep(2000);
        Thread client = new Thread(torrentClient);
        client.start();
    }

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("p", "port", true, "Port number to start the torrent");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            LOGGER.fatal("Failed to parse command line arguments: {}", e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Torrent", options);
            return;
        }

        if (!cmd.hasOption("p")) {
            LOGGER.fatal("Usage: java Torrent -p <port>");
            return;
        }

        int port;
        try {
            port = Integer.parseInt(cmd.getOptionValue("p"));
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number.");
            return;
        }

        LOGGER.info("Starting creating torrent peer on port {}", port);

        TorrentPeer peer = new TorrentPeer(port);
        try {
            peer.startWork();
        } catch (Exception e) {
            LOGGER.error("Something went wrong while torrent processing");
            throw new TorrentException("Torrent error", e);
        }
    }
}