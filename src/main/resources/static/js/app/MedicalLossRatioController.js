(function(){
'use strict';
var app = angular.module('my-app');

app.controller('MedicalLossRatioController',
    ['MedicalLossRatioService','ProviderService', 'InsuranceService','RiskReconService', '$scope', '$compile','$state','$stateParams', '$filter' ,'DTOptionsBuilder', 'DTColumnBuilder', 'DTColumnDefBuilder', function(MedicalLossRatioService, ProviderService,  InsuranceService, RiskReconService, $scope,$compile,$state,$stateParams, $filter, DTOptionsBuilder, DTColumnBuilder,DTColumnDefBuilder) {

        var self = this;
        self.medicalLossRatio = {};
        self.medicalLossRatios = [];
        self.prvdrs=[];
        self.categories =['Total', 'Pharmacy','Inst','Prof','Fund','Unwanted_Claims','Stop_Loss', 'Patients' ,'MLR','QMLR','AVGMLR', 'AVGQMLR'];
        self.reportMonths = [];
        self.selectedPrvdrs = [];
        self.selectedReportMonths =[];
        self.selectedCategories =[];
        self.insurance ={};
        self.insurance.id= 0;
        self.display =false;
        self.displayEditButton = false;
        self.insurances=[];
        self.statuses=[];
        self.dtInstance = {};
        self.dt1Instance = {};
        self.mbrId = null;
		self.prvdrId = null;
        self.reset = reset;
        self.getAllInsurances = getAllInsurances;
        self.getAllProviders = getAllProviders;
        self.getAllMedicalLossRatios = getAllMedicalLossRatios;
        self.getAllRiskRecons = getAllRiskRecons;
        self.getAllReportMonths = getAllReportMonths;
        self.setProviders = setProviders;
        self.cancelEdit = cancelEdit;
        self.generate = generate;
        self.providers =  [];
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.insurances = getAllInsurances();
        self.prvdrs = getAllProviders();
       // self.categories = getAllRiskRecons();
        self.reportMonths = getAllReportMonths();
        self.dtInstanceCallback = dtInstanceCallback;
        self.dt1InstanceCallback = dt1InstanceCallback;
        self.displayTable = false;
        
        function dtInstanceCallback(dtInstance) {
	        self.dtInstance = dtInstance;
	    }
        
        function dt1InstanceCallback(dt1Instance) {
	        self.dt1Instance = dt1Instance;
	    }
        
        self.dtColumns =[];
        self.dt1Columns =[];
       
       function createdRow(row, data, dataIndex) {
            // Recompiling so we can bind Angular directive to the DT
            $compile(angular.element(row).contents())($scope);
        }
        
       
       function generate(){
    			// All the parameters you need is in the aoData
   			// variable
    	   self.dtInstance = {};
    	   self.dtColumns =[];
    	   self.displayTable =false;
   			  var insId =  (self.insurance === undefined || self.insurance === null)? 0 : self.insurance.id;
                 var prvdrIds = (self.selectedPrvdrs === undefined || self.selectedPrvdrs === null)? 0 :  self.selectedPrvdrs.map(a => a.id);
                 var prvdrIdss = prvdrIds.join();
                 var reportMonths = self.selectedReportMonths.join();
                 var categories = self.selectedCategories.join();
                 var finalData = [];
     			// Then just call your service to get the
     			// records from server side
     			MedicalLossRatioService
     					.loadMedicalLossRatios( insId,prvdrIdss, reportMonths, categories)
     					.then(
     							function(result) {
                                    var headers = result.data[0];  
                                    var resultData = result.data.slice(1, result.data.length); 
                                    var summaryResultData = [];
     								
                                	angular.forEach(resultData, function(value1, key1){
   										var resultData1 = value1.reduce(function(result1, item, index, array) {
   										result1[index] = item; //a, b, c
   		  								  return result1;
   		  								}, {});
   										finalData.push(resultData1);
   									});
                                	 self.dt1Columns =[];
     								 angular.forEach(headers, function(value, key){
     									       if(key <2){
     									    	 self.dtColumns.push( DTColumnBuilder.newColumn(key).withTitle(value).notVisible());
     									    	self.dt1Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).notVisible());
    									       }else if(key >= 2 && key <= 4) {
    									    	  self.dtColumns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center'));
    									    	  self.dt1Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center'));
    									       }else {
    									    	  self.dtColumns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center'));
    									    	  self.dt1Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center'));
    									       }
     										  if(key >4){
     	 									      angular.forEach(self.selectedCategories, function(categoryValue, categoryKey){
     	 									    	summaryResultData[categoryKey] = summaryResultData[categoryKey]||{};
     	 									    	summaryResultData[categoryKey][1] = 'ProviderId';
     	 									    	summaryResultData[categoryKey][2] = 'Provider';
     	 									    	summaryResultData[categoryKey][3] = self.reportMonths[0];
     	 									    	
     	 									    	summaryResultData[categoryKey][key] =   summaryResultData[categoryKey][key]|| parseFloat('0.00') ;
     	 									    	summaryResultData[categoryKey][key] = Number.parseFloat(summaryResultData[categoryKey][key]);
     	 									    	
     	 		     									angular.forEach(finalData, function(finalDataValue, finalDataKey){
     	 		     										summaryResultData[categoryKey][4] = ($filter('uppercase')(categoryValue)).trim();
     	 		     										
     	 		     										if(finalDataValue[4].trim() ===  ($filter('uppercase')(categoryValue)).trim() ){
     	 		     											summaryResultData[categoryKey][0] = finalDataValue[0];
     	 		     											finalDataValue[key] = finalDataValue[key]|| parseFloat('0.00') ;
     	 		     											finalDataValue[key] = (Number.isNaN(finalDataValue[key]))? parseFloat('0.00'):parseFloat(finalDataValue[key]);
      	 		     											summaryResultData[categoryKey][key] =  parseFloat(parseFloat(summaryResultData[categoryKey][key]) + parseFloat(finalDataValue[key])).toFixed(2) ;
     	 		     										}
     	 		     									});
     	 		     								 });
     	 									      }
   			                        });
     								 
     								summaryResultData = $filter('orderBy')(summaryResultData, "0");
   								
   									self.dtOptions = DTOptionsBuilder.newOptions()
								     							        .withOption('data', finalData) 
								     							        .withDisplayLength(500)
								   							            .withDOM('ft')
									   									.withOption('bSort', false)
									   									.withOption('searchDelay', 1000)
									   									.withOption('bProcessing', true)
									   									.withOption('responsive', true)
									   								    .withOption('createdRow', createdRow)
									   									.withOption('bDeferRender', true)
									   									.withOption('bDestroy', true);
   									
   									self.dt1Options = DTOptionsBuilder.newOptions()
 							        .withOption('data', summaryResultData) 
 							        .withDisplayLength(500)
							            .withDOM('ft')
   									.withOption('bSort', false)
   									.withOption('searchDelay', 1000)
   									.withOption('bProcessing', true)
   									.withOption('responsive', true)
   								    .withOption('createdRow', createdRow)
   									.withOption('bDeferRender', true)
   									.withOption('bDestroy', true);
   									self.displayTable = true;
   						    	   
     							});
     			if( !angular.equals(self.dtInstance, {}) ) {self.dtInstance.rerender();self.dt1Instance.rerender();}
    	   
       }
       

        function getAllProviders(){
             self.prvdrs = ProviderService.getAllProviders();
   
            return self.prvdrs;
        }

        function getAllMedicalLossRatios(){
            self.medicalLossRatios = MedicalLossRatioService.getAllMedicalLossRatios();
  
           return self.medicalLossRatios;
       }
        
        function getAllRiskRecons() {
			return RiskReconService.getAllRiskRecons();
		}
        
        function getAllReportMonths() {
        	self.reportMonths = MedicalLossRatioService.getAllReportMonths();
        	return self.reportMonths;
		}
        
        
        function getAllInsurances() {
			return InsuranceService.getAllInsurances();
		}
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.medicalLossRatio={};
            $scope.myForm.$setPristine(); //reset Form
        }
        
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.provider={};
            self.display = false;
            $state.go('main.medicalLossRatio', {}, {location: true,reload: false,notify: false});
        }
       
        function setProviders(insId){
        	self.providers =   $filter('providerFilter')(self.prvdrs, self.insurance.id);
        	self.providers =   $filter('orderBy')(self.providers, 'name');
        }
    
        function  sum(items, prop){
            return items.reduce( function(a, b){
                return a + b[prop];
            }, 0);
        }
    }
    

    ]);
   })();