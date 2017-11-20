'use strict';

app.controller('InsuranceController',
    ['InsuranceService','PlanTypeService', 'StateService','$scope', '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( InsuranceService,  PlanTypeService,  StateService, $scope,$compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.insurance = {};
        self.insurances=[];
        self.display =$stateParams.insuranceDisplay||false;;
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
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
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
					.loadInsurances(page, length, search.value, sortCol+','+sortDir)
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
            if (self.insurance.id === undefined || self.insurance.id === null) {
                console.log('Saving New Insurance', self.insurance);
                createInsurance(self.insurance);
            } else {
                updateInsurance(self.insurance, self.insurance.id);
                console.log('Insurance updated with id ', self.insurance.id);
            }
            self.displayEditButton = false;
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
                        $scope.myForm.$setPristine();
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
                        $state.go("insurance");
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
             self.insurances = InsuranceService.getAllInsurances();
   
            return self.insurances;
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
            $state.go('insurance');
        }
        
        function insuranceEdit(id) {
        	var params = {'insuranceDisplay':true};
			var trans =  $state.go('insurance.edit',params).transition;
			trans.onSuccess({}, function() { editInsurance(id);  }, { priority: -1 });
			
        }
        
        function addInsurance() {
            self.successMessage='';
            self.errorMessage='';
            self.planTypes = getAllPlanTypes();
            self.states = getAllStates();
            self.display =true;
        }
        
    
    }
    

    ]);