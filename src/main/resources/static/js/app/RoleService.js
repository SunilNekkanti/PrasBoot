'use strict';

app.service('RoleService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllRoles: loadAllRoles,
                loadRoles : loadRoles,
                getAllRoles: getAllRoles,
                getRole: getRole,
                createRole: createRole,
                updateRole: updateRole,
                removeRole: removeRole
            };

            return factory;

            function loadRoles(pageNo, length, search, order) {
                console.log('Fetching  Roles');
                var deferred = $q.defer();
                var pageable = {
                 		 page:pageNo, size:length,search: search||''
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.ROLE_SERVICE_API, config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  roles');
                            $localStorage.roles = response.data.content;
                            deferred.resolve(response);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading roles');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
            
            function loadAllRoles() {
                console.log('Fetching all roles');
                var deferred = $q.defer();
                $http.get(urls.ROLE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all roles');
                            $localStorage.roles = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading roles');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllRoles(){
            	console.log('$localStorage.roles');
                return $localStorage.roles;
            }

            function getRole(id) {
                console.log('Fetching Role with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.ROLE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Role with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading role with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createRole(role) {
                console.log('Creating Role');
                var deferred = $q.defer();
                $http.post(urls.ROLE_SERVICE_API, role)
                    .then(
                        function (response) {
                            loadAllRoles();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Role : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateRole(role, id) {
                console.log('Updating Role with id '+id);
                var deferred = $q.defer();
                $http.put(urls.ROLE_SERVICE_API + id, role)
                    .then(
                        function (response) {
                            loadAllRoles();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Role with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeRole(id) {
                console.log('Removing Role with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.ROLE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllRoles();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Role with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);