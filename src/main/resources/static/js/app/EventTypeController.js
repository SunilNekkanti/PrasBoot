(function(){
'use strict';
var app = angular.module('my-app');

app.controller('EventTypeController',
    ['EventTypeService', '$scope', '$compile','$state','DTOptionsBuilder', 'DTColumnBuilder',  function( EventTypeService, $scope, $compile, $state, DTOptionsBuilder, DTColumnBuilder) {


        var self = this;
        self.eventType = {};
        self.eventTypes=[];
        self.display =false;
        self.displayEditButton = false;
        self.submit = submit;
        self.addEventType = addEventType;
        self.getAllEventTypes = getAllEventTypes;
        self.createEventType = createEventType;
        self.updateEventType = updateEventType;
        self.removeEventType = removeEventType;
        self.editEventType = editEventType;
        self.reset = reset;
        self.eventTypeId = null;
        self.cancelEdit = cancelEdit;
        self.dtInstance = {};
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.dtColumns = [
            DTColumnBuilder.newColumn('description').withTitle('FACILITY TYPE').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.editEventType('+full.id+')">'+data+'</a>';
					}).withClass("text-left")
          ];


        self.dtOptions = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
		.withOption('bServerSide', true)
				.withOption("bLengthChange", false)
				.withOption("bPaginate", true)
				.withOption('bProcessing', true)
				.withOption('bStateSave', true)
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
			EventTypeService
					.loadEventTypes(page, length, search.value, sortCol+','+sortDir)
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
			self.dtInstance.rerender();
		}


		function callback(json) {
				console.log(json);
		}


       function createdRow(row, data, dataIndex) {
            // Recompiling so we can bind Angular directive to the DT
            $compile(angular.element(row).contents())($scope);
        }

       function checkBoxChange(checkStatus, eventTypeId) {
			self.displayEditButton = checkStatus;
			self.eventTypeId = eventTypeId

		}

        function submit() {
            console.log('Submitting');
            if (self.eventType.id === undefined || self.eventType.id === null) {
                console.log('Saving New EventType', self.eventType);
                createEventType(self.eventType);
            } else {
                updateEventType(self.eventType, self.eventType.id);
                console.log('EventType updated with id ', self.eventType.id);
            }
            self.displayEditButton = false;
        }

        function createEventType(eventType) {
            console.log('About to create eventType');
            EventTypeService.createEventType(eventType)
                .then(
                    function (response) {
                        console.log('EventType created successfully');
                        self.successMessage = 'EventType created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.eventType={};
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function (errResponse) {
                        console.error('Error while creating EventType');
                        self.errorMessage = 'Error while creating EventType: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateEventType(eventType, id){
            console.log('About to update eventType');
            EventTypeService.updateEventType(eventType, id)
                .then(
                    function (response){
                        console.log('EventType updated successfully');
                        self.successMessage='EventType updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function(errResponse){
                        console.error('Error while updating EventType');
                        self.errorMessage='Error while updating EventType '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeEventType(id){
            console.log('About to remove EventType with id '+id);
            EventTypeService.removeEventType(id)
                .then(
                    function(){
                        console.log('EventType '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing eventType '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllEventTypes(){
            return EventTypeService.getAllEventTypes();
        }

        function editEventType(id) {
            self.successMessage='';
            self.errorMessage='';
            EventTypeService.getEventType(id).then(
                function (eventType) {
                    self.eventType = eventType;
                    self.display =true;
                },
                function (errResponse) {
                    console.error('Error while removing eventType ' + id + ', Error :' + errResponse.data);
                }
            );
        }

        function addEventType() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }


        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.eventType={};
            $scope.myForm.$setPristine(); //reset Form
        }

        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.eventType={};
            self.display = false;
            $state.go('main.eventType');
        }
 
 
     }
    ]);
   })();
