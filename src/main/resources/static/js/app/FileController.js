(function(){
'use strict';
var app = angular.module('my-app');
app.controller('FileController', ['FileService', 'InsuranceService', 'FileTypeService', 'FileUploadService', '$scope', '$compile', '$state', '$stateParams', 'DTOptionsBuilder', 'DTColumnBuilder', function(FileService, InsuranceService, FileTypeService, FileUploadService, $scope, $compile, $state, $stateParams, DTOptionsBuilder, DTColumnBuilder) {

    var self = this;
    self.file = {};
    self.files = [];
    self.selectedInsurances = [];
    self.display = $stateParams.fileDisplay || false;;
    self.displayUploadButton = true;
    self.submit = submit;
    self.getAllFiles = getAllFiles;
    self.createFile = createFile;
    self.updateFile = updateFile;
    self.removeFile = removeFile;
    self.editFile = editFile;
    self.addFile = addFile;
    self.getAllInsurances = getAllInsurances;
    self.getAllFileTypes = getAllFileTypes;
    self.dtInstance = {};
    self.fileTypes = getAllFileTypes();
    self.insurances = getAllInsurances();
    self.fileId = null;
    self.uploadFile = uploadFile;
    self.reset = reset;
    self.fileEdit = fileEdit;
    self.cancelEdit = cancelEdit;
    self.successMessage = '';
    self.errorMessage = '';
    self.done = false;
    self.onlyIntegers = /^\d+$/;
    self.onlyNumbers = /^\d+([,.]\d+)?$/;
    self.checkBoxChange = checkBoxChange;
    self.dtColumns = [
      DTColumnBuilder.newColumn('fileName').withTitle('FILE_NAME').renderWith(
        function(data, type, full,
          meta) {
          return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.fileEdit(' + full.id + ')">' + data + '</a>';
        }).withClass("text-left"),
      DTColumnBuilder.newColumn('fileType.description').withTitle('FILE_TYPE'),
      DTColumnBuilder.newColumn('fileType.ins.name').withTitle('INSURANCE')
    ];


    self.dtOptions = DTOptionsBuilder.newOptions()
      .withDisplayLength(10)
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
      FileService
        .loadFiles(page, length, search.value, sortCol + ',' + sortDir)
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

    function checkBoxChange(checkStatus, fileId) {
      self.displayEditButton = checkStatus;
      self.fileId = fileId

    }


    function submit() {
      console.log('Submitting');
      if (self.file.id === undefined || self.file.id === null) {
        console.log('Saving New File', self.file);
        createFile(self.file);
      } else {
        updateFile(self.file, self.file.id);
        console.log('File updated with id ', self.file.id);
      }
      self.displayEditButton = false;
    }

    function createFile(file) {
      console.log('About to create file');
      FileService.createFile(file)
        .then(
          function(response) {
            console.log('File created successfully');
            self.successMessage = 'File created successfully';
            self.errorMessage = '';
            self.done = true;
            self.display = false;
            self.files = getAllFiles();
            self.file = {};
            $scope.myForm.$setPristine();
          },
          function(errResponse) {
            console.error('Error while creating File');
            self.errorMessage = 'Error while creating File: ' + errResponse.data.errorMessage;
            self.successMessage = '';
          }
        );
    }


    function updateFile(file, id) {
      console.log('About to update file' + file);
      FileService.updateFile(file, id)
        .then(
          function(response) {
            console.log('File updated successfully');
            self.successMessage = 'File updated successfully';
            self.errorMessage = '';
            self.done = true;
            self.display = false;
            $state.go("file");
          },
          function(errResponse) {
            console.error('Error while updating File');
            self.errorMessage = 'Error while updating File ' + errResponse.data;
            self.successMessage = '';
          }
        );
    }


    function removeFile(id) {
      console.log('About to remove File with id ' + id);
      FileService.removeFile(id)
        .then(
          function() {
            console.log('File ' + id + ' removed successfully');
          },
          function(errResponse) {
            console.error('Error while removing file ' + id + ', Error :' + errResponse.data);
          }
        );
    }


    function getAllFiles() {
      self.files = FileService.getAllFiles();

      return self.files;
    }


    function getAllInsurances() {
      self.insurances = InsuranceService.getAllInsurances();

      return self.insurances;
    }

    function getAllFileTypes() {
      self.fileTypes = FileTypeService.getAllFileTypes();

      return self.fileTypes;
    }

    function editFile(id) {
      self.successMessage = '';
      self.errorMessage = '';
      FileService.getFile(id).then(
        function(file) {
          self.file = file;

          self.display = true;
        },
        function(errResponse) {
          console.error('Error while removing file ' + id + ', Error :' + errResponse.data);
        }
      );
    }

    function reset() {
      self.successMessage = '';
      self.errorMessage = '';
      self.file = {};
      self.insurance = {};
      self.fileType = {};
      self.activityMonth = null;
      self.myFile = null;
      $scope.myForm.$setPristine(); //reset Form
    }

    function cancelEdit() {
      self.successMessage = '';
      self.errorMessage = '';
      self.file = {};
      self.display = false;
      $state.go('main.file');
    }

    function fileEdit(id) {
      var params = {
        'fileDisplay': true
      };
      var trans = $state.go('main.file.edit', params).transition;
      trans.onSuccess({}, function() {
        editFile(id);
      }, {
        priority: -1
      });

    }

    function addFile() {
      self.successMessage = '';
      self.errorMessage = '';
      self.display = true;
    }

    function uploadFile() { 
      self.displayUploadButton = false;
      self.successMessage = '';
      self.errorMessage = '';
      self.warningMessage = 'Processing uploaded file';
      if (self.myFile) {
    	 var activityMonth = moment(self.activityMonth).format('YYYYMM');
        var promise = FileUploadService.uploadFileToUrl(self.myFile, self.insurance.id, self.fileType.id, activityMonth);

        promise.then( function(response) {  
         				self.serverResponse = 'Processed uploaded file '+ self.myFile.name+ ' successfully';
         				self.successMessage = 'Processed uploaded file '+ self.myFile.name+ ' successfully';
         				 self.displayUploadButton = true;
         				  self.warningMessage='';
         				 $scope.myForm.$setPristine();
                       },
        		      function() {
                      self.serverResponse = 'An error has occurred while processing'+ self.myFile.name;
                      self.errorMessage = 'An error has occurred while processing'+ self.myFile.name;
                       self.displayUploadButton = true;
                       self.warningMessage='';
                       $scope.myForm.$setPristine();
                      }
                    );
      }

    }

  }

]);
   })();