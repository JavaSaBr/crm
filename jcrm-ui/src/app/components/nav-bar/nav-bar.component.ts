import {Component, OnInit} from '@angular/core';
import {SideMenuService} from '../../services/side-menu.service';
import {SecurityService} from '../../services/security.service';

@Component({
    selector: 'app-nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

    searchValue: string;
    additionalHamburgerStyle: string;

    constructor(
        private readonly securityService: SecurityService,
        private readonly sideMenuService: SideMenuService
    ) {
        this.additionalHamburgerStyle = '';
    }

    ngOnInit() {

        this.sideMenuService.startOpeningProperty()
            .subscribe(() => {
                this.additionalHamburgerStyle = 'is-active';
            });
        this.sideMenuService.startClosingProperty()
            .subscribe(() => {
                this.additionalHamburgerStyle = '';
            });
    }

    toggleSideMenu() {
        this.sideMenuService.toggleMenu();
    }
}
