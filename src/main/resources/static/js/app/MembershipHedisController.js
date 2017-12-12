(function(){
'use strict';
var app = angular.module('my-app');

app.controller('MembershipHedisController',
    [ 'MembershipHedisService','ProviderService',  'InsuranceService', 'HedisMeasureRuleService', 'MedicalLossRatioService','$scope', '$compile', '$filter' ,'$localStorage','$modal','$log','DTOptionsBuilder', 'DTColumnBuilder', 
    	function( MembershipHedisService, ProviderService,  InsuranceService, HedisMeasureRuleService,MedicalLossRatioService, $scope,$compile,  $filter,$localStorage,$modal,$log, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.display =false;
        self.effectiveYear = $filter('date')(new Date($localStorage.loginUser.effectiveYear+'-01-01T06:00:00Z') ,'yyyy');
        self.displayEditButton = false;
        self.selectedHedisRules = [];
        self.isCaps = [{id:'Y',name:'Yes'}, {id:'N',name:'No'}];
        self.selectedCaps =[];
        self.insurance = {};
        self.insurance.id=0;
        self.selectedPrvdrs = [];
        self.isRosters = [{id:'Y',name:'Yes'}, {id:'N',name:'No'}];
        self.statuses = [{id:'Pending',name:'Pending'}, {id:'Completed',name:'Completed'}];
        self.selectedRosters =[];
        self.selectedProviders = [];
        self.selectedStatuses = [];
        self.startDate =null;
        self.endDate = null;
        self.insurances=[];
        self.selectedReportMonth =null;
		self.prvdrId = null;
        self.reset = reset;
        self.getAllInsurances = getAllInsurances;
        self.getAllProviders = getAllProviders;
        self.getAllReportMonths = getAllReportMonths;
        self.insurances = getAllInsurances();
        self.getAllHedisMeasureRules = getAllHedisMeasureRules;
        self.setProviders = setProviders;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.prvdrs = getAllProviders();
        self.providers = [];
        self.generate = generate;
        self.displayTable = false;
        self.reportMonths = getAllReportMonths();
        self.hedisMeasureRules = getAllHedisMeasureRules();
        self.hedisMeasures =  [];
        self.mbrId =0;
        self.activityMonth =0;
        self.finalData = [];
        self.dtInstance = {};
        self.dtInstanceCallback = dtInstanceCallback;
        self.setColumnHeaders = setColumnHeaders;
        self.dtColumns = [];
        self.dtRows = [];
        self.open = open;
        
        
        self.dtStaticColumns = [
            DTColumnBuilder.newColumn('0').withTitle('ACTION').renderWith(
					function(data, type, full,
							meta) {
						self.dtRows[meta.row]= full;
						if(self.dtRows[meta.row][8] === 'Y'){
							return '<a href="javascript:void(0)"  ng-click="ctrl.open(ctrl.dtRows['+meta.row+'])"><span class="glyphicon glyphicon-pencil"> <span class="glyphicon glyphicon-ok"> </span></span></a>';
						}
						 return '<a href="javascript:void(0)"  ng-click="ctrl.open(ctrl.dtRows['+meta.row+'])"><span class="glyphicon glyphicon-pencil"></span></a>';
					}).withClass("text-left"),
			DTColumnBuilder.newColumn('1').withTitle('LAST NAME').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('2').withTitle('FIRST NAME').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('3').withTitle('Birthday').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('4').withTitle('GENDER').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('5').withTitle('COUNT').withOption('defaultContent', '').notVisible(),
			DTColumnBuilder.newColumn('6').withTitle('PROVIDER').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('7').withTitle('PHONE#').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('8').withTitle('RECENT').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('9').withTitle('MbrId').withOption('defaultContent', '').notVisible(),
			DTColumnBuilder.newColumn('10').withTitle('ruleId').withOption('defaultContent', '').notVisible()
          ];
        
        
		 self.dtOptions = DTOptionsBuilder.newOptions()
		 .withOption('bServerSide', true)
		 .withOption('responsive', true)
         .withDisplayLength(20)
         .withDOM('ftrip')
		 .withOption('bSort', false)
		 .withOption('searchDelay', 1000)
		 .withOption('bProcessing', true)
		 .withOption('bPaginate', true)
		 .withPaginationType('full_numbers')
	     .withOption('createdRow', createdRow)
	     .withOption('ordering', true)
		 .withOption('bDeferRender', true)
		 .withOption('bDestroy', true)
		 .withFnServerData(serverData);
		 
        function dtInstanceCallback(dtInstance) {
	        self.dtInstance = dtInstance;
	    }
        
        function serverData(sSource, aoData, fnCallback) {
			
			// All the parameters you need is in the aoData
			// variable
			var order = aoData[2].value;
			var page = (aoData[3].value / aoData[4].value) ;
			var length = aoData[4].value ;
			var search = aoData[5].value;
            var insId =  (self.insurance === undefined || self.insurance === null)? 0 : self.insurance.id;
            var prvdrId = (self.prvdr === undefined || self.prvdr === null)? 0 :  self.prvdr.id;
            var hedisRules = ( self.selectedHedisRules === undefined || self.selectedHedisRules === null)? 0 :  self.selectedHedisRules.map(a => a.id);
            var hedisRuless =   hedisRules.join() ;
            var statuses = ( self.selectedStatuses === undefined || self.selectedStatuses === null)? 0 :  self.selectedStatuses.map(a => a.id); 
            var statusess=  statuses.join() ;
            var caps = ( self.selectedCaps === undefined || self.selectedCaps === null)? 0 :  self.selectedCaps.map(a => a.id);
            var capss =   caps.join() ;
            var rosters = ( self.selectedRosters === undefined || self.selectedRosters === null)? 0 :  self.selectedRosters.map(a => a.id);
            var rosterss =  rosters.join() ;
            var startDate =  moment(self.startDate, "MM/DD/YYYY").format('YYYY-MM-DD') ;
            var endDate =  moment(self.endDate, "MM/DD/YYYY").format('YYYY-MM-DD') ;
            self.finalData = [];
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
			 MembershipHedisService
				.loadMembershipHedis(page, length, search.value, insId, prvdrId, hedisRuless,self.selectedReportMonth,  startDate, endDate,capss,rosterss,statusess)  
				.then(
						function(result) {
                        var resultData = result.data ; 
							
                    	angular.forEach(resultData, function(value1, key1){
								var resultData1 = value1.reduce(function(result1, item, index, array) {
 								  result1[index] = item; //a, b, c
 								  return result1;
 								}, {});
								self.finalData.push(resultData1);
							});
							
			     
						var records = {
							'recordsTotal' : self.finalData[1][5]||0,
							'recordsFiltered' : self.finalData[1][5]||0,
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
    	   
    	  self.displayTable =true;
    	  if(!angular.equals(self.dtInstance, {}) ) { self.dtInstance.rerender();}
           		    	   
       }


        function getAllProviders(){
             self.prvdrs = ProviderService.getAllProviders();
   
            return self.prvdrs;
        }

        
        function setProviders(){
        	self.selectedProviders = [];
            self.selectedClaimss = [];
             
        	self.providers =   $filter('filter')(self.prvdrs, {refInsContracts:[{ins:{id:self.insurance.id}}]});
        	self.providers =   $filter('orderBy')(self.providers, 'name');
        	
        	self.Claimss =   $filter('filter')(self.pbms, {insId:{id:self.insurance.id}});
          	self.Claimss =   $filter('orderBy')(self.Claimss, 'description');
          	getAllHedisMeasureRules();
        }
        
        
        
        function getAllInsurances() {
			return InsuranceService.getAllInsurances();
		}
        
        
        function reset(){
        	self.finalData=[];
            self.displayTable =false;
        }
        
       
       
       function getAllReportMonths() {
       	self.reportMonths = MedicalLossRatioService.getAllReportMonths();
       	return self.reportMonths;
		}
       
        
        function getAllHedisMeasureRules(){
        	var hedisMeasureRules =  HedisMeasureRuleService.getAllHedisMeasureRules();
        	self.hedisMeasureRules =   $filter('filter')(hedisMeasureRules, {insId:{id:self.insurance.id}});
        	return self.hedisMeasureRules;
        }
       
        function setColumnHeaders(){
           self.dtColumns = [];
           angular.copy(self.dtStaticColumns, self.dtColumns);
       	   angular.forEach(self.selectedHedisRules, function(value, key){
   			     self.dtColumns.push( DTColumnBuilder.newColumn((key+11)).withTitle(value.shortDescription).withClass('text-center').withOption('defaultContent', ''));
   			 });
       	   reset();
        }
        
        
        function  open( mbrHedisMeasure) {
            var modalInstance = $modal.open({
              templateUrl: 'myModalContent.html',
              controller: 'HedisReportModalInstanceController',
              size: 'lg',
              resolve: {
		            	  mbrId :function () { return mbrHedisMeasure['0']; },
		            	  hedisRules: function () { 
		            		  var hedisRules = ( self.selectedHedisRules === undefined || self.selectedHedisRules === null)? 0 :  self.selectedHedisRules.map(a => a.id);
		                      var hedisRuless =   hedisRules.join() ;
		            		  return hedisRuless; }
                       } 
              } );
            
            modalInstance.result.then(function () {  },
            		                  function () {$log.info('Modal dismissed at: ' + new Date());});
            
          }
        
    }

    ]);
})();