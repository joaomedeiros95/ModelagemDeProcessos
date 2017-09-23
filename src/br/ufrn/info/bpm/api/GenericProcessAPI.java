package br.ufrn.info.bpm.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Lane;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.UserTask;

public abstract class GenericProcessAPI<T> {

    private static final String SIG_ID_PARAM_NAME = "sigID";
    private static final String START_EXECUTION_TIME_VAR = "startExecTime";
    private static final String GATEWAY_VAR = "proc";

    private static ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration()
            .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
            .setJdbcUrl("jdbc:mysql://localhost:3306/bpm_integration2")
            .setJdbcDriver("com.mysql.jdbc.Driver")
            .setJdbcUsername("root").setJdbcPassword("123456")
            .setJobExecutorActivate(true)
            .buildProcessEngine();


    private String procDefId;

    public GenericProcessAPI(String prcDefId) {
        this.procDefId = prcDefId;
    }

    protected abstract Object getGatewayObject(long sigId);

    /**
     * Criar nova instância do processo, usando a versão mais atual de sua modelagem BPMN.
     *
     * @param sigId Chave do objeto do sistema SIG que será vinculado a este processo.
     * @throws BPMIntegrityException
     */
    public void startNewProcessInstance(long sigId) throws BPMIntegrityException {
        createProcessInstanceId(sigId);
    }

    public void startTask(T task, long sigId) throws BPMIntegrityException {
        String procInstId = getOrCreateProcessInstanceId(sigId);
        Task found = getNextTaskByName(procInstId, task.toString());
        processEngine.getTaskService().setVariableLocal(found.getId(), START_EXECUTION_TIME_VAR, new Date());
    }

    public void completeTask(T task, long sigId) throws BPMIntegrityException {
        executeTask(sigId, task.toString());
    }

    // TODO: verificar como dar o synchronized em m�quinas distribu�das
    private synchronized String createProcessInstanceId(long sigId)
            throws BPMIntegrityException {
        return getOrCreateProcessInstanceId(sigId, true);
    }

    private synchronized String getOrCreateProcessInstanceId(long sigId, boolean haveToBeNew)
            throws BPMIntegrityException {
        ProcessInstance p = processEngine.getRuntimeService().createProcessInstanceQuery()
                .processDefinitionKey(procDefId).variableValueEquals(SIG_ID_PARAM_NAME, sigId).singleResult();
        if (p == null) {
            HashMap<String, Object> variables = new HashMap<>();
            variables.put(SIG_ID_PARAM_NAME, sigId);
            variables.put(GATEWAY_VAR, getGatewayObject(sigId));
            p = processEngine.getRuntimeService().startProcessInstanceByKey(
                    procDefId, variables);
            System.out.println("instancia criada -> " + sigId + ", " + p.getId());
        } else {
            if (haveToBeNew) {
                throw new BPMIntegrityException("sigId j� em uso");
            }
        }
        return p.getId();
    }

    // TODO ver problema de sincroniza��o com m�quinas distribuidas
    // TODO ver se troca o getOrCreate por apenas get
    private void executeTask(long sigId, String taskName)
            throws BPMIntegrityException {
        String procInstId = getOrCreateProcessInstanceId(sigId);
        Task found = getNextTaskByName(procInstId, taskName);
        processEngine.getTaskService().complete(found.getId());
    }

    // TODO: verificar como dar o synchronized em m�quinas distribu�das
    private synchronized String getOrCreateProcessInstanceId(long sigId)
            throws BPMIntegrityException {
        return getOrCreateProcessInstanceId(sigId, false);
    }

    private Task getNextTaskByName(String procInstId, String taskName)
            throws BPMIntegrityException {
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().processInstanceId(procInstId).list();
        Task found = null;
        if (taskList.isEmpty()) {
            throw new BPMIntegrityException(
                    "Nenhuma tarefa disponível para instância de processo de id "
                            + procInstId);
        }
        for (Task task : taskList) {
            if (taskName.equals(task.getTaskDefinitionKey())) {
                found = task;
            }
        }
        if (found == null) {
            throw new BPMIntegrityException("Tarefa " + taskName
                    + " n�o dispon�vel para inst�ncia de processo de id "
                    + procInstId);
        }
        return found;
    }

    public Deployment deployDiagram(InputStream is) {
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(is);

        Process p = (Process) ((Collection<Process>) modelInstance.getModelElementsByType(Process.class)).toArray()[0];
        applyDeploymentRules(p.getId(), modelInstance);

        Bpmn.validateModel(modelInstance);

        return processEngine.getRepositoryService().createDeployment()
                .addModelInstance(p.getId() + ".bpmn", modelInstance).deploy();
    }

    public void applyDeploymentRules(String modelName, BpmnModelInstance modelInstance) {

        //set process id
        Collection<Process> processes = modelInstance.getModelElementsByType(Process.class);
        int pCount = 0;
        for (Process process : processes) {
            String pname = modelName;
            if (processes.size() > 1) {
                pname = pCount + "_" + pname;
            }
            process.setId(pname);
            System.out.println("Process name -> " + pname);
            pCount++;
        }

        //add lane as role
        Collection<Lane> lanes = modelInstance.getModelElementsByType(Lane.class);
        for (Lane lane : lanes) {
            Collection<FlowNode> refs = lane.getFlowNodeRefs();
            for (FlowNode node : refs) {
                if (node instanceof UserTask) {
                    List<String> groups = new ArrayList<String>();
                    List<String> currentCandidateGroups = ((UserTask) node).getCamundaCandidateGroupsList();
                    groups.add(lane.getName());
                    System.out.println("Group name -> " + lane.getName());
                    groups.addAll(currentCandidateGroups);
                    ((UserTask) node).builder().camundaCandidateGroups(groups);
                }
            }
        }
    }

	/*

	// TODO ver se troca o getOrCreate por apenas get



	/*	
//	public void startProcessInstanceIfNotStarted(long sigId) throws BPMIntegrityException {
//			getOrCreateProcessInstanceId(sigId);
//	}

	
	//TODO ver se tira ao colocar em produ��o
	public List<ProcessInstance> getProcessInstances() {
		List<ProcessInstance> lp = processEngine.getRuntimeService().createProcessInstanceQuery()
				.list();
		return lp;		
	}

	//TODO ver se tira ao colocar em produ��o
	public List<Task> getTasks(String procInstId)
			throws BPMIntegrityException {
System.out.println("tarefas do proc -> " + procInstId);
//		String procInstId = getOrCreateProcessInstanceId(ridId);
		return processEngine.getTaskService().createTaskQuery().processInstanceId(procInstId).list();
	}
*/
}
