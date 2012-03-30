package fr.irit.smac.may.lib.components.distribution.ivy;

import fr.irit.smac.may.lib.components.distribution.ivy.IvyReceive;

public abstract class IvyReceive {

	private IvyReceive.ComponentImpl selfComponent = null;

	/**
	 * This can be called by the implementation to access the component itself and its provided ports.
	 *
	 * This is not meant to be called from the outside by hand.
	 */
	protected IvyReceive.Component self() {
		assert this.selfComponent != null;
		return this.selfComponent;
	};

	/**
	 * This can be called by the implementation to access this required port.
	 *
	 * This is not meant to be called from the outside.
	 */
	protected fr.irit.smac.may.lib.interfaces.Push<java.util.List<java.lang.String>> receive() {
		assert this.selfComponent != null;
		return this.selfComponent.bridge.receive();
	};

	/**
	 * This should be overridden by the implementation to define the provided port.
	 * This will be called once during the construction of the component to initialize the port.
	 *
	 * This is not meant to be called on from the outside.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Push<java.lang.String> make_bindMsg();

	/**
	 * This should be overridden by the implementation to define the provided port.
	 * This will be called once during the construction of the component to initialize the port.
	 *
	 * This is not meant to be called on from the outside.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Pull<fr.irit.smac.may.lib.components.distribution.ivy.IvyConnectionStatus> make_connectionStatus();

	/**
	 * This should be overridden by the implementation to define the provided port.
	 * This will be called once during the construction of the component to initialize the port.
	 *
	 * This is not meant to be called on from the outside.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Push<fr.irit.smac.may.lib.components.distribution.ivy.IvyConnectionConfig> make_connect();

	/**
	 * This should be overridden by the implementation to define the provided port.
	 * This will be called once during the construction of the component to initialize the port.
	 *
	 * This is not meant to be called on from the outside.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Do make_disconnect();

	public static interface Bridge {
		public fr.irit.smac.may.lib.interfaces.Push<java.util.List<java.lang.String>> receive();

	}

	public static interface Component {

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public fr.irit.smac.may.lib.interfaces.Push<java.lang.String> bindMsg();
		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public fr.irit.smac.may.lib.interfaces.Pull<fr.irit.smac.may.lib.components.distribution.ivy.IvyConnectionStatus> connectionStatus();
		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public fr.irit.smac.may.lib.interfaces.Push<fr.irit.smac.may.lib.components.distribution.ivy.IvyConnectionConfig> connect();
		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public fr.irit.smac.may.lib.interfaces.Do disconnect();

		/**
		 * This should be called to start the component
		 */
		public void start();
	}

	private final static class ComponentImpl implements IvyReceive.Component {

		private final IvyReceive.Bridge bridge;

		private final IvyReceive implementation;

		private ComponentImpl(final IvyReceive implem, final IvyReceive.Bridge b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.selfComponent == null;
			implem.selfComponent = this;

			this.bindMsg = implem.make_bindMsg();
			this.connectionStatus = implem.make_connectionStatus();
			this.connect = implem.make_connect();
			this.disconnect = implem.make_disconnect();

		}

		private final fr.irit.smac.may.lib.interfaces.Push<java.lang.String> bindMsg;

		public final fr.irit.smac.may.lib.interfaces.Push<java.lang.String> bindMsg() {
			return this.bindMsg;
		};
		private final fr.irit.smac.may.lib.interfaces.Pull<fr.irit.smac.may.lib.components.distribution.ivy.IvyConnectionStatus> connectionStatus;

		public final fr.irit.smac.may.lib.interfaces.Pull<fr.irit.smac.may.lib.components.distribution.ivy.IvyConnectionStatus> connectionStatus() {
			return this.connectionStatus;
		};
		private final fr.irit.smac.may.lib.interfaces.Push<fr.irit.smac.may.lib.components.distribution.ivy.IvyConnectionConfig> connect;

		public final fr.irit.smac.may.lib.interfaces.Push<fr.irit.smac.may.lib.components.distribution.ivy.IvyConnectionConfig> connect() {
			return this.connect;
		};
		private final fr.irit.smac.may.lib.interfaces.Do disconnect;

		public final fr.irit.smac.may.lib.interfaces.Do disconnect() {
			return this.disconnect;
		};

		public final void start() {

			this.implementation.start();
		}
	}

	/**
	 * Can be overridden by the implementation
	 * It will be called after the component has been instantiated, after the components have been instantiated
	 * and during the containing component start() method is called.
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected void start() {
	}

	public IvyReceive.Component newComponent(IvyReceive.Bridge b) {
		return new IvyReceive.ComponentImpl(this, b);
	}

}
