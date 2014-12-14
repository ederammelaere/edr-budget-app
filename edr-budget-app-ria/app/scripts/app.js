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

function resetFormObj($scope)
{
	$scope.formObj = {};		
}

function addFalse($scope, property)
{
	if (!$scope.formObj[property])
	{
		$scope.formObj[property] = false;
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

function openModal (modal, formObj, scope, templateUrl) {

    var modalInstance = modal.open({
      templateUrl: templateUrl,
      controller: 'ModalInstanceCtrl',
      resolve: {
          formObj: function () {
            return formObj;
          }
        }
    });
    
    modalInstance.result.then(function (formObj) {
        scope.save(formObj);
      });
};

angular.module('edrBudgetAppRiaApp')
	.controller('ModalInstanceCtrl', ['$scope', '$modalInstance', 'formObj', function ($scope, $modalInstance, formObj) {
	$scope.formObj = formObj;
	$scope.actie = (formObj.id) ? 'Bewerken' : 'Toevoegen';
	
	$scope.reset = function(){
    	$modalInstance.dismiss('cancel');
    };
    $scope.save = function(){
    	$modalInstance.close($scope.formObj);
    };	    
}]);
