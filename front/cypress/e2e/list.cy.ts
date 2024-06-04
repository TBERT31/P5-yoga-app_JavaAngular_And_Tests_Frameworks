/// <reference types="cypress" />

describe('ListComponent', () => {
    beforeEach(() => {
        // Simuler l'état de session pour un utilisateur connecté
        const sessionInformation = {
            token: 'fake-jwt-token',
            type: 'Bearer',
            id: 1,
            username: 'john.doe',
            firstName: 'John',
            lastName: 'Doe',
            admin: true // ou false selon ce que vous voulez tester
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

    it('should display the list of sessions and handle detail and edit buttons', () => {
        // Visiter la page des sessions
        cy.visit('/sessions');

        // Attendre la requête et vérifier le nombre d'éléments
        cy.wait('@getSessions');

        // Vérifier que les sessions sont affichées correctement
        cy.get('.items .item').should('have.length', 2);

        // Vérifier le contenu de la première session
        cy.get('.items .item').first().within(() => {
            cy.get('.session-name').should('contain', 'Yoga Session');
            cy.get('.session-date').should('exist');
            cy.get('.session-description').should('contain', 'A relaxing yoga session.');

            // Tester le bouton de détail
            cy.get('button').contains('Detail').click();
            cy.url().should('include', '/sessions/detail/1');

            // Retourner à la page des sessions
            cy.go('back');

            // Tester le bouton d'édition si l'utilisateur est admin
            cy.get('button').contains('Edit').click();
            cy.url().should('include', '/sessions/update/1');
        });

        // Vérifier le contenu de la deuxième session
        cy.get('.items .item').last().within(() => {
            cy.get('.session-name').should('contain', 'Pilates Session');
            cy.get('.session-date').should('exist');
            cy.get('.session-description').should('contain', 'A rejuvenating pilates session.');

            // Tester le bouton de détail
            cy.get('button').contains('Detail').click();
            cy.url().should('include', '/sessions/detail/2');

            // Retourner à la page des sessions
            cy.go('back');

            // Tester le bouton d'édition si l'utilisateur est admin
            cy.get('button').contains('Edit').click();
            cy.url().should('include', '/sessions/update/2');
        });
    });
});
