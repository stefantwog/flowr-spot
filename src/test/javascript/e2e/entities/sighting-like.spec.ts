import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('SightingLike e2e test', () => {

    let navBarPage: NavBarPage;
    let sightingLikeDialogPage: SightingLikeDialogPage;
    let sightingLikeComponentsPage: SightingLikeComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load SightingLikes', () => {
        navBarPage.goToEntity('sighting-like');
        sightingLikeComponentsPage = new SightingLikeComponentsPage();
        expect(sightingLikeComponentsPage.getTitle())
            .toMatch(/flowrSpotApp.sightingLike.home.title/);

    });

    it('should load create SightingLike dialog', () => {
        sightingLikeComponentsPage.clickOnCreateButton();
        sightingLikeDialogPage = new SightingLikeDialogPage();
        expect(sightingLikeDialogPage.getModalTitle())
            .toMatch(/flowrSpotApp.sightingLike.home.createOrEditLabel/);
        sightingLikeDialogPage.close();
    });

    it('should create and save SightingLikes', () => {
        sightingLikeComponentsPage.clickOnCreateButton();
        sightingLikeDialogPage.sightingSelectLastOption();
        sightingLikeDialogPage.save();
        expect(sightingLikeDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SightingLikeComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-sighting-like div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SightingLikeDialogPage {
    modalTitle = element(by.css('h4#mySightingLikeLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    sightingSelect = element(by.css('select#field_sighting'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    sightingSelectLastOption = function() {
        this.sightingSelect.all(by.tagName('option')).last().click();
    };

    sightingSelectOption = function(option) {
        this.sightingSelect.sendKeys(option);
    };

    getSightingSelect = function() {
        return this.sightingSelect;
    };

    getSightingSelectedOption = function() {
        return this.sightingSelect.element(by.css('option:checked')).getText();
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
