import {Component, Input} from '@angular/core';
import {fabButtonAnimations} from './fab-button.animations';

export interface FabButtonElement {
    routerLink: string,
    icon: string;
    tooltip: string,
    callback: Function | null
}

@Component({
    selector: 'app-fab-button',
    templateUrl: './fab-button.component.html',
    styleUrls: ['./fab-button.component.css'],
    animations: fabButtonAnimations
})
export class FabButtonComponent {

    @Input('availableButtons')
    availableButtons: FabButtonElement[];

    buttons: FabButtonElement[];
    toggleState: string = 'inactive';

    constructor() {
        this.buttons = [];
        this.availableButtons = [];
    }

    showItems() {
        this.toggleState = 'active';
        this.buttons = this.availableButtons;
    }

    hideItems() {
        this.toggleState = 'inactive';
        this.buttons = [];
    }

    onToggleFab() {
        this.buttons.length ? this.hideItems() : this.showItems();
    }

    onClick(button: FabButtonElement) {
        if (button.callback != null) {
            button.callback();
            this.onToggleFab();
        }
    }
}
