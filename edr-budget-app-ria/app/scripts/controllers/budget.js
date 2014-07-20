'use strict';

angular.module('edrBudgetAppRiaApp').factory('Budget', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'budget/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BudgetCtrl', ['$scope', 'Budget', 'Boekrekening', function ($scope, Budget, Boekrekening) {
    
	$scope.budgetten = Budget.query({'jaar' : new Date().getFullYear()});
    $scope.boekrekeningen = Boekrekening.query();
    
    $scope.bt = {};
    
    $scope.jaar = new Date().getFullYear();
    
    $scope.save = function() {
    	var param;
    	if ($scope.bt.id)
    		param = {'id': $scope.bt.id};
    	else
    		param = {};
    	Budget.save(param, $scope.bt, 
    			function(data, responseHeaders)
    			{
					$scope.budgetten = Budget.query({'jaar' : $scope.jaar});
				},
				function(error)
				{
					$scope.budgetten = Budget.query({'jaar' : $scope.jaar});
					alert("Fout gebeurd...");
				});
    	$scope.bt = {};
    };
    
    $scope.verwijderen = function(index) {
    	Budget.remove({ id:$scope.budgetten[index].id }, 
			function(data, responseHeaders)
			{
    			$scope.budgetten = Budget.query({'jaar' : $scope.jaar});
			},
			function(error)
			{
				$scope.budgetten = Budget.query({'jaar' : $scope.jaar});
				alert("Fout gebeurd...");
			});
    };
    
    $scope.bijwerken = function(index) {
    	$scope.bt = $scope.budgetten[index];
    };
    
    $scope.refresh = function(){
    	$scope.budgetten = Budget.query({'jaar' : $scope.jaar});
    };
    
    $scope.reset = function(){
    	$scope.bt = {};
    };
    
  }]);
