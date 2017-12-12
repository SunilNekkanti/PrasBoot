(function(){
'use strict';
var app = angular.module('my-app');

app.controller('PlaceOfServiceController',
    ['PlaceOfServiceService','$scope', '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( PlaceOfServiceService,  $scope,$compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.placeOfService = {};
        self.placeOfServices=[];
        self.display =$stateParams.placeOfServiceDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllPlaceOfServices = getAllPlaceOfServices;
        self.createPlaceOfService = createPlaceOfService;
        self.updatePlaceOfService = updatePlaceOfService;
        self.removePlaceOfService = removePlaceOfService;
        self.editPlaceOfService = editPlaceOfService;
        self.addPlaceOfService = addPlaceOfService;
        self.dtInstance = {};
		self.placeOfServiceId = null;
        self.reset = reset;
        self.placeOfServiceEdit = placeOfServiceEdit;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('code').withTitle('CODE').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.placeOfServiceEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
            DTColumnBuilder.newColumn('name').withTitle('NAME'),
            DTColumnBuilder.newColumn('description').withTitle('DESCRIPTION')
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
			PlaceOfServiceService
					.loadPlaceOfServices(page, length, search.value, sortCol+','+sortDir)
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
        
       function checkBoxChange(checkStatus, placeOfServiceId) {
			self.displayEditButton = checkStatus;
			self.placeOfServiceId = placeOfServiceId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.placeOfService.id === undefined || self.placeOfService.id === null) {
                console.log('Saving New PlaceOfService', self.placeOfService);
                createPlaceOfService(self.placeOfService);
            } else {
                updatePlaceOfService(self.placeOfService, self.placeOfService.id);
                console.log('PlaceOfService updated with id ', self.placeOfService.id);
            }
            self.displayEditButton = false;
        }

        function createPlaceOfService(placeOfService) {
            console.log('About to create placeOfService');
            PlaceOfServiceService.createPlaceOfService(placeOfService)
                .then(
                    function (response) {
                        console.log('PlaceOfService created successfully');
                        self.successMessage = 'PlaceOfService created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.placeOfServices = getAllPlaceOfServices();
                        self.placeOfService={};
                        $scope.myForm.$setPristine();
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating PlaceOfService');
                        self.errorMessage = 'Error while creating PlaceOfService: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updatePlaceOfService(placeOfService, id){
            console.log('About to update placeOfService'+placeOfService);
            PlaceOfServiceService.updatePlaceOfService(placeOfService, id)
                .then(
                    function (response){
                        console.log('PlaceOfService updated successfully');
                        self.successMessage='PlaceOfService updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating PlaceOfService');
                        self.errorMessage='Error while updating PlaceOfService '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removePlaceOfService(id){
            console.log('About to remove PlaceOfService with id '+id);
            PlaceOfServiceService.removePlaceOfService(id)
                .then(
                    function(){
                        console.log('PlaceOfService '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing placeOfService '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllPlaceOfServices(){
             self.placeOfServices = PlaceOfServiceService.getAllPlaceOfServices();
   
            return self.placeOfServices;
        }
        
        
        
        function editPlaceOfService(id) {
            self.successMessage='';
            self.errorMessage='';
            PlaceOfServiceService.getPlaceOfService(id).then(
                function (placeOfService) {
                    self.placeOfService = placeOfService;
                   
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing placeOfService ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.placeOfService={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.placeOfService={};
            self.display = false;
            $state.go('main.roomType', {}, {location: true,reload: false,notify: false});
        }
        
        function placeOfServiceEdit(id) {
        	var params = {'placeOfServiceDisplay':true};
			var trans =  $state.go('main.roomType.edit',params).transition;
			trans.onSuccess({}, function() { editPlaceOfService(id);  }, { priority: -1 });
			
        }
        
        function addPlaceOfService() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
    
    }
    

    ]);
   })();