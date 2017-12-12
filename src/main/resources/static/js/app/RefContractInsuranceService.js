(function(){
'use strict';
var app = angular.module('my-app');
app.service('RefContractInsuranceService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllRefContractInsurances: loadAllRefContractInsurances,
                loadRefContractInsurances: loadRefContractInsurances,
                getAllRefContractInsurances: getAllRefContractInsurances,
                getRefContractInsurance: getRefContractInsurance,
                createRefContractInsurance: createRefContractInsurance,
                updateRefContractInsurance: updateRefContractInsurance,
                removeRefContractInsurance: removeRefContractInsurance
            };

            return factory;

            function loadAllRefContractInsurances() {
                console.log('Fetching all refContractInsurances');
                var deferred = $q.defer();
                $http.get(urls.INSURANCE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all refContractInsurances');
                            $localStorage.refContractInsurances = response.data.content;
                            
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading refContractInsurances');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadRefContractInsurances(pageNo, length, search, order) {
                console.log('Fetching  RefContractInsurances');
                var pageable = {
                  		 page:pageNo, size:length,sort: order,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.INSURANCE_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  refContractInsurances');
                            $localStorage.refContractInsurances = response.data.content;
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading refContractInsurances');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllRefContractInsurances(){
            	console.log('$localStorage.refContractInsurances');
                return $localStorage.refContractInsurances;
            }

            function getRefContractInsurance(id) {
                console.log('Fetching RefContractInsurance with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.INSURANCE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully RefContractInsurance with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createRefContractInsurance(user) {
                console.log('Creating RefContractInsurance');
                var deferred = $q.defer();
                $http.post(urls.INSURANCE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllRefContractInsurances();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating RefContractInsurance : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateRefContractInsurance(user, id) {
                console.log('Updating RefContractInsurance with id '+id);
                var deferred = $q.defer();
                $http.put(urls.INSURANCE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllRefContractInsurances();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating RefContractInsurance with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeRefContractInsurance(id) {
                console.log('Removing RefContractInsurance with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.INSURANCE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllRefContractInsurances();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing RefContractInsurance with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();