'use strict';

var edrBudgetAppRiaApp = angular.module('edrBudgetAppRiaApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute'
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

function errorHandler(error)
{
	alert("Fout gebeurd...");
}

function succesHandler(functie)
{
	return function(){functie()};
}
