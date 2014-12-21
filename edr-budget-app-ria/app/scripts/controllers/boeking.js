'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boeking', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'boeking/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekingCtrl', ['$scope', 'Boeking', 'MyModalWindow', function ($scope, Boeking, MyModalWindow) {
    
	function refresh()
	{
		$scope.boekingen = Boeking.query({'jaar' : $scope.jaar});
	}
	  
	$scope.jaar = new Date().getFullYear();
	refresh();
			
	$scope.$watch("jaar", function(newValue, oldValue){
		if (newValue === oldValue) return; 
		if (newValue > 2000 && newValue < 3000) refresh(); });
    
    $scope.save = function(formObj) {
    	formObj.datum = formObj.datum.split("-").reverse().toString().replace(/,/g, "/");
    	Boeking.save(addIdObject(formObj), formObj, succesHandler(refresh), errorHandler); 
    };
    
    $scope.verwijderen = function(index) {
    	Boeking.remove({ id:$scope.boekingen[index].id }, succesHandler(refresh), errorHandler);
    };
    
    $scope.bijwerken = function(index) {
    	var formObj = angular.copy($scope.boekingen[index]);
    	formObj.datum = formObj.datum.split("/").reverse().toString().replace(/,/g, "-");
    	MyModalWindow.openModal(formObj, $scope.save, 'boekingModal.html');
    };
    
    $scope.toevoegen = function() {
    	MyModalWindow.openModal({}, $scope.save, 'boekingModal.html');
    };
        
  }]);
