package fr.irit.smac.may.lib.classic.impl;

import java.util.concurrent.Executors;

import fr.irit.smac.may.lib.classic.interfaces.CreateClassic;
import fr.irit.smac.may.lib.classic.local.Classic;
import fr.irit.smac.may.lib.classic.local.ClassicBehaviour;
import fr.irit.smac.may.lib.classic.local.Factory;
import fr.irit.smac.may.lib.components.messaging.SenderImpl;
import fr.irit.smac.may.lib.components.messaging.Sender;
import fr.irit.smac.may.lib.components.messaging.receiver.AgentRef;
import fr.irit.smac.may.lib.components.messaging.receiver.ReceiverImpl;
import fr.irit.smac.may.lib.components.messaging.receiver.Receiver;
import fr.irit.smac.may.lib.components.scheduling.ExecutorService;
import fr.irit.smac.may.lib.components.scheduling.ExecutorServiceWrapperImpl;
import fr.irit.smac.may.lib.components.scheduling.Scheduler;
import fr.irit.smac.may.lib.components.scheduling.SchedulerImpl;

public class ClassicImpl<Msg> extends Classic<Msg> {

	private SchedulerImpl schedulerImpl;
	private SenderImpl<Msg, AgentRef> send;
	private ReceiverImpl<Msg> receive;
	private FactoryImpl<Msg, AgentRef> factory;

	private volatile int i = 0;
	
	@Override
	protected Scheduler make_scheduler() {
		schedulerImpl = new SchedulerImpl();
		return schedulerImpl;
	}

	@Override
	protected ExecutorService make_executor() {
		return new ExecutorServiceWrapperImpl(Executors.newFixedThreadPool(5));
	}

	@Override
	protected Sender<Msg, AgentRef> make_sender() {
		send = new SenderImpl<Msg, AgentRef>();
		return send;
	}

	@Override
	protected Receiver<Msg> make_receive() {
		receive = new ReceiverImpl<Msg>();
		return receive;
	}

	@Override
	protected Factory<Msg, AgentRef> make_fact() {
		factory = new FactoryImpl<Msg, AgentRef>();
		return factory;
	}

	@Override
	protected CreateClassic<Msg, AgentRef> create() {
		return new CreateClassic<Msg, AgentRef>() {
			public AgentRef create(
					final ClassicBehaviour<Msg, AgentRef> beh) {
				ClassicAgent<Msg> agent = new ClassicAgent<Msg>(
						new ClassicAgentComponentImpl<Msg>(beh),
						schedulerImpl.new AgentSide(), factory.new AgentSide(),
						receive.new AgentSide("agent"+(i++)), send.new AgentSide());
				agent.start();
				return agent.receive().me().pull();
			}
		};
	}
}
