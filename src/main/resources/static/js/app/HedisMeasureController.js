(function(){
'use strict';
var app = angular.module('my-app');

app.controller('HedisMeasureController',
    ['HedisMeasureService', 'HedisMeasureGroupService', '$scope', '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( HedisMeasureService,HedisMeasureGroupService,   $scope,$compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.hedisMeasure = {};
        self.hedisMeasures=[];
        self.display =$stateParams.hedisMeasureDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllHedisMeasures = getAllHedisMeasures;
        self.createHedisMeasure = createHedisMeasure;
        self.updateHedisMeasure = updateHedisMeasure;
        self.removeHedisMeasure = removeHedisMeasure;
        self.editHedisMeasure = editHedisMeasure;
        self.addHedisMeasure = addHedisMeasure;
        self.getAllHedisMeasureGroups = getAllHedisMeasureGroups;
        self.dtInstance = {};
		self.hedisMeasureId = null;
		self.hedisGroups = [];
        self.reset = reset;
        self.hedisMeasureEdit = hedisMeasureEdit;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('code').withTitle('Hedis_CODE').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.hedisMeasureEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left").withOption("sWidth",'20%'),
			DTColumnBuilder.newColumn('description').withTitle('DESCRIPTION').withOption("sWidth",'50%'),
            DTColumnBuilder.newColumn('hedisMsrGrp.description').withTitle('HEDIS GROUP').withOption("sWidth",'10%').withOption("sClass",' { max-width: 300px; word-wrap: break-word;  }')
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
			HedisMeasureService
					.loadHedisMeasures(page, length, search.value, sortCol+','+sortDir)
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
        
       function checkBoxChange(checkStatus, hedisMeasureId) {
			self.displayEditButton = checkStatus;
			self.hedisMeasureId = hedisMeasureId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.hedisMeasure.id === undefined || self.hedisMeasure.id === null) {
                console.log('Saving New HedisMeasure', self.hedisMeasure);
                createHedisMeasure(self.hedisMeasure);
            } else {
                updateHedisMeasure(self.hedisMeasure, self.hedisMeasure.id);
                console.log('HedisMeasure updated with id ', self.hedisMeasure.id);
            }
            self.displayEditButton = false;
        }

        function createHedisMeasure(hedisMeasure) {
            console.log('About to create hedisMeasure');
            HedisMeasureService.createHedisMeasure(hedisMeasure)
                .then(
                    function (response) {
                        console.log('HedisMeasure created successfully');
                        self.successMessage = 'HedisMeasure created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.hedisMeasures = getAllHedisMeasures();
                        self.hedisMeasure={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating HedisMeasure');
                        self.errorMessage = 'Error while creating HedisMeasure: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateHedisMeasure(hedisMeasure, id){
            console.log('About to update hedisMeasure'+hedisMeasure);
            HedisMeasureService.updateHedisMeasure(hedisMeasure, id)
                .then(
                    function (response){
                        console.log('HedisMeasure updated successfully');
                        self.successMessage='HedisMeasure updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating HedisMeasure');
                        self.errorMessage='Error while updating HedisMeasure '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeHedisMeasure(id){
            console.log('About to remove HedisMeasure with id '+id);
            HedisMeasureService.removeHedisMeasure(id)
                .then(
                    function(){
                        console.log('HedisMeasure '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing hedisMeasure '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllHedisMeasures(){
             self.hedisMeasures = HedisMeasureService.getAllHedisMeasures();
   
            return self.hedisMeasures;
        }
        
        
        function editHedisMeasure(id) {
            self.successMessage='';
            self.errorMessage='';
            HedisMeasureService.getHedisMeasure(id).then(
                function (hedisMeasure) {
                    self.hedisMeasure = hedisMeasure;
                    self.hedisGroups = getAllHedisMeasureGroups();
                    cancelEdit();
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing hedisMeasure ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function getAllHedisMeasureGroups(){
        	return HedisMeasureGroupService.getAllHedisMeasureGroups();
        }
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.hedisMeasure={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.hedisMeasure={};
            self.display = false;
            $state.go('main.hedis',{},{location: true,reload: false,notify: false});
        }
        
        function hedisMeasureEdit(id) {
        	var params = {'hedisMeasureDisplay':true};
			var trans =  $state.go('main.hedis.edit',params).transition;
			trans.onSuccess({}, function() { editHedisMeasure(id);  }, { priority: -1 });
			
        }
        
        function addHedisMeasure() {
            self.successMessage='';
            self.errorMessage='';
            self.hedisGroups = getAllHedisMeasureGroups();
            self.display =true;
        }
        
    
    }
    

    ]);
   })();