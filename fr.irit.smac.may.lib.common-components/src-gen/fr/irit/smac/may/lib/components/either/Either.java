package fr.irit.smac.may.lib.components.either;

public abstract class Either<L, R> {

	private Component<L, R> structure = null;

	/**
	 * This can be called by the implementation to access this required port
	 * It will be initialized before the provided ports are initialized
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected final fr.irit.smac.may.lib.interfaces.Push<fr.irit.smac.may.lib.components.either.datatypes.Either<L, R>> out() {
		assert this.structure != null;
		return this.structure.bridge.out();
	};

	/**
	 * This should be overridden by the implementation to define the provided port
	 * This will be called once during the construction of the component to initialize the port
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Push<L> left();

	/**
	 * This should be overridden by the implementation to define the provided port
	 * This will be called once during the construction of the component to initialize the port
	 *
	 * This is not meant to be called on the object by hand.
	 */
	protected abstract fr.irit.smac.may.lib.interfaces.Push<R> right();

	public static interface Bridge<L, R> {
		public fr.irit.smac.may.lib.interfaces.Push<fr.irit.smac.may.lib.components.either.datatypes.Either<L, R>> out();

	}

	public static final class Component<L, R> {

		private final Bridge<L, R> bridge;

		private final Either<L, R> implementation;

		public Component(final Either<L, R> implem, final Bridge<L, R> b) {
			this.bridge = b;

			this.implementation = implem;

			assert implem.structure == null;
			implem.structure = this;

			this.left = implem.left();
			this.right = implem.right();

		}

		private final fr.irit.smac.may.lib.interfaces.Push<L> left;

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public final fr.irit.smac.may.lib.interfaces.Push<L> left() {
			return this.left;
		};
		private final fr.irit.smac.may.lib.interfaces.Push<R> right;

		/**
		 * This can be called to access the provided port
		 * start() must have been called before
		 */
		public final fr.irit.smac.may.lib.interfaces.Push<R> right() {
			return this.right;
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