(function(){
'use strict';
var app = angular.module('my-app');

app.controller('PlanTypeController',
    ['PlanTypeService','$scope', '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( PlanTypeService,  $scope,$compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.planType = {};
        self.planTypes=[];
        self.display =$stateParams.planTypeDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllPlanTypes = getAllPlanTypes;
        self.createPlanType = createPlanType;
        self.updatePlanType = updatePlanType;
        self.removePlanType = removePlanType;
        self.editPlanType = editPlanType;
        self.addPlanType = addPlanType;
        self.dtInstance = {};
		self.planTypeId = null;
        self.reset = reset;
        self.planTypeEdit = planTypeEdit;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('code').withTitle('code').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.planTypeEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
            DTColumnBuilder.newColumn('description').withTitle('Description') 
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
			PlanTypeService.loadPlanTypes(page, length, search.value, sortCol+','+sortDir)
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
        
       function checkBoxChange(checkStatus, planTypeId) {
			self.displayEditButton = checkStatus;
			self.planTypeId = planTypeId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.planType.id === undefined || self.planType.id === null) {
                console.log('Saving New PlanType', self.planType);
                createPlanType(self.planType);
            } else {
                updatePlanType(self.planType, self.planType.id);
                console.log('PlanType updated with id ', self.planType.id);
            }
            self.displayEditButton = false;
        }

        function createPlanType(planType) {
            console.log('About to create planType');
            PlanTypeService.createPlanType(planType)
                .then(
                    function (response) {
                        console.log('PlanType created successfully');
                        self.successMessage = 'PlanType created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.planTypes = getAllPlanTypes();
                        self.planType={};
                        $scope.myForm.$setPristine();
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating PlanType');
                        self.errorMessage = 'Error while creating PlanType: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updatePlanType(planType, id){
            console.log('About to update planType'+planType);
            PlanTypeService.updatePlanType(planType, id)
                .then(
                    function (response){
                        console.log('PlanType updated successfully');
                        self.successMessage='PlanType updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating PlanType');
                        self.errorMessage='Error while updating PlanType '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removePlanType(id){
            console.log('About to remove PlanType with id '+id);
            PlanTypeService.removePlanType(id)
                .then(
                    function(){
                        console.log('PlanType '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing planType '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllPlanTypes(){
             self.planTypes = PlanTypeService.getAllPlanTypes();
            return self.planTypes;
        }
        
        
        
        function editPlanType(id) {
            self.successMessage='';
            self.errorMessage='';
            PlanTypeService.getPlanType(id).then(
                function (planType) {
                    self.planType = planType;
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing planType ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.planType={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.planType={};
            self.display = false;
            $state.go('main.planType',{},{location: true,reload: false,notify: false});
        }
        
        function planTypeEdit(id) {
        	var params = {'planTypeDisplay':true};
			var trans =  $state.go('main.planType.edit',params).transition;
			trans.onSuccess({}, function() { editPlanType(id);  }, { priority: -1 });
			
        }
        
        function addPlanType() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
    
    }
    

    ]);
})();