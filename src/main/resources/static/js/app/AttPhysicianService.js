(function(){
'use strict';
var app = angular.module('my-app');

app.service('AttPhysicianService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllAttPhysicians: loadAllAttPhysicians,
                loadAttPhysicians: loadAttPhysicians,
                getAllAttPhysicians: getAllAttPhysicians,
                getAttPhysician: getAttPhysician,
                createAttPhysician: createAttPhysician,
                updateAttPhysician: updateAttPhysician,
                removeAttPhysician: removeAttPhysician
            };

            return factory;

            function loadAllAttPhysicians() {
                console.log('Fetching all attPhysicians');
                var deferred = $q.defer();
                $http.get(urls.ATTPHYISICIAN_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all attPhysicians');
                            $localStorage.attPhysicians = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading attPhysicians');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadAttPhysicians(pageNo, length, search, order) {
                console.log('Fetching  AttPhysicians');
                var pageable = {
                  		 page:pageNo, size:length,sort: order,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.ATTPHYISICIAN_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  attPhysicians');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading attPhysicians');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllAttPhysicians(){
            	console.log('$localStorage.attPhysicians');
                return $localStorage.attPhysicians;
            }

            function getAttPhysician(id) {
                console.log('Fetching AttPhysician with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.ATTPHYISICIAN_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully AttPhysician with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createAttPhysician(user) {
                console.log('Creating AttPhysician');
                var deferred = $q.defer();
                $http.post(urls.ATTPHYISICIAN_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllAttPhysicians();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating AttPhysician : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateAttPhysician(user, id) {
                console.log('Updating AttPhysician with id '+id);
                var deferred = $q.defer();
                $http.put(urls.ATTPHYISICIAN_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllAttPhysicians();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating AttPhysician with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeAttPhysician(id) {
                console.log('Removing AttPhysician with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.ATTPHYISICIAN_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllAttPhysicians();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing AttPhysician with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();