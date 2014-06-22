'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boekrekening', ['$resource', function($resource) {
	  return $resource('http://localhost:8080/edr-budget-app-rest/rest/boekrekening/');
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekrekeningCtrl', ['$scope', 'Boekrekening', function ($scope, Boekrekening) {
    $scope.boekrekeningen = Boekrekening.query();
  }]);
