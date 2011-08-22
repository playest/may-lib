package fr.irit.smac.may.lib.components.meta;

public abstract class Collection<K, I> {

	private Component<K, I> structure = null;

	/**
	 * This should be overridden by the implementation to define the provided port
	 * This will be called once during the construction of the component to initialize the port
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.MapGet<K, I> get();

	public static interface Bridge<K, I> {

	}

	public static final class Component<K, I> {

		@SuppressWarnings("unused")
		private final Bridge<K, I> bridge;

		private final Collection<K, I> implementation;

		public Component(final Collection<K, I> implem, final Bridge<K, I> b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.structure == null;
			implem.structure = this;

			this.get = implem.get();

		}

		private final fr.irit.smac.may.lib.interfaces.MapGet<K, I> get;

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public final fr.irit.smac.may.lib.interfaces.MapGet<K, I> get() {
			return this.get;
		};

		public final void start() {

			this.implementation.start();
		}
	}

	public static abstract class Agent<K, I> {

		private Component<K, I> structure = null;

		/**
		 * This can be called by the implementation to access this required port
		 * It will be initialized before the provided ports are initialized
		 *
		 * This is not meant to be called on the object by hand.
		 */
		protected final I p() {
			assert this.structure != null;
			return this.structure.bridge.p();
		};

		/**
		 * This should be overridden by the implementation to define the provided port
		 * This will be called once during the construction of the component to initialize the port
		 *
		 * This is not meant to be called on the object by hand.
		 */
		protected abstract fr.irit.smac.may.lib.interfaces.Pull<K> idx();

		public static interface Bridge<K, I> {
			public I p();

		}

		public static final class Component<K, I> {

			private final Bridge<K, I> bridge;

			private final Agent<K, I> implementation;

			public Component(final Agent<K, I> implem, final Bridge<K, I> b) {
				this.bridge = b;

				this.implementation = implem;

				assert implem.structure == null;
				implem.structure = this;

				this.idx = implem.idx();

			}

			private final fr.irit.smac.may.lib.interfaces.Pull<K> idx;

			/**
			 * This can be called to access the provided port
			 * start() must have been called before
			 */
			public final fr.irit.smac.may.lib.interfaces.Pull<K> idx() {
				return this.idx;
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
