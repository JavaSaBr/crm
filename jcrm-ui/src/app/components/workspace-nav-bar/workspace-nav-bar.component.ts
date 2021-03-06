import {Component, Input, OnInit} from '@angular/core';
import {WorkspaceSideMenuService} from '@app/service/workspace-side-menu.service';
import {SecurityService} from '@app/service/security.service';
import {Router} from '@angular/router';
import {WorkspaceService} from '@app/service/workspace.service';
import {NoAuthHomeComponent} from '@app/component/no-auth-home/no-auth-home.component';
import {Location} from '@angular/common';

@Component({
    selector: 'app-workspace-nav-bar',
    templateUrl: './workspace-nav-bar.component.html',
    styleUrls: ['./workspace-nav-bar.component.scss']
})
export class WorkspaceNavBarComponent implements OnInit {

    @Input('title')
    title: string;

    searchValue: string;
    additionalHamburgerStyle: string;

    showMenuButton: boolean;
    showBackButton: boolean;
    showSearchField: boolean;

    constructor(
        private readonly securityService: SecurityService,
        private readonly sideMenuService: WorkspaceSideMenuService,
        private readonly router: Router,
        private readonly workspaceService: WorkspaceService,
        private readonly location: Location
    ) {
        this.searchValue = '';
        this.additionalHamburgerStyle = '';
        this.workspaceService.component.subscribe(component => {
            this.showMenuButton = component ? component.isNeedGlobalMenu() : true;
            this.showBackButton = component ? component.isFullScreen() : false;
            this.showSearchField = component ? component.isNeedGlobalSearch() : true;
        });
    }

    ngOnInit(): void {

        this.sideMenuService.startOpeningProperty()
            .subscribe(() => {
                this.additionalHamburgerStyle = 'is-active';
            });
        this.sideMenuService.startClosingProperty()
            .subscribe(() => {
                this.additionalHamburgerStyle = '';
            });
    }

    toggleSideMenu(): void {
        this.sideMenuService.toggleMenu();
    }

    logout(): void {
        this.securityService.logout();
        this.router.navigate(['/' + NoAuthHomeComponent.COMPONENT_PATH]);
    }

    back(): void {
        this.location.back();
    }
}
