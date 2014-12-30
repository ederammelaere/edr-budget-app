'use strict';

var edrBudgetAppRiaApp = angular.module('edrBudgetAppRiaApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute',
  'ui.bootstrap'
])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/boekrekening', {
        templateUrl: 'views/boekrekening.html',
        controller: 'BoekrekeningCtrl'
      })
      .when('/bankrekening', {
        templateUrl: 'views/bankrekening.html',
        controller: 'BankrekeningCtrl'
      })
      .when('/budgetstaat', {
        templateUrl: 'views/budgetstaat.html',
        controller: 'BudgetstaatCtrl'
      })
      .when('/boeking', {
        templateUrl: 'views/boeking.html',
        controller: 'BoekingCtrl'
      })
      .when('/budget', {
        templateUrl: 'views/budget.html',
        controller: 'BudgetCtrl'
      })
      .when('/journaal', {
        templateUrl: 'views/journaal.html',
        controller: 'JournaalCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

if (window.location.host=='127.0.0.1:9000') {
	edrBudgetAppRiaApp.constant('baseRestPath','http://localhost:8080/edr-budget-app-rest/rest/');
} else {
	edrBudgetAppRiaApp.constant('baseRestPath','/edr-budget-app-rest-prod/rest/');
}

angular.module('edrBudgetAppRiaApp').config(['$sceDelegateProvider', function($sceDelegateProvider) {
	  $sceDelegateProvider.resourceUrlWhitelist([
	    // Allow same origin resource loads.
	    'self',
	    // Allow loading from our assets domain.  Notice the difference between * and **.
	    'http://localhost:8080/edr-budget-app-rest/**'
	  ]);
	}]);

function resetFormObj($scope)
{
	$scope.formObj = {};		
}

function addFalse(formObj, property)
{
	if (!formObj[property])
	{
		formObj[property] = false;
	}
}

function addId($scope)
{
	if ($scope.formObj.id)
		return {'id': $scope.formObj.id};
	else
		return {};
}

function addIdObject(formObj)
{
	if (formObj.id)
		return {'id': formObj.id};
	else
		return {};
}

function errorHandler(error)
{
	alert("Fout gebeurd...");
}

function succesHandler(functie)
{
	return function(){functie();};
}

angular.module('edrBudgetAppRiaApp').factory('MyModalWindow', ['$modal', function($modal){
	return {
		openModal : function (formObj, save, templateUrl) {

			var modalInstance = $modal.open({
				templateUrl: templateUrl,
				controller: 'ModalInstanceCtrl',
				resolve: {
					formObj: function () {
						return formObj;
					}
				}
			});
	    
			modalInstance.result.then(function (formObj) {
				save(formObj);
			});
		}
	};
}]);

angular.module('edrBudgetAppRiaApp')
	.controller('ModalInstanceCtrl', ['$scope', '$modalInstance', 'formObj', 'Bankrekening', 'Boekrekening', 
	                                  function ($scope, $modalInstance, formObj, Bankrekening, Boekrekening) {
	$scope.formObj = formObj;
	$scope.actie = (formObj.id) ? 'Bewerken' : 'Toevoegen';
	
	$scope.initBankrekeningen = function(){
		$scope.bankrekeningen = Bankrekening.query();		
	};
	
	$scope.initBoekrekeningen = function(){
		$scope.boekrekeningen = Boekrekening.query();		
	};
		
	$scope.reset = function(){
    	$modalInstance.dismiss('cancel');
    };
    $scope.save = function(){
    	$modalInstance.close($scope.formObj);
    };	    
}]);
