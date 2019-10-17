import {AfterViewInit, Component} from '@angular/core';
import {FabButtonElement} from '@app/component/fab-button/fab-button.component';
import {Router} from '@angular/router';
import {merge} from 'rxjs';
import {catchError, map, startWith, switchMap} from 'rxjs/operators';
import {User} from '@app/entity/user';
import {UserRepository} from '@app/repository/user/user.repository';
import {EntityPage} from '@app/entity/entity-page';
import {DatePipe} from '@angular/common';
import {ErrorService} from '@app/service/error.service';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {AbstractEntityTableComponent} from '@app/component/table/abstract-entity-table.component';

@Component({
    selector: 'app-users',
    templateUrl: './users.component.html',
    styleUrls: ['./users.component.css'],
    host: {'class': 'flex-column'}
})
export class UsersComponent extends AbstractEntityTableComponent<User> implements AfterViewInit {

    public static readonly COMPONENT_NAME = 'users';

    private static readonly FAB_ACTIONS: FabButtonElement[] = [
        {
            //routerLink: `../${ContactWorkspaceComponent.COMPONENT_NAME}/${ContactWorkspaceComponent.NEW_MODE}`,
            routerLink: `./user/new`,
            icon: 'perm_identity',
            tooltip: 'Add new user',
            callback: null
        }
    ];

    private static readonly DISPLAYED_COLUMNS: string[] = [
        'select',
        'creation_date',
        'full_name',
        'email',
    ];

    constructor(
        private readonly userRepository: UserRepository,
        private readonly router: Router,
        datePipe: DatePipe,
        errorService: ErrorService,
        globalLoadingService: GlobalLoadingService
    ) {
        super(datePipe, errorService, globalLoadingService);
    }

    createDisplayedColumns(): string[] {
        return UsersComponent.DISPLAYED_COLUMNS;
    }

    createFabActions(): FabButtonElement[] {
        return UsersComponent.FAB_ACTIONS;
    }

    ngAfterViewInit(): void {
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

    buildCreated(user: User): string {

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
