$(document).ready(function() {
  
  // Settings
  var pacienteId = $("#paciente-id").val();
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  var _context = $("meta[name='_context']").attr("content");
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
      $.each(data, function (key, value) {
        //console.log(value);
      });
    }
  });
});