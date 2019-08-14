import {Component} from '@angular/core';
import {fabButtonAnimations} from './fab-button.animations';

@Component({
    selector: 'app-fab-button',
    templateUrl: './fab-button.component.html',
    styleUrls: ['./fab-button.component.css'],
    animations: fabButtonAnimations
})
export class FabButtonComponent {

    fabButtons = [
        {
            icon: 'timeline'
        },
        {
            icon: 'view_headline'
        },
        {
            icon: 'room'
        },
        {
            icon: 'lightbulb_outline'
        },
        {
            icon: 'lock'
        }
    ];
    buttons = [];
    fabTogglerState = 'inactive';

    constructor() {
    }

    showItems() {
        this.fabTogglerState = 'active';
        this.buttons = this.fabButtons;
    }

    hideItems() {
        this.fabTogglerState = 'inactive';
        this.buttons = [];
    }

    onToggleFab() {
        this.buttons.length ? this.hideItems() : this.showItems();
    }
}
