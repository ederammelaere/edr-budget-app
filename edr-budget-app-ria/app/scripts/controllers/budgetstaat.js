'use strict';

angular.module('edrBudgetAppRiaApp').factory('BudgetStaat', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'budgetstaat?jaar=:jaar', {jaar:'@jaar'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BudgetstaatCtrl', ['$scope', 'BudgetStaat', function ($scope, BudgetStaat) {

    var currJaar = 0;
    var currReferentieJaar = 0;

	  function refresh()
	  {
      if (currJaar == $scope.jaar && currReferentieJaar == $scope.referentieJaar)
        return;

      currJaar = $scope.jaar;
      currReferentieJaar = $scope.referentieJaar;

		  $scope.budgetstaat = BudgetStaat.query({'jaar' : $scope.jaar, 'referentieJaar' : $scope.referentieJaar});
	  }

	  $scope.jaar = new Date().getFullYear();
    $scope.referentieJaar = $scope.jaar - 1;
	  refresh();

	  $scope.$watch("jaar", function(newValue, oldValue){
			if (newValue === oldValue) return;
		  if (newValue > 2000 && newValue < 3000) { $scope.referentieJaar = $scope.jaar - 1 ; refresh(); } });

    $scope.$watch("referentieJaar", function(newValue, oldValue){
      if (newValue === oldValue) return;
      if (newValue > 2000 && newValue < 3000) { refresh(); } });
  }]);
