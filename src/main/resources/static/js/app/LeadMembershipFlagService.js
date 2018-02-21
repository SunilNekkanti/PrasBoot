(function(){
'use strict';
var app = angular.module('my-app');

app.service('LeadMembershipFlagService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                getLeadMembershipFlag: getLeadMembershipFlag,
                createLeadMembershipFlag: createLeadMembershipFlag,
                updateLeadMembershipFlag: updateLeadMembershipFlag,
            };

            return factory;


            function getLeadMembershipFlag(id) {
                console.log('Fetching LeadMembershipFlag with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.LEAD_FLAG_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully LeadMembershipFlag with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading leadMembershipFlag with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createLeadMembershipFlag(user) {
                console.log('Creating LeadMembershipFlag');
                var deferred = $q.defer();
                $http.post(urls.LEAD_FLAG_SERVICE_API, user)
                    .then(
                        function (response) {
                        	loadLeadMembershipFlags(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating LeadMembershipFlag : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateLeadMembershipFlag(user, id) {
                console.log('Updating LeadMembershipFlag with id '+id);
                var deferred = $q.defer();
                $http.put(urls.LEAD_FLAG_SERVICE_API + id, user)
                    .then(
                        function (response) {
                        	loadLeadMembershipFlags(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating LeadMembershipFlag with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
          
        }
    ]);
   })();
