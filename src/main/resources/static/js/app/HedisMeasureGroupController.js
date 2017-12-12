(function(){
'use strict';
var app = angular.module('my-app');
app.controller('HedisMeasureGroupController',
    ['HedisMeasureGroupService','$scope', '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( HedisMeasureGroupService,   $scope,$compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.hedisMeasureGroup = {};
        self.hedisMeasureGroups=[];
        self.display =$stateParams.hedisMeasureGroupDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllHedisMeasureGroups = getAllHedisMeasureGroups;
        self.createHedisMeasureGroup = createHedisMeasureGroup;
        self.updateHedisMeasureGroup = updateHedisMeasureGroup;
        self.removeHedisMeasureGroup = removeHedisMeasureGroup;
        self.editHedisMeasureGroup = editHedisMeasureGroup;
        self.addHedisMeasureGroup = addHedisMeasureGroup;
        self.dtInstance = {};
		self.hedisMeasureGroupId = null;
        self.reset = reset;
        self.hedisMeasureGroupEdit = hedisMeasureGroupEdit;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('code').withTitle('GROUP_NAME').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.hedisMeasureGroupEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left").withOption("width",'20%'),
			DTColumnBuilder.newColumn('description').withTitle('DESCRIPTION').withOption("width",'50%')
          ];
     
        
        self.dtOptions = DTOptionsBuilder.newOptions()
		.withOption(
				'ajax',
				{
					url : '/Pras/api/hedisMeasureGroup/',
					type : 'GET'
				}).withDataProp('data').withOption('bServerSide', true)
				.withOption("bLengthChange", false)
				.withOption("bPaginate", true)
				.withOption('bProcessing', true)
				.withOption('bSaveState', true)
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
			var page = aoData[3].value / aoData[4].value ;
			var length = aoData[4].value ;
			var search = aoData[5].value;

			
			// Then just call your service to get the
			// records from server side
			HedisMeasureGroupService
					.loadHedisMeasureGroups(page, length, search.value, order)
					.then(
							function(result) {
								var records = {
									'recordsTotal' : result.data.totalElements,
									'recordsFiltered' : result.data.totalElements,
									'data' : result.data.content
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
        
       function checkBoxChange(checkStatus, hedisMeasureGroupId) {
			self.displayEditButton = checkStatus;
			self.hedisMeasureGroupId = hedisMeasureGroupId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.hedisMeasureGroup.id === undefined || self.hedisMeasureGroup.id === null) {
                console.log('Saving New HedisMeasureGroup', self.hedisMeasureGroup);
                createHedisMeasureGroup(self.hedisMeasureGroup);
            } else {
                updateHedisMeasureGroup(self.hedisMeasureGroup, self.hedisMeasureGroup.id);
                console.log('HedisMeasureGroup updated with id ', self.hedisMeasureGroup.id);
            }
            self.displayEditButton = false;
        }

        function createHedisMeasureGroup(hedisMeasureGroup) {
            console.log('About to create hedisMeasureGroup');
            HedisMeasureGroupService.createHedisMeasureGroup(hedisMeasureGroup)
                .then(
                    function (response) {
                        console.log('HedisMeasureGroup created successfully');
                        self.successMessage = 'HedisMeasureGroup created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.hedisMeasureGroups = getAllHedisMeasureGroups();
                        self.hedisMeasureGroup={};
                        $scope.myForm.$setPristine();
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating HedisMeasureGroup');
                        self.errorMessage = 'Error while creating HedisMeasureGroup: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateHedisMeasureGroup(hedisMeasureGroup, id){
            console.log('About to update hedisMeasureGroup'+hedisMeasureGroup);
            HedisMeasureGroupService.updateHedisMeasureGroup(hedisMeasureGroup, id)
                .then(
                    function (response){
                        console.log('HedisMeasureGroup updated successfully');
                        self.successMessage='HedisMeasureGroup updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating HedisMeasureGroup');
                        self.errorMessage='Error while updating HedisMeasureGroup '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeHedisMeasureGroup(id){
            console.log('About to remove HedisMeasureGroup with id '+id);
            HedisMeasureGroupService.removeHedisMeasureGroup(id)
                .then(
                    function(){
                        console.log('HedisMeasureGroup '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing hedisMeasureGroup '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllHedisMeasureGroups(){
             self.hedisMeasureGroups = HedisMeasureGroupService.getAllHedisMeasureGroups();
   
            return self.hedisMeasureGroups;
        }
        
        
        function editHedisMeasureGroup(id) {
            self.successMessage='';
            self.errorMessage='';
            HedisMeasureGroupService.getHedisMeasureGroup(id).then(
                function (hedisMeasureGroup) {
                    self.hedisMeasureGroup = hedisMeasureGroup;
                   
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing hedisMeasureGroup ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.hedisMeasureGroup={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.hedisMeasureGroup={};
            self.display = false;
            $state.go('main.hedisGroup',{},{location: true,reload: false,notify: false});
        }
        
        function hedisMeasureGroupEdit(id) {
        	var params = {'hedisGroupDisplay':true};
			var trans =  $state.go('main.hedisGroup.edit',params).transition;
			trans.onSuccess({}, function() { editHedisMeasureGroup(id);  }, { priority: -1 });
			
        }
        
        function addHedisMeasureGroup() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
    
    }
    

    ]);
   })();