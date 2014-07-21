'use strict';

angular.module('edrBudgetAppRiaApp').factory('Journaal', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'journaal/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('JournaalCtrl', ['$scope', 'Journaal', 'Boekrekening', 'baseRestPath', function ($scope, Journaal, Boekrekening, baseRestPath) {
	$scope.baseRestPath = baseRestPath;
    $scope.journaal = Journaal.query({'jaar' : new Date().getFullYear()});
    $scope.boekrekeningen = Boekrekening.query();
    $scope.jaar = new Date().getFullYear();
    $scope.refresh = function(){
    	$scope.journaal = Journaal.query({'jaar' : $scope.jaar});
    };
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
    	for (var i=0; i<$scope.journaal.length; i++)
        {
        	$scope.editeerZichtbaar[i] = false;
        }
    	$scope.editeerZichtbaar[index] = true;
    	var bedrag = $scope.journaal[index].bedrag;
    	for (var i=0; i<$scope.journaal[index].boekingen.length; i++)
    	{
    		bedrag = bedrag - $scope.journaal[index].boekingen[i].bedrag;
    	}
    	$scope.newboeking = {};
    	$scope.newboeking.bedrag = Math.round(bedrag*100)/100;
    };
    
    $scope.toevoegen = function(index){
    	$scope.newboeking.boekrekening.id = parseInt($scope.newboeking.boekrekening.id, 10);
    	$scope.journaal[index].boekingen.push($scope.newboeking);
    	$scope.newboeking = {};
    	
    	var bedrag = $scope.journaal[index].bedrag;
    	for (var i=0; i<$scope.journaal[index].boekingen.length; i++)
    	{
    		bedrag = bedrag - $scope.journaal[index].boekingen[i].bedrag;
    	}
    	$scope.newboeking.bedrag = Math.round(bedrag*100)/100;
    };
        
    $scope.verwijderen = function(journaalindex, boekingindex){
    	$scope.journaal[journaalindex].boekingen.splice(boekingindex, 1);
    };
    
    $scope.save = function(index){
    	Journaal.save({'id': $scope.journaal[index].id}, $scope.journaal[index], 
    			null,
				function(error)
				{
					alert("Fout gebeurd...");
				});

    }
  }]);
