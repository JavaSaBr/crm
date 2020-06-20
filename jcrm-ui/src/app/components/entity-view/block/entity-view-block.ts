import {UniqEntity} from '@app/entity/uniq-entity';
import {Observable} from '@app/node-modules/rxjs';

export enum EntityViewBlockType {
    entityFieldForm
}

export interface EntityProvider {
    observableEntity(): Observable<UniqEntity>
}

export class EntityViewBlockData {

    readonly entityProvider: EntityProvider;

    constructor(entityProvider: EntityProvider) {
        this.entityProvider = entityProvider;
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
