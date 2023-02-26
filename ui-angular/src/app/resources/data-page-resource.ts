export class DataPageResource<T> {

    constructor(
        public totalSize: number,
        public resources: T[]
    ) {
    }
}
