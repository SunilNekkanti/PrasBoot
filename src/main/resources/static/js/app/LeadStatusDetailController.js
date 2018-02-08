(function(){
'use strict';
var app = angular.module('my-app');

app.controller('LeadStatusDetailController',
    ['LeadStatusDetailService', 'LeadStatusService', '$scope', '$compile','$state','DTOptionsBuilder', 'DTColumnBuilder', function( LeadStatusDetailService, LeadStatusService, $scope,$compile, $state, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.leadStatusDetail = {};
        self.leadStatusDetails=[];
        self.leadStatuses=[];
        self.display =false;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllLeadStatusDetails = getAllLeadStatusDetails;
        self.createLeadStatusDetail = createLeadStatusDetail;
        self.updateLeadStatusDetail = updateLeadStatusDetail;
        self.removeLeadStatusDetail = removeLeadStatusDetail;
        self.editLeadStatusDetail = editLeadStatusDetail;
        self.addLeadStatusDetail = addLeadStatusDetail;
        self.getAllLeadStatuses = getAllLeadStatuses;
        self.dtInstance = {};
		self.leadStatusDetailId = null;
        self.reset = reset;
        self.cancelEdit = cancelEdit;
        self.dtInstance = {};
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.dtInstance = {};
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('description').withTitle('LEAD STATUS DETAIL').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.editLeadStatusDetail('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
			DTColumnBuilder.newColumn('leadStatus.description').withTitle('LEAD STATUS')
          ];
     
        
        self.dtOptions = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
		.withOption('bServerSide', true)
				.withOption("bLengthChange", false)
				.withOption("bPaginate", true)
				.withOption('bProcessing', true)
				.withOption('bSaveState', true)
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
			LeadStatusDetailService
					.loadLeadStatusDetails(page, length, search.value, sortCol+','+sortDir)
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
        
       function checkBoxChange(checkStatus, leadStatusDetailId) {
			self.displayEditButton = checkStatus;
			self.leadStatusDetailId = leadStatusDetailId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.leadStatusDetail.id === undefined || self.leadStatusDetail.id === null) {
                console.log('Saving New LeadStatusDetail', self.leadStatusDetail);
                createLeadStatusDetail(self.leadStatusDetail);
            } else {
                updateLeadStatusDetail(self.leadStatusDetail, self.leadStatusDetail.id);
                console.log('LeadStatusDetail updated with id ', self.leadStatusDetail.id);
            }
            self.displayEditButton = false;
        }

        function createLeadStatusDetail(leadStatusDetail) {
            console.log('About to create leadStatusDetail');
            LeadStatusDetailService.createLeadStatusDetail(leadStatusDetail)
                .then(
                    function (response) {
                        console.log('LeadStatusDetail created successfully');
                        self.successMessage = 'LeadStatusDetail created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        console.log(self.languages);
                        self.leadStatusDetail={};
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function (errResponse) {
                        console.error('Error while creating LeadStatusDetail');
                        self.errorMessage = 'Error while creating LeadStatusDetail: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateLeadStatusDetail(leadStatusDetail, id){
            console.log('About to update leadStatusDetail'+leadStatusDetail);
            LeadStatusDetailService.updateLeadStatusDetail(leadStatusDetail, id)
                .then(
                    function (response){
                        console.log('LeadStatusDetail updated successfully');
                        self.successMessage='LeadStatusDetail updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function(errResponse){
                        console.error('Error while updating LeadStatusDetail');
                        self.errorMessage='Error while updating LeadStatusDetail '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeLeadStatusDetail(id){
            console.log('About to remove LeadStatusDetail with id '+id);
            LeadStatusDetailService.removeLeadStatusDetail(id)
                .then(
                    function(){
                        console.log('LeadStatusDetail '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing leadStatusDetail '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllLeadStatusDetails(){
             self.leadStatusDetails = LeadStatusDetailService.getAllLeadStatusDetails();
   
            return self.leadStatusDetails;
        }

        function editLeadStatusDetail(id) {
            self.successMessage='';
            self.errorMessage='';
            LeadStatusDetailService.getLeadStatusDetail(id).then(
                function (leadStatusDetail) {
                    self.leadStatusDetail = leadStatusDetail;
                    self.leadStatuses = getAllLeadStatuses();
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing leadStatusDetail ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.leadStatusDetail={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.leadStatusDetail={};
            self.display = false;
            $state.go('main.leadStatusDetail', {}, {reload: true});
        }
        
        function addLeadStatusDetail() {
            self.successMessage='';
            self.errorMessage='';
            self.leadStatuses = getAllLeadStatuses();
            self.display =true;
        }
       
        function getAllLeadStatuses(){
        	return LeadStatusService.getAllLeadStatuses();
        }


        }
    ]);
   })();