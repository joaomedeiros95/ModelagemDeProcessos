package br.ufrn.info.bpm.progressao;

import br.ufrn.info.bpm.api.GatewayObject;

public class GatewayProgressao extends GatewayObject<Progressao> {
	
	public GatewayProgressao(Progressao progressao) {
		super(progressao);
	}
	
	public boolean isParecerOk() {
		Progressao p = ProgressaoDAO.getInstance().procurarProgressao(getSigId());
		if (p.getParecerOk() == null) {
			throw new RuntimeException("Informação não disponível: getParecerOk()");
		} else {
			return p.getParecerOk().booleanValue();
		}
	}
	
	public boolean isParecerRelatorOk() {
		Progressao p = ProgressaoDAO.getInstance().procurarProgressao(getSigId());
		if (p.getParecerRelatorOk() == null) {
			throw new RuntimeException("Informação não disponível: getParecerRelatorOk()");
		} else {
			return p.getParecerRelatorOk().booleanValue();
		}
	}
	
	public boolean isTodosAutenticaram() {
		return ProgressaoDAO.getInstance().isTodosAutenticaram();
	}
	
}
