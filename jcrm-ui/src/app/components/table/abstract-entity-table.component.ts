import {AfterViewInit, ViewChild} from '@app/node-modules/@angular/core';
import {FabButtonElement} from '@app/component/fab-button/fab-button.component';
import {SelectionModel} from '@app/node-modules/@angular/cdk/collections';
import {MatPaginator, MatSort, PageEvent} from '@app/node-modules/@angular/material';
import {UniqEntity} from '@app/entity/uniq-entity';
import {ErrorService} from '@app/service/error.service';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {DatePipe} from '@app/node-modules/@angular/common';

export abstract class AbstractEntityTableComponent<T extends UniqEntity> implements AfterViewInit {

    fabActions: FabButtonElement[] = this.createFabActions();
    displayedColumns: string[] = this.createDisplayedColumns();

    @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;
    @ViewChild(MatSort, {static: false}) sort: MatSort;

    dataSource: T[] = [];
    selection = new SelectionModel<T>(true, []);

    pageSize: number = 50;
    pageEvent: PageEvent = null;
    resultsLength: number = 0;

    protected constructor(
        protected readonly datePipe: DatePipe,
        protected readonly errorService: ErrorService,
        protected readonly globalLoadingService: GlobalLoadingService
    ) {
    }

    createFabActions(): FabButtonElement[] {
        return [];
    }

    abstract createDisplayedColumns(): string[];

    ngAfterViewInit(): void {
        this.sort.sortChange.subscribe(() => this.paginator.pageIndex = 0);
    }

    openEntity(entity: T): void {
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

    toggleSelection(row?: T): string {
        if (!row) {
            return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
        } else {
            return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
        }
    }
}
