'use strict';

angular.module('edrBudgetAppRiaApp').factory('Journaal', ['$resource', 'baseRestPath', function ($resource, baseRestPath) {
  return $resource(baseRestPath + 'journaal/:id', {id: '@id'});
}]);

angular.module('edrBudgetAppRiaApp')
  .controller('JournaalCtrl', ['$scope', 'Journaal', 'Boekrekening', 'baseRestPath', '$modal',
    function ($scope, Journaal, Boekrekening, baseRestPath, $modal) {

      function refresh() {
        $scope.journaal = Journaal.query({'jaar': $scope.jaar});
      }

      $scope.jaar = new Date().getFullYear();
      refresh();

      // we werken hier met een timeout omdat bij gebruik van de pijltjes anders twee units ineens
      // omhoog geteld wordt
      $scope.$watch("jaar", function (newValue, oldValue) {
        if (newValue === oldValue) return;
        setTimeout(function () {
          if (newValue > 2000 && newValue < 3000) refresh();
        }, 500);
      });

      $scope.baseRestPath = baseRestPath;

      $scope.enkelOngeboekt = function (journaalitem) {
        if ($scope.toonEnkelOngeboekte)
          return journaalitem.boekingen.length == 0;
        else
          return true;
      };

      $scope.isGeboekt = function (index) {
        return $scope.journaal[index].boekingen.length > 0 ? "V" : "X";
      };

      $scope.bewerken = function (index) {
        var journaalitem = angular.copy($scope.journaal[index]);
        $scope.openModal(journaalitem);
      };

      $scope.save = function (journaalitem) {
        Journaal.save({'id': journaalitem.id}, journaalitem,
          succesHandler(refresh), errorHandler);
      };

      $scope.openModal = function (journaalitem) {
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
  .controller('JournaalModalInstanceCtrl', ['$scope', '$modalInstance', 'journaalitem', 'Bankrekening', 'Boekrekening', '$http', 'baseRestPath',
    function ($scope, $modalInstance, journaalitem, Bankrekening, Boekrekening, $http, baseRestPath) {

      var restBedrag = function (journaalitem) {
        var bedrag = journaalitem.bedrag;
        for (var i = 0; i < journaalitem.boekingen.length; i++) {
          bedrag = bedrag - journaalitem.boekingen[i].bedrag;
        }
        $scope.newboeking.bedrag = Math.round(bedrag * 100) / 100;
      };

      var initialiseer = function (journaalitem) {
        $scope.isNormaal = true;

        if (journaalitem.boekingen.length == 0) {
          // Opzoeken van gelijkaardige boekingen in bestaande input
          $http({
            method: "GET",
            url: baseRestPath + 'journaal/previousBoekingen/' + journaalitem.id
          })
            .success(function (data) {
                for (var index in data) {
                  var boeking = data[index];
                  journaalitem.boekingen.push({
                    boekrekening: boeking.boekrekening,
                    omschrijving: boeking.omschrijving,
                    bankrekening: boeking.bankrekening
                  });
                }

                if (journaalitem.boekingen.length == 2
                  && data[0].bedrag + data[1].bedrag == 0) {
                  var indexRekening = data[0].bankrekening.id == journaalitem.bankrekening.id ? 0 : 1;
                  var indexTegenpartij = indexRekening == 0 ? 1 : 0;
                  journaalitem.boekingen[indexRekening].bedrag = journaalitem.bedrag;
                  journaalitem.boekingen[indexTegenpartij].bedrag = -1 * journaalitem.bedrag;
                } else if (journaalitem.boekingen.length > 1) {
                  journaalitem.boekingen = journaalitem.boekingen.slice(0, 1);
                  journaalitem.boekingen[0].bedrag = journaalitem.bedrag;
                } else if (journaalitem.boekingen.length == 1) {
                  journaalitem.boekingen[0].bedrag = journaalitem.bedrag;
                }

                // recursieve oproep
                if (journaalitem.boekingen.length > 0) {
                  initialiseer(journaalitem);
                }
              }
            );
        }

        if (journaalitem.boekingen.length == 2) {
          if (journaalitem.boekingen[0].bedrag == journaalitem.bedrag) {
            if (journaalitem.boekingen[1].bedrag == -1 * journaalitem.bedrag) {
              $scope.transferboeking.boekrekening = journaalitem.boekingen[0].boekrekening;
              $scope.transferboeking.bankrekening = journaalitem.boekingen[1].bankrekening;
              $scope.transferboeking.omschrijving = journaalitem.boekingen[0].omschrijving;
              $scope.isNormaal = false;
              journaalitem.boekingen = [];
            }
          } else if (journaalitem.boekingen[1].bedrag == journaalitem.bedrag) {
            if (journaalitem.boekingen[0].bedrag == -1 * journaalitem.bedrag) {
              $scope.transferboeking.boekrekening = journaalitem.boekingen[1].boekrekening;
              $scope.transferboeking.bankrekening = journaalitem.boekingen[0].bankrekening;
              $scope.transferboeking.omschrijving = journaalitem.boekingen[1].omschrijving;
              $scope.isNormaal = false;
              journaalitem.boekingen = [];
            }
          }
        }
        $scope.isTransfer = !$scope.isNormaal;

        restBedrag($scope.journaalitem);
      };

      $scope.journaalitem = journaalitem;
      $scope.newboeking = {};
      $scope.transferboeking = {};
      restBedrag($scope.journaalitem);
      initialiseer($scope.journaalitem);

      $scope.bankrekeningen = Bankrekening.query();
      $scope.boekrekeningen = Boekrekening.query();

      $scope.verwijderen = function (index) {
        $scope.journaalitem.boekingen.splice(index, 1);
        restBedrag($scope.journaalitem);
      };

      $scope.toevoegen = function () {
        if (!$scope.newboeking.omschrijving)
          return;

        if (!$scope.newboeking.boekrekening)
          return;

        $scope.newboeking.boekrekening.id = parseInt($scope.newboeking.boekrekening.id, 10);
        $scope.journaalitem.boekingen.push($scope.newboeking);
        $scope.newboeking = {};

        restBedrag($scope.journaalitem);
      };

      $scope.reset = function () {
        $modalInstance.dismiss('cancel');
      };

      $scope.saveNormaal = function () {
        $modalInstance.close($scope.journaalitem);
      };

      $scope.saveTransfer = function () {
        $scope.journaalitem.boekingen = [];
        var boekingVan = {
          boekrekening: {id: $scope.transferboeking.boekrekening.id},
          bankrekening: {id: $scope.journaalitem.bankrekening.id},
          omschrijving: $scope.transferboeking.omschrijving,
          datum: $scope.journaalitem.datum,
          bedrag: $scope.journaalitem.bedrag
        };
        var boekingNaar = {
          boekrekening: {id: $scope.transferboeking.boekrekening.id},
          bankrekening: {id: $scope.transferboeking.bankrekening.id},
          omschrijving: $scope.transferboeking.omschrijving,
          datum: $scope.journaalitem.datum,
          bedrag: -1 * $scope.journaalitem.bedrag
        };
        $scope.journaalitem.boekingen.push(boekingVan);
        $scope.journaalitem.boekingen.push(boekingNaar);
        $modalInstance.close($scope.journaalitem);
      };

      $scope.changeTab = function (tabIndex) {
        $scope.isNormaal = tabIndex == 0;
        $scope.isTransfer = tabIndex == 1;
      };

    }]);
