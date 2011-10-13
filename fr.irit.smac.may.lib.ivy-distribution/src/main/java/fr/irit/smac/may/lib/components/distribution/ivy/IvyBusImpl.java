package fr.irit.smac.may.lib.components.distribution.ivy;

import java.util.Arrays;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;
import fr.irit.smac.may.lib.interfaces.Do;
import fr.irit.smac.may.lib.interfaces.Pull;
import fr.irit.smac.may.lib.interfaces.Push;

public class IvyBusImpl extends IvyBus {

	private IvyConnectionConfig connectionConfig;
	private Ivy bus = null;
	private boolean connected = false;

	private IvyMessageListener listener;

	@Override
	protected Pull<IvyConnectionStatus> connectionStatus() {
		return new Pull<IvyConnectionStatus>() {

			public IvyConnectionStatus pull() {
				IvyConnectionStatus status = new IvyConnectionStatus(
						IvyBusImpl.this.connectionConfig.getBroadCastAdress(),
						IvyBusImpl.this.connectionConfig.getPort(),
						IvyBusImpl.this.connectionConfig.getActorName(),
						IvyBusImpl.this.connectionConfig.getHelloWorldMessage(),
						IvyBusImpl.this.connected);
				return status;
			}
		};
	}

	@Override
	protected Push<IvyConnectionConfig> connect() {
		return new Push<IvyConnectionConfig>() {
			public void push(IvyConnectionConfig config) {
				if (IvyBusImpl.this.connected) {
					IvyBusImpl.this.bus.stop();
					IvyBusImpl.this.bus = null;
					IvyBusImpl.this.connected = false;
				}

				IvyBusImpl.this.bus = new Ivy(config.getActorName(),
						config.getHelloWorldMessage(), null);
				try {
					IvyBusImpl.this.connectionConfig = config;

					String domainBus = config.getBroadCastAdress() + ":"
							+ config.getPort();

					IvyBusImpl.this.bus.start(domainBus);

					// System.out.println("connected "+domainBus );
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					IvyBusImpl.this.connected = true;
				} catch (IvyException ie) {
					System.out.println("Error : " + ie.getMessage());
				}

			}

		};
	}

	@Override
	protected Do disconnect() {
		return new Do() {
			public void doIt() {
				IvyBusImpl.this.bus.stop();
				IvyBusImpl.this.bus = null;
				IvyBusImpl.this.connected = false;
			}
		};
	}

	@Override
	protected Push<String> bindMsg() {
		return new Push<String>() {

			public void push(String thing) {
				IvyBusImpl.this.listener = new IvyMessageListener() {
					public void receive(IvyClient client, String[] args) {
						IvyBusImpl.this.receive().push(Arrays.asList(args));
					}
				};

				try {
					IvyBusImpl.this.bus.bindMsg(thing, listener);
				} catch (IvyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	protected Push<String> send() {
		return new Push<String>() {
			public void push(String ivyMessage) {
				if (IvyBusImpl.this.connected) {
					try {
						// System.out.println(ivyMessage);
						IvyBusImpl.this.bus.sendMsg(ivyMessage);
						// System.out.println("sent"+i );
					} catch (IvyException e) {
						e.printStackTrace();
					}
				}

			}

		};
	}
}
