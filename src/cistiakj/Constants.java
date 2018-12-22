package cistiakj;

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *	Constants used by classes in OpenFlow project.
 */
public interface Constants {
	
	int PACKETSIZE = 65536;
	int OF_VERSION = 1;
	int PACKET_WAITING_TIME_IN_SEC = 3;
	int WAIT_LIMIT = 3;
	int PROTOCOL_VERSION = 1;
	int BUFFER_SIZE = 32255;
	
	//default ports
	int CONTROLLER_DEFAULT_PORT = 50000;
	//Packet options
	int ECHO_OPTION_DEFAULT = 0;
}
