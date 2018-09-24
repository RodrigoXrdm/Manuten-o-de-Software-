package ufc.npi.prontuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Estrutura;
import ufc.npi.prontuario.model.Face;
import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.model.enums.TipoEstrutura;

@DatabaseSetup(EstruturaServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { EstruturaServiceTest.DATASET })
public class EstruturaServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-estrutura.xml";

	@Autowired
	private EstruturaService estruturaService;

	@Autowired
	private PacienteService pacienteService;

	@Test
	public void testbuscarPorId() {
		Estrutura boca = estruturaService.buscarPorId(1);
		assertEquals((Integer) 1, boca.getId());
		assertEquals(TipoEstrutura.BOCA, boca.getTipoEstrutura());
		assertEquals((Integer) 1, boca.getPaciente().getId());

		Estrutura arcada = estruturaService.buscarPorId(2);
		assertEquals((Integer) 2, arcada.getId());
		assertEquals(TipoEstrutura.ARCADA, arcada.getTipoEstrutura());
		assertEquals((Integer) 1, arcada.getPaciente().getId());
	}

	@Test
	public void testSalvar() {
		Estrutura dente = estruturaService.buscarPorId(11);
		Face face = new Face();
		face.setDente((Dente) dente);
		face.setNome(NomeFace.L);
		face.setPaciente(pacienteService.buscarPorId(1));
		face.setTipoEstrutura(TipoEstrutura.FACE);

		estruturaService.salvar(face);
	}
}
