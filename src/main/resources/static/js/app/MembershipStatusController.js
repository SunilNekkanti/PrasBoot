(function(){
'use strict';
var app = angular.module('my-app');
app.controller('MembershipStatusController',
    ['MembershipStatusService','$scope', '$compile','$state','DTOptionsBuilder', 'DTColumnBuilder', function( MembershipStatusService,  $scope,$compile, $state, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.membershipStatus = {};
        self.membershipStatuses=[];
        self.display =false;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllMembershipStatuses = getAllMembershipStatuses;
        self.createMembershipStatus = createMembershipStatus;
        self.updateMembershipStatus = updateMembershipStatus;
        self.removeMembershipStatus = removeMembershipStatus;
        self.editMembershipStatus = editMembershipStatus;
        self.addMembershipStatus = addMembershipStatus;
        self.dtInstance = {};
		self.membershipStatusId = null;
        self.reset = reset;
        self.cancelEdit = cancelEdit;
        self.dtInstance = {};
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.dtInstance = {};
        self.dtColumns = [
            DTColumnBuilder.newColumn('description').withTitle('LEADSTATUS').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.editMembershipStatus('+full.id+')">'+data+'</a>';
					}).withClass("text-left")
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

			// Then just call your service to get the
			// records from server side
			MembershipStatusService
					.loadMembershipStatuses(page, length, search.value, order)
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
        
        function submit() {
            console.log('Submitting');
            if (self.membershipStatus.id === undefined || self.membershipStatus.id === null) {
                console.log('Saving New MembershipStatus', self.membershipStatus);
                createMembershipStatus(self.membershipStatus);
            } else {
                updateMembershipStatus(self.membershipStatus, self.membershipStatus.id);
                console.log('MembershipStatus updated with id ', self.membershipStatus.id);
            }
            self.displayEditButton = false;
        }

        function createMembershipStatus(membershipStatus) {
            console.log('About to create membershipStatus');
            MembershipStatusService.createMembershipStatus(membershipStatus)
                .then(
                    function (response) {
                        console.log('MembershipStatus created successfully');
                        self.successMessage = 'MembershipStatus created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        console.log(self.languages);
                        self.membershipStatus={};
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function (errResponse) {
                        console.error('Error while creating MembershipStatus');
                        self.errorMessage = 'Error while creating MembershipStatus: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateMembershipStatus(membershipStatus, id){
            console.log('About to update membershipStatus'+membershipStatus);
            MembershipStatusService.updateMembershipStatus(membershipStatus, id)
                .then(
                    function (response){
                        console.log('MembershipStatus updated successfully');
                        self.successMessage='MembershipStatus updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function(errResponse){
                        console.error('Error while updating MembershipStatus');
                        self.errorMessage='Error while updating MembershipStatus '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeMembershipStatus(id){
            console.log('About to remove MembershipStatus with id '+id);
            MembershipStatusService.removeMembershipStatus(id)
                .then(
                    function(){
                        console.log('MembershipStatus '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing membershipStatus '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllMembershipStatuses(){
             self.membershipStatuses = MembershipStatusService.getAllMembershipStatuses();
   
            return self.membershipStatuses;
        }

        function editMembershipStatus(id) {
            self.successMessage='';
            self.errorMessage='';
            MembershipStatusService.getMembershipStatus(id).then(
                function (membershipStatus) {
                    self.membershipStatus = membershipStatus;
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing membershipStatus ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.membershipStatus={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.membershipStatus={};
            self.display = false;
            $state.go('main.membershipStatus');
        }
        
        function addMembershipStatus() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
    
    }
    

    ]);
   })();