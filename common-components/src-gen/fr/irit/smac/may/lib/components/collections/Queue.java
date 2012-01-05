package fr.irit.smac.may.lib.components.collections;

import fr.irit.smac.may.lib.components.collections.Queue;

public abstract class Queue<Truc> {

	private Queue.ComponentImpl<Truc> structure = null;

	/**
	 * This can be called by the implementation to access the component itself and its provided ports.
	 *
	 * This is not meant to be called from the outside by hand.
	 */
	protected Queue.Component<Truc> self() {
		assert this.structure != null;
		return this.structure;
	};

	/**
	 * This should be overridden by the implementation to define the provided port.
	 * This will be called once during the construction of the component to initialize the port.
	 *
	 * This is not meant to be called on from the outside.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Push<Truc> put();

	/**
	 * This should be overridden by the implementation to define the provided port.
	 * This will be called once during the construction of the component to initialize the port.
	 *
	 * This is not meant to be called on from the outside.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Pull<Truc> get();

	/**
	 * This should be overridden by the implementation to define the provided port.
	 * This will be called once during the construction of the component to initialize the port.
	 *
	 * This is not meant to be called on from the outside.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Pull<java.util.Collection<Truc>> getAll();

	public static interface Bridge<Truc> {

	}

	public static interface Component<Truc> {

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public fr.irit.smac.may.lib.interfaces.Push<Truc> put();
		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public fr.irit.smac.may.lib.interfaces.Pull<Truc> get();
		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public fr.irit.smac.may.lib.interfaces.Pull<java.util.Collection<Truc>> getAll();

		/**
		 * This should be called to start the component
		 */
		public void start();
	}

	private final static class ComponentImpl<Truc>
			implements
				Queue.Component<Truc> {

		@SuppressWarnings("unused")
		private final Queue.Bridge<Truc> bridge;

		private final Queue<Truc> implementation;

		private ComponentImpl(final Queue<Truc> implem,
				final Queue.Bridge<Truc> b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.structure == null;
			implem.structure = this;

			this.put = implem.put();
			this.get = implem.get();
			this.getAll = implem.getAll();

		}

		private final fr.irit.smac.may.lib.interfaces.Push<Truc> put;

		public final fr.irit.smac.may.lib.interfaces.Push<Truc> put() {
			return this.put;
		};
		private final fr.irit.smac.may.lib.interfaces.Pull<Truc> get;

		public final fr.irit.smac.may.lib.interfaces.Pull<Truc> get() {
			return this.get;
		};
		private final fr.irit.smac.may.lib.interfaces.Pull<java.util.Collection<Truc>> getAll;

		public final fr.irit.smac.may.lib.interfaces.Pull<java.util.Collection<Truc>> getAll() {
			return this.getAll;
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

	public Queue.Component<Truc> createComponent(Queue.Bridge<Truc> b) {
		return new Queue.ComponentImpl<Truc>(this, b);
	}

	public Queue.Component<Truc> createComponent() {
		return this.createComponent(new Queue.Bridge<Truc>() {
		});
	}
	public static final <Truc> Queue.Component<Truc> createComponent(
			Queue<Truc> _compo) {
		return _compo.createComponent();
	}

}
