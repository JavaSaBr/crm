export class Utils {

    static isNumber(string: string | null): boolean {
        return (string && !isNaN(Number(string)));
    }

    static emptyIfNull(value: string) {
        return value == null ? '' : value;
    }

    static ifNull<T>(value: T, supplier: () => T) {
        return value == null ? supplier() : value;
    }

    static copyArray<T>(array: T[] | null) {

        if (array == null) {
            return [];
        } else {
            return Array.from(array);
        }
    }
}
