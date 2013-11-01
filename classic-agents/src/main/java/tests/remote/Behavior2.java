package tests.remote;

import java.io.Serializable;

import tests.messages.WithRef;
import fr.irit.smac.may.lib.classic.remote.impl.AbstractRemoteClassicBehaviour;
import fr.irit.smac.may.lib.components.remote.place.Place;
import fr.irit.smac.may.lib.interfaces.Push;
import fr.irit.smac.may.lib.pmbehaviour.PatternMatchingBehavior;
import fr.irit.smac.may.lib.pmbehaviour.PatternMatchingMessage;

public class Behavior2<Ref> extends AbstractRemoteClassicBehaviour<PatternMatchingMessage,Ref> implements Serializable {

	private static final long serialVersionUID = -7663551892033127305L;

	private final PatternMatchingBehavior matcher = new PatternMatchingBehavior(this);
	
	public void caseWorld() {
		System.out.println(requires().me().pull() + " received : world");

		Ref wr = requires().create().create(new BehaviorWithRef<Ref>(), new Place("localhost",1098));
		requires().send().send(new WithRef<Ref>(requires().me().pull()), wr);

		// wait for message to get into my mailbox
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// move(new Place("localhost:1098"));
	}

	public void caseOther(String e) {
		System.out.println(requires().me().pull() + " : received other : " + e);
	}

	public void caseZero() {
		System.out.println(requires().me().pull() + " : received : zero");
		
		System.out.println(requires().me().pull() + " : die");
		requires().die().doIt();
	}
	
	@Override
	protected Push<PatternMatchingMessage> make_cycle() {
		return new Push<PatternMatchingMessage>() {
			public void push(PatternMatchingMessage thing) {
				matcher.match(thing);
			}
		};
	}
}