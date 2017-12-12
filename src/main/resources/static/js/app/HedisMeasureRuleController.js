(function(){
'use strict';
var app = angular.module('my-app');

app.controller('HedisMeasureRuleController',
    ['HedisMeasureRuleService', 'InsuranceService', 'ICDMeasureService','ProblemService', 'HedisMeasureService', 'FrequencyTypeService', 'GenderService', '$scope', '$compile','$state','$stateParams','$filter','$modal','$log','$localStorage','DTOptionsBuilder', 'DTColumnBuilder', function( HedisMeasureRuleService,InsuranceService, ICDMeasureService,ProblemService,HedisMeasureService, FrequencyTypeService,GenderService,  $scope,$compile, $state, $stateParams,$filter,$modal,$log,$localStorage, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.hedisMeasureRule = {};
        self.hedisMeasureRules=[];
        self.hedisMeasures=[];
        self.problems=[];
        self.frequencyTypes=[];
        self.genders=[];
        self.effectiveYear = $filter('date')(new Date($localStorage.loginUser.effectiveYear+'-01-01T06:00:00Z') ,'yyyy');
        self.display =$stateParams.hedisMeasureRuleDisplay||false;;
        self.displayEditButton = false;
        self.submit = submit;
        self.getAllHedisMeasureRules = getAllHedisMeasureRules;
        self.createHedisMeasureRule = createHedisMeasureRule;
        self.updateHedisMeasureRule = updateHedisMeasureRule;
        self.removeHedisMeasureRule = removeHedisMeasureRule;
        self.editHedisMeasureRule = editHedisMeasureRule;
        self.addHedisMeasureRule = addHedisMeasureRule;
        self.getAllInsurances = getAllInsurances;
        self.getEffectiveYears = getEffectiveYears; 
        self.getAllICDMeasures = getAllICDMeasures;
        self.getAllHedisMeasures = getAllHedisMeasures;
        self.getAllFrequencyTypes = getAllFrequencyTypes;
        self.getAllGenders = getAllGenders;
        self.getAllProblems = getAllProblems;
        self.dtInstance = {};
		self.hedisMeasureRuleId = null;
		self.insurances = getAllInsurances();
		self.effectiveYears = getEffectiveYears();
		self.generate = generate;
        self.reset = reset;
        self.hedisMeasureRuleEdit = hedisMeasureRuleEdit;
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
        self.currentHedisMeasureRuleICDCodes =[];
        self.removeHedisMeasureRuleICDCodes = removeHedisMeasureRuleICDCodes;
        self.removeHedisMeasureRuleCPTCodes = removeHedisMeasureRuleCPTCodes;
        self.getNumber = getNumber;
        self.datePartList = ['month','Year'];
        function disableCheck(){
        	return true;
        }
        function  open(size, popupType) {
            var modalInstance = $modal.open({
              templateUrl: 'myModalContent.html',
              controller: 'ModalInstanceController',
              size: size,
              resolve: {
                items: function () {
                	if( popupType === 'ICD'){
                		return self.hedisMeasureRule.icdCodes ;
                	}else if( popupType === 'CPT'){
                		return self.hedisMeasureRule.cptCodes ;
                	}
                  
                },
                popType :function () { return popupType; }
              }
            });

            modalInstance.result.then(function (selectedItems) {
            	 if(popupType == 'ICD'){
            		 self.hedisMeasureRule.icdCodes = selectedItems;
            	 }else if(popupType == 'CPT') {
            		 self.hedisMeasureRule.cptCodes = selectedItems;
            	 }
              
             self.myForm.$setDirty();
            }, function () {
              $log.info('Modal dismissed at: ' + new Date());
            });
          }

  
      //  Short Description	Description	Hedis Code	Problem	CPT Codes	ICD Codes	Frequency	Dose	Sex	Lower Age Limit	Upper Age Limit	Eff. Date From	Eff. Date To

        
        
        self.dtColumns = [
        	DTColumnBuilder.newColumn('shortDescription').withTitle('SHORT_DESCRIPTION').withOption("width",'10%').renderWith(
					function(data, type, full,meta) {
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.hedisMeasureRuleEdit('+full.id+')">'+data+'</a>';
					}).withClass("text-left").withOption("width",'20%'),
        	DTColumnBuilder.newColumn('description').withTitle('DESCRIPTION').withOption("width",'10%'),
        	DTColumnBuilder.newColumn('hedisMeasure.code').withTitle('HEDIS_CODE').withOption("width",'20%'),
        	DTColumnBuilder.newColumn('pbm.description').withTitle('PROBLEM').withOption('defaultContent', ''),
            DTColumnBuilder.newColumn('cptCodes').withTitle('CPT CODES').withOption("width",'10%').withClass('cptCodes').renderWith(
					function(data, type, full, meta) {
						var cptCodes=[];
		            	if(data !== undefined && data !== null ){
		            		for(var i = 0, j = 0; i<data.length ; i++)
		                    {
		            			if(data[i] == null) continue;
		            			cptCodes[j++] =  '<span data-toggle="tooltip" title="'+data[i].shortDescription+'">'+data[i].code+'</span>';
		            			
		                    }
		    					return   cptCodes.join(', ') ;
		            	}
		            	return '';
						
					}),
            DTColumnBuilder.newColumn('icdCodes').withTitle('ICD CODES').withOption("width",'10%').withClass('icdCodes').renderWith(
					function(data, type, full, meta) {
						var icdCodes=[];
		            	if(data !== undefined && data !== null ){
		            		for(var i = 0, j = 0; i<data.length ; i++)
		                    {
		            			if(data[i] == null) continue;
		            			icdCodes[j++] =  '<span data-toggle="tooltip" title="'+data[i].description+'">'+data[i].code+'</span>';  
		            			
		                    }
		    					return   icdCodes.join(', ') ;
		            	}
		            	return '';
						
						 return '<a href="javascript:void(0)" class="'+full.id+'" ng-click="ctrl.hedisMeasureRuleEdit('+full.id+')">'+data+'</a>';
					}),
					DTColumnBuilder.newColumn('frequencyType.description').withTitle('FREQUENCY').withOption("width",'5%').withClass('text-left').withOption('defaultContent', ''),
					DTColumnBuilder.newColumn('doseCount').withTitle('DOSE').withClass('text-left').withOption("width",'5%').withOption('defaultContent', ''),
					DTColumnBuilder.newColumn('genderDescription').withTitle('SEX').withClass('text-left').withOption("width",'5%').withOption('defaultContent', ''),
					DTColumnBuilder.newColumn('lowerAgeLimit').withTitle('LOWER_AGE_LIMIT').withClass('text-left').withOption("width",'10%').withOption('defaultContent', ''),
					DTColumnBuilder.newColumn('upperAgeLimit').withTitle('UPPER_AGE_LIMIT').withClass('text-left').withOption("width",'10%').withOption('defaultContent', ''),
					DTColumnBuilder.newColumn('ageEffectiveFrom').withTitle('EFFECTIVE_FROM').withClass('text-left').withOption("width",'10%').withOption('defaultContent', ''),
					DTColumnBuilder.newColumn('ageEffectiveTo').withTitle('EFFECTIVE_TO').withClass('text-left').withOption("width",'10%').withOption('defaultContent', '')
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
			HedisMeasureRuleService
					.loadHedisMeasureRules(page, length, search.value, sortCol+','+sortDir, insId, effectiveYear)
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
        
       function checkBoxChange(checkStatus, hedisMeasureRuleId) {
			self.displayEditButton = checkStatus;
			self.hedisMeasureRuleId = hedisMeasureRuleId

		}
       
       
        function submit() {
            console.log('Submitting');
            if (self.hedisMeasureRule.id === undefined || self.hedisMeasureRule.id === null) {
                console.log('Saving New HedisMeasureRule', self.hedisMeasureRule);
                createHedisMeasureRule(self.hedisMeasureRule);
            } else {
                updateHedisMeasureRule(self.hedisMeasureRule, self.hedisMeasureRule.id);
                console.log('HedisMeasureRule updated with id ', self.hedisMeasureRule.id);
            }
            self.displayEditButton = false;
        }

        function createHedisMeasureRule(hedisMeasureRule) {
            console.log('About to create hedisMeasureRule');
            HedisMeasureRuleService.createHedisMeasureRule(hedisMeasureRule)
                .then(
                    function (response) {
                        console.log('HedisMeasureRule created successfully');
                        self.successMessage = 'HedisMeasureRule created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.hedisMeasureRules = getAllInsurances();
                        self.hedisMeasureRule={};
                        $scope.myForm.$setPristine();
                        cancelEdit();
                    },
                    function (errResponse) {
                        console.error('Error while creating HedisMeasureRule');
                        self.errorMessage = 'Error while creating HedisMeasureRule: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateHedisMeasureRule(hedisMeasureRule, id){
            console.log('About to update hedisMeasureRule'+hedisMeasureRule);
            HedisMeasureRuleService.updateHedisMeasureRule(hedisMeasureRule, id)
                .then(
                    function (response){
                        console.log('HedisMeasureRule updated successfully');
                        self.successMessage='HedisMeasureRule updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        cancelEdit();
                    },
                    function(errResponse){
                        console.error('Error while updating HedisMeasureRule');
                        self.errorMessage='Error while updating HedisMeasureRule '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeHedisMeasureRule(id){
            console.log('About to remove HedisMeasureRule with id '+id);
            HedisMeasureRuleService.removeHedisMeasureRule(id)
                .then(
                    function(){
                        console.log('HedisMeasureRule '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing hedisMeasureRule '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllHedisMeasureRules(){
        	return   HedisMeasureRuleService.getAllHedisMeasureRules();
        }
        
        function getAllFrequencyTypes(){
        	return   FrequencyTypeService.getAllFrequencyTypes();
        }
        
        function getAllGenders(){
        	return GenderService.getAllGenders()
        }
        
        function editHedisMeasureRule(id) {
            self.successMessage='';
            self.errorMessage='';
            HedisMeasureRuleService.getHedisMeasureRule(id).then(
                function (hedisMeasureRule) {
                    self.hedisMeasureRule = hedisMeasureRule;
                    self.hedisMeasures = getAllHedisMeasures();
                    self.problems = getAllProblems();
                    self.frequencyTypes = getAllFrequencyTypes();
                    self.insurances = getAllInsurances();
                    self.genders = getAllGenders();
                    self.currentHedisMeasureRuleICDCodes = self.hedisMeasureRule.icdCodes;
                    self.display = true;
                },
                function (errResponse) {
                    console.error('Error while removing hedisMeasureRule ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function getAllICDMeasures(){
        	return ICDMeasureService.getAllICDMeasures();
       }
        
        function getAllInsurances(){
        	return InsuranceService.getAllInsurances();
        }
        
        function getAllHedisMeasures(){
        	return HedisMeasureService.getAllHedisMeasures();
        }
        
        function getAllProblems(){
        	return ProblemService.getAllProblems();
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.hedisMeasureRule={};
            $scope.myForm.$setPristine(); //reset Form
        }
       
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.hedisMeasureRule={};
            self.display = false;
            $state.go('main.hedisMeasureRule',{},{location: true,reload: false,notify: false});
        }
        
        function hedisMeasureRuleEdit(id) {
        	var params = {'hedisMeasureRuleDisplay':true};
			var trans =  $state.go('main.hedisMeasureRule.edit',params).transition;
			trans.onSuccess({}, function() { editHedisMeasureRule(id);  }, { priority: -1 });
			
        }
        
        function addHedisMeasureRule() {
            self.successMessage='';
            self.errorMessage='';
            self.insurances = getAllInsurances();
            self.hedisMeasures = getAllHedisMeasures();
            self.frequencyTypes = getAllFrequencyTypes();
            self.genders = getAllGenders();
            self.problems = getAllProblems();
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
        
        function removeHedisMeasureRuleICDCodes(icdCodes){
        	if( self.currentHedisMeasureRuleICDCodes === undefined ||  self.currentHedisMeasureRuleICDCodes.length == 0   ){
				return self.hedisMeasureRule.icdCodes;
			}else{
				
				 $filter('filter')(self.currentHedisMeasureRuleICDCodes, function (item) {
					    for (var i = 0; i <  self.hedisMeasureRule.icdCodes.length; i++) {
					        if ( self.hedisMeasureRule.icdCodes[i].id  ===item.id) {
					        	self.hedisMeasureRule.icdCodes.splice(i, 1);
					        }
					    }
                 });
			}
        }
        
        function removeHedisMeasureRuleCPTCodes(cptCodes){
        	if( self.currentHedisMeasureRuleCPTCodes === undefined ||  self.currentHedisMeasureRuleCPTCodes.length == 0   ){
				return self.hedisMeasureRule.cptCodes;
			}else{
				
				 $filter('filter')(self.currentHedisMeasureRuleCPTCodes, function (item) {
					    for (var i = 0; i <  self.hedisMeasureRule.cptCodes.length; i++) {
					        if ( self.hedisMeasureRule.cptCodes[i].id  ===item.id) {
					        	self.hedisMeasureRule.cptCodes.splice(i, 1);
					        }
					    }
                 });
			}
        }
        
        function  getNumber(num) {
        	  var res = [];
        	for (var i = 0; i < num; i++) {
                res.push(i);
              }
            return res;   
        }
    
    }
    

    ]);
})();