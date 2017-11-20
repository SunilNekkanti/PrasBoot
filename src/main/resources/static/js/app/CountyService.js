'use strict';

app.service('CountyService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllCounties: loadAllCounties,
                getAllCounties: getAllCounties,
                getCounty: getCounty,
                createCounty: createCounty,
                updateCounty: updateCounty,
                removeCounty: removeCounty
            };

            return factory;

            function loadAllCounties() {
                console.log('Fetching all countys');
                var deferred = $q.defer();
                $http.get(urls.COUNTY_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all countys');
                            $localStorage.counties = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading countys');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllCounties(){
            	console.log('$localStorage.countys');
                return $localStorage.counties;
            }

            function getCounty(id) {
                console.log('Fetching County with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.COUNTY_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully County with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createCounty(user) {
                console.log('Creating County');
                var deferred = $q.defer();
                $http.post(urls.COUNTY_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllCountys();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating County : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateCounty(user, id) {
                console.log('Updating County with id '+id);
                var deferred = $q.defer();
                $http.put(urls.COUNTY_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllCountys();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating County with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeCounty(id) {
                console.log('Removing County with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.COUNTY_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllCountys();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing County with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);