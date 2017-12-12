(function(){
'use strict';
var app = angular.module('my-app');
app.service('PlanTypeService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllPlanTypes: loadAllPlanTypes,
                loadPlanTypes: loadPlanTypes,
                getAllPlanTypes: getAllPlanTypes,
                getPlanType: getPlanType,
                createPlanType: createPlanType,
                updatePlanType: updatePlanType,
                removePlanType: removePlanType
            };

            return factory;

            function loadAllPlanTypes() {
                console.log('Fetching all planTypes');
                var deferred = $q.defer();
                $http.get(urls.PLANTYPE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all planTypes');
                            $localStorage.planTypes = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading planTypes');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function loadPlanTypes(pageNo, length, search, order) {
                console.log('Fetching  planTypes');
                var pageable = {
                  		 page:pageNo, size:length,sort: order,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.PLANTYPE_SERVICE_API,  config)
                    .then(
                        function (response) {
                        	 $localStorage.planTypes = response.data.content;
                            console.log('Fetched successfully  insurances');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading insurances');
                            return   errResponse ;
                        }
                    );
            }
            

            function getAllPlanTypes(){
            	console.log('$localStorage.planTypes');
                return $localStorage.planTypes;
            }

            function getPlanType(id) {
                console.log('Fetching PlanType with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.PLANTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully PlanType with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createPlanType(user) {
                console.log('Creating PlanType');
                var deferred = $q.defer();
                $http.post(urls.PLANTYPE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllPlanTypes();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating PlanType : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updatePlanType(user, id) {
                console.log('Updating PlanType with id '+id);
                var deferred = $q.defer();
                $http.put(urls.PLANTYPE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllPlanTypes();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating PlanType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removePlanType(id) {
                console.log('Removing PlanType with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.PLANTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllPlanTypes();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing PlanType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
})();