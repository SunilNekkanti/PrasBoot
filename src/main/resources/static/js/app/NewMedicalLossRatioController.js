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
        self.reportingQuarters = [{quarter:'Q1', months:['01','02','03']},{quarter:'Q2',months:['04','05','06']},{quarter:'Q3',months:['07','08','09']},{quarter:'Q4',months:['10','11','12']}];
        self.selectedReportingQuarters =[];
        self.reportingYears = [];
        self.selectedPrvdrs = [];
        self.selectedReportMonths =[];
        self.selectedReportingYears = [];
        self.selectedCategories =[];
        self.isChecked = isChecked;
        self.isCheckedQuarter = isCheckedQuarter;
        self.isCheckedMonth = isCheckedMonth;
        self.display =false;
        self.displayEditButton = false;
        self.insurances=[];
        self.statuses=[];
        self.dtInstance = {};
        self.dt1Instance = {};
        self.mbrId = null;
		self.prvdrId = null;
        self.reset = reset;
        self.sync =sync;
         self.syncQuarters =syncQuarters;
         self.syncMonths = syncMonths;
        self.isChecked = isChecked;
        self.getAllInsurances = getAllInsurances;
        self.getAllProviders = getAllProviders;
        self.getAllNewMedicalLossRatios = getAllNewMedicalLossRatios;
        self.getAllReportMonths = getAllReportMonths;
        self.getAllReportingYears = getAllReportingYears;
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
        self.reportingYears = getAllReportingYears();
        self.dtInstanceCallback = dtInstanceCallback;
        self.dt1InstanceCallback = dt1InstanceCallback;
        self.reset = reset;
        self.selectedActivityMonths  = [];
        if(self.insurances != null && self.insurances.length > 0){
           self.insurance = self.insurance || self.insurances[0];
        }else  {
           self.insurance = {};
        }
         
        self.selectedReportMonths.push(self.reportMonths[0]);
        self.reportingMonths = [{month:'Jan',value:'01'},{month:'Feb',value:'02'},{month:'Mar',value:'03'},{month:'Apr',value:'04'},{month:'May',value:'05'},{month:'Jun',value:'06'},{month:'Jul',value:'07'},{month:'Aug',value:'08'},{month:'Sep',value:'09'},{month:'Oct',value:'10'},{month:'Nov',value:'11'},{month:'Dec',value:'12'}];
        self.selectedReportingMonths = [];
        
        self.displayTable = false;
        
        function dtInstanceCallback(dtInstance) {
	        self.dtInstance = dtInstance;
	    }
        
        
        function dt1InstanceCallback(dt1Instance) {
	        self.dt1Instance = dt1Instance;
	    }
        
        setProviders();
        self.dtColumns =[
        	DTColumnBuilder.newColumn('prvdr.name').withTitle('PROVIDER').withClass("text-left"),
 			DTColumnBuilder.newColumn('reportMonth').withTitle('DATAFILE').withClass("text-center"),
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
 			DTColumnBuilder.newColumn('reportMonth').withTitle('DATAFILE').withClass("text-center"),
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
        .withDOM('Bft')
		.withOption('bServerSide', true)
		.withOption('ordering', false)
	    .withOption('createdRow', createdRow)
		.withOption('bDeferRender', true)
		.withOption('scrollY', 450)
		.withOption('scrollX', 750)
		.withOption('bDestroy', true)
		.withFixedColumns({
							fixedColumns: {
							            heightMatch: 'auto',
					        			leftColumns: 3
					    				}
        			
    				     })
    	.withButtons([
            		   {
            extend: 'excelHtml5',
            text: 'Save as Excel',
            customize: function( xlsx ) {
                var sheet = xlsx.xl.worksheets['sheet1.xml'];
                $('row:first c', sheet).attr( 's', '42' );
            }
        }
					  ]
					)
		.withFnServerData(serverData);
        
        
        function serverData(sSource, aoData, fnCallback) {
			
            var insId =  (self.insurance === undefined || self.insurance === null)? 0 : self.insurance.id;
            var prvdrIds = (self.selectedPrvdrs === undefined || self.selectedPrvdrs === null)? 0 :  self.selectedPrvdrs.map(a => a.id);
            var prvdrIdss = prvdrIds.join();
            var activityMonths = self.selectedActivityMonths.join() ;
            var isSummary = false;
            var reportMonths = self.selectedReportMonths.join();
            var search = aoData[5].value;
			
			// Then just call your service to get the
			// records from server side
			 NewMedicalLossRatioService
				.loadNewMedicalLossRatios( insId,prvdrIdss, reportMonths, activityMonths, isSummary,search.value)
					.then(
							function(result) {
							self.finalData1  = result.data.content;
								var records = {
									'recordsTotal' : result.data.totalElements||0,
									'recordsFiltered' : result.data.totalElements||0,
									'data' : self.finalData1||{}
								};
								fnCallback(records);
							});
		}



		self.dt1Options = DTOptionsBuilder.newOptions()
		 .withDOM('Bft')
		.withOption('bServerSide', true)
		.withOption('ordering', false)
		.withOption('createdRow', createdRow)
		.withOption('bDeferRender', true)
		.withOption('bDestroy', true)
		.withOption('scrollY', 450)
		.withOption('scrollX', 750)
		.withFixedColumns({
		                   heightMatch: 'none',
        				   leftColumns: 4,
        				   rightColumns: 1
    						})
    	.withButtons([
					   {
			            extend: 'excelHtml5',
			            text: 'Save as Excel',
			            customize: function( xlsx ) {
			                		var sheet = xlsx.xl.worksheets['sheet1.xml'];
			                		$('row:first c', sheet).attr( 's', '42' );
			                       }
                        }
					  ]
					)
		.withFnServerData(serverData1);

        function serverData1(sSource, aoData, fnCallback) {
			
            var insId =  (self.insurance === undefined || self.insurance === null)? 0 : self.insurance.id;
            var prvdrIds = (self.selectedPrvdrs === undefined || self.selectedPrvdrs === null)? 0 :  self.selectedPrvdrs.map(a => a.id);
            var prvdrIdss = prvdrIds.join();
            var isSummary = true;
            var reportMonths = self.selectedReportMonths.join();
			 var activityMonths = self.selectedActivityMonths.join() ;
			 
			// Then just call your service to get the
			// records from server side
			 NewMedicalLossRatioService
				.loadNewMedicalLossRatios( insId, prvdrIdss, reportMonths, activityMonths, isSummary)
					.then(
							function(result) {
								self.finalData  = result.data.content;
								var rowTotals = {};
								if( result.data.content != null && result.data.content.length > 0){
									var keys = Object.keys(result.data.content[0]);
									keys.forEach( function ( key) {
									var keyTotal = $filter('sumByKey')(result.data.content, key);
										 switch(key) {
										  case  'activityMonth' : rowTotals[key] = 'Total' ; break;
										  case  'reportMonth' : rowTotals[key]  = result.data.content[0]['reportMonth'];  break;
										  case  'mlr' : rowTotals[key] =  (rowTotals['totalExp'] / rowTotals['funding'] *100).toFixed(2);  break;
										  case  'qmlr' : rowTotals[key] =  (rowTotals['totalExp'] / rowTotals['funding']*100 ).toFixed(2);  break;
										  default : rowTotals[key] = keyTotal.toFixed(2);
										 }
									
									});
									console.log('rowTotals:',rowTotals);
								}
								if(Object.keys(rowTotals).length > 0){
									self.finalData.push(rowTotals);
								}
							  	
								var records = {
									'recordsTotal' : result.data.totalElements||0,
									'recordsFiltered' : result.data.totalElements||0,
									'data' : self.finalData||{}
								};
								fnCallback(records);
							});
		}
        
       function createdRow(row, data, dataIndex) {
            // Recompiling so we can bind Angular directive to the DT
            $compile(angular.element(row).contents())($scope);
        }
        
       
       function generate(){
       self.selectedActivityMonths = [];
       self.selectedReportingYears.forEach(function(reportYear){
        
		        if(self.selectedReportingQuarters != null && self.selectedReportingQuarters.length> 0){
			         self.selectedReportingQuarters.forEach(function(reportingQuarter){
			            reportingQuarter.months.forEach(function(reportMonth) {
			            self.selectedActivityMonths.push(''+reportYear+reportMonth);
			            });
			         });
		        } else if(self.selectedReportingMonths != null && self.selectedReportingMonths.length> 0){
		        	self.selectedReportingMonths.forEach(function(reportMonth) {
			            self.selectedActivityMonths.push(''+reportYear+reportMonth.value);
			            });
		        }
         
        }) ;
       
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
        
         function getAllReportingYears() {
        	self.reportingYears = NewMedicalLossRatioService.getAllReportingYears();
        	console.log('self.reportingYears', self.reportingYears);
        	return self.reportingYears;
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
        	self.providers.forEach(function(prvdr){self.selectedPrvdrs.push(prvdr) });
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
        
        function isChecked(item){
							      var match = false;
							      for(var i=0 ; i < self.selectedReportingYears.length; i++) 
							        if(self.selectedReportingYears[i] == item){
							           match = true;
							          break;
							        }
							      return match;
							  };
							  
							  
         function sync(bool, item){
								  reset();
								    if(bool){
								    	self.selectedReportingYears.push(item);
								    } else {
								      // remove item
								      for(var i=0 ; i < self.selectedReportingYears.length; i++) {
								        if(self.selectedReportingYears[i] == item){
								        	self.selectedReportingYears.splice(i,1);
								        }
								      }      
								    }
								  };
								  
		function syncQuarters(bool, item){
		 reset();
								    if(bool){
								    	self.selectedReportingQuarters.push(item);
								    	self.reportingMonths.forEach(function(reportingMonth){
								    	 syncMonths(false, reportingMonth);
								    	  });
								    } else {
								      // remove item
								      for(var i=0 ; i < self.selectedReportingQuarters.length; i++) {
								        if(self.selectedReportingQuarters[i].quarter == item.quarter){
								        	self.selectedReportingQuarters.splice(i,1);
								        }
								      }      
								    }
								  };
								  
         function isCheckedQuarter(item){
							      var match = false;
							      for(var i=0 ; i < self.selectedReportingQuarters.length; i++) 
							        if(self.selectedReportingQuarters[i].quarter == item.quarter){
							           match = true;
							          break;
							        }
							      return match;
							  };
		
		function syncMonths(bool, item){
								  reset();
								    if(bool){
								    	self.selectedReportingMonths.push(item);
								    	 self.reportingQuarters.forEach(function(reportingQuarterrr){
								    	    syncQuarters(false,reportingQuarterrr);
								    	  });
								    } else {
								      // remove item
								      for(var i=0 ; i < self.selectedReportingMonths.length; i++) {
								        if(self.selectedReportingMonths[i].month == item.month){
								        	self.selectedReportingMonths.splice(i,1);
								        }
								      }      
								    }
								  };
								  
         function isCheckedMonth(item){
							      var match = false;
							      for(var i=0 ; i < self.selectedReportingMonths.length; i++) 
							        if(self.selectedReportingMonths[i].month == item.month){
							           match = true;
							          break;
							        }
							      return match;
							  };
							  
    }
    
 					
    ]);
   })();
   
