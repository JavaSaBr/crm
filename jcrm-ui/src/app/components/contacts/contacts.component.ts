import {Component, Type} from '@angular/core';
import {FabButtonElement} from '@app/component/fab-button/fab-button.component';
import {BaseWorkspaceComponent, WorkspaceComponent} from '@app/component/workspace/workspace.component';
import {WorkspaceService} from '@app/service/workspace.service';
import {Contact} from '@app/entity/contact';
import {ContactService} from '@app/service/contact.service';
import {Router} from '@angular/router';
import {ContactWorkspaceComponent} from '@app/component/contact/workspace-component/contact-workspace.component';

@Component({
    selector: 'app-contacts',
    templateUrl: './contacts.component.html',
    styleUrls: ['./contacts.component.css'],
    host: {'class': 'flex-column'}
})
export class ContactsComponent extends BaseWorkspaceComponent {

    public static readonly COMPONENT_NAME = 'contacts';

    fabButtons: FabButtonElement[] = [
        {
            routerLink: '../contact/new',
            icon: 'perm_identity',
            tooltip: 'Add new contact',
            callback: null
        }
    ];

    displayedColumns: string[] = ['first_name', 'second_name', 'third_name'];
    dataSource: Contact[] = [];

    length = 100;
    pageSize = 10;
    pageSizeOptions: number[] = [5, 10, 25, 100];

    // MatPaginator Output
    //pageEvent: PageEvent;

    constructor(
        protected readonly workspaceService: WorkspaceService,
        private readonly contactService: ContactService,
        private readonly router: Router
    ) {
        super(workspaceService);
    }

    setPageSizeOptions(options: string) {
        this.pageSizeOptions = options.split(',')
            .map(str => +str);
    }

    getComponentType(): Type<BaseWorkspaceComponent> {
        return ContactsComponent;
    }

    ngAfterViewInit(): void {
        super.ngAfterViewInit();
        this.contactService.getContacts()
            .then(value => this.dataSource = value)
            .catch(reason => this.dataSource = []);
    }

    openContact(contact: Contact): void {
        this.router.navigate([
            WorkspaceComponent.COMPONENT_NAME,
            ContactWorkspaceComponent.COMPONENT_NAME,
            ContactWorkspaceComponent.VIEW_MODE,
            contact.id
        ]);
    }
}
