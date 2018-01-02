(function(){
'use strict';
var app = angular.module('my-app');

app.controller('NewMedicalLossRatioController',
    ['NewMedicalLossRatioService','MedicalLossRatioService','ProviderService', 'InsuranceService','$scope', '$compile','$state','$stateParams', '$filter' ,'DTOptionsBuilder', 'DTColumnBuilder', function(NewMedicalLossRatioService,MedicalLossRatioService, ProviderService,  InsuranceService,  $scope,$compile,$state,$stateParams, $filter, DTOptionsBuilder, DTColumnBuilder) {

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
        	DTColumnBuilder.newColumn('prvdr.name').withTitle('PROVIDER').withClass("text-left"),
 			DTColumnBuilder.newColumn('reportMonth').withTitle('REPORT_MONTH').withClass("text-center"),
 			DTColumnBuilder.newColumn('activityMonth').withTitle('ACTIVITY_MONTH').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgMbrCnt').withTitle('MBR_CNT').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('funding').withTitle('FUNDING').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('totalExp').withTitle('TOTAL_EXP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('balance').withTitle('BALANCE').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('mlr').withTitle('MLR').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('qmlr').withTitle('QMLR').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgProf').withTitle('PROF').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgInst').withTitle('INST').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgPhar').withTitle('PHAR').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('ibnr').withTitle('IBNR').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('pcpCap').withTitle('PCP_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('specCap').withTitle('SPEC_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('dentalCap').withTitle('DEN_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('transCap').withTitle('TRANS_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('visCap').withTitle('VISION_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('ibnrInst').withTitle('IBNR_INST').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('ibnrProf').withTitle('IBNR_PROF').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgSLExp').withTitle('SL_EXP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgSLCredit').withTitle('SL_CREDIT').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgVabAdjust').withTitle('VAB_ADJUSTMENT').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('adjust').withTitle('ADJUSTMENT').withOption('defaultContent', '').withClass("text-center")
 			
        ];
        
        self.dt1Columns =[
 			DTColumnBuilder.newColumn('reportMonth').withTitle('REPORT_MONTH').withClass("text-center"),
 			DTColumnBuilder.newColumn('activityMonth').withTitle('ACTIVITY_MONTH').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgMbrCnt').withTitle('MBR_CNT').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('funding').withTitle('FUNDING').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('totalExp').withTitle('TOTAL_EXP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('balance').withTitle('BALANCE').withOption('defaultContent', ''),
 			DTColumnBuilder.newColumn('mlr').withTitle('MLR').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('qmlr').withTitle('QMLR').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgProf').withTitle('PROF').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgInst').withTitle('INST').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgPhar').withTitle('PHAR').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('ibnr').withTitle('IBNR').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('pcpCap').withTitle('PCP_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('specCap').withTitle('SPEC_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('dentalCap').withTitle('DEN_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('transCap').withTitle('TRANS_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('visCap').withTitle('VISION_CAP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('ibnrInst').withTitle('IBNR_INST').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('ibnrProf').withTitle('IBNR_PROF').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgSLExp').withTitle('SL_EXP').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgSLCredit').withTitle('SL_CREDIT').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('amgVabAdjust').withTitle('VAB_ADJUSTMENT').withOption('defaultContent', '').withClass("text-center"),
 			DTColumnBuilder.newColumn('adjust').withTitle('ADJUSTMENT').withOption('defaultContent', '').withClass("text-center")
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
		.withOption('scrollY', 450)
		.withOption('scrollX', 750)
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
		.withOption('scrollY', 450)
		.withOption('scrollX', 750)
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
        	console.log('self.reportMonths',self.reportMonths);
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