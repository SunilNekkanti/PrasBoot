(function(){
'use strict';
var app = angular.module('my-app');

app.controller('ProblemController',
    ['ProblemService', 'InsuranceService', 'ICDMeasureService', '$scope', '$compile','$state','$stateParams','$filter','$modal','$log','$localStorage','DTOptionsBuilder', 'DTColumnBuilder', function( ProblemService,InsuranceService, ICDMeasureService, $scope,$compile, $state, $stateParams,$filter,$modal,$log, $localStorage,DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.problem = {};
        self.problems=[];
        self.display =$stateParams.problemDisplay||false;;
        self.effectiveYear= $filter('date')(new Date($localStorage.loginUser.effectiveYear+'-01-01T06:00:00Z') ,'yyyy');
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllProblems = getAllProblems;
        self.createProblem = createProblem;
        self.updateProblem = updateProblem;
        self.removeProblem = removeProblem;
        self.editProblem = editProblem;
        self.addProblem = addProblem;
        self.getAllInsurances = getAllInsurances;
        self.getEffectiveYears = getEffectiveYears; 
        self.getAllICDMeasures = getAllICDMeasures;
        self.dtInstance = {};
		self.problemId = null;
		self.insurances = getAllInsurances();
		self.effectiveYears = getEffectiveYears();
		self.generate = generate;
        self.reset = reset;
        self.problemEdit = problemEdit;
        self.cancelEdit = cancelEdit;
        self.open = open;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.disableCheck = disableCheck;
        self.selected = {};
        self.selection = [];
        self.icdMeasures=getAllICDMeasures();
        self.currentProblemICDCodes =[];
        self.removeProblemICDCodes = removeProblemICDCodes;
        
        function disableCheck(){
        	return true;
        }
        function  open(size) {
            var modalInstance = $modal.open({
              templateUrl: 'myModalContent.html',
              controller: 'ModalInstanceController',
              size: size,
              resolve: {
                items: function () {
                  return self.problem.icdCodes ;
                },
                popType :function () { return 'ICD'; }
              }
            });

            modalInstance.result.then(function (selectedItems) {
              self.problem.icdCodes = selectedItems;
             self.myForm.$setDirty();
            }, function () {
              $log.info('Modal dismissed at: ' + new Date());
            });
          }

        self.dtColumns = [
        	DTColumnBuilder.newColumn('effectiveYear').withTitle('EFFECTIVE_YEAR').withOption("sWidth",'10%'),
        	DTColumnBuilder.newColumn('insId.name').withTitle('INSURANCE').withOption("sWidth",'20%'),
        	DTColumnBuilder.newColumn('description').withTitle('DESCRIPTION').renderWith(
					function(data, type, full,meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.problemEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left").withOption("sWidth",'20%'),
            DTColumnBuilder.newColumn('icdCodes').withTitle('ICD CODES').renderWith(
					function(data, type, full, meta) {
						var icdCodes=[];
		            	if(data !== undefined && data !== null ){
		            		for(var i = 0, j = 0; i<data.length ; i++)
		                    {
		            			if(data[i] == null) continue;
		            			icdCodes[j++] = $filter('uppercase') (data[i].code);
		            			
		                    }
		    					return icdCodes.join(', ');
		            	}
		            	return '';
						
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.problemEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left").withOption("sWidth",'10%').withOption("sClass",' { max-width: 300px; word-wrap: break-word;  }')
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
			var insId =  (self.insurance === undefined || self.insurance === null)? 0 : self.insurance.id;
	        var effectiveYear = (self.effectiveYear === undefined || self.effectiveYear === null)? 0 :  self.effectiveYear;
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
			ProblemService
					.loadProblems(page, length, search.value, sortCol+','+sortDir, insId, effectiveYear)
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

    	 function generate(){
             self.dtInstance.rerender();
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
        
       function checkBoxChange(checkStatus, problemId) {
			self.displayEditButton = checkStatus;
			self.problemId = problemId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.problem.id === undefined || self.problem.id === null) {
                console.log('Saving New Problem', self.problem);
                createProblem(self.problem);
            } else {
                updateProblem(self.problem, self.problem.id);
                console.log('Problem updated with id ', self.problem.id);
            }
            self.displayEditButton = false;
        }

        function createProblem(problem) {
            console.log('About to create problem');
            ProblemService.createProblem(problem)
                .then(
                    function (response) {
                        console.log('Problem created successfully');
                        self.successMessage = 'Problem created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.problems = getAllInsurances();
                        self.problem={};
                        $scope.myForm.$setPristine();
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating Problem');
                        self.errorMessage = 'Error while creating Problem: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateProblem(problem, id){
            console.log('About to update problem'+problem);
            ProblemService.updateProblem(problem, id)
                .then(
                    function (response){
                        console.log('Problem updated successfully');
                        self.successMessage='Problem updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating Problem');
                        self.errorMessage='Error while updating Problem '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeProblem(id){
            console.log('About to remove Problem with id '+id);
            ProblemService.removeProblem(id)
                .then(
                    function(){
                        console.log('Problem '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing problem '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllProblems(){
             self.problems = ProblemService.getAllProblems();
   
            return self.problems;
        }
        
        
        function editProblem(id) {
            self.successMessage='';
            self.errorMessage='';
            ProblemService.getProblem(id).then(
                function (problem) {
                    self.problem = problem;
                    self.insurances = getAllInsurances();
                    self.currentProblemICDCodes = self.problem.icdCodes;
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing problem ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function getAllICDMeasures(){
        	return ICDMeasureService.getAllICDMeasures();
       }
        
        function getAllInsurances(){
        	return InsuranceService.getAllInsurances();
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.problem={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.problem={};
            self.display = false;
            $state.go('main.problem',{},{location: true,reload: false,notify: false});
        }
        
        function problemEdit(id) {
        	var params = {'problemDisplay':true};
			var trans =  $state.go('main.problem.edit',params).transition;
			trans.onSuccess({}, function() { editProblem(id);  }, { priority: -1 });
			
        }
        
        function addProblem() {
            self.successMessage='';
            self.errorMessage='';
            self.insurances = getAllInsurances();
            self.display =true;
        }
        
        function getEffectiveYears(){
        	
        	var myDate = new Date();
        	var previousYear = new Date(myDate);
        	previousYear.setYear(myDate.getFullYear()-1);
        	var nextYear = new Date(myDate);
        	nextYear.setYear(myDate.getFullYear()+1);
        	var years = [];
        	years.push($filter('date')(previousYear,'yyyy'));
        	years.push($filter('date')(myDate,'yyyy'));
        	years.push($filter('date')(nextYear,'yyyy'));
        	
        	return years;
        }
        
        function removeProblemICDCodes(icdCodes){
        	if( self.currentProblemICDCodes === undefined ||  self.currentProblemICDCodes.length == 0   ){
				return self.problem.icdCodes;
			}else{
				
				 $filter('filter')(self.currentProblemICDCodes, function (item) {
					    for (var i = 0; i <  self.problem.icdCodes.length; i++) {
					        if ( self.problem.icdCodes[i].id  ===item.id) {
					        	self.problem.icdCodes.splice(i, 1);
					        }
					    }
                 });
			}
        }
    
    }
    

    ]);
   })();