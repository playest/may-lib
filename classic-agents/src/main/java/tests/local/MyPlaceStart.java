package tests.local;

import tests.messages.Start;
import fr.irit.smac.may.lib.classic.impl.ClassicImpl;
import fr.irit.smac.may.lib.classic.local.Classic;
import fr.irit.smac.may.lib.components.interactions.directreferences.DirRef;
import fr.irit.smac.may.lib.components.remote.place.Place;
import fr.irit.smac.may.lib.pmbehaviour.PatternMatchingMessage;

public class MyPlaceStart {

	public static void main(String[] args) {

		Classic.Component<PatternMatchingMessage> infra = Classic.newComponent(new ClassicImpl<PatternMatchingMessage>());
		
		DirRef starter = infra.create().create(new BehaviorSetup<DirRef>());

		infra.send().send(new Start(new Place(null, 0), new Place(null, 0)), starter);
		
		System.gc();
		
		System.out.println("a");
	}
}
