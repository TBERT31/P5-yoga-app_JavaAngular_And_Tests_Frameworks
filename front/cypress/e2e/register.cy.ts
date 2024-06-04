/// <reference types="cypress" />

describe('Register spec', () => {
    beforeEach(() => {
      cy.visit('/register');
    });
  
    it('Disabled submit button on empty form submission', () => {
        cy.get('button[type=submit]').should('be.disabled');
    

        cy.get('input[formControlName=firstName]').clear().blur();;
        cy.get('input[formControlName=lastName]').clear().blur();
        cy.get('input[formControlName=email]').clear().blur();
        cy.get('input[formControlName=password]').clear().blur();
  
        cy.get('button[type=submit]').should('be.disabled');
    });
  
    it('Disabled submit button on invalid email format', () => {
        cy.get('input[formControlName=firstName]').type('John').blur();;
        cy.get('input[formControlName=lastName]').type('Doe').blur();
        cy.get('input[formControlName=email]').type('invalid-email').blur();
        cy.get('input[formControlName=password]').type('password123').blur();

        cy.get('button[type=submit]').should('be.disabled');
    });
  
    it('Enables submit button when form is valid', () => {

      cy.get('input[formControlName=firstName]').type('John');
      cy.get('input[formControlName=lastName]').type('Doe');
      cy.get('input[formControlName=email]').type('john.doe@example.com');
      cy.get('input[formControlName=password]').type('Password123');
  
      cy.get('button[type=submit]').should('not.be.disabled');
    });
  
    it('Submits the form successfully', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 200,
      }).as('registerRequest');
  
      cy.get('input[formControlName=firstName]').type('John');
      cy.get('input[formControlName=lastName]').type('Doe');
      cy.get('input[formControlName=email]').type('john.doe@example.com');
      cy.get('input[formControlName=password]').type('Password123');
      cy.get('button[type=submit]').click();
  
      cy.wait('@registerRequest').its('response.statusCode').should('eq', 200);
      cy.url().should('include', '/login');
    });
  
    it('Shows error on registration failure', () => {
      cy.intercept('POST', '/api/auth/register', {
        statusCode: 400,
        body: {
          message: 'Registration failed'
        },
      }).as('registerRequest');
  
      cy.get('input[formControlName=firstName]').type('John');
      cy.get('input[formControlName=lastName]').type('Doe');
      cy.get('input[formControlName=email]').type('john.doe@example.com');
      cy.get('input[formControlName=password]').type('Password123');
      cy.get('button[type=submit]').click();
  
      cy.wait('@registerRequest').its('response.statusCode').should('eq', 400);
      cy.get('.error').should('contain', 'An error occurred');
    });
  });
  