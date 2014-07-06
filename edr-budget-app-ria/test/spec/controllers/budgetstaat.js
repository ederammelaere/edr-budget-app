'use strict';

describe('Controller: BudgetstaatCtrl', function () {

  // load the controller's module
  beforeEach(module('edrBudgetAppRiaApp'));

  var BudgetstaatCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BudgetstaatCtrl = $controller('BudgetstaatCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
