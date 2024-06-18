/// <reference types="cypress" />

describe('User Journey', () => {
    beforeEach(() => {
        const sessionInformation = {
            token: 'fake-jwt-token',
            type: 'Bearer',
            id: 1,
            username: 'john.doe@example.com',
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

        // Set session state in localStorage
        localStorage.setItem('sessionInformation', JSON.stringify(sessionInformation));
        localStorage.setItem('isLogged', 'true');
    });

    it('should create a user, participate-unparticipate to a session, and delete the account', () => {
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
                username: 'john.doe@example.com',
                firstName: 'John',
                lastName: 'Doe',
                admin: false,
                token: 'fake-jwt-token'
            },
        }).as('loginRequest');

        cy.get('button[type=submit]').click();

        cy.wait('@loginRequest').its('response.statusCode').should('eq', 200);

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

        // Step 3: Mock sessions and navigate to the session detail page
        cy.get('.items .item').first().within(() => {
            cy.get('button').contains('Detail').click();
        });

        cy.wait('@getSessionDetail');
        cy.wait('@getTeacherDetail');

        cy.get('.session-name').should('contain', 'Yoga Session');
        cy.get('.description').should('contain', 'A relaxing yoga session.');
        cy.get('.session-date').should('exist');
        cy.get('.teacher-name').should('contain', 'Jane DOE');

        // Step 4: Click Participate button
        cy.intercept('GET', '/api/session/1', {
            statusCode: 200,
            body: {
                id: 1,
                name: 'Yoga Session',
                date: new Date().toISOString(),
                description: 'A relaxing yoga session.',
                teacher_id: 1,
                users: [1]
            }
        }).as('getSessionDetailAfterParticipate');

        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('participateRequest');

        cy.get('.participate-button').click();
        cy.wait('@participateRequest').its('response.statusCode').should('eq', 200);
        cy.wait('@getSessionDetailAfterParticipate');

        // Step 5: Click Unparticipate button
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
        }).as('getSessionDetailAfterUnparticipate');

        cy.intercept('DELETE', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('unParticipateRequest');

        cy.get('.unparticipate-button').click();
        cy.wait('@unParticipateRequest').its('response.statusCode').should('eq', 200);
        cy.wait('@getSessionDetailAfterUnparticipate');

        // Step 6: Click return button
        cy.get('.back-button').click();
        cy.url().should('include', '/sessions');

        // Step 7: Navigate to Account page
        cy.intercept('GET', '/api/user/1', {
            statusCode: 200,
            body: {
                id: 1,
                firstName: 'John',
                lastName: 'Doe',
                email: 'john.doe@example.com',
                admin: false,
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString()
            }
        }).as('getUserDetail');

        cy.get('.link').contains('Account').click();
        cy.url().should('include', '/me');
        cy.wait('@getUserDetail');

        cy.get('[data-testid="user-name"]').should('contain', 'John DOE');
        cy.get('[data-testid="user-email"]').should('contain', 'john.doe@example.com');

        // Step 8: Delete the account
        cy.intercept('DELETE', '/api/user/1', {
            statusCode: 200
        }).as('deleteUserRequest');

        cy.get('[data-testid="delete-account"]').click();
        cy.wait('@deleteUserRequest').its('response.statusCode').should('eq', 200);

        cy.url().should('include', '/');
    });
});


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

        cy.get('.session-name').each(($el) => {
            const text = $el.text();
            expect(['Yoga Session', 'Pilates Session']).to.include(text);
        });

        cy.get('.session-description').each(($el) => {
            const text = $el.text().replace(/\s+/g, ' ').trim(); 
            const regex = /A relaxing yoga session\.|A rejuvenating pilates session\./;
            expect(text).to.match(regex);
        });

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
