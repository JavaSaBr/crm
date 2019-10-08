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

    static toString(object: any): string {

        let result: string = '{\n';

        for (const propName in object) {

            if (!object.hasOwnProperty(propName)) {
                continue;
            }

            const fieldString = this.toStringField(1, propName, object[propName]);

            if (fieldString != null) {
                result += fieldString;
            }
        }

        result += '}';

        return result;
    }

    private static toStringField(level: number, name: string, object: any): string | null {

        if (!object) {
            return null;
        } else if (typeof object === 'string') {
            return `${'  '.repeat(level)}${name != null ? name + ':' : ''}"${object}",\n`;
        } else if (typeof object === 'number') {
            return `${'  '.repeat(level)}${name != null ? name + ':' : ''}${object},\n`;
        } else if (object instanceof Array) {
            return this.toStringArrayField(level, name, object);
        }

        let result: string = `${'  '.repeat(level)}{\n`;

        for (const propName in object) {

            if (!object.hasOwnProperty(propName)) {
                continue;
            }

            const fieldString = this.toStringField(level + 1, propName, object[propName]);

            if (fieldString != null) {
                result += fieldString;
            }
        }

        result += `${'  '.repeat(level)}}\n`;

        return result;
    }

    private static toStringArrayField(level: number, name: string, object: any[]) {

        if (object.length < 1) {
            return `${'  '.repeat(level) + name}:[],\n`;
        }

        let result: string = `${'  '.repeat(level) + name}:[\n`;

        object.forEach(value => {

            const fieldString = this.toStringField(level + 1, null, value);

            if (fieldString != null) {
                result += fieldString;
            }
        });

        result += `${'  '.repeat(level)}]\n`;

        return result;
    }

    public static distinct<T>(array: T[]): T[] {

        if (array.length < 2) {
            return array;
        }

        return array.filter((val, index, self) => self.indexOf(val) == index)
    }

    public static distinctFunc(): (val, index, self) => boolean {
        return (val, index, self) => self.indexOf(val) == index;
    }
}
