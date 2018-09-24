package ufc.npi.prontuario.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_CAMPOS_OBRIGATORIOS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_PACIENTE_CNS_EXISTENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_PACIENTE_CPF_EXISTENTE;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_PACIENTE_EXISTENTE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import ufc.npi.prontuario.model.Aluno;
import ufc.npi.prontuario.model.Anamnese;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.PacienteAnamnese;
import ufc.npi.prontuario.model.Pergunta;
import ufc.npi.prontuario.model.Resposta;
import ufc.npi.prontuario.model.enums.Estado;
import ufc.npi.prontuario.model.enums.EstadoCivil;
import ufc.npi.prontuario.model.enums.Raca;
import ufc.npi.prontuario.model.enums.Sexo;

@DatabaseSetup(PacienteServiceTest.DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { PacienteServiceTest.DATASET })
public class PacienteServiceTest extends AbstractServiceTest {

	protected static final String DATASET = "/database-tests-paciente.xml";

	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private AnamneseService anamneseService;

	@Autowired
	private AlunoService alunoService;

	@Test
	public void testBuscarTodosPacientes() {
		List<Paciente> paciente = pacienteService.buscarTudo();
		assertEquals(2, paciente.size());
	}

	@Test
	public void testBuscarPacientePorId() {
		Paciente paciente = pacienteService.buscarPorId(1);
		assertNotNull(paciente);
		assertEquals("SARAH LUISA GOMES DE SOUSA LEMOS", paciente.getNome());
		assertEquals("MARIA LUISA NEGRAO", paciente.getNomeDaMae());
		assertEquals("1952-02-02 00:00:00.0", paciente.getNascimento().toString());
		assertEquals(EstadoCivil.SOLTEIRO, paciente.getEstadoCivil());
		assertEquals("Brasileira", paciente.getNacionalidade());
		assertEquals("Fortaleza", paciente.getNaturalidade());
		assertEquals("85853317636", paciente.getCpf());
		assertEquals("831327015240007", paciente.getCns());
		assertEquals(Sexo.F, paciente.getSexo());
		assertEquals(Raca.BRANCA, paciente.getRaca());
		assertEquals("Médico", paciente.getProfissao());
		assertEquals("MARGARIDA FERREIRA CAVACANTE", paciente.getResponsavel());
		assertEquals("(88) 98888-9999", paciente.getTelefone());
		assertEquals("(88) 99999-8888", paciente.getSegundoTelefone());
		assertEquals("Rua. João Cordeiro, 949 / Apto. 1901", paciente.getEndereco());
		assertEquals("Centro", paciente.getBairro());
		assertEquals("60110-300", paciente.getCep());
		assertEquals("Fortaleza", paciente.getCidade());
		assertEquals(Estado.CE, paciente.getEstado());
		assertEquals("Brasil", paciente.getPais());
	}

	@Test
	public void testBuscarPorCpf() {
		Paciente paciente = pacienteService.buscarPorCpf("85853317636");
		assertNotNull(paciente);
		assertEquals("SARAH LUISA GOMES DE SOUSA LEMOS", paciente.getNome());
		assertEquals("MARIA LUISA NEGRAO", paciente.getNomeDaMae());
		assertEquals("1952-02-02 00:00:00.0", paciente.getNascimento().toString());
		assertEquals(EstadoCivil.SOLTEIRO, paciente.getEstadoCivil());
		assertEquals("Brasileira", paciente.getNacionalidade());
		assertEquals("Fortaleza", paciente.getNaturalidade());
		assertEquals("85853317636", paciente.getCpf());
		assertEquals("831327015240007", paciente.getCns());
		assertEquals(Sexo.F, paciente.getSexo());
		assertEquals(Raca.BRANCA, paciente.getRaca());
		assertEquals("Médico", paciente.getProfissao());
		assertEquals("MARGARIDA FERREIRA CAVACANTE", paciente.getResponsavel());
		assertEquals("(88) 98888-9999", paciente.getTelefone());
		assertEquals("(88) 99999-8888", paciente.getSegundoTelefone());
		assertEquals("Rua. João Cordeiro, 949 / Apto. 1901", paciente.getEndereco());
		assertEquals("Centro", paciente.getBairro());
		assertEquals("60110-300", paciente.getCep());
		assertEquals("Fortaleza", paciente.getCidade());
		assertEquals(Estado.CE, paciente.getEstado());
		assertEquals("Brasil", paciente.getPais());
	}

