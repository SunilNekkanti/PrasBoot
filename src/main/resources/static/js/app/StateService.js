'use strict';

app.service('StateService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllStates: loadAllStates,
                getAllStates: getAllStates,
                getState: getState,
                createState: createState,
                updateState: updateState,
                removeState: removeState
            };

            return factory;

            function loadAllStates() {
                console.log('Fetching all States');
                var deferred = $q.defer();
                $http.get(urls.STATE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all States');
                            if(!$localStorage.states)
                            $localStorage.states = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading States');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllStates(){
            	console.log('$localStorage.states');
                return $localStorage.states;
            }

            function getState(id) {
                console.log('Fetching State with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.STATE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully State with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createState(user) {
                console.log('Creating State');
                var deferred = $q.defer();
                $http.post(urls.STATE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllStates();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating State : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateState(user, id) {
                console.log('Updating State with id '+id);
                var deferred = $q.defer();
                $http.put(urls.STATE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllStates();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating State with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeState(id) {
                console.log('Removing State with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.STATE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllStates();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing State with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);