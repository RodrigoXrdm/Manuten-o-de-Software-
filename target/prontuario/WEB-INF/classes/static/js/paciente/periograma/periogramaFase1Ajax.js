
$(document).ready(function(){
    
    function normalizeText(text) {
        return text
            .normalize('NFD')
            .replace(/ /ig, "-")
            .replace(/[\u0300-\u036f]/g, "")
            .toLowerCase();
    }
    
    function buildTemplate(str1, str2) {
        var template = '<div>';
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
        template += '</div>';
    
        return template;
    }


    var idPeriograma = $("#id-periograma").val();
    console.log(idPeriograma);
    $.ajax({
        type: 'GET',
        url: '/periograma-fase1/' + idPeriograma,
        dataType: 'json',
        success: function (dados, status) {
            dados.patologias.forEach(function(patologia) {
                switch(normalizeText(patologia.tipo.nome)){
                    case "psr":
                        var id = normalizeText(patologia.tipo.nome) + '-' + normalizeText(patologia.estrutura.nome);
                        var $InputPSR = $('#' + id);
                        $InputPSR.val(patologia.descricao);
                        break;
                    case "calculo-supragengival":
                        var id = normalizeText(patologia.tipo.nome) + '-' + normalizeText(patologia.estrutura.numero);
                        var $CheckBoxCalculoSupragengival = $('#' + id);
                        $CheckBoxCalculoSupragengival.addClass('orange-text');
                        $CheckBoxCalculoSupragengival.addClass('text-lighten-3');
                        $CheckBoxCalculoSupragengival.html('check_box');
                        break;
                    case "outros-fatores":
                        var id = normalizeText(patologia.tipo.nome) + '-' + normalizeText(patologia.estrutura.numero);
                        var $TdOutrosFatores = $('#' + id);
                        var $NodeUl = $TdOutrosFatores.find('ul');
                        var fatores = patologia.descricao.split(',');
                        var template = '';
                        for(var i = 0; i < 6; i +=2 ) {
                            template += '<li>';
                            template += buildTemplate(fatores[i], fatores[i+1]);
                            template += '</li>';
                        }
                        $($NodeUl).remove("li");
                        $($NodeUl).empty();
                        $($NodeUl).html(template);
                        break;
                    case "sangramento-marginal":
                        var id = normalizeText(patologia.tipo.nome) + '-' + normalizeText(patologia.estrutura.dente.numero) + '-f' + normalizeText(patologia.estrutura.nome);
                        var $CheckBoxSangramentoMarginal = $('#' + id); 
                        $CheckBoxSangramentoMarginal.addClass('red-text');
                        $CheckBoxSangramentoMarginal.html('check_box');
                        break;
                    case "placa-bacteriana":
                        var id = normalizeText(patologia.tipo.nome) + '-' + normalizeText(patologia.estrutura.dente.numero) + '-f' + normalizeText(patologia.estrutura.nome);
                        var $CheckBoxPlacaBacteriana = $('#' + id); 
                        $CheckBoxPlacaBacteriana.addClass('light-blue-text');
                        $CheckBoxPlacaBacteriana.html('check_box');
                        break;
                }
            
            });
            console.log(dados);
        }
    });


/**
 * Métodos Ajax para atualizar status do periograma de um atendimento.
 */
$('.status-periograma').each(function(index) {
    var $InputRadioStatus = $(this);

    $InputRadioStatus.on('click', function(event) {
        var dados = {
            status: $InputRadioStatus.val(),
            periogramaId: $InputRadioStatus.attr('data-periograma')
        };
        $.ajax({
            type: 'PUT',
            url: '/periograma-fase1/status',
            dataType: 'json',
            success: function (response, status) {},
            data: dados
        });
    });
});


/**
 * Métodos Ajax para buscar e salvar PSR em um sextante do paciente.
 */
    // POST PSR
    PubSub.subscribe('PSR', function(msg, id) {
        var $InputPSR = $('#' + id);
        var dados = {
            descricao: $InputPSR.val(),
            sextanteId: $InputPSR.attr('data-sextante'),
            periogramaId: $InputPSR.attr('data-periograma')
        };
        $.post('/periograma-fase1/psr', dados, function(response, status) {});
    });

/**
 * Métodos Ajax para buscar e salvar o cálculo supragengival em um dente do paciente.
 */
    // POST, DELETE e GET Cálculo Supragengival 
    $('.supragengival')
    .each(function(index) {
        var $CheckBoxCalculoSupragengival = $(this);
        
        var dados = {
            periogramaId: $CheckBoxCalculoSupragengival.attr('data-periograma'),
            pacienteId: $CheckBoxCalculoSupragengival.attr('data-paciente'),
            numeroDente: 'D' + $CheckBoxCalculoSupragengival.attr('data-dente')
        };

        // POST e DELETE
        $CheckBoxCalculoSupragengival.on('click', function(event) {
            if($CheckBoxCalculoSupragengival.html() == 'check_box') {
                $.post('/periograma-fase1/calculo-supragengival', dados, function(response, status) {});
            } else {
                $.ajax({
                    type: 'DELETE',
                    url: '/periograma-fase1/calculo-supragengival?' + $.param(dados),
                    dataType: 'json',
                    success: function (response, status) {}
                });
            }
        });

    });

/**
 * Métodos Ajax para buscar e salvar outros fatores em um dente do paciente.
 */
    // POST Outros fatores
    PubSub.subscribe('OUTROS_FATORES', function(msg, bundle) {
        var dados = {
            periogramaId: bundle.$TdOutrosFatores.attr('data-periograma'),
            pacienteId: bundle.$TdOutrosFatores.attr('data-paciente'),
            numeroDente: 'D' + bundle.$TdOutrosFatores.attr('data-dente'),
            descricao: bundle.fatores.join(',')
        };

        $.post('/periograma-fase1/outros-fatores', dados, function(response, status) {});
    });

/**
 * Métodos Ajax para buscar e salvar sangramento marginal em uma face de um dente do paciente.
 */
    // POST, DELETE e GET Cálculo Supragengival 
     $('.sangramento-marginal-face').each(function(index) {
        var $CheckBoxSangramentoMarginal = $(this);

        var dados = {
            periogramaId: $CheckBoxSangramentoMarginal.attr('data-periograma'),
            pacienteId: $CheckBoxSangramentoMarginal.attr('data-paciente'),
            numeroDente: 'D' + $CheckBoxSangramentoMarginal.attr('data-dente'),
            face: $CheckBoxSangramentoMarginal.attr('data-face')
        };

        // POST e DELETE
        $CheckBoxSangramentoMarginal.on('click', function(event) {
            if($CheckBoxSangramentoMarginal.html() == 'check_box') {
                $.post('/periograma-fase1/sangramento-marginal', dados, function(response, status) {});
            } else {
                $.ajax({
                    type: 'DELETE',
                    url: '/periograma-fase1/sangramento-marginal?' + $.param(dados),
                    dataType: 'json',
                    success: function (response, status) {}
                });
            }
        });

     });

/**
 * Métodos Ajax para buscar e salvar placa bacteriana em uma face de um dente do paciente.
 */
    $('.placa-bacteriana-face').each(function(index) {
        var $CheckBoxPlacaBacteriana = $(this);

        var dados = {
            periogramaId: $CheckBoxPlacaBacteriana.attr('data-periograma'),
            pacienteId: $CheckBoxPlacaBacteriana.attr('data-paciente'),
            numeroDente: 'D' + $CheckBoxPlacaBacteriana.attr('data-dente'),
            face: $CheckBoxPlacaBacteriana.attr('data-face')
        };

        // POST e DELETE
        $CheckBoxPlacaBacteriana.on('click', function(event) {
            if($CheckBoxPlacaBacteriana.html() == 'check_box') {
                $.post('/periograma-fase1/placa-bacteriana', dados, function(response, status) {});
            } else {
                $.ajax({
                    type: 'DELETE',
                    url: '/periograma-fase1/placa-bacteriana?' + $.param(dados),
                    dataType: 'json',
                    success: function (response, status) {}
                });
            }
        });

     });

});
    