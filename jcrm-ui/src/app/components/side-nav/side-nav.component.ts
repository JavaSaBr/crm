import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl} from '@angular/forms';
import {MatDrawerToggleResult, MatSidenav} from '@angular/material';

@Component({
    selector: 'app-side-nav',
    templateUrl: './side-nav.component.html',
    styleUrls: ['./side-nav.component.css']
})
export class SideNavComponent implements OnInit {

    mode = new FormControl('over');
    opened: boolean;

    @ViewChild(MatSidenav)
    matSidenav: MatSidenav;

    constructor() {
    }

    ngOnInit() {
    }

    toggle(): Promise<MatDrawerToggleResult> {
        return this.matSidenav.toggle();
    }
}
