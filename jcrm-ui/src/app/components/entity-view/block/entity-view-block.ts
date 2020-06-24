import {UniqEntity} from '@app/entity/uniq-entity';
import {Observable} from '@app/node-modules/rxjs';

export enum EntityViewBlockType {
    entityFieldForm
}

export interface EntityControl {

    observableEntity(): Observable<UniqEntity>

    observableDisabled(): Observable<boolean>

    createEntity(): void;

    saveEntity(): void
}

export class EntityViewBlockData {

    readonly entityControl: EntityControl;

    constructor(entityControl1: EntityControl) {
        this.entityControl = entityControl1;
    }
}

export class EntityViewBlock<T extends EntityViewBlockData> {

    readonly type: EntityViewBlockType;
    readonly data: T;

    protected constructor(type: EntityViewBlockType, data: T) {
        this.type = type;
        this.data = data;
    }
}

export interface EditableEntityViewBlock {
    setEditing(editing: boolean): void
}
