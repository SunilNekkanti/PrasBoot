(function(){
'use strict';
var app = angular.module('my-app');

app.controller('NewMedicalLossRatioController',
    ['NewMedicalLossRatioService','ProviderService', 'InsuranceService','$scope', '$compile','$state','$stateParams', '$filter' ,'DTOptionsBuilder', 'DTColumnBuilder', function(NewMedicalLossRatioService, ProviderService,  InsuranceService,  $scope,$compile,$state,$stateParams, $filter, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.newMedicalLossRatio = {};
        self.newMedicalLossRatios = [];
        self.prvdrs=[];
        self.categories =['Total', 'Pharmacy','Inst','Fund','Unwanted_Claims','Stop_Loss', 'Patients' ,'MLR','QMLR','AVGMLR', 'AVGQMLR'];
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
        self.getAllNewMedicalLossRatios = getAllNewMedicalLossRatios;
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
        self.reportMonths = getAllReportMonths();
        self.dtInstanceCallback = dtInstanceCallback;
        self.dt1InstanceCallback = dt1InstanceCallback;
        self.reset = reset;
        
        self.displayTable = false;
        function dtInstanceCallback(dtInstance) {
	        self.dtInstance = dtInstance;
	    }
        
        
        function dt1InstanceCallback(dt1Instance) {
	        self.dt1Instance = dt1Instance;
	    }
        
        self.dtColumns =[
        	DTColumnBuilder.newColumn('prvdr.name').withTitle('PROVIDER'),
 			DTColumnBuilder.newColumn('reportMonth').withTitle('REPORT_MONTH'),
 			DTColumnBuilder.newColumn('activityMonth').withTitle('ACTIVITY_MONTH'),
 			DTColumnBuilder.newColumn('patients').withTitle('PATIENTS').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('fund').withTitle('FUNDING').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('prof').withTitle('PROF').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('inst').withTitle('INST').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('pharmacy').withTitle('Rx').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('ibnr').withTitle('IBNR').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('pcpCap').withTitle('PCP_CAP').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('specCap').withTitle('SPEC_CAP').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('stopLossExp').withTitle('STOPLOSS_EXP').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('stopLossCredit').withTitle('STOPLOSS_CREDIT').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('adjust').withTitle('ADJUSTMENT').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('totalExp').withTitle('TOTAL_EXP').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('balance').withTitle('BALANCE').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('unwantedClaims').withTitle('UNWANTED_CLAIMS').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('stopLoss').withTitle('STOPLOSS').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('mlr').withTitle('MLR').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('qmlr').withTitle('QMLR').withOption('defaultContent', '')
        ];
        
        self.dt1Columns =[
			DTColumnBuilder.newColumn('reportMonth').withTitle('REPORT_MONTH'),
			DTColumnBuilder.newColumn('activityMonth').withTitle('ACTIVITY_MONTH'),
			DTColumnBuilder.newColumn('patients').withTitle('PATIENTS').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('fund').withTitle('FUNDING').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('prof').withTitle('PROF').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('inst').withTitle('INST').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('pharmacy').withTitle('Rx').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('ibnr').withTitle('IBNR').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('pcpCap').withTitle('PCP_CAP').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('specCap').withTitle('SPEC_CAP').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('stopLossExp').withTitle('STOPLOSS_EXP').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('stopLossCredit').withTitle('STOPLOSS_CREDIT').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('adjust').withTitle('ADJUSTMENT').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('totalExp').withTitle('TOTAL_EXP').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('balance').withTitle('BALANCE').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('unwantedClaims').withTitle('UNWANTED_CLAIMS').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('stopLoss').withTitle('STOPLOSS').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('mlr').withTitle('AVG_MLR').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('qmlr').withTitle('AVG_QMLR').withOption('defaultContent', '')
        	
        ];
        
        
        self.dtOptions = DTOptionsBuilder.newOptions()
		.withOption('bServerSide', true)
		.withOption('responsive', true)
		.withOption("bLengthChange", false)
		.withOption("bPaginate", false)
		.withOption('bProcessing', true)
		.withOption('bSaveState', true)
		.withOption('searchDelay', 1000)
	    .withOption('createdRow', createdRow)
        .withPaginationType('full_numbers')
		.withOption('bDeferRender', true)
		.withOption('bDestroy', true)
		.withFnServerData(serverData);
        
        
        function serverData(sSource, aoData, fnCallback) {
			
            var insId =  (self.insurance === undefined || self.insurance === null)? 0 : self.insurance.id;
            var prvdrIds = (self.selectedPrvdrs === undefined || self.selectedPrvdrs === null)? 0 :  self.selectedPrvdrs.map(a => a.id);
            var prvdrIdss = prvdrIds.join();
            var isSummary = false;
            var reportMonths = self.selectedReportMonths.join();
            var search = aoData[5].value;
			
			// Then just call your service to get the
			// records from server side
			 NewMedicalLossRatioService
				.loadNewMedicalLossRatios( insId,prvdrIdss, reportMonths, isSummary,search.value)
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



		self.dt1Options = DTOptionsBuilder.newOptions()
		.withOption('bServerSide', true)
		.withOption('responsive', true)
		.withOption("bLengthChange", false)
		.withOption("bPaginate", false)
		.withOption('bProcessing', true)
		.withOption('bSaveState', true)
		.withOption('searchDelay', 1000)
		.withOption('createdRow', createdRow)
		.withPaginationType('full_numbers')
		.withOption('bDeferRender', true)
		.withOption('bDestroy', true)
		.withFnServerData(serverData1);

        function serverData1(sSource, aoData, fnCallback) {
			
            var insId =  (self.insurance === undefined || self.insurance === null)? 0 : self.insurance.id;
            var prvdrIds = (self.selectedPrvdrs === undefined || self.selectedPrvdrs === null)? 0 :  self.selectedPrvdrs.map(a => a.id);
            var prvdrIdss = prvdrIds.join();
            var isSummary = true;
            var reportMonths = self.selectedReportMonths.join();
			
			// Then just call your service to get the
			// records from server side
			 NewMedicalLossRatioService
				.loadNewMedicalLossRatios( insId,prvdrIdss, reportMonths, isSummary)
					.then(
							function(result) {
								self.finalData  = [];
							  	
								var records = {
									'recordsTotal' : result.data.totalElements||0,
									'recordsFiltered' : result.data.totalElements||0,
									'data' : result.data.content||{}
								};
								fnCallback(records);
							});
		}
        
       function createdRow(row, data, dataIndex) {
            // Recompiling so we can bind Angular directive to the DT
            $compile(angular.element(row).contents())($scope);
        }
        
       
       function generate(){
    	   self.displayTable =true;
       }
       

        function getAllProviders(){
             self.prvdrs = ProviderService.getAllProviders();
            return self.prvdrs;
        }

        function getAllNewMedicalLossRatios(){
            self.newMedicalLossRatios = NewMedicalLossRatioService.getAllNewMedicalLossRatios();
  
           return self.newMedicalLossRatios;
       }
        
        function getAllReportMonths() {
        	self.reportMonths = NewMedicalLossRatioService.getAllReportMonths();
        	return self.reportMonths;
		}
        
        
        function getAllInsurances() {
			return InsuranceService.getAllInsurances();
		}
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.newMedicalLossRatio={};
            $scope.myForm.$setPristine(); //reset Form
        }
        
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.provider={};
            self.display = false;
            $state.go('main.newMedicalLossRatio');
        }
       
        function setProviders(){
            self.providers = $filter('providerFilter')(self.prvdrs,  self.insurance.id );
        	self.providers =   $filter('orderBy')(self.providers, 'name');
        	reset();
        }
        
        function  sum(items, prop){
            return items.reduce( function(a, b){
                return a + b[prop];
            }, 0);
        }
        
        function reset(){
        	self.displayTable = false;
        }
    }
    

    ]);
   })();