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

/**
 * 
 * @author Jevgenijus Cistiakovas   cistiakj@tcd.ie
 *
 * Run this class to spawn a demostration of the assignment
 */
public class OpenFlowDemo implements Constants {

	static final int CONTROLLER_PORT = 50000;
	static final int ROUTER_1_PORT = 50001;
	static final int ROUTER_2_PORT = 50002;
	static final int ROUTER_3_PORT = 50003;
	static final int ROUTER_4_PORT = 50004;
	static final int ROUTER_5_PORT = 50005;
	static final int ROUTER_6_PORT = 50006;
	static final int ROUTER_7_PORT = 50007;
	static final int ROUTER_8_PORT = 50008;
	static final int ENDPOINT_1_PORT = 50051;
	static final int ENDPOINT_2_PORT = 50052;

	public static void main(String[] args) {
		int difficulty = 2;
		try {
			if (difficulty == 1) {
				// simple topology is
				// EP1 -- R1 -- R2 -- R3 -- EP2

				// create controller
				Controller controller1 = new Controller(CONTROLLER_PORT);
				InetSocketAddress controllerAddress = new InetSocketAddress(InetAddress.getLocalHost(),
						CONTROLLER_PORT);
				// create interfaces lists
				ArrayList<Interface> interfacesRouter1 = new ArrayList<>();
				interfacesRouter1.add(new Interface(1, ENDPOINT_1_PORT, 1));
				interfacesRouter1.add(new Interface(2, ROUTER_2_PORT, 1));
				ArrayList<Interface> interfacesRouter2 = new ArrayList<>();
				interfacesRouter2.add(new Interface(1, ROUTER_1_PORT, 1));
				interfacesRouter2.add(new Interface(2, ROUTER_3_PORT, 1));
				ArrayList<Interface> interfacesRouter3 = new ArrayList<>();
				interfacesRouter3.add(new Interface(1, ROUTER_2_PORT, 1));
				interfacesRouter3.add(new Interface(2, ENDPOINT_2_PORT, 1));
				// create routers
				Router router1 = new Router(ROUTER_1_PORT, controllerAddress, interfacesRouter1);
				Router router2 = new Router(ROUTER_2_PORT, controllerAddress, interfacesRouter2);
				Router router3 = new Router(ROUTER_3_PORT, controllerAddress, interfacesRouter3);
				// creat endpoints
				EndPoint endPoint1 = new EndPoint(ENDPOINT_1_PORT, ROUTER_1_PORT);
				EndPoint endPoint2 = new EndPoint(ENDPOINT_2_PORT, ROUTER_3_PORT);

				// start all the threads
				new Thread(controller1).start();
				new Thread(router1).start();
				new Thread(router2).start();
				new Thread(router3).start();
				new Thread(endPoint1).start();
				new Thread(endPoint2).start();
			}else if (difficulty == 2) {
				// topoly given on the assignment sheet
				//			 R3 --------- R6
				//			/     		 /  \
				//	EP1 -- R1 --------- R4   R8 -- EP2
				//			\            \  /
				//			 R2 -- R5 --- R7
				// create controller
				Controller controller1 = new Controller(CONTROLLER_PORT);
				InetSocketAddress controllerAddress = new InetSocketAddress(InetAddress.getLocalHost(),
						CONTROLLER_PORT);
				// create interfaces lists
				ArrayList<Interface> interfacesRouter1 = new ArrayList<>();
				interfacesRouter1.add(new Interface(1, ENDPOINT_1_PORT, 1));
				interfacesRouter1.add(new Interface(2, ROUTER_2_PORT, 1));
				interfacesRouter1.add(new Interface(3, ROUTER_3_PORT, 1));
				interfacesRouter1.add(new Interface(4, ROUTER_4_PORT, 1));
				ArrayList<Interface> interfacesRouter2 = new ArrayList<>();
				interfacesRouter2.add(new Interface(1, ROUTER_1_PORT, 1));
				interfacesRouter2.add(new Interface(2, ROUTER_5_PORT, 1));
				ArrayList<Interface> interfacesRouter3 = new ArrayList<>();
				interfacesRouter3.add(new Interface(1, ROUTER_1_PORT, 1));
				interfacesRouter3.add(new Interface(2, ROUTER_6_PORT, 1));
				ArrayList<Interface> interfacesRouter4 = new ArrayList<>();
				interfacesRouter4.add(new Interface(1, ROUTER_1_PORT, 1));
				interfacesRouter4.add(new Interface(2, ROUTER_6_PORT, 1));
				interfacesRouter4.add(new Interface(3, ROUTER_7_PORT, 1));
				ArrayList<Interface> interfacesRouter5 = new ArrayList<>();
				interfacesRouter5.add(new Interface(1, ROUTER_2_PORT, 1));
				interfacesRouter5.add(new Interface(2, ROUTER_7_PORT, 1));
				ArrayList<Interface> interfacesRouter6 = new ArrayList<>();
				interfacesRouter6.add(new Interface(1, ROUTER_3_PORT, 1));
				interfacesRouter6.add(new Interface(2, ROUTER_4_PORT, 1));
				interfacesRouter6.add(new Interface(3, ROUTER_8_PORT, 1));
				ArrayList<Interface> interfacesRouter7 = new ArrayList<>();
				interfacesRouter7.add(new Interface(1, ROUTER_5_PORT, 1));
				interfacesRouter7.add(new Interface(2, ROUTER_4_PORT, 1));
				interfacesRouter7.add(new Interface(3, ROUTER_8_PORT, 1));
				ArrayList<Interface> interfacesRouter8 = new ArrayList<>();
				interfacesRouter8.add(new Interface(1, ROUTER_6_PORT, 1));
				interfacesRouter8.add(new Interface(2, ROUTER_7_PORT, 1));
				interfacesRouter8.add(new Interface(3, ENDPOINT_2_PORT, 1));
				// create routers
				Router router1 = new Router(ROUTER_1_PORT, controllerAddress, interfacesRouter1);
				Router router2 = new Router(ROUTER_2_PORT, controllerAddress, interfacesRouter2);
				Router router3 = new Router(ROUTER_3_PORT, controllerAddress, interfacesRouter3);
				Router router4 = new Router(ROUTER_4_PORT, controllerAddress, interfacesRouter4);
				Router router5 = new Router(ROUTER_5_PORT, controllerAddress, interfacesRouter5);
				Router router6 = new Router(ROUTER_6_PORT, controllerAddress, interfacesRouter6);
				Router router7 = new Router(ROUTER_7_PORT, controllerAddress, interfacesRouter7);
				Router router8 = new Router(ROUTER_8_PORT, controllerAddress, interfacesRouter8);
				// creat endpoints
				EndPoint endPoint1 = new EndPoint(ENDPOINT_1_PORT, ROUTER_1_PORT);
				EndPoint endPoint2 = new EndPoint(ENDPOINT_2_PORT, ROUTER_3_PORT);

				// start all the threads
				new Thread(controller1).start();
				new Thread(router1).start();
				new Thread(router2).start();
				new Thread(router3).start();
				new Thread(router4).start();
				new Thread(router5).start();
				new Thread(router6).start();
				new Thread(router7).start();
				new Thread(router8).start();
				new Thread(endPoint1).start();
				new Thread(endPoint2).start();
			}
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