	@Test
	public void testBuscarPorCns() {
		Paciente paciente = pacienteService.buscarPorCns("827307676390001");
		assertEquals("827307676390001", paciente.getCns());
		assertNotNull(paciente);
		assertEquals("LUIZ ROBERTO BARRADAS BARATA", paciente.getNome());
		assertEquals("MARIA DE LOURDES MARINHO DE OLIVEIRA", paciente.getNomeDaMae());
		assertEquals("1951-01-01 00:00:00.0", paciente.getNascimento().toString());
		assertEquals(EstadoCivil.CASADO, paciente.getEstadoCivil());
		assertEquals("Brasileira", paciente.getNacionalidade());
		assertEquals("Fortaleza", paciente.getNaturalidade());
		assertEquals("52482883474", paciente.getCpf());
		assertEquals("827307676390001", paciente.getCns());
		assertEquals(Sexo.M, paciente.getSexo());
		assertEquals(Raca.PARDA, paciente.getRaca());
		assertEquals("Médico", paciente.getProfissao());
		assertEquals("Ana Paula Rodrigues Henkel", paciente.getResponsavel());
		assertEquals("(88) 98888-9999", paciente.getTelefone());
		assertEquals("(88) 99999-8888", paciente.getSegundoTelefone());
		assertEquals("Rua. Barão de Aracati, 1502 / Apto. 301", paciente.getEndereco());
		assertEquals("Aldeota", paciente.getBairro());
		assertEquals("60115-000", paciente.getCep());
		assertEquals("Fortaleza", paciente.getCidade());
		assertEquals(Estado.CE, paciente.getEstado());
		assertEquals("Brasil", paciente.getPais());
	}

	@Test
	public void testBuscarPorIdInexistente() {
		Paciente paciente = pacienteService.buscarPorId(888);
		assertNull(paciente);
	}

	@Test
	public void testBuscarPorCpfInexistente() {
		assertNull(pacienteService.buscarPorCpf("1"));
	}

	@Test
	public void testBuscarPorCnsInexistente() {
		assertNull(pacienteService.buscarPorCns("1"));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarPacienteSUS() throws ProntuarioException, ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dataNascimento = formatter.parse("24-08-1944");

		Paciente paciente = new Paciente();
		paciente.setNome("Júlio Leminski Filho");
		paciente.setNomeDaMae("Áurea Pereira Mendes");
		paciente.setNascimento(dataNascimento);
		paciente.setEstadoCivil(EstadoCivil.CASADO);
		paciente.setNacionalidade("Brasileira");
		paciente.setNaturalidade("Curitiba");
		paciente.setCpf("47247752548");
		paciente.setCns("136111333770002");
		paciente.setSexo(Sexo.M);
		paciente.setRaca(Raca.BRANCA);
		paciente.setProfissao("Escritor");
		paciente.setResponsavel("Neiva Maria de Souza");
		paciente.setTelefone("(88) 98888-9999");
		paciente.setSegundoTelefone("(88) 99999-8888");
		paciente.setEndereco("Rua. José Amadeu Soares, 109");
		paciente.setBairro("Combate");
		paciente.setCep("63900-000");
		paciente.setCidade("Quixadá");
		paciente.setEstado(Estado.CE);
		paciente.setPais("Brasil");

		pacienteService.salvar(paciente);

		Paciente pacienteSalvo = pacienteService.buscarPorCpf("47247752548");

		assertNotNull(pacienteSalvo);
		assertEquals("JULIO LEMINSKI FILHO", pacienteSalvo.getNome());
		assertEquals("AUREA PEREIRA MENDES", pacienteSalvo.getNomeDaMae());
		assertEquals(dataNascimento, pacienteSalvo.getNascimento());
		assertEquals(EstadoCivil.CASADO, pacienteSalvo.getEstadoCivil());
		assertEquals("Brasileira", pacienteSalvo.getNacionalidade());
		assertEquals("Curitiba", pacienteSalvo.getNaturalidade());
		assertEquals("47247752548", pacienteSalvo.getCpf());
		assertEquals("136111333770002", pacienteSalvo.getCns());
		assertEquals(Sexo.M, pacienteSalvo.getSexo());
		assertEquals(Raca.BRANCA, pacienteSalvo.getRaca());
		assertEquals("Escritor", pacienteSalvo.getProfissao());
		assertEquals("Neiva Maria de Souza", pacienteSalvo.getResponsavel());
		assertEquals("(88) 98888-9999", pacienteSalvo.getTelefone());
		assertEquals("(88) 99999-8888", pacienteSalvo.getSegundoTelefone());
		assertEquals("Rua. José Amadeu Soares, 109", pacienteSalvo.getEndereco());
		assertEquals("Combate", pacienteSalvo.getBairro());
		assertEquals("63900-000", pacienteSalvo.getCep());
		assertEquals("Quixadá", pacienteSalvo.getCidade());
		assertEquals(Estado.CE, pacienteSalvo.getEstado());
		assertEquals("Brasil", pacienteSalvo.getPais());

	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testSalvarPacienteLivreDemanda() throws ProntuarioException, ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dataNascimento = formatter.parse("19-04-1882");

		Paciente paciente = new Paciente();
		paciente.setNome("José Dornelles Vargas");
		paciente.setNomeDaMae("Cândida Francisca Dornelles Vargas");
		paciente.setNascimento(dataNascimento);
		paciente.setSexo(Sexo.M);
		paciente.setTelefone("(88) 98888-9999");

		pacienteService.salvar(paciente);

		Paciente pacienteSalvo = pacienteService.buscarPorNome("jose").get(0);

		assertNotNull(pacienteSalvo);
		assertEquals("JOSE DORNELLES VARGAS", pacienteSalvo.getNome());
		assertEquals("CANDIDA FRANCISCA DORNELLES VARGAS", pacienteSalvo.getNomeDaMae());
		assertEquals(Sexo.M, pacienteSalvo.getSexo());
		assertEquals("(88) 98888-9999", pacienteSalvo.getTelefone());
	}

