package br.ufrn.info.bpm.progressao;

import br.ufrn.info.bpm.api.GatewayObject;

public class GatewayProgressao extends GatewayObject<Progressao> {
	
	public GatewayProgressao(Progressao progressao) {
		super(progressao);
	}

	public boolean empresaPronunciou() {
		return false;
	}
	
}
