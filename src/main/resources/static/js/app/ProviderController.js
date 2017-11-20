'use strict';

app.controller('ProviderController',
    ['ProviderService', 'LanguageService', 'StateService', 'InsuranceService','$scope', '$compile','$state','$stateParams', '$filter' ,'DTOptionsBuilder', 'DTColumnBuilder', function( ProviderService,  LanguageService, StateService,InsuranceService, $scope,$compile,$state,$stateParams, $filter, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.prvdr = {};
        self.prvdrs=[];
        self.display =false;
        self.displayEditButton = false;
        self.submit = submit;
        self.languages=[];
        self.states=[];
        self.insurances=[];
        self.getAllProviders = getAllProviders;
        self.createProvider = createProvider;
        self.updateProvider = updateProvider;
        self.removeProvider = removeProvider;
        self.editProvider = editProvider;
        self.addProvider = addProvider;
        self.dtInstance = {};
		self.prvdrId = null;
        self.reset = reset;
        self.getAllLanguages = getAllLanguages;
        self.getAllStates = getAllStates;
        self.getAllInsurances = getAllInsurances;
        self.providerEdit = providerEdit;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            
            DTColumnBuilder.newColumn('name').withTitle('PROVIDER').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.providerEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
			DTColumnBuilder.newColumn('code').withTitle('NPI').withOption('defaultContent', ''),
            DTColumnBuilder.newColumn('refInsContracts').renderWith(function(data) {
            	var insurances=[];
            	if(data !== undefined && data !== null ){
            		for(var i = 0, j = 0; i<data.length ; i++)
                    {
            			if(data[i] == null) continue;
            			insurances[j++] = $filter('uppercase') (data[i].ins.name);
            			
                    }
    					return insurances.join(', ');
            	}
            	return '';
                
			}) .withTitle('Insurance').withOption('defaultContent', '')
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
			ProviderService
					.loadProviders(page, length, search.value, sortCol+','+sortDir)
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
        
       function checkBoxChange(checkStatus, prvdrId) {
			self.displayEditButton = checkStatus;
			self.prvdrId = prvdrId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.prvdr.id === undefined || self.prvdr.id === null) {
                console.log('Saving New Provider', self.prvdr);
                createProvider(self.prvdr);
            } else {
                updateProvider(self.prvdr, self.prvdr.id);
                console.log('Provider updated with id ', self.prvdr.id);
            }
            self.displayEditButton = false;
        }

        function createProvider(prvdr) {
            console.log('About to create prvdr');
            ProviderService.createProvider(prvdr)
                .then(
                    function (response) {
                        console.log('Provider created successfully');
                        self.successMessage = 'Provider created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.prvdrs = getAllProviders();
                        self.prvdr={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating Provider');
                        self.errorMessage = 'Error while creating Provider: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateProvider(prvdr, id){
            console.log('About to update prvdr'+prvdr);
            ProviderService.updateProvider(prvdr, id)
                .then(
                    function (response){
                        console.log('Provider updated successfully');
                        self.successMessage='Provider updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                    },
                    function(errResponse){
                        console.error('Error while updating Provider');
                        self.errorMessage='Error while updating Provider '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeProvider(id){
            console.log('About to remove Provider with id '+id);
            ProviderService.removeProvider(id)
                .then(
                    function(){
                        console.log('Provider '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing prvdr '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllProviders(){
             self.prvdrs = ProviderService.getAllProviders();
   
            return self.prvdrs;
        }

        
        function getAllLanguages(){
        	return  LanguageService.getAllLanguages();
        }
        
        function getAllStates() {
			return StateService.getAllStates();
		}
        
        function getAllInsurances() {
			return InsuranceService.getAllInsurances();
		}
        
        function editProvider(id) {
            self.successMessage='';
            self.errorMessage='';
            ProviderService.getProvider(id).then(
                function (prvdr) {
                    self.prvdr = prvdr;
                    self.languages = getAllLanguages();
                    self.states = getAllStates();
                    self.insurances = getAllInsurances();
                    console.log('self.languages ' +self.languages ) ;
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing prvdr ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function providerEdit(id) {
        	var params = {'providerDisplay':true};
			var trans =  $state.go('provider.edit',params).transition;
			trans.onSuccess({}, function() { editProvider(id);  }, { priority: -1 });
			
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.prvdr={};
            $scope.myForm.$setPristine(); //reset Form
        }
        
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.provider={};
            self.display = false;
            $state.go('provider');
        }
       
        function addProvider() {
            self.successMessage='';
            self.errorMessage='';
            self.languages = getAllLanguages();
            self.states = getAllStates();
            self.insurances = getAllInsurances();
            self.display =true;
        }
        
    
    }
    

    ]);