(function(){
'use strict';
var app = angular.module('my-app');

app.controller('ICDMeasureController',
    ['ICDMeasureService','$scope', '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( ICDMeasureService,   $scope,$compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.icdMeasure = {};
        self.icdMeasures=[];
        self.display =$stateParams.icdMeasureDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllICDMeasures = getAllICDMeasures;
        self.createICDMeasure = createICDMeasure;
        self.updateICDMeasure = updateICDMeasure;
        self.removeICDMeasure = removeICDMeasure;
        self.editICDMeasure = editICDMeasure;
        self.addICDMeasure = addICDMeasure;
        self.dtInstance = {};
		self.icdMeasureId = null;
        self.reset = reset;
        self.icdMeasureEdit = icdMeasureEdit;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('code').withTitle('ICD_CODE').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.icdMeasureEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left").withOption("sWidth",'20%'),
			DTColumnBuilder.newColumn('description').withTitle('DESCRIPTION').withOption("sWidth",'50%'),
            DTColumnBuilder.newColumn('hcc').withTitle('HCC').withOption("sWidth",'10%').withOption("sClass",' { max-width: 300px; word-wrap: break-word;  }'),
            DTColumnBuilder.newColumn('rxhcc').withTitle('RxHCC').withOption("sWidth",'10%'),
          ];
     
        
        self.dtOptions = DTOptionsBuilder.newOptions()
		 .withDisplayLength(20)
		.withOption('bServerSide', true)
		.withOption('responsive', true)
		.withOption("bLengthChange", false)
		.withOption("bPaginate", true)
		.withOption('bProcessing', true)
		.withOption('bSaveState', true)
		.withOption('searchDelay', 3000)
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
			ICDMeasureService
					.loadICDMeasures(page, length, search.value, sortCol+','+sortDir)
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
        
       function checkBoxChange(checkStatus, icdMeasureId) {
			self.displayEditButton = checkStatus;
			self.icdMeasureId = icdMeasureId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.icdMeasure.id === undefined || self.icdMeasure.id === null) {
                console.log('Saving New ICDMeasure', self.icdMeasure);
                createICDMeasure(self.icdMeasure);
            } else {
                updateICDMeasure(self.icdMeasure, self.icdMeasure.id);
                console.log('ICDMeasure updated with id ', self.icdMeasure.id);
            }
            self.displayEditButton = false;
        }

        function createICDMeasure(icdMeasure) {
            console.log('About to create icdMeasure');
            ICDMeasureService.createICDMeasure(icdMeasure)
                .then(
                    function (response) {
                        console.log('ICDMeasure created successfully');
                        self.successMessage = 'ICDMeasure created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.icdMeasures = getAllICDMeasures();
                        self.icdMeasure={};
                        $scope.myForm.$setPristine();
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating ICDMeasure');
                        self.errorMessage = 'Error while creating ICDMeasure: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateICDMeasure(icdMeasure, id){
            console.log('About to update icdMeasure'+icdMeasure);
            ICDMeasureService.updateICDMeasure(icdMeasure, id)
                .then(
                    function (response){
                        console.log('ICDMeasure updated successfully');
                        self.successMessage='ICDMeasure updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating ICDMeasure');
                        self.errorMessage='Error while updating ICDMeasure '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeICDMeasure(id){
            console.log('About to remove ICDMeasure with id '+id);
            ICDMeasureService.removeICDMeasure(id)
                .then(
                    function(){
                        console.log('ICDMeasure '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing icdMeasure '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllICDMeasures(){
             self.icdMeasures = ICDMeasureService.getAllICDMeasures();
   
            return self.icdMeasures;
        }
        
        
        function editICDMeasure(id) {
            self.successMessage='';
            self.errorMessage='';
            ICDMeasureService.getICDMeasure(id).then(
                function (icdMeasure) {
                    self.icdMeasure = icdMeasure;
                   
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing icdMeasure ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.icdMeasure={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.icdMeasure={};
            self.display = false;
            $state.transitionTo('main.icd', {}, {location: true,reload: false,notify: false});
            $state.go('main.icd', {}, {reload: false}); 
        }
        
        function icdMeasureEdit(id) {
        	var params = {'icdMeasureDisplay':true};
			var trans =  $state.go('main.icd.edit',params).transition;
			trans.onSuccess({}, function() { editICDMeasure(id);  }, { priority: -1 });
			
        }
        
        function addICDMeasure() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
    
    }
    

    ]);
   })();