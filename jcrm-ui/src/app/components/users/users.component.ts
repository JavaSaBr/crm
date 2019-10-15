import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {FabButtonElement} from '@app/component/fab-button/fab-button.component';
import {Router} from '@angular/router';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {SelectionModel} from '@angular/cdk/collections';
import {MatSort} from '@angular/material/sort';
import {merge} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import {User} from '@app/entity/user';
import {UserRepository} from '@app/repository/user/user.repository';
import {EntityPage} from '@app/entity/entity-page';
import {DatePipe} from '@angular/common';
import {ErrorService} from '@app/service/error.service';
import {GlobalLoadingService} from '@app/service/global-loading.service';

@Component({
    selector: 'app-users',
    templateUrl: './users.component.html',
    styleUrls: ['./users.component.css'],
    host: {'class': 'flex-column'}
})
export class UsersComponent implements AfterViewInit {

    public static readonly COMPONENT_NAME = 'users';

    fabButtons: FabButtonElement[] = [
        {
            //routerLink: `../${ContactWorkspaceComponent.COMPONENT_NAME}/${ContactWorkspaceComponent.NEW_MODE}`,
            routerLink: `./user/new`,
            icon: 'perm_identity',
            tooltip: 'Add new user',
            callback: null
        }
    ];

    displayedColumns: string[] = [
        'select',
        'creation_date',
        'full_name',
        'email',
    ];

    dataSource: User[] = [];

    selection = new SelectionModel<User>(true, []);

    pageSize = 50;
    pageEvent: PageEvent;

    resultsLength = 0;

    @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
    @ViewChild(MatSort, {static: false}) sort: MatSort;

    constructor(
        private readonly userRepository: UserRepository,
        private readonly router: Router,
        private readonly datePipe: DatePipe,
        private readonly errorService: ErrorService,
        private readonly globalLoadingService: GlobalLoadingService
    ) {
    }

    ngAfterViewInit(): void {

        // If the user changes the sort order, reset back to the first page.
        this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);

        merge(this.sort.sortChange, this.paginator.page)
            .pipe(
                startWith({}),
                switchMap(() => this.startLoadingUsers()),
                map(data => this.finishLoadingContacts(data)),
                catchError(reason => {
                    this.globalLoadingService.decreaseLoading();
                    this.errorService.showError(reason);
                    return Promise.resolve([]);
                })
            )
            .subscribe(data => this.dataSource = data);
    }

    private startLoadingUsers(): Promise<EntityPage<User>> {
        this.globalLoadingService.increaseLoading();
        const pageSize = this.paginator.pageSize;
        const offset = this.paginator.pageIndex * pageSize;
        return this.userRepository.findEntityPage(pageSize, offset);
    }

    private finishLoadingContacts(entityPage: EntityPage<User>): User[] {

        this.globalLoadingService.decreaseLoading();
        this.resultsLength = entityPage.totalSize;

        return entityPage.entities;
    }

    openUser(contact: User): void {
        /*this.router.navigate([
            WorkspaceComponent.COMPONENT_NAME,
            ContactWorkspaceComponent.COMPONENT_NAME,
            ContactWorkspaceComponent.VIEW_MODE,
            contact.id
        ]);*/
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

    toggleSelection(row?: User): string {
        if (!row) {
            return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
        } else {
            return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
        }
    }

    buildCreated(contact: User): string {

        const now = new Date();
        const currentDate = now.getDate();

        const created = new Date(); //TODO contact.created;
        const createdDate = created.getDate();

        if (currentDate === createdDate) {
            return `Today ${this.datePipe.transform(created, 'HH:mm')}`;
        } else {
            return `${this.datePipe.transform(created, 'yyyy/mm/dd HH:mm')}`;
        }
    }
}