	@Test
	public void testSalvarPacienteSemCamposObrigatorios() throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dataNascimento = formatter.parse("19-04-1882");

		Paciente paciente = new Paciente();
		paciente.setNome("José Dornelles Vargas");
		paciente.setNomeDaMae("Cândida Francisca Dornelles Vargas");
		paciente.setNascimento(dataNascimento);
		// paciente.setSexo(Sexo.M);
		paciente.setTelefone("(88) 98888-9999");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção pois o campo sexo do paciente está nulo.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, erro);
		}

		paciente = new Paciente();
		paciente.setNome("José Dornelles Vargas");
		paciente.setNomeDaMae("Cândida Francisca Dornelles Vargas");
		paciente.setNascimento(dataNascimento);
		paciente.setSexo(Sexo.M);
		// paciente.setTelefone("(88) 98888-9999");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção pois o telefone do paciente está nulo.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, erro);
		}

		paciente = new Paciente();
		paciente.setNome("José Dornelles Vargas");
		paciente.setNomeDaMae("Cândida Francisca Dornelles Vargas");
		paciente.setNascimento(dataNascimento);
		paciente.setSexo(Sexo.M);
		paciente.setTelefone("");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção pois o telefone do paciente está vazio.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, erro);
		}

		paciente = new Paciente();
		paciente.setNome("José Dornelles Vargas");
		// paciente.setNomeDaMae("Cândida Francisca Dornelles Vargas");
		paciente.setNascimento(dataNascimento);
		paciente.setSexo(Sexo.M);
		paciente.setTelefone("(88) 98888-9999");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção pois o nome da mãe está nulo.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, erro);
		}

		paciente = new Paciente();
		paciente.setNome("José Dornelles Vargas");
		paciente.setNomeDaMae("");
		paciente.setNascimento(dataNascimento);
		paciente.setSexo(Sexo.M);
		paciente.setTelefone("(88) 98888-9999");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção pois o nome da mãe está vazio.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, erro);
		}

		paciente = new Paciente();
		// paciente.setNome("José Dornelles Vargas");
		paciente.setNomeDaMae("Cândida Francisca Dornelles Vargas");
		paciente.setNascimento(dataNascimento);
		paciente.setSexo(Sexo.M);
		paciente.setTelefone("(88) 98888-9999");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção pois o nome do paciente está nulo.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, erro);
		}

		paciente = new Paciente();
		paciente.setNome("");
		paciente.setNomeDaMae("Cândida Francisca Dornelles Vargas");
		paciente.setNascimento(dataNascimento);
		paciente.setSexo(Sexo.M);
		paciente.setTelefone("(88) 98888-9999");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção pois o nome do paciente está vazio.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_CAMPOS_OBRIGATORIOS, erro);
		}

	}

	@Test
	public void testSalvarPacienteComCpfJaExistente() throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dataNascimento = formatter.parse("19-04-1882");

		Paciente paciente = new Paciente();
		paciente.setCpf("85853317636");
		paciente.setNome("Getúlio Dornelles Vargas");
		paciente.setNomeDaMae("Cândida Francisca Dornelles Vargas");
		paciente.setNascimento(dataNascimento);
		paciente.setSexo(Sexo.M);
		paciente.setTelefone("(88) 98888-9999");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção para Cpf de paciente já cadastrado.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_PACIENTE_CPF_EXISTENTE, erro);
		}
	}

	@Test
	public void testSalvarPacienteComMesmoNomeEoMesmoNomeDaMaeEaDataDeNascimento() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dataNascimento = formatter.parse("02-02-1952");

		Paciente paciente = new Paciente();
		paciente.setNome("Sarah Luisa Gomes De Sousa Lemos");
		paciente.setNomeDaMae("Maria Luisa Negrao");
		paciente.setSexo(Sexo.F);
		paciente.setNascimento(dataNascimento);
		paciente.setTelefone("(88) 98888-9999");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção pois possui mesmo nome, nome da mãe e data de nascimento.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_PACIENTE_EXISTENTE, erro);
		}
	}

	@Test
	public void testSalvarPacienteComCnsJaExistente() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dataNascimento = formatter.parse("02-02-1952");

		Paciente paciente = new Paciente();
		paciente.setCns("827307676390001");
		paciente.setNome("Francisco Dornelles Marinho");
		paciente.setNomeDaMae("Maria Vargas Marinho");
		paciente.setNascimento(dataNascimento);
		paciente.setSexo(Sexo.M);
		paciente.setTelefone("(88) 98888-9999");

		try {
			pacienteService.salvar(paciente);
			fail("Deveria ter lançado exceção para Cns de paciente já cadastrado.");
		} catch (ProntuarioException e) {
			String erro = e.getMessage();
			assertEquals(ERRO_PACIENTE_CNS_EXISTENTE, erro);
		}
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void testAdicionarAnamnese() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date dataAnamnese = formatter.parse("26-08-2018");

		String descricao = "Teste Anamnese";
		Anamnese anamnese = anamneseService.buscarPorId(1);
		Paciente paciente = pacienteService.buscarPorId(1);
		Aluno aluno = alunoService.buscarPorId(1);
		List<Resposta> respostas = new ArrayList<>();
		Pergunta pergunta = anamnese.getPerguntas().get(0);

		PacienteAnamnese pacienteAnamnese = new PacienteAnamnese();
		pacienteAnamnese.setData(dataAnamnese);
		pacienteAnamnese.setDescricao(descricao);
		pacienteAnamnese.setAnamnese(anamnese);
		pacienteAnamnese.setPaciente(paciente);
		pacienteAnamnese.setResponsavel(aluno);

		Resposta resposta = new Resposta();
		resposta.setOpcao(true);
		resposta.setPacienteAnamnese(pacienteAnamnese);
		resposta.setPergunta(pergunta);
		resposta.setTexto("Teste Resposta");
		respostas.add(resposta);

		pacienteAnamnese.setRespostas(respostas);

		pacienteService.adicionarAnamnese(paciente, pacienteAnamnese);

		PacienteAnamnese pacienteAnamneseSalva = pacienteService.buscarPacienteAnamnesePorId(2);

		assertEquals(paciente, pacienteAnamneseSalva.getPaciente());
		assertEquals(anamnese, pacienteAnamneseSalva.getAnamnese());
		assertEquals(dataAnamnese, pacienteAnamneseSalva.getData());
		assertEquals(aluno, pacienteAnamneseSalva.getResponsavel());
		assertEquals(descricao, pacienteAnamneseSalva.getDescricao());
		assertEquals(1, pacienteAnamneseSalva.getRespostas().size());

	}

	@Test
	public void testBuscarAnamnesesDePaciente() throws ParseException {
		Paciente paciente = pacienteService.buscarPorId(2);
		List<PacienteAnamnese> listaPacienteAnamnes = pacienteService.buscarPacienteAnamneses(paciente);

		assertEquals(1, listaPacienteAnamnes.size());

		PacienteAnamnese pacienteAnamnese = listaPacienteAnamnes.get(0);

		assertEquals((Integer) 2, pacienteAnamnese.getPaciente().getId());
		assertEquals((Integer) 2, pacienteAnamnese.getAnamnese().getId());
		assertEquals((Integer) 1, pacienteAnamnese.getResponsavel().getId());

		assertEquals("2018-02-20 00:00:00.0", pacienteAnamnese.getData().toString());
		assertEquals("ksjdvbskdvb", pacienteAnamnese.getDescricao());
		assertEquals(1, pacienteAnamnese.getRespostas().size());

	}
}
