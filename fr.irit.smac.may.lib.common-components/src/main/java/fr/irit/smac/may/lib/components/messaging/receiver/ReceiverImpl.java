package fr.irit.smac.may.lib.components.messaging.receiver;

import fr.irit.smac.may.lib.interfaces.Do;
import fr.irit.smac.may.lib.interfaces.Pull;
import fr.irit.smac.may.lib.interfaces.Send;

public class ReceiverImpl<Msg> extends Receiver<Msg> {
	
	public class AgentSide extends Agent<Msg> {
		
		private final AgentRef<Msg> agentRef;
		
		/**
		 * Name is not used as a reference
		 */
		public AgentSide(String name) {
			this.agentRef = new AgentRef<Msg>(this, name);
		}
		
		void receive(Msg m) {
			put().push(m);
		}
		
		@Override
		protected Pull<AgentRef<Msg>> me() {
			return new Pull<AgentRef<Msg>>() {
				public AgentRef<Msg> pull() {
					return AgentSide.this.agentRef;
				}
			};
		}

		@Override
		protected Do stop() {
			return new Do() {
				public void doIt() {
					AgentSide.this.agentRef.stop();
				}
			};
		}
	}
	
	@Override
	public Send<Msg, AgentRef<Msg>> deposit() {
		return new Send<Msg, AgentRef<Msg>>() {
			public void send(Msg msg, AgentRef<Msg> receiver) {
				receiver.receive(msg);
			};
		};
	}

}