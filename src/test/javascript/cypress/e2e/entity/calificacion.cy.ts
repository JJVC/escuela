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

describe('Calificacion e2e test', () => {
  const calificacionPageUrl = '/calificacion';
  const calificacionPageUrlPattern = new RegExp('/calificacion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const calificacionSample = { calificacion: 14459 };

  let calificacion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/calificacions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/calificacions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/calificacions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (calificacion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/calificacions/${calificacion.id}`,
      }).then(() => {
        calificacion = undefined;
      });
    }
  });

  it('Calificacions menu should load Calificacions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('calificacion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Calificacion').should('exist');
    cy.url().should('match', calificacionPageUrlPattern);
  });

  describe('Calificacion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(calificacionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Calificacion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/calificacion/new$'));
        cy.getEntityCreateUpdateHeading('Calificacion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', calificacionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/calificacions',
          body: calificacionSample,
        }).then(({ body }) => {
          calificacion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/calificacions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [calificacion],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(calificacionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Calificacion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('calificacion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', calificacionPageUrlPattern);
      });

      it('edit button click should load edit Calificacion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Calificacion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', calificacionPageUrlPattern);
      });

      it('edit button click should load edit Calificacion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Calificacion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', calificacionPageUrlPattern);
      });

      it('last delete button click should delete instance of Calificacion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('calificacion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', calificacionPageUrlPattern);

        calificacion = undefined;
      });
    });
  });

  describe('new Calificacion page', () => {
    beforeEach(() => {
      cy.visit(`${calificacionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Calificacion');
    });

    it('should create an instance of Calificacion', () => {
      cy.get(`[data-cy="calificacion"]`).type('19734').should('have.value', '19734');

      cy.get(`[data-cy="fechaRegistro"]`).type('2023-01-11T04:43').blur().should('have.value', '2023-01-11T04:43');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        calificacion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', calificacionPageUrlPattern);
    });
  });
});
