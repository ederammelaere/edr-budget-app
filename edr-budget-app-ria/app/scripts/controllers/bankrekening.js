'use strict';

angular.module('edrBudgetAppRiaApp').factory('Bankrekening', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'bankrekening/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BankrekeningCtrl', ['$scope', 'Bankrekening', function ($scope, Bankrekening) {
    $scope.bankrekeningen = Bankrekening.query();
    
    $scope.br = {};
    
    $scope.save = function() {
    	var param;
    	if ($scope.br.id)
    		param = {'id': $scope.br.id};
    	else
    		param = {};
    	Bankrekening.save(param, $scope.br, 
    			function(data, responseHeaders)
    			{
					$scope.bankrekeningen = Bankrekening.query();
				},
				function(error)
				{
					$scope.bankrekeningen = Bankrekening.query();
					alert("Fout gebeurd...");
				});
    	$scope.br = {};
    };
    
    $scope.verwijderen = function(index) {
    	Bankrekening.remove({ id:$scope.bankrekeningen[index].id }, 
			function(data, responseHeaders)
			{
				$scope.bankrekeningen = Bankrekening.query();
			},
			function(error)
			{
				$scope.bankrekeningen = Bankrekening.query();
				alert("Fout gebeurd...");
			});
    };
    
    $scope.bijwerken = function(index) {
    	$scope.br = $scope.bankrekeningen[index];
    };
    
    $scope.reset = function(){
    	$scope.br = {};
    };
    
  }]);
