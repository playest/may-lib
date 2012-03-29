package fr.irit.smac.may.lib.components.messaging.interfaces;

import fr.irit.smac.may.lib.components.messaging.exceptions.AgentDoesNotExistException;
import fr.irit.smac.may.lib.interfaces.Send;

public interface ReliableSend<T, R> extends Send<T, R> {
	/**
	 * Send message to receiver.
	 * Asynchronous and reliable.
	 * Should not block.
	 * 
	 * @param message
	 *            the message to send
	 * @param receiver
	 *            the receiver of the message
	 * @throws AgentDoesNotExistException
	 *             when the receiver does not exist in the system
	 */
	public void reliableSend(T message, R receiver)
			throws AgentDoesNotExistException;
}
