'use strict';

angular.module('edrBudgetAppRiaApp').factory('BudgetStaat', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'budgetstaat?jaar=:jaar', {jaar:'@jaar'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BudgetstaatCtrl', ['$scope', 'BudgetStaat', function ($scope, BudgetStaat) {
    $scope.budgetstaat = BudgetStaat.query({'jaar' : new Date().getFullYear()});
    $scope.jaar = new Date().getFullYear();
    $scope.refresh = function(){
    	$scope.budgetstaat = BudgetStaat.query({'jaar' : $scope.jaar});
    };
  }]);
