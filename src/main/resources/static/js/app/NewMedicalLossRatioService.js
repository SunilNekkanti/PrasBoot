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
            };

            return factory;

            
            function loadNewMedicalLossRatios( insId, prvdrIds, reportMonths, isSummary, search) {
                console.log('Fetching  NewMedicalLossRatios');
                var pageable = {
                		 insId:insId,prvdrIds:prvdrIds, reportMonths:reportMonths, isSummary:isSummary,search:search
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
                            console.log('Fetched successfully all reportMonths');
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
            	console.log('$localStorage.reportMonths');
                return $localStorage.reportMonths;
            }

        }
    ]);
   })();