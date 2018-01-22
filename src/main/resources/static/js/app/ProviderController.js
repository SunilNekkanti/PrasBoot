(function(){
'use strict';
var app = angular.module('my-app');

app.controller('ProviderController',
    ['ProviderService', 'LanguageService', 'StateService', 'InsuranceService','RefContractInsuranceService','FileUploadService', '$sce','$scope', '$compile','$state','$stateParams', '$filter' ,'DTOptionsBuilder', 'DTColumnBuilder', function( ProviderService,  LanguageService, StateService,InsuranceService, RefContractInsuranceService, FileUploadService, $sce,$scope,$compile,$state,$stateParams, $filter, DTOptionsBuilder, DTColumnBuilder) {

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
        self.getAllRefContractInsurances = getAllRefContractInsurances;
        self.addInsPrvdrContract = addInsPrvdrContract;
        self.setInsPrvdrContractDates = setInsPrvdrContractDates;
        self.providerEdit = providerEdit;
        self.cancelEdit = cancelEdit;
        self.readUploadedFile = readUploadedFile;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.providers=[];
        self.currentScreen = $state.current.data.currentScreen;
        self.toScreen = $state.current.data.toScreen;
        self.linkToScreen = $state.current.data.linkToScreen;
        self.dtColumns = [
            
            DTColumnBuilder.newColumn('name').withTitle('PROVIDER').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.providerEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
			DTColumnBuilder.newColumn('code').withTitle('NPI').withOption('defaultContent', '')
           /* DTColumnBuilder.newColumn('prvdrInsContracts').renderWith(function(data) {
            	var insurances=[];
            	var keys = Object.keys(data);
            	if(data !== undefined && keys !== null ){
            		
            		for(var i = 0, j = 0; i<keys.length ; i++)
                    {
            			if(keys[i] == null || keys[i].ins  == null ) continue;
            			insurances[j++] = $filter('uppercase') (keys[i].ins.name);
            			
                    }
    					return insurances.join(', ');
            	}
            	return '';
                
			}) .withTitle('Insurance').withOption('defaultContent', '')*/
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
					.loadProviders(page, length, search.value, sortCol+','+sortDir, self.currentScreen)
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
        	 var myFiles = self.prvdr.prvdrRefContracts.map(a => a.myFile );
        	 console.log('myFiles',myFiles);
             if(myFiles.length > 0){
 				var  promise = FileUploadService.uploadContractFileToUrl(myFiles);

 	            promise.then(function (response) {
 	            	
 	            	  self.prvdr.prvdrRefContracts.forEach(function (refContract, idx){
 	            		  
 	            		 response.forEach(function (fileUpload,uploadidx) {
	 	            		 if(fileUpload && self.prvdr.prvdrRefContracts[idx].myFile ){
	  	            	    	self.prvdr.prvdrRefContracts[idx].contract.fileUpload = fileUpload;
	  	            	    	response.splice(uploadidx, 1);
	  	            	     }
 	            	     });
 	            	  });
 	            	  
 	            	
 	            	 if (self.prvdr.id === undefined || self.prvdr.id === null) {
 							console.log('Saving New Provider');
 							createProvider(self.prvdr);
 							
 						} else {
 							updateProvider(self.prvdr, self.prvdr.id)	 
 							console.log('Provider updated with id ',
 									self.prvdr.id);
 						}
 						self.displayEditButton = false;
 						
 	             
 	            }, function () {
 	                self.serverResponse = 'An error has occurred';
 	            });
 			}else{
 				if (self.prvdr.id === undefined || self.prvdr.id === null) {
 					console.log('Saving New Provider');
 					createProvider(self.prvdr);
 					
 				} else {
 					updateProvider(self.prvdr, self.prvdr.id)	 
 					console.log('Provider updated with id ',
 							self.prvdr.id);
 				}
 				self.displayEditButton = false;
 				cancelEdit();
 			} 
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
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating Provider');
                        self.errorMessage = 'Error while creating Provider: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateProvider(prvdr, id){
            console.log('About to update prvdr');
            ProviderService.updateProvider(prvdr, id)
                .then(
                    function (response){
                        console.log('Provider updated successfully');
                        self.successMessage='Provider updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.prvdr = {};
                        self.display =false;
                        cancelEdit();
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
        
        function getAllRefContractInsurances() {
        	return  RefContractInsuranceService.getAllRefContractInsurances();
			 
			 
		}
        
        
        function editProvider(id) {
            self.successMessage='';
            self.errorMessage='';
            ProviderService.getProvider(id).then(
                function (prvdr) {
                    self.prvdr = prvdr;
                    if(!self.prvdr.prvdrRefContracts || self.prvdr.prvdrRefContracts.length ==0){
                    	self.prvdr.prvdrRefContracts = [];
            			self.prvdr.prvdrRefContracts.push({});
                    }
                    
                    self.languages = getAllLanguages();
                    self.states = getAllStates();
                    self.insurances = getAllInsurances();
                    self.refContractInsurances = getAllRefContractInsurances();
                    self.prvdrInsurances  = self.prvdr.prvdrRefContracts.map(refContract => refContract.ins);
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing prvdr ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function providerEdit(id) {
        	var params = {'providerDisplay':true};
			var trans ;
			if(self.currentScreen === 'Active') {
				trans =  $state.go('main.provider.edit',params).transition;
			}else {
			    trans =  $state.go('main.providerArchives.edit',params).transition;
			}
			
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
            if(self.currentScreen === 'Active') {
           		 $state.go('main.provider', {}, {location: true,reload: false,notify: false});
            }else{
            	$state.go('main.providerArchives', {}, {location: true,reload: false,notify: false});
            }
            self.prvdr={};
            self.display = false;
        }
       
        function addProvider() {
            self.successMessage='';
            self.errorMessage='';
            self.prvdr ={}
            self.prvdr.prvdrRefContracts = [];
            self.prvdr.prvdrRefContracts.push({});
            self.languages = getAllLanguages();
            self.states = getAllStates();
            self.insurances = getAllInsurances();
            self.refContractInsurances = getAllRefContractInsurances();
            self.display =true;
        }
        
        function readUploadedFile(index){
			console.log('About to read file');
			FileUploadService.getFileUpload(self.prvdr.prvdrRefContracts[index].contract.fileUpload.id).then(
					function(response) {
						self.errorMessage = '';
						var file = new Blob([response], {type: self.prvdr.prvdrRefContracts[index].contract.fileUpload.contentType});
						var fileURL = URL.createObjectURL(file);
					    self.content = $sce.trustAsResourceUrl(fileURL); 
					    
					},
					function(errResponse) {
						console
								.error('Error while reading file');
						self.errorMessage = 'Error while reading file: '
								+ errResponse.data.errorMessage;
						self.successMessage = '';
					}); 
		}
		
		function addInsPrvdrContract(){
		self.prvdrInsurances  = self.prvdr.prvdrRefContracts.map(refContract => refContract.ins);
		  if(!self.prvdr.prvdrRefContracts || self.prvdr.prvdrRefContracts.length  ===0){
		      self.prvdr.prvdrRefContracts = [];
		  }
		  self.prvdr.prvdrRefContracts.push({contract:{}});
		}
        
        function setInsPrvdrContractDates(index){
               self.prvdr.prvdrRefContracts[index].contract.startDate= self.prvdr.prvdrRefContracts[index].contract.startDate || self.prvdr.prvdrRefContracts[index].ins.contracts[0].startDate;
               self.prvdr.prvdrRefContracts[index].contract.endDate= self.prvdr.prvdrRefContracts[index].contract.endDate || self.prvdr.prvdrRefContracts[index].ins.contracts[0].endDate;
                 
        }
        
        
    }

    ]);
   })();