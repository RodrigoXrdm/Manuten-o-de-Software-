package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

import ufc.npi.prontuario.model.Dente;
import ufc.npi.prontuario.model.Face;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Sextante;
import ufc.npi.prontuario.model.enums.NomeFace;
import ufc.npi.prontuario.model.enums.TipoEstrutura;

@DatabaseSetup(FaceServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { FaceServiceTest.DATASET })
public class FaceServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-face.xml";

	@Autowired
	private FaceService faceService;

	@Autowired
	private DenteService denteService;

	@Autowired
	private SextanteService sextanteService;

	@Autowired
	private PacienteService pacienteService;

	@Test
	public void testSalvarFace() {
		Paciente paciente = pacienteService.buscarPorId(1);
		Dente dente = denteService.getByPaciente(paciente).get(0);
		Face face = new Face();
		face.setDente(dente);
		face.setNome(NomeFace.V);
		face.setPaciente(paciente);
		face.setTipoEstrutura(TipoEstrutura.FACE);
		faceService.salvar(face);
	}

	@Test
	public void testBuscarPorId() {
		Face face = faceService.buscarPorId(20);
		assertEquals(NomeFace.O, face.getNome());
		assertEquals((Integer) 13, face.getDente().getId());
		assertEquals((Integer) 1, face.getPaciente().getId());
		assertEquals(TipoEstrutura.FACE, face.getTipoEstrutura());
	}

	@Test
	public void criarFaces() {
		Paciente paciente = pacienteService.buscarPorId(2);
		Sextante sextante = sextanteService.findAllByPacienteOrderByNomeAsc(paciente).get(0);
		Dente dente = sextante.getDentes().get(0);
		List<Face> faces = faceService.criarFaces(paciente, dente);

		Face face = faces.get(0);
		assertEquals(NomeFace.V, face.getNome());
		assertEquals((Integer) 15, face.getDente().getId());
		assertEquals((Integer) 2, face.getPaciente().getId());
		assertEquals(TipoEstrutura.FACE, face.getTipoEstrutura());

		face = faces.get(1);
		assertEquals(NomeFace.L, face.getNome());
		assertEquals((Integer) 15, face.getDente().getId());
		assertEquals((Integer) 2, face.getPaciente().getId());
		assertEquals(TipoEstrutura.FACE, face.getTipoEstrutura());

		face = faces.get(2);
		assertEquals(NomeFace.M, face.getNome());
		assertEquals((Integer) 15, face.getDente().getId());
		assertEquals((Integer) 2, face.getPaciente().getId());
		assertEquals(TipoEstrutura.FACE, face.getTipoEstrutura());

		face = faces.get(3);
		assertEquals(NomeFace.D, face.getNome());
		assertEquals((Integer) 15, face.getDente().getId());
		assertEquals((Integer) 2, face.getPaciente().getId());
		assertEquals(TipoEstrutura.FACE, face.getTipoEstrutura());

		face = faces.get(4);
		assertEquals(NomeFace.O, face.getNome());
		assertEquals((Integer) 15, face.getDente().getId());
		assertEquals((Integer) 2, face.getPaciente().getId());
		assertEquals(TipoEstrutura.FACE, face.getTipoEstrutura());
	}

}
