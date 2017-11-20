'use strict';

app.service('GenderService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllGenders: loadAllGenders,
                getAllGenders: getAllGenders,
                getGender: getGender,
                createGender: createGender,
                updateGender: updateGender,
                removeGender: removeGender
            };

            return factory;

            function loadAllGenders() {
                console.log('Fetching all genders');
                var deferred = $q.defer();
                $http.get(urls.GENDER_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all genders');
                            $localStorage.genders = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading genders');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllGenders(){
            	console.log('$localStorage.genders');
                return $localStorage.genders;
            }

            function getGender(id) {
                console.log('Fetching Gender with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.GENDER_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Gender with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createGender(user) {
                console.log('Creating Gender');
                var deferred = $q.defer();
                $http.post(urls.GENDER_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllGenders();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Gender : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateGender(user, id) {
                console.log('Updating Gender with id '+id);
                var deferred = $q.defer();
                $http.put(urls.GENDER_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllGenders();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Gender with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeGender(id) {
                console.log('Removing Gender with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.GENDER_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllGenders();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Gender with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);