import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Flower e2e test', () => {

    let navBarPage: NavBarPage;
    let flowerDialogPage: FlowerDialogPage;
    let flowerComponentsPage: FlowerComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Flowers', () => {
        navBarPage.goToEntity('flower');
        flowerComponentsPage = new FlowerComponentsPage();
        expect(flowerComponentsPage.getTitle())
            .toMatch(/flowrSpotApp.flower.home.title/);

    });

    it('should load create Flower dialog', () => {
        flowerComponentsPage.clickOnCreateButton();
        flowerDialogPage = new FlowerDialogPage();
        expect(flowerDialogPage.getModalTitle())
            .toMatch(/flowrSpotApp.flower.home.createOrEditLabel/);
        flowerDialogPage.close();
    });

    it('should create and save Flowers', () => {
        flowerComponentsPage.clickOnCreateButton();
        flowerDialogPage.setNameInput('name');
        expect(flowerDialogPage.getNameInput()).toMatch('name');
        flowerDialogPage.setImageInput('image');
        expect(flowerDialogPage.getImageInput()).toMatch('image');
        flowerDialogPage.setDescriptionInput('description');
        expect(flowerDialogPage.getDescriptionInput()).toMatch('description');
        flowerDialogPage.save();
        expect(flowerDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class FlowerComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-flower div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class FlowerDialogPage {
    modalTitle = element(by.css('h4#myFlowerLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    nameInput = element(by.css('input#field_name'));
    imageInput = element(by.css('input#field_image'));
    descriptionInput = element(by.css('textarea#field_description'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setNameInput = function(name) {
        this.nameInput.sendKeys(name);
    };

    getNameInput = function() {
        return this.nameInput.getAttribute('value');
    };

    setImageInput = function(image) {
        this.imageInput.sendKeys(image);
    };

    getImageInput = function() {
        return this.imageInput.getAttribute('value');
    };

    setDescriptionInput = function(description) {
        this.descriptionInput.sendKeys(description);
    };

    getDescriptionInput = function() {
        return this.descriptionInput.getAttribute('value');
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
