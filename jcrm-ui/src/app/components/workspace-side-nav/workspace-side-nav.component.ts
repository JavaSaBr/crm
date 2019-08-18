import {AfterViewInit, Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {MatSidenav} from '@angular/material';
import {SideMenuService} from '../../services/side-menu.service';
import {WorkspaceMode, WorkspaceService} from '../../services/workspace.service';

@Component({
    selector: 'app-workspace-side-nav',
    templateUrl: './workspace-side-nav.component.html',
    styleUrls: ['./workspace-side-nav.component.scss'],
    host: {'class': 'flex-column'}
})
export class WorkspaceSideNavComponent implements OnInit, AfterViewInit {

    @ViewChild(MatSidenav, {static: true})
    matSidenav: MatSidenav;

    @ViewChild('staticSidePanel', {static: false})
    staticSidePanel: ElementRef<HTMLElement>;

    @ViewChild('mainView', {static: true})
    mainView: ElementRef<HTMLElement>;

    showSideMenu: boolean;

    constructor(
        private readonly sideMenuService: SideMenuService,
        private readonly workspaceService: WorkspaceService
    ) {
        sideMenuService.requestMenuProperty()
            .subscribe(open => {
                this.toggleMenu(open);
            });
        workspaceService.workspaceMode
            .subscribe(value => {
                this.showSideMenu = this.canShowSideMenu(value);
                this.updateMainViewStyle(value);
            });

        this.showSideMenu = this.canShowSideMenu(workspaceService.workspaceMode.value);
        this.updateMainViewStyle(workspaceService.workspaceMode.value);
    }

    private canShowSideMenu(value: WorkspaceMode): boolean {
        return value == WorkspaceMode.DEFAULT;
    }

    private updateMainViewStyle(value: WorkspaceMode) {

        const nativeElement = this.mainView &&
            this.mainView.nativeElement;

        if (!nativeElement) {
            return;
        }

        nativeElement.classList.remove('main-view-padding');

        switch (value) {
            case WorkspaceMode.OBJECT_CREATION: {
                nativeElement.classList.add('main-view-padding');
                return;
            }
        }
    }

    ngOnInit() {
        this.matSidenav.openedStart
            .subscribe(() => {
                this.sideMenuService.notifyStartOpening();
            });
        this.matSidenav.closedStart
            .subscribe(() => {
                this.sideMenuService.notifyStartClosing();
            });
    }

    ngAfterViewInit() {
        this.updateSidePanelVisibility();
    }

    private updateSidePanelVisibility() {

        const nativeElement = this.staticSidePanel &&
            this.staticSidePanel.nativeElement;

        if (!nativeElement) {
            return;
        }

        nativeElement.style
            .display = window.innerWidth < 700 ? 'none' : 'flex';
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

    @HostListener('window:resize')
    onResize() {
        this.updateSidePanelVisibility();
    }
}
