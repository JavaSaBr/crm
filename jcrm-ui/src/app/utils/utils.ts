export class Utils {

    static isNumber(string: string | null): boolean {
        return (string && !isNaN(Number(string)));
    }

    static emptyIfNull(value: string) {
        return value == null ? '' : value;
    }
}
