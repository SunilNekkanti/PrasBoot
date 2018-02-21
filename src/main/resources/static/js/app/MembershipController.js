(function() {
  'use strict';
  var app = angular.module('my-app');

  app.controller('MembershipController', ['MembershipService', 'ProviderService', 'MembershipProblemService', 'ICDMeasureService', 'GenderService', 'StateService', 'InsuranceService', 'MembershipStatusService', '$scope', '$compile', '$state', '$stateParams', '$filter', 'DTOptionsBuilder', 'DTColumnBuilder', function(MembershipService, ProviderService, MembershipProblemService, ICDMeasureService, GenderService, StateService, InsuranceService, MembershipStatusService, $scope, $compile, $state, $stateParams, $filter, DTOptionsBuilder, DTColumnBuilder) {

      var self = this;
      $scope.localNavbar = true;
      self.membership = {};
      self.memberships = [];
      self.prvdrs = [];
      self.display = false;
      self.displayEditButton = false;
      self.submit = submit;
      self.pbmsubmit = pbmsubmit;
      self.genders = [];
      self.states = [];
      self.insurances = [];
      self.statuses = [];
      self.icdMeasures = [];
      self.getAllMemberships = getAllMemberships;
      self.createMembership = createMembership;
      self.updateMembership = updateMembership;
      self.removeMembership = removeMembership;
      self.editMembership = editMembership;
      self.addMembership = addMembership;
      self.updateMembershipForPbms = updateMembershipForPbms;
      self.addMembershipProblem = addMembershipProblem;
      self.getAllICDMeasures = getAllICDMeasures;
      self.dtInstance = {};
      self.dt1Instance = {};
      self.dt2Instance = {};
      self.dt3Instance = {};
      self.dt4Instance = {};
      self.dt5Instance = {};
      self.dt6Instance = {};
      self.dt7Instance = {};
      self.mbrId = null;
      self.prvdrId = null;
      self.reset = reset;
      self.getAllGenders = getAllGenders;
      self.getAllStates = getAllStates;
      self.getAllInsurances = getAllInsurances;
      self.getAllMembershipStatuses = getAllMembershipStatuses;
      self.getAllProviders = getAllProviders;
      self.membershipEdit = membershipEdit;
      self.cancelEdit = cancelEdit;
      self.cancelMbrPrblmEdit = cancelMbrPrblmEdit;
      self.successMessage = '';
      self.errorMessage = '';
      self.mbrPbmSuccessMessage = '';
      self.mbrPbmErrorMessage = '';
      self.done = false;
      self.onlyIntegers = /^\d+$/;
      self.onlyNumbers = /^\d+([,.]\d+)?$/;
      self.insurances = getAllInsurances();
      self.icdMeasures = getAllICDMeasures();
      self.prvdrs = getAllProviders();
      self.generate = generate;
      self.dt1InstanceCallback = dt1InstanceCallback;
      self.dt2InstanceCallback = dt2InstanceCallback;
      self.dt3InstanceCallback = dt3InstanceCallback;
      self.dt4InstanceCallback = dt4InstanceCallback;
      self.dt5InstanceCallback = dt5InstanceCallback;
      self.dt6InstanceCallback = dt6InstanceCallback;
      self.dt7InstanceCallback = dt7InstanceCallback;
      self.dt4NonHCCInstanceCallback = dt4NonHCCInstanceCallback;
      self.dt4HistoryInstanceCallback = dt4HistoryInstanceCallback;
      self.dt4RxInstanceCallback = dt4RxInstanceCallback;
      self.dt4RxHistoryInstanceCallback = dt4RxHistoryInstanceCallback;
      self.setProviders = setProviders;
      self.membershipProblems = [];
      self.add = add;
      self.resetMbrPrblm = resetMbrPrblm;
      self.displayProblem = false;




      function add() {
        var mbrPrm = {
          icdMeasure: {},
          startDate: '',
          resolvedDate: '',
          fileId: 1
        };
        self.membershipProblems.push(mbrPrm);
      }
      self.remove = function(index) {
        self.membershipProblems.splice(index, 1);
      }



      function dt1InstanceCallback(dt1Instance) {
        self.dt1Instance = dt1Instance;
      }

      function dt2InstanceCallback(dt2Instance) {
        self.dt2Instance = dt2Instance;
      }

      function dt3InstanceCallback(dt3Instance) {
        self.dt3Instance = dt3Instance;
      }

      function dt4InstanceCallback(dt4Instance) {
        self.dt4Instance = dt4Instance;
      }

      function dt4NonHCCInstanceCallback(dt4NonHCCInstanceCallback) {
        self.dt4NonHCCInstanceCallback = dt4NonHCCInstanceCallback;
      }

      function dt4HistoryInstanceCallback(dt4HistoryInstanceCallback) {
        self.dt4HistoryInstanceCallback = dt4HistoryInstanceCallback;
      }

      function dt4RxInstanceCallback(dt4RxInstanceCallback) {
        self.dt4RxInstanceCallback = dt4RxInstanceCallback;
      }

      function dt4RxHistoryInstanceCallback(dt4RxHistoryInstanceCallback) {
        self.dt4RxHistoryInstanceCallback = dt4RxHistoryInstanceCallback;
      }

      function dt5InstanceCallback(dt5Instance) {
        self.dt5Instance = dt5Instance;
      }

      function dt6InstanceCallback(dt6Instance) {
        self.dt6Instance = dt6Instance;
      }

      function dt7InstanceCallback(dt7Instance) {
        self.dt7Instance = dt7Instance;
      }

      self.dtColumns = [
        DTColumnBuilder.newColumn('lastName').withTitle('LAST NAME').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.membershipEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-left"),
        DTColumnBuilder.newColumn('firstName').withTitle('FIRST NAME').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('dob').withTitle('BIRTHDAY').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('dob').withTitle('AGE').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('genderId.code').withTitle('GENDER').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('status.description').withTitle('STATUS').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('contact.address').withTitle('ADDRESS').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('contact.homePhone').withTitle('PHONE').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('rafScore').withTitle('RAFSCORE').withOption('defaultContent', '')

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
          [0, 'ASC'],
          [1, 'ASC']
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
        var insId = (self.insurance === undefined || self.insurance === null) ? 0 : self.insurance.id;
        var prvdrId = (self.prvdr === undefined || self.prvdr === null) ? 0 : self.prvdr.id;
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
        MembershipService
          .loadMemberships(page, length, search.value, sortCol + ',' + sortDir, insId, prvdrId)
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

      self.dt1Columns = [
        DTColumnBuilder.newColumn('prvdr.name').withTitle('NAME').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.membershipEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-left"),
        DTColumnBuilder.newColumn('prvdr.code').withTitle('NPI').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('prvdr.contact.contactPerson').withTitle('CONTACT PERSON').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('prvdr.contact.homePhone').withTitle('CONTACT #').withOption('defaultContent', '')
      ];


      self.dt1Options = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData1);

      function serverData1(sSource, aoData, fnCallback) {
        var records = {
          'data': self.membership.mbrProviderList || {}
        };
        fnCallback(records);
      }


      self.dt2Columns = [
        DTColumnBuilder.newColumn('insId.name').withTitle('NAME').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.membershipEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-left"),
        DTColumnBuilder.newColumn('activityDate').withTitle('ACTIVITY DATE').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('activityMonth').withTitle('ACTIVITY MONTH').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('effStartDate').withTitle('EFFECTIVE START DATE').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('effEndDate').withTitle('EFFECTIVE END DATE').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('clazz').withTitle('CLASS').withOption('defaultContent', '')
      ];


      self.dt2Options = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData2);

      function serverData2(sSource, aoData, fnCallback) {
        var records = {
          'data': self.membership.mbrInsuranceList || {}
        };
        fnCallback(records);
      }

      self.dt3Columns = [
        DTColumnBuilder.newColumn('hedisMeasureRule.shortDescription').withTitle('HEDIS CODE').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.membershipEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-left"),
        DTColumnBuilder.newColumn('hedisMeasureRule.hedisMeasure.description').withTitle('DESCRIPTION'),
        DTColumnBuilder.newColumn('dueDate').withTitle('DUE DATE').withOption('defaultContent', '')
      ];


      self.dt3Options = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData3);


      function serverData3(sSource, aoData, fnCallback) {
        var filteredArr1 = self.membership.mbrHedisMeasureList || [];
        filteredArr1 = filteredArr1.filter(function(item) {
          return !item.dos;
        });
        var records = {
          'data': filteredArr1
        };
        fnCallback(records);
      }

      self.dt4Columns = [
        DTColumnBuilder.newColumn('icdMeasure.code').withTitle('CODE').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.membershipEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-center"),
        DTColumnBuilder.newColumn('startDate').withTitle('DIAGNOSED DATE').withClass("text-center"),
        DTColumnBuilder.newColumn('resolvedDate').withTitle('RESOLVED DATE').withOption('defaultContent', '').withClass("text-center")
      ];


      self.dt4Options = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withOption('scrollY', 200)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData4);

      function serverData4(sSource, aoData, fnCallback) {
        var filteredArr1 = self.membership.mbrProblemList || [];
        filteredArr1 = filteredArr1.filter(function(item) {
          return item.icdMeasure.hcc !== '' && (item.resolvedDate === undefined || item.resolvedDate === null || item.resolvedDate === '');
        });
        var records = {
          'data': filteredArr1
        };
        fnCallback(records);
      }

      self.dt4NonHCCOptions = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withOption('scrollY', 200)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData4NonHCC);

      function serverData4NonHCC(sSource, aoData, fnCallback) {
        var filteredArr1 = self.membership.mbrProblemList || [];
        filteredArr1 = filteredArr1.filter(function(item) {
          return item.icdMeasure.hcc === '' && (item.resolvedDate === undefined || item.resolvedDate === null || item.resolvedDate === '');
        });
        var records = {
          'data': filteredArr1
        };
        fnCallback(records);
      }

      self.dt4HistoryOptions = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withOption('scrollY', 200)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData4History);

      function serverData4History(sSource, aoData, fnCallback) {
        var filteredArr1 = self.membership.mbrProblemList || [];
        filteredArr1 = filteredArr1.filter(function(item) {
            return item.resolvedDate !== undefined && item.resolvedDate !== null && item.resolvedDate !== '';
        });
        var records = {
          'data': filteredArr1
        };
        fnCallback(records);
      }

      self.dt4RxOptions = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withOption('scrollY', 200)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData4Rx);

      function serverData4Rx(sSource, aoData, fnCallback) {
        var filteredArr1 = self.membership.mbrProblemList || [];
        filteredArr1 = filteredArr1.filter(function(item) {
          return item.icdMeasure.hcc === '' && (item.resolvedDate === undefined || item.resolvedDate === null || item.resolvedDate === '');
        });
        var records = {
          'data': filteredArr1
        };
        fnCallback(records);
      }

      self.dt4RxHistoryOptions = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withOption('scrollY', 200)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData4RxHistory);

      function serverData4RxHistory(sSource, aoData, fnCallback) {
        var filteredArr1 = self.membership.mbrProblemList || [];
        filteredArr1 = filteredArr1.filter(function(item) {
          console.log('item', item)
          return item.resolvedDate !== undefined && item.resolvedDate !== null && item.resolvedDate !== '';
        });
        var records = {
          'data': filteredArr1
        };
        fnCallback(records);
      }


      self.dt5Columns = [
        DTColumnBuilder.newColumn('hospital.name').withTitle('Hospital').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.membershipEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-left"),
        DTColumnBuilder.newColumn('admitDate').withTitle('ADMIT DATE'),
        DTColumnBuilder.newColumn('expDisDate').withTitle('EXPECTED DISCHARGE DATE').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('priorAdmits').withTitle('PRIOR ADMITS').withOption('defaultContent', '')

      ];


      self.dt5Options = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData5);

      function serverData5(sSource, aoData, fnCallback) {
        var records = {
          'data': self.membership.mbrHospitalizationList || {}
        };
        fnCallback(records);
      }


      self.dt6Columns = [
        DTColumnBuilder.newColumn('activityMonth').withTitle('ACTIVITY MONTH').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.membershipEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-left"),
        DTColumnBuilder.newColumn('isRoster').withTitle('IS IN ROSTER').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('isCap').withTitle('IS IN CAP').withOption('defaultContent', '')
      ];


      self.dt6Options = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withDOM('ft')
        .withOption('deferRender', true)
        .withOption('scrollY', 200)
        .withOption('bServerSide', true)
        .withOption('bSearchable', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData6);

      function serverData6(sSource, aoData, fnCallback) {
        var search = aoData[5].value;
        var filteredArr = self.membership.mbrActivityMonthList;
        if (search.value !== '') {
          filteredArr = filteredArr.filter(function(item) {
            return String(item.activityMonth).indexOf(search.value) > -1 || item.isRoster.indexOf(search.value.toUpperCase()) > -1 || item.isCap.indexOf(search.value.toUpperCase()) > -1;
          });
        }

        var records = {
          'data': filteredArr || {}
        };
        fnCallback(records);
      }

      self.dt7Columns = [
        DTColumnBuilder.newColumn('hedisMeasureRule.shortDescription').withTitle('HEDIS CODE').renderWith(
          function(data, type, full,
            meta) {
            return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.membershipEdit(' + full.id + ')">' + data + '</a>';
          }).withClass("text-left"),
        DTColumnBuilder.newColumn('hedisMeasureRule.hedisMeasure.description').withTitle('DESCRIPTION'),
        DTColumnBuilder.newColumn('dueDate').withTitle('DUE DATE').withOption('defaultContent', ''),
        DTColumnBuilder.newColumn('dos').withTitle('DATE OF SERVICE').withOption('defaultContent', '')
      ];


      self.dt7Options = DTOptionsBuilder.newOptions()
        .withDisplayLength(20)
        .withDOM('t')
        .withOption('bServerSide', true)
        .withOption('createdRow', createdRow)
        .withOption('bDeferRender', true)
        .withFnServerData(serverData7);


      function serverData7(sSource, aoData, fnCallback) {
        var filteredArr1 = self.membership.mbrHedisMeasureList || [];
        filteredArr1 = filteredArr1.filter(function(item) {
          return item.dos;
        });
        var records = {
          'data': filteredArr1
        };
        fnCallback(records);
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

      function generate() {
        self.dtInstance.rerender();
      }

      function submit() {
        console.log('Submitting', self.membership.id);
        if (self.membership.id === undefined || self.membership.id === null) {
          console.log('Saving New Membership');
          createMembership(self.membership);
        } else {
          console.log(' self.membership.mbrProblemList', self.membership.mbrProblemList);
          updateMembership(self.membership, self.membership.id);
          console.log('Membership updated with id ', self.membership.id);
        }
        self.displayEditButton = false;
      }

      function pbmsubmit() {
        console.log('Submitting Membership Problem');
        if (self.membership.id === undefined || self.membership.id === null) {
          console.log('No Membership exists to add  problem(s)');
        } else {
          console.log('self.membershipProblems', self.membershipProblems);
          self.membership.mbrProblemList = self.membership.mbrProblemList.concat(self.membershipProblems);
          updateMembershipForPbms(self.membership, self.membership.id);
          console.log('Adding Membership Problems for membership id ', self.membership.id);
        }
        self.displayEditButton = false;
      }

      function createMembership(mbr) {
        console.log('About to create mbr');
        MembershipService.createMembership(mbr)
          .then(
            function(response) {
              console.log('Membership created successfully');
              self.successMessage = 'Membership created successfully';
              self.errorMessage = '';
              self.done = true;
              self.display = false;
              self.memberships = getAllMemberships();
              self.membership = {};
              cancelEdit();
            },
            function(errResponse) {
              console.error('Error while creating Membership');
              self.errorMessage = 'Error while creating Membership: ' + errResponse.data.errorMessage;
              self.successMessage = '';
            }
          );
      }


      function updateMembership(mbr, id) {
        console.log('About to update mbr');
        MembershipService.updateMembership(mbr, id)
          .then(
            function(response) {
              console.log('Membership updated successfully');
              self.successMessage = 'Membership updated successfully';
              self.errorMessage = '';
              self.done = true;
              self.display = false;
              cancelEdit();
            },
            function(errResponse) {
              console.error('Error while updating Membership');
              self.errorMessage = 'Error while updating Membership ' + errResponse.data;
              self.successMessage = '';
            }
          );
      }

      function updateMembershipForPbms(mbr, id) {
        console.log('About to update mbr');
        MembershipService.updateMembership(mbr, id)
          .then(
            function(response) {
              console.log('Membership updated successfully');
              self.mbrPbmSuccessMessage = 'Membership Problems updated successfully';
              self.mbrPbmErrorMessage = '';
              self.done = true;
              self.displayProblem = false;
              cancelMbrPrblmEdit();
            },
            function(errResponse) {
              console.error('Error while updating Membership');
              self.mbrPbmErrorMessage = 'Error while updating Membership ' + errResponse.data;
              self.mbrPbmSuccessMessage = '';
              self.displayProblem = true;
            }
          );
      }

      function removeMembership(id) {
        console.log('About to remove Membership with id ' + id);
        MembershipService.removeMembership(id)
          .then(
            function() {
              console.log('Membership ' + id + ' removed successfully');
            },
            function(errResponse) {
              console.error('Error while removing mbr ' + id + ', Error :' + errResponse.data);
            }
          );
      }


      function getAllProviders() {
        self.prvdrs = ProviderService.getAllProviders();

        return self.prvdrs;
      }

      function getAllMemberships() {
        self.memberships = MembershipService.getAllMemberships();

        return self.memberships;
      }

      function getAllGenders() {
        return GenderService.getAllGenders();
      }

      function getAllStates() {
        return StateService.getAllStates();
      }

      function getAllInsurances() {
        return InsuranceService.getAllInsurances();
      }

      function getAllICDMeasures() {
        return ICDMeasureService.getAllICDMeasures();
      }


      function getAllMembershipStatuses() {
        return MembershipStatusService.getAllMembershipStatuses();
      }


      function editMembership(id) {
        self.successMessage = '';
        self.errorMessage = '';
        MembershipService.getMembership(id).then(
          function(mbr) {
            self.membership = mbr;
            self.genders = getAllGenders();
            self.states = getAllStates();
            self.statuses = getAllMembershipStatuses();
            self.insurances = getAllInsurances();
            self.display = true;
          },
          function(errResponse) {
            console.error('Error while removing prvdr ' + id + ', Error :' + errResponse.data);
          }
        );
      }

      function membershipEdit(id) {
        var params = {
          'membershipDisplay': true
        };
        var trans = $state.go('main.membership.edit', params).transition;
        trans.onSuccess({}, function() {
          editMembership(id)
        }, {
          priority: -1
        });


      }

      function reset() {
        self.successMessage = '';
        self.errorMessage = '';
        self.membership = {};
      }

      function resetMbrPrblm() {
        self.successMessage = '';
        self.errorMessage = '';
        self.membershipProblems = [];
        add();
      }


      function cancelEdit() {
        self.successMessage = '';
        self.errorMessage = '';
        self.membership = {};
        cancelMbrPrblmEdit();
        self.display = false;
        $state.go('main.membership', {}, {
          location: true,
          reload: false,
          notify: false
        });
      }

      function cancelMbrPrblmEdit() {
        self.successMessage = '';
        self.errorMessage = '';
        self.mbrPbmSuccessMessage = '';
        self.mbrPbmErrorMessage = '';
        self.membershipProblems = [];
        self.displayProblem = false;
      }

      function addMembership() {
        self.successMessage = '';
        self.errorMessage = '';
        self.membership = {};
        self.genders = getAllGenders();
        self.states = getAllStates();
        self.insurances = getAllInsurances();
        self.statuses = getAllMembershipStatuses();
        self.display = true;
      }

      function setProviders() {
        self.providers = $filter('filter')(self.prvdrs, {
          prvdrRefContracts: [{
            ins: {
              id: self.insurance.id
            }
          }]
        }, true);

      }

      function addMembershipProblem() {
        self.displayProblem = true;
        self.successMessage = '';
        self.errorMessage = '';
        self.membershipProblem = {};
        self.display = true;
        add();
      }

      function addMembershipProblems(membershipProblems, mbrId) {
        console.log('About to add membershipProblems');
        MembershipProblemService.addMembershipProblems(membershipProblems, mbrId)
          .then(
            function(response) {
              console.log('Membership created successfully');
              self.successMessage = 'Added Membership Problems successfully';
              self.errorMessage = '';
              self.displayProblem = false;
              self.membershipProblems = [];

            },
            function(errResponse) {
              console.error('Error while creating Membership');
              self.errorMessage = 'Error while creating Membership: ' + errResponse.data.errorMessage;
              self.successMessage = '';
            }
          );
      }

    }


  ]);
})();
