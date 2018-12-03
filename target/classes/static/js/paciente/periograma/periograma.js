$(document).ready(function(){

	//checkboxes
	function checkIcon(){
		if($(this).html() == "check_box_outline_blank"){
			//checked style
			
			$(this).html("check_box");


			if ($(this).hasClass("sangramento-sondagem")){
				$(this).addClass("red-text");
			}
			
			if ($(this).parent().parent().hasClass("sangramento-marginal")){
				$(this).addClass("red-text");
			}
			
			if ($(this).parent().parent().hasClass("placa-bacteriana")){
				$(this).addClass("light-blue-text");
			}

			if ($(this).hasClass("supuracao")){
				$(this).addClass("green-text text-darken-3");
			}
			if ($(this).hasClass("supragengival")){
				$(this).addClass("orange-text text-lighten-3");
			}
		}
		else{
			//unchecked style
			$(this).html("check_box_outline_blank");
			$(this).removeClass("red-text green-text light-blue-text orange-text text-darken-3 text-lighten-3");
		}
	}
	//clickable-icon
	$('.periograma-icon').html("check_box_outline_blank");
	$('.periograma-icon').attr('checked', 0);
	$('.periograma-icon').on('click', checkIcon);
	
	//clickable-cell
	$('.clickable-cell').children().prop('onclick',null).off('click');
	$('.clickable-cell').on('click', function(){
		checkIcon.call($(this).children());
	});

	//furca
	function checkFurca(){
		$value = parseInt($(this).attr("value"));
		$new_value = ($value+1)%4;
		console.log($new_value);
		//incrementa o value
		$(this).attr("value", $new_value);
		if(($new_value)===0){
			$(this).addClass("almost-hidden");
		}
		else{
			$(this).removeClass("almost-hidden");
			$(this).attr('src', '/img/periograma/furca-'+$(this).attr("value")+'.png');
			console.log($(this).attr('src'));
		}
	}
	//clickable-furca
	$('.furca').attr('src', '/img/periograma/furca-3.png');
	$('.furca').addClass("almost-hidden");
	$('.furca').attr("value", 0);
	$('.furca').on('click', checkFurca);
	//disabled
	$('.disabled').removeClass("almost-hidden");
	$('.disabled').prop('onclick', null).off('click');
	//clickable-cell-furca
	$('.clickable-cell-furca').children().children().prop('onclick',null).off('click');
	$('.clickable-cell-furca').on('click', function(){
		checkFurca.call($(this).children().children());
	});
	
	//Modal PSR
	function fecharlimparModal() {

		$('input[type=radio][name=psrRadio]').prop('checked', false);
		$("#selecionarAsterisco").prop('checked', false);
		$("#sextanteAusente").prop('checked', false);
		$('input[type=radio][name=psrRadio]').prop('disabled', false);
		$('input[type=checkbox][name=psrCheck][id!=sextanteAusente]').prop(
				'disabled', false);
		$("#modal-psr").closeModal();
	}

	$(document)
			.ready(
					function() {

						var inputActiveID = "";
						var inputValue = "";

						$('.tooltipped').tooltip('remove');
						$(".tooltipped")
								.on(
										"mouseleave",
										function() {
											inputActiveID = $(this).attr("id");
											var valor = $("#" + inputActiveID).val();
											if (valor == "") {
												console.log("true");
												$("#" + inputActiveID).tooltip(
														'remove');
											} else if (valor == '0' || valor == '0*') {
												$("#" + inputActiveID)
														.tooltip(
																{
																	delay : 50,
																	html : true,
																	tooltip : 'Ausência de sangramento e fatores retentivos e faixa escura da sonda totalmente visível;'
																});
											} else if (valor == '1' || valor == '1*') {
												$("#" + inputActiveID)
														.tooltip(
																{
																	delay : 50,
																	html : true,
																	tooltip : 'Presença de sangramento à sondagem, /ausência de fatores retentivos/faixa escura da sonda totalmente visível;'
																});
											} else if (valor == '2' || valor == '2*') {
												$("#" + inputActiveID)
														.tooltip(
																{
																	delay : 50,
																	html : true,
																	tooltip : 'Presença de fatores retentivos de placa (cálculo, restaurações com falhas marginais, cáries)/faixa escura totalmente visível;'
																});
											} else if (valor == '3' || valor == '3*') {
												$("#" + inputActiveID)
														.tooltip(
																{
																	delay : 50,
																	html : true,
																	tooltip : 'Faixa escura da sonda parcialmente visível - 4 a 5 milímetros de profundidade de sondagem;'
																});
											} else if (valor == '4' || valor == '4*') {

												$("#" + inputActiveID)
														.tooltip(
																{
																	delay : 50,
																	html : true,
																	tooltip : 'Faixa escura da sonda não mais visível - 6 milímetros ou mais de profundidade de sondagem'
																});

											}
										});

						$('input[type=radio][name=psrRadio]').change(function() {
							inputValue = $(this).val();
						});

						$('#sextanteAusente')
								.change(
										function() {
											if ($(this).is(':checked')) {
												$(
														'input[type=radio][name=psrRadio]')
														.prop('disabled', true);
												$(
														'input[type=checkbox][name=psrCheck][id!=sextanteAusente]')
														.prop('disabled', true);
											} else {
												$(
														'input[type=radio][name=psrRadio]')
														.prop('disabled', false);
												$(
														'input[type=checkbox][name=psrCheck][id!=sextanteAusente]')
														.prop('disabled', false);
											}
										});

						$("#adicionarPSR")
								.click(
										function() {
											if ($("#sextanteAusente").is(':checked')) {
												$("#" + inputActiveID)
												.val("Sextante Ausente");
												inputActiveID = "";
												inputValue = "";
												fecharlimparModal();
											} else if(inputValue != ''){
												$("#" + inputActiveID)
												.val(inputValue+ ($('#selecionarAsterisco')
																		.is(':checked') ? "*"
																		: ""));
												inputActiveID = "";
												inputValue = "";
												fecharlimparModal();
											}else{
												if ($('#selecionarAsterisco').is(':checked')) {
													var $toastContent = $('<span>Erro!</span>');
													Materialize.toast($toastContent, 3000);
												}else{
													$("#" + inputActiveID).val('');
													inputActiveID = "";
													inputValue = "";
													fecharlimparModal();	
												}
											}
										});
						$("#cancelarPSR").click(function() {
							fecharlimparModal();
						});
						$(".selecionar-psr")
								.focus(
										function() {
											$('.tooltipped').tooltip('remove');
											inputActiveID = $(this).attr("id");
											if ($(this).val() == 'Sextante Ausente') {
												$("#sextanteAusente").prop(
														'checked', true);
												$(
														'input[type=radio][name=psrRadio]')
														.prop('disabled', true);
												$(
														'input[type=checkbox][name=psrCheck][id!=sextanteAusente]')
														.prop('disabled', true);
											}
											$("#modal-psr").openModal();
										});
					});
					loadSVGs();
});

