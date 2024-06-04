/// <reference types="cypress" />

describe('Admin Journey', () => {
    beforeEach(() => {
        const sessionInformation = {
            token: 'fake-jwt-token',
            type: 'Bearer',
            id: 1,
            username: 'yoga@studio.com',
            firstName: 'Yoga',
            lastName: 'Studio',
            admin: true 
        };
        
        cy.intercept('POST', '/api/auth/login', {
            statusCode: 200,
            body: sessionInformation
        }).as('loginRequest');

        // Set session state in localStorage
        localStorage.setItem('sessionInformation', JSON.stringify(sessionInformation));
        localStorage.setItem('isLogged', 'true');
    });

    it('should create, update and delete a session', () => {
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

        // Step 1: Log in 
        cy.visit('/login');

        cy.get('input[formControlName=email]').type('yoga@studio.com');
        cy.get('input[formControlName=password]').type('Password123');

        cy.intercept('POST', '/api/auth/login', {
            statusCode: 200,
            body: {
                id: 1,
                username: 'yoga@studio.com',
                firstName: 'Yoga',
                lastName: 'Studio',
                admin: true,
                token: 'fake-jwt-token'
            },
        }).as('loginRequest');

        cy.get('button[type=submit]').click();

        cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

        // Step 2: Create a new session
        cy.intercept('POST', '/api/session', {
            statusCode: 200,
            body: {
                id: 3,
                name: 'New Yoga Session',
                date: new Date().toISOString(),
                description: 'A brand new yoga session.',
                teacher_id: 1,
                users: []
            }
        }).as('createSession');

        cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: [
                {
                    id: 1,
                    firstName: 'John',
                    lastName: 'Doe',
                    createdAt: new Date(),
                    updatedAt: new Date()
                },
                {
                    id: 2,
                    firstName: 'Jane',
                    lastName: 'Smith',
                    createdAt: new Date(),
                    updatedAt: new Date()
                }
            ]
        }).as('getTeachers');

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
                },
                {
                    id: 3,
                    name: 'New Yoga Session',
                    date: new Date().toISOString(),
                    description: 'A brand new yoga session.',
                    teacher_id: 1,
                    users: []
                }
            ]
        }).as('getUpdatedSessions');

        cy.get('button[routerLink="create"]').click();

        cy.wait('@getTeachers');

        cy.get('input[formControlName=name]').type('New Yoga Session');
        cy.get('input[formControlName=date]').type(new Date().toISOString().split('T')[0]);  
        cy.get('mat-select[formControlName=teacher_id]').click().get('mat-option').contains('John Doe').click(); 
        cy.get('textarea[formControlName=description]').type('A brand new yoga session.');

        cy.get('button[type=submit]').click();

        cy.wait('@createSession').its('response.statusCode').should('eq', 200);

        cy.wait('@getUpdatedSessions');

        cy.get('.session-name').contains('New Yoga Session').should('exist');

        // Step 3: Update the new session
        cy.intercept('GET', '/api/session/3', {
            statusCode: 200,
            body: {
                id: 3,
                name: 'New Yoga Session',
                date: new Date().toISOString(),
                description: 'A brand new yoga session.',
                teacher_id: 1,
                users: []
            }
        }).as('getSessionDetails');

        cy.intercept('PUT', '/api/session/3', {
            statusCode: 200,
            body: {
                id: 3,
                name: 'Updated Yoga Session',
                date: new Date().toISOString(),
                description: 'An updated yoga session.',
                teacher_id: 1,
                users: []
            }
        }).as('updateSession');

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
                },
                {
                    id: 3,
                    name: 'Updated Yoga Session',
                    date: new Date().toISOString(),
                    description: 'An updated yoga session.',
                    teacher_id: 1,
                    users: []
                }
            ]
        }).as('getUpdatedSessions');

        cy.get('.edit-button').eq(2).click();

        cy.wait('@getSessionDetails');

        cy.get('input[formControlName=name]').clear().type('Updated Yoga Session');
        cy.get('textarea[formControlName=description]').clear().type('An updated yoga session.');

        cy.get('button[type=submit]').click();

        cy.wait('@updateSession').its('response.statusCode').should('eq', 200);

        cy.get('.session-name').contains('Updated Yoga Session').should('exist');
        cy.get('.session-description').contains('An updated yoga session.').should('exist');

        // Step 4: Delete the new session
        cy.intercept('GET', '/api/session/3', {
            statusCode: 200,
            body: {
                id: 3,
                name: 'Updated Yoga Session',
                date: new Date().toISOString(),
                description: 'An updated yoga session.',
                teacher_id: 1,
                users: []
            }
        }).as('getSessionDetails');

        cy.intercept('DELETE', '/api/session/3', {
            statusCode: 200,
            body: {}
        }).as('deleteSession');

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
        }).as('getUpdatedSessions');

        cy.get('.detail-button').eq(2).click();

        cy.get('.delete-button').click();

    });
});
