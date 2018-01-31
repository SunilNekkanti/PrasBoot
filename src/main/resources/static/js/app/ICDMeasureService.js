(function(){
'use strict';
var app = angular.module('my-app');

app.service('ICDMeasureService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllICDMeasures: loadAllICDMeasures,
                loadICDMeasures: loadICDMeasures,
                getAllICDMeasures: getAllICDMeasures,
                getICDMeasure: getICDMeasure,
                createICDMeasure: createICDMeasure,
                updateICDMeasure: updateICDMeasure,
                removeICDMeasure: removeICDMeasure
            };

            return factory;

            function loadAllICDMeasures() {
                console.log('Fetching all icdMeasures');
                var deferred = $q.defer();
                $http.get(urls.ICD_MEASURE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all icdMeasures');
                            if (localStorage.getItem("icdMeasures") === null) {
                            	 $localStorage.icdMeasures = response.data.content;
                           }else {
                           	localStorage.removeItem("icdMeasures") ;
                             	$localStorage.icdMeasures = response.data.content;
                           }
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading icdMeasures');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadICDMeasures(pageNo, length, search, order) {
                console.log('Fetching  ICDMeasures');
                var pageable = {
                  		 page:pageNo, size:length,sort:order, search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.ICD_MEASURE_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  icdMeasures');
                            if (localStorage.getItem("icdMeasures") === null) {
                            	 $localStorage.icdMeasures = response.data.content;
                           }else {
                           	localStorage.removeItem("icdMeasures") ;
                             	$localStorage.icdMeasures = response.data.content;
                           }
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading icdMeasures');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllICDMeasures(){
            	console.log('$localStorage.icdMeasures');
                return $localStorage.icdMeasures;
            }

            function getICDMeasure(id) {
                console.log('Fetching ICDMeasure with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.ICD_MEASURE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully ICDMeasure with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createICDMeasure(user) {
                console.log('Creating ICDMeasure');
                var deferred = $q.defer();
                $http.post(urls.ICD_MEASURE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllICDMeasures();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating ICDMeasure : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateICDMeasure(user, id) {
                console.log('Updating ICDMeasure with id '+id);
                var deferred = $q.defer();
                $http.put(urls.ICD_MEASURE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllICDMeasures();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating ICDMeasure with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeICDMeasure(id) {
                console.log('Removing ICDMeasure with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.ICD_MEASURE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllICDMeasures();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing ICDMeasure with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();