(function(){
'use strict';
var app = angular.module('my-app');
app.controller('MembershipProblemController',
    ['MembershipService','ProviderService',  'InsuranceService','ProblemService', '$scope', '$compile','$state','$stateParams', '$filter' ,'$localStorage','DTOptionsBuilder', 'DTColumnBuilder', function(MembershipService, ProviderService,  InsuranceService,ProblemService, $scope,$compile,$state,$stateParams, $filter,$localStorage, DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.membershipProblem = {};
        self.membershipProblems = [];
        self.display =false;
        self.effectiveYear = $filter('date')(new Date($localStorage.loginUser.effectiveYear+'-01-01T06:00:00Z') ,'yyyy');
        self.displayEditButton = false;
        self.selectedProviders = [];
        self.selectedProblems = [];
        self.insurances=[];
        self.getAllMembershipProblems = getAllMembershipProblems;
        self.dtInstance = {};
        self.mbrId = null;
		self.prvdrId = null;
        self.reset = reset;
        self.getAllInsurances = getAllInsurances;
        self.getAllProviders = getAllProviders;
        self.getEffectiveYears = getEffectiveYears; 
        self.setProviders = setProviders;
        self.cancelEdit = cancelEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.insurances = getAllInsurances();
        self.prvdrs = getAllProviders();
        self.providers = [];
        self.pbms = getAllProblems();
        self.generate = generate;
        self.effectiveYears = getEffectiveYears();
        self.displayTable = false;
        self.setColumnHeaders = setColumnHeaders;
        
        self.dtColumns =[   ];
        
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
		.withOption('bDestroy', true)
		.withFnServerData(serverData);

    	function serverData(sSource, aoData, fnCallback) {
    		
    		var page = ( !angular.equals(self.dtInstance, {}) )  ? aoData[3].value / aoData[4].value :1 ;
    		var length = ( !angular.equals(self.dtInstance, {}) )  ?  aoData[4].value :20 ;
    		var search =( !angular.equals(self.dtInstance, {}) )  ?  aoData[5].value :'';
            var insId =  (self.insurance === undefined || self.insurance === null)? 0 : self.insurance.id;
            var prvdrId = (self.prvdr === undefined || self.prvdr === null)? 0 :  self.prvdr.id;
            var problemIds = (self.selectedProblems === undefined || self.selectedProblems === null)? 0 :  self.selectedProblems.map(pbm => pbm.id);
            var effectiveYear = self.effectiveYear; 
            var sortCol ='';
      		var sortDir ='';
      		
           if( !angular.equals(self.dtInstance, {}) ){
        	   var paramMap = {};
       		for ( var i = 0; i < aoData.length; i++) {
       		  paramMap[aoData[i].name] = aoData[i].value;
       		}
       		
       		// extract sort information
       		 if(paramMap['columns'] !== undefined && paramMap['columns'] !== null && paramMap['order'] !== undefined && paramMap['order'] !== null ){
       			 sortCol = paramMap['columns'][paramMap['order'][0]['column']].data;
       			  sortDir = paramMap['order'][0]['dir'];
       		 }
           }
    		 
    			
    	// Then just call your service to get the
    	// records from server side
    	MembershipService
    			.loadMemberships(page, length, search.value, sortCol+','+sortDir,insId,prvdrId, effectiveYear,problemIds)
    			.then(
    					function(result) {
    						var records = {
    							'recordsTotal' : result.data.totalElements||0,
    							'recordsFiltered' : result.data.totalElements||0,
    							'data' : result.data.content||{}
    								};
    						fnCallback(records);
    						 self.displayTable = true;
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
		 self.displayTable = true;
	 
		  if( !angular.equals(self.dtInstance, {}) ) {
			  self.dtInstance.rerender();
		   }
       }


        function getAllProviders(){
             self.prvdrs = ProviderService.getAllProviders();
   
            return self.prvdrs;
        }

        function getAllProblems(){
            self.problems = ProblemService.getAllProblems();
           return self.problems;
       }
        
        function setProviders(){
        	self.selectedProviders = [];
            self.selectedProblems = [];
             
        	self.providers =   $filter('filter')(self.prvdrs, {refInsContracts:[{ins:{id:self.insurance.id}}]});
        	self.providers =   $filter('orderBy')(self.providers, 'name');
        	
        	self.problems =   $filter('filter')(self.pbms, {insId:{id:self.insurance.id}});
          	self.problems =   $filter('orderBy')(self.problems, 'description');
        	
        }
        
        
        function getAllMembershipProblems(){
            self.membershipProblems = MembershipProblemService.getAllMembershipProblems();
  
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
  
       function   problemDisplayHTML(data, type, full, meta ) {
    	 	
    	  var problemDesc =  meta.settings.aoColumns[meta.col].sTitle;
          var  mbrProblemList  =    $filter('filter')(data, { pbm:{description: problemDesc}  });
           if(mbrProblemList !== undefined && mbrProblemList.length > 0 )
        	   return 'Y';
           else 
        	   return '';
       }
       
       function   providerNameHTML(data, type, full, meta ) {
 		           if(data !== undefined && data.length > 0 ){
 		        	   return  data[0].prvdr.name ;
 		           }
 		           else 
 		        	   return '';
        }
       
       function setColumnHeaders(){
    	   self.displayTable = false;
    		 self.dtColumns =[
				   DTColumnBuilder.newColumn('mbrProviderList').withTitle('PROVIDER').renderWith(providerNameHTML).withClass("text-left"),
	   		       DTColumnBuilder.newColumn('firstName').withTitle('FIRST_NAME').withClass("text-left"),
	              DTColumnBuilder.newColumn('lastName').withTitle('LAST_NAME').withClass("text-left")
              ];

			angular.forEach(self.selectedProblems, function(value, key){
			self.dtColumns.push( DTColumnBuilder.newColumn('mbrProblemList').withTitle(value.description.toUpperCase()).renderWith(problemDisplayHTML).withClass("text-center"));
			});
       }
        
       
    }

    ]);
   })();