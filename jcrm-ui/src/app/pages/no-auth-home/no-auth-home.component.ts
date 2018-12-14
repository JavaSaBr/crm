import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'app-no-auth-home',
    templateUrl: './no-auth-home.component.html',
    styleUrls: ['./no-auth-home.component.scss']
})
export class NoAuthHomeComponent implements OnInit {

    private currentSubPage: string = null;

    constructor() {
    }

    ngOnInit() {
    }

    hasSubPage() {
        return this.currentSubPage != null;
    }

    activateSubPage(page: string) {
        this.currentSubPage = page;
    }

    getCurrentSubPage() {
        return this.currentSubPage;
    }
}
