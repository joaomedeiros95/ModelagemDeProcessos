package br.ufrn.info.bpm.progressao.teste;

import br.ufrn.info.bpm.api.BPMIntegrityException;
import br.ufrn.info.bpm.progressao.Progressao;
import br.ufrn.info.bpm.progressao.ProgressaoDAO;
import br.ufrn.info.bpm.progressao.ProgressaoProcAPI;
import br.ufrn.info.bpm.progressao.TarefaProgressao;

public class MainProgressao {

	// EXEMPLO DE USO
	public static void main(String[] args) throws BPMIntegrityException {
		long idPedidoProgressao = (long) (Math.random()*10000);
		
		Progressao progressao = new Progressao();
		progressao.setId(idPedidoProgressao);
		
		ProgressaoDAO.getInstance().adicionarProgressao(progressao);

		ProgressaoProcAPI api = ProgressaoProcAPI.getInstance();

		// EMITIR NOVO RID
		api.startNewProcessInstance(idPedidoProgressao);
//		api.startTask(TarefaProgressao.ORGANIZAR_PEDIDO, idPedidoProgressao);

		
		// Ao submeter RID
		api.completeTask(TarefaProgressao.ORGANIZAR_PEDIDO, idPedidoProgressao);
		
		// startProcessInstance(idProjeto);

		// Chefia ao avaliar  RID
		progressao.setParecerOk(true);
		api.completeTask(TarefaProgressao.EMITIR_PARECER_CHEFIA, idPedidoProgressao);
		
		// Relator ao avaliar progressao
		progressao.setParecerRelatorOk(true);
		api.completeTask(TarefaProgressao.EMITIR_PARECER_RELATOR, idPedidoProgressao);

		// Após relator autenticar
		api.completeTask(TarefaProgressao.ASSINAR_PARECER, idPedidoProgressao);
		
		System.out.println("fim");
	}
}
