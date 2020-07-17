import {AfterViewInit, Directive} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {ErrorService} from '@app/service/error.service';
import {EntityViewTab} from '@app/component/entity-view/tab/entity-view-tab';
import {EntityControl, EntityViewBlockType} from '@app/component/entity-view/block/entity-view-block';
import {UniqEntity} from '@app/entity/uniq-entity';
import {BehaviorSubject, Observable} from '@app/node-modules/rxjs';
import {GlobalLoadingService} from '@app/service/global-loading.service';

@Directive()
export abstract class EntityViewComponent<T extends UniqEntity> implements AfterViewInit, EntityControl {

    readonly viewBlockType = EntityViewBlockType;
    readonly entityViewTabs: EntityViewTab[];

    readonly entityProperty: BehaviorSubject<T | null>;
    readonly disabledProperty: BehaviorSubject<boolean>;

    protected constructor(
        protected readonly translateService: TranslateService,
        protected readonly errorService: ErrorService,
        protected readonly globalLoadingService: GlobalLoadingService
    ) {
        this.entityProperty = new BehaviorSubject(null);
        this.disabledProperty = new BehaviorSubject(false);
        this.entityViewTabs = this.buildEntityViewTabs();
    }

    ngAfterViewInit(): void {
    }

    abstract reloadEntity(entity: T | null): void;

    protected abstract buildEntityViewTabs(): EntityViewTab[];

    observableEntity(): Observable<T | null> {
        return this.entityProperty;
    }

    get entity(): T | null {
        return this.entityProperty.getValue();
    }

    observableDisabled(): Observable<boolean> {
        return this.disabledProperty;
    }

    get disabled(): boolean {
        return this.disabledProperty.getValue();
    }

    createEntity(): void {
    }

    saveEntity(): void {
    }
}

