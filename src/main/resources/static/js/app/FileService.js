(function(){
'use strict';
var app = angular.module('my-app');

app.service('FileService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllFiles: loadAllFiles,
                loadFiles : loadFiles,
                getAllFiles: getAllFiles,
                getFile: getFile,
                createFile: createFile,
                updateFile: updateFile,
                removeFile: removeFile
            };

            return factory;

            function loadFiles(pageNo, length, search, order) {
                console.log('Fetching  files');
                var deferred = $q.defer();
                var pageable = {
                 		 page:pageNo, size:length,search: search||''
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.FILE_SERVICE_API, config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  files');
                            $localStorage.files = response.data.content;
                            deferred.resolve(response);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading files');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
            
            function loadAllFiles() {
                console.log('Fetching all files');
                var deferred = $q.defer();
                $http.get(urls.FILE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all files');
                            $localStorage.files = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading files');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllFiles(){
            	console.log('$localStorage.files');
                return $localStorage.files;
            }

            function getFile(id) {
                console.log('Fetching File with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.FILE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully File with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading file with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createFile(user) {
                console.log('Creating File');
                var deferred = $q.defer();
                $http.post(urls.FILE_SERVICE_API, user)
                    .then(
                        function (response) {
                        	loadFiles(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating File : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateFile(user, id) {
                console.log('Updating File with id '+id);
                var deferred = $q.defer();
                $http.put(urls.FILE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                        	loadFiles(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating File with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeFile(id) {
                console.log('Removing File with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.FILE_SERVICE_API + id)
                    .then(
                        function (response) {
                        	loadFiles(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing File with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();