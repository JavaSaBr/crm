import {Component, Type} from '@angular/core';
import {MatDialog, PageEvent} from '@angular/material';
import {FabButtonElement} from '../fab-button/fab-button.component';
import {NewContactComponent} from '../contact/new/new-contact.component';
import {BaseWorkspaceComponent} from '../workspace/workspace.component';
import {WorkspaceService} from '../../services/workspace.service';

export interface PeriodicElement {
    name: string;
    position: number;
    weight: number;
    symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
    {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
    {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
    {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
    {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
    {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
    {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
    {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
    {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
    {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
    {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
    {position: 11, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
    {position: 12, name: 'Helium', weight: 4.0026, symbol: 'He'},
    {position: 13, name: 'Lithium', weight: 6.941, symbol: 'Li'},
    {position: 14, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
    {position: 15, name: 'Boron', weight: 10.811, symbol: 'B'},
    {position: 16, name: 'Carbon', weight: 12.0107, symbol: 'C'},
    {position: 17, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
    {position: 18, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
    {position: 19, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
    {position: 20, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
];

@Component({
    selector: 'app-contacts',
    templateUrl: './contacts.component.html',
    styleUrls: ['./contacts.component.css'],
    host: {'class': 'flex-column'}
})
export class ContactsComponent extends BaseWorkspaceComponent {

    fabButtons: FabButtonElement[] = [
        {
            routerLink: '../contact/new',
            icon: 'perm_identity',
            tooltip: 'Add new contact',
            callback: null
        }
    ];

    displayedColumns: string[] = ['position', 'name', 'weight', 'symbol'];
    dataSource = ELEMENT_DATA;

    length = 100;
    pageSize = 10;
    pageSizeOptions: number[] = [5, 10, 25, 100];

    // MatPaginator Output
    //pageEvent: PageEvent;

    constructor(
        protected workspaceService: WorkspaceService,
        private dialog: MatDialog
    ) {
        super(workspaceService);
    }

    setPageSizeOptions(setPageSizeOptionsInput: string) {
        this.pageSizeOptions = setPageSizeOptionsInput.split(',').map(str => +str);
    }

    getComponentType(): Type<BaseWorkspaceComponent> {
        return ContactsComponent;
    }
}
