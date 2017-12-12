(function(){
'use strict';
var app = angular.module('my-app');

app.controller('RoleController',
    ['RoleService', '$scope', '$compile','$state','DTOptionsBuilder', 'DTColumnBuilder',  function( RoleService, $scope, $compile,$state,DTOptionsBuilder, DTColumnBuilder) {
    	
        var self = this;
        self.role = {};
        self.roles=[];
        self.display =false;
        self.displayEditButton = false;
        self.submit = submit;
        self.addRole = addRole;
        self.getAllRoles = getAllRoles;
        self.createRole = createRole;
        self.updateRole = updateRole;
        self.removeRole = removeRole;
        self.editRole = editRole;
        self.reset = reset;
        self.cancelEdit = cancelEdit;
        self.dtInstance = {};
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.dtColumns = [
            DTColumnBuilder.newColumn('role').withTitle('ROLE').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.editRole('+full.id+')">'+data+'</a>';
					}).withClass("text-left")
          ];
     
        
        self.dtOptions = DTOptionsBuilder.newOptions()
		.withOption(
				'ajax',
				{
					url : '/Pras/api/role/',
					type : 'GET'
				}).withDataProp('data').withOption('bServerSide', true)
				.withOption("bLengthChange", false)
				.withOption("bPaginate", true)
				.withOption('bProcessing', true)
				.withOption('bStateSave', true)
		        .withDisplayLength(20).withOption( 'columnDefs', [ {
					                                orderable : false,
													className : 'select-checkbox',
													targets : 0,
													sortable : false,
													aTargets : [ 0, 1 ] } ])
				.withOption('select', {
										style : 'os',
										selector : 'td:first-child' })
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
			RoleService
					.loadRoles(page, length, search.value, order)
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
        
       function checkBoxChange(checkStatus, facilityTypeId) {
			self.displayEditButton = checkStatus;
			self.facilityTypeId = facilityTypeId

		}
        function submit() {
            console.log('Submitting');
            if (self.role.id === undefined || self.role.id === null) {
                console.log('Saving New Role', self.role);
                createRole(self.role);
            } else {
                updateRole(self.role, self.role.id);
                console.log('Role updated with id ', self.role.id);
            }
        }

        function createRole(role) {
            console.log('About to create role');
            RoleService.createRole(role)
                .then(
                    function (response) {
                        console.log('Role created successfully');
                        self.successMessage = 'Role created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.role={};
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function (errResponse) {
                        console.error('Error while creating Role');
                        self.errorMessage = 'Error while creating Role: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateRole(role, id){
            console.log('About to update role');
            RoleService.updateRole(role, id)
                .then(
                    function (response){
                        console.log('Role updated successfully');
                        self.successMessage='Role updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function(errResponse){
                        console.error('Error while updating Role');
                        self.errorMessage='Error while updating Role '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeRole(id){
            console.log('About to remove Role with id '+id);
            RoleService.removeRole(id)
                .then(
                    function(){
                        console.log('Role '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing role '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllRoles(){
            return RoleService.getAllRoles();
        }

        function editRole(id) {
            self.successMessage='';
            self.errorMessage='';
            RoleService.getRole(id).then(
                function (role) {
                    self.role = role;
                    self.display =true;
                },
                function (errResponse) {
                    console.error('Error while removing role ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function addRole() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.role={};
            $scope.myForm.$setPristine(); //reset Form
        }
        
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.role={};
            self.display = false;
            $state.go('main.role');
        }
    }


    ]);
   })();