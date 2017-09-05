package br.ufrn.info.bpm.progressao;

import br.ufrn.info.bpm.api.KeySigObject;

public class Progressao implements KeySigObject{
	private long id;
	private Boolean parecerOk;
	private Boolean parecerRelatorOk;

	public long getId() {
		return id;
	}

	public Boolean getParecerOk() {
		return parecerOk;
	}

	public void setParecerOk(Boolean parecerOk) {
		this.parecerOk = parecerOk;
	}

	public Boolean getParecerRelatorOk() {
		return parecerRelatorOk;
	}

	public void setParecerRelatorOk(Boolean parecerRelatorOk) {
		this.parecerRelatorOk = parecerRelatorOk;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
