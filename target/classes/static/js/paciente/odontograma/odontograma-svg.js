$(document).ready(function(){
	$.attrHooks['viewbox'] = {
		set : function(elem, value, name) {
			elem.setAttributeNS(null, 'viewBox', value + '');
			return value;
		}
	};

	// Código TOP começa aqui
	// Settings
  var pacienteId = $("#paciente-id").val();
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
	var _context = $("meta[name='_context']").attr("content");
	
	// Settings for odontograma
	if (_context == null) {
		_context = "";
	}
	$.ajax({
    url: _context + "/paciente/" + pacienteId + "/dentes",
    beforeSend: function (request) {
      request.setRequestHeader(header, token);
    },
    async: false,
    type: 'GET',
    success: function (data) {
      gerarArcadaPermanenteDente("PA", data);
      gerarArcadaPermanenteDente("PR", data);
      gerarArcadaPermanenteDente("PE", data);
    }
  });
});

var faceSuperior = "<path d='m342 740c443,-116 951,-92 1377,75 82,32 149,68 226,99l30 -49c64,-121 418,-784 429,-825l-2982 0c10,45 292,568 337,653 21,40 36,72 57,112l51 93c7,10 8,11 12,16 202,-81 214,-109 463,-174z'/>";
var faceLingual = "<path d='m1405 850c-316,-82 -661,-82 -978,0 -108,28 -323,101 -397,153l395 747c176,-68 252,-124 491,-124 237,0 317,55 494,126l393 -749c-79,-54 -286,-124 -398,-153z'/>";
var faceDistal = "<path  d='m300 1850l-410 -778c-67,29 -252,214 -297,271 -52,64 -74,81 -127,161 -457,677 -454,1481 3,2157 77,113 318,382 424,428l404 -781c-79,-118 -116,-120 -196,-312 -114,-272 -110,-568 2,-838 74,-178 112,-189 197,-308z'/>";
var faceOclusal = "<path  d='m820 1850c-400,53 -661,434 -615,826 46,394 398,694 798,641 399,-54 662,-432 617,-824 -46,-395 -398,-697 -800,-643z'/>";
var faceMesial = "<path  d='m1700 3050c-25,56 -66,128 -103,176 -63,83 -98,74 -49,164 83,149 317,626 378,716 158,-72 439,-435 532,-603 308,-561 308,-1249 0,-1810 -93,-168 -375,-531 -532,-603 -67,99 -292,564 -381,722 -43,77 -37,37 59,167 228,307 245,729 96,1071z'/>";
var faceVestibular = "<path d='m1800 4200l-397 -749c-394,170 -588,172 -986,-2l-392 751c578,285 1195,290 1775,0z'/>";
var faceInferior = "<path d='m1945 4300c-205,82 -218,112 -470,176 -368,95 -765,94 -1133,-2 -248,-64 -261,-92 -463,-174l-120 221c-44,81 -328,611 -337,653l2982 0 -459 -874z'/>";

// Arcadas
var arcadaSuperior = [18, 17, 16, 15, 14, 13, 12, 11, 21, 22, 23, 24, 25, 26, 27, 28];
var arcadaInferior = [48, 47, 46, 45, 44, 43, 42, 41, 31, 32, 33, 34, 35, 36, 37, 38];
var arcadaSuperiorDecidua = [55, 54, 53, 52, 51, 61, 62, 63, 64, 65];
var arcadaInferiorDecidua = [85, 84, 83, 82, 81, 71, 72, 73, 74, 75];

var espaco = 4500;

