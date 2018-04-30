(function(){
'use strict';
var app = angular.module('my-app');

app.controller('ModalInstanceController',
    [  'ICDMeasureService', 'CPTMeasureService', '$scope', '$modalInstance','$compile', 'items' ,'popType', 'DTOptionsBuilder', 'DTColumnBuilder',function (ICDMeasureService, CPTMeasureService, $scope, $modalInstance,$compile,items,popType, DTOptionsBuilder, DTColumnBuilder) {

    	
     // Please note that $modalInstance represents a modal window (instance) dependency.
     // It is not the same as the $modal service used above.

       $scope.items = [];
       $scope.selection = items||[];
       $scope.popType = popType;
       angular.forEach($scope.selection, function(item, index) {
    	   item.checkbox_status =  true;
       });
       
       $scope.dtColumns = [
    	   DTColumnBuilder.newColumn('id')
			.withTitle('ACTION').renderWith(
					function(data, type, full,
							meta) {
						$scope.items[data] = full;
						$scope.items[data].checkbox_status =  true;
						if($scope.containsObject($scope.items[data])<0){
							$scope.items[data].checkbox_status = false;
						}
						
						return '<input type="checkbox" ng-model="items['
								+ data
								+ '].checkbox_status"  ng-true-value="true" ng-false-value="false" ng-change="checkBoxChange(items['
								+   data
								+ '])" />';
					}).withClass("text-center"),
           DTColumnBuilder.newColumn('code').withTitle('CODE').withClass("text-left").withOption("sWidth",'20%'),
	       DTColumnBuilder.newColumn('description').renderWith(
					function(data, type, full,
							meta) { 
						if( $scope.popType === 'ICD'){
							return data;
						}else if ($scope.popType === 'CPT'){
							return full.shortDescription;
						}
					}).withTitle('DESCRIPTION').withOption("sWidth",'50%')
         ];
    
       
       $scope.dtOptions = DTOptionsBuilder.newOptions()
		 .withDisplayLength(7)
		.withOption('bServerSide', true)
		.withOption('responsive', true)
		.withOption("bLengthChange", false)
		.withOption("bPaginate", true)
		.withOption('bProcessing', true)
		.withOption('stateSave', true)
		.withOption('searchDelay', 1000)
	    .withOption('createdRow', createdRow)
       .withPaginationType('full_numbers')
       .withOption('ordering', true)
		.withOption('order', [[0,'ASC']])
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
				
			 if(popType === 'ICD'){
				 ICDMeasureService
					.loadICDMeasures(page, length, search.value, sortCol+','+sortDir)
					.then(
							function(result) {
								var records = {
									'recordsTotal' : result.data.totalElements||0,
									'recordsFiltered' : result.data.totalElements||0,
									'data' : result.data.content||{}
								};
								
								fnCallback(records);
							});
			 }else if(popType === 'CPT'){
				 CPTMeasureService
					.loadCPTMeasures(page, length, search.value, sortCol+','+sortDir)
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
			
		}

      function createdRow(row, data, dataIndex) {
           // Recompiling so we can bind Angular directive to the DT
           $compile(angular.element(row).contents())($scope);
       }
      
       $scope.ok = function () {
         $modalInstance.close($scope.selection);
       };

       $scope.cancel = function () {
         $modalInstance.dismiss('cancel');
       };
       
       $scope.checkBoxChange = function ( icdMeasure) {
    	   var index = $scope.containsObject(icdMeasure);
    	    if (index < 0) {
    	        $scope.selection.push(icdMeasure);
    	    } else {
    	        $scope.selection.splice(index, 1);
    	    }
		};
		
		$scope.containsObject = function(obj, list) {
			if( $scope.selection === undefined ||  $scope.selection.length == 0 ){
				return -1;
			}
		    var i ;
		    for (i = 0; i <  $scope.selection.length; i++) {
		        if ( $scope.selection[i].id  ===obj.id) {
		            return i;
		        }
		    }
		    return -1;
		};
		
    }
    ]);
   })();