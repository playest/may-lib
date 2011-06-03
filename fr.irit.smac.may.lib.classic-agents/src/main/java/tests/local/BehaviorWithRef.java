package tests.local;

import tests.messages.Other;
import fr.irit.smac.may.lib.classic.local.ClassicBehaviour;
import fr.irit.smac.may.lib.interfaces.Push;
import fr.irit.smac.may.lib.pmbehaviour.PatternMatchingBehavior;
import fr.irit.smac.may.lib.pmbehaviour.PatternMatchingMessage;

public class BehaviorWithRef<Ref> extends ClassicBehaviour<PatternMatchingMessage,Ref> {

	private static final long serialVersionUID = 2560010704401134710L;

	private final PatternMatchingBehavior matcher = new PatternMatchingBehavior(this);
	
	public void caseWithRef(Ref r) {
		System.out.println(me().pull() + " : got ref of " + r);

		/*
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		send().send(new Other("ahahah"), r);

		System.out.println(me().pull() + " : die");
		die().doIt();
	}
	
	@Override
	protected Push<PatternMatchingMessage> cycle() {
		return new Push<PatternMatchingMessage>() {
			public void push(PatternMatchingMessage thing) {
				matcher.match(thing);
			}
		};
	}
}