// Outros fatores

(function($) {
	
	const idsFatores = ['cc', 'pma', 'ri', 'rr', 'mpd', 'todos'];
	const TODOS = 'todos';
	var checkedFatores = []; 
	var nodeUl = null;
	
	$(document).ready(function() {
		
		$('.selecionar-outro-fator').on('click', function() {
			checkedFatores = [];
			nodeUl = $(this).find('ul');
			
			var listCheckbox = $('.outros-fatores-checkbox');

			for(var i = 0; i < listCheckbox.length; i++) {
				var input = $(listCheckbox).find('input');
				$(input).	attr("checked", false);
			}
			
			$('#modal-outros-fatores').openModal();
		});
		
		idsFatores.forEach(function(id) {
			$('#'+id).on('click', function() {
				if(checkedFatores.indexOf(this.id) != -1) {
					checkedFatores.splice(checkedFatores.indexOf(this.id), 1);
				}
				else {
					checkedFatores.push(this.id);
				}
			});
		});
		

		function buildTemplate(str1, str2) {
			var template = "<div>";
			if(str1 != undefined && str1 != 'todos') {
				template += "<span class='periograma-badge'>" + str1.toUpperCase() + "</span>";
			} else {
				template += "<span class='periograma-badge periograma-badge-invisivel'>MPD</span>";
			}
			if(str2 != undefined && str2 != 'todos') {
				template += "<span class='periograma-badge'>" + str2.toUpperCase() + "</span>";
			} else {
				template += "<span class='periograma-badge periograma-badge-invisivel'>MPD</span>";
			}
			template += "</div>";
		
			return template;
		}

		$('#adicionarOutroFator').on('click', function() {
			template = '';
			console.log(checkedFatores);
			if(checkedFatores.indexOf(TODOS) != -1) {

				for(var i = 0; i < 6; i +=2 ) {
					template += "<li>";
					template += buildTemplate(idsFatores[i], idsFatores[i+1]);
					template += "</li>";
				}

			} else if(checkedFatores.length != 0) {
				for(var i = 0; i < 6; i +=2 ) {
					template += "<li>";
					template += buildTemplate(checkedFatores[i], checkedFatores[i+1]);
					template += "</li>";
				}
			} else {
				for(var i = 0; i < 6; i +=2 ) {
					template += "<li>";
					template += buildTemplate(null, null);
					template += "</li>";
				}
			}
			$(nodeUl).remove("li");
			$(nodeUl).empty();
			$(nodeUl).html(template);
			checkedFatores = [];
		    $('#modal-outros-fatores').closeModal();	
		})
		
		$('#cancelarOutroFator').on('click', function() {
			checkedFatores = [];			
		    $('#modal-outros-fatores').closeModal();	
		})
	});	
})(jQuery)

