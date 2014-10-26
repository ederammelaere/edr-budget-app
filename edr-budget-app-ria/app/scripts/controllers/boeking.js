'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boeking', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'boeking/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekingCtrl', ['$scope', 'Boeking', 'Bankrekening', 'Boekrekening', function ($scope, Boeking, Bankrekening, Boekrekening) {
    
	function refresh()
	{
		$scope.boekingen = Boeking.query({'jaar' : $scope.jaar});
	}
	  
	$scope.jaar = new Date().getFullYear();
	refresh();
	resetFormObj($scope);
	
	$scope.$watch("jaar", function(newValue){ 
		if (newValue > 2000 && newValue < 3000) refresh(); });
	
    $scope.bankrekeningen = Bankrekening.query();
    $scope.boekrekeningen = Boekrekening.query();
    
    $scope.save = function() {
    	var param = addId($scope);
    	$scope.formObj.datum = $scope.formObj.datum.split("-").reverse().toString().replace(/,/g, "/");
    	Boeking.save(param, $scope.formObj, succesHandler(refresh), errorHandler); 
    	resetFormObj($scope);
    };
    
    $scope.verwijderen = function(index) {
    	Boeking.remove({ id:$scope.boekingen[index].id }, succesHandler(refresh), errorHandler);
    };
    
    $scope.bijwerken = function(index) {
    	$scope.formObj = angular.copy($scope.boekingen[index]);
    	$scope.formObj.datum = $scope.formObj.datum.split("/").reverse().toString().replace(/,/g, "-");
    };
    
    $scope.reset = function(){
    	resetFormObj($scope);
    };
    
  }]);
