(function(){
'use strict';
var app = angular.module('my-app');
   
    app.service('FileUploadService', ['$http', '$q', 'urls', function ($http, $q, urls) {
    	
    	 var factory = {
    			 getFileUpload : getFileUpload,
    			 uploadFileToUrl: uploadFileToUrl,
    			 uploadContractFileToUrl: uploadContractFileToUrl,
    			 uploadConsentFormFileToUrl : uploadConsentFormFileToUrl
             };

             return factory;
             
    	 function getFileUpload(id) {
             console.log('Fetching Lead with id :'+id);
             var deferred = $q.defer();
             $http.get(urls.FILE_UPLOADED_SERVICE_API + id , {responseType: 'arraybuffer'})
                 .then(
                     function (response) {
                         console.log('Fetched successfully File response');
                         
                         var uri = urls.FILE_UPLOADED_SERVICE_API + id;
                         var link = angular.element('<a href="' + uri + '" target="_blank"></a>');

                         angular.element(document.body).append(link);

                         link[0].click();
                         link.remove();
                         
                         
                         deferred.resolve(response.data);
                     },
                     function (errResponse) {
                         console.error('Error while loading File with id :'+id);
                         deferred.reject(errResponse);
                     }
                 );
             return deferred.promise;
         }
    	 

	    function uploadConsentFormFileToUrl(files) {
            //FormData, object of key/value pair for form fields and values
        	
    		
            var fileFormData = new FormData();
            if(files.length>0 ){
            	   //  fileFormData.append('files', files);
           	 $.each(files, function(i, file) {
           		fileFormData.append('files', file);
           	  });
            }else{
            	fileFormData.append('files', files);
            }
            
             
            var deffered = $q.defer();
             		
            $http.post(urls.CONSENT_FORM_FILE_UPLOADER, fileFormData, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}

            }).success(function (response) {
                deffered.resolve(response);

            }).error(function (response) {
                deffered.reject(response);
            });

            return deffered.promise;
        }
        
    	 function uploadFileToUrl(files, insId, fileTypeId,activityMonth) {
            //FormData, object of key/value pair for form fields and values
        	
    		
            var fileFormData = new FormData();
            if(files.length>0 ){
            	   //  fileFormData.append('files', files);
           	 $.each(files, function(i, file) {
           		fileFormData.append('files', file);
           	  });
            }else{
            	fileFormData.append('file', files);
            }
            fileFormData.append('insId',insId);
            fileFormData.append('activityMonth',activityMonth);
            fileFormData.append('fileTypeCode',fileTypeId);
             
            var deffered = $q.defer();
             		
            $http.post(urls.FILE_UPLOADER, fileFormData, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}

            }).success(function (response) {
                deffered.resolve(response);

            }).error(function (response) {
                deffered.reject(response);
            });

            return deffered.promise;
        }
    	 
    	 function uploadContractFileToUrl(files) {
             //FormData, object of key/value pair for form fields and values
         	
     		
             var fileFormData = new FormData();
             if(files.length>0 ){
             	   //  fileFormData.append('files', files);
            	 $.each(files, function(i, file) {
            		fileFormData.append('files', file);
            	  });
             }else{
             	fileFormData.append('file', files);
             }
              
             var deffered = $q.defer();
              		
             $http.post(urls.CONTRACT_FILE_UPLOADER, fileFormData, {
                 transformRequest: angular.identity,
                 headers: {'Content-Type': undefined}

             }).success(function (response) {
                 deffered.resolve(response);

             }).error(function (response) {
                 deffered.reject(response);
             });

             return deffered.promise;
         }
   }
    ]);
       })();