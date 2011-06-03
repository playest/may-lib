package fr.irit.smac.may.lib.components.remrefreceive.impl;

import java.io.Serializable;

import fr.irit.smac.may.lib.components.remplace.impl.Place;

public class RemoteAgentRef<Msg> implements Serializable {

	private static final long serialVersionUID = 3786174379034488447L;
	
	final RemoteAgent<Msg> agent;

	private final Place place;

	private final String name;

	RemoteAgentRef(RemoteAgent<Msg> agent, Place place, String name) {
		this.place = place;
		this.name = name;
		this.agent = agent;
	}
	
	@Override
	public String toString() {
		return name+"@"+place;
	};
}

