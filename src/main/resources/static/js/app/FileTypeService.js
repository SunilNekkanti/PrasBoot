(function(){
'use strict';
var app = angular.module('my-app');

app.service('FileTypeService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllFileTypes: loadAllFileTypes,
                loadFileTypes: loadFileTypes,
                getAllFileTypes: getAllFileTypes,
                getFileType: getFileType,
                createFileType: createFileType,
                updateFileType: updateFileType,
                removeFileType: removeFileType
            };

            return factory;

            function loadAllFileTypes() {
                console.log('Fetching all fileTypes');
                var deferred = $q.defer();
                $http.get(urls.FILE_TYPE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all fileTypes');
                            $localStorage.fileTypes = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading fileTypes');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadFileTypes(pageNo, length, search, order) {
                console.log('Fetching  FileTypes');
                var pageable = {
                  		 page:pageNo, size:length,sort: order,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.FILE_TYPE_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  fileTypes');
                            $localStorage.fileTypes = response.data.content;
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading fileTypes');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllFileTypes(){
            	console.log('$localStorage.fileTypes');
                return $localStorage.fileTypes;
            }

            function getFileType(id) {
                console.log('Fetching FileType with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.FILE_TYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully FileType with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createFileType(user) {
                console.log('Creating FileType');
                var deferred = $q.defer();
                $http.post(urls.FILE_TYPE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllFileTypes();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating FileType : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateFileType(user, id) {
                console.log('Updating FileType with id '+id);
                var deferred = $q.defer();
                $http.put(urls.FILE_TYPE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllFileTypes();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating FileType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeFileType(id) {
                console.log('Removing FileType with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.FILE_TYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllFileTypes();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing FileType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();