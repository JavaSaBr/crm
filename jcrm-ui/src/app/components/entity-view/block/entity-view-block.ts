import {UniqEntity} from '@app/entity/uniq-entity';

export enum EntityViewBlockType {
    entityFieldForm
}

export class EntityViewBlockData {
    editing: boolean;
    disabled: boolean;
    hasChanges: boolean;
    entity: UniqEntity;
}

export class EntityViewBlock<T extends EntityViewBlockData> {

    type: EntityViewBlockType;
    data: T;

    protected constructor(type: EntityViewBlockType, data: T) {
        this.type = type;
        this.data = data;
    }
}
