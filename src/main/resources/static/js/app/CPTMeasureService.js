(function(){
'use strict';
var app = angular.module('my-app');
app.service('CPTMeasureService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllCPTMeasures: loadAllCPTMeasures,
                loadCPTMeasures: loadCPTMeasures,
                getAllCPTMeasures: getAllCPTMeasures,
                getCPTMeasure: getCPTMeasure,
                createCPTMeasure: createCPTMeasure,
                updateCPTMeasure: updateCPTMeasure,
                removeCPTMeasure: removeCPTMeasure
            };

            return factory;

            function loadAllCPTMeasures() {
                console.log('Fetching all cptMeasures');
                var deferred = $q.defer();
                $http.get(urls.CPT_MEASURE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all cptMeasures');
                            $localStorage.cptMeasures = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading cptMeasures');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadCPTMeasures(pageNo, length, search, order) {
                console.log('Fetching  CPTMeasures');
                var pageable = {
                  		 page:pageNo, size:length,sort:order,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.CPT_MEASURE_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  providers');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading providers');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllCPTMeasures(){
            	console.log('$localStorage.cptMeasures');
                return $localStorage.cptMeasures;
            }

            function getCPTMeasure(id) {
                console.log('Fetching CPTMeasure with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.CPT_MEASURE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully CPTMeasure with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createCPTMeasure(user) {
                console.log('Creating CPTMeasure');
                var deferred = $q.defer();
                $http.post(urls.CPT_MEASURE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllCPTMeasures();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating CPTMeasure : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateCPTMeasure(user, id) {
                console.log('Updating CPTMeasure with id '+id);
                var deferred = $q.defer();
                $http.put(urls.CPT_MEASURE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllCPTMeasures();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating CPTMeasure with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeCPTMeasure(id) {
                console.log('Removing CPTMeasure with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.CPT_MEASURE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllCPTMeasures();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing CPTMeasure with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();