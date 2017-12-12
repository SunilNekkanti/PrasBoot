(function(){
'use strict';
var app = angular.module('my-app');

app.controller('MembershipClaimsController',
    ['MembershipClaimsService','ProviderService',  'InsuranceService', 'RiskReconService', 'MedicalLossRatioService','$scope', '$compile','$state','$stateParams', '$filter' ,'$localStorage','DTOptionsBuilder', 'DTColumnBuilder', function(MembershipClaimsService, ProviderService,  InsuranceService, RiskReconService,MedicalLossRatioService, $scope,$compile,$state,$stateParams, $filter,$localStorage, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.display =false;
        self.effectiveYear = $filter('date')(new Date($localStorage.loginUser.effectiveYear+'-01-01T06:00:00Z') ,'yyyy');
        self.displayEditButton = false;
        self.claimTypes = [{id:'INST'}, {id:'PHAR'}, {id:'PROF'}];
        self.selectedClaimTypes = [];
        self.isCaps = [{id:'Y'}, {id:'N'}];
        self.selectedCaps =[];
        self.isRosters = [{id:'Y'}, {id:'N'}];
        self.selectedRosters =[];
        self.selectedProviders = [];
        self.insurances=[];
        self.selectedCategories=[];
        self.selectedReportMonths =[];
        self.mbrId = null;
		self.prvdrId = null;
        self.reset = reset;
        self.getAllInsurances = getAllInsurances;
        self.getAllProviders = getAllProviders;
        self.getAllRiskCategories = getAllRiskCategories;
        self.getAllReportMonths = getAllReportMonths;
        self.insurances = getAllInsurances();
        self.setProviders = setProviders;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.prvdrs = getAllProviders();
        self.providers = [];
        self.generate = generate;
        self.displayTable = false;
        self.displayTable1 = false;
        self.displayTable2 = false;
        self.displayTable3 = false;
        self.reportMonths = getAllReportMonths();
        self.categories = getAllRiskCategories();
        self.levelNo = 1;
        self.mbrId =0;
        self.activityMonth =0;
        self.rowsInfo=[];
        self.table1RowsInfo = [];
        self.table2RowsInfo = [];
        self.goTolevel2Tab = goTolevel2Tab;
        self.goTolevel3Tab = goTolevel3Tab;
        self.goTolevel4Tab = goTolevel4Tab;
        self.activateLevel1 = activateLevel1;
        self.activateLevel2 = activateLevel2;
        self.activateLevel3 = activateLevel3;
        self.finalData = [];
        self.dtInstance = {};
        self.dt1Instance = {};
        self.dt2Instance = {};
        self.dt3Instance = {};
        self.maxReportMonth = (self.reportMonths && self.reportMonths.length >0)? self.reportMonths[0]:null;
        
        self.dtInstanceCallback = dtInstanceCallback;
        self.dt1InstanceCallback = dt1InstanceCallback;
        self.dt2InstanceCallback = dt2InstanceCallback;
        self.dt3InstanceCallback = dt3InstanceCallback;
        
        function dtInstanceCallback(dtInstance) {
	        self.dtInstance = dtInstance;
	    }
        
        function dt1InstanceCallback(dt1Instance) {
	        self.dt1Instance = dt1Instance;
	    }

        function dt2InstanceCallback(dt2Instance) {
	        self.dt2Instance = dt2Instance;
	    }
        
        function dt3InstanceCallback(dt3Instance) {
	        self.dt3Instance = dt3Instance;
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
        
       
       function generate(){
   	   self.displayTable1 = false;
   	   self.displayTable2 = false;
   	   self.displayTable3 = false;
   	   
   	// All the parameters you need is in the aoData  variable
   	   
  			  var insId =  (self.insurance === undefined || self.insurance === null)? 0 : self.insurance.id;
                var prvdrIds = (self.selectedPrvdrs === undefined || self.selectedPrvdrs === null)? 0 :  self.selectedPrvdrs.map(a => a.id);
                var prvdrIdss = self.selectedPrvdrId || prvdrIds.join();
                var claimTypes = ( self.selectedClaimTypes === undefined || self.selectedClaimTypes === null)? 0 :  self.selectedClaimTypes.map(a => a.id);
                var claimTypess = '\''+claimTypes.join()+'\'';
                //var categories = (self.selectedCategories === undefined || self.selectedCategories === null)? 0 :  self.selectedCategories.map(a => a.id);
                var categoriess = self.category||self.selectedCategories.join();
                categoriess= '\''+categoriess+'\'';
                var caps = ( self.selectedCaps === undefined || self.selectedCaps === null)? 0 :  self.selectedCaps.map(a => a.id);
                var capss =  '\''+caps.join()+'\'';
                var rosters = ( self.selectedRosters === undefined || self.selectedRosters === null)? 0 :  self.selectedRosters.map(a => a.id);
                var rosterss =  '\''+rosters.join()+'\'';
                var mbrId =  self.selectedMbrId || self.mbrId;
    			// Then just call your service to get the
    			// records from server side
    			MembershipClaimsService
    					.loadMembershipClaims( insId, prvdrIdss, mbrId ,claimTypess,categoriess, self.selectedReportMonth, self.activityMonth,capss,rosterss ,self.levelNo,self.maxReportMonth)
    					.then(
    							function(result) {
    								self.finalData[self.levelNo -1] = [];
                                   var headers = result.data[0];  
                                   var resultData = result.data.slice(1, result.data.length); 
    								
                               	angular.forEach(resultData, function(value1, key1){
  										var resultData1 = value1.reduce(function(result1, item, index, array) {
  		  								  result1[index] =  (item === null)?'':item; //a, b, c
  		  								  return result1;
  		  								}, {});
  										self.finalData[self.levelNo -1].push(resultData1);
  									});
    								
								 switch (self.levelNo) {
								 case 2:
									 self.dt1Columns =[ ];
									 angular.forEach(headers, function(value, key){
									    	if(key ==0 || key ==4 || key ==5){
  									    	   self.dt1Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left'));
 									       }else if(key == 9) {
 									    	  self.dt1Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').renderWith(level3LinkHTML).withOption('defaultContent', ''));
 									       }else{
 									    	  self.dt1Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center'));
 									       }
									   });
									 self.dt1Options = DTOptionsBuilder.newOptions()
	  							        .withOption('data', self.finalData[self.levelNo -1]) 
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
							                 self.displayTable1 = true;
								       break;
								    
								 case 3:
									 self.dt2Columns =[ ];
									 angular.forEach(headers, function(value, key){
									    	if(key ==0 || key ==4 || key ==5){
									    	   self.dt2Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left'));
									       }else if(key ==12){
									    	  self.dt2Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').renderWith(level4LinkHTML).withOption('defaultContent', ''));
									       }else{
									    	  self.dt2Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center'));
									       }
										});
									 self.dt2Options = DTOptionsBuilder.newOptions()
	  							        .withOption('data', self.finalData[self.levelNo -1]) 
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
										     self.displayTable1 = true;
							                 self.displayTable2 = true;
							            break;
								 case 4:
									 self.dt3Columns =[ ];
									 angular.forEach(headers, function(value, key){
									    	if(key ==0 || key ==4 || key ==5){
									    	   self.dt3Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left'));
									       }else{
									    	  self.dt3Columns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').withOption('defaultContent', ''));
									       }
										 });
									 self.dt3Options = DTOptionsBuilder.newOptions()
	  							        .withOption('data', self.finalData[self.levelNo -1]) 
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
										     self.displayTable1 = true;
										     self.displayTable2 = true;
							                 self.displayTable3 = true;
								        break;
								 default : 
									 self.dtColumns =[ ];
									 angular.forEach(headers, function(value, key){
										 if(key <=2){
									    	   self.dtColumns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left no-click').notVisible());
									       }else  if(key <=4){
 									    	   self.dtColumns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left no-click'));
									       }else{
									    	  self.dtColumns.push( DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').renderWith(level2LinkHTML).withOption('defaultContent', ''));
									     }
									 });
									 self.dtOptions = DTOptionsBuilder.newOptions()
 							        .withOption('data', self.finalData[0]) 
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
							    }
  			              });
    								 
  						    	   
    			if( !angular.equals(self.dtInstance, {}) ) {self.dtInstance.rerender();}
       }


        function getAllProviders(){
             self.prvdrs = ProviderService.getAllProviders();
   
            return self.prvdrs;
        }

        
        function setProviders(){
        	self.selectedProviders = [];
            self.selectedClaimss = [];
             
        	self.providers =   $filter('providerFilter')(self.prvdrs, self.insurance.id);
        	self.providers =   $filter('orderBy')(self.providers, 'name');
        	
        	self.Claimss =   $filter('filter')(self.pbms, {insId:{id:self.insurance.id}});
          	self.Claimss =   $filter('orderBy')(self.Claimss, 'description');
          	reset();
        }
        
        
        
        function getAllInsurances() {
			return InsuranceService.getAllInsurances();
		}
        
        
        
        function membershipEdit(id) {
        	var params = {'membershipDisplay':true};
			var trans =  $state.go('main.membership.edit',params).transition;
			trans.onSuccess({}, function() { editMembership(id)}, { priority: -1 });
			
			
        }
        
        function reset(){
        	self.finalData=[];
        	self.dtColumns=[];
        	self.dt1Columns=[];
        	self.dt2Columns=[];
        	self.dt3Columns=[];
        	self.selectedProvider =null;
      	    self.selectedMbrId =null;
      	    self.activityMonth =0;
   	        self.reportMonth =null;
   	        self.category =  null;
   	        self.displayTable =false;
   	        self.levelNo=1;
        }
        
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.provider={};
            self.display = false;
            $state.go('main.membership');
        }
       
       function getAllRiskCategories() {
			return MembershipClaimsService.getAllRiskCategories();
		}
       
       function getAllReportMonths() {
       	self.reportMonths = MedicalLossRatioService.getAllReportMonths();
       	return self.reportMonths;
		}
       
       function   level2LinkHTML(data, type, full, meta ) {
     	  var columnHeader =  meta.settings.aoColumns[meta.col].sTitle;
     	 self.rowsInfo[meta.row] = self.rowsInfo[meta.row]|| [];
     	 self.rowsInfo[meta.row][meta.col] =  self.rowsInfo[meta.row][meta.col]|| {};
    	 self.rowsInfo[meta.row][meta.col] = {activityMonth: columnHeader.replace('-',''), reportMonth:full[3].replace('-',''),category:full[4]} ;
    	return '<a href="javascript:void(0)"  ng-click="ctrl.goTolevel2Tab(ctrl.rowsInfo['+meta.row+']['+meta.col+'])">'+data+'</a>';
        }
        
       function goTolevel2Tab(colInfo){
    	   self.activityMonth =colInfo.activityMonth;
    	   self.reportMonth =colInfo.reportMonth;
    	   self.category =  colInfo.category;
    	   self.levelNo =2;
    	   generate();
    	   
       }
       
       function activateLevel1(){
    	   self.levelNo =1;
    	   self.displayTable1 =false;
    	   self.displayTable2 =false;
    	   self.displayTable3 =false;
       }
       function activateLevel2(){
    	   self.levelNo =2;
    	   self.displayTable1 =true;
    	   self.displayTable2 =false;
    	   self.displayTable3 =false;
       }
       function activateLevel3(){
    	   self.levelNo =3;
     	   self.displayTable1 =true;
     	   self.displayTable2 =true;
     	   self.displayTable3 =false;
        }
       
       function   level3LinkHTML(data, type, full, meta ) {
      	 self.table1RowsInfo[meta.row] = self.table1RowsInfo[meta.row]|| [];
      	 self.table1RowsInfo[meta.row][meta.col] =  self.table1RowsInfo[meta.row][meta.col]|| {};
     	 self.table1RowsInfo[meta.row][meta.col] = {activityMonth: full[3].replace('-',''), reportMonth:full[2].replace('-',''),prvdrId:full[1]} ;
     	return '<a href="javascript:void(0)"  ng-click="ctrl.goTolevel3Tab(ctrl.table1RowsInfo['+meta.row+']['+meta.col+'])">'+data+'</a>';
    	   }
       
       function goTolevel3Tab(colInfo){
    	   self.activityMonth =colInfo.activityMonth;
    	   self.reportMonth =colInfo.reportMonth;
    	   self.selectedPrvdrId = colInfo.prvdrId;
    	   self.levelNo =3;
    	   generate();
    	   
       }
       
       
       
       function   level4LinkHTML(data, type, full, meta ) {
       	  var columnHeader =  meta.settings.aoColumns[meta.col].sTitle;
       	 self.table2RowsInfo[meta.row] = self.table2RowsInfo[meta.row]|| [];
       	 self.table2RowsInfo[meta.row][meta.col] =  self.table2RowsInfo[meta.row][meta.col]|| {};
      	 self.table2RowsInfo[meta.row][meta.col] = {activityMonth: full[6].replace('-',''), reportMonth:full[5].replace('-',''),mbrId:parseInt(full[4]),prvdrId:full[3]} ;
      	return '<a href="javascript:void(0)"  ng-click="ctrl.goTolevel4Tab(ctrl.table2RowsInfo['+meta.row+']['+meta.col+'])">'+data+'</a>';
          }
        
        function goTolevel4Tab(colInfo){
     	   self.activityMonth =colInfo.activityMonth;
     	   self.reportMonth =colInfo.reportMonth;
     	   self.selectedPrvdrId = colInfo.prvdrId;
     	   self.selectedMbrId = colInfo.mbrId;
     	   self.levelNo =4;
     	   generate();
     	   
        }
        
        
    }

    ]);
   })();