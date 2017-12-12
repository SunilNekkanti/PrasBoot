(function(){
'use strict';
var app = angular.module('my-app');

app.service('ProblemService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllProblems: loadAllProblems,
                loadProblems: loadProblems,
                getAllProblems: getAllProblems,
                getProblem: getProblem,
                createProblem: createProblem,
                updateProblem: updateProblem,
                removeProblem: removeProblem
            };

            return factory;

            function loadAllProblems() {
                console.log('Fetching all problems');
                var deferred = $q.defer();
                $http.get(urls.PROBLEM_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all problems');
                            $localStorage.problems = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading problems');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadProblems(pageNo, length, search, order, insId, effectiveYear) {
                console.log('Fetching  Problems');
                var pageable = {
                  		 page:pageNo, size:length,sort:order, search: search||'',insId:insId,effectiveYear:effectiveYear
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.PROBLEM_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  problems');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading problems');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllProblems(){
            	console.log('$localStorage.problems');
                return $localStorage.problems;
            }

            function getProblem(id) {
                console.log('Fetching Problem with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.PROBLEM_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Problem with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createProblem(user) {
                console.log('Creating Problem');
                var deferred = $q.defer();
                $http.post(urls.PROBLEM_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllProblems();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Problem : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateProblem(user, id) {
                console.log('Updating Problem with id '+id);
                var deferred = $q.defer();
                $http.put(urls.PROBLEM_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllProblems();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Problem with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeProblem(id) {
                console.log('Removing Problem with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.PROBLEM_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllProblems();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Problem with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();