(function() {
  'use strict';
  var app = angular.module('my-app');

  app.controller('MembershipClaimsController', ['MembershipClaimsService', 'ProviderService', 'InsuranceService', 'RiskReconService', 'MedicalLossRatioService', '$scope', '$compile', '$state', '$stateParams', '$filter', '$localStorage', 'DTOptionsBuilder', 'DTColumnBuilder', function(MembershipClaimsService, ProviderService, InsuranceService, RiskReconService, MedicalLossRatioService, $scope, $compile, $state, $stateParams, $filter, $localStorage, DTOptionsBuilder, DTColumnBuilder) {

      var self = this;
      self.display = false;
      self.effectiveYear = $filter('date')(new Date($localStorage.loginUser.effectiveYear + '-01-01T06:00:00Z'), 'yyyy');
      self.displayEditButton = false;
      self.claimTypes = [{
        id: 'INST'
      }, {
        id: 'PHAR'
      }, {
        id: 'PROF'
      }];
      self.selectedClaimTypes = [{
        id: 'INST'
      }, {
        id: 'PHAR'
      }, {
        id: 'PROF'
      }];
      self.isCaps = [{
        id: 'Y'
      }, {
        id: 'N'
      }];
      self.selectedCaps = [{
        id: 'Y'
      }, {
        id: 'N'
      }];
      self.isRosters = [{
        id: 'Y'
      }, {
        id: 'N'
      }];
      self.selectedRosters = [{
        id: 'Y'
      }, {
        id: 'N'
      }];
      self.selectedProviders = [];
      self.selectedPrvdrs = [];
      self.insurances = [];
      self.selectedCategories = [];
      self.selectedReportMonths = [];
      self.mbrId = null;
      self.prvdrId = null;
      self.reset = reset;
      self.getAllInsurances = getAllInsurances;
      self.getAllProviders = getAllProviders;
      self.getAllRiskCategories = getAllRiskCategories;
      self.getAllReportMonths = getAllReportMonths;
      self.insurances = getAllInsurances();
      self.setProviders = setProviders;
      self.setCategories = setCategories;
      self.cancelEdit = cancelEdit;
      self.successMessage = '';
      self.errorMessage = '';
      self.done = false;
      self.onlyIntegers = /^\d+$/;
      self.onlyNumbers = /^\d+([,.]\d+)?$/;
      self.prvdrs = getAllProviders();
      self.providers = [];
      self.generate = generate;
      self.displayTable = false;
      self.displayTable1 = false;
      self.displayTable2 = false;
      self.displayTable3 = false;
      self.reportMonths = getAllReportMonths();
      self.categories = getAllRiskCategories();
      self.levelNo = 1;
      self.mbrId = 0;
      self.activityMonth = 0;
      self.select = select;
      self.deselect = deselect;
      self.rowsInfo = [];
      self.table1RowsInfo = [];
      self.table2RowsInfo = [];
      self.goTolevel2Tab = goTolevel2Tab;
      self.goTolevel3Tab = goTolevel3Tab;
      self.goTolevel4Tab = goTolevel4Tab;
      self.goTolevel5Tab = goTolevel5Tab;
      self.activateLevel1 = activateLevel1;
      self.activateLevel2 = activateLevel2;
      self.activateLevel3 = activateLevel3;
      self.activateLevel4 = activateLevel4;
      self.finalData = [];
      self.tableHeaders = [];
      self.dtInstance = {};
      self.dt1Instance = {};
      self.dt2Instance = {};
      self.dt3Instance = {};
      self.dtSelPrvdrsInstance = {};
      self.tableHeader = {};

      self.maxReportMonth = (self.reportMonths && self.reportMonths.length > 0) ? self.reportMonths[0] : null;

      self.dtInstanceCallback = dtInstanceCallback;
      self.dtSelPrvdrsInstanceCallback = dtSelPrvdrsInstanceCallback;
      self.dt1InstanceCallback = dt1InstanceCallback;
      self.dt2InstanceCallback = dt2InstanceCallback;
      self.dt3InstanceCallback = dt3InstanceCallback;



      function dtInstanceCallback(dtInstance) {
        self.dtInstance = dtInstance;
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

      function dtSelPrvdrsInstanceCallback(dtSelPrvdrsInstance) {
        self.dtSelPrvdrsInstance = dtSelPrvdrsInstance;
      }

      if (self.insurances != null && self.insurances.length > 0) {
        self.insurance = self.insurance || self.insurances[0];
      } else {
        self.insurance = {};
      }
      self.selectedReportMonths.push(self.reportMonths[0]);

      setProviders();
      setCategories();

      function reloadData() {
        var resetPaging = false;
        self.dtInstance.reloadData(callback,
          resetPaging);
      }

      function createdRow(row, data, dataIndex) {
        // Recompiling so we can bind Angular directive to the DT
        $compile(angular.element(row).contents())($scope);
      }


      function generate(resetvalue) {
        if(resetvalue) {
          self.finalData = [];
          self.tableHeaders = [];
        }
        self.displayTable = false;
        self.displayTable1 = false;
        self.displayTable2 = false;
        self.displayTable3 = false;
        self.displayTableSelPrvdr = false;

        // All the parameters you need is in the aoData  variable

        var insId = (self.insurance === undefined || self.insurance === null) ? 0 : self.insurance.id;
        var prvdrIds = (self.selectedPrvdrs === undefined || self.selectedPrvdrs === null) ? 0 : self.selectedPrvdrs.map(a => a.id);
        var prvdrIdss = self.selectedPrvdrId || prvdrIds.join();
        var claimTypes = (self.selectedClaimTypes === undefined || self.selectedClaimTypes === null) ? 0 : self.selectedClaimTypes.map(a => a.id);
        var claimTypess = '\'' + claimTypes.join() + '\'';
        var categoriess = self.category || self.selectedCategories.join();
        categoriess = '\'' + categoriess + '\'';
        var caps = (self.selectedCaps === undefined || self.selectedCaps === null) ? 0 : self.selectedCaps.map(a => a.id);
        var capss = '\'' + caps.join() + '\'';
        var rosters = (self.selectedRosters === undefined || self.selectedRosters === null) ? 0 : self.selectedRosters.map(a => a.id);
        var rosterss = '\'' + rosters.join() + '\'';
        var mbrId = self.selectedMbrId || self.mbrId;
        var reportMonth = self.selectedReportMonths.join();

        // Then just call your service to get the records from server side
        MembershipClaimsService
          .loadMembershipClaims(insId, prvdrIdss, mbrId, claimTypess, categoriess, reportMonth, self.activityMonth, capss, rosterss, self.levelNo, self.maxReportMonth)
          .then(
            function(result) {
              self.finalData[self.levelNo - 1] = [];
              var headers = result.data[0];
              self.tableHeaders[self.levelNo - 1] = headers
              var resultData = result.data.slice(1, result.data.length);

              angular.forEach(resultData, function(value1, key1) {
                var resultData1 = value1.reduce(function(result1, item, index, array) {
                  result1[index] = (item === null) ? '' : item; //a, b, c
                  return result1;
                }, {});
                self.finalData[self.levelNo - 1].push(resultData1);
              });

              switch (self.levelNo) {
                case 2:
                  self.dtSelPrvdrsColumns = [];
                  angular.forEach(headers, function(value, key) {
                    if (key == 0 || key == 2 || key == 4 || key == 5) {
                      self.dtSelPrvdrsColumns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left'));
                    } else if (key >= 9) {
                      self.dtSelPrvdrsColumns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').renderWith(level3LinkHTML).withOption('defaultContent', ''));
                    }
                  });
                  self.dtSelPrvdrsOptions = DTOptionsBuilder.newOptions().withBootstrap()
                    .withOption('data', self.finalData[self.levelNo - 1])
                    .withDOM('ft')
                    .withDisplayLength(500)
                    .withOption('searchDelay', 1000)
                    .withOption('bProcessing', true)
                    .withOption('bResponsive', true)
                    .withOption('createdRow', createdRow)
                    .withOption('bDeferRender', true)
                    .withColReorder()
        			.withFixedHeader({bottom: true})
                    .withOption('bDestroy', true);
                  graphData();
                  self.displayTable = true;
                  self.displayTableSelPrvdr = true;
                  break;

                case 3:
                  self.dt1Columns = [];
                  angular.forEach(headers, function(value, key) {
                    if (key == 0 || key == 2 || key == 4 || key == 5) {
                      self.dt1Columns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left'));
                    } else if (key == 9) {
                      self.dt1Columns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').renderWith(level4LinkHTML).withOption('defaultContent', ''));
                    } else if (key >= 2) {
                      self.dt1Columns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center'));
                    }
                  });
                  self.dt1Options = DTOptionsBuilder.newOptions()
                    .withOption('data', self.finalData[self.levelNo - 1])
                    .withDisplayLength(500)
                    .withDOM('ft')
                    .withOption('bSort', false)
                    .withOption('searchDelay', 1000)
                    .withOption('bProcessing', true)
                    .withOption('responsive', true)
                    .withOption('createdRow', createdRow)
                    .withOption('bDeferRender', true)
                    .withColReorder()
        			.withFixedHeader({bottom: true})
                    .withOption('bDestroy', true);
                  graphData();
                  self.displayTable = true;
                  self.displayTableSelPrvdr = true;
                  self.displayTable1 = true;
                  break;

                case 4:
                  self.dt2Columns = [];
                  angular.forEach(headers, function(value, key) {
                    if (key == 0 || key == 4 || key == 5) {
                      self.dt2Columns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left'));
                    } else if (key == 12) {
                      self.dt2Columns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').renderWith(level5LinkHTML).withOption('defaultContent', ''));
                    } else {
                      self.dt2Columns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center'));
                    }
                  });
                  self.dt2Options = DTOptionsBuilder.newOptions()
                    .withOption('data', self.finalData[self.levelNo - 1])
                    .withDisplayLength(500)
                    .withDOM('ft')
                    .withOption('bSort', false)
                    .withOption('searchDelay', 1000)
                    .withOption('bProcessing', true)
                    .withOption('responsive', true)
                    .withOption('createdRow', createdRow)
                    .withOption('bDeferRender', true)
                    .withColReorder()
        			.withFixedHeader({bottom: true})
                    .withOption('bDestroy', true);
                  mbrLevelGraphData();
                  self.displayTable = true;
                  self.displayTableSelPrvdr = true;
                  self.displayTable1 = true;
                  self.displayTable2 = true;
                  break;
                case 5:
                  self.dt3Columns = [];
                  angular.forEach(headers, function(value, key) {
                    if (key == 0 || key == 4 || key == 5) {
                      self.dt3Columns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left'));
                    } else {
                      self.dt3Columns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').withOption('defaultContent', ''));
                    }
                  });
                  self.dt3Options = DTOptionsBuilder.newOptions()
                    .withOption('data', self.finalData[self.levelNo - 1])
                    .withDisplayLength(500)
                    .withDOM('ft')
                    .withOption('bSort', false)
                    .withOption('searchDelay', 1000)
                    .withOption('bProcessing', true)
                    .withOption('responsive', true)
                    .withOption('createdRow', createdRow)
                    .withOption('bDeferRender', true)
                    .withColReorder()
        			.withFixedHeader({bottom: true})
                    .withOption('bDestroy', true);
                  self.displayTable = true;
                  self.displayTableSelPrvdr = true;
                  self.displayTable1 = true;
                  self.displayTable2 = true;
                  self.displayTable3 = true;
                  break;
                default:
                  self.dtColumns = [];
                  angular.forEach(headers, function(value, key) {
                    if (key <= 2) {
                      self.dtColumns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left no-click').notVisible());
                    } else if (key <= 3) {
                      self.dtColumns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-left no-click'));
                    } else if (key == 4) {
                      self.dtColumns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').renderWith(level2LinkHTML).withOption('defaultContent', ''));
                    } else {
                      self.dtColumns.push(DTColumnBuilder.newColumn(key).withTitle(value).withClass('text-center').withOption('defaultContent', ''));
                    }
                  });
                  self.dtOptions = DTOptionsBuilder.newOptions().withBootstrap()
                    .withOption('data', self.finalData[0])
                    .withDOM('Bft')
                    .withDisplayLength(500)
                    .withOption('bSort', false)
                    .withOption('searchDelay', 1000)
                    .withOption('bProcessing', true)
                    .withOption('bResponsive', true)
                    .withOption('createdRow', createdRow)
                    .withOption('bDeferRender', true)
                    .withOption('scrollY', '450')
                    .withOption('scrollX', '716')
                    .withFixedColumns({
                      heightMatch: 'none',
                      leftColumns: 5
                    })
                    .withButtons([{
                      extend: 'excelHtml5',
                      text: 'Save as Excel',
                      customize: function(xlsx) {
                        var sheet = xlsx.xl.worksheets['sheet1.xml'];
                        $('row:first c', sheet).attr('s', '42');
                      }
                    }])
                    .withOption('bDestroy', true);
                  graphData();
                  self.displayTable = true;

                  break;
              }

            });


        if (!angular.equals(self.dtInstance, {})) {
          self.dtInstance.rerender();
        }
      }

      function getAllProviders() {
        return ProviderService.getAllProviders();
      }


      function setProviders() {
        self.selectedProviders = [];
        self.selectedClaimss = [];

        self.providers = $filter('providerFilter')(self.prvdrs, self.insurance.id);
        self.providers = $filter('orderBy')(self.providers, 'name');
        self.providers.forEach(function(prvdr) {
          self.selectedPrvdrs.push(prvdr)
        });

        self.Claimss = $filter('filter')(self.pbms, {
          insId: {
            id: self.insurance.id
          }
        });
        self.Claimss = $filter('orderBy')(self.Claimss, 'description');
        reset();

      }


      function setCategories() {
        self.categories.forEach(function(category) {
          self.selectedCategories.push(category);
        });
      }


      function getAllInsurances() {
        return $filter('orderBy')(InsuranceService.getAllInsurances(), 'name');
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
        self.finalData = [];
        self.dtColumns = [];
        self.dt1Columns = [];
        self.dt2Columns = [];
        self.dt3Columns = [];
        self.selectedProvider = null;
        self.selectedMbrId = null;
        self.activityMonth = 0;
        self.reportMonth = null;
        self.category = null;
        self.displayTable = false;
        self.displayTableSelPrvdr = false;
        self.levelNo = 1;
      }

      function cancelEdit() {
        self.successMessage = '';
        self.errorMessage = '';
        self.provider = {};
        self.display = false;
        $state.go('main.membership');
      }

      function getAllRiskCategories() {
        return MembershipClaimsService.getAllRiskCategories();
      }

      function getAllReportMonths() {
        self.reportMonths = MedicalLossRatioService.getAllReportMonths();
        return self.reportMonths;
      }

      function activateLevel1() {
        self.levelNo = 1;
        self.displayTable = true;
        self.displayTableSelPrvdr = false;
        self.displayTable1 = false;
        self.displayTable2 = false;
        self.displayTable3 = false;
        graphData();
      }

      function activateLevel2() {
        self.levelNo = 2;
        self.displayTable1 = false;
        self.displayTable2 = false;
        self.displayTable3 = false;
        graphData();
      }

      function activateLevel3() {
        self.levelNo = 3;
        self.displayTable2 = false;
        self.displayTable3 = false;
        graphData();
      }

      function activateLevel4() {
        self.levelNo = 4;
        self.displayTable3 = false;
        mbrLevelGraphData();
      }

      function level2LinkHTML(data, type, full, meta) {
        var columnHeader = meta.settings.aoColumns[meta.col].sTitle;
        self.rowsInfo[meta.row] = self.rowsInfo[meta.row] || [];
        self.rowsInfo[meta.row][meta.col] = self.rowsInfo[meta.row][meta.col] || {};
        self.rowsInfo[meta.row][meta.col] = {
          reportMonth: full[3].replace('-', ''),
          category: full[4]
        };
        return '<a href="javascript:void(0)"  ng-click="ctrl.goTolevel2Tab(ctrl.rowsInfo[' + meta.row + '][' + meta.col + '])">' + data + '</a>';
      }

      function goTolevel2Tab(colInfo) {
        self.reportMonth = colInfo.reportMonth;
        self.category = colInfo.category;
        self.levelNo = 2;
        generate(false);
      }

      function level3LinkHTML(data, type, full, meta) {
        var columnHeader = meta.settings.aoColumns[meta.col].sTitle;
        self.rowsInfo[meta.row] = self.rowsInfo[meta.row] || [];
        self.rowsInfo[meta.row][meta.col] = self.rowsInfo[meta.row][meta.col] || {};
        self.rowsInfo[meta.row][meta.col] = {
          activityMonth: columnHeader.replace('-', ''),
          reportMonth: full[3].replace('-', ''),
          category: full[4]
        };
        return '<a href="javascript:void(0)"  ng-click="ctrl.goTolevel3Tab(ctrl.rowsInfo[' + meta.row + '][' + meta.col + '])">' + data + '</a>';
      }

      function goTolevel3Tab(colInfo) {
        self.activityMonth = colInfo.activityMonth;
        self.reportMonth = colInfo.reportMonth;
        self.category = colInfo.category;
        self.levelNo = 3;
        generate(false);
      }



      function level4LinkHTML(data, type, full, meta) {
        self.table1RowsInfo[meta.row] = self.table1RowsInfo[meta.row] || [];
        self.table1RowsInfo[meta.row][meta.col] = self.table1RowsInfo[meta.row][meta.col] || {};
        self.table1RowsInfo[meta.row][meta.col] = {
          activityMonth: full[3].replace('-', ''),
          reportMonth: full[2].replace('-', ''),
          prvdrId: full[1]
        };
        return '<a href="javascript:void(0)"  ng-click="ctrl.goTolevel4Tab(ctrl.table1RowsInfo[' + meta.row + '][' + meta.col + '])">' + data + '</a>';
      }

      function goTolevel4Tab(colInfo) {
        self.activityMonth = colInfo.activityMonth;
        self.reportMonth = colInfo.reportMonth;
        self.selectedPrvdrId = colInfo.prvdrId;
        self.levelNo = 4;
        generate(false);

      }


      function level5LinkHTML(data, type, full, meta) {
        var columnHeader = meta.settings.aoColumns[meta.col].sTitle;
        self.table2RowsInfo[meta.row] = self.table2RowsInfo[meta.row] || [];
        self.table2RowsInfo[meta.row][meta.col] = self.table2RowsInfo[meta.row][meta.col] || {};
        self.table2RowsInfo[meta.row][meta.col] = {
          activityMonth: full[6].replace('-', ''),
          reportMonth: full[5].replace('-', ''),
          mbrId: parseInt(full[4]),
          prvdrId: full[3]
        };
        return '<a href="javascript:void(0)"  ng-click="ctrl.goTolevel5Tab(ctrl.table2RowsInfo[' + meta.row + '][' + meta.col + '])">' + data + '</a>';
      }

      function goTolevel5Tab(colInfo) {
        self.activityMonth = colInfo.activityMonth;
        self.reportMonth = colInfo.reportMonth;
        self.selectedPrvdrId = colInfo.prvdrId;
        self.selectedMbrId = colInfo.mbrId;
        self.levelNo = 5;
        generate(false);

      }

      function select() {
        self.chartTabShow = true;
      }

      function deselect() {
        self.chartTabShow = false;
      }

      function isStringInArray(str, arr) {

        if (arr.findIndex(_strCheck) === -1) return false;

        return true;

        function _strCheck(el) {

          return el.match(str);
        }
      }

      function graphData() {
        var tableData = self.finalData[self.levelNo - 1];
        var headers = self.tableHeaders[self.levelNo - 1];
        headers = Object.values(headers);
        var seriesIndex = 0
           if(self.levelNo == 1) seriesIndex = headers.indexOf('RiskCategory');
           else if(self.levelNo == 2) seriesIndex = headers.indexOf('name');
           else if(self.levelNo == 3) seriesIndex = headers.indexOf('name');
           else if(self.levelNo == 4) seriesIndex = headers.indexOf('lastname');

        var firstActivityMonthIndex = headers.findIndex(function(el) {
          return el.match('-');
        });
        var series = tableData.map(a => a[seriesIndex]);
        series = Object.values(series);

        var newArrayLength = headers.length - firstActivityMonthIndex;

        function _strCheck(el) {
          return el.match('-');
        }
        var activityMonths = headers.splice(firstActivityMonthIndex, newArrayLength);
        self.graph = {};
        self.graph.data = [];
        tableData.forEach(function(tableDataElement) {
          var rowdata = [];
          Object.keys(tableDataElement).forEach(function(key) {
            if (key >= Number(firstActivityMonthIndex)) {
              rowdata.push(tableDataElement[key]);
            }
          });
          self.graph.data.push(rowdata);
        });


        self.graph.labels = activityMonths;
        self.graph.series = series;
        console.log('self.graph.data', self.graph.data);
        console.log('activityMonths', activityMonths);
        console.log('self.graph.labels', self.graph.labels);
        self.graph.legend = true;
        self.graph.datasetOverride = [{
          fill: false
        }, {
          fill: false
        }, {
          fill: false
        }, {
          fill: false
        }, {
          fill: false
        }];
        self.graph.options = {
          legend: {
            display: true,
            labels: {
                    filter: function(legendItem, data) {
                      return legendItem.index != 1
                    }
            }
          },
          showLines: true,
          fill: false,
          line: {
            fill: false
          },
          scales: {
            yAxes: [{
              id: 'y-axis-1',
              type: 'linear',
              display: true,
              position: 'left'
            }]
          }
        };
      }

      function mbrLevelGraphData() {
        var tableData = self.finalData[self.levelNo - 1];
        var headers = self.tableHeaders[self.levelNo - 1];
        headers = Object.values(headers);
        var seriesIndex =   headers.indexOf('RiskCategory');
        var series = tableData.map(a => a[seriesIndex]);
        series = Object.values(series)[0];
        //series = series.filter(function (item, pos) {return series.indexOf(item) == pos});
        var firstActivityMonthIndex = headers.findIndex(function(el) {
          return el.match('-');
        });
        var activityMonth = headers[firstActivityMonthIndex];
        series = series.concat(activityMonth);

        var labelIndex = headers.indexOf('lastname');
        var labels = tableData.map(a => a[labelIndex]);
        labels = Object.values(labels);

        var data = tableData.map(a => a[firstActivityMonthIndex]);
        data = Object.values(data)

        function _strCheck(el) {
          return el.match('-');
        }

        var chart = new Chart('chart-0', {
        			type: 'line',
        			data: data,
        			options: self.graph.options
        		});

        self.graph = {};
        self.graph.data = data;
        self.graph.labels = labels;
        self.graph.series = series;
        console.log('self.graph.series', self.graph.series);
        console.log('self.graph.data', self.graph.data);
        console.log('self.graph.labels', self.graph.labels);
        self.graph.legend = true;
        self.graph.datasetOverride = [{
          fill: false
        }];
        self.graph.options = {
          legend: {
            display: true,
            labels: {
                    filter: function(legendItem, data) {
                      return legendItem.index != 1
                    }
            }
          },
          showLines: true,
          fill: false,
          line: {
            fill: false
          },
          scales: {
            yAxes: [{
              id: 'y-axis-1',
              type: 'linear',
              display: true,
              position: 'left'
            }]
          },
    			plugins: {
    				filler: {
    					propagate: false
    				},
    				'samples-filler-analyser': {
    					target: 'chart-analyser'
    				}
    			}
        };


      }

    }

  ]);
})();
