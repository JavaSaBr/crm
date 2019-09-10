import {Country} from '@app/entity/country';
import {Entity} from '@app/entity/entity';

export class PhoneNumber extends Entity {
    constructor(
        public country: Country | null,
        public phoneRegion: string | null,
        public phoneNumber: string | null
    ) {
        super();
    }
}
