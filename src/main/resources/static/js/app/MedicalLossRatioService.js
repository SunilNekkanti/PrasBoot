(function(){
'use strict';
var app = angular.module('my-app');

app.service('MedicalLossRatioService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllMedicalLossRatios: loadAllMedicalLossRatios,
                loadMedicalLossRatios: loadMedicalLossRatios,
                getAllMedicalLossRatios: getAllMedicalLossRatios,
                loadAllReportMonths: loadAllReportMonths,
                getAllReportMonths: getAllReportMonths,
            };

            return factory;

            function loadAllMedicalLossRatios(insId, prvdrId, reportMonth, category) {
                console.log('Fetching all medicalLossRatios');
                var deferred = $q.defer();
                var pageable = {
                 		 page:0, size:1000, insId:insId,prvdrId:prvdrId
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
                $http.get(urls.MLR_SERVICE_API, config )
                    .then(
                        function (response) {
                            console.log('Fetched successfully all medicalLossRatios');
                            if (localStorage.getItem("medicalLossRatios") === null) {
                            	 $localStorage.medicalLossRatios = response.data.content;
                           }else {
                           	localStorage.removeItem("medicalLossRatios") ;
                             	$localStorage.medicalLossRatios = response.data.content;
                           }
                            
                           
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading medicalLossRatios');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            
            function loadMedicalLossRatios(insId, prvdrIds, reportMonths, categories) {
                console.log('Fetching  MedicalLossRatios');
                var pageable = {
                		 insId:insId,prvdrIds:prvdrIds, reportMonths:reportMonths,categories:categories
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.MLR_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  medicalLossRatios');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading medicalLossRatios');
                            return   errResponse ;
                        }
                    );
            }
            
            function loadAllReportMonths() {
                console.log('Fetching all reportMonths');
                var deferred = $q.defer();
                var pageable = {
                 		 page:0, size:1000
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
                $http.get(urls.REPORTMONTH_SERVICE_API, config )
                    .then(
                        function (response) {
                            if (localStorage.getItem("reportMonths") === null) {
                            	 $localStorage.reportMonths = response.data;
                           }else {
                           	localStorage.removeItem("reportMonths") ;
                             	$localStorage.reportMonths = response.data;
                           }
                            
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading medicalLossRatios');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function getAllMedicalLossRatios(){
            	console.log('$localStorage.medicalLossRatios');
                return $localStorage.medicalLossRatios;
            }

            function getAllReportMonths(){
            	console.log('$localStorage.reportMonths');
                return $localStorage.reportMonths;
            }

           
        }
    ]);
   })();