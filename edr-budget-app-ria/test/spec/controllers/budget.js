'use strict';

describe('Controller: BudgetCtrl', function () {

  // load the controller's module
  beforeEach(module('edrBudgetAppRiaApp'));

  var BudgetCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BudgetCtrl = $controller('BudgetCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
