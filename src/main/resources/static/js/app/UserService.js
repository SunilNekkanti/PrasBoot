(function(){
'use strict';
var app = angular.module('my-app');

app.service('UserService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllUsers: loadAllUsers,
                loadUsers: loadUsers,
                getAllUsers: getAllUsers,
                getUser: getUser,
                createUser: createUser,
                updateUser: updateUser,
                removeUser: removeUser,
                loginUser : loginUser
            };

            return factory;
         

            function loginUser() {
                console.log('Fetching loginUser');
                var deferred = $q.defer();
                
                $http.get(urls.LOGIN_USER)
                    .then(
                        function (response) {
                            console.log('Fetched successfully loginUser');
                            if (localStorage.getItem("loginUser") === null) {
                            	 $localStorage.loginUser = response.data;
                            }else {
                            	localStorage.removeItem("loginUser") ;
                            	$localStorage.loginUser = response.data;
                            }
                           
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while fetching loginUser');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function loadUsers(pageNo, length, search, order) {
                console.log('Fetching  users');
                var deferred = $q.defer();
                var pageable = {
                 		 page:pageNo, size:length,search: search||''
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.USER_SERVICE_API, config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  users');
                            if (localStorage.getItem("users") === null) {
                            $localStorage.users = response.data.content;
                            }else{
                            	localStorage.removeItem("users") ;
                            	$localStorage.users = response.data.content;
                            }
                            deferred.resolve(response);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading users');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
            
            function loadAllUsers() {
                console.log('Fetching all users');
                var deferred = $q.defer();
                $http.get( urls.USER_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all users');
                            if (localStorage.getItem("users") === null) {
                            $localStorage.users = response.data.content;
                            }else{
                            	localStorage.removeItem("users") ;
                            	$localStorage.users = response.data.content;
                            }
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading users');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllUsers(){
            	console.log('$localStorage.users');
                return $localStorage.users;
            }

            function getUser(id) {
                console.log('Fetching User with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.USER_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully User with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createUser(user) {
                console.log('Creating User');
                var deferred = $q.defer();
                $http.post(urls.USER_SERVICE_API, user)
                    .then(
                        function (response) {
                        	loadUsers(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating User : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateUser(user, id) {
                console.log('Updating User with id '+id);
                var deferred = $q.defer();
                $http.put(urls.USER_SERVICE_API + id, user)
                    .then(
                        function (response) {
                        	loadUsers(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating User with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeUser(id) {
                console.log('Removing User with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.USER_SERVICE_API + id)
                    .then(
                        function (response) {
                        	loadUsers(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing User with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();