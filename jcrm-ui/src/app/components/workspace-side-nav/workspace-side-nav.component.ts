import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatSidenav} from '@angular/material';
import {WorkspaceSideMenuService} from '@app/service/workspace-side-menu.service';
import {WorkspaceService} from '@app/service/workspace.service';
import {NavigationEnd, Router} from '@angular/router';
import {filter} from 'rxjs/operators';
import {BaseWorkspaceComponent} from '@app/component/workspace/workspace.component';

@Component({
    selector: 'app-workspace-side-nav',
    templateUrl: './workspace-side-nav.component.html',
    styleUrls: ['./workspace-side-nav.component.scss'],
    host: {'class': 'flex-column'}
})
export class WorkspaceSideNavComponent implements OnInit {

    @ViewChild(MatSidenav, {static: true})
    matSidenav: MatSidenav;

    @ViewChild('mainView', {static: true})
    mainView: ElementRef<HTMLElement>;

    showSideMenu: boolean;

    constructor(
        private readonly sideMenuService: WorkspaceSideMenuService,
        private readonly workspaceService: WorkspaceService,
        private readonly router: Router
    ) {
        sideMenuService.requestMenuProperty()
            .subscribe(open => this.toggleMenu(open));
        workspaceService.component
            .subscribe(component => this.updateMainViewStyle(component));
        router.events.pipe(filter(value => value instanceof NavigationEnd))
            .subscribe(() => this.closeMenu());
    }

    private updateMainViewStyle(component: BaseWorkspaceComponent) {
        this.showSideMenu = component ? component.isNeedGlobalMenu() : true;

        const nativeElement = this.mainView &&
            this.mainView.nativeElement;

        if (!nativeElement) {
            return;
        }

        nativeElement.classList.remove('main-view-padding');

        if (component ? component.isNeedContentPadding() : false) {
            nativeElement.classList.add('main-view-padding');
        }
    }

    ngOnInit() {
        this.matSidenav.openedStart
            .subscribe(() => this.sideMenuService.notifyStartOpening());
        this.matSidenav.closedStart
            .subscribe(() => this.sideMenuService.notifyStartClosing());
    }

    private toggleMenu(open: boolean) {

        if (this.matSidenav.opened === open) {
            return;
        }

        this.sideMenuService.notifyStartChanging();
        this.matSidenav.toggle()
            .then(result => {

                if (result === 'open') {
                    this.sideMenuService.notifyOpened();
                } else {
                    this.sideMenuService.notifyClosed();
                }

                this.sideMenuService.notifyFinishChanging();
            });
    }

    closeMenu(): void {
        if (!this.matSidenav.opened) {
            return;
        } else {
            this.matSidenav.toggle();
        }
    }
}
