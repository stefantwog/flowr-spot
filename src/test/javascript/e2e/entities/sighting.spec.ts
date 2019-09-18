import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Sighting e2e test', () => {

    let navBarPage: NavBarPage;
    let sightingDialogPage: SightingDialogPage;
    let sightingComponentsPage: SightingComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Sightings', () => {
        navBarPage.goToEntity('sighting');
        sightingComponentsPage = new SightingComponentsPage();
        expect(sightingComponentsPage.getTitle())
            .toMatch(/flowrSpotApp.sighting.home.title/);

    });

    it('should load create Sighting dialog', () => {
        sightingComponentsPage.clickOnCreateButton();
        sightingDialogPage = new SightingDialogPage();
        expect(sightingDialogPage.getModalTitle())
            .toMatch(/flowrSpotApp.sighting.home.createOrEditLabel/);
        sightingDialogPage.close();
    });

    it('should create and save Sightings', () => {
        sightingComponentsPage.clickOnCreateButton();
        sightingDialogPage.setLongitudeInput('5');
        expect(sightingDialogPage.getLongitudeInput()).toMatch('5');
        sightingDialogPage.setLatitudeInput('5');
        expect(sightingDialogPage.getLatitudeInput()).toMatch('5');
        sightingDialogPage.setImageInput('image');
        expect(sightingDialogPage.getImageInput()).toMatch('image');
        sightingDialogPage.flowerSelectLastOption();
        sightingDialogPage.save();
        expect(sightingDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class SightingComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-sighting div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SightingDialogPage {
    modalTitle = element(by.css('h4#mySightingLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    longitudeInput = element(by.css('input#field_longitude'));
    latitudeInput = element(by.css('input#field_latitude'));
    imageInput = element(by.css('input#field_image'));
    flowerSelect = element(by.css('select#field_flower'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setLongitudeInput = function(longitude) {
        this.longitudeInput.sendKeys(longitude);
    };

    getLongitudeInput = function() {
        return this.longitudeInput.getAttribute('value');
    };

    setLatitudeInput = function(latitude) {
        this.latitudeInput.sendKeys(latitude);
    };

    getLatitudeInput = function() {
        return this.latitudeInput.getAttribute('value');
    };

    setImageInput = function(image) {
        this.imageInput.sendKeys(image);
    };

    getImageInput = function() {
        return this.imageInput.getAttribute('value');
    };

    flowerSelectLastOption = function() {
        this.flowerSelect.all(by.tagName('option')).last().click();
    };

    flowerSelectOption = function(option) {
        this.flowerSelect.sendKeys(option);
    };

    getFlowerSelect = function() {
        return this.flowerSelect;
    };

    getFlowerSelectedOption = function() {
        return this.flowerSelect.element(by.css('option:checked')).getText();
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
