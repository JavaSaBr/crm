import {Component, Type, ViewChild} from '@angular/core';
import {FabButtonElement} from '@app/component/fab-button/fab-button.component';
import {BaseWorkspaceComponent, WorkspaceComponent} from '@app/component/workspace/workspace.component';
import {WorkspaceService} from '@app/service/workspace.service';
import {Contact} from '@app/entity/contact';
import {ContactRepository} from '@app/repository/contact/contact.repository';
import {Router} from '@angular/router';
import {ContactWorkspaceComponent} from '@app/component/contact/workspace-component/contact-workspace.component';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {SelectionModel} from '@angular/cdk/collections';
import {MatSort} from '@angular/material/sort';
import {merge} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import {User} from '@app/entity/user';
import {UserRepository} from '@app/repository/user/user.repository';
import {Utils} from '@app/util/utils';
import {EntityPage} from '@app/entity/entity-page';

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
            routerLink: `../${ContactWorkspaceComponent.COMPONENT_NAME}/${ContactWorkspaceComponent.NEW_MODE}`,
            icon: 'perm_identity',
            tooltip: 'Add new contact',
            callback: null
        }
    ];

    displayedColumns: string[] = [
        'select',
        'creation_date',
        'assigner',
        'full_name',
        'phone_numbers',
        'emails',
        'deals'
    ];

    dataSource: Contact[] = [];
    assigners: Map<number, User> = new Map<number, User>();

    selection = new SelectionModel<Contact>(true, []);

    pageSize = 50;
    pageEvent: PageEvent;

    resultsLength = 0;
    isLoadingResults = true;
    isRateLimitReached = false;

    @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
    @ViewChild(MatSort, {static: false}) sort: MatSort;

    constructor(
        protected readonly workspaceService: WorkspaceService,
        private readonly contactService: ContactRepository,
        private readonly userRepository: UserRepository,
        private readonly router: Router
    ) {
        super(workspaceService);
    }

    getComponentType(): Type<BaseWorkspaceComponent> {
        return ContactsComponent;
    }

    ngAfterViewInit(): void {
        super.ngAfterViewInit();

        // If the user changes the sort order, reset back to the first page.
        this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

        merge(this.sort.sortChange, this.paginator.page)
            .pipe(
                startWith({}),
                switchMap(() => this.startLoadingContacts()),
                switchMap(data => this.loadAssigners(data)),
                map(data => this.finishLoadingContacts(data)),
                catchError(() => {
                    this.isLoadingResults = false;
                    this.isRateLimitReached = true;
                    return Promise.resolve([]);
                })
            ).subscribe(data => this.dataSource = data);
    }

    private startLoadingContacts(): Promise<EntityPage<Contact>> {
        this.isLoadingResults = true;
        return this.contactService.findEntityPage(
            this.paginator.pageSize,
            this.paginator.pageIndex * this.paginator.pageSize
        );
    }

    private loadAssigners(entityPage: EntityPage<Contact>): Promise<EntityPage<Contact>> {

        const assignerIds: number[] = entityPage.entities
            .map(value => value.assigner)
            .filter(Utils.distinctFunc);

        if (assignerIds.length < 1) {
            return Promise.resolve(entityPage);
        }

        return this.userRepository.findByIds(assignerIds)
            .then(users => {
                this.assigners.clear();
                users.forEach(user => this.assigners.set(user.id, user));
                return entityPage;
            });
    }

    private finishLoadingContacts(entityPage: EntityPage<Contact>): Contact[] {

        this.isLoadingResults = false;
        this.isRateLimitReached = false;
        this.resultsLength = entityPage.totalSize;

        return entityPage.entities;
    }

    openContact(contact: Contact): void {
        this.router.navigate([
            WorkspaceComponent.COMPONENT_NAME,
            ContactWorkspaceComponent.COMPONENT_NAME,
            ContactWorkspaceComponent.VIEW_MODE,
            contact.id
        ]);
    }

    isAllSelected() {
        return this.selection.selected.length === this.dataSource.length;
    }

    toggleAllSelection() {
        if (!this.isAllSelected()) {
            this.dataSource.forEach(row => this.selection.select(row));
        } else {
            this.selection.clear();
        }
    }

    toggleSelection(row?: Contact): string {
        if (!row) {
            return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
        } else {
            return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
        }
    }

    buildAssignerName(contact: Contact): string {

        const names: string[] = [];
        const assigner = this.assigners.get(contact.assigner);

        if (assigner == null) {
            return 'No assigner';
        }

        if (assigner.firstName) {
            names.push(assigner.firstName);
        }

        if (assigner.secondName) {
            names.push(assigner.secondName);
        }

        const resultName = names.length > 0 && names.join(' ');

        if (resultName.length > 1) {
            return resultName;
        } else {
            return assigner.email;
        }
    }

    buildFullName(contact: Contact): string {

        const names: string[] = [];

        if (contact.firstName) {
            names.push(contact.firstName);
        }

        if (contact.secondName) {
            names.push(contact.secondName);
        }

        if (contact.thirdName) {
            names.push(contact.thirdName);
        }

        return names.join(' ');
    }

    buildPhoneNumbers(contact: Contact): string {

        const contactPhoneNumbers = contact.phoneNumbers;

        if (contactPhoneNumbers.length < 1) {
            return '';
        }

        const phoneNumber = contactPhoneNumbers[0].phoneNumber;
        const phoneNumberString = `${phoneNumber.country.phoneCode}(${phoneNumber.regionCode})${phoneNumber.phoneNumber}`;

        if (contactPhoneNumbers.length == 1) {
            return phoneNumberString;
        }

        return phoneNumberString + ', ...';
    }

    buildEmails(contact: Contact): string {

        const contactEmails = contact.emails;

        if (contactEmails.length < 1) {
            return '';
        } else if (contactEmails.length == 1) {
            return contactEmails[0].email;
        } else {
            return contactEmails[0].email + ', ...';
        }
    }
}
