function normalizeText(text) {
    return text
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, "");
}

$('#formulario input').each(function (){
	$(this).on('keyup', function(){
		$(this).val(normalizeText($(this).val()));
	})
})


$('#formulario input[type=radio]').on('change', function () {
	var demanda = $('input[name=tipo_cadastro]:checked', '#formulario').val();
	
	if(demanda === "SUS") {
		$('#raca').attr("required", "required");
		$('#raca_label').text("* Raça");
		
		console.log($('#raca'))
		
		$('#cns').attr("required", "required");
		$('#cns_label').text("* Cartão Nacional do SUS");
		
		$('#cep').attr("required", "required");
		$('#cep_label').text("* CEP");
		
		$('#cidade').attr("required", "required");
		$('#cidade_label').text("* Cidade");
		
		$('#estado').attr("required", "required");
		$('#estado_label').text("* Estado");
		
		$('#pais').attr("required", "required");
		$('#pais_label').text("* País");
		
		$('#naturalidade').attr("required", "required");
		$('#naturalidade_label').text("* Naturalidade");
		
		$('#nacionalidade').attr("required", "required");
		$('#nacionalidade_label').text("* Nacionalidade");
		
		$("select[required]").css({display: "block", height: 0, padding: 0, width: 0, position: 'absolute'});
	}
	
	else {
		$('#raca').removeAttr("required");
		$('#raca_label').text("Raça");
		
		$('#cns').removeAttr("required");
		$('#cns_label').text("Cartão Nacional do SUS");
		
		$('#cep').removeAttr("required");
		$('#cep_label').text("CEP");
		
		$('#cidade').removeAttr("required");
		$('#cidade_label').text("Cidade");
		
		$('#estado').removeAttr("required");
		$('#estado_label').text("Estado");
		
		$('#pais').removeAttr("required");
		$('#pais_label').text("País");
		
		$('#naturalidade').removeAttr("required");
		$('#naturalidade_label').text("Naturalidade");
		
		$('#nacionalidade').removeAttr("required");
		$('#nacionalidade_label').text("Nacionalidade");
		
		$("select[required]").css({display: "block", height: 0, padding: 0, width: 0, position: 'absolute'});
	}
});

$("select[required]").css({display: "block", height: 0, padding: 0, width: 0, position: 'absolute'});



