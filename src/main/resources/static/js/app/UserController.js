(function(){
'use strict';
var app = angular.module('my-app');

app.controller('UserController',
    ['UserService', 'roles', 'languages', 'states', 'insurances',  '$scope', '$compile','$location','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( UserService, roles, languages, states, insurances, $scope,$compile,$location,$state, $stateParams, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
      //$location.url('/');
        self.user = {};
        self.users=[];
        self.display =  $stateParams.userDisplay||false;
        self.displayEditButton = false;
        self.submit = submit;
        self.roles=roles;
        self.languages=languages;
        self.counties=[];
        self.states = states;
        self.insurances = insurances;
        self.getAllUsers = getAllUsers;
        self.createUser = createUser;
        self.updateUser = updateUser;
        self.removeUser = removeUser;
        self.editUser = editUser;
		self.userId = null;
        self.reset = reset;
        self.userEdit = userEdit;
        self.cancelEdit = cancelEdit;
        self.addUser = addUser;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.dtInstance = {};
        
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('name').withTitle('USERNAME').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.userEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
            DTColumnBuilder.newColumn('role.role').withTitle('ROLE').withOption('defaultContent', ''),
            DTColumnBuilder.newColumn('effectiveYear').withTitle('EFFECTIVE_YEAR').withOption('defaultContent', ''),
            DTColumnBuilder.newColumn('contact.mobilePhone').withTitle('MOBILE').withOption('defaultContent', ''),
            DTColumnBuilder.newColumn('contact.email').withTitle('EMAIL').withOption('defaultContent', '')
          ];
     
        
        self.dtOptions = DTOptionsBuilder.newOptions()
		.withOption(
				'ajax',
				{
					url : '/Pras/api/user/',
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
			UserService
					.loadUsers(page, length, search.value, order)
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
        
       function checkBoxChange(checkStatus, userId) {
			self.displayEditButton = checkStatus;
			self.userId = userId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.user.id === undefined || self.user.id === null) {
                console.log('Saving New User', self.user);
                createUser(self.user);
            } else {
                updateUser(self.user, self.user.id);
                console.log('User updated with id ', self.user.id);
            }
            self.displayEditButton = false;
        }

        function createUser(user) {
            console.log('About to create user');
            UserService.createUser(user)
                .then(
                    function (response) {
                        console.log('User created successfully');
                        self.successMessage = 'User created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.user={};
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                        $state.go('main.user');
                    },
                    function (errResponse) {
                        console.error('Error while creating User');
                        self.errorMessage = 'Error while creating User: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateUser(user, id){
            console.log('About to update user'+user);
            UserService.updateUser(user, id)
                .then(
                    function (response){
                        console.log('User updated successfully');
                        self.successMessage='User updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                        $state.go('main.user');
                    },
                    function(errResponse){
                        console.error('Error while updating User');
                        self.errorMessage='Error while updating User '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeUser(id){
            console.log('About to remove User with id '+id);
            UserService.removeUser(id)
                .then(
                    function(){
                        console.log('User '+id + ' removed successfully');
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function(errResponse){
                        console.error('Error while removing user '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllUsers(){
             self.users = UserService.getAllUsers();
   
            return self.users;
        }

        function editUser(id) {
            self.successMessage='';
            self.errorMessage='';
            UserService.getUser(id).then(
                function (user) {
                    self.user = user;
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing user ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.user={};
            $scope.myForm.$setPristine(); //reset Form
        }
        
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.user={};
            self.display = false;
            $state.go('main.user');
        }
       
        function addUser() {
        	var params = {'userDisplay':true};
			var trans =  $state.go('main.user.edit',params).transition;
			trans.onSuccess({}, function() { 
				   self.successMessage='';
		            self.errorMessage='';
		            self.display =true;
			}, { priority: -1 });
			
         
        }
        
        function userEdit(id){
        	
        	var params = {'userDisplay':true};
			var trans =  $state.go('main.user.edit',params).transition;
			trans.onSuccess({}, function() { editUser(id); }, { priority: -1 });
			 
		}
    }
    

    ]);
   })();