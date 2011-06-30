package fr.irit.smac.may.lib.components.scheduling;

public abstract class Clock {

	private Component structure = null;

	/**
	 * This can be called by the implementation to access this required port
	 * It will be initialized before the provided ports are initialized
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final java.util.concurrent.Executor sched() {
		assert this.structure != null;
		return this.structure.bridge.sched();
	};
	/**
	 * This can be called by the implementation to access this required port
	 * It will be initialized before the provided ports are initialized
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final fr.irit.smac.may.lib.interfaces.Do tick() {
		assert this.structure != null;
		return this.structure.bridge.tick();
	};

	/**
	 * This should be overridden by the implementation to define the provided port
	 * This will be called once during the construction of the component to initialize the port
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected abstract fr.irit.smac.may.lib.components.scheduling.interfaces.SchedulingControl control();

	public static interface Bridge {
		public java.util.concurrent.Executor sched();
		public fr.irit.smac.may.lib.interfaces.Do tick();

	}

	public static final class Component {

		private final Bridge bridge;

		private final Clock implementation;

		public Component(final Clock implem, final Bridge b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.structure == null;
			implem.structure = this;

			this.control = implem.control();

		}

		private final fr.irit.smac.may.lib.components.scheduling.interfaces.SchedulingControl control;

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public final fr.irit.smac.may.lib.components.scheduling.interfaces.SchedulingControl control() {
			return this.control;
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

}
