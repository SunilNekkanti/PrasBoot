'use strict';

app.service('LeadService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllLeads: loadAllLeads,
                loadLeads: loadLeads,
                getAllLeads: getAllLeads,
                getLead: getLead,
                createLead: createLead,
                updateLead: updateLead,
                removeLead: removeLead,
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
                            $localStorage.loginUser = response.data;
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while fetching loginUser');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function loadAllLeads() {
                console.log('Fetching all leads');
                var deferred = $q.defer();
                var pageable = {
                  		 page:0, size:20
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
                $http.get(urls.LEAD_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all leads');
                            $localStorage.leads = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading leads');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function loadLeads(pageNo, length, search, order) {
                console.log('Fetching  leads');
                var deferred = $q.defer();
                var pageable = {
                  		 page:pageNo, size:length,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.LEAD_SERVICE_API,  config)
                    .then(
                        function (response) {
                            $localStorage.leads = response.data.content;
                            deferred.resolve(response);
                            return response;
                        },
                        function (errResponse) {
                            console.error('Error while loading leads');
                            deferred.reject(errResponse);
                        }
                    );
            }

            function getAllLeads(){
            	console.log('$localStorage.leads');
                return $localStorage.leads;
            }

            function getLead(id) {
                console.log('Fetching Lead with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.LEAD_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Lead with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading lead with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createLead(lead) {
                console.log('Creating Lead');
                var deferred = $q.defer();
                $http.post(urls.LEAD_SERVICE_API, lead)
                    .then(
                        function (response) {
                        	loadLeads(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Lead : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateLead(lead, id) {
                console.log('Updating Lead with id '+id);
                var deferred = $q.defer();
                $http.put(urls.LEAD_SERVICE_API + id, lead)
                    .then(
                        function (response) {
                        	loadLeads(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Lead with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeLead(id) {
                console.log('Removing Lead with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.LEAD_SERVICE_API + id)
                    .then(
                        function (response) {
                        	loadLeads(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Lead with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);