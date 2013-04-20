package fr.irit.smac.may.lib.components.scheduling;

public abstract class Scheduled {

	private Component structure = null;

	/**
	 * This can be called by the implementation to access this required port
	 * It will be initialized before the provided ports are initialized
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final fr.irit.smac.may.lib.components.scheduling.interfaces.AdvancedExecutor sched() {
		assert this.structure != null;
		return this.structure.bridge.sched();
	};

	/**
	 * This should be overridden by the implementation to define the provided port
	 * This will be called once during the construction of the component to initialize the port
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Do tick();

	/**
	 * This should be overridden by the implementation to define the provided port
	 * This will be called once during the construction of the component to initialize the port
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected abstract fr.irit.smac.may.lib.components.scheduling.interfaces.SchedulingControl async();

	public static interface Bridge {
		public fr.irit.smac.may.lib.components.scheduling.interfaces.AdvancedExecutor sched();

	}

	public static final class Component {

		private final Bridge bridge;

		private final Scheduled implementation;

		public Component(final Scheduled implem, final Bridge b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.structure == null;
			implem.structure = this;

			this.tick = implem.tick();
			this.async = implem.async();

		}

		private final fr.irit.smac.may.lib.interfaces.Do tick;

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public final fr.irit.smac.may.lib.interfaces.Do tick() {
			return this.tick;
		};
		private final fr.irit.smac.may.lib.components.scheduling.interfaces.SchedulingControl async;

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public final fr.irit.smac.may.lib.components.scheduling.interfaces.SchedulingControl async() {
			return this.async;
		};

		public final void start() {

			this.implementation.start();
		}
	}

	public static abstract class Agent {

		private Component structure = null;

		/**
		 * This can be called by the implementation to access this required port
		 * It will be initialized before the provided ports are initialized
		 *
		 * This is not meant to be called on the object by hand.
		 */
		protected final fr.irit.smac.may.lib.interfaces.Do cycle() {
			assert this.structure != null;
			return this.structure.bridge.cycle();
		};

		/**
		 * This should be overridden by the implementation to define the provided port
		 * This will be called once during the construction of the component to initialize the port
		 *
		 * This is not meant to be called on the object by hand.
		 */
		protected abstract fr.irit.smac.may.lib.interfaces.Do stop();

		public static interface Bridge {
			public fr.irit.smac.may.lib.interfaces.Do cycle();

		}

		public static final class Component {

			private final Bridge bridge;

			private final Agent implementation;

			public Component(final Agent implem, final Bridge b) {
				this.bridge = b;

				this.implementation = implem;

				assert implem.structure == null;
				implem.structure = this;

				this.stop = implem.stop();

			}

			private final fr.irit.smac.may.lib.interfaces.Do stop;

			/**
			 * This can be called to access the provided port
			 * start() must have been called before
			 */
			public final fr.irit.smac.may.lib.interfaces.Do stop() {
				return this.stop;
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
