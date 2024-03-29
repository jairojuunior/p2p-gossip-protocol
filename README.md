# p2p-gossip-protocol
Implementation of a Gossip protocol for p2p communication in Java.
Below a simple diagram of classes.

Basically each Peer implements a Listener and a Gossiper. The Listener receives UDP messages from other peers and passes them to the Message Controller that validates and store in a singleton class accessed through the PeerDAO. Old records saved are cleaned from time to time via a class called Garbage Collector. The peer itself reads a given file system (directory) via FileSystemReader and stores itself in the same DAO. The Gossiper chooses a random known peer from time to time to retransmit a record saved.
![P2P Gossip Protol UML diagram of classes](https://github.com/jairojuunior/p2p-gossip-protocol/blob/master/UML.png?raw=true)

### How to start a Peer?
Feel free to use the already implemented .jar file. Start each Peer using the following command line:
`java -jar "/Peer/dist/Peer.jar" <IP> <PORT> <PEER_NAME> <FOLDER_TO_SCAN> <FILE SYSTEM SCAN REFRESH TIME> <SELF GOSSIP TIME> <RELAY GOSSIP TIME> <CACHE TIME OF MESSAGES>`
Description of args:
* IP, Port and Peer Name: IP, Port and Peer Name of this Peer
* Folder to Scan and File System Scan Refresh Time: A directory that will be scanned every <FILE SYSTEM SCAN REFRESH TIME> to propagate to other peers the files this peer has
* Self gossip time: Interval between transmissions of this peer files to another random peer
* Relay gossip time: Interval between transmissions of another peer files received by this peer to a third random peer
* Cache time of messages: Every peer communicates its files to some peers randomly. This cache times deletes the old messages so the Peer keeps relaying only recent information, preventing the propagation of data about dead peers
  
### Question?
Open an issue.
