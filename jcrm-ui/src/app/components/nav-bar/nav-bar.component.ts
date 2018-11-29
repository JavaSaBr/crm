import {Component, OnInit} from '@angular/core';
import {MatDrawerToggleResult} from '@angular/material';
import {UserService} from '../../services/user.service';
import {SideMenuService} from '../../services/side-menu.service';

@Component({
    selector: 'app-nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

    additionalHamburgerStyle: string;

    constructor(public userService: UserService, private sideMenuService: SideMenuService) {
    }

    ngOnInit() {
    }

    toggleSideNav() {

        if (this.sideMenuService.isOpeningNow) {
            return;
        }

        if (this.sideNavComponentOpen) {
            this.additionalHamburgerStyle = '';
        } else {
            this.additionalHamburgerStyle = 'is-active';
        }

        this.sideNavComponent.toggle()
            .then(value => this.updateHamburgerState(value));
    }

    private updateHamburgerState(value: MatDrawerToggleResult) {
        this.sideNavComponentOpen = value === 'open';
    }
}
