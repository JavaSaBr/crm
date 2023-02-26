import {UniqEntity} from '@app/entity/uniq-entity';
import {Observable} from '@app/node-modules/rxjs';

export enum EntityViewBlockType {
    ENTITY_FIELD_FORM
}

export interface EntityControl {

    observableEntity(): Observable<UniqEntity>

    observableDisabled(): Observable<boolean>

    createEntity(): void;

    saveEntity(): void
}

export class EntityViewBlockData {

    readonly entityControl: EntityControl;

    constructor(entityControl: EntityControl) {
        this.entityControl = entityControl;
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
