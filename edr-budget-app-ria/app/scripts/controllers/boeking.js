'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boeking', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'boeking/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekingCtrl', ['$scope', 'Boeking', 'Bankrekening', 'Boekrekening', function ($scope, Boeking, Bankrekening, Boekrekening) {
    
	$scope.boekingen = Boeking.query({'jaar' : new Date().getFullYear()});
    $scope.bankrekeningen = Bankrekening.query();
    $scope.boekrekeningen = Boekrekening.query();
    
    $scope.bg = {};
    
    $scope.jaar = new Date().getFullYear();
    
    $scope.save = function() {
    	var param;
    	if ($scope.bg.id)
    		param = {'id': $scope.bg.id};
    	else
    		param = {};
    	Boeking.save(param, $scope.bg, 
    			function(data, responseHeaders)
    			{
					$scope.boekingen = Boeking.query({'jaar' : $scope.jaar});
				},
				function(error)
				{
					$scope.boekingen = Boeking.query({'jaar' : $scope.jaar});
					alert("Fout gebeurd...");
				});
    	$scope.bg = {};
    };
    
    $scope.verwijderen = function(index) {
    	Boeking.remove({ id:$scope.boekingen[index].id }, 
			function(data, responseHeaders)
			{
				$scope.boekingen = Boeking.query({'jaar' : $scope.jaar});
			},
			function(error)
			{
				$scope.boekingen = Boeking.query({'jaar' : $scope.jaar});
				alert("Fout gebeurd...");
			});
    };
    
    $scope.bijwerken = function(index) {
    	$scope.bg = $scope.boekingen[index];
    };
    
    $scope.refresh = function(){
    	$scope.boekingen = Boeking.query({'jaar' : $scope.jaar});
    };
    
    $scope.reset = function(){
    	$scope.bg = {};
    };
    
  }]);
