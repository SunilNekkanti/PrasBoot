'use strict';

app.service('FacilityTypeService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllFacilityTypes: loadAllFacilityTypes,
                loadFacilityTypes : loadFacilityTypes,
                getAllFacilityTypes: getAllFacilityTypes,
                getFacilityType: getFacilityType,
                createFacilityType: createFacilityType,
                updateFacilityType: updateFacilityType,
                removeFacilityType: removeFacilityType
            };

            return factory;

            function loadFacilityTypes(pageNo, length, search, order) {
                console.log('Fetching  facilityTypes');
                var deferred = $q.defer();
                var pageable = {
                 		 page:pageNo, size:length,search: search||''
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.FACILITYTYPE_SERVICE_API, config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  facilityTypes');
                            $localStorage.facilityTypes = response.data.content;
                            deferred.resolve(response);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading facilityTypes');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
            
            function loadAllFacilityTypes() {
                console.log('Fetching all facilityTypes');
                var deferred = $q.defer();
                $http.get(urls.FACILITYTYPE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all facilityTypes');
                            $localStorage.facilityTypes = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading facilityTypes');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllFacilityTypes(){
            	console.log('$localStorage.facilityTypes');
                return $localStorage.facilityTypes;
            }

            function getFacilityType(id) {
                console.log('Fetching FacilityType with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.FACILITYTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully FacilityType with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading facilityType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createFacilityType(user) {
                console.log('Creating FacilityType');
                var deferred = $q.defer();
                $http.post(urls.FACILITYTYPE_SERVICE_API, user)
                    .then(
                        function (response) {
                        	loadFacilityTypes(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating FacilityType : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateFacilityType(user, id) {
                console.log('Updating FacilityType with id '+id);
                var deferred = $q.defer();
                $http.put(urls.FACILITYTYPE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                        	loadFacilityTypes(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating FacilityType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeFacilityType(id) {
                console.log('Removing FacilityType with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.FACILITYTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                        	loadFacilityTypes(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing FacilityType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);