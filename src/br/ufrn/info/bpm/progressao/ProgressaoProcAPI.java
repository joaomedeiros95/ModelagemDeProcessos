package br.ufrn.info.bpm.progressao;

import br.ufrn.info.bpm.api.GenericProcessAPI;

public class ProgressaoProcAPI extends GenericProcessAPI<FiscalizacaoContratos> {

	private static ProgressaoProcAPI instance;
	
	public static synchronized ProgressaoProcAPI getInstance() {
		if (instance == null) {
			instance = new ProgressaoProcAPI();
		}
		return instance;
	}
	
	private ProgressaoProcAPI() {
		super("Process_1");
	}
	
	@Override
	protected Object getGatewayObject(long sigId) {
		Progressao p = new Progressao();
		p.setId(sigId);
		return new GatewayProgressao(p);
	}
}
