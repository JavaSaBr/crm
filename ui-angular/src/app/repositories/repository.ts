import {UniqEntity} from '@app/entity/uniq-entity';

export interface Repository<T extends UniqEntity> {

    findAll(): Promise<T[]>;

    findById(id: number) : Promise<T | null>;
}
