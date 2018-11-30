import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {SideMenuService} from '../../services/side-menu.service';

@Component({
    selector: 'app-nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

    additionalHamburgerStyle: string;

    constructor(private userService: UserService, private sideMenuService: SideMenuService) {
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

    isAuthenticated() {
        return this.userService.isAuthenticated();
    }
}
