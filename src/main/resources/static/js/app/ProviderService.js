(function(){
'use strict';
var app = angular.module('my-app');

app.service('ProviderService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllProviders: loadAllProviders,
                loadProviders: loadProviders,
                getAllProviders: getAllProviders,
                getProvider: getProvider,
                createProvider: createProvider,
                updateProvider: updateProvider,
                removeProvider: removeProvider
            };

            return factory;
            
            function loadAllProviders() {
                console.log('Fetching all providers');
                var deferred = $q.defer();
                var pageable = {
                		page:0, size:200
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
                $http.get(urls.PROVIDER_SERVICE_API, config )
                    .then(
                        function (response) {
                            console.log('Fetched successfully all providers');
                            $localStorage.providers = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading providers');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadProviders(pageNo, length, search, order, currentScreen) {
                console.log('Fetching  Providers');
                var pageable = {
                  		 page:pageNo, size:length,sort:order,currentScreen:currentScreen, search: search||'' 
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.PROVIDER_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  providers');
                            $localStorage.providers = response.data.content;
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading providers');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllProviders(){
            	console.log('$localStorage.providers');
                return $localStorage.providers;
            }

            function getProvider(id) {
                console.log('Fetching Provider with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.PROVIDER_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Provider with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createProvider(prvdr) {
                console.log('Creating Provider');
                var deferred = $q.defer();
                $http.post(urls.PROVIDER_SERVICE_API, prvdr)
                    .then(
                        function (response) {
                            loadProviders(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Provider : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateProvider(prvdr, id) {
                console.log('Updating Provider with id '+id);
                var deferred = $q.defer();
                $http.put(urls.PROVIDER_SERVICE_API + id, prvdr )
                    .then(
                        function (response) {
                        	 loadProviders(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Provider with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeProvider(id) {
                console.log('Removing Provider with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.PROVIDER_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllProviders();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Provider with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();