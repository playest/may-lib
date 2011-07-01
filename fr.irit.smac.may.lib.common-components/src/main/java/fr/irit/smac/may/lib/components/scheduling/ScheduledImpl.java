package fr.irit.smac.may.lib.components.scheduling;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

import fr.irit.smac.may.lib.interfaces.Do;

public class ScheduledImpl extends Scheduled {

	private final Set<AgentSide> agents = Collections.synchronizedSet(new HashSet<AgentSide>());
	
	public class AgentSide extends Scheduled.Agent {

		private FutureTask<?> currentTask;
		private AtomicBoolean run = new AtomicBoolean(true);

		@Override
		protected Do stop() {
			return new Do() {
				public void doIt() {
					run.set(false);
					if (currentTask != null && !currentTask.isDone()) {
						currentTask.cancel(true);
					}
				}
			};
		}
		
		private void tick() {
			if (run.get()) {
				currentTask = new FutureTask<Object>(new Runnable() {
					public void run() {
						cycle().doIt();
					}
				}, null);
				sched().execute(currentTask);
			}
		}
	}

	@Override
	protected Do tick() {
		return new Do() {
			public void doIt() {
				synchronized (agents) {
					// start agents
					for(AgentSide a: agents) {
						a.tick();
					}
					// wait for all of them to finish before giving back control
					for(AgentSide a: agents) {
						if (a.currentTask != null) {
							try {
								a.currentTask.get();
							} catch (InterruptedException e) {
								// TODO what to do here?
								e.printStackTrace();
							} catch (ExecutionException e) {
								System.err.println("Error when executing cycle in ScheduledImpl:");
								e.printStackTrace();
							} finally {
								a.currentTask = null;
							}
						}
					}
				}
			}
		};
	}
}
