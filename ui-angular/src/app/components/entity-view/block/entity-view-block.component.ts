import {Directive, Input, Type} from '@angular/core';
import {EntityViewBlockData} from '@app/component/entity-view/block/entity-view-block';

@Directive()
export abstract class EntityViewBlockComponent<T extends EntityViewBlockData> {

    data: T;

    @Input('data')
    set initData(data: EntityViewBlockData) {
        if (!(data instanceof this.dataType)) {
            throw new Error(`${data} is not ${this.dataType}`);
        } else {
            this.data = data;
        }
    }

    abstract get dataType(): Type<T>;
}
