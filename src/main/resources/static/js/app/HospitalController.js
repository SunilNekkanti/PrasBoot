(function(){
'use strict';
var app = angular.module('my-app');

app.controller('HospitalController',
    ['HospitalService','$scope', '$compile','$state','$stateParams','DTOptionsBuilder', 'DTColumnBuilder', function( HospitalService,  $scope,$compile, $state, $stateParams,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.hospital = {};
        self.hospitals=[];
        self.display =$stateParams.hospitalDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllHospitals = getAllHospitals;
        self.createHospital = createHospital;
        self.updateHospital = updateHospital;
        self.removeHospital = removeHospital;
        self.editHospital = editHospital;
        self.addHospital = addHospital;
        self.dtInstance = {};
		self.hospitalId = null;
        self.reset = reset;
        self.hospitalEdit = hospitalEdit;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.dtColumns = [
            DTColumnBuilder.newColumn('name').withTitle('NAME').renderWith(
					function(data, type, full,
							meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.hospitalEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left"),
            DTColumnBuilder.newColumn('code').withTitle('CODE') 
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
			HospitalService
					.loadHospitals(page, length, search.value, sortCol+','+sortDir)
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
        
       function checkBoxChange(checkStatus, hospitalId) {
			self.displayEditButton = checkStatus;
			self.hospitalId = hospitalId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.hospital.id === undefined || self.hospital.id === null) {
                console.log('Saving New Hospital', self.hospital);
                createHospital(self.hospital);
            } else {
                updateHospital(self.hospital, self.hospital.id);
                console.log('Hospital updated with id ', self.hospital.id);
            }
            self.displayEditButton = false;
        }

        function createHospital(hospital) {
            console.log('About to create hospital');
            HospitalService.createHospital(hospital)
                .then(
                    function (response) {
                        console.log('Hospital created successfully');
                        self.successMessage = 'Hospital created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.hospitals = getAllHospitals();
                        self.hospital={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating Hospital');
                        self.errorMessage = 'Error while creating Hospital: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateHospital(hospital, id){
            console.log('About to update hospital'+hospital);
            HospitalService.updateHospital(hospital, id)
                .then(
                    function (response){
                        console.log('Hospital updated successfully');
                        self.successMessage='Hospital updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $state.go("hospital");
                    },
                    function(errResponse){
                        console.error('Error while updating Hospital');
                        self.errorMessage='Error while updating Hospital '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeHospital(id){
            console.log('About to remove Hospital with id '+id);
            HospitalService.removeHospital(id)
                .then(
                    function(){
                        console.log('Hospital '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing hospital '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllHospitals(){
             self.hospitals = HospitalService.getAllHospitals();
   
            return self.hospitals;
        }
        
        
        
        function editHospital(id) {
            self.successMessage='';
            self.errorMessage='';
            HospitalService.getHospital(id).then(
                function (hospital) {
                    self.hospital = hospital;
                   
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing hospital ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.hospital={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.hospital={};
            self.display = false;
            $state.go('main.hospital');
        }
        
        function hospitalEdit(id) {
        	var params = {'hospitalDisplay':true};
			var trans =  $state.go('main.hospital.edit',params).transition;
			trans.onSuccess({}, function() { editHospital(id);  }, { priority: -1 });
			
        }
        
        function addHospital() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
    
    }
    

    ]);
   })();