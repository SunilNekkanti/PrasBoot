(function(){
'use strict';
var app = angular.module('my-app');

app.service('HedisMeasureService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllHedisMeasures: loadAllHedisMeasures,
                loadHedisMeasures: loadHedisMeasures,
                getAllHedisMeasures: getAllHedisMeasures,
                getHedisMeasure: getHedisMeasure,
                createHedisMeasure: createHedisMeasure,
                updateHedisMeasure: updateHedisMeasure,
                removeHedisMeasure: removeHedisMeasure
            };

            return factory;

            function loadAllHedisMeasures() {
                console.log('Fetching all hedisMeasures');
                var deferred = $q.defer();
                $http.get(urls.HEDIS_MEASURE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all hedisMeasures');
                            $localStorage.hedisMeasures = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading hedisMeasures');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadHedisMeasures(pageNo, length, search, order) {
                console.log('Fetching  HedisMeasures');
                var pageable = {
                  		 page:pageNo, size:length,sort:order, search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.HEDIS_MEASURE_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  hedisMeasures');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading hedisMeasures');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllHedisMeasures(){
            	console.log('$localStorage.hedisMeasures');
                return $localStorage.hedisMeasures;
            }

            function getHedisMeasure(id) {
                console.log('Fetching HedisMeasure with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.HEDIS_MEASURE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully HedisMeasure with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createHedisMeasure(user) {
                console.log('Creating HedisMeasure');
                var deferred = $q.defer();
                $http.post(urls.HEDIS_MEASURE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllHedisMeasures();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating HedisMeasure : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateHedisMeasure(user, id) {
                console.log('Updating HedisMeasure with id '+id);
                var deferred = $q.defer();
                $http.put(urls.HEDIS_MEASURE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllHedisMeasures();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating HedisMeasure with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeHedisMeasure(id) {
                console.log('Removing HedisMeasure with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.HEDIS_MEASURE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllHedisMeasures();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing HedisMeasure with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();