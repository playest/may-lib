package fr.irit.smac.may.lib.classic.local;

import fr.irit.smac.may.lib.classic.local.ClassicBehaviour;
import fr.irit.smac.may.lib.components.SequentialDispatcher;

public abstract class ClassicAgentComponent<Msg, Ref> {

	private final void init() {
		this.dispatcher = make_dispatcher();
		this.beh = make_beh();

	}

	private Component<Msg, Ref> structure = null;

	/**
	 * This can be called by the implementation to access this required port
	 * It will be initialised before the provided ports are initialised
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final fr.irit.smac.may.lib.interfaces.Send<Msg, Ref> send() {
		assert this.structure != null;
		return this.structure.bridge.send();
	};
	/**
	 * This can be called by the implementation to access this required port
	 * It will be initialised before the provided ports are initialised
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final fr.irit.smac.may.lib.interfaces.Pull<Ref> me() {
		assert this.structure != null;
		return this.structure.bridge.me();
	};
	/**
	 * This can be called by the implementation to access this required port
	 * It will be initialised before the provided ports are initialised
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final java.util.concurrent.Executor executor() {
		assert this.structure != null;
		return this.structure.bridge.executor();
	};
	/**
	 * This can be called by the implementation to access this required port
	 * It will be initialised before the provided ports are initialised
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final fr.irit.smac.may.lib.interfaces.Do die() {
		assert this.structure != null;
		return this.structure.bridge.die();
	};
	/**
	 * This can be called by the implementation to access this required port
	 * It will be initialised before the provided ports are initialised
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final fr.irit.smac.may.lib.classic.interfaces.CreateClassic<Msg, Ref> create() {
		assert this.structure != null;
		return this.structure.bridge.create();
	};

	/**
	 * This should be overridden by the implementation to define how to create this sub-component
	 * This will be called once during the construction of the component to initialise this sub-component
	 */
	protected abstract SequentialDispatcher<Msg> make_dispatcher();

	private SequentialDispatcher<Msg> dispatcher;

	/**
	 * This can be called by the implementation to access the sub-component instance and its provided ports
	 * It will be initialised after the required ports are initialised and before the provided ports are initialised
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final SequentialDispatcher.Component<Msg> dispatcher() {
		assert this.structure != null;
		return this.structure.dispatcher;
	}

	/**
	 * This should be overridden by the implementation to define how to create this sub-component
	 * This will be called once during the construction of the component to initialise this sub-component
	 */
	protected abstract ClassicBehaviour<Msg, Ref> make_beh();

	private ClassicBehaviour<Msg, Ref> beh;

	/**
	 * This can be called by the implementation to access the sub-component instance and its provided ports
	 * It will be initialised after the required ports are initialised and before the provided ports are initialised
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final ClassicBehaviour.Component<Msg, Ref> beh() {
		assert this.structure != null;
		return this.structure.beh;
	}

	public static interface Bridge<Msg, Ref> {
		public fr.irit.smac.may.lib.interfaces.Send<Msg, Ref> send();
		public fr.irit.smac.may.lib.interfaces.Pull<Ref> me();
		public java.util.concurrent.Executor executor();
		public fr.irit.smac.may.lib.interfaces.Do die();
		public fr.irit.smac.may.lib.classic.interfaces.CreateClassic<Msg, Ref> create();

	}

	public static final class Component<Msg, Ref> {

		private final Bridge<Msg, Ref> bridge;

		private final ClassicAgentComponent<Msg, Ref> implementation;

		/**
		 * This constructor can be called directly to instantiate stand-alone components
		 */
		public Component(final ClassicAgentComponent<Msg, Ref> implem,
				final Bridge<Msg, Ref> b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.structure == null;
			implem.structure = this;
			implem.init();

			this.dispatcher = new SequentialDispatcher.Component<Msg>(
					implem.dispatcher, new ClassicAgentComponent_dispatcher());
			this.beh = new ClassicBehaviour.Component<Msg, Ref>(implem.beh,
					new ClassicAgentComponent_beh());

		}

		private final SequentialDispatcher.Component<Msg> dispatcher;

		private final class ClassicAgentComponent_dispatcher
				implements
					SequentialDispatcher.Bridge<Msg> {

			public final java.util.concurrent.Executor executor() {
				return Component.this.bridge.executor();

			};

			public final fr.irit.smac.may.lib.interfaces.Push<Msg> handler() {
				return Component.this.beh.cycle();

			};

		}
		private final ClassicBehaviour.Component<Msg, Ref> beh;

		private final class ClassicAgentComponent_beh
				implements
					ClassicBehaviour.Bridge<Msg, Ref> {

			public final fr.irit.smac.may.lib.interfaces.Send<Msg, Ref> send() {
				return Component.this.bridge.send();

			};

			public final fr.irit.smac.may.lib.interfaces.Pull<Ref> me() {
				return Component.this.bridge.me();

			};

			public final fr.irit.smac.may.lib.interfaces.Do die() {
				return Component.this.bridge.die();

			};

			public final fr.irit.smac.may.lib.classic.interfaces.CreateClassic<Msg, Ref> create() {
				return Component.this.bridge.create();

			};

		}

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public final fr.irit.smac.may.lib.interfaces.Push<Msg> put() {
			return this.dispatcher.dispatch();
		};

		/**
		 * This must be called to start the component and its sub-components.
		 */
		public final void start() {
			this.dispatcher.start();
			this.beh.start();

			this.implementation.start();
		}
	}

	/**
	 * Can be overriden by the implementation
	 * It will be called after the component has been instantiated, after the components have been instantiated
	 * and during the containing component start() method is called.
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected void start() {
	}
}
