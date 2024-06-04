/// <reference types="cypress" />

describe('Login spec', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('Disabled submit button on empty form submission', () => {
    cy.get('button[type=submit]').should('be.disabled');
    cy.get('input[formControlName=email]').type('hello@test.com');
    cy.get('input[formControlName=password]').type('hellopassword123');
    
    cy.get('input[formControlName=email]').clear().blur();
    cy.get('input[formControlName=password]').clear().blur();
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('Disabled submit button on invalid email format', () => {
    cy.get('input[formControlName=email]').type("invalid-email");
    cy.get('input[formControlName=password]').type("password123");
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('Toggles password visibility', () => {
    cy.get('input[formControlName=password]').type("password123");
    cy.get('button[mat-icon-button]').click();
    cy.get('input[formControlName=password]').should('have.attr', 'type', 'text');
    cy.get('button[mat-icon-button]').click();
    cy.get('input[formControlName=password]').should('have.attr', 'type', 'password');
  });

  it('Login successfully', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
        token: 'fake-jwt-token'
      },
    }).as('loginRequest');

    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: []
    }).as('sessionRequest');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('button[type=submit]').click();

    cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);
    cy.url().should('include', '/sessions');
  });

  it('Shows error on login failure', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        message: 'Invalid credentials'
      },
    }).as('loginRequest');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("wrongPassword");
    cy.get('button[type=submit]').click();

    cy.wait('@loginRequest').its('response.statusCode').should('eq', 401);
    cy.get('.error').should('contain', 'An error occurred');
  });
});
