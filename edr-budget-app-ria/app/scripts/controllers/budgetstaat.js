'use strict';

angular.module('edrBudgetAppRiaApp').factory('BudgetStaat', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'budgetstaat?jaar=:jaar', {jaar:'@jaar'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BudgetstaatCtrl', ['$scope', 'BudgetStaat', function ($scope, BudgetStaat) {
	  
	  function refresh()
	  {
		  $scope.budgetstaat = BudgetStaat.query({'jaar' : $scope.jaar});
	  }
			
	  $scope.jaar = new Date().getFullYear();
	  refresh();
		
	  $scope.$watch("jaar", function(newValue){ 
		  if (newValue > 2000 && newValue < 3000) refresh(); });
  }]);
