import {AbstractControl, AsyncValidator, ValidationErrors} from '@angular/forms';

export class BaseLazyAsyncValidator<T> implements AsyncValidator {

    protected lastValue: T | null;

    validate(control: AbstractControl): Promise<ValidationErrors | null> {

        const currentValue = control.value;

        this.lastValue = currentValue;

        if (!currentValue) {
            return Promise.resolve(null);
        }

        let errors = this.validateSync(currentValue);

        if (errors) {
            return Promise.resolve(errors);
        }

        return new Promise((resolve, reject) => {
            setTimeout(() => {

                if (currentValue === this.lastValue) {

                    this.validateAsync(currentValue)
                        .then(res => resolve(this.convertToResult(res, currentValue)))
                        .catch(reason => reject(reason))

                } else {
                    resolve(null);
                }

            }, this.getTimeout());
        });
    }

    validateSync(value): ValidationErrors {
        return null;
    }

    validateAsync(value): Promise<T> {
        throw Error("not implemented")
    }

    convertToResult(result: T, value: string): ValidationErrors | null {
        throw Error("not implemented")
    }

    getTimeout(): number {
        return 500
    }
}