//canvas
let canvas_upper_teeth_vestibular, canvas_upper_teeth_palatina, canvas_lower_teeth_lingual, canvas_lower_teeth_vestibular;

//polylines
let upper_vestibular_margem_gengival_polyline, upper_palatina_margem_gengival_polyline, lower_lingual_margem_gengival_polyline, lower_vestibular_margem_gengival_polyline;
let upper_vestibular_profundidade_sondagem_polyline, upper_palatina_profundidade_sondagem_polyline, lower_lingual_profundidade_sondagem_polyline, lower_vestibular_profundidade_sondagem_polyline;

//fills
let upper_vestibular_fill, upper_palatina_fill, lower_lingual_fill, lower_vestibular_fill;

//distância entre linhas verticais no canvas
let distance = 5.92;

//carrega SVGs (dentes)
function loadSVGs() {
	
	//fase 2
	$('#upper-teeth-vestibular').load('/img/paciente/periograma/dentes_superiores_vestibular.min.svg', function() {
		for (let i = 1; i <= 8; i++) {
			$(`.teeth-svg-container #vestibular_1${i}_implante`).toggle();
			$(`.teeth-svg-container #vestibular_2${i}_implante`).toggle();
			if (i >= 6 && i <= 8) {
				for (let f = 1; f <= 3; f++) {
					$(`.teeth-svg-container #vestibular_1${i}_furca_${f}`).toggle();
					$(`.teeth-svg-container #vestibular_2${i}_furca_${f}`).toggle();
				}
			}
		}
	});
	$('#upper-teeth-palatina').load('/img/paciente/periograma/dentes_superiores_palatina.min.svg', function() {
		for (let i = 1; i <= 8; i++) {
			$(`.teeth-svg-container #palatina_1${i}_implante`).toggle();
			$(`.teeth-svg-container #palatina_2${i}_implante`).toggle();
			if (i >= 6 && i <= 8 || i === 4) {
				for (let f = 1; f <= 3; f++) {
					$(`.teeth-svg-container #palatina_1${i}_furca_esquerda_${f}`).toggle();
					$(`.teeth-svg-container #palatina_1${i}_furca_direita_${f}`).toggle();
					$(`.teeth-svg-container #palatina_2${i}_furca_esquerda_${f}`).toggle();
					$(`.teeth-svg-container #palatina_2${i}_furca_direita_${f}`).toggle();
				}
			}
		}
	});
	$('#lower-teeth-lingual').load('/img/paciente/periograma/dentes_inferiores_lingual.min.svg', function() {
		for (let i = 1; i <= 8; i++) {
			$(`.teeth-svg-container #lingual_3${i}_implante`).toggle();
			$(`.teeth-svg-container #lingual_4${i}_implante`).toggle();
			if (i >= 6 && i <= 8) {
				for (let f = 1; f <= 3; f++) {
					$(`.teeth-svg-container #lingual_3${i}_furca_${f}`).toggle();
					$(`.teeth-svg-container #lingual_4${i}_furca_${f}`).toggle();
				}
			}
		}
	});
	$('#lower-teeth-vestibular').load('/img/paciente/periograma/dentes_inferiores_vestibular.min.svg', function() {
		for (let i = 1; i <= 8; i++) {
			$(`.teeth-svg-container #vestibular_3${i}_implante`).toggle();
			$(`.teeth-svg-container #vestibular_4${i}_implante`).toggle();
			if (i >= 6 && i <= 8) {
				for (let f = 1; f <= 3; f++) {
					$(`.teeth-svg-container #vestibular_3${i}_furca_${f}`).toggle();
					$(`.teeth-svg-container #vestibular_4${i}_furca_${f}`).toggle();
				}
			}
		}
	});
	
	canvas_upper_teeth_vestibular = SVG('canvas-upper-teeth-vestibular').size(840, 160);
	canvas_upper_teeth_palatina = SVG('canvas-upper-teeth-palatina').size(840, 160);
	canvas_lower_teeth_lingual = SVG('canvas-lower-teeth-lingual').size(840, 160);
	canvas_lower_teeth_vestibular = SVG('canvas-lower-teeth-vestibular').size(840, 160);
	    
	// desenha o grid horizontal
    let v = 0;
    for (let i = -17; i <=9; i++) {
        if (i <= 0) {
        	canvas_upper_teeth_vestibular.polyline(`0,${v} 840,${v}`).stroke('rgba(0, 0, 0, 0.42)').stroke({width: 1});
        	canvas_upper_teeth_palatina.polyline(`0,${v} 840,${v}`).stroke('rgba(0, 0, 0, 0.42)').stroke({width: 1});
        } else {
//        	canvas_upper_teeth_vestibular.polyline(`0,${v} 840,${v}`).stroke('#C2C2C2').stroke({width: 1});
//        	canvas_upper_teeth_palatina.polyline(`0,${v} 840,${v}`).stroke('#C2C2C2').stroke({width: 1});
        }
        v+=distance; //distância entre linhas horizontais
    }
    v = 0;
    for (let i = 9; i >=-17; i--) {
        if (i <= 0) {
        	canvas_lower_teeth_lingual.polyline(`0,${v} 840,${v}`).stroke('rgba(0, 0, 0, 0.42)').stroke({width: 1});
        	canvas_lower_teeth_vestibular.polyline(`0,${v} 840,${v}`).stroke('rgba(0, 0, 0, 0.42)').stroke({width: 1});
        } else {
//        	canvas_lower_teeth_lingual.polyline(`0,${v} 840,${v}`).stroke('#C2C2C2').stroke({width: 1});
//        	canvas_lower_teeth_vestibular.polyline(`0,${v} 840,${v}`).stroke('#C2C2C2').stroke({width: 1});
        }
        v+=distance; //distância entre linhas horizontais
    }
    
    //upper-vestibular
    upper_vestibular_fill = canvas_upper_teeth_vestibular.polyline('').stroke({width: 2}).stroke('none').fill('rgba(0, 0, 100, 0.5)');
    upper_vestibular_profundidade_sondagem_polyline = canvas_upper_teeth_vestibular.polyline('').stroke({width: 2}).stroke('blue').fill('none');
	upper_vestibular_margem_gengival_polyline = canvas_upper_teeth_vestibular.polyline('').stroke({width: 2}).stroke('red').fill('none');
    
    //upper-palatina
    upper_palatina_fill = canvas_upper_teeth_palatina.polyline('').stroke({width: 2}).stroke('none').fill('rgba(0, 0, 100, 0.5)');
    upper_palatina_profundidade_sondagem_polyline = canvas_upper_teeth_palatina.polyline('').stroke({width: 2}).stroke('blue').fill('none');
	upper_palatina_margem_gengival_polyline = canvas_upper_teeth_palatina.polyline('').stroke({width: 2}).stroke('red').fill('none');
	
	//lower-lingual
	lower_lingual_fill = canvas_lower_teeth_lingual.polyline('').stroke({width: 2}).stroke('none').fill('rgba(0, 0, 100, 0.5)');
	lower_lingual_profundidade_sondagem_polyline = canvas_lower_teeth_lingual.polyline('').stroke({width: 2}).stroke('blue').fill('none');
	lower_lingual_margem_gengival_polyline = canvas_lower_teeth_lingual.polyline('').stroke({width: 2}).stroke('red').fill('none');
	 
	//lower-vestibular
	lower_vestibular_fill = canvas_lower_teeth_vestibular.polyline('').stroke({width: 2}).stroke('none').fill('rgba(0, 0, 100, 0.5)');
	lower_vestibular_profundidade_sondagem_polyline = canvas_lower_teeth_vestibular.polyline('').stroke({width: 2}).stroke('blue').fill('none');
	lower_vestibular_margem_gengival_polyline = canvas_lower_teeth_vestibular.polyline('').stroke({width: 2}).stroke('red').fill('none');
	
	setToggles();
}

