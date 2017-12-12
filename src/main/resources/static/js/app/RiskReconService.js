(function(){
'use strict';
var app = angular.module('my-app');

app.service('RiskReconService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllRiskRecons: loadAllRiskRecons,
                getAllRiskRecons: getAllRiskRecons,
                getRiskRecon: getRiskRecon,
                createRiskRecon: createRiskRecon,
                updateRiskRecon: updateRiskRecon,
                removeRiskRecon: removeRiskRecon
            };

            return factory;

            function loadAllRiskRecons() {
                console.log('Fetching all riskRecons');
                var deferred = $q.defer();
                $http.get(urls.CATEGORY_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all riskRecons');
                            $localStorage.riskRecons = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading riskRecons');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllRiskRecons(){
            	console.log('$localStorage.riskRecons');
                return $localStorage.riskRecons;
            }

            function getRiskRecon(id) {
                console.log('Fetching RiskRecon with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.CATEGORY_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully RiskRecon with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createRiskRecon(user) {
                console.log('Creating RiskRecon');
                var deferred = $q.defer();
                $http.post(urls.CATEGORY_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllRiskRecons();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating RiskRecon : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateRiskRecon(user, id) {
                console.log('Updating RiskRecon with id '+id);
                var deferred = $q.defer();
                $http.put(urls.CATEGORY_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllRiskRecons();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating RiskRecon with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeRiskRecon(id) {
                console.log('Removing RiskRecon with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.CATEGORY_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllRiskRecons();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing RiskRecon with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();