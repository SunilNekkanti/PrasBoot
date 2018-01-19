(function(){
'use strict';
var app = angular.module('my-app');

app.service('NewMedicalLossRatioService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadNewMedicalLossRatios: loadNewMedicalLossRatios,
                getAllNewMedicalLossRatios: getAllNewMedicalLossRatios,
                loadAllReportMonths: loadAllReportMonths,
                getAllReportMonths: getAllReportMonths,
                loadAllReportingYears: loadAllReportingYears,
                getAllReportingYears: getAllReportingYears
            };

            return factory;

            
            function loadNewMedicalLossRatios( insId, prvdrIds, reportMonths, activityMonths, isSummary, search) {
                console.log('Fetching  NewMedicalLossRatios');
                var pageable = {
                		 insId:insId,prvdrIds:prvdrIds, reportMonths:reportMonths, activityMonths:activityMonths, isSummary:isSummary,search:search
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.NEW_MLR_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  newMedicalLossRatios');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading newMedicalLossRatios');
                            return   errResponse ;
                        }
                    );
            }
            
            function loadAllReportMonths() {
                console.log('Fetching all reportMonths');
                var deferred = $q.defer();
                var pageable = {
                 		 page:0, size:5000
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
                $http.get(urls.REPORTMONTH_SERVICE_API, config )
                    .then(
                        function (response) {
                            console.log('Fetched successfully all reportMonths',response);
                            if (localStorage.getItem("reportMonths") === null) {
                            	 $localStorage.reportMonths = response.data;
                           }else {
                           	localStorage.removeItem("reportMonths") ;
                             	$localStorage.reportMonths = response.data;
                           }
                            
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading newMedicalLossRatios');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function getAllNewMedicalLossRatios(){
            	console.log('$localStorage.newMedicalLossRatios');
                return $localStorage.newMedicalLossRatios;
            }

            function getAllReportMonths(){
              return $localStorage.reportMonths;
            }


           function loadAllReportingYears() {
                console.log('Fetching all reportingYears');
                var deferred = $q.defer();
                 		   var pageable = {
                 		 page:0, size:5000
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
                $http.get(urls.MLR_REPORTING_YEARS_SERVICE_API, config )
                    .then(
                        function (response) {
                            console.log('Fetched successfully all reportingYears',response);
                            if (localStorage.getItem("reportingYears") === null) {
                            	 $localStorage.reportingYears = response.data;
                           }else {
                           	localStorage.removeItem("reportingYears") ;
                             	$localStorage.reportingYears = response.data;
                           }
                            
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading reportingYears');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
             function getAllReportingYears(){
              return $localStorage.reportingYears;
            }
        }
    ]);
   })();