//configura os toggles
function setToggles() {
	//toggle implantes
	$('.implante-cell-container i').click(function() {
		let numero_dente = parseInt(this.id.replace(/\D/g, ''));
		$(`.teeth-svg-container #vestibular_${numero_dente}_implante`).toggle();
		$(`.teeth-svg-container #vestibular_${numero_dente}_dente`).toggle();
		$(`.teeth-svg-container #furca-${numero_dente}`).toggleClass('disabled');
		if (numero_dente >= 11 && numero_dente <= 28) {
			$(`.teeth-svg-container #palatina_${numero_dente}_implante`).toggle();
			$(`.teeth-svg-container #palatina_${numero_dente}_dente`).toggle();
		} else {
			$(`.teeth-svg-container #lingual_${numero_dente}_implante`).toggle();
			$(`.teeth-svg-container #lingual_${numero_dente}_dente`).toggle();
		}
	});
	
	//toggle furcas
	$('.furca-cell-container img').click(function() {
		let container = $(this).parents('.furca-cell-container');
		let valor_furca = parseInt($(this).attr('value')) + 1;
		let dente = this.id.replace(/\D/g, '');
		let furca, face;
		
		if ($(container).hasClass('upper-vestibular') || $(container).hasClass('lower-vestibular')) {
			face = 'vestibular'; 
		}
		if ($(container).hasClass('upper-palatina')) {
			face = 'palatina';
		} 
		if ($(container).hasClass('lower-lingual')) {
			face = 'lingual';
		}
		if (face == 'palatina') {
			furca = this.id.substring(this.id.indexOf("-") + 1);
			furca = furca.substring(furca.indexOf('-') + 1);
		} else {
			furca = this.id.substring(this.id.indexOf("-") + 1);
		}
		if (face == 'palatina') {
			$(`#fase2 #${face}_${dente}_furca_direita_${furca}`).hide();
			$(`#fase2 #${face}_${dente}_furca_esquerda_${furca}`).hide();
		} else {
			for (let i = 0; i < valor_furca; i++) {
				$(`#fase2 #${face}_${dente}_furca_${i}`).hide();
			}
		}
		if (valor_furca !== 4) {
			if (face == 'palatina') {
				$(`#fase2 #${face}_${dente}_furca_${furca}`).show();
				$(`#fase2 #${face}_${dente}_furca_${furca}_${valor_furca}`).show();
			} else {
				$(`#fase2 #${face}_${dente}_furca_${valor_furca}`).show();
			}
		}
	});
}

