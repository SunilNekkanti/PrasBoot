(function(){
'use strict';
var app = angular.module('my-app');

app.controller('InsuranceController',
    ['InsuranceService','PlanTypeService', 'StateService','FileUploadService', '$sce', '$scope',  '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( InsuranceService,  PlanTypeService,  StateService, FileUploadService, $sce, $scope, $compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.insurance = {};
        self.insurances=[];
        self.display =$stateParams.insuranceDisplay||false;
        self.displayEditButton = false;
        self.submit = submit;
        self.planTypes=[];
        self.states=[];
        self.getAllInsurances = getAllInsurances;
        self.createInsurance = createInsurance;
        self.updateInsurance = updateInsurance;
        self.removeInsurance = removeInsurance;
        self.editInsurance = editInsurance;
        self.addInsurance = addInsurance;
        self.getAllPlanTypes = getAllPlanTypes;
        self.getAllStates = getAllStates;
        self.dtInstance = {};
		self.insuranceId = null;
        self.reset = reset;
        self.insuranceEdit = insuranceEdit;
        self.cancelEdit = cancelEdit;
        self.readUploadedFile = readUploadedFile;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.currentScreen = $state.current.data.currentScreen;
        self.toScreen = $state.current.data.toScreen;
        self.linkToScreen = $state.current.data.linkToScreen;
                
        self.dtColumns = [
            DTColumnBuilder.newColumn('name').withTitle('INSURANCE').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.insuranceEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
            DTColumnBuilder.newColumn('planType.description').withTitle('PLAN TYPE')
          ];
     
        
        self.dtOptions = DTOptionsBuilder.newOptions()
		 .withDisplayLength(20)
		.withOption('bServerSide', true)
		.withOption('responsive', true)
		.withOption("bLengthChange", false)
		.withOption("bPaginate", true)
		.withOption('bProcessing', true)
		.withOption('bSaveState', true)
		.withOption('searchDelay', 1000)
	    .withOption('createdRow', createdRow)
        .withPaginationType('full_numbers')
        .withOption('ordering', true)
		.withOption('order', [[0,'ASC']])
		.withOption('aLengthMenu', [[15, 20, -1],[ 15, 20, "All"]])
		.withOption('bDeferRender', true)
		.withFnServerData(serverData);

    	function serverData(sSource, aoData, fnCallback) {
			
			
			// All the parameters you need is in the aoData
			// variable
			var order = aoData[2].value;
			var page = aoData[3].value / aoData[4].value ;
			var length = aoData[4].value ;
			var search = aoData[5].value;

			var paramMap = {};
			for ( var i = 0; i < aoData.length; i++) {
			  paramMap[aoData[i].name] = aoData[i].value;
			}
			
			var sortCol ='';
			var sortDir ='';
			// extract sort information
			 if(paramMap['columns'] !== undefined && paramMap['columns'] !== null && paramMap['order'] !== undefined && paramMap['order'] !== null ){
				 sortCol = paramMap['columns'][paramMap['order'][0]['column']].data;
				  sortDir = paramMap['order'][0]['dir'];
			 }
			 
			// Then just call your service to get the
			// records from server side
			InsuranceService
					.loadInsurances(page, length, search.value, sortCol+','+sortDir, self.currentScreen)
					.then(
							function(result) {
								var records = {
									'recordsTotal' : result.data.totalElements||0,
									'recordsFiltered' : result.data.totalElements||0,
									'data' : result.data.content||{}
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
        
       function checkBoxChange(checkStatus, insuranceId) {
			self.displayEditButton = checkStatus;
			self.insuranceId = insuranceId

		}
       
       
        function submit() {
            console.log('Submitting');
            if(self.myFile){
				var  promise = FileUploadService.uploadContractFileToUrl(self.myFile);

	            promise.then(function (response) {
	            	if(!self.insurance.contracts[0].fileUpload){
	            		self.insurance.contract[0].fileUpload = {};
	            	}
	            	 if(response.length >0 )
	                self.insurance.contracts[0].fileUpload= response[0];
	            	 if (self.insurance.id === undefined || self.insurance.id === null) {
							console.log('Saving New Insurance');
							createInsurance(self.insurance);
							
						} else {
							updateInsurance(self.insurance, self.insurance.id)	 
							console.log('Insurance updated with id ',
									self.insurance.id);
						}
						self.displayEditButton = false;
	                
	            }, function () {
	                self.serverResponse = 'An error has occurred';
	            });
			}else{
				if (self.insurance.id === undefined || self.insurance.id === null) {
					console.log('Saving New Insurance');
					createInsurance(self.insurance);
					
				} else {
					updateInsurance(self.insurance, self.insurance.id)	 
					console.log('Insurance updated with id ',
							self.insurance.id);
				}
				self.displayEditButton = false;
			}
        }

        function createInsurance(insurance) {
            console.log('About to create insurance');
            InsuranceService.createInsurance(insurance)
                .then(
                    function (response) {
                        console.log('Insurance created successfully');
                        self.successMessage = 'Insurance created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.insurances = getAllInsurances();
                        self.insurance={};
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating Insurance');
                        self.errorMessage = 'Error while creating Insurance: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateInsurance(insurance, id){
            console.log('About to update insurance'+insurance);
            InsuranceService.updateInsurance(insurance, id)
                .then(
                    function (response){
                        console.log('Insurance updated successfully');
                        self.successMessage='Insurance updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating Insurance');
                        self.errorMessage='Error while updating Insurance '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeInsurance(id){
            console.log('About to remove Insurance with id '+id);
            InsuranceService.removeInsurance(id)
                .then(
                    function(){
                        console.log('Insurance '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing insurance '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllInsurances(){
        	return  InsuranceService.getAllInsurances();
        }
        
        
        function getAllPlanTypes(){
            self.planTypes = PlanTypeService.getAllPlanTypes();
  
           return self.planTypes;
       }
       
        function getAllStates() {
			return StateService.getAllStates();
		}
        
        function editInsurance(id) {
            self.successMessage='';
            self.errorMessage='';
            self.planTypes = getAllPlanTypes();
            self.states = getAllStates();
            InsuranceService.getInsurance(id).then(
                function (insurance) {
                    self.insurance = insurance;
                    if(!self.insurance.contracts || self.insurance.contracts.length == 0){
                       self.insurance.contracts = [];
	                   self.insurance.contracts.push({});
                    }
                    
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing insurance ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.insurance={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.insurance={};
            self.display = false;
            if(self.currentScreen === 'Active') {
           		 $state.go('main.insurance', {}, {location: true,reload: false,notify: false});
            }else{
            	$state.go('main.insuranceArchives', {}, {location: true,reload: false,notify: false});
            }
            
        }
        
        function insuranceEdit(id) {
        	var params = {'insuranceDisplay':true};
        	var trans;
        	if(self.currentScreen === 'Active') {
				trans =  $state.go('main.insurance.edit',params).transition;
			}else {
			    trans =  $state.go('main.insuranceArchives.edit',params).transition;
			}
			trans.onSuccess({}, function() { editInsurance(id);  }, { priority: -1 });
			
        }
        
        function addInsurance() {
        	var params = {'insuranceDisplay':true};
			var trans =  $state.go('main.insurance.edit',params).transition;
			trans.onSuccess({}, function() { 
				self.successMessage='';
	            self.errorMessage='';
	            self.insurance = {};
	            self.insurance.contracts = [];
	            self.insurance.contracts.push({});
	            self.planTypes = getAllPlanTypes();
	            self.states = getAllStates();
	            self.display =true; 
				}, { priority: -1 });
            
        }
        
        function readUploadedFile(){
			console.log('About to read consignment form');
			FileUploadService.getFileUpload(self.insurance.contracts[0].fileUpload.id).then(
					function(response) {
						self.errorMessage = '';
						var file = new Blob([response], {type: self.insurance.contracts[0].fileUpload.contentType});
						 var fileURL = URL.createObjectURL(file);
					    self.content = $sce.trustAsResourceUrl(fileURL); 
					    
					},
					function(errResponse) {
						console
								.error('Error while reading consignment form');
						self.errorMessage = 'Error while reading consignment form: '
								+ errResponse.data.errorMessage;
						self.successMessage = '';
					}); 
		}
    
    }
    

    ]);
   })();