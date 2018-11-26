import {Component, Injectable, OnInit, ViewChild} from '@angular/core';
import {SideNavComponent} from '../side-nav/side-nav.component';
import {MatDrawerToggleResult} from '@angular/material';
import {UserService} from '../../services/user.service';

@Component({
    selector: 'app-nav-bar',
    templateUrl: './nav-bar.component.html',
    styleUrls: ['./nav-bar.component.scss']
})
export class NavBarComponent implements OnInit {

    @ViewChild(SideNavComponent)
    sideNavComponent: SideNavComponent;

    additionalHamburgerStyle: string;
    sideNavComponentOpen: boolean;

    constructor(public userService: UserService) {
    }

    ngOnInit() {
    }

    toggleSideNav() {

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
