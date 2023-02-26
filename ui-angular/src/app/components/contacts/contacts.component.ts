import {Component, ViewChild} from '@angular/core';
import {FabButtonElement} from '@app/component/fab-button/fab-button.component';
import {WorkspaceComponent} from '@app/component/workspace/workspace.component';
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
import {UserRepository} from '@app/repository/user/user.repository';
import {Utils} from '@app/util/utils';
import {EntityPage} from '@app/entity/entity-page';
import {DatePipe} from '@angular/common';
import {ErrorService} from '@app/service/error.service';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {MinimalUser} from '@app/entity/minimal-user';
import {BaseWorkspaceComponent} from '@app/component/workspace/base-workspace.component';

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
    assigners: Map<number, MinimalUser> = new Map<number, MinimalUser>();

    selection = new SelectionModel<Contact>(true, []);

    pageSize = 50;
    pageEvent: PageEvent;

    resultsLength = 0;

    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

    constructor(
        private readonly contactRepository: ContactRepository,
        private readonly userRepository: UserRepository,
        private readonly router: Router,
        private readonly datePipe: DatePipe,
        private readonly errorService: ErrorService,
        private readonly globalLoadingService: GlobalLoadingService,
        workspaceService: WorkspaceService,
    ) {
        super(workspaceService);
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
                catchError(reason => {
                    this.globalLoadingService.decreaseLoading();
                    this.errorService.showError(reason);
                    return Promise.resolve([]);
                })
            )
            .subscribe(data => this.dataSource = data);
    }

    isNeedGlobalMenu(): boolean {
        return true;
    }

    isNeedGlobalSearch(): boolean {
        return true;
    }

    private startLoadingContacts(): Promise<EntityPage<Contact>> {
        this.globalLoadingService.increaseLoading();
        const pageSize = this.paginator.pageSize;
        const offset = this.paginator.pageIndex * pageSize;
        return this.contactRepository.findEntityPage(pageSize, offset);
    }

    private loadAssigners(entityPage: EntityPage<Contact>): Promise<EntityPage<Contact>> {

        const assignerIds: number[] = Utils.distinct(entityPage.entities
            .map(value => value.assigner));

        if (assignerIds.length < 1) {
            return Promise.resolve(entityPage);
        }

        return this.userRepository.findMinimalByIds(assignerIds)
            .then(users => {
                this.assigners.clear();
                users.forEach(user => this.assigners.set(user.id, user));
                return entityPage;
            });
    }

    private finishLoadingContacts(entityPage: EntityPage<Contact>): Contact[] {

        this.globalLoadingService.decreaseLoading();
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

    buildCreated(contact: Contact): string {

        const now = new Date();
        const currentDate = now.getDate();

        const created = contact.created;
        const createdDate = created.getDate();

        if (currentDate === createdDate) {
            return `Today ${this.datePipe.transform(created, 'HH:mm')}`;
        } else {
            return `${this.datePipe.transform(created, 'yyyy/mm/dd HH:mm')}`;
        }
    }

    buildAssignerName(contact: Contact): string {

        const assigner = this.assigners.get(contact.assigner);

        if (assigner == null) {
            return 'No assigner';
        } else {
            return assigner.namePresentation;
        }
    }

    buildPhoneNumbers(contact: Contact): string {

        const phoneNumbers = contact.phoneNumbers;

        if (phoneNumbers.length < 1) {
            return '';
        }

        const phoneNumber = phoneNumbers[0];
        const phoneNumberString = `${phoneNumber.countryCode}(${phoneNumber.regionCode})${phoneNumber.phoneNumber}`;

        if (phoneNumbers.length == 1) {
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
