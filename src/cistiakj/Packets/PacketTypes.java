package cistiakj.Packets;

public interface PacketTypes {
	enum OPF_TYPE {
		/* Immutable messages. */
		OFPT_HELLO,
		OFPT_ERROR,
		OFPT_ECHO_REQUEST,
		OFPT_ECHO_REPLY,
		OFPT_EXPERIMENTER,
		OFPT_ACK,
		/* Switch configuration messages. */
		OFPT_FEATURES_REQUEST ,
		OFPT_FEATURES_REPLY,
		OFPT_GET_CONFIG_REQUEST,
		OFPT_GET_CONFIG_REPLY,
		OFPT_SET_CONFIG,
		/* Asynchronous messages. */
		OFPT_PACKET_IN ,
		OFPT_FLOW_REMOVED,
		OFPT_PORT_STATUS,
		OFPT_PACKET_OUT,
		OFPT_FLOW_MOD,
		OFPT_GROUP_MOD,
		OFPT_PORT_MOD,
		OFPT_TABLE_MOD,
		/* Multipart messages. */
		OFPT_MULTIPART_REQUEST,
		OFPT_MULTIPART_REPLY ,
		/* Barrier messages. */
		OFPT_BARRIER_REQUEST,
		OFPT_BARRIER_REPLY,
		/* Controller role change request messages.
		*/
		OFPT_ROLE_REQUEST,
		OFPT_ROLE_REPLY ,
		/* Asynchronous message configuration. */
		OFPT_GET_ASYNC_REQUEST,
		OFPT_GET_ASYNC_REPLY,
		OFPT_SET_ASYNC,
		/* Meters and rate limiters configuration messages. */
		OFPT_METER_MOD,
		/* Controller role change event messages. */
		OFPT_ROLE_STATUS,
		/* Asynchronous messages. */
		OFPT_TABLE_STATUS,
		/* Request forwarding by the switch. */
		OFPT_REQUESTFORWARD,
		/* Bundle operations. */
		OFPT_BUNDLE_CONTROL,
		OFPT_BUNDLE_ADD_MESSAGE,
		/* Controller Status async message. */
		OFPT_CONTROLLER_STATUS; 
	}
}
