package br.ufrn.info.bpm.progressao;

import java.util.ArrayList;
import java.util.List;

public class ProgressaoDAO {

	private static ProgressaoDAO instance;
	private List<Progressao> listaProgressao = new ArrayList<Progressao>();
	
	public static synchronized ProgressaoDAO getInstance() {
		if (instance == null) {
			instance = new ProgressaoDAO();
		}
		return instance;
	}
	
	private ProgressaoDAO() {
	}
	
	public void adicionarProgressao(Progressao p) {
		listaProgressao.add(p);
	}

	
	public Progressao procurarProgressao(long sigId) {
		Progressao prog = null;
		for (Progressao progressao : listaProgressao) {
			if (progressao.getId() == sigId) {
				prog = progressao;
				break;
			}
		}
		if (prog == null) {
			throw new RuntimeException("Progressão não encontrada: " + sigId);
		}
		return prog;
	}

	public boolean isTodosAutenticaram() {
		// simulando que todos sempre autenticaram
		return true; 
	}

}
