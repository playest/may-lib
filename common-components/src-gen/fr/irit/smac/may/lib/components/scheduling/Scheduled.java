package fr.irit.smac.may.lib.components.scheduling;

import fr.irit.smac.may.lib.components.scheduling.Scheduled;

public abstract class Scheduled {

	private Scheduled.ComponentImpl structure = null;

	/**
	 * This can be called by the implementation to access the component itself and its provided ports.
	 *
	 * This is not meant to be called from the outside by hand.
	 */
	protected Scheduled.Component self() {
		assert this.structure != null;
		return this.structure;
	};

	/**
	 * This can be called by the implementation to access this required port.
	 *
	 * This is not meant to be called from the outside.
	 */
	protected java.util.concurrent.Executor sched() {
		assert this.structure != null;
		return this.structure.bridge.sched();
	};

	/**
	 * This should be overridden by the implementation to define the provided port.
	 * This will be called once during the construction of the component to initialize the port.
	 *
	 * This is not meant to be called on from the outside.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Do tick();

	public static interface Bridge {
		public java.util.concurrent.Executor sched();

	}

	public static interface Component {

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public fr.irit.smac.may.lib.interfaces.Do tick();

		/**
		 * This should be called to start the component
		 */
		public void start();
	}

	private final static class ComponentImpl implements Scheduled.Component {

		private final Scheduled.Bridge bridge;

		private final Scheduled implementation;

		private ComponentImpl(final Scheduled implem, final Scheduled.Bridge b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.structure == null;
			implem.structure = this;

			this.tick = implem.tick();

		}

		private final fr.irit.smac.may.lib.interfaces.Do tick;

		public final fr.irit.smac.may.lib.interfaces.Do tick() {
			return this.tick;
		};

		public final void start() {

			this.implementation.start();
		}
	}

	/**
	 * Should not be called
	 */
	protected abstract Scheduled.Agent make_Agent();

	public Scheduled.Agent createImplementationOfAgent() {
		Scheduled.Agent implem = make_Agent();
		assert implem.infraStructure == null;
		assert this.structure == null;
		implem.infraStructure = this.structure;

		return implem;
	}

	public static abstract class Agent {

		private Scheduled.Agent.ComponentImpl structure = null;

		/**
		 * This can be called by the implementation to access the component itself and its provided ports.
		 *
		 * This is not meant to be called from the outside by hand.
		 */
		protected Scheduled.Agent.Component self() {
			assert this.structure != null;
			return this.structure;
		};

		/**
		 * This can be called by the implementation to access this required port.
		 *
		 * This is not meant to be called from the outside.
		 */
		protected fr.irit.smac.may.lib.interfaces.Do cycle() {
			assert this.structure != null;
			return this.structure.bridge.cycle();
		};

		/**
		 * This should be overridden by the implementation to define the provided port.
		 * This will be called once during the construction of the component to initialize the port.
		 *
		 * This is not meant to be called on from the outside.
		 */
		protected abstract fr.irit.smac.may.lib.interfaces.Do stop();

		private Scheduled.ComponentImpl infraStructure = null;

		/**
		 * This can be called by the implementation to access the component of the infrastructure itself and its provided ports.
		 *
		 * This is not meant to be called from the outside by hand.
		 */
		protected Scheduled.Component infraSelf() {
			assert this.infraStructure != null;
			return this.infraStructure;
		};

		public static interface Bridge {
			public fr.irit.smac.may.lib.interfaces.Do cycle();

		}

		public static interface Component {

			/**
			 * This can be called to access the provided port
			 * start() must have been called before
			 */
			public fr.irit.smac.may.lib.interfaces.Do stop();

			/**
			 * This should be called to start the component
			 */
			public void start();
		}

		private final static class ComponentImpl
				implements
					Scheduled.Agent.Component {

			private final Scheduled.Agent.Bridge bridge;

			private final Scheduled.Agent implementation;

			private ComponentImpl(final Scheduled.Agent implem,
					final Scheduled.Agent.Bridge b) {
				this.bridge = b;

				this.implementation = implem;

				assert implem.structure == null;
				implem.structure = this;

				this.stop = implem.stop();

			}

			private final fr.irit.smac.may.lib.interfaces.Do stop;

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

		public Scheduled.Agent.Component createComponent(
				Scheduled.Agent.Bridge b) {
			return new Scheduled.Agent.ComponentImpl(this, b);
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

	public Scheduled.Component createComponent(Scheduled.Bridge b) {
		return new Scheduled.ComponentImpl(this, b);
	}

}
