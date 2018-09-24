package ufc.npi.prontuario.service.impl;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_AVALIACAO_CAMPOS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_EXCLUIR_AVALIACAO_COM_ATENDIMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERRO_UNICA_AVALIACAO_ATIVA;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Avaliacao;
import ufc.npi.prontuario.model.ItemAvaliacao;
import ufc.npi.prontuario.model.enums.StatusAvaliacao;
import ufc.npi.prontuario.repository.AtendimentoRepository;
import ufc.npi.prontuario.repository.AvaliacaoRepository;
import ufc.npi.prontuario.repository.ItemAvaliacaoRepository;
import ufc.npi.prontuario.service.AvaliacaoService;

@Service
public class AvaliacaoServiceImpl implements AvaliacaoService {

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired
	private ItemAvaliacaoRepository itemRepository;

	@Autowired
	private AtendimentoRepository atendimentoRepository;


	@Override
	public void salvar(Avaliacao avaliacao) {
		avaliacaoRepository.save(avaliacao);
	}

	@Override
	public Avaliacao addItem(Avaliacao avaliacao, ItemAvaliacao item) throws ProntuarioException {
		if (!item.getNome().isEmpty() && item.getNome() != null && item.getPeso() != null) {
			item.setNome(item.getNome().toUpperCase());
			avaliacao.addItem(item);
			return avaliacaoRepository.saveAndFlush(avaliacao);
		} else {
			throw new ProntuarioException(ERRO_AVALIACAO_CAMPOS);
		}
	}

	@Override
	public Avaliacao getAvaliacaoAtiva() {
		return avaliacaoRepository.findAvaliacaoAtiva();
	}

	@Override
	public List<Avaliacao> getTodasAvaliacoes() {
		return avaliacaoRepository.findAllByOrderById();
	}

	@Override
	public void deleteAvaliacao(Avaliacao avaliacao) throws ProntuarioException {
		if (atendimentoRepository.findAllByAvaliacao_avaliacao(avaliacao).size() > 0) {
			throw new ProntuarioException(ERRO_EXCLUIR_AVALIACAO_COM_ATENDIMENTO);
		}
		avaliacaoRepository.delete(avaliacao);
	}

	@Override
	public void deleteItem(ItemAvaliacao item) {
		itemRepository.delete(item);
	}

	@Override
	public void finalizar(Avaliacao avaliacao) {
		avaliacao.setStatus(StatusAvaliacao.FINALIZADA);
		avaliacaoRepository.save(avaliacao);
	}

	@Override
	public void ativarDesativar(Avaliacao avaliacao) throws ProntuarioException { 
		List<Avaliacao> avaliacoes = avaliacaoRepository.findAll();
		Avaliacao avaliacaoAtiva = getAvaliacaoAtiva();
		
		if (avaliacao.isAtiva()) {
			if(avaliacoes.size() == 1 || avaliacao.equals(avaliacaoAtiva)){
				throw new ProntuarioException(ERRO_UNICA_AVALIACAO_ATIVA);
			}
			avaliacao.setAtiva(false);
			avaliacaoAtiva.setAtiva(true);
		} else {	
			avaliacao.setAtiva(true);
			avaliacaoAtiva.setAtiva(false);
		}
		avaliacaoRepository.save(avaliacao);
		avaliacaoRepository.save(avaliacaoAtiva);
	}
}

