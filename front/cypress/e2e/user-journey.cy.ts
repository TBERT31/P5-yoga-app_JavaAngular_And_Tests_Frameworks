/// <reference types="cypress" />

describe('User Journey', () => {
    beforeEach(() => {
        const sessionInformation = {
            token: 'fake-jwt-token',
            type: 'Bearer',
            id: 1,
            username: 'john.doe',
            firstName: 'John',
            lastName: 'Doe',
            admin: false 
        };
        cy.intercept('POST', '/api/auth/login', {
            statusCode: 200,
            body: sessionInformation
        }).as('loginRequest');

        cy.intercept('GET', '/api/session', {
            statusCode: 200,
            body: [
                {
                    id: 1,
                    name: 'Yoga Session',
                    date: new Date().toISOString(),
                    description: 'A relaxing yoga session.',
                    teacher_id: 1,
                    users: []
                },
                {
                    id: 2,
                    name: 'Pilates Session',
                    date: new Date().toISOString(),
                    description: 'A rejuvenating pilates session.',
                    teacher_id: 1,
                    users: []
                }
            ]
        }).as('getSessions');

        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: 'Yoga Session',
                date: new Date().toISOString(),
                description: 'A relaxing yoga session.',
                teacher_id: 1,
                users: []
            }
        }).as('getSessionDetail');

        cy.intercept('GET', '/api/teacher/1', {
            statusCode: 200,
            body: {
                id: 1,
                firstName: 'Jane',
                lastName: 'Doe',
                updatedAt: new Date().toISOString(),
                createdAt: new Date().toISOString()
            }
        }).as('getTeacherDetail');

        // Set session state in localStorage
        localStorage.setItem('sessionInformation', JSON.stringify(sessionInformation));
        localStorage.setItem('isLogged', 'true');
    });

    it('should create a user, participate in a session, and delete the account', () => {
        // Step 1: Register a new user
        cy.visit('/register');

        cy.get('input[formControlName=firstName]').type('John');
        cy.get('input[formControlName=lastName]').type('Doe');
        cy.get('input[formControlName=email]').type('john.doe@example.com');
        cy.get('input[formControlName=password]').type('Password123');

        cy.intercept('POST', '/api/auth/register', {
        statusCode: 200,
        }).as('registerRequest');

        cy.get('button[type=submit]').click();

        cy.wait('@registerRequest').its('response.statusCode').should('eq', 200);

        // Step 2: Log in with the new user
        cy.visit('/login');

        cy.get('input[formControlName=email]').type('john.doe@example.com');
        cy.get('input[formControlName=password]').type('Password123');

        cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: {
            id: 1,
            username: 'john.doe',
            firstName: 'John',
            lastName: 'Doe',
            admin: false,
            token: 'fake-jwt-token'
        },
        }).as('loginRequest');

        cy.get('button[type=submit]').click();

        cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

        
        // Step 3: Mock sessions and navigate to the session detail page
        cy.get('.items .item').first().within(() => {
            cy.get('button').contains('Detail').click();
        });

        cy.get('.session-name').should('contain', 'Yoga Session');
        cy.get('.description').should('contain', 'A relaxing yoga session.');
        cy.get('.session-date').should('exist');
        cy.get('.teacher-name').should('contain', 'Jane DOE');

        // Step 4: Click Participate button
        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('participateRequest');

        cy.get('.participate-button').click();
        cy.wait('@participateRequest').its('response.statusCode').should('eq', 200);
  
        // Step 5: Click Unparticipate button
        cy.intercept('DELETE', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('unParticipateRequest');

        cy.get('.unparticipate-button').click();
        cy.wait('@unParticipateRequest').its('response.statusCode').should('eq', 200);
    
        // Step 6: Click return button
        
    
        // Step 7: Navigate to Account page
      
  
      // Step 8: Delete the account
      
    });
  });
  