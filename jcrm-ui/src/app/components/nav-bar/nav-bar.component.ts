import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {SideMenuService} from '../../services/side-menu.service';

@Component({
    selector: 'app-nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent {

    additionalHamburgerStyle: string;

    constructor(private userService: UserService, private sideMenuService: SideMenuService) {
        this.sideMenuService.changingProperty()
            .subscribe(() => {
                console.log(this.sideMenuService.isOpen());
                this.additionalHamburgerStyle = this.sideMenuService.isOpen() ? '' : 'is-active';
            });
        this.additionalHamburgerStyle = '';
    }

    toggleSideMenu() {
        if (!this.sideMenuService.isChanging()) {
            this.sideMenuService.toggleMenu();
        }
    }

    isAuthenticated() {
        return this.userService.isAuthenticated();
    }
}
