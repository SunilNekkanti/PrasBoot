(function(){
'use strict';
var app = angular.module('my-app');

app.controller('LanguageController',
    [ 'LanguageService',  '$scope', '$compile','DTOptionsBuilder', 'DTColumnBuilder', function(   LanguageService, $scope,$compile,  DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.language = {};
        self.languages=[];
        self.display =false;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllLanguages = getAllLanguages;
        self.createLanguage = createLanguage;
        self.updateLanguage = updateLanguage;
        self.removeLanguage = removeLanguage;
        self.editLanguage = editLanguage;
        self.addLanguage = addLanguage;
        self.dtInstance = {};
		self.languageId = null;
        self.reset = reset;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.dtInstance = {};
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            
			DTColumnBuilder.newColumn('id')
			.withTitle('ACTION').renderWith(
					function(data, type, full,
							meta) {
						return '<input type="checkbox" ng-model="ctrl.language['
								+ data
								+ '].checkbox_status" ng-checked="ctrl.language['
								+ data
								+ '].selected" ng-true-value="true" ng-false-value="false" ng-change="ctrl.checkBoxChange(ctrl.language['
								+ data
								+ '].checkbox_status,'
								+ data
								+ ')" />';
					}).withClass("text-center"),
            DTColumnBuilder.newColumn('description').withTitle('LANGUAGE')
          ];
     
        
        self.dtOptions = DTOptionsBuilder.newOptions()
		.withOption(
				'ajax',
				{
					url : '/LeadManagement/api/language/',
					type : 'GET'
				}).withDataProp('data').withOption('bServerSide', true)
				.withOption("bLengthChange", false)
				.withOption("bPaginate", true)
				.withOption('bProcessing', true)
				.withOption('bSaveState', true)
		        .withDisplayLength(20).withOption( 'columnDefs', [ {
					                                orderable : false,
													className : 'select-checkbox',
													targets : 0,
													sortable : false,
													aTargets : [ 0, 1 ] } ])
				.withOption('select', {
										style : 'os',
										selector : 'td:first-child' })
			    .withOption('createdRow', createdRow)
		        .withPaginationType('full_numbers')
		        
		        .withFnServerData(serverData);

    	function serverData(sSource, aoData, fnCallback) {
			
			
			// All the parameters you need is in the aoData
			// variable
			var order = aoData[2].value;
			var page = aoData[3].value / aoData[4].value;
			var length = aoData[4].value;
			var search = aoData[5].value;

			// Then just call your service to get the
			// records from server side
			LanguageService
					.loadLanguages(page, length, search.value, order)
					.then(
							function(result) {
								var records = {
									'recordsTotal' : result.data.totalElements,
									'recordsFiltered' : result.data.totalElements,
									'data' : result.data.content
								};
								fnCallback(records);
							});
		}

		 function reloadData() {
			var resetPaging = false;
			self.dtInstance.reloadData(callback,
					resetPaging);
		} 
		 
       function createdRow(row, data, dataIndex) {
            // Recompiling so we can bind Angular directive to the DT
            $compile(angular.element(row).contents())($scope);
        }
        
       function checkBoxChange(checkStatus, languageId) {
			self.displayEditButton = checkStatus;
			self.languageId = languageId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.language.id === undefined || self.language.id === null) {
                console.log('Saving New Language', self.language);
                createLanguage(self.language);
            } else {
                updateLanguage(self.language, self.language.id);
                console.log('Language updated with id ', self.language.id);
            }
            self.displayEditButton = false;
        }

        function createLanguage(language) {
            console.log('About to create language');
            LanguageService.createLanguage(language)
                .then(
                    function (response) {
                        console.log('Language created successfully');
                        self.successMessage = 'Language created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.languages = getAllLanguages();
                        console.log(self.languages);
                        self.language={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating Language');
                        self.errorMessage = 'Error while creating Language: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateLanguage(language, id){
            console.log('About to update language'+language);
            LanguageService.updateLanguage(language, id)
                .then(
                    function (response){
                        console.log('Language updated successfully');
                        self.successMessage='Language updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                    },
                    function(errResponse){
                        console.error('Error while updating Language');
                        self.errorMessage='Error while updating Language '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeLanguage(id){
            console.log('About to remove Language with id '+id);
            LanguageService.removeLanguage(id)
                .then(
                    function(){
                        console.log('Language '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing language '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllLanguages(){
             self.languages = LanguageService.getAllLanguages();
   
            return self.languages;
        }

        
        function editLanguage(id) {
            self.successMessage='';
            self.errorMessage='';
            LanguageService.getLanguage(id).then(
                function (language) {
                    self.language = language;
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing language ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.language={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function addLanguage() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
    
        }
    ]);
   })();