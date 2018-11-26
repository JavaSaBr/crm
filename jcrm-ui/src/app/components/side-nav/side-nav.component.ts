import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormControl} from '@angular/forms';
import {MatDrawerToggleResult, MatSidenav} from '@angular/material';

export interface Tile {
    color: string;
    cols: number;
    rows: number;
    text: string;
}


@Component({
    selector: 'app-side-nav',
    templateUrl: './side-nav.component.html',
    styleUrls: ['./side-nav.component.scss']
})
export class SideNavComponent implements OnInit {

    mode = new FormControl('over');
    opened: boolean;

    @ViewChild(MatSidenav)
    matSidenav: MatSidenav;

    tiles: Tile[] = [
        {text: 'One', cols: 3, rows: 1, color: 'lightblue'},
        {text: 'Two', cols: 1, rows: 2, color: 'lightgreen'},
        {text: 'Three', cols: 1, rows: 1, color: 'lightpink'},
        {text: 'Four', cols: 2, rows: 1, color: '#DDBDF1'},
    ];

    constructor() {
    }

    ngOnInit() {
    }

    toggle(): Promise<MatDrawerToggleResult> {
        return this.matSidenav.toggle();
    }
}
