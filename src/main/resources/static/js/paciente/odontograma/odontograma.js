$(document).ready(function () {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");

	var _context = $("meta[name='_context']").attr("content");
	if (_context == null) {
		_context = "";
	}

	var odontogramaId = $("#odontograma-id").val();
	var atendimentoId = $("#atendimento-id").val();
	var pacienteId = $("#paciente-id").val();
	var existeAtendimento = $("#existe-atendimento").val();

	$("#odonto-procedimentos").hide();
	$("#odonto-procedimentos-existentes").hide();
	$("#procedimento-geral").hide();
	$("#procedimento-existente-geral").hide();
	$("#label-resultado-busca-patologia").hide();
	$("#label-patologias-selecionadas").hide();
	$("#label-resultado-busca-novo-procedimento").hide();
	$("#label-novos-procedimentos-selecionados").hide();
	$("#label-resultado-busca-procedimento-existente").hide();
	$("#label-procedimentos-selecionados-existentes").hide();
	$("#patologias-dente-busca").hide();


	carregarTablePatologias();
	carregarTableProcedimentos();
	marcarPatologias();
	marcarProcedimentos();
	marcarProcedimentosExistentes();

	$("#tab-patologias").click(function () {
		$(".arcadas-container").hide();
		$("#odonto-patologias").fadeIn(500);

		$(".geral").hide();
		$("#patologia-geral").show();
	});

	$("#tab-procedimentos").click(function () {
		$(".arcadas-container").hide();
		$("#odonto-procedimentos").fadeIn(500);

		$(".geral").hide();
		$("#procedimento-geral").show();
	});

	$("#tab-procedimentos-existentes").click(function () {
		$(".arcadas-container").hide();
		$("#odonto-procedimentos-existentes").fadeIn(500);

		$(".geral").hide();
		$("#procedimento-existente-geral").show();
	});

	$("#select-arcada").change(function () {
		var arcada = $("#select-arcada").val();

		if (arcada == "permanente") {
			$(".arcada-permanente").show();
			$(".arcada-decidua").hide();

		} else if (arcada == "decidua") {
			$(".arcada-decidua").show();
			$(".arcada-permanente").hide();
		} else {
			$(".arcada-permanente").show();
			$(".arcada-decidua").show();
		}
	});

	$(".face, .txt-svg").on("mouseenter", function () {
		// ...
		$(this).addClass("focus-face");
	});

	$(".face, .txt-svg").on("mouseleave", function () {
		$(this).removeClass("focus-face");
	});

	$(".face").on("click", function () {
		if(odontogramaId == null || odontogramaId == '' || odontogramaId == undefined || odontogramaId == 'null') {
			return;
		}
		
		if($(this).hasClass("raiz")) {
			loadRaizes($(this).attr("dente"));
			$("#raiz-select").parent().parent().show();
		} else {
			$("#raiz-select").parent().parent().hide();
		}

		var tipo = $(this).attr("tipo");
		//Decidindo qual modal deve ser aberto de acordo com o tipo do odontograma
		switch (tipo) {
			case "PA":
				if ($("#modal-patologias").length) {
					$("#modal-patologias").openModal();
					$("#id-face-dente-patologia").val($(this).attr("id"));
					$("#id-dente-patologia").val("");
				}
				break;
			case "PR":
				if ($("#modal-procedimentos").length) {
					$("#modal-procedimentos").openModal();
					$("#id-face-dente-procedimento").val($(this).attr("id"));
					$("#id-dente-procedimento").val("");
					var face = $("#id-face-dente-procedimento").val();
					var dente = face.split("_")[0];
					$('#patologias-dente-selecionado-1').empty();
					$('#patologias-dente-selecionado-2').empty();
					$('#patologias-dente-selecionado').empty();
					$('#data-patologia-tratamento').val("");
					$.ajax({
						url: _context + "/odontograma/buscar-todas-patologias/" + odontogramaId,
						beforeSend: function (request) {
							request.setRequestHeader(header, token);
						},
						async: false,
						type: 'GET',
						data: {
							face: face,
							dente: dente
						},
						success: function (data) {
							if (data.length > 0) {
								$("#patologias-dente-busca").show();
								var index = 2;
								$.each(data, function (key, value) {
									adicionarResultado(value.id, "patologias-dente", '#patologias-dente-selecionado-1', '#patologias-dente-selecionado-2', index, value.tipo.nome, "patologias-dente");
									index++;
								});
							} else {
								$("#patologias-dente-busca").hide();
							}
						}
					});
				}
				break;
			case "PE":
				if ($("#modal-procedimentos-existentes").length) {
					$("#modal-procedimentos-existentes").openModal();
					$("#id-face-dente-proced-exist").val($(this).attr("id"));
					$("#id-dente-proced-exist").val("");
				}
				break;
			default:
				break;
		}
	});


	$(".txt-dente").on("click", function () {
		var tipo = $(this).attr("tipo");
		if(odontogramaId == null || odontogramaId == '' || odontogramaId == undefined || odontogramaId == 'null') {
			return;
		}
		$("#raiz-select").parent().parent().hide();
		//Decidindo qual modal deve ser aberto de acordo com o tipo do odontograma
		switch (tipo) {
			case "PA":
				if ($("#modal-patologias").length) {
					$("#modal-patologias").openModal();
					$("#id-face-dente-patologia").val("");
					$("#id-dente-patologia").val($(this).attr("id"));
				}
				break;
			case "PR":
				if ($("#modal-procedimentos").length) {
					$("#modal-procedimentos").openModal();
					$("#id-face-dente-procedimento").val("");
					$("#id-dente-patologia").val($(this).attr("id"));
					var dente = $(this).attr("dente");
					$('#patologias-dente-selecionado-1').empty();
					$('#patologias-dente-selecionado-2').empty();
					$('#patologias-dente-selecionado').empty();
					$('#data-patologia-tratamento').val("");

					$.ajax({
						url: _context + "/odontograma/buscar-patologia/" + odontogramaId,
						beforeSend: function (request) {
							request.setRequestHeader(header, token);
						},
						async: false,
						type: 'GET',
						data: {
							dente: dente
						},
						success: function (data) {
							if (data.length > 0) {
								$("#patologias-dente-busca").show();
								var index = 2;
								$.each(data, function (key, value) {
									adicionarResultado(value.id, "patologias-dente", '#patologias-dente-selecionado-1', '#patologias-dente-selecionado-2', index, value.tipo.nome, "patologias-dente");
									index++;
								});
							} else {
								$("#patologias-dente-busca").hide();
							}
						}
					});
				}
				break;
			case "PE":
				if ($("#modal-procedimentos-existentes").length) {
					$("#modal-procedimentos-existentes").openModal();
					$("#id-face-dente-proced-exist").val("");
					$("#id-dente-proced-exist").val(this.textContent);
				}
				break;
			default:
				break;
		}
	});

	$(".geral").on("click", function () {
		var tipo = $(this).attr("tipo");
		$("#id-boca").val($(this).attr("data-id"));
		$("#raiz-select").parent().parent().hide();

		//Decidindo qual modal deve ser aberto de acordo com o tipo do odontograma
		switch (tipo) {
			case "PA":
				if ($("#modal-patologias").length) {
					$("#modal-patologias").openModal();
					$("#id-face-dente-patologia").val("");
					$("#id-dente-patologia").val("");
				}
				break;
			case "PR":
				if ($("#modal-procedimentos").length) {
					$("#modal-procedimentos").openModal();
					$("#id-face-dente-procedimento").val("");
					$("#id-dente-procedimento").val("");
				}
				$('#patologias-dente-selecionado-1').empty();
				$('#patologias-dente-selecionado-2').empty();
				$('#patologias-dente-selecionado').empty();
				$('#data-patologia-tratamento').val("");

				$.ajax({
					url: _context + "/odontograma/buscar-patologia/" + odontogramaId,
					beforeSend: function (request) {
						request.setRequestHeader(header, token);
					},
					async: false,
					type: 'GET',
					success: function (data) {
						if (data.length > 0) {
							$("#patologias-dente-busca").show();
							var index = 2;
							$.each(data, function (key, value) {
								adicionarResultado(value.id, "patologias-dente", '#patologias-dente-selecionado-1', '#patologias-dente-selecionado-2', index, value.tipo.nome, "patologias-dente");
								index++;
							});
						} else {
							$("#patologias-dente-busca").hide();
						}
					}
				});
				break;

			case "PE":

				if ($("#modal-procedimentos-existentes").length) {
					$("#modal-procedimentos-existentes").openModal();
					$("#id-face-dente-proced-exist").val("");
					$("#id-dente-proced-exist").val("");
				}

				break;

			default:
				break;
		}
	});

	$("#confirm-patologia").click(function () {
		var idLocal;
		var local;

		if($("#raiz-select").parent().parent().is(":visible")){
			local = "RAIZ";
			idLocal = $("#raiz-select").val();
		} else if ($("#id-face-dente-patologia").val() != "") {
			local = "FACE";
			idLocal = $("#id-face-dente-patologia").val();
		} else if ($("#id-dente-patologia").val() != "") {
			local = "DENTE";
			idLocal = $("#id-dente-patologia").val();
		} else {
			local = "GERAL";
			idLocal = $("#id-boca").val();
		}

		if ($("input[name=patologia]").length > 0 && $("input[name=patologia]").is(":checked")) {
			adicionarPatologia(idLocal, local);
		}
	});

	$("#confirm-procedimento").click(function () {
		var idLocal;
		var local;

		if ($("#id-face-dente-procedimento").val() != "") {
			local = "FACE";
			idLocal = $("#id-face-dente-procedimento").val();
		} else if ($("#id-dente-procedimento").val() != "") {
			local = "DENTE";
			idLocal = $("#id-dente-procedimento").val();
		} else if ($("#id-boca").val() != "") {
			local = "GERAL";
			idLocal = $("#id-boca").val();
		}

		if ($("input[name=procedimento]").length > 0 && $("input[name=procedimento]").is(":checked")) {
			adicionarProcedimento(idLocal, local);
		}
	});

	$("#confirm-proced-exist").click(function () {
		var idLocal;
		var local;

		if ($("#id-face-dente-proced-exist").val() != "") {
			local = "FACE";
			idLocal = $("#id-face-dente-proced-exist").val();
		} else if ($("#id-dente-proced-exist").val() != "") {
			local = "DENTE";
			idLocal = $("#id-dente-proced-exist").val();
		} else {
			local = "GERAL";
			idLocal = $("#id-boca").val();
		}

		if ($("input[name=proced-exist]").length > 0 && $("input[name=proced-exist]").is(":checked")) {
			adicionarProcedimentoExistente(idLocal, local);
		}
	});

	$("#confirm-patologia").click(function () {
		$("input[name=patologia]").attr("checked", false);
		$("#modal-patologias").closeModal();
		$("#descricao-patologia").val("");
	});

	$("#confirm-procedimento").click(function () {
		$("input[name=procedimento]").attr("checked", false);
		$("#modal-procedimentos").closeModal();
		$("#descricao-procedimento").val("");
	});

	$("#confirm-proced-exist").click(function () {
		$("input[name=proced-exist]").attr("checked", false);
		$("#modal-procedimentos-existentes").closeModal();
		$("#descricao-proced-exist").val("");
	});

	$("#cancelar-patologia").click(function () {
		$("#modal-patologias").closeModal();
	});

	$("#cancelar-procedimento").click(function () {
		$("#modal-procedimentos").closeModal();
	});

	$("#cancelar-proced-exist").click(function () {
		$("#modal-procedimentos-existentes").closeModal();
	});

	$("#cancelar-editar-patologia").click(function () {
		$("#modal-editar-patologias").closeModal();
	});

	$("#cancelar-editar-procedimento").click(function () {
		$("#modal-editar-procedimentos").closeModal();
	});

	$("#cancelar-editar-pre-procedimento").click(function () {
		$("#modal-editar-pre-procedimentos").closeModal();
	});

	$("#confirm-tratamento-patologia").click(function () {
		var idPatologia = $("#id-patologia").val();

		$.post(_context + "/odontograma/tratar/" + idPatologia, $("#form-tratamento-patologia").serialize(), function () {
			$("#modal-tratamento-patologia").closeModal();
		})
			.done(function (patologias) {
				marcarPatologias();
				carregarTablePatologias();

				$("#data-tratamento").val(null);
				$("#descricao-tratamento").val(null);
			});
	});

	$("#cancelar-tratamento-patologia").click(function () {
		$("#modal-tratamento-patologia").closeModal();

		$("#data-tratamento").val(null);
		$("#descricao-tratamento").val(null);
	});



	function adicionarPatologia(idLocal, local) {
		var patologias = $("input[name=patologia]:checked").map(function () {
			return this.value;
		}).get().join(",");

		var descricao = $("#descricao-patologia").val();

		$.ajax({
			url: _context + "/odontograma/adicionarPatologia",
			beforeSend: function (request) {
				request.setRequestHeader(header, token);
			},
			async: false,
			type: 'POST',
			data: {
				patologias: patologias,
				idEstrutura: idLocal,
				idOdontograma: odontogramaId,
				descricao: descricao,
			},
			success: function () {
				marcarPatologias();
				carregarTablePatologias();
				$("#resultado-busca-patologia-1").empty();
				$("#resultado-busca-patologia-2").empty();
				$("#selecionados-busca-patologia").empty();
				$("#label-resultado-busca-patologia").hide();
				$("#label-patologias-selecionadas").hide();
				document.getElementById("busca-patologia-input").value = "";
			}
		});
	}

	function adicionarProcedimento(idLocal, local) {

		var procedimentos = $("input[name=procedimento]:checked").map(function () {
			return this.value;
		}).get().join(",");

		var patologias = $("input[name=patologias-dente]:checked").map(function () {
			return this.value;
		}).get().join(",");

		var data = $("#data-patologia-tratamento").val();

		var descricao = $("#descricao-procedimento").val();
		$.ajax({
			url: _context + "/odontograma/adicionarProcedimento",
			beforeSend: function (request) {
				request.setRequestHeader(header, token);
			},
			async: true,
			type: 'POST',
			data: {
				idEstrutura: idLocal,
				atendimento: atendimentoId,
				patologias: patologias,
				procedimentos: procedimentos,
				idOdontograma: odontogramaId,
				descricao: descricao,
				preExistente: false
			},
			success: function (result) {
				marcarProcedimentos();
				carregarTableProcedimentos();
				carregarTablePatologias();
				marcarPatologias();
				$("#resultado-busca-procedimento-1").empty();
				$("#resultado-busca-procedimento-2").empty();
				$("#label-resultado-busca-novo-procedimento").hide();
				$("#label-novos-procedimentos-selecionados").hide();
				$("#selecionados-busca-procedimento").empty();
				document.getElementById("busca-novo-procedimento-input").value = "";
			}
		});
	}

	function adicionarProcedimentoExistente(idLocal) {

		var procedimentos = $("input[name=proced-exist]:checked").map(function () {
			return this.value;
		}).get().join(",");

		var descricao = $("#descricao-proced-exist").val();

		$.ajax({
			url: _context + "/odontograma/adicionarProcedimento",
			beforeSend: function (request) {
				request.setRequestHeader(header, token);
			},
			async: true,
			type: 'POST',
			data: {
				procedimentos: procedimentos,
				idEstrutura: idLocal,
				idOdontograma: odontogramaId,
				descricao: descricao,
				atendimento: atendimentoId,
				preExistente: true
			},
			success: function () {
				marcarProcedimentosExistentes()
				carregarTableProcedimentos();
				$("#resultado-busca-procedimento-existente-1").empty();
				$("#resultado-busca-procedimento-existente-2").empty();
				$("#label-resultado-busca-procedimento-existente").hide();
				$("#label-procedimentos-selecionados-existentes").hide();
				$("#selecionados-busca-procedimento-existente").empty();
				document.getElementById("busca-procedimento-existente-input").value = "";
			}
		});
	}

	function marcarPatologias() {
		$(".patologia").attr("data-tooltip", "");
		$(".face").removeClass("patologia");
		$(".face").removeClass("patologia-ativa");

		var urlAux = _context + "/odontograma/patologia/paciente/" + pacienteId;
		
		$.ajax({
			url: urlAux,
			type: 'GET',
			success: function (data) {
				classePatologia(data);
			}
		});
	}

	function marcarProcedimentos() {
		var urlAux = _context + "/odontograma/procedimentos/paciente/" + pacienteId;
		$.ajax({
			url: urlAux,
			type: 'GET',
			success: function (data) {
				console.log(data);
				classeProcedimento(data);
			}
		});
	}

	function marcarProcedimentosExistentes() {
		var urlAux = _context + "/odontograma/procedimentos/paciente/" + pacienteId;
		$.ajax({
			url: urlAux,
			type: 'GET',
			data: {
				preExistente: true
			},
			success: function (data) {
				classeProcedimentoExistente(data);
			}
		});
	}

	var faces = ["R", "L", "D", "O", "M", "V"];

	function classePatologia(patologias) {
		$.each(patologias, function(key, patologia) {
			var estrutura = patologia.estrutura;
			var tipoEstrutura = estrutura.tipoEstrutura;
			var elem = null;
			// Caso seja na RAIZ
			if(tipoEstrutura === "RAIZ") {
				elem = $("#odonto-patologias .raiz." + estrutura.id);
			} else if(tipoEstrutura === "DENTE") { // caso seja no DENTE
				var raiz = estrutura.raizes[0];
				elem = $("#" + estrutura.id);

				$.each(estrutura.faces, function(key, face) {
					var elemAux = $("#odonto-patologias #" + face.id);
					if(!elemAux.hasClass("patologia-ativa")) {
						elemAux.addClass("patologia");
						if (patologia.tratamento == null) {
							elemAux.addClass("patologia-ativa");
						}
					}
					addTextTooltip(elemAux, patologia.tipo.nome);
				});

				var elemRaiz = elem = $("#odonto-patologias .raiz." + raiz.id);
				elemRaiz.addClass("patologia");
				if (patologia.tratamento == null) {
					elemRaiz.addClass("patologia-ativa");
				}
			} else if(tipoEstrutura === "FACE") { // Caso seja FACE
				elem = $("#odonto-patologias #" + estrutura.id);
			}

			elem.addClass("patologia");
			if (patologia.tratamento == null) {
				elem.addClass("patologia-ativa");
			}
			
			addTextTooltip(elem, patologia.tipo.nome);
		});
	}

	function classeProcedimento(procedimentos) {
		$.each(procedimentos, function(key, procedimento) {
			var estrutura = procedimento.estrutura;
			var elem = null;
			// Caso seja na RAIZ
			if(estrutura.furcas !== null && estrutura.furcas !== undefined) { // caso seja raiz
				elem = $("#odonto-procedimentos .raiz." + estrutura.id);
			} else if(estrutura.faces !== null && estrutura.faces !== undefined) { // caso seja no DENTE
				var raiz = estrutura.raizes[0];
				elem = $("#odonto-procedimentos #" + estrutura.id);

				$.each(estrutura.faces, function(key, face) {
					var elemAux = $("#" + face.id);
					elemAux.addClass("procedimento");
					addTextTooltip(elemAux, procedimento.tipoProcedimento.nome);
				});

				var elemRaiz = elem = $("#odonto-procedimentos .raiz." + raiz.id);
				elemRaiz.addClass("procedimento");
			} else { // Caso seja FACE
				elem = $("#odonto-procedimentos #" + estrutura.id);
			}

			elem.addClass("procedimento");
			addTextTooltip(elem, procedimento.tipoProcedimento.nome);
		});
	}

	function classeProcedimentoExistente(procedimentos) {
		$.each(procedimentos, function(key, procedimento) {
			var estrutura = procedimento.estrutura;
			var elem = null;
			// Caso seja na RAIZ
			if(estrutura.furcas !== null && estrutura.furcas !== undefined) { // caso seja raiz
				elem = $("#odonto-procedimentos-existentes .raiz." + estrutura.id);
			} else if(estrutura.faces !== null && estrutura.faces !== undefined) { // caso seja no DENTE
				var raiz = estrutura.raizes[0];
				elem = $("#odonto-procedimentos-existentes #" + estrutura.id);

				$.each(estrutura.faces, function(key, face) {
					var elemAux = $("#" + face.id);
					elemAux.addClass("proced-exist");
					addTextTooltip(elemAux, procedimento.tipoProcedimento.nome);
				});

				var elemRaiz = elem = $("#odonto-procedimentos-existentes .raiz." + raiz.id);
				elemRaiz.addClass("proced-exist");
			} else { // Caso seja FACE
				elem = $("#odonto-procedimentos-existentes #" + estrutura.id);
			}

			elem.addClass("proced-exist");
			addTextTooltip(elem, procedimento.tipoProcedimento.nome);
		});
	}


	function carregarTablePatologias() {
		var url = _context + "/odontograma/tablePatologias/" + pacienteId;
		
		$("#result-patologias").load(url);
	}

	function carregarTableProcedimentos() {
		var url = _context + "/odontograma/tableProcedimentos/" + pacienteId;

		$("#result-procedimentos").load(url);
	}

	function limparPatologias(patologias) {
		var id;

		for (i = 0; i < patologias.length; i++) {
			if (patologias[i].local == "FACE") {
				id = "PA_" + patologias[i].dente.substring(1) + "_" + patologias[i].face;
				$("#" + id).removeClass("patologia");
				$("#" + id).removeClass("patologia-ativa");

			} else if (patologias[i].local == "DENTE") {
				id = patologias[i].dente.substring(1);

				for (j = 0; j < faces.length; j++) {
					var idAux = "PA_" + id + "_" + faces[j];
					$("#" + idAux).removeClass("patologia");
					$("#" + idAux).removeClass("patologia-ativa");
				}
			}
		}
	}

	function limparProcedimentos(procedimentos) {
		var id;

		for (i = 0; i < procedimentos.length; i++) {
			if (procedimentos[i].local == "FACE") {
				id = "PE_" + procedimentos[i].dente.substring(1) + "_" + procedimentos[i].face;
				$("#" + id).removeClass("proced-exist");

			} else if (procedimentos[i].local == "DENTE") {
				id = procedimentos[i].dente.substring(1);

				for (j = 0; j < faces.length; j++) {
					var idAux = "PE_" + id + "_" + faces[j];
					$("#" + idAux).removeClass("proced-exist");
				}
			}
		}
	}

	function addTextTooltip(elem, descricao) {
		if (elem.attr("data-tooltip")) {
			elem.attr("data-tooltip", elem.attr("data-tooltip") + ", " + descricao);
			elem.tooltip();
		} else {
			var tooltip = descricao;
			elem.attr("data-tooltip", tooltip);
			elem.tooltip();
		}
	}

	$("#busca-novo-procedimento-input").keyup(function () {
		$('#resultado-busca-procedimento-1').empty();
		$('#resultado-busca-procedimento-2').empty();
		var valor = $('#busca-novo-procedimento-input').val();
		if (this.value.length > 2) {
			$("#label-resultado-busca-novo-procedimento").show();
			$.ajax({
				url: _context + "/odontograma/buscar-procedimento",
				beforeSend: function (request) {
					request.setRequestHeader(header, token);
				},
				async: false,
				type: 'GET',
				data: {
					query: valor
				},
				success: function (data) {
					var index = 2;
					$.each(data, function (key, value) {
						if (!existeSelecionado(value.id, "selecionados-busca-procedimento")) {
							adicionarResultado(value.id, "procedimento", '#resultado-busca-procedimento-1', '#resultado-busca-procedimento-2', index, value.nome, "resultado-busca-procedimento");
							index++;
						}
					});
				}
			});
		} else {
			$("#label-resultado-busca-novo-procedimento").hide();
		}
	});

	$("#busca-procedimento-existente-input").keyup(function () {
		$('#resultado-busca-procedimento-existente-1').empty();
		$('#resultado-busca-procedimento-existente-2').empty();
		var valor = $('#busca-procedimento-existente-input').val();
		if (this.value.length > 2) {
			$("#label-resultado-busca-procedimento-existente").show();
			$.ajax({
				url: _context + "/odontograma/buscar-procedimento",
				beforeSend: function (request) {
					request.setRequestHeader(header, token);
				},
				async: false,
				type: 'GET',
				data: {
					query: valor
				},
				success: function (data) {
					var index = 2;
					$.each(data, function (key, value) {
						if (!existeSelecionado(value.id, "selecionados-busca-procedimento-existente")) {

							adicionarResultado(value.id, "proced-exist", '#resultado-busca-procedimento-existente-1', '#resultado-busca-procedimento-existente-2', index, value.nome, "resultado-busca-procedimento-existente");
							index++;
						}
					});
				}
			});
		} else {
			$("#label-resultado-busca-procedimento-existente").hide();
		}
	});

	$("#busca-patologia-input").keyup(function () {
		$('#resultado-busca-patologia-1').empty();
		$('#resultado-busca-patologia-2').empty();
		var valor = $('#busca-patologia-input').val();
		if (this.value.length > 2) {
			$("#label-resultado-busca-patologia").show();
			$.ajax({
				url: _context + "/odontograma/buscar-patologia",
				beforeSend: function (request) {
					request.setRequestHeader(header, token);
				},
				async: false,
				type: 'GET',
				data: {
					query: valor
				},
				success: function (data) {
					var index = 2;
					$.each(data, function (key, value) {
						if (!existeSelecionado(value.id, "selecionados-busca-patologia")) {

							adicionarResultado(value.id, "patologia", '#resultado-busca-patologia-1', '#resultado-busca-patologia-2', index, value.nome, "resultado-busca-patologia");
							index++;


						}
					});
				},
				error: function(err) {
					console.log(err);
				}
			});
		} else {
			$("#label-resultado-busca-patologia").hide();
		}
	});

	$('body').on('click', '.resultado-busca-patologia', function () {
		if (this.checked) {
			$("#label-patologias-selecionadas").show();
			var $lb = $("label[for='" + this.id + "']");
			if ($lb != null) {
				this.remove();
				$lb.remove();
				adicionarSelecionado(this.id, "patologia", "selecionados-busca-patologia", $(this).attr('nome'), '#selecionados-busca-patologia', $(this).attr('col'));

			}
		}
	});

	$('body').on('click', '.selecionados-busca-patologia', function () {
		if (!this.checked) {
			var $lb = $("label[for='" + this.id + "']");
			this.remove();
			$lb.remove();
			removerSelecionado(this.id, "patologia", "resultado-busca-patologia", $(this).attr('nome'), $(this).attr('col'), '#resultado-busca-patologia-1', '#resultado-busca-patologia-2');

		}
	});

	$('body').on('click', '.resultado-busca-procedimento', function () {
		if (this.checked) {
			$("#label-novos-procedimentos-selecionados").show();
			var $lb = $("label[for='" + this.id + "']");
			if ($lb != null) {
				this.remove();
				$lb.remove();
				adicionarSelecionado(this.id, "procedimento", "selecionados-busca-procedimento", $(this).attr('nome'), '#selecionados-busca-procedimento', $(this).attr('col'));


			}
		}
	});

	$('body').on('click', '.selecionados-busca-procedimento', function () {
		if (!this.checked) {
			var $lb = $("label[for='" + this.id + "']");
			this.remove();
			$lb.remove();

			removerSelecionado(this.id, "procedimento", "resultado-busca-procedimento", $(this).attr('nome'), $(this).attr('col'), '#resultado-busca-procedimento-1', '#resultado-busca-procedimento-2');

		}
	});



	$('body').on('click', '.resultado-busca-procedimento-existente', function () {
		if (this.checked) {
			$("#label-procedimentos-selecionados-existentes").show();
			var $lb = $("label[for='" + this.id + "']");
			if ($lb != null) {
				this.remove();
				$lb.remove();
				adicionarSelecionado(this.id, "proced-exist", "selecionados-busca-procedimento-existente", $(this).attr('nome'), '#selecionados-busca-procedimento-existente', $(this).attr('col'));
			}
		}
	});

	$('body').on('click', '.selecionados-busca-procedimento-existente', function () {
		if (!this.checked) {
			var $lb = $("label[for='" + this.id + "']");
			this.remove();
			$lb.remove();
			removerSelecionado(this.id, "proced-exist", "resultado-busca-procedimento-existente", $(this).attr('nome'), $(this).attr('col'), '#resultado-busca-procedimento-existente-1', '#resultado-busca-procedimento-existente-2');
		}
	});

	$('body').on('click', '.patologias-dente', function () {
		if (this.checked) {
			var $lb = $("label[for='" + this.id + "']");
			this.remove();
			$lb.remove();
			adicionarSelecionado(this.id, "patologias-dente", "patologias-dente-selecionadas", $(this).attr('nome'), '#patologias-dente-selecionado', $(this).attr('col'));
		}
	});

	$('body').on('click', '.patologias-dente-selecionadas', function () {
		if (!this.checked) {
			var $lb = $("label[for='" + this.id + "']");
			this.remove();
			$lb.remove();
			removerSelecionado(this.id, "patologias-dente", "patologias-dente", $(this).attr('nome'), $(this).attr('col'), '#patologias-dente-selecionado-1', '#patologias-dente-selecionado-2');
		}
	});


	function adicionarResultado(id, name, coluna1, coluna2, index, nome, classe) {
		var checkbox = document.createElement('input');
		checkbox.className = classe;
		checkbox.type = "checkbox";
		checkbox.name = name;
		checkbox.value = id;
		checkbox.id = id;
		checkbox.setAttribute("nome", nome);
		var label = document.createElement('label')
		label.htmlFor = id;
		label.style.zIndex = "9";
		label.style.position = "relative";
		label.style.display = "inline-block";
		var span = document.createElement('span');
		span.innerHTML = nome;
		label.append(checkbox);
		label.append(span);
		if (index % 2 == 0) {
			checkbox.setAttribute("col", 1);
			$(coluna1).append(label);
			$(coluna1).append(' ');
		} else {
			checkbox.setAttribute("col", 2);
			$(coluna2).append(label);
			$(coluna2).append(' ');
		}
	}

	function adicionarSelecionado(id, name, classe, nome, div, col) {
		var checkbox = document.createElement('input');
		checkbox.type = "checkbox";
		checkbox.name = name;
		checkbox.className = classe;
		checkbox.value = id;
		checkbox.id = id;
		checkbox.checked = true;
		checkbox.setAttribute("nome", nome);
		checkbox.setAttribute("col", col);
		var label = document.createElement('label');
		label.htmlFor = id;
		label.style.zIndex = "9";
		label.style.position = "relative";
		label.style.display = "inline-block";
		var span = document.createElement('span');
		span.html = nome;
		label.append(checkbox);
		label.append(span);
		label.appendChild(document.createTextNode(nome));
		$(div).append(label);
		$(div).append(' ');
	}

	function removerSelecionado(id, name, classe, nome, col, div1, div2) {
		var checkbox = document.createElement('input');
		checkbox.className = classe;
		checkbox.type = "checkbox";
		checkbox.name = name;
		checkbox.value = id;
		checkbox.id = id;
		checkbox.setAttribute("nome", nome);
		checkbox.setAttribute("col", col);
		var label = document.createElement('label')
		label.htmlFor = id;
		label.style.zIndex = "9";
		label.style.position = "relative";
		label.style.display = "inline-block";
		var span = document.createElement('span');
		span.html = nome;
		label.append(checkbox);
		label.append(span);
		label.appendChild(document.createTextNode(nome));

		if (col == 1) {
			$(div1).append(label);
			$(div1).append(' ');
		} else {
			$(div2).append(label);
			$(div2).append(' ');
		}
	}

	function existeSelecionado(idSelecionado, elemento) {
		var pai = document.getElementById(elemento).childNodes;
		var i = 0;
		var existe = false;
		while (i != pai.length) {
			if (pai[i].id == idSelecionado) {
				existe = true;
			}
			i++;
		}
		return existe;
	}

	function loadRaizes(idDente) {
		$.ajax({
			url: _context + "/dente/" + idDente + "/raizes",
			type: 'GET',
			context: this,
			success: function (data) {
				var select = $("#raiz-select");
				select.empty();
				$.each(data, function(key, item) {
					select.append($('<option>', { 
						value: item.id,
						text : item.nome 
					}));
				});
				select.material_select();
			}
		});
	}

	$(".delete-patologia-all").click(function () {
		var id = $(this).attr("data-id");
		$.ajax({
			url: _context + "/odontograma/patologia/delete/" + id,
			type: 'GET',
			context: this,
			success: function () {
				$(this).parent().remove();
			}
		});
	});
})