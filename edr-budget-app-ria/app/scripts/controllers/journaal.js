'use strict';

angular.module('edrBudgetAppRiaApp').factory('Journaal', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'journaal/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('JournaalCtrl', ['$scope', 'Journaal', 'Boekrekening', 'baseRestPath', '$modal', '$sce', 
                function ($scope, Journaal, Boekrekening, baseRestPath, $modal, $sce) {
	  
	function refresh()
	{
		$scope.journaal = Journaal.query({'jaar' : $scope.jaar});
	}
	
	$scope.jaar = new Date().getFullYear();
	refresh();
	
	// we werken hier met een timeout omdat bij gebruik van de pijltjes anders twee units ineens
	// omhoog geteld wordt
	$scope.$watch("jaar", function(newValue, oldValue){
		if (newValue === oldValue) return; 
		setTimeout(function(){if (newValue > 2000 && newValue < 3000) refresh(); },500);});
	  
	$scope.baseRestPath = baseRestPath;
    
    $scope.isGeboekt = function(index)
    {
    	return $scope.journaal[index].boekingen.length > 0 ? "V" : "X";
    };
    
    $scope.bewerken = function(index){
    	var journaalitem = angular.copy($scope.journaal[index]);
    	$scope.openModal(journaalitem);
    };
            
    $scope.save = function(journaalitem){
    	Journaal.save({'id': journaalitem.id}, journaalitem, 
    			succesHandler(refresh),errorHandler);
    };
    
    $scope.openModal = function(journaalitem) {
		var modalInstance = $modal.open({
			templateUrl: 'journaalModal.html',
			controller: 'JournaalModalInstanceCtrl',
			windowClass: 'app-modal-window',
			resolve: {
				journaalitem: function () {
					return journaalitem;
				}
			}
		});
    
		modalInstance.result.then(function (journaalitem) {
			$scope.save(journaalitem);
		});
	
    };
    
  }]);

angular.module('edrBudgetAppRiaApp')
	.controller('JournaalModalInstanceCtrl', ['$scope', '$modalInstance', 'journaalitem', 'Bankrekening', 'Boekrekening', 
                                  function ($scope, $modalInstance, journaalitem, Bankrekening, Boekrekening) {
		
	var restBedrag = function(journaalitem)
	{
		var bedrag = journaalitem.bedrag;
    	for (var i=0; i<journaalitem.boekingen.length; i++)
    	{
    		bedrag = bedrag - journaalitem.boekingen[i].bedrag;
    	}
    	$scope.newboeking.bedrag = Math.round(bedrag*100)/100;
	};
		
	$scope.journaalitem = journaalitem;
	$scope.newboeking = {};
	restBedrag($scope.journaalitem);
	
	$scope.initBankrekeningen = function(){
		$scope.bankrekeningen = Bankrekening.query();		
	};
	
	$scope.initBoekrekeningen = function(){
		$scope.boekrekeningen = Boekrekening.query();		
	};
	
    $scope.verwijderen = function(index){
    	$scope.journaalitem.boekingen.splice(index, 1);
    	restBedrag($scope.journaalitem);
    };
    
    $scope.toevoegen = function(index){
    	if (!$scope.newboeking.omschrijving)
    		return;
    	
    	if (!$scope.newboeking.boekrekening)
    		return;
    	
    	$scope.newboeking.boekrekening.id = parseInt($scope.newboeking.boekrekening.id, 10);
    	$scope.journaalitem.boekingen.push($scope.newboeking);
    	$scope.newboeking = {};
    	
    	restBedrag($scope.journaalitem);
    };
    		
	$scope.reset = function(){
		$modalInstance.dismiss('cancel');
	};
	
	$scope.save = function(){
		$modalInstance.close($scope.journaalitem);
	};	    
}]);
