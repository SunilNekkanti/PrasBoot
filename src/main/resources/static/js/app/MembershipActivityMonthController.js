(function(){
'use strict';
var app = angular.module('my-app');

app.controller('MembershipActivityMonthController',
    ['MembershipService','ProviderService',  'InsuranceService','$scope', '$compile','$state','$stateParams', '$filter' ,'$localStorage','DTOptionsBuilder', 'DTColumnBuilder', function(MembershipService, ProviderService,  InsuranceService, $scope,$compile,$state,$stateParams, $filter,$localStorage, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.membershipActivityMonth = {};
        self.membershipActivityMonths = [];
        self.prvdrs=[];
        self.display =false;
        self.effectiveYear = $filter('date')(new Date($localStorage.loginUser.effectiveYear+'-01-01T06:00:00Z') ,'yyyy');
        self.displayEditButton = false;
        self.insurances=[];
        self.getAllMembershipActivityMonths = getAllMembershipActivityMonths;
        self.dtInstance = {};
        self.mbrId = null;
		self.prvdrId = null;
        self.reset = reset;
        self.getAllInsurances = getAllInsurances;
        self.getAllProviders = getAllProviders;
        self.membershipEdit = membershipEdit;
        self.getEffectiveYears = getEffectiveYears; 
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.insurances = getAllInsurances();
        self.prvdrs = getAllProviders();
        self.generate = generate;
        self.effectiveYears = getEffectiveYears();
        self.dt1InstanceCallback = dt1InstanceCallback;
        
        function dt1InstanceCallback(dt1Instance) {
	        self.dt1Instance = dt1Instance;
	    }
        
        
    	self.dtColumns = [
    		DTColumnBuilder.newColumn('firstName').withTitle('FIRST_NAME').withClass("text-left"),
            DTColumnBuilder.newColumn('lastName').withTitle('LAST_NAME').withClass("text-left"),
            DTColumnBuilder.newColumn('mbrProviderList[0].prvdr.name').withTitle('PROVIDER').withClass("text-left"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('JAN').renderWith( isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('FEB').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('MAR').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('APR').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('MAY').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('JUN').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('JUL').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('AUG').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('SEP').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('OCT').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('NOV').renderWith(isCapIsRoster).withClass("text-center"),
			DTColumnBuilder.newColumn('mbrActivityMonthList').withTitle('DEC').renderWith(isCapIsRoster).withClass("text-center")
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
	    .withDOM('ftrip<"clear">')
        .withPaginationType('full_numbers')
        .withOption('ordering', true)
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
            var prvdrId = (self.prvdr === undefined || self.prvdr === null)? 0 :  self.prvdr.id;
            var effectiveYear = self.effectiveYear; 

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
				MembershipService
						.loadMemberships(page, length, search.value, sortCol+','+sortDir,insId,prvdrId, effectiveYear)
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
        
       
       function generate(){
              self.dtInstance.rerender();
       }


        function getAllProviders(){
             self.prvdrs = ProviderService.getAllProviders();
   
            return self.prvdrs;
        }

        function getAllMembershipActivityMonths(){
            self.membershipActivityMonths = MembershipActivityMonthService.getAllMembershipActivityMonths();
  
           return self.memberships;
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
            self.successMessage='';
            self.errorMessage='';
            self.membership={};
            $scope.myForm.$setPristine(); //reset Form
        }
        
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.provider={};
            self.display = false;
            $state.go('main.membership');
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
  
       function   isCapIsRoster(data, type, full, meta ) {
    	  var activityMonth =  (meta.col -1 < 10 ) ? self.effectiveYear+'0'+(meta.col-1):self.effectiveYear+''+(meta.col-1);
		       var  mbrActivityMonthList  =    $filter('filter')(data, { activityMonth: activityMonth  });
		           if(mbrActivityMonthList !== undefined && mbrActivityMonthList.length > 0 )
		        	   return ((mbrActivityMonthList[0].isCap =='Y')?'C':'').concat( (mbrActivityMonthList[0].isRoster =='Y')?'R':'');
		           else 
		        	   return '';
       }
       
       
       
    }

    ]);
   })();