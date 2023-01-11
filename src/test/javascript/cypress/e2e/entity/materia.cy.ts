import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Materia e2e test', () => {
  const materiaPageUrl = '/materia';
  const materiaPageUrlPattern = new RegExp('/materia(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const materiaSample = { nombre: 'Account Gorro deposit' };

  let materia;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/materias+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/materias').as('postEntityRequest');
    cy.intercept('DELETE', '/api/materias/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (materia) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/materias/${materia.id}`,
      }).then(() => {
        materia = undefined;
      });
    }
  });

  it('Materias menu should load Materias page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('materia');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Materia').should('exist');
    cy.url().should('match', materiaPageUrlPattern);
  });

  describe('Materia page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(materiaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Materia page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/materia/new$'));
        cy.getEntityCreateUpdateHeading('Materia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', materiaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/materias',
          body: materiaSample,
        }).then(({ body }) => {
          materia = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/materias+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [materia],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(materiaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Materia page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('materia');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', materiaPageUrlPattern);
      });

      it('edit button click should load edit Materia page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Materia');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', materiaPageUrlPattern);
      });

      it('edit button click should load edit Materia page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Materia');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', materiaPageUrlPattern);
      });

      it('last delete button click should delete instance of Materia', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('materia').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', materiaPageUrlPattern);

        materia = undefined;
      });
    });
  });

  describe('new Materia page', () => {
    beforeEach(() => {
      cy.visit(`${materiaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Materia');
    });

    it('should create an instance of Materia', () => {
      cy.get(`[data-cy="nombre"]`).type('microchip').should('have.value', 'microchip');

      cy.get(`[data-cy="activo"]`).should('not.be.checked');
      cy.get(`[data-cy="activo"]`).click().should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        materia = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', materiaPageUrlPattern);
    });
  });
});
