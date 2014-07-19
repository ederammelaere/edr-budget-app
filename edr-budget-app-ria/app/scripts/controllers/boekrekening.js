'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boekrekening', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'boekrekening/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekrekeningCtrl', ['$scope', 'Boekrekening', function ($scope, Boekrekening) {
    $scope.boekrekeningen = Boekrekening.query();
    
    $scope.br = {};
    
    $scope.save = function() {
    	if (!$scope.br.budgeteerbaar)
    	{
    		$scope.br.budgeteerbaar = false;
    	}
    	if (!$scope.br.boekbaar)
    	{
    		$scope.br.boekbaar = false;
    	}
    	var param;
    	if ($scope.br.id)
    		param = {'id': $scope.br.id};
    	else
    		param = {};
    	Boekrekening.save(param, $scope.br, 
    			function(data, responseHeaders)
    			{
					$scope.boekrekeningen = Boekrekening.query();
				},
				function(error)
				{
					$scope.boekrekeningen = Boekrekening.query();
					alert("Fout gebeurd...");
				});
    	$scope.br = {};
    };
    
    $scope.verwijderen = function(index) {
    	Boekrekening.remove({ id:$scope.boekrekeningen[index].id }, 
			function(data, responseHeaders)
			{
				$scope.boekrekeningen = Boekrekening.query();
			},
			function(error)
			{
				$scope.boekrekeningen = Boekrekening.query();
				alert("Fout gebeurd...");
			});
    };
    
    $scope.bijwerken = function(index) {
    	$scope.br = $scope.boekrekeningen[index];
    };
  }]);
