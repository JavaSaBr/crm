import {Entity} from '../entity/entity';

export interface Repository<T extends Entity> {

    findAll(): Promise<T[]>;

    findById(id: number) : Promise<T | null>;
}
