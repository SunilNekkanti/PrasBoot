(function() {
  'use strict';
  var app = angular.module('my-app');
  app.controller('AttPhysicianController', ['AttPhysicianService', '$scope', '$compile', '$state', '$stateParams', 'DTOptionsBuilder', 'DTColumnBuilder', function(AttPhysicianService, $scope, $compile, $state, $stateParams, DTOptionsBuilder, DTColumnBuilder) {

      var self = this;
      self.attPhysician = {};
      self.attPhysicians = [];
      self.display = $stateParams.attPhysicianDisplay || false;
      self.displayEditButton = false;
      self.submit = submit;
      self.getAllAttPhysicians = getAllAttPhysicians;
      self.createAttPhysician = createAttPhysician;
      self.updateAttPhysician = updateAttPhysician;
      self.removeAttPhysician = removeAttPhysician;
      self.editAttPhysician = editAttPhysician;
      self.addAttPhysician = addAttPhysician;
      self.dtInstance = {};
      self.attPhysicianId = null;
      self.reset = reset;
      self.attPhysicianEdit = attPhysicianEdit;
      self.cancelEdit = cancelEdit;
      self.successMessage = '';
      self.errorMessage = '';
      self.done = false;
      self.onlyIntegers = /^\d+$/;
      self.onlyNumbers = /^\d+([,.]\d+)?$/;
      self.checkBoxChange = checkBoxChange;
      self.dtColumns = [
        DTColumnBuilder.newColumn('name').withTitle('NAME').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.attPhysicianEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-left"),
        DTColumnBuilder.newColumn('code').withTitle('CODE')
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
        .withPaginationType('full_numbers')
        .withOption('ordering', true)
        .withOption('order', [
          [0, 'ASC']
        ])
        .withOption('aLengthMenu', [
          [15, 20, -1],
          [15, 20, "All"]
        ])
        .withOption('bDeferRender', true)
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
        AttPhysicianService
          .loadAttPhysicians(page, length, search.value, sortCol + ',' + sortDir)
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

      function checkBoxChange(checkStatus, attPhysicianId) {
        self.displayEditButton = checkStatus;
        self.attPhysicianId = attPhysicianId;

      }


      function submit() {
        console.log('Submitting');
        if (self.attPhysician.id === undefined || self.attPhysician.id === null) {
          console.log('Saving New AttPhysician', self.attPhysician);
          createAttPhysician(self.attPhysician);
        } else {
          updateAttPhysician(self.attPhysician, self.attPhysician.id);
          console.log('AttPhysician updated with id ', self.attPhysician.id);
        }
        self.displayEditButton = false;
      }

      function createAttPhysician(attPhysician) {
        console.log('About to create attPhysician');
        AttPhysicianService.createAttPhysician(attPhysician)
          .then(
            function(response) {
              console.log('AttPhysician created successfully');
              self.successMessage = 'AttPhysician created successfully';
              self.errorMessage = '';
              self.done = true;
              self.display = false;
              self.attPhysicians = getAllAttPhysicians();
              self.attPhysician = {};
              $scope.myForm.$setPristine();
            },
            function(errResponse) {
              console.error('Error while creating AttPhysician');
              self.errorMessage = 'Error while creating AttPhysician: ' + errResponse.data.errorMessage;
              self.successMessage = '';
            }
          );
      }


      function updateAttPhysician(attPhysician, id) {
        console.log('About to update attPhysician' + attPhysician);
        AttPhysicianService.updateAttPhysician(attPhysician, id)
          .then(
            function(response) {
              console.log('AttPhysician updated successfully');
              self.successMessage = 'AttPhysician updated successfully';
              self.errorMessage = '';
              self.done = true;
              self.display = false;
              $state.go("main.attPhysician");
            },
            function(errResponse) {
              console.error('Error while updating AttPhysician');
              self.errorMessage = 'Error while updating AttPhysician ' + errResponse.data;
              self.successMessage = '';
            }
          );
      }


      function removeAttPhysician(id) {
        console.log('About to remove AttPhysician with id ' + id);
        AttPhysicianService.removeAttPhysician(id)
          .then(
            function() {
              console.log('AttPhysician ' + id + ' removed successfully');
            },
            function(errResponse) {
              console.error('Error while removing attPhysician ' + id + ', Error :' + errResponse.data);
            }
          );
      }


      function getAllAttPhysicians() {
        self.attPhysicians = AttPhysicianService.getAllAttPhysicians();

        return self.attPhysicians;
      }



      function editAttPhysician(id) {
        self.successMessage = '';
        self.errorMessage = '';
        AttPhysicianService.getAttPhysician(id).then(
          function(attPhysician) {
            self.attPhysician = attPhysician;

            self.display = true;
          },
          function(errResponse) {
            console.error('Error while removing attPhysician ' + id + ', Error :' + errResponse.data);
          }
        );
      }

      function reset() {
        self.successMessage = '';
        self.errorMessage = '';
        self.attPhysician = {};
        $scope.myForm.$setPristine(); //reset Form
      }

      function cancelEdit() {
        self.successMessage = '';
        self.errorMessage = '';
        self.attPhysician = {};
        self.display = false;
        $state.go('main.attPhysician', {}, {
          reload: false
        });
      }

      function attPhysicianEdit(id) {
        var params = {
          'attPhysicianDisplay': true
        };
        var trans = $state.go('main.attPhysician.edit', params).transition;
        trans.onSuccess({}, function() {
          editAttPhysician(id);
        }, {
          priority: -1
        });

      }

      function addAttPhysician() {
        self.successMessage = '';
        self.errorMessage = '';
        self.display = true;
      }

    }


  ]);

})();
