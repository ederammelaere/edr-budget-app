'use strict';

angular.module('edrBudgetAppRiaApp').factory('Journaal', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'journaal/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('JournaalCtrl', ['$scope', 'Journaal', 'Boekrekening', 'baseRestPath', function ($scope, Journaal, Boekrekening, baseRestPath) {
	  
	function refresh()
	{
		$scope.journaal = Journaal.query({'jaar' : $scope.jaar});
	}
	
	function restBedrag(index)
	{
		var bedrag = $scope.journaal[index].bedrag;
    	for (var i=0; i<$scope.journaal[index].boekingen.length; i++)
    	{
    		bedrag = bedrag - $scope.journaal[index].boekingen[i].bedrag;
    	}
    	$scope.newboeking.bedrag = Math.round(bedrag*100)/100;
	}
	
	$scope.jaar = new Date().getFullYear();
	refresh();
	
	// we werken hier met een timeout omdat bij gebruik van de pijltjes anders twee units ineens
	// omhoog geteld wordt
	$scope.$watch("jaar", function(newValue){ 
		setTimeout(function(){if (newValue > 2000 && newValue < 3000) refresh(); },500);});
	  
	$scope.baseRestPath = baseRestPath;
    
    $scope.boekrekeningen = Boekrekening.query();
        
    $scope.editeerZichtbaar = new Array;
    $scope.newboeking = {};
    $scope.geboekt = new Array;
    for (var i=0; i<$scope.journaal.length; i++)
    {
    	$scope.editeerZichtbaar[i] = false;
    }
    
    $scope.isGeboekt = function(index)
    {
    	return $scope.journaal[index].boekingen.length > 0 ? "V" : "X";
    };
    
    $scope.bewerken = function(index){
    	if ($scope.editeerZichtbaar[index])
    	{
    		$scope.editeerZichtbaar[index] = false;
    		return;
    	}
    	
    	for (var i=0; i<$scope.journaal.length; i++)
        {
        	$scope.editeerZichtbaar[i] = false;
        }
    	$scope.editeerZichtbaar[index] = true;
    	$scope.newboeking = {};
    	restBedrag(index);
    };
    
    $scope.toevoegen = function(index){
    	$scope.newboeking.boekrekening.id = parseInt($scope.newboeking.boekrekening.id, 10);
    	$scope.journaal[index].boekingen.push($scope.newboeking);
    	$scope.newboeking = {};
    	
    	restBedrag(index);
    };
        
    $scope.verwijderen = function(journaalindex, boekingindex){
    	$scope.journaal[journaalindex].boekingen.splice(boekingindex, 1);
    	restBedrag(journaalindex);
    };
    
    $scope.save = function(index){
    	for (var i=0; i<$scope.journaal[index].boekingen.length; i++)
    	{
    		delete $scope.journaal[index].boekingen[i].isJournaalSet;
    	}
    	
    	Journaal.save({'id': $scope.journaal[index].id}, $scope.journaal[index], 
    			null,errorHandler);
    };
  }]);
