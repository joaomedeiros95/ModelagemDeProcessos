package br.ufrn.info.bpm.progressao.teste;

import br.ufrn.info.bpm.api.BPMIntegrityException;
import br.ufrn.info.bpm.progressao.Progressao;
import br.ufrn.info.bpm.progressao.ProgressaoDAO;
import br.ufrn.info.bpm.progressao.ProgressaoProcAPI;
import br.ufrn.info.bpm.progressao.FiscalizacaoContratos;

public class MainProgressao {

	// EXEMPLO DE USO
	public static void main(String[] args) throws BPMIntegrityException {
		final long idPedidoProgressao = (long) (Math.random()*10000);
		
		Progressao progressao = new Progressao();
		progressao.setId(idPedidoProgressao);
		
		ProgressaoDAO.getInstance().adicionarProgressao(progressao);

		ProgressaoProcAPI api = ProgressaoProcAPI.getInstance();

		// EMITIR NOVO RID
		api.startNewProcessInstance(idPedidoProgressao);
		api.startTask(FiscalizacaoContratos.ENVIAR_MEMORANDO, idPedidoProgressao);

		
		// Ao submeter RID
//		api.completeTask(FiscalizacaoContratos.ORGANIZAR_PEDIDO, idPedidoProgressao);
		
		// startProcessInstance(idProjeto);

		// Chefia ao avaliar  RID
		progressao.setParecerOk(true);
//		api.completeTask(FiscalizacaoContratos.EMITIR_PARECER_CHEFIA, idPedidoProgressao);
		
		// Relator ao avaliar progressao
		progressao.setParecerRelatorOk(true);
//		api.completeTask(FiscalizacaoContratos.EMITIR_PARECER_RELATOR, idPedidoProgressao);

		// Após relator autenticar
//		api.completeTask(FiscalizacaoContratos.ASSINAR_PARECER, idPedidoProgressao);
		
		System.out.println("fim");
	}
}