let dentes_data = {};

//inputs de recessão gengival e profundidade de sondagem
$('.recessao-profundidade-input-container input').change(function() {
	let parsedValue = parseFloat($(this).val());
    if (parsedValue){
		if (parsedValue > 9) {
	        $(this).val(9);
	    } else if (parsedValue < -17) {
	        $(this).val(-17);
	    }
    } else {
    	$(this).val(0);
    }
    
	let container = $(this).parents('.recessao-profundidade-input-container');
	let face, classe_canvas;
	if ($(container).hasClass('upper-vestibular')) {
		face = 'vestibular';
		classe_canvas = 'upper-vestibular';
	} 
	if ($(container).hasClass('lower-vestibular')) {
		face = 'vestibular';
		classe_canvas = 'lower-vestibular';
	} 
	if ($(container).hasClass('upper-palatina')) {
		face = 'palatina';
		classe_canvas = 'upper-palatina';
	} 
	if ($(container).hasClass('lower-lingual')) {
		face = 'lingual';
		classe_canvas = 'lower-lingual';
	}
	
	let dente = this.id.slice(18, 20);
	let pos = this.id.slice(21, 22);
	let value = parseInt(this.value);
	
	atualizarDentesData(dentes_data, `${face}_${dente}.margem_gengival.${pos}`, value);
	
	drawGraph(dentes_data, face, classe_canvas);
});

