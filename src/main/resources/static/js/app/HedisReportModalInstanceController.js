(function(){
'use strict';
var app = angular.module('my-app');

app.controller('HedisReportModalInstanceController',
    [  'MembershipHedisMeasureService', 'MembershipFollowupService',  '$scope', '$modalInstance','$compile','$filter' , 'mbrId' ,'hedisRules', 'DTOptionsBuilder', 'DTColumnBuilder',
    	function (MembershipHedisMeasureService, MembershipFollowupService, $scope, $modalInstance,$compile,$filter,mbrId, hedisRules,DTOptionsBuilder, DTColumnBuilder) {

    	
     // Please note that $modalInstance represents a modal window (instance) dependency.
     // It is not the same as the $modal service used above.

       $scope.items = [];
       $scope.hedisRules =hedisRules;
       $scope.mbrId = mbrId;
       $scope.selection = [];
       $scope.notesHistory = '';
       $scope.notes = '';
       
       $scope.dtColumns = [
    	   DTColumnBuilder.newColumn('id')
			.withTitle('SELECT').renderWith(
					function(data, type, full,
							meta) {
						$scope.items[data] = full;
						
						return '<input type="checkbox" ng-model="items['
								+ data
								+ '].checkbox_status"  ng-true-value="true" ng-false-value="false" ng-change="checkBoxChange(items['
								+   data
								+ '])"/>';
					}).withClass("text-center"),
           DTColumnBuilder.newColumn('hedisMeasureRule.shortDescription').withTitle('HEDIS MEASURE').withClass("text-left").withOption("sWidth",'20%'),
	       DTColumnBuilder.newColumn('dos').renderWith(
					function(data, type, full,
							meta) { 
						$scope.items[full.id]['dos'] = $scope.items[full.id]['dos']|| null;
							
							return	'<div class="input-group date" id="dos" ng-model="items['+full.id+'].dos" date1-picker>'+
	                    '<input type="text" ng-model="items['+full.id+'].dos" name="dos" class="username form-control input-sm col-md-9" placeholder="Enter Date Of Service" ng-required="items['+full.id+'].checkbox_status === true" />'+
	 					'<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>   </div>';
	                    
	                    
							//return '<input type="text" ng-model="items['+meta.row+']['+meta.col+']"/>';
						
					}).withTitle('DATE OF SERVICE').withOption("sWidth",'50%')
         ];
    
       $scope.dtOptions = DTOptionsBuilder.newOptions()
		 .withDisplayLength(15)
		 .withDOM('t')
		 .withOption('bServerSide', true)
		.withOption("bLengthChange", false)
		.withOption('bSaveState', true)
	    .withOption('createdRow', createdRow)
		.withOption('bDeferRender', true)
		.withOption('bDestroy', true)
		.withFnServerData(serverData);

   	function serverData(sSource, aoData, fnCallback) {
			
			
			// All the parameters you need is in the aoData
			// variable
			var order = aoData[2].value;
			var page = aoData[3].value / aoData[4].value ;
			var length = aoData[4].value ;
			var search = aoData[5].value;

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
				
			 MembershipHedisMeasureService
				.getMembershipHedisMeasures(mbrId, hedisRules)
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
      
       $scope.ok = function () {
    	   $scope.items = $filter('filter')($scope.items, {checkbox_status:true});
    	   var mbrFollowup = {mbr:{id:mbrId},followupType:{id:1}, dateOfContact:moment(new Date()).format('YYYY-MM-DD'),followupDetails:$scope.notes};
    	   MembershipFollowupService.createMembershipFollowup(mbrFollowup);
    	   angular.forEach($scope.items, function(value, key){
    		   MembershipHedisMeasureService.updateMembershipHedisMeasure(value, value.id );
			});
    	   
         $modalInstance.close();
       };

       $scope.cancel = function () {
         $modalInstance.dismiss('cancel');
       };
       
       $scope.checkBoxChange = function ( mbrHedisMeasure) {
    	   
    	   	if(mbrHedisMeasure.checkbox_status === true)  {
    	   	 $scope.items[mbrHedisMeasure.id].dos = moment(new Date()).format('MM/DD/YYYY')  ;
    	   	}  else{
    	   	 $scope.items[mbrHedisMeasure.id].dos = null;
    	   	} 
		};
		
		$scope.disableCheck = function(){
			 var selectedItems = $filter('filter')($scope.items, {checkbox_status:true});
			 var selectedItemsWithNull = $filter('filter')($scope.items, {checkbox_status:true ,  dos:'!'});
			 if(selectedItems === undefined || selectedItems.length == 0 
					 ||  (selectedItemsWithNull !== undefined && selectedItemsWithNull !== null && selectedItemsWithNull.length > 0)
					 || $scope.notes.trim() === ''){
				 return true;
			 }
				
			 return false;
			 
		}
		
		$scope.getAllMembershipFollowupsPerMbr = function(mbrId){
			MembershipFollowupService.getAllMembershipFollowupsPerMbr(mbrId).then(
					function(result) {
						if( result.length > 0){
							
							$scope.notesHistory =  result.map(function(elem){
							    return  elem.notesHistory;
							}).join("\n");
							
						}
					});
		}
		$scope.getAllMembershipFollowupsPerMbr(mbrId);
    }
    ]);
   })();