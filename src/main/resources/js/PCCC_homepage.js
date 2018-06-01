$(function() {
	var pages = $("#container1").find("div[for='page']");
	pages.each(function() {
		if (!$(this).hasClass("hidden")) {
			$(this).addClass("hidden");
		}
	});
});

function afficheAccueil(divPageId) {
	window.location.href = "/static?pccc_homepage.html";
};

function ouvreLien(url) {
	window.open(url);
};

function ouvrir_popup_todo(idModal) {
	var modal = document.getElementById(idModal);
	if (modal.style.display == "none" || modal.style.display == "") {
		modal.style.display = "block";
		setTimeout(function() {
			modal.style.display = "none";
		}, 3000);
		// window.alert('Implémentation en cours -- Contacter ??');

	}
};

function affichePage(divPageId) {
	var pages = $("#container1").find("div[for='page']");
	pages.each(function() {
		if (!$(this).hasClass("hidden")) {
			$(this).addClass("hidden");
		}
	});
	var div = $(divPageId);
	if (div.hasClass("hidden")) {
		div.removeClass("hidden");
	} else {
		div.addClass("hidden");
	}
};

function confirmStop() {
	if (confirm("Etes-vous sûr de vouloir ferme l'application ?")) {
		$.get('/cmd?stop', function(code_html, status) {
			window.close();
		}).fail(function(code_html, status) {
			processError(code_html, status);
		});
	} else {
		// Do nothing!
	}
};

function hasError(json, status) {
	return json.success != true;
};

function processError(jsonError, status) {
	if (status == "error") {
		alert("Erreur lors de l'appel serveur . Le serveur est-il toujours en fonctionnement ?");
	} else if (jsonError.responseText != undefined) {
		alert("Erreur [" + status + "]\n" + jsonError.responseText);
	} else {
		alert("Erreur inconnu lors de l'appel serveur [" + status + "]\n");
	}
}

function refreshListIndexExtract() {
	$("#button_extract").prop('disabled', true);
	$('#list_index_extract').empty();
	$.get('info?index=flow-extract-*', function(code_html, status) {

		$.each(code_html.result, function(i, item) {
			$('#list_index_extract').append($('<option>', {
				value : item,
				text : item
			}));
			$("#button_extract").prop('disabled', false);
		});
	}).fail(function(code_html, status) {
		processError(code_html, status);
	});
};

function refreshAvailableMonthsFromGenericExtract() {
	// $("#button_extract").prop('disabled', true);
	$('#list_month_extract').empty();
	// $.get('info?index=flow-extract-generic-*', function(jsonResult, status){
	// TODO
	$.get('info?index=metrics_c1_archive_flow_cad_ido_',
			function(jsonResult, status) {

				$("#button_extract").prop('disabled', false);

				// <input type="radio" id="choice2" name="whatType"
				// value="FLOW_ACM">
				// <label for="choice2">ACM</label>

				$.each(jsonResult.result.months, function(i, item) {
					$('#list_month_extract').append($('<option>', {
						value : item,
						text : item
					}));
				});

				$.each(jsonResult.result.weeks, function(i, item) {
					$('#list_week_extract').append($('<option>', {
						value : item,
						text : item
					}));
				});

			}).fail(function(jsonResult, status) {
		processError(jsonResult, status);
	});
};

function refreshJsonMdgRunExtraction() {
	// $("#button_extract").prop('disabled', true);
	$('#list_month_extract').empty();
	// $.get('info?index=flow-extract-generic-*', function(jsonResult, status){
	// TODO
	$.get('info?analyse',
			function(jsonResult, status) {

				$("#button_extract").prop('disabled', false);

				// <input type="radio" id="choice2" name="whatType"
				// value="FLOW_ACM">
				// <label for="choice2">ACM</label>

				$.each(jsonResult.result.json_data_found, function(i, item) {
					$('#list_mdg_run').append($('<option>', {
						value : item.display_fn,
						text : item.filename
					}));
				});

			}).fail(function(jsonResult, status) {
		processError(jsonResult, status);
	});
};

