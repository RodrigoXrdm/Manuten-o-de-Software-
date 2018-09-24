package ufc.npi.prontuario.controller;

import static ufc.npi.prontuario.util.ExceptionSuccessConstants.ERROR;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_EXCLUIR_DOCUMENTO;
import static ufc.npi.prontuario.util.ExceptionSuccessConstants.SUCCESS_SALVAR_DOCUMENTO;
import static ufc.npi.prontuario.util.PagesConstants.LISTAGEM_DOCUMENTOS;
import static ufc.npi.prontuario.util.RedirectConstants.REDIRECT_LISTAGEM_DOCUMENTOS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ufc.npi.prontuario.exception.ProntuarioException;
import ufc.npi.prontuario.model.Documento;
import ufc.npi.prontuario.model.Paciente;
import ufc.npi.prontuario.model.Registro.Acao;
import ufc.npi.prontuario.model.Usuario;
import ufc.npi.prontuario.service.DocumentoService;
import ufc.npi.prontuario.service.RegistroService;

@Controller
@RequestMapping("/documento")
public class DocumentoController {

	@Autowired
	private DocumentoService documentoService;

	@Autowired
	private RegistroService registroService;

	@PostMapping("/upload/{id}")
	public ModelAndView upload(@PathVariable("id") Paciente paciente, @RequestParam("files") MultipartFile[] files,
			RedirectAttributes attributes, Authentication auth) {

		Usuario usuario = (Usuario) auth.getPrincipal();
		
		Documento ultimoDocumentoSalvo = paciente.getDocumentos().get(paciente.getDocumentos().size() - 1);

		if(files != null && files.length > 0) {
			try {
				documentoService.salvar(paciente, files);
				attributes.addFlashAttribute(SUCCESS, SUCCESS_SALVAR_DOCUMENTO);
				registroService.salvar(usuario.getId(), paciente.getId(), ultimoDocumentoSalvo.getId(), Acao.CADASTRAR_EXAME, null);

			} catch (ProntuarioException e) {
				attributes.addFlashAttribute(ERROR, e.getMessage());
			}
		}
		return new ModelAndView(REDIRECT_LISTAGEM_DOCUMENTOS + paciente.getId());
	}

	@GetMapping("/paciente/{id}")
	public ModelAndView listarDocumentos(@PathVariable("id") Paciente paciente, Authentication auth) {
		ModelAndView mv = new ModelAndView(LISTAGEM_DOCUMENTOS);
		Usuario usuario = (Usuario) auth.getPrincipal();
		
		Documento ultimoDocumentoSalvo = paciente.getDocumentos().get(paciente.getDocumentos().size() - 1);
		
		mv.addObject("paciente", paciente);
		registroService.salvar(usuario.getId(), paciente.getId(), ultimoDocumentoSalvo.getId(), Acao.VISUALIZAR_EXAME, ultimoDocumentoSalvo.toStringRegistro());
		return mv;
	}

	@GetMapping("/download/{id}")
	public HttpEntity<?> download(@PathVariable("id") Documento documento, Authentication auth) {
		Usuario usuario = (Usuario) auth.getPrincipal();
		Paciente paciente = documento.getPaciente();

		try {
			documento = documentoService.buscarArquivo(documento);
		} catch (ProntuarioException e) {
			e.printStackTrace();
		}
		
		registroService.salvar(usuario.getId(), paciente.getId(), documento.getId(), Acao.DOWNLOAD_EXAME, documento.toStringRegistro());
		return documentoService.downloadDocumento(documento, "attachment");
	}

	@GetMapping("/visualizar/{id}")
	public HttpEntity<?> visualizar(@PathVariable("id") Documento documento, Authentication auth) {

		Usuario usuario = (Usuario) auth.getPrincipal();
		Paciente paciente = documento.getPaciente();
		
		try {
			documento = documentoService.buscarArquivo(documento);
		} catch (ProntuarioException e) {
			e.printStackTrace();
		}
		registroService.salvar(usuario.getId(), paciente.getId(), documento.getId(), Acao.VISUALIZAR_EXAME, documento.toStringRegistro());
		return documentoService.downloadDocumento(documento, "inline");
	}

	@GetMapping("/deletar/{idPaciente}/{idDocumento}")
	public ModelAndView deletar(@PathVariable("idPaciente") Paciente paciente, @PathVariable("idDocumento") Documento documento,
			RedirectAttributes attributes,  Authentication auth) {
		Usuario usuario = (Usuario) auth.getPrincipal();

		registroService.salvar(usuario.getId(), paciente.getId(), documento.getId(), Acao.REMOVER_EXAME, documento.toStringRegistro());

		documentoService.deletar(documento, paciente);
		attributes.addFlashAttribute(SUCCESS, SUCCESS_EXCLUIR_DOCUMENTO);
		

		return new ModelAndView(REDIRECT_LISTAGEM_DOCUMENTOS + paciente.getId());
	}
}
