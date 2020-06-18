import {Component, Type} from '@angular/core';

import {FormGroup} from '@app/node-modules/@angular/forms';
import {EntityViewBlock, EntityViewBlockData, EntityViewBlockType} from '@app/component/entity-view/block/entity-view-block';
import {EntityViewBlockComponent} from '@app/component/entity-view/block/entity-view-block.component';

export class EntityFieldsViewBlockData extends EntityViewBlockData {

    title: string;
    entityFormGroup: FormGroup;
    fields: EntityFieldDescriptor[];

    constructor(title: string, entityFormGroup: FormGroup, fields: EntityFieldDescriptor[]) {
        super();
        this.title = title;
        this.entityFormGroup = entityFormGroup;
        this.fields = fields;
    }
}

export class EntityFieldsViewBlock extends EntityViewBlock<EntityFieldsViewBlockData> {

    constructor(data: EntityFieldsViewBlockData) {
        super(EntityViewBlockType.entityFieldForm, data);
    }
}

export class EntityFieldDescriptor {
    title: string;
    formControlName: string;
}

@Component({
    selector: 'app-entity-view-entity-fields',
    templateUrl: 'entity-fields-view-block.component.html',
    styleUrls: ['../../../entity-view.component.scss'],
})
export class EntityFieldsViewBlockComponent extends EntityViewBlockComponent<EntityFieldsViewBlockData> {

    get dataType(): Type<EntityFieldsViewBlockData> {
        return EntityFieldsViewBlockData;
    }
}
