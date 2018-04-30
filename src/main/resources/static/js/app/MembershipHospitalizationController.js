(function(){
'use strict';
var app = angular.module('my-app');

app.controller('MembershipHospitalizationController',
    [ 'MembershipHospitalizationService','ProviderService',  'InsuranceService', '$scope', '$compile', '$filter' ,'$localStorage','$modal','$log','DTOptionsBuilder', 'DTColumnBuilder', 
    	function( MembershipHospitalizationService, ProviderService,  InsuranceService,  $scope,$compile,  $filter,$localStorage,$modal,$log, DTOptionsBuilder, DTColumnBuilder) {

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
        self.insurances = getAllInsurances();
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
        self.hedisMeasures =  [];
        self.mbrId =0;
        self.activityMonth =0;
        self.finalData = [];
        self.dtInstance = {};
        self.setColumnHeaders = setColumnHeaders;
        self.dtColumns = [];
        self.dtRows = [];
        self.open = open;
        
        
        self.dtColumns = [
            DTColumnBuilder.newColumn('0').withTitle('ACTION').renderWith(
					function(data, type, full,
							meta) {
						self.dtRows[meta.row]= full;
						if(self.dtRows[meta.row][8] === 'Y'){
							return '<a href="javascript:void(0)"  ng-click="ctrl.open(ctrl.dtRows['+meta.row+'])"><span class="glyphicon glyphicon-pencil"> <span class="glyphicon glyphicon-ok"> </span></span></a>';
						}
						 return '<a href="javascript:void(0)"  ng-click="ctrl.open(ctrl.dtRows['+meta.row+'])"><span class="glyphicon glyphicon-pencil"></span></a>';
					}).withClass("text-left"),
			DTColumnBuilder.newColumn('mbr.medicareNo').withTitle('REMARKS').withClass("text-left").withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('mbr.firstName').withTitle('FIRST NAME').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('mbr.lastName').withTitle('LAST NAME').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('mbr.contact.homePhone').withTitle('Pt.PHONE').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('prvdr.name').withTitle('PROVIDER').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('erNoOfVisits').withTitle('#ER_VISITS').withOption('defaultContent', '').withClass("text-center"),
			DTColumnBuilder.newColumn('erVisitDate').withTitle('ER_VISIT').withOption('defaultContent', '').withClass("text-center"),
			DTColumnBuilder.newColumn('erLastVisitDate').withTitle('LAST_ER_VISIT').withOption('defaultContent', '').withClass("text-center"),
			DTColumnBuilder.newColumn('facilityName').withTitle('FACILITY').withOption('defaultContent', ''),
			DTColumnBuilder.newColumn('potentiallyAvoidableERVisit').withTitle('IS_ER_AVOIDABLE').withOption('defaultContent', '').withClass("text-center"),
			DTColumnBuilder.newColumn('primaryDiagnosis').withTitle('DIAGNOSIS').withOption('defaultContent', '') 
          ];
        
        
		 self.dtOptions = DTOptionsBuilder.newOptions()
		 .withOption('bServerSide', true)
		 .withOption('responsive', true)
         .withDisplayLength(20)
         .withDOM('ftrip')
		 .withOption('bSort', false)
		 .withOption('searchDelay', 1000)
		 .withOption('bProcessing', true)
		 .withOption('bStateSave', true) 
		 .withOption('bPaginate', true)
		 .withPaginationType('full_numbers')
	     .withOption('createdRow', createdRow)
	     .withOption('ordering', true)
		 .withOption('bDeferRender', true)
		 .withOption('bDestroy', true)
		 .withOption('order', [
          [1, 'ASC'],
          [2, 'ASC']
        ])
		 .withFnServerData(serverData);
		 
        
        function serverData(sSource, aoData, fnCallback) {
			
			// All the parameters you need is in the aoData
			// variable
			var order = aoData[2].value;
			var page = (aoData[3].value / aoData[4].value) ;
			var length = aoData[4].value ;
			var search = aoData[5].value;
            var insIds =  (self.selectedInsurances === undefined || self.selectedInsurances.length === 0)? 0 : self.selectedInsurances.map(a => a.id).join();
            var prvdrIds = (self.selectedPrvdrs === undefined || self.selectedPrvdrs.length ==0)? 0 :  self.selectedPrvdrs.map(a => a.id).join();
		
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
			 MembershipHospitalizationService
				.loadMembershipHospitalizations(page, length, search.value, sortCol+','+sortDir,insIds, prvdrIds)  
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
       
       function createdRow(row, data, dataIndex) {
            // Recompiling so we can bind Angular directive to the DT
            $compile(angular.element(row).contents())($scope);
        }
        
       
       
       
       function generate(){
    	   
    	// self.displayTable =true;
    	 // if(!angular.equals(self.dtInstance, {}) ) { self.dtInstance.rerender();}
    	 self.dtInstance.rerender();
           		    	   
       }


        function getAllProviders(){
             self.prvdrs = ProviderService.getAllProviders();
            return self.prvdrs;
        }

        
        function setProviders(){
        	self.selectedProviders = [];
        	self.providers =   $filter('providerFilterByInsurances')(self.prvdrs, self.selectedInsurances);
        	self.providers =   $filter('orderBy')(self.providers, 'name');
        }
        
        
        
        function getAllInsurances() {
			return InsuranceService.getAllInsurances();
		}
        
        
        function reset(){
        	self.finalData=[];
            self.displayTable =false;
        }
        
       
        function setColumnHeaders(){
           self.dtColumns = [];
           angular.copy(self.dtStaticColumns, self.dtColumns);
       	   angular.forEach(self.selectedHedisRules, function(value, key){
   			     self.dtColumns.push( DTColumnBuilder.newColumn((key+11)).withTitle(value.shortDescription).withClass('text-center').withOption('defaultContent', ''));
   			 });
       	   reset();
        }
        
        
        function  open( mbrHospitalization) {
            var modalInstance = $modal.open({
              templateUrl: 'myModalContent.html',
              controller: 'MembershipHospitalizationModalInstanceController',
              size: 'lg',
              resolve: {
		            	  mbrId :function () { return mbrHospitalization.mbr.id; }
                       } 
              } );
            
            modalInstance.result.then(function () { self.dtInstance.rerender();   },
            		                  function () {$log.info('Modal dismissed at: ' + new Date());});
            
          }
        
    }

    ]);
})();