function generateCsvExport() {
	var filterType = $('input[name=filterType]:checked', '#reportForm').val();
	var filterValue = "";
	var paramFilterType = "";

	var flowName = $('input[name=whatType]:checked', '#reportForm').val();
	var paramFlow = "flowName=" + flowName;

	if (filterType == 'month') {
		filterValue = $('#list_month_extract option:selected').val();
		paramFilterType = "requestedMonth=" + filterValue;
	} else {
		filterValue = $('#list_week_extract option:selected').val();
		paramFilterType = "requestedWeek=" + filterValue;
	}

	// cmd?flowName=r151&requestedMonth=8

	$.get(
			'cmd?extract=1&' + paramFlow + "&" + paramFilterType,
			function(code_html, status) {
				$("#extract_result").html(
						'Génération terminée.<br />Cliquer ici: <a href="'
								+ code_html.result.target + '">'
								+ code_html.result.target + '</a>');
				// $("#button_extract").prop('disabled', false);
			}).fail(function(code_html, status) {
		processError(code_html, status);
		$("#extract_result").html("Erreur durant l'extraction");
		// $("#button_extract").prop('disabled', false);
	});
	// $("#button_extract").prop('disabled', true);
	$("#extract_result").html("Extraction en cours...");
};


function generateMDGRunExport() {
	var filterValue = $('#list_mdg_run option:selected').val();
      
    	$.get('info?display_mdgResult='+filterValue,
			function(jsonResult, status) {

				$("#button_analyse").prop('disabled', false);
				var reportLineSupervisionInfoDatatable = $('#result_analyse').DataTable({
				    "destroy": true,
			        "orderMulti": false,
			        "data": jsonResult.result.mdg_run_info,
			    	"columns": [
						{"data": "flow_documentid_keyword"},
						{"data": "flow_map_key_keyword"},
						{"data": "flow_map_value_keyword"}			
			        ]
			      });
				
	}).fail(function(jsonResult, status) {
		processError(jsonResult, status);
	});  
	$('#result_analyse').show();

};



function extractIndex() {
	// alert($('#list_index_extract').val());

	$
			.get(
					'cmd?extract=' + $('#list_index_extract').val(),
					function(code_html, status) {
						$("#extract_result")
								.html(
										"L'extraction 'index' est terminée.<br />Cliquer ici: <a href=\"javascript:openFile('"
												+ code_html.result.target
														.replace(new RegExp(
																'\\\\', 'g'),
																'\\\\')
												+ "')\">"
												+ code_html.result.target
												+ "</a>");
						// $("#button_extract").prop('disabled', false);
					}).fail(function(code_html, status) {
				processError(code_html, status);
				$("#extract_result").html("Erreur durant l'extraction");
				// $("#button_extract").prop('disabled', false);
			});
	// $("#button_extract").prop('disabled', true);
	$("#extract_result").html("Extraction en cours...");
};

function extractIndexAgg() {
	// alert($('#list_index_extract').val());
	if ($('#list_index_extract_agg').val().length == 0)
		return;

	$
			.get(
					'cmd?extract=' + $('#list_index_extract_agg').val(),
					function(code_html, status) {
						$("#extract_result")
								.html(
										"L'extraction AGG est terminée.<br />Cliquer ici: <a href=\"javascript:openFile('"
												+ code_html.result.target
														.replace(new RegExp(
																'\\\\', 'g'),
																'\\\\')
												+ "')\">"
												+ code_html.result.target
												+ "</a>");
					}).fail(function(code_html, status) {
				processError(code_html, status);
				$("#extract_result").html("Erreur durant l'extraction");
			});
	$("#extract_result").html("Extraction en cours...");
};

function analyseIndexLocal(indexName) {
	//
	// TODO
	//
	$.get('cmd?analyse=' + $('#list_index_analyse').val(),
			function(code_html, status) {
				$("#analyse_result").html("L'analyse est terminée.<br />");
				$("#button_analyse").prop('disabled', false);
			}).fail(function(code_html, status) {
		processError(code_html, status);
		$("#analyse_result").html("Erreur durant l'analyse");
		$("#button_analyse").prop('disabled', false);
	});
	$("#button_analyse").prop('disabled', true);
	$("#analyse_result").html("Analyse en cours...");
};

function openFile(filePath) {
	$.get('cmd?open=' + encodeURI(filePath), function(code_html, status) {
		if (hasError(code_html)) {
			processError(code_html, status);
			return;
		}
	});
}
