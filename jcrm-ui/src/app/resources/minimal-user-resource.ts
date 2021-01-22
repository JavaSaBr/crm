
export class MinimalUserResource {

    constructor(
        public id: number | null,
        public email: string | null,
        public firstName: string | null,
        public secondName: string | null,
        public thirdName: string | null,
        public birthday: string | null,
    ) {}
}
