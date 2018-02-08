(function(){
'use strict';
var app = angular.module('my-app');

app.controller('LeadStatusController', ['LeadStatusService', '$scope', '$compile', '$state', 'DTOptionsBuilder', 'DTColumnBuilder', function(LeadStatusService, $scope, $compile, $state, DTOptionsBuilder, DTColumnBuilder) {

    var self = this;
    self.leadStatus = {};
    self.leadStatuses = [];
    self.display = false;
    self.displayEditButton = false;
    self.submit = submit;
    self.getAllLeadStatuses = getAllLeadStatuses;
    self.createLeadStatus = createLeadStatus;
    self.updateLeadStatus = updateLeadStatus;
    self.removeLeadStatus = removeLeadStatus;
    self.editLeadStatus = editLeadStatus;
    self.addLeadStatus = addLeadStatus;
    self.dtInstance = {};
    self.leadStatusId = null;
    self.reset = reset;
    self.cancelEdit = cancelEdit;
    self.dtInstance = {};
    self.successMessage = '';
    self.errorMessage = '';
    self.done = false;
    self.onlyIntegers = /^\d+$/;
    self.onlyNumbers = /^\d+([,.]\d+)?$/;
    self.dtInstance = {};
    self.checkBoxChange = checkBoxChange;
    self.dtColumns = [
      DTColumnBuilder.newColumn('description').withTitle('LEADSTATUS').renderWith(
        function(data, type, full,
          meta) {
          return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.editLeadStatus(' + full.id + ')">' + data + '</a>';
        }).withClass("text-left")
    ];


    self.dtOptions = DTOptionsBuilder.newOptions()
      .withDisplayLength(20)
      .withOption('bServerSide', true)
      .withOption("bLengthChange", false)
      .withOption("bPaginate", true)
      .withOption('bProcessing', true)
      .withOption('bSaveState', true)
      .withOption('createdRow', createdRow)
      .withPaginationType('full_numbers')

      .withFnServerData(serverData);

    function serverData(sSource, aoData, fnCallback) {


      // All the parameters you need is in the aoData
      // variable
      var order = aoData[2].value;
      var page = aoData[3].value / aoData[4].value;
      var length = aoData[4].value;
      var search = aoData[5].value;

      var paramMap = {};
      for (var i = 0; i < aoData.length; i++) {
        paramMap[aoData[i].name] = aoData[i].value;
      }

      var sortCol = '';
      var sortDir = '';
      // extract sort information
      if (paramMap['columns'] !== undefined && paramMap['columns'] !== null && paramMap['order'] !== undefined && paramMap['order'] !== null) {
        sortCol = paramMap['columns'][paramMap['order'][0]['column']].data;
        sortDir = paramMap['order'][0]['dir'];
      }

      // Then just call your service to get the
      // records from server side
      LeadStatusService
        .loadLeadStatuses(page, length, search.value, sortCol + ',' + sortDir)
        .then(
          function(result) {
            var records = {
              'recordsTotal': result.data.totalElements || 0,
              'recordsFiltered': result.data.totalElements || 0,
              'data': result.data.content || {}
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

    function checkBoxChange(checkStatus, leadStatusId) {
      self.displayEditButton = checkStatus;
      self.leadStatusId = leadStatusId

    }


    function submit() {
      console.log('Submitting');
      if (self.leadStatus.id === undefined || self.leadStatus.id === null) {
        console.log('Saving New LeadStatus', self.leadStatus);
        createLeadStatus(self.leadStatus);
      } else {
        updateLeadStatus(self.leadStatus, self.leadStatus.id);
        console.log('LeadStatus updated with id ', self.leadStatus.id);
      }
      self.displayEditButton = false;
    }

    function createLeadStatus(leadStatus) {
      console.log('About to create leadStatus');
      LeadStatusService.createLeadStatus(leadStatus)
        .then(
          function(response) {
            console.log('LeadStatus created successfully');
            self.successMessage = 'LeadStatus created successfully';
            self.errorMessage = '';
            self.done = true;
            self.display = false;
            console.log(self.languages);
            self.leadStatus = {};
            $scope.myForm.$setPristine();
            self.dtInstance.reloadData();
            self.dtInstance.rerender();
          },
          function(errResponse) {
            console.error('Error while creating LeadStatus');
            self.errorMessage = 'Error while creating LeadStatus: ' + errResponse.data.errorMessage;
            self.successMessage = '';
          }
        );
    }


    function updateLeadStatus(leadStatus, id) {
      console.log('About to update leadStatus' + leadStatus);
      LeadStatusService.updateLeadStatus(leadStatus, id)
        .then(
          function(response) {
            console.log('LeadStatus updated successfully');
            self.successMessage = 'LeadStatus updated successfully';
            self.errorMessage = '';
            self.done = true;
            self.display = false;
            $scope.myForm.$setPristine();
            self.dtInstance.reloadData();
            self.dtInstance.rerender();
          },
          function(errResponse) {
            console.error('Error while updating LeadStatus');
            self.errorMessage = 'Error while updating LeadStatus ' + errResponse.data;
            self.successMessage = '';
          }
        );
    }


    function removeLeadStatus(id) {
      console.log('About to remove LeadStatus with id ' + id);
      LeadStatusService.removeLeadStatus(id)
        .then(
          function() {
            console.log('LeadStatus ' + id + ' removed successfully');
          },
          function(errResponse) {
            console.error('Error while removing leadStatus ' + id + ', Error :' + errResponse.data);
          }
        );
    }


    function getAllLeadStatuses() {
      self.leadStatuses = LeadStatusService.getAllLeadStatuses();

      return self.leadStatuses;
    }

    function editLeadStatus(id) {
      self.successMessage = '';
      self.errorMessage = '';
      LeadStatusService.getLeadStatus(id).then(
        function(leadStatus) {
          self.leadStatus = leadStatus;
          self.display = true;
        },
        function(errResponse) {
          console.error('Error while removing leadStatus ' + id + ', Error :' + errResponse.data);
        }
      );
    }

    function reset() {
      self.successMessage = '';
      self.errorMessage = '';
      self.leadStatus = {};
      $scope.myForm.$setPristine(); //reset Form
    }

    function cancelEdit() {
      self.successMessage = '';
      self.errorMessage = '';
      self.leadStatus = {};
      self.display = false;
      $state.go('main.leadStatus', {}, {
        reload: true
      });
    }

    function addLeadStatus() {
      self.successMessage = '';
      self.errorMessage = '';
      self.display = true;
    }


        }
    ]);
   })();
