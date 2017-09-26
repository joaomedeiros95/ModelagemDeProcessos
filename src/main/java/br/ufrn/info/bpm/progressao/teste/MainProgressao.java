package br.ufrn.info.bpm.progressao.teste;

import br.ufrn.info.bpm.api.BPMIntegrityException;
import br.ufrn.info.bpm.progressao.Progressao;
import br.ufrn.info.bpm.progressao.ProgressaoDAO;
import br.ufrn.info.bpm.progressao.ProgressaoProcAPI;
import br.ufrn.info.bpm.progressao.FiscalizacaoContratos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class MainProgressao {

    private static Map<Integer, FiscalizacaoContratos> processos =
            new ConcurrentHashMap<>();

    static {
        processos.put(1, FiscalizacaoContratos.ENVIAR_MEMORANDO);
        processos.put(2, FiscalizacaoContratos.ENVIAR_INFO_FISCAL);
        processos.put(3, FiscalizacaoContratos.PORTARIA);
        processos.put(4, FiscalizacaoContratos.OCORRENCIA);
        processos.put(5, FiscalizacaoContratos.OCORRENCIA_SIPAC);
        processos.put(6, FiscalizacaoContratos.PEDIR_PRONUNCIAMENTO);
        processos.put(7, FiscalizacaoContratos.RECEBER_PRONUNCIAMENTO);
        processos.put(8, FiscalizacaoContratos.TOMAR_PROVIDENCIAS);
    }

    public FiscalizacaoContratos passoAtual;

    // EXEMPLO DE USO
    public void start() throws BPMIntegrityException {
		final long idPedidoProgressao = (long) (Math.random() * 10000);

		final Progressao progressao = new Progressao();
		progressao.setId(idPedidoProgressao);

		ProgressaoDAO.getInstance().adicionarProgressao(progressao);

		final ProgressaoProcAPI api = ProgressaoProcAPI.getInstance();

        // EMITIR NOVO RID
		api.startNewProcessInstance(idPedidoProgressao);

        final List infoFiscal = new ArrayList();
        final List portaria = new ArrayList();
        final List ocorrencia = new ArrayList();
        final List sipac = new ArrayList();
        final List pedirPronunciamento = new ArrayList();
        final List receberPronunciamento = new ArrayList();
        final List providencia = new ArrayList();

        final Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("------------MENU--------------------------------");
            System.out.println("ENVIAR MEMORANDO - DIGITE 1 ");
            System.out.println("ENVIAR INFO. FISCAL - DIGITE 2 ");
            System.out.println("FAZER PORTARIA - DIGITE 3 ");
            System.out.println("ENVIAR OCORRENCIA - DIGITE 4 ");
            System.out.println("REGISTRAR SIPAC - DIGITE 5 ");
            System.out.println("PEDIR PRONUNCIAMENTO - DIGITE 6 ");
            System.out.println("RECEBER PRONUNCIAMENTO - DIGITE 7 ");
            System.out.println("TOMAR PROVIDENCIA - DIGITE 8 ");
            System.out.println("------------------------------------------------");

            final int acao = sc.nextInt();

            final FiscalizacaoContratos task = processos.get(acao);
            final FiscalizacaoContratos taskAnterior = processos.get(acao - 1);

            if (taskAnterior != null) {
                api.completeTask(taskAnterior, idPedidoProgressao);
            }

            if (task != null) {
                api.startTask(task, idPedidoProgressao);
            }

            if (acao == 1) {
                System.out.println("DIGITE O NOME DO MEMORANDO:");
                String nome = sc.next();
                infoFiscal.add(nome);
                System.out.println("MEMORANDO ENVIADO");
            } else if (acao == 2 && infoFiscal.size() != 0) {
                System.out.println("MEMORANDOS:");
                System.out.println(infoFiscal);
                System.out.println("DIGITE O NOME DO MEMORANDO PARA ENVIAR AS INFORMACOES FISCAIS:");
                String nome = sc.next();
                infoFiscal.remove(nome);
                portaria.add(nome);
                System.out.println("INFORMACOES ENVIADAS");
            } else if (acao == 3 && portaria.size() != 0) {
                System.out.println("MEMORANDOS:");
                System.out.println(portaria);
                System.out.println("DIGITE O NOME DO MEMORANDO PARA FAZER A PORTARIA:");
                String nome = sc.next();
                portaria.remove(nome);
                ocorrencia.add(nome);
                System.out.println("PORTARIA REALIZADA");
            } else if (acao == 4 && ocorrencia.size() != 0) {
                System.out.println("MEMORANDOS:");
                System.out.println(ocorrencia);
                System.out.println("DIGITE O NOME DO MEMORANDO PARA ENVIAR AS OCORRENCIAS:");
                String nome = sc.next();
                ocorrencia.remove(nome);
                sipac.add(nome);
                System.out.println("OCORRENCIA ENVIADA");
            } else if (acao == 5 && sipac.size() != 0) {
                System.out.println("MEMORANDOS:");
                System.out.println(sipac);
                System.out.println("DIGITE O NOME DO MEMORANDO PARA REGISTRA NO SIPAC:");
                String nome = sc.next();
                sipac.remove(nome);
                pedirPronunciamento.add(nome);
                System.out.println("REGISTRADO NO SIPAC");
            } else if (acao == 6 && pedirPronunciamento.size() != 0) {
                System.out.println("MEMORANDOS:");
                System.out.println(pedirPronunciamento);
                System.out.println("DIGITE O NOME DO MEMORANDO PARA PEDIR PRONUNCIAMENTO:");
                String nome = sc.next();
                pedirPronunciamento.remove(nome);
                receberPronunciamento.add(nome);
                System.out.println("PEDIDO REALIZADO");
            } else if (acao == 7 && receberPronunciamento.size() != 0) {
                System.out.println("MEMORANDOS:");
                System.out.println(receberPronunciamento);
                System.out.println("DIGITE O NOME DO MEMORANDO PARA INFORMAR PRONUNCIAMENTO:");
                String nome = sc.next();
                pedirPronunciamento.remove(nome);
                System.out.println("PARA ESSE PROCESSO A EMPRESA SE PRONUNCIOU? DIGITE 1 PARA SIM E 2 PARA NAO");
                int acao2 = sc.nextInt();

                if (acao2 == 1) {
                    receberPronunciamento.remove(nome);
                    System.out.println("###PROCESSO FINALIZADO###");
                } else if (acao2 == 2) {
                    receberPronunciamento.remove(nome);
                    providencia.add(nome);
                    System.out.println("PEDIDO DE PROVIDENCIA REALIZADO");
                }
            } else if (acao == 8 && providencia.size() != 0) {
                System.out.println("MEMORANDOS:");
                System.out.println(providencia);
                System.out.println("DIGITE O NOME DO MEMORANDO PARA TOMAR PREVIDENCIA:");
                String nome = sc.next();
                providencia.remove(nome);
                System.out.println("###PROCESSO FINALIZADO###");
            } else if (acao == -1) {
                System.out.println("infoFiscal");
                System.out.println(infoFiscal);
                System.out.println("portaria");
                System.out.println(portaria);
                System.out.println("ocorrencia");
                System.out.println(ocorrencia);
                System.out.println("sipac");
                System.out.println(sipac);
                System.out.println("pedirPronunciamento");
                System.out.println(pedirPronunciamento);
                System.out.println("receberPronunciamento");
                System.out.println(receberPronunciamento);
                System.out.println("providencia");
                System.out.println(providencia);
            } else {
                System.out.printf("Opção não disponível, tente novamente");
            }

            passoAtual = task;
        }
    }

}
