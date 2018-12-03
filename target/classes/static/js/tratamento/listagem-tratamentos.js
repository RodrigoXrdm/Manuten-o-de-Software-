var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
var _context = $("meta[name='_context']").attr("content");

$(document).ready(function() {
	if (_context == null) {
		_context = "";
	}

	loadListeners();
});

function loadListeners() {
	$(".button-open-modal-excluir").on("click", function() {
		if (!$(this).hasClass("disabled")) {
			var link_exclusao_tratamento = $(this).attr("link");
			$("#modal-excluir-tratamento").openModal();
		}

		$("#cancelar-excluir-tratamento").on("click", function() {
			$("#modal-excluir-tratamento").closeModal();
		});

		$("#confirm-excluir-tratamento").attr("href", link_exclusao_tratamento);
		
		$("#confirm-excluir-tratamento").on("click", function() {
			$("#modal-excluir-tratamento").closeModal();
		});
	});
}