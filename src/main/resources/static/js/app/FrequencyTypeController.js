(function(){
'use strict';
var app = angular.module('my-app');

app.controller('FrequencyTypeController',
    ['FrequencyTypeService','$scope', '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( FrequencyTypeService,  $scope,$compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.frequencyType = {};
        self.frequencyTypes=[];
        self.display =$stateParams.frequencyTypeDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllFrequencyTypes = getAllFrequencyTypes;
        self.createFrequencyType = createFrequencyType;
        self.updateFrequencyType = updateFrequencyType;
        self.removeFrequencyType = removeFrequencyType;
        self.editFrequencyType = editFrequencyType;
        self.addFrequencyType = addFrequencyType;
        self.dtInstance = {};
		self.frequencyTypeId = null;
        self.reset = reset;
        self.frequencyTypeEdit = frequencyTypeEdit;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('description').withTitle('DESCRIPTION').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.frequencyTypeEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
            DTColumnBuilder.newColumn('shortName').withTitle('NAME'),
            DTColumnBuilder.newColumn('numberOfDays').withTitle('NO_OF_DAYS')
          ];
     
        
        self.dtOptions = DTOptionsBuilder.newOptions()
		 .withDisplayLength(20)
		.withOption('bServerSide', true)
		.withOption('responsive', true)
		.withOption("bLengthChange", false)
		.withOption("bPaginate", true)
		.withOption('bProcessing', true)
		.withOption('stateSave', true)
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
			FrequencyTypeService
					.loadFrequencyTypes(page, length, search.value, sortCol+','+sortDir)
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
        
       function checkBoxChange(checkStatus, frequencyTypeId) {
			self.displayEditButton = checkStatus;
			self.frequencyTypeId = frequencyTypeId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.frequencyType.id === undefined || self.frequencyType.id === null) {
                console.log('Saving New FrequencyType', self.frequencyType);
                createFrequencyType(self.frequencyType);
            } else {
                updateFrequencyType(self.frequencyType, self.frequencyType.id);
                console.log('FrequencyType updated with id ', self.frequencyType.id);
            }
            self.displayEditButton = false;
        }

        function createFrequencyType(frequencyType) {
            console.log('About to create frequencyType');
            FrequencyTypeService.createFrequencyType(frequencyType)
                .then(
                    function (response) {
                        console.log('FrequencyType created successfully');
                        self.successMessage = 'FrequencyType created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.frequencyTypes = getAllFrequencyTypes();
                        self.frequencyType={};
                        $scope.myForm.$setPristine();
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating FrequencyType');
                        self.errorMessage = 'Error while creating FrequencyType: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateFrequencyType(frequencyType, id){
            console.log('About to update frequencyType'+frequencyType);
            FrequencyTypeService.updateFrequencyType(frequencyType, id)
                .then(
                    function (response){
                        console.log('FrequencyType updated successfully');
                        self.successMessage='FrequencyType updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating FrequencyType');
                        self.errorMessage='Error while updating FrequencyType '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeFrequencyType(id){
            console.log('About to remove FrequencyType with id '+id);
            FrequencyTypeService.removeFrequencyType(id)
                .then(
                    function(){
                        console.log('FrequencyType '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing frequencyType '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllFrequencyTypes(){
             self.frequencyTypes = FrequencyTypeService.getAllFrequencyTypes();
   
            return self.frequencyTypes;
        }
        
        
        
        function editFrequencyType(id) {
            self.successMessage='';
            self.errorMessage='';
            FrequencyTypeService.getFrequencyType(id).then(
                function (frequencyType) {
                    self.frequencyType = frequencyType;
                   
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing frequencyType ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.frequencyType={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.frequencyType={};
            self.display = false;
            $state.go('main.frequencyType', {}, {location: true,reload: false,notify: false});
        }
        
        function frequencyTypeEdit(id) {
        	var params = {'frequencyTypeDisplay':true};
			var trans =  $state.go('main.frequencyType.edit',params).transition;
			trans.onSuccess({}, function() { editFrequencyType(id);  }, { priority: -1 });
			
        }
        
        function addFrequencyType() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
    
    }

    ]);
   })();