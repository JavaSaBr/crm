import {Component, OnInit} from '@angular/core';
import {SideMenuService} from '../../services/side-menu.service';
import {SecurityService} from '../../services/security.service';
import {Router} from '@angular/router';
import {WorkspaceMode, WorkspaceService} from '../../services/workspace.service';
import {NoAuthHomeComponent} from '../no-auth-home/no-auth-home.component';
import {Observable, Subject} from 'rxjs';

@Component({
    selector: 'app-workspace-nav-bar',
    templateUrl: './workspace-nav-bar.component.html',
    styleUrls: ['./workspace-nav-bar.component.scss']
})
export class WorkspaceNavBarComponent implements OnInit {

    searchValue: string;
    additionalHamburgerStyle: string;

    showMenuButton: Subject<boolean>;
    showSearchField: Subject<boolean>;

    constructor(
        private readonly securityService: SecurityService,
        private readonly sideMenuService: SideMenuService,
        private readonly router: Router,
        private readonly workspaceService: WorkspaceService
    ) {
        this.additionalHamburgerStyle = '';
        this.showMenuButton = new Subject();
        this.showMenuButton.next(this.canShowMenuButton(workspaceService.workspaceMode.value));
        this.showSearchField = new Subject();
        this.showSearchField.next(this.canShowSearchField(workspaceService.workspaceMode.value))
        this.workspaceService.workspaceMode.subscribe(value => {
            this.showMenuButton.next(this.canShowMenuButton(value));
            this.showSearchField.next(this.canShowSearchField(value));
        })
    }

    private canShowSearchField(value) {
        return value == WorkspaceMode.DEFAULT;
    }

    private canShowMenuButton(value) {
        return value == WorkspaceMode.DEFAULT;
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
        this.router.navigate(['/' + NoAuthHomeComponent.COMPONENT_PATH]);
    }
}
