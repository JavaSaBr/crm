import {Component, OnInit} from '@angular/core';
import {NoAuthHomeService} from '../../services/no-auth-home.service';

@Component({
    selector: 'app-no-auth-home',
    templateUrl: './no-auth-home.component.html',
    styleUrls: ['./no-auth-home.component.scss']
})
export class NoAuthHomeComponent implements OnInit {

    constructor(private noAuthHomeService: NoAuthHomeService) {
    }

    ngOnInit() {
    }

    hasSubPage() {
        return this.noAuthHomeService.hasSubPage();
    }

    public activateSubPage(page: string) {
        this.noAuthHomeService.activateSubPage(page);
    }

    getCurrentSubPage() {
        return this.noAuthHomeService.getCurrentSubPage();
    }
}
