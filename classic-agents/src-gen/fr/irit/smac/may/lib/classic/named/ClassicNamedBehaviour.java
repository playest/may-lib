package fr.irit.smac.may.lib.classic.named;

import fr.irit.smac.may.lib.classic.named.ClassicNamedBehaviour;

public abstract class ClassicNamedBehaviour<Msg, Ref> {

	private ClassicNamedBehaviour.ComponentImpl<Msg, Ref> structure = null;

	/**
	 * This can be called by the implementation to access the component itself and its provided ports.
	 *
	 * This is not meant to be called from the outside by hand.
	 */
	protected ClassicNamedBehaviour.Component<Msg, Ref> self() {
		assert this.structure != null;
		return this.structure;
	};

	/**
	 * This can be called by the implementation to access this required port.
	 *
	 * This is not meant to be called from the outside.
	 */
	protected fr.irit.smac.may.lib.interfaces.Send<Msg, Ref> send() {
		assert this.structure != null;
		return this.structure.bridge.send();
	};
	/**
	 * This can be called by the implementation to access this required port.
	 *
	 * This is not meant to be called from the outside.
	 */
	protected fr.irit.smac.may.lib.interfaces.Pull<Ref> me() {
		assert this.structure != null;
		return this.structure.bridge.me();
	};
	/**
	 * This can be called by the implementation to access this required port.
	 *
	 * This is not meant to be called from the outside.
	 */
	protected fr.irit.smac.may.lib.interfaces.Do die() {
		assert this.structure != null;
		return this.structure.bridge.die();
	};
	/**
	 * This can be called by the implementation to access this required port.
	 *
	 * This is not meant to be called from the outside.
	 */
	protected fr.irit.smac.may.lib.classic.interfaces.CreateNamed<Msg, Ref> create() {
		assert this.structure != null;
		return this.structure.bridge.create();
	};

	/**
	 * This should be overridden by the implementation to define the provided port.
	 * This will be called once during the construction of the component to initialize the port.
	 *
	 * This is not meant to be called on from the outside.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Push<Msg> cycle();

	public static interface Bridge<Msg, Ref> {
		public fr.irit.smac.may.lib.interfaces.Send<Msg, Ref> send();
		public fr.irit.smac.may.lib.interfaces.Pull<Ref> me();
		public fr.irit.smac.may.lib.interfaces.Do die();
		public fr.irit.smac.may.lib.classic.interfaces.CreateNamed<Msg, Ref> create();

	}

	public static interface Component<Msg, Ref> {

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public fr.irit.smac.may.lib.interfaces.Push<Msg> cycle();

		/**
		 * This should be called to start the component
		 */
		public void start();
	}

	private final static class ComponentImpl<Msg, Ref>
			implements
				ClassicNamedBehaviour.Component<Msg, Ref> {

		private final ClassicNamedBehaviour.Bridge<Msg, Ref> bridge;

		private final ClassicNamedBehaviour<Msg, Ref> implementation;

		private ComponentImpl(final ClassicNamedBehaviour<Msg, Ref> implem,
				final ClassicNamedBehaviour.Bridge<Msg, Ref> b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.structure == null;
			implem.structure = this;

			this.cycle = implem.cycle();

		}

		private final fr.irit.smac.may.lib.interfaces.Push<Msg> cycle;

		public final fr.irit.smac.may.lib.interfaces.Push<Msg> cycle() {
			return this.cycle;
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

	public ClassicNamedBehaviour.Component<Msg, Ref> createComponent(
			ClassicNamedBehaviour.Bridge<Msg, Ref> b) {
		return new ClassicNamedBehaviour.ComponentImpl<Msg, Ref>(this, b);
	}

}
