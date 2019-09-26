import {ValidationErrors} from '@angular/forms';

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

    static pushTo(errors: ValidationErrors | null | undefined, container: ValidationErrors) {

        if (!errors) {
            return;
        }

        Object.keys(errors).forEach(key => {
            container[key] = errors[key];
        });
    }
}
