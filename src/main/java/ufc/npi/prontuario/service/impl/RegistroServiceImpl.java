package ufc.npi.prontuario.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.model.Registro;
import ufc.npi.prontuario.model.Registro.Acao;
import ufc.npi.prontuario.repository.RegistroRepository;
import ufc.npi.prontuario.service.RegistroService;

@Service
public class RegistroServiceImpl implements RegistroService {

	@Autowired
	private RegistroRepository registroRepository;
	
	@Override
	public Registro salvar(Integer idUsuario, Integer idPaciente, Integer valor, Acao acao, String observacao){
		
		Registro registro = new Registro();	
		registro.setData(new Date(System.currentTimeMillis()));
		registro.setIdUsuario(idUsuario);
		registro.setIdPaciente(idPaciente);
		registro.setTipoAcao(acao);
		registro.setValor(valor);
		registro.setObservacao(observacao);
	
		return registroRepository.save(registro);
	}
	
	@Override
	public List<Registro> buscarResumoRegistros() {
		return registroRepository.findAllSample();
	}
	
	public Registro findById(Integer id){
		return registroRepository.findById(id);
	};

	public String formatarObservacaoHtml(String observacao) {
		
		if(observacao == null){
			return "";
		}
		
		String[] splitObservacao = observacao.split("\n");
		
		if(splitObservacao[0].equals("Paciente: ")) {
			return "";
		}
		
		String template = "<span class=\"bold-text grey-text text-darken-3\">" + splitObservacao[0] + "</span></br>";
		template += "<div class=\"card-content\">";
		template += "<div class=\"row\">";
		
		for(int i = 1; i < splitObservacao.length; i++) {
			String[] key_value = splitObservacao[i].split(":"); 
			template += "<div class=\"col l6 m4 s12 no-padding\" style=\"margin-bottom:15px;\">";
			template += "<span class=\"bold-text grey-text text-darken-3\">"+key_value[0]+":</span>" ;
			template += "<span class=\"light-text text-18 truncate\">"+key_value[1]+"</span>";
			template += "</div>";
		}
		
		template += "</div></div>";
		
		return template;
	}
	
	
	
}
