(function(){
'use strict';
var app = angular.module('my-app');

app.controller('MembershipHospitalizationModalInstanceController',
    [  'MembershipHospitalizationService', 'MembershipFollowupService',  '$scope', '$modalInstance','$compile','$filter' , 'mbrId' ,
    	function (MembershipHospitalizationService, MembershipFollowupService, $scope, $modalInstance,$compile,$filter,mbrId) {

    	
     // Please note that $modalInstance represents a modal window (instance) dependency.
     // It is not the same as the $modal service used above.

       $scope.items = [];
       $scope.mbrId = mbrId;
       $scope.selection = [];
       $scope.notesHistory = '';
       $scope.notes = '';
       
  
       $scope.ok = function () {
    	   $scope.items = $filter('filter')($scope.items, {checkbox_status:true});
    	   var mbrFollowup = {mbr:{id:mbrId},followupType:{id:2}, dateOfContact:moment(new Date()).format('YYYY-MM-DD'),followupDetails:$scope.notes};
    	   MembershipFollowupService.createMembershipFollowup(mbrFollowup);
    	   angular.forEach($scope.items, function(value, key){
    		   MembershipHedisMeasureService.updateMembershipHedisMeasure(value, value.id );
			});
    	   
         $modalInstance.close();
       };

       $scope.cancel = function () {
         $modalInstance.dismiss('cancel');
       };
       
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