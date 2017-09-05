package br.ufrn.info.bpm.progressao.teste;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.camunda.bpm.engine.repository.Deployment;

import br.ufrn.info.bpm.progressao.ProgressaoProcAPI;

public class DeployModel {

	public static void main(String[] args) throws FileNotFoundException {
		String model = "progressao-funcional.bpmn"; // COLOCAR AQUI O CAMINHO DO ARQUIVO .BPMN
		InputStream is = new FileInputStream(new File(model));
		Deployment d = ProgressaoProcAPI.getInstance().deployDiagram(is);
	}

}
