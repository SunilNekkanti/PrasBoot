(function() {
  'use strict';
  var app = angular.module('my-app');

  app.controller('LeadMembershipFlagModalInstanceController', ['LeadService', 'LeadMembershipFlagService', '$scope', '$modalInstance', '$compile', '$filter', 'leadMembership', 'DTOptionsBuilder', 'DTColumnBuilder',
    function(LeadService, LeadMembershipFlagService, $scope, $modalInstance, $compile, $filter, leadMembership, DTOptionsBuilder, DTColumnBuilder) {


      // Please note that $modalInstance represents a modal window (instance) dependency.
      // It is not the same as the $modal service used above.

      $scope.leadMembership = {};
      $scope.leadMembership = leadMembership;
      $scope.selection = [];
      $scope.notesHistory = '';
      $scope.notes = '';
      $scope.errorMessage = '';



      $scope.ok = function() {
        $scope.items = $filter('filter')($scope.items, {
          checkbox_status: true
        });
        var leadNote = {
          notes: $scope.notes,
          lead: {
            id: leadMembership.id
          }
        };
        $scope.leadMembership.leadNotes.push(leadNote);
        LeadService.updateLead(leadMembership, leadMembership.id).then(
          function(response) {
            $scope.errorMessage = '';
            $modalInstance.close();
          },
          function(errResponse) {
            console
              .error('Error while updating flags');
            $scope.errorMessage = 'Error while updating flags: ' +
              errResponse.data.errorMessage;
            $scope.successMessage = '';
          });

      };

      $scope.cancel = function() {
        $modalInstance.dismiss('cancel');
      };

      $scope.onScheduledFlagCheckBoxChange = function() {
        if ($scope.leadMembership.leadMembershipFlag.scheduledFlag === 'Y') {
          $scope.leadMembership.leadMembershipFlag.scheduledDate = moment(new Date()).format('MM/DD/YYYY');
        } else {
          $scope.leadMembership.leadMembershipFlag.scheduledDate = null;
        }

      };

      $scope.onEngagedFlagCheckBoxChange = function() {
        if ($scope.leadMembership.leadMembershipFlag.engagedFlag === 'Y') {
          $scope.leadMembership.leadMembershipFlag.engagedDate = moment(new Date()).format('MM/DD/YYYY');
        } else {
          $scope.leadMembership.leadMembershipFlag.engagedDate = null;
        }

      };

    }
  ]);
})();
