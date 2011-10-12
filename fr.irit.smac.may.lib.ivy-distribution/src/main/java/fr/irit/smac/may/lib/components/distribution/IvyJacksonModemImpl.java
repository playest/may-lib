package fr.irit.smac.may.lib.components.distribution;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import fr.irit.smac.may.lib.components.distribution.ivy.IvyConnectionConfig;
import fr.irit.smac.may.lib.interfaces.Pull;
import fr.irit.smac.may.lib.interfaces.Push;
import fr.irit.smac.may.lib.interfaces.Send;

public class IvyJacksonModemImpl<Msg> extends IvyJacksonModem<Msg> {

	private final static ObjectMapper mapper = new ObjectMapper();
	
	private final TypeReference<Msg> clazz;
	
	private volatile int i = 0;
	
	private final String platformName;
	private final String broadCastAdress;
	private final String port;

	
	public IvyJacksonModemImpl(String platformName, String broadCastAdress, String port, TypeReference<Msg> clazz) {
		this.platformName = platformName;
		this.broadCastAdress = broadCastAdress;
		this.port = port;
		this.clazz = clazz;
	}
	
	@Override
	protected void start() {
		super.start();
		ivyConnect().push(new IvyConnectionConfig(broadCastAdress, port, platformName, platformName));
		ivyBindMsg().push("IvyJacksonModemImpl: ^([:alnum:]*)@([:alnum:]*): (.*)$");
	}
	
	@Override
	protected Pull<DistRef> generateRef() {
		return new Pull<DistRef>() {
			public DistRef pull() {
				int me = i++;
				return new DistRef("agent"+me,platformName);
			}
		};
	}

	@Override
	protected Send<Msg, DistRef> send() {
		return new Send<Msg, DistRef>() {
			public void send(Msg message, DistRef receiver) {
				String m;
				try {
					m = mapper.writeValueAsString(message);
					ivySend().push("IvyJacksonModemImpl: " + receiver.name + "@" + receiver.platform + ": " + m);
				} catch (JsonGenerationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		};
	}

	@Override
	protected Push<List<String>> ivyReceive() {
		return new Push<List<String>>() {
			public void push(List<String> thing) {
				// there should be 3 parts: name platform and message
				if (thing.get(1).equals(platformName)) {
					DistRef r = new DistRef(thing.get(0), thing.get(1));
					try {
						Msg m = mapper.readValue(thing.get(2), clazz);
						deposit().send(m, r);
					} catch (JsonParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		};
	}

}
