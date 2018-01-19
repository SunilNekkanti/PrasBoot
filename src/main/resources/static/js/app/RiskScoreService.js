(function(){
'use strict';
var app = angular.module('my-app');

app.service('RiskScoreService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllRiskScores: loadAllRiskScores,
                loadRiskScores : loadRiskScores,
                getAllRiskScores: getAllRiskScores,
                getRiskScore: getRiskScore,
                createRiskScore: createRiskScore,
                updateRiskScore: updateRiskScore,
                removeRiskScore: removeRiskScore,
                loadAllPaymentYears: loadAllPaymentYears,
                calcularRiskScore: calcularRiskScore
            };

            return factory;

            function loadRiskScores(pageNo, length, search, order) {
                console.log('Fetching  riskScores');
                var deferred = $q.defer();
                var pageable = {
                 		 page:pageNo, size:length,search: search||''
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.FACILITYTYPE_SERVICE_API, config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  riskScores');
                            $localStorage.riskScores = response.data.content;
                            deferred.resolve(response);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading riskScores');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
            
            function loadAllRiskScores() {
                console.log('Fetching all riskScores');
                var deferred = $q.defer();
                $http.get(urls.FACILITYTYPE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all riskScores');
                            $localStorage.riskScores = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading riskScores');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllRiskScores(){
            	console.log('$localStorage.riskScores');
                return $localStorage.riskScores;
            }

            function getRiskScore(id) {
                console.log('Fetching RiskScore with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.FACILITYTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully RiskScore with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading riskScore with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createRiskScore(user) {
                console.log('Creating RiskScore');
                var deferred = $q.defer();
                $http.post(urls.FACILITYTYPE_SERVICE_API, user)
                    .then(
                        function (response) {
                        	loadRiskScores(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating RiskScore : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateRiskScore(user, id) {
                console.log('Updating RiskScore with id '+id);
                var deferred = $q.defer();
                $http.put(urls.FACILITYTYPE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                        	loadRiskScores(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating RiskScore with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeRiskScore(id) {
                console.log('Removing RiskScore with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.FACILITYTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                        	loadRiskScores(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing RiskScore with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
             function loadAllPaymentYears( ) {
                console.log('Fetching  paymentYears');
                var deferred = $q.defer();

                 		var config = {
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.PAYMENT_YEARS_SERVICE_API, config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  paymentYears');
                            $localStorage.paymentYears = response.data;
                            deferred.resolve(response);
                         return     response.data ;
                        },
                        function (errResponse) {
                            console.error('Error while loading paymentYears');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
            
              function calcularRiskScore(icdcodes, instOrComm, paymentYear, sex,dob, medicaid,isAgedOrDisabled, originallyDueToDis ) {
                console.log('calculating riskscore for given icdcodes');
                var pageable = {
                 		 icdcodes:icdcodes, instOrComm:instOrComm ,effYear:paymentYear,gender:sex,dob:dob, medicaid:medicaid,isAgedOrDisabled:isAgedOrDisabled,originallyDueToDis:originallyDueToDis
                 		};
                var deferred = $q.defer();
                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.CALCULATE_RISK_SCORE_SERVICE_API, config)
                    .then(
                        function (response) {
                           console.log('*******response.data*****'+JSON.stringify(response));
                            console.log('Fetched successfully  riskscore');
                            deferred.resolve(response);
                         return     response.data ;
                        },
                        function (errResponse) {
                            console.error('Error while fetching riskscore');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
        }
    ]);
   })();