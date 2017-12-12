(function(){
'use strict';
var app = angular.module('my-app');

app.controller('FileTypeController',
    ['FileTypeService','InsuranceService','$scope', '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( FileTypeService, InsuranceService, $scope,$compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.fileType = {};
        self.fileTypes=[];
        self.insurances=[];
        self.display =$stateParams.fileTypeDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllFileTypes = getAllFileTypes;
        self.createFileType = createFileType;
        self.updateFileType = updateFileType;
        self.removeFileType = removeFileType;
        self.editFileType = editFileType;
        self.addFileType = addFileType;
        self.getAllInsurances = getAllInsurances;
        self.dtInstance = {};
		self.fileTypeId = null;
        self.reset = reset;
        self.fileTypeEdit = fileTypeEdit;
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
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.fileTypeEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
            DTColumnBuilder.newColumn('ins.name').withTitle('INSURANCE').withOption('defaultContent', ''),
            DTColumnBuilder.newColumn('activityMonthInd').withTitle('ACTIVITY_MONTH_IND')
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
			FileTypeService
					.loadFileTypes(page, length, search.value, sortCol+','+sortDir)
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
        
       function checkBoxChange(checkStatus, fileTypeId) {
			self.displayEditButton = checkStatus;
			self.fileTypeId = fileTypeId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.fileType.id === undefined || self.fileType.id === null) {
                console.log('Saving New FileType', self.fileType);
                createFileType(self.fileType);
            } else {
                updateFileType(self.fileType, self.fileType.id);
                console.log('FileType updated with id ', self.fileType.id);
            }
            self.displayEditButton = false;
        }

        function createFileType(fileType) {
            console.log('About to create fileType');
            FileTypeService.createFileType(fileType)
                .then(
                    function (response) {
                        console.log('FileType created successfully');
                        self.successMessage = 'FileType created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.fileTypes = getAllFileTypes();
                        self.fileType={};
                        $scope.myForm.$setPristine();
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating FileType');
                        self.errorMessage = 'Error while creating FileType: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateFileType(fileType, id){
            console.log('About to update fileType'+fileType);
            FileTypeService.updateFileType(fileType, id)
                .then(
                    function (response){
                        console.log('FileType updated successfully');
                        self.successMessage='FileType updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating FileType');
                        self.errorMessage='Error while updating FileType '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeFileType(id){
            console.log('About to remove FileType with id '+id);
            FileTypeService.removeFileType(id)
                .then(
                    function(){
                        console.log('FileType '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing fileType '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllFileTypes(){
             self.fileTypes = FileTypeService.getAllFileTypes();
   
            return self.fileTypes;
        }
        
        function getAllInsurances() {
			return InsuranceService.getAllInsurances();
		}
        
        function editFileType(id) {
            self.successMessage='';
            self.errorMessage='';
            FileTypeService.getFileType(id).then(
                function (fileType) {
                    self.fileType = fileType;
                    self.insurances = getAllInsurances();
                   
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing fileType ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.fileType={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.fileType={};
            self.display = false;
            $state.go('main.fileType', {}, {location: true,reload: false,notify: false});
        }
        
        function fileTypeEdit(id) {
        	var params = {'fileTypeDisplay':true};
			var trans =  $state.go('main.fileType.edit',params).transition;
			trans.onSuccess({}, function() { editFileType(id);  }, { priority: -1 });
			
        }
        
        function addFileType() {
            self.successMessage='';
            self.errorMessage='';
            self.insurances = getAllInsurances();
            self.display =true;
        }
    
    }

    ]);
   })();