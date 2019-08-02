import {Component, OnInit} from '@angular/core';
import {SideMenuService} from '../../services/side-menu.service';
import {SecurityService} from '../../services/security.service';
import {Router} from '@angular/router';

@Component({
    selector: 'app-workspace-nav-bar',
    templateUrl: './workspace-nav-bar.component.html',
    styleUrls: ['./workspace-nav-bar.component.scss']
})
export class WorkspaceNavBarComponent implements OnInit {

    searchValue: string;
    additionalHamburgerStyle: string;

    constructor(
        private readonly securityService: SecurityService,
        private readonly sideMenuService: SideMenuService,
        private readonly router: Router
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

    logout() {
        this.securityService.logout();
        this.router.navigate(['/no-auth']);
    }
}
