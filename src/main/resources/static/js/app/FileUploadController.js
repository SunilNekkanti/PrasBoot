(function(){
'use strict';
var app = angular.module('my-app');
app.controller('FileUploadController',
    ['FileUploadService','InsuranceService','$scope', '$compile','$state','$stateParams','$interval','DTOptionsBuilder', 'DTColumnBuilder', function( FileUploadService, InsuranceService, $scope,$compile, $state, $stateParams,$interval,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.fileUpload = {};
        self.fileUploads=[];
        self.insurances=[];
        self.display =$stateParams.fileUploadDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllFileUploads = getAllFileUploads;
        self.createFileUpload = createFileUpload;
        self.updateFileUpload = updateFileUpload;
        self.removeFileUpload = removeFileUpload;
        self.editFileUpload = editFileUpload;
        self.addFileUpload = addFileUpload;
        self.getAllInsurances = getAllInsurances;
        self.dtInstance = {};
		self.fileUploadId = null;
        self.reset = reset;
        self.fileUploadEdit = fileUploadEdit;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('description').withTitle('DESCRIPTION').renderWith(
					function(data, upload, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.fileUploadEdit('+full.id+')">'+data+'</a>';
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
        .withPaginationUpload('full_numbers')
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
			FileUploadService
					.loadFileUploads(page, length, search.value, sortCol+','+sortDir)
					.then(
							function(result) {
								var records = {
									'recordsTotal' : result.data.totalElements||0,
									'recordsFiltered' : result.data.totalElements||0,
									'data' : result.data.content||{}
								};
								fnCallback(records);
							});
				$interval(reloadData,  1000);			
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
        
       function checkBoxChange(checkStatus, fileUploadId) {
			self.displayEditButton = checkStatus;
			self.fileUploadId = fileUploadId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.fileUpload.id === undefined || self.fileUpload.id === null) {
                console.log('Saving New FileUpload', self.fileUpload);
                createFileUpload(self.fileUpload);
            } else {
                updateFileUpload(self.fileUpload, self.fileUpload.id);
                console.log('FileUpload updated with id ', self.fileUpload.id);
            }
            self.displayEditButton = false;
        }

        function createFileUpload(fileUpload) {
            console.log('About to create fileUpload');
            FileUploadService.createFileUpload(fileUpload)
                .then(
                    function (response) {
                        console.log('FileUpload created successfully');
                        self.successMessage = 'FileUpload created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.fileUploads = getAllFileUploads();
                        self.fileUpload={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating FileUpload');
                        self.errorMessage = 'Error while creating FileUpload: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateFileUpload(fileUpload, id){
            console.log('About to update fileUpload'+fileUpload);
            FileUploadService.updateFileUpload(fileUpload, id)
                .then(
                    function (response){
                        console.log('FileUpload updated successfully');
                        self.successMessage='FileUpload updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $state.go("fileUpload");
                    },
                    function(errResponse){
                        console.error('Error while updating FileUpload');
                        self.errorMessage='Error while updating FileUpload '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeFileUpload(id){
            console.log('About to remove FileUpload with id '+id);
            FileUploadService.removeFileUpload(id)
                .then(
                    function(){
                        console.log('FileUpload '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing fileUpload '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllFileUploads(){
             self.fileUploads = FileUploadService.getAllFileUploads();
   
            return self.fileUploads;
        }
        
        function getAllInsurances() {
			return InsuranceService.getAllInsurances();
		}
        
        function editFileUpload(id) {
            self.successMessage='';
            self.errorMessage='';
            FileUploadService.getFileUpload(id).then(
                function (fileUpload) {
                    self.fileUpload = fileUpload;
                    self.insurances = getAllInsurances();
                   
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing fileUpload ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.fileUpload={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.fileUpload={};
            self.display = false;
            $state.go('main.fileUpload');
        }
        
        function fileUploadEdit(id) {
        	var params = {'fileUploadDisplay':true};
			var trans =  $state.go('main.fileUpload.edit',params).transition;
			trans.onSuccess({}, function() { editFileUpload(id);  }, { priority: -1 });
			
        }
        
        function addFileUpload() {
            self.successMessage='';
            self.errorMessage='';
            self.insurances = getAllInsurances();
            self.display =true;
        }
        
    
    }
    

    ]);
   })();