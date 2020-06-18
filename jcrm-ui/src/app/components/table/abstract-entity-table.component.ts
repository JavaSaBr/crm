import {FabButtonElement} from '@app/component/fab-button/fab-button.component';
import {UniqEntity} from '@app/entity/uniq-entity';
import {ErrorService} from '@app/service/error.service';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {DatePipe} from '@app/node-modules/@angular/common';
import {merge} from '@app/node-modules/rxjs';
import {catchError, map, startWith, switchMap} from '@app/node-modules/rxjs/operators';
import {EntityPage} from '@app/entity/entity-page';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {SelectionModel} from '@angular/cdk/collections';
import {MatSort} from '@angular/material/sort';
import {AfterViewInit, ViewChild} from '@angular/core';

export abstract class AbstractEntityTableComponent<T extends UniqEntity> implements AfterViewInit {

    @ViewChild(MatSort, {static: true})
    sort: MatSort;

    @ViewChild(MatPaginator, {static: true})
    paginator: MatPaginator;

    fabActions: FabButtonElement[] = this.createFabActions();
    displayedColumns: string[] = this.createDisplayedColumns();

    dataSource: T[] = [];
    selection = new SelectionModel<T>(true, []);

    pageSize: number = 50;
    pageEvent: PageEvent = null;

    resultsLength: number = 0;

    protected constructor(
        protected readonly datePipe: DatePipe,
        protected readonly errorService: ErrorService,
        protected readonly globalLoadingService: GlobalLoadingService,
    ) {
    }

    createFabActions(): FabButtonElement[] {
        return [];
    }

    abstract createDisplayedColumns(): string[];

    ngAfterViewInit(): void {

        const sort = this.sort;
        const paginator = this.paginator;

        sort.sortChange.subscribe(() => paginator.pageIndex = 0);

        merge(sort.sortChange, paginator.page)
            .pipe(
                startWith({}),
                switchMap(() => this.startLoadingEntities()),
                map(data => this.finishLoadingEntities(data)),
                catchError(reason => {
                    this.globalLoadingService.decreaseLoading();
                    this.errorService.showError(reason);
                    return Promise.resolve([]);
                })
            )
            .subscribe(data => this.dataSource = data);
    }

    startLoadingEntities(): Promise<EntityPage<T>> {

        this.globalLoadingService.increaseLoading();

        const paginator = this.paginator
        const pageSize = paginator.pageSize;
        const offset = paginator.pageIndex * pageSize;

        return this.loadEntityPage(pageSize, offset);
    }

    abstract loadEntityPage(pageSize: number, offset: number): Promise<EntityPage<T>>

    finishLoadingEntities(entityPage: EntityPage<T>): T[] {
        this.globalLoadingService.decreaseLoading();
        this.resultsLength = entityPage.totalSize;
        return entityPage.entities;
    }

    openEntity(entity: T): void {}

    isAllSelected(): boolean {
        return this.selection.selected.length === this.dataSource.length;
    }

    toggleAllSelection(): void {
        if (!this.isAllSelected()) {
            this.dataSource.forEach(row => this.selection.select(row));
        } else {
            this.selection.clear();
        }
    }

    toggleSelection(row?: T): string {
        if (!row) {
            return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
        } else {
            return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
        }
    }
}