//Recebe o elemento no qual será injetado o svg e o tipo do odontograma e a lista de dentes
function gerarArcadaPermanenteDente(tipo, dentes) {
	var permanente = null;
	var decidua = null;
	if(tipo === "PA") {
		permanente = $("#arcada-permanente-patologias");
		decidua    =  $("#arcada-decidua-patologias");
	} else if(tipo === "PE") {
		permanente = $("#arcada-permanente-procedimentos-existentes");
		decidua    =  $("#arcada-decidua-procedimentos-existentes");
	} else if(tipo === "PR") {
		permanente = $("#arcada-permanente-procedimentos");
		decidua    =  $("#arcada-decidua-procedimentos");
	}
	permanente.attr("viewBox", "-1500 0 74500 14500");
	permanente.attr("width", "230mm");
	permanente.attr("height", "50mm");
	permanente.attr("version", "1.1");
	
	decidua.attr("viewBox", "1200 -900 42200 16000");
	decidua.attr("width", "150mm");
	decidua.attr("height", "50mm");
	decidua.attr("version", "1.1");

	permanente.svg();
	var svgPermanente = permanente.svg("get");

	decidua.svg();
	var svgDecidua = decidua.svg("get");

	//GERANDO A PARTE SUPERIOR DA ARCADA
	var posAux = 0;

	//Dentes de 18 a 11// XX
	var facesId = ["R", "V", "D", "O", "M", "L"];
	
	var g1Permanente = svgPermanente.group(); // superior
	var g2Permanente = svgPermanente.group({transform:"translate(0, 7500)"}); // inferior

	var g1Decidua = svgDecidua.group(); // superior
	var g2Decidua = svgDecidua.group({transform:"translate(0, 7500)"}); // inferior

	$.each(dentes, function (key, dente) {
		if(key == 0) {
			$(".geral").attr("data-id", dente.sextante.arcada.boca.id);
		}
		
		var g1 = ((dente.tipo === "PERMANENTE") ? g1Permanente : g1Decidua);
		var g2 = ((dente.tipo === "PERMANENTE") ? g2Permanente : g2Decidua);

 		var denteId = dente.id;
		var faces = dente.faces;
		var raizes = dente.raizes;
		var numero = parseInt(dente.numero.substring(1, 3));
		var index = 0;
		var g = null;
		var idRaizes = "";
		$.each(raizes, function(key, raiz) {
			idRaizes += " " + raiz.id;
		});

		if((numero >= 11 && numero <= 18) || (numero >= 21 && numero <= 28)) {
			index = arcadaSuperior.indexOf(numero);
			var translate = index * espaco;
			if(numero >= 21 && numero <= 28) {
				translate += 2000;
			}

			g = svgPermanente.group(g1, {id: tipo + "_" + numero, transform:"translate(" + translate + ")"});

			// Adicionando raiz separadamente, pois não é uma face
			var aux = svgPermanente.group(g, {id: numero + "_r", class: "face raiz" + idRaizes, face: numero + "_r", tipo: tipo, dente: denteId});
			svgPermanente.add(aux, faceSuperior);
			svgPermanente.text(g, 250, 7000, numero.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer; font-weight: bold;", tipo: tipo, dente: numero, id: denteId});
		} else if ((numero >= 41 && numero <= 48) || (numero >= 31 && numero <= 38)) {
			index = arcadaInferior.indexOf(numero);
			var translate = index * espaco;
			if(numero >= 31 && numero <= 38) {
				translate += 2000;
			}

			g = svgPermanente.group(g2, {id: tipo + "_" + numero, transform:"translate(" + translate + ", 2000)"});

			// Adicionando raiz separadamente, pois não é uma face
			var aux = svgPermanente.group(g, {id: numero + "_r", class: "face raiz", face: numero + "_r", tipo: tipo, dente: denteId});
			svgPermanente.add(aux, faceInferior);
			svgPermanente.text(g, 250, -600, numero.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer; font-weight: bold;", tipo: tipo, dente: numero, id: denteId});
		} else if ((numero >= 51 && numero <= 55) || (numero >= 61 && numero <= 65)) {
			index = arcadaSuperiorDecidua.indexOf(numero);
			var translate = index * espaco;
			if(numero >= 61 && numero <= 65) {
				translate += 2000;
			}

			g = svgDecidua.group(g1, {id: tipo + "_" + numero, transform:"translate(" + translate + ")"});

			// Adicionando raiz separadamente, pois não é uma face
			var aux = svgPermanente.group(g, {id: numero + "_r", class: "face raiz", face: numero + "_r", tipo: tipo, dente: denteId});
			svgPermanente.add(aux, faceInferior);
			svgPermanente.text(g, 250, 7000, numero.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer; font-weight: bold;", tipo: tipo, dente: numero, id: denteId});
		} else if ((numero >= 81 && numero <= 85) || (numero >= 71 && numero <= 75)) {
			index = arcadaInferiorDecidua.indexOf(numero);
			var translate = index * espaco;
			if(numero >= 71 && numero <= 75) {
				translate += 2000;
			}

			g = svgDecidua.group(g2, {id: tipo + "_" + numero, transform:"translate(" + translate + ", 2000)"});

			// Adicionando raiz separadamente, pois não é uma face
			var aux = svgPermanente.group(g, {id: numero + "_r", class: "face raiz", face: numero + "_r", tipo: tipo, dente: denteId});
			svgPermanente.add(aux, faceInferior);
			svgPermanente.text(g, 250, -600, numero.toString(), {fontFamily: 'inherit', fontSize: '1250', class:"txt-dente", style:"cursor: pointer; font-weight: bold;", tipo: tipo, dente: numero, id: denteId});
		}

		$.each(faces, function(i, face) {
			var nomeFace = face.nome;
			var faceId = face.id;
			var aux = svgPermanente.group(g, {id: faceId, class: "face", face: numero + "_" + faceId, tipo: tipo, dente: numero});
			if(nomeFace === "L") {
				svgPermanente.add(aux, faceLingual);
			} else if(nomeFace === "D") {
				svgPermanente.add(aux, faceDistal);
			} else if(nomeFace === "O") {
				svgPermanente.add(aux, faceOclusal);
			} else if(nomeFace === "M") {
				svgPermanente.add(aux, faceMesial);
			} else if(nomeFace === "V") {
				svgPermanente.add(aux, faceVestibular);
			}
		});
	});

}