(function(){
'use strict';
var app = angular.module('my-app');

app.service('MembershipService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllMemberships: loadAllMemberships,
                loadMemberships: loadMemberships,
                getAllMemberships: getAllMemberships,
                getMembership: getMembership,
                createMembership: createMembership,
                updateMembership: updateMembership,
                removeMembership: removeMembership
            };

            return factory;

            function loadAllMemberships(insId, prvdrId) {
                console.log('Fetching all memberships');
                var deferred = $q.defer();
                var pageable = {
                 		 page:0, size:20, insId:insId,prvdrId:prvdrId
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
                $http.get(urls.MEMBERSHIP_SERVICE_API, config )
                    .then(
                        function (response) {
                            console.log('Fetched successfully all memberships');
                            if (localStorage.getItem("memberships") === null) {
                            	 $localStorage.memberships = response.data.content;
                           }else {
                           	localStorage.removeItem("memberships") ;
                             	$localStorage.memberships = response.data.content;
                           }
                            
                           
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading memberships');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadMemberships(pageNo, length, search, order, insId, prvdrId, effectiveYear,problemIds) {
                console.log('Fetching  Memberships');
                var pageable = {
                		 page:pageNo, size:length,sort: order,search: search||'',insId:insId,prvdrId:prvdrId, effectiveYear:effectiveYear ,problemIds:problemIds
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.MEMBERSHIP_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  memberships');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading memberships');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllMemberships(){
            	console.log('$localStorage.memberships');
                return $localStorage.memberships;
            }

            function getMembership(id) {
                console.log('Fetching Membership with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.MEMBERSHIP_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Membership with id :'+id);
                            deferred.resolve(response.data);
                            return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createMembership(user) {
                console.log('Creating Membership');
                var deferred = $q.defer();
                $http.post(urls.MEMBERSHIP_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllMemberships();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Membership : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateMembership(user, id) {
                console.log('Updating Membership with id '+id);
                var deferred = $q.defer();
                $http.put(urls.MEMBERSHIP_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllMemberships();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Membership with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeMembership(id) {
                console.log('Removing Membership with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.MEMBERSHIP_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllMemberships();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Membership with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();