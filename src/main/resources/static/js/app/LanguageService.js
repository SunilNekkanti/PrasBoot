(function(){
'use strict';
var app = angular.module('my-app');

app.service('LanguageService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllLanguages: loadAllLanguages,
                loadLanguages: loadLanguages,
                getAllLanguages: getAllLanguages,
                getLanguage: getLanguage,
                createLanguage: createLanguage,
                updateLanguage: updateLanguage,
                removeLanguage: removeLanguage
            };

            return factory;

            function loadAllLanguages() {
                console.log('Fetching all Languages');
                var deferred = $q.defer();
                $http.get(urls.LANGUAGE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all Languages');
                            $localStorage.Languages = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading Languages');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadLanguages(pageNo, length, search, order) {
                console.log('Fetching  Languages');
                var pageable = {
                  		 page:pageNo, size:length,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.LANGUAGE_SERVICE_API,  config)
                    .then(
                        function (response) {
                        	
                            console.log('Fetched successfully  languages');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading languages');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllLanguages(){
            	console.log('$localStorage.Languages');
                return $localStorage.Languages;
            }

            function getLanguage(id) {
                console.log('Fetching Language with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.LANGUAGE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Language with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createLanguage(language) {
                console.log('Creating Language:');
                var deferred = $q.defer();
                
                $http.post(urls.LANGUAGE_SERVICE_API, language)
                    .then(
                        function (response) {
                            loadAllLanguages();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Language : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateLanguage(user, id) {
                console.log('Updating Language with id '+id);
                var deferred = $q.defer();
                $http.put(urls.LANGUAGE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllLanguages();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Language with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeLanguage(id) {
                console.log('Removing Language with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.LANGUAGE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllLanguages();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Language with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();