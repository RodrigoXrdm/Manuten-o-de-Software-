package ufc.npi.prontuario.service;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Disciplina;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PlanoTratamento;
import ufc.npi.prontuario.model.Servidor;
import ufc.npi.prontuario.model.enums.*;

@DatabaseSetup(PlanoTratamentoServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { PlanoTratamentoServiceTest.DATASET })
public class PlanoTratamentoServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-planoTratamento.xml";

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private PlanoTratamentoService planoTratamentoService;

	@Autowired
	private DisciplinaService disciplinaService;

	@Autowired
	private ServidorService servidorService;

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testBuscarPlanoTratamentoPorId() {
		Paciente paciente = pacienteService.buscarPorId(2);
		PlanoTratamento plano = planoTratamentoService.buscarPlanoPorId(4);

		assertEquals((Integer) 4, plano.getId());
		assertEquals((Integer) 1, plano.getPrioridade());
		assertEquals(paciente, plano.getPaciente());
		assertEquals(StatusPlanoTratamento.EM_ESPERA, plano.getStatus());
		assertEquals((Integer) 2, plano.getClinica().getId());
		assertEquals("disciplina2", plano.getClinica().getNome());
		assertEquals("2", plano.getClinica().getCodigo());
		assertEquals(1, plano.getClinica().getTurmas().size());
		assertEquals((Integer) 4, plano.getResponsavel().getId());
		assertEquals("2018-08-15 00:00:00.0", plano.getData().toString());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testBuscarPlanoTratamentoPorPaciente() {
		Paciente paciente = pacienteService.buscarPorId(2);
		List<PlanoTratamento> planoTratamentoList = planoTratamentoService.buscarPlanoTratamentoPorPaciente(paciente);
		assertEquals(4, planoTratamentoList.size());

		PlanoTratamento plano = planoTratamentoList.get(0);

		assertEquals((Integer) 4, plano.getId());
		assertEquals((Integer) 1, plano.getPrioridade());
		assertEquals(paciente, plano.getPaciente());
		assertEquals(StatusPlanoTratamento.EM_ESPERA, plano.getStatus());
		assertEquals((Integer) 2, plano.getClinica().getId());
		assertEquals("disciplina2", plano.getClinica().getNome());
		assertEquals("2", plano.getClinica().getCodigo());
		assertEquals(1, plano.getClinica().getTurmas().size());
		assertEquals((Integer) 4, plano.getResponsavel().getId());
		assertEquals("2018-08-15 00:00:00.0", plano.getData().toString());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testExcluirPlanoTratamento() {
		PlanoTratamento plano = planoTratamentoService.buscarPlanoPorId(4);
		try {
			planoTratamentoService.excluirPlanoTratamento(plano);
			plano = planoTratamentoService.buscarPlanoPorId(4);
			assertNull(plano);
		} catch (ProntuarioException e) {
			fail("O plano de tratamento deveria ter sido excluído.");
			e.printStackTrace();
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testExcluirPlanoTratamentoEmAndamento() {
		PlanoTratamento plano = planoTratamentoService.buscarPlanoPorId(5);
		try {
			planoTratamentoService.excluirPlanoTratamento(plano);
			fail("O plano de tratamento não deveria ter sido excluído.");
		} catch (ProntuarioException e) {
			assertEquals(ERROR_EXCLUIR_PLANO_TRATAMENTO, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarPlanoDeTratamento() {
		Disciplina clinica = disciplinaService.buscarPorId(1);
		Paciente paciente = pacienteService.buscarPorId(3);
		Servidor responsavel = servidorService.buscarPorId(3);

		PlanoTratamento planoTratamento = new PlanoTratamento();
		Date data = new Date();
		planoTratamento.setData(data);
		planoTratamento.setPrioridade(4);
		planoTratamento.setStatus(StatusPlanoTratamento.EM_ESPERA);
		planoTratamento.setClinica(clinica);
		planoTratamento.setPaciente(paciente);
		planoTratamento.setResponsavel(responsavel);

		try {
			planoTratamentoService.salvar(planoTratamento, responsavel, paciente);
		} catch (ProntuarioException e) {
			fail("Errro! O plano de tratamento deveria ter sido salvo.");
			e.printStackTrace();
		}

		PlanoTratamento plano = planoTratamentoService.buscarPlanoTratamentoPorPaciente(paciente).get(0);
		assertEquals((Integer) 8, plano.getId());
		assertEquals((Integer) 4, plano.getPrioridade());
		assertEquals(paciente, plano.getPaciente());
		assertEquals(StatusPlanoTratamento.EM_ESPERA, plano.getStatus());
		assertEquals((Integer) 1, plano.getClinica().getId());
		assertEquals("disciplina1", plano.getClinica().getNome());
		assertEquals("1", plano.getClinica().getCodigo());
		assertEquals(1, plano.getClinica().getTurmas().size());
		assertEquals((Integer) 3, plano.getResponsavel().getId());
		assertEquals(data, plano.getData());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarPlanoDeTratamentoParaPacienteComAtendimentoAtivoEmClinica() {
		Disciplina clinica = disciplinaService.buscarPorId(2);
		Paciente paciente = pacienteService.buscarPorId(2);
		Servidor responsavel = servidorService.buscarPorId(3);

		PlanoTratamento planoTratamento = new PlanoTratamento();
		Date data = new Date();
		planoTratamento.setData(data);
		planoTratamento.setPrioridade(4);
		planoTratamento.setStatus(StatusPlanoTratamento.EM_ESPERA);
		planoTratamento.setClinica(clinica);
		planoTratamento.setPaciente(paciente);
		planoTratamento.setResponsavel(responsavel);

		try {
			planoTratamentoService.salvar(planoTratamento, responsavel, paciente);
			fail("O plano de tratamento não deveria ter sido criado pois existe atendimento ativo na mesma clinica.");
		} catch (ProntuarioException e) {
			assertEquals(ERRO_ADD_PLANO_TRATAMENTO, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testFinalizarPlanoDeTratamento() {
		Paciente paciente = pacienteService.buscarPorId(2);
		PlanoTratamento plano = planoTratamentoService.buscarPlanoTratamentoPorPaciente(paciente).get(0);
		assertEquals(StatusPlanoTratamento.EM_ESPERA, plano.getStatus());

		try {
			planoTratamentoService.finalizar(plano);
			plano = planoTratamentoService.buscarPlanoPorId(4);
			assertEquals(StatusPlanoTratamento.CONCLUIDO, plano.getStatus());
		} catch (ProntuarioException e) {
			fail("Erro! Deveria ter finalizado o plano de tratamento.");
			e.printStackTrace();
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testFinalizarPlanoDeTratamentoSemEstarEmEspera() {
		Paciente paciente = pacienteService.buscarPorId(2);
		PlanoTratamento plano = planoTratamentoService.buscarPlanoTratamentoPorPaciente(paciente).get(1);
		assertEquals(StatusPlanoTratamento.EM_ANDAMENTO, plano.getStatus());

		try {
			planoTratamentoService.finalizar(plano);
			fail("Erro! Não deveria ter finalizado o plano de tratamento.");
		} catch (ProntuarioException e) {
			assertEquals(ERROR_FINALIZAR_PLANO_TRATAMENTO, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testBuscarPlanoTratamentoPorClinicaEStatus() {
		Disciplina disciplina = disciplinaService.buscarPorId(1);
		Paciente paciente = pacienteService.buscarPorId(1);
		Servidor responsavel = servidorService.buscarPorId(3);
		List<PlanoTratamento> lista = planoTratamentoService.buscarPlanoTratamentoPorClinicaEStatus(disciplina,
				StatusPlanoTratamento.EM_ESPERA.toString());

		PlanoTratamento plano = lista.get(0);
		assertEquals((Integer) 1, plano.getId());
		assertEquals((Integer) 1, plano.getPrioridade());
		assertEquals(paciente, plano.getPaciente());
		assertEquals(StatusPlanoTratamento.EM_ESPERA, plano.getStatus());
		assertEquals(disciplina, plano.getClinica());
		assertEquals(responsavel, plano.getResponsavel());
		assertEquals("2018-08-16 00:00:00.0", plano.getData().toString());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testBuscarPlanoTratamentoPorClinicaSemStatus() {
		Disciplina disciplina = disciplinaService.buscarPorId(1);
		Paciente paciente = pacienteService.buscarPorId(1);
		Servidor responsavel = servidorService.buscarPorId(3);
		List<PlanoTratamento> lista = planoTratamentoService.buscarPlanoTratamentoPorClinicaEStatus(disciplina, "");

		PlanoTratamento plano = lista.get(0);
		assertEquals((Integer) 1, plano.getId());
		assertEquals((Integer) 1, plano.getPrioridade());
		assertEquals(paciente, plano.getPaciente());
		assertEquals(StatusPlanoTratamento.EM_ESPERA, plano.getStatus());
		assertEquals(disciplina, plano.getClinica());
		assertEquals(responsavel, plano.getResponsavel());
		assertEquals("2018-08-16 00:00:00.0", plano.getData().toString());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testEditarPlanoTratamento() {
		PlanoTratamento plano = planoTratamentoService.buscarPlanoPorId(4);

		try {
			plano.setStatus(StatusPlanoTratamento.EM_ANDAMENTO);
			plano.setPrioridade(3);
			planoTratamentoService.editar(plano);
			plano = planoTratamentoService.buscarPlanoPorId(4);
			assertEquals((Integer) 4, plano.getId());
			assertEquals((Integer) 3, plano.getPrioridade());
			assertEquals((Integer) 2, plano.getPaciente().getId());
			assertEquals(StatusPlanoTratamento.EM_ANDAMENTO, plano.getStatus());
			assertEquals((Integer) 2, plano.getClinica().getId());
			assertEquals((Integer) 4, plano.getResponsavel().getId());
			assertEquals("2018-08-15 00:00:00.0", plano.getData().toString());
		} catch (ProntuarioException e) {
			fail("O plano de tratamento deveria ter sido editado");
			e.printStackTrace();
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testEditarPlanoTratamentoInterrompido() {
		PlanoTratamento plano = planoTratamentoService.buscarPlanoPorId(6);
		plano.setPrioridade(8);
		try {
			planoTratamentoService.editar(plano);
			fail("O plano de tratamento não deveria ter sido editado pois está interrompido.");
		} catch (ProntuarioException e) {
			assertEquals(ERROR_EDITAR_PLANO_TRATAMENTO, e.getMessage());
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testEditarPlanoTratamentoConcluido() {
		PlanoTratamento plano = planoTratamentoService.buscarPlanoPorId(7);
		plano.setPrioridade(8);
		try {
			planoTratamentoService.editar(plano);
			fail("O plano de tratamento não deveria ter sido editado pois está concluído.");
		} catch (ProntuarioException e) {
			assertEquals(ERROR_EDITAR_PLANO_TRATAMENTO, e.getMessage());
		}
	}

}
