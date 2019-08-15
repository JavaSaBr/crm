import {AfterViewInit, Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {MatSidenav} from '@angular/material';
import {SideMenuService} from '../../services/side-menu.service';

@Component({
    selector: 'app-workspace-side-nav',
    templateUrl: './workspace-side-nav.component.html',
    styleUrls: ['./workspace-side-nav.component.scss'],
    host: {'class': 'flex-column'}
})
export class WorkspaceSideNavComponent implements OnInit, AfterViewInit {

    @ViewChild(MatSidenav, {static: true})
    matSidenav: MatSidenav;

    @ViewChild('staticSidePanel', {static: true})
    staticSidePanel: ElementRef<HTMLElement>;

    constructor(private sideMenuService: SideMenuService) {
        this.sideMenuService.requestMenuProperty()
            .subscribe(open => {
                this.toggleMenu(open);
            });
    }

    ngOnInit() {
        this.matSidenav.openedStart.subscribe(() => {
            this.sideMenuService.notifyStartOpening();
        });
        this.matSidenav.closedStart.subscribe(() => {
            this.sideMenuService.notifyStartClosing();
        });
    }

    ngAfterViewInit() {
        this.updateSidePanelVisibility();
    }

    private updateSidePanelVisibility() {

        this.staticSidePanel
            .nativeElement
            .style
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
