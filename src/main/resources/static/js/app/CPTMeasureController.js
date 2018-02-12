(function() {
  'use strict';
  var app = angular.module('my-app');

  app.controller('CPTMeasureController', ['CPTMeasureService', '$scope', '$compile', '$state', '$stateParams', 'DTOptionsBuilder', 'DTColumnBuilder', function(CPTMeasureService, $scope, $compile, $state, $stateParams, DTOptionsBuilder, DTColumnBuilder) {

      var self = this;
      self.cptMeasure = {};
      self.cptMeasures = [];
      self.display = $stateParams.cptMeasureDisplay || false;;
      self.displayEditButton = false;
      self.submit = submit;
      self.getAllCPTMeasures = getAllCPTMeasures;
      self.createCPTMeasure = createCPTMeasure;
      self.updateCPTMeasure = updateCPTMeasure;
      self.removeCPTMeasure = removeCPTMeasure;
      self.editCPTMeasure = editCPTMeasure;
      self.addCPTMeasure = addCPTMeasure;
      self.dtInstance = {};
      self.cptMeasureId = null;
      self.reset = reset;
      self.cptMeasureEdit = cptMeasureEdit;
      self.cancelEdit = cancelEdit;
      self.successMessage = '';
      self.errorMessage = '';
      self.done = false;
      self.onlyIntegers = /^\d+$/;
      self.onlyNumbers = /^\d+([,.]\d+)?$/;
      self.checkBoxChange = checkBoxChange;
      self.dtColumns = [
        DTColumnBuilder.newColumn('code').withTitle('CPT_CODE').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.cptMeasureEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-left").withOption("width", '20%'),
        DTColumnBuilder.newColumn('shortDescription').withTitle('SHORT DESCRIPTION').withOption("width", '20%'),
        DTColumnBuilder.newColumn('description').withTitle('DESCRIPTION').withOption("sWidth", '60%').withOption("sClass", ' { max-width: 300px; word-wrap: break-word;  }')
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
        CPTMeasureService
          .loadCPTMeasures(page, length, search.value, sortCol + ',' + sortDir)
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

      function checkBoxChange(checkStatus, cptMeasureId) {
        self.displayEditButton = checkStatus;
        self.cptMeasureId = cptMeasureId;

      }


      function submit() {
        console.log('Submitting');
        if (self.cptMeasure.id === undefined || self.cptMeasure.id === null) {
          console.log('Saving New CPTMeasure', self.cptMeasure);
          createCPTMeasure(self.cptMeasure);
        } else {
          updateCPTMeasure(self.cptMeasure, self.cptMeasure.id);
          console.log('CPTMeasure updated with id ', self.cptMeasure.id);
        }
        self.displayEditButton = false;
      }

      function createCPTMeasure(cptMeasure) {
        console.log('About to create cptMeasure');
        CPTMeasureService.createCPTMeasure(cptMeasure)
          .then(
            function(response) {
              console.log('CPTMeasure created successfully');
              self.successMessage = 'CPTMeasure created successfully';
              self.errorMessage = '';
              self.done = true;
              self.display = false;
              self.cptMeasures = getAllCPTMeasures();
              self.cptMeasure = {};
              $scope.myForm.$setPristine();
              cancelEdit();

            },
            function(errResponse) {
              console.error('Error while creating CPTMeasure');
              self.errorMessage = 'Error while creating CPTMeasure: ' + errResponse.data.errorMessage;
              self.successMessage = '';
            }
          );
      }


      function updateCPTMeasure(cptMeasure, id) {
        console.log('About to update cptMeasure' + cptMeasure);
        CPTMeasureService.updateCPTMeasure(cptMeasure, id)
          .then(
            function(response) {
              console.log('CPTMeasure updated successfully');
              self.successMessage = 'CPTMeasure updated successfully';
              self.errorMessage = '';
              self.done = true;
              self.display = false;
              cancelEdit();
            },
            function(errResponse) {
              console.error('Error while updating CPTMeasure');
              self.errorMessage = 'Error while updating CPTMeasure ' + errResponse.data;
              self.successMessage = '';
            }
          );
      }


      function removeCPTMeasure(id) {
        console.log('About to remove CPTMeasure with id ' + id);
        CPTMeasureService.removeCPTMeasure(id)
          .then(
            function() {
              console.log('CPTMeasure ' + id + ' removed successfully');
            },
            function(errResponse) {
              console.error('Error while removing cptMeasure ' + id + ', Error :' + errResponse.data);
            }
          );
      }


      function getAllCPTMeasures() {
        self.cptMeasures = CPTMeasureService.getAllCPTMeasures();

        return self.cptMeasures;
      }


      function editCPTMeasure(id) {
        self.successMessage = '';
        self.errorMessage = '';
        CPTMeasureService.getCPTMeasure(id).then(
          function(cptMeasure) {
            self.cptMeasure = cptMeasure;

            self.display = true;
          },
          function(errResponse) {
            console.error('Error while removing cptMeasure ' + id + ', Error :' + errResponse.data);
          }
        );
      }

      function reset() {
        self.successMessage = '';
        self.errorMessage = '';
        self.cptMeasure = {};
        $scope.myForm.$setPristine(); //reset Form
      }

      function cancelEdit() {
        self.successMessage = '';
        self.errorMessage = '';
        self.cptMeasure = {};
        self.display = false;
        $state.go('main.cpt', {}, {
          location: true,
          reload: false,
          notify: false
        });
      }

      function cptMeasureEdit(id) {
        var params = {
          'cptMeasureDisplay': true
        };
        var trans = $state.go('main.cpt.edit', params).transition;
        trans.onSuccess({}, function() {
          editCPTMeasure(id);
        }, {
          priority: -1
        });
      }

      function addCPTMeasure() {
        self.successMessage = '';
        self.errorMessage = '';
        self.display = true;
      }

    }


  ]);
})();
