(function(){
'use strict';
var app = angular.module('my-app');

app.service('BestTimeToCallService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllBestTimeToCalls: loadAllBestTimeToCalls,
                getAllBestTimeToCalls: getAllBestTimeToCalls,
                getBestTimeToCall: getBestTimeToCall,
                createBestTimeToCall: createBestTimeToCall,
                updateBestTimeToCall: updateBestTimeToCall,
                removeBestTimeToCall: removeBestTimeToCall
            };

            return factory;

            function loadAllBestTimeToCalls() {
                console.log('Fetching all bestTimeToCalls');
                var deferred = $q.defer();
                $http.get(urls.BESTTIMETOCALL_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all bestTimeToCalls');
                            $localStorage.bestTimeToCalls = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading bestTimeToCalls');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllBestTimeToCalls(){
            	console.log('$localStorage.bestTimeToCalls');
                return $localStorage.bestTimeToCalls;
            }

            function getBestTimeToCall(id) {
                console.log('Fetching BestTimeToCall with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.BESTTIMETOCALL_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully BestTimeToCall with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createBestTimeToCall(user) {
                console.log('Creating BestTimeToCall');
                var deferred = $q.defer();
                $http.post(urls.BESTTIMETOCALL_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllBestTimeToCalls();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating BestTimeToCall : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateBestTimeToCall(user, id) {
                console.log('Updating BestTimeToCall with id '+id);
                var deferred = $q.defer();
                $http.put(urls.BESTTIMETOCALL_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllBestTimeToCalls();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating BestTimeToCall with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeBestTimeToCall(id) {
                console.log('Removing BestTimeToCall with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.BESTTIMETOCALL_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllBestTimeToCalls();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing BestTimeToCall with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();