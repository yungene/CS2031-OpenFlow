package cistiakj;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cistiakj.Controller.Controller;
import cistiakj.EndPoint.EndPoint;
import cistiakj.Router.Interface;
import cistiakj.Router.Router;

public class OpenFlowDemo implements Constants{
	
	static final int CONTROLLER_PORT = 50000;
	static final int ROUTER_1_PORT = 50001;
	static final int ROUTER_2_PORT = 50002;
	static final int ROUTER_3_PORT = 50003;
	static final int ROUTER_4_PORT = 50004;
	static final int ENDPOINT_1_PORT = 50051;
	static final int ENDPOINT_2_PORT = 50052;

	public static void main(String[] args) {
		try {
			//simple topology is 
			//					EP1 -- R1 -- R2 -- R3 -- EP2
			
			//create controller
			Controller controller1 = new Controller(CONTROLLER_PORT);
			InetSocketAddress controllerAddress = new InetSocketAddress(InetAddress.getLocalHost(), CONTROLLER_PORT);
			//create interfaces lists
			ArrayList<Interface> interfacesRouter1 = new ArrayList<>();
			interfacesRouter1.add(new Interface(1, ENDPOINT_1_PORT, 1));
			interfacesRouter1.add(new Interface(2, ROUTER_2_PORT, 1));
			ArrayList<Interface> interfacesRouter2 = new ArrayList<>();
			interfacesRouter2.add(new Interface(1, ROUTER_1_PORT, 1));
			interfacesRouter2.add(new Interface(2, ROUTER_3_PORT, 1));
			ArrayList<Interface> interfacesRouter3 = new ArrayList<>();
			interfacesRouter3.add(new Interface(1, ROUTER_2_PORT, 1));
			interfacesRouter3.add(new Interface(2, ENDPOINT_2_PORT, 1));
			//create routers
			Router router1 = new Router(ROUTER_1_PORT,controllerAddress,interfacesRouter1);
			Router router2 = new Router(ROUTER_2_PORT,controllerAddress,interfacesRouter2);
			Router router3 = new Router(ROUTER_3_PORT,controllerAddress,interfacesRouter3);
			//creat endpoints
			EndPoint endPoint1 = new EndPoint(ENDPOINT_1_PORT, ROUTER_1_PORT);
			EndPoint endPoint2 = new EndPoint(ENDPOINT_2_PORT, ROUTER_3_PORT);
			
			//start all the threads
			new Thread(controller1).start();
			new Thread(router1).start();
			new Thread(router2).start();
			new Thread(router3).start();
			new Thread(endPoint1).start();
			new Thread(endPoint2).start();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		
		

	}

}
