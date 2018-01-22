(function(){
'use strict';
var app = angular.module('my-app');

app.service('InsuranceService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllInsurances: loadAllInsurances,
                loadInsurances: loadInsurances,
                getAllInsurances: getAllInsurances,
                getInsurance: getInsurance,
                createInsurance: createInsurance,
                updateInsurance: updateInsurance,
                removeInsurance: removeInsurance
            };

            return factory;

            function loadAllInsurances() {
                console.log('Fetching all insurances');
                var deferred = $q.defer();
                $http.get(urls.INSURANCE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all insurances');
                            $localStorage.insurances = response.data.content;
                            
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading insurances');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadInsurances(pageNo, length, search, order, currentScreen) {
                console.log('Fetching  Insurances');
                var pageable = {
                  		 page:pageNo, size:length,sort: order,currentScreen:currentScreen, search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.INSURANCE_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  insurances');
                            $localStorage.insurances = response.data.content;
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading insurances');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllInsurances(){
            	console.log('$localStorage.insurances');
                return $localStorage.insurances;
            }

            function getInsurance(id) {
                console.log('Fetching Insurance with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.INSURANCE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Insurance with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createInsurance(user) {
                console.log('Creating Insurance');
                var deferred = $q.defer();
                $http.post(urls.INSURANCE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllInsurances();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Insurance : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateInsurance(user, id) {
                console.log('Updating Insurance with id '+id);
                var deferred = $q.defer();
                $http.put(urls.INSURANCE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllInsurances();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Insurance with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeInsurance(id) {
                console.log('Removing Insurance with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.INSURANCE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllInsurances();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Insurance with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();