function atualizarDentesData(obj, arr, val) {
    if (typeof arr == 'string')
        arr = arr.split(".");
    obj[arr[0]] = obj[arr[0]] || {};
    var tmpObj = obj[arr[0]];
    if (arr.length > 1) {
        arr.shift();
        atualizarDentesData(tmpObj, arr, val);
    }
    else
        obj[arr[0]] = val;
    return obj;
}

function drawGraph(data, face, classe_canvas) {
	
	let profundidade_sondagem_polyline_data = '';

    let xAxisPositions = [
    	[20, 37, 55], [70, 90, 108], [120, 148, 174], [186, 199, 212], [226, 240, 252],
    	[294, 306, 320], [336, 348, 362], [380, 394, 410], [430, 446, 460], [476, 490, 502], [518, 530, 546],
    	[584, 598, 610], [624, 638, 652], [664, 690, 716], [728, 748, 768], [784, 800, 820]
    ];
    
    let yAxisPositions, dentes;
    
    if (classe_canvas == 'upper-vestibular' || classe_canvas == 'upper-palatina') {
    	dentes = [18, 17, 16, 15, 14, 13, 12, 11, 21, 22, 23, 24, 25, 26, 27, 28];
    	yAxisPositions = {'-17':0, '-16':5.92, '-15':11.84, '-14':17.75, '-13':23.68, '-12':29.6, '-11':35.52, '-10':41.44,
						  '-9':47.36, '-8':53.28, '-7':59.2, '-6':65.12, '-5':71.04, '-4':76.96, '-3':82.88, '-2':88.8,
						  '-1':94.72, '0':100.64, '1':106.56, '2':112.48, '3':118.4, '4':124.32, '5':130.24, '6':136.16,
						  '7':142.07, '8':147.99, '9':153.91};
    } else {
    	dentes = [48, 47, 46, 45, 44, 43, 42, 41, 31, 32, 33, 34, 35, 36, 37, 38];
    	yAxisPositions = {'-17':153.91, '-16':147.99, '-15':142.07, '-14':136.16, '-13':130.24, '-12':124.32, '-11':118.4, '-10':112.48,
						  '-9':106.56, '-8':100.64, '-7':94.72, '-6':88.8, '-5':82.88, '-4':76.96, '-3':71.04, '-2':65.12,
						  '-1':59.2, '0':53.28, '1':47.36, '2':41.44, '3':35.52, '4':29.6, '5':23.68, '6':17.75,
						  '7':11.84, '8':5.92, '9':0};
    }
    
    let array_margem_gengival = [];
    
    for (let x = 0; x < xAxisPositions.length; x++) {
    	for (let y = 0; y < 3; y++) {
    		// RECESSÃO
    		let id_dente_recessao_gengival = `${face}-recessao-gengival-${dentes[x]}-${y+1}`; 		
    		let valor_dente_recessao_gengival = document.getElementById(id_dente_recessao_gengival).value;
    		
    		//PROF_SONDAGEM
    		let id_dente_profundidade_sondagem = `${face}-profundidade-sondagem-${dentes[x]}-${y+1}`;
    		let valor_dente_profundidade_sondagem = document.getElementById(id_dente_profundidade_sondagem).value;
    		let diferenca_recessao_profundidade = parseFloat(valor_dente_recessao_gengival) - parseFloat(valor_dente_profundidade_sondagem);
    		
    		if (diferenca_recessao_profundidade < -17) {
    			diferenca_recessao_profundidade = -17;
    		} else if (diferenca_recessao_profundidade > 9) {
    			diferenca_recessao_profundidade = 9;
    		}
 
    		profundidade_sondagem_polyline_data += `${xAxisPositions[x][y]}, ${yAxisPositions[diferenca_recessao_profundidade]} `;
    		
    		array_margem_gengival.push(`${xAxisPositions[x][y]}, ${yAxisPositions[valor_dente_recessao_gengival]} `);
    	}
    }
    
    //fill
    let inverted_margem_gengival_polyline_data = array_margem_gengival.reverse().toString();
    let margem_gengival_polyline_data = array_margem_gengival.toString();
    polygon_data = profundidade_sondagem_polyline_data + inverted_margem_gengival_polyline_data;

    let fill, profundidade_sondagem_polyline, margem_gengival_polyline;
    
    //identifica fill e polylines a ser utilizado
    if(classe_canvas == "upper-vestibular"){
    	fill = upper_vestibular_fill;
    	profundidade_sondagem_polyline = upper_vestibular_profundidade_sondagem_polyline;
    	margem_gengival_polyline = upper_vestibular_margem_gengival_polyline;
    }
    if(classe_canvas == "upper-palatina"){
    	fill = upper_palatina_fill;
    	profundidade_sondagem_polyline = upper_palatina_profundidade_sondagem_polyline;
    	margem_gengival_polyline = upper_palatina_margem_gengival_polyline;
    }
    if(classe_canvas == "lower-lingual"){
    	fill = lower_lingual_fill;
    	profundidade_sondagem_polyline = lower_lingual_profundidade_sondagem_polyline;
    	margem_gengival_polyline = lower_lingual_margem_gengival_polyline;
    }
    if(classe_canvas == "lower-vestibular"){
    	fill = lower_vestibular_fill;
    	profundidade_sondagem_polyline = lower_vestibular_profundidade_sondagem_polyline;
    	margem_gengival_polyline = lower_vestibular_margem_gengival_polyline;
    }
    
    fill.plot(polygon_data);
    profundidade_sondagem_polyline.plot(profundidade_sondagem_polyline_data);
    margem_gengival_polyline.plot(margem_gengival_polyline_data);
}