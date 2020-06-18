import {EntityViewBlock} from '@app/component/entity-view/block/entity-view-block';

export class EntityViewTab {

    title: string;
    blocks: EntityViewBlock<any>[];

    constructor(title: string, blocks: EntityViewBlock<any>[]) {
        this.title = title;
        this.blocks = blocks;
    }
}
