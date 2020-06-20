import {AfterViewInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {ErrorService} from '@app/service/error.service';
import {EntityViewTab} from '@app/component/entity-view/tab/entity-view-tab';
import {EntityProvider, EntityViewBlockType} from '@app/component/entity-view/block/entity-view-block';
import {UniqEntity} from '@app/entity/uniq-entity';
import {BehaviorSubject, Observable} from '@app/node-modules/rxjs';

export abstract class EntityViewComponent<T extends UniqEntity> implements AfterViewInit, EntityProvider {

    readonly viewBlockType = EntityViewBlockType;
    readonly entityViewTabs: EntityViewTab[];

    readonly entityProperty: BehaviorSubject<T | null>;

    disabled: boolean;

    protected constructor(
        protected readonly translateService: TranslateService,
        protected readonly errorService: ErrorService
    ) {
        this.entityProperty = new BehaviorSubject(null);
        this.disabled = false;
        this.entityViewTabs = this.buildEntityViewTabs();
    }

    ngAfterViewInit(): void {
    }

    abstract reloadEntity(entity: T | null): void;

    createEntity(): void {
    }

    updateEntity(): void {
    }

    protected abstract buildEntityViewTabs(): EntityViewTab[];

    observableEntity(): Observable<T | null> {
        return this.entityProperty;
    }

    get entity(): T | null {
        return this.entityProperty.getValue();
    }
}

