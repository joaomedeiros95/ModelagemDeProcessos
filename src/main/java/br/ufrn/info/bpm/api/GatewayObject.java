package br.ufrn.info.bpm.api;

import java.io.Serializable;

/**
 * Classe que representa a interface de comunicação de uma instância de processo com o SIG.
 * 
 * @author Eduardo
 */
public abstract class GatewayObject<T extends KeySigObject> implements Serializable {
	
	/**
	 * Chave do objeto do sistema SIG que será vinculado ao processo que interage com este gateway.
	 */
	private long sigId;
	
	/**
	 * Cria um objeto gateway que será utilizado
	 * @param sigId
	 */
	public GatewayObject(T keySigObject) {
		this.sigId = keySigObject.getId();
	}

	/**
	 * Retorna a chave do objeto do sistema SIG que será vinculado ao processo que interage com este gateway.
	 */
	protected long getSigId() {
		return sigId;
	}
}
