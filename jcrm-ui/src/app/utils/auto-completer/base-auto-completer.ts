import {AbstractControl} from '@angular/forms';
import {Subject} from 'rxjs';

export abstract class BaseAutoCompleter<T> {

    private static readonly EMPTY_ELEMENTS: any[] = [];

    protected readonly _filteredElements = new Subject<T[]>();

    private lastRequested: Date;

    protected constructor(control: AbstractControl) {
        this._filteredElements.next(BaseAutoCompleter.EMPTY_ELEMENTS);
        control.valueChanges
            .subscribe(value => this.searchByValue(value));
    }

    protected searchByValue(value: T | string) {
        if (typeof value === 'string' && value.length > 0) {

            const newRequestedTime = new Date();
            this.lastRequested = newRequestedTime;

            setTimeout(() => {
                if (newRequestedTime == this.lastRequested) {
                    this.searchByString(value);
                }
            }, 500);

        } else {
            this._filteredElements.next(BaseAutoCompleter.EMPTY_ELEMENTS);
        }
    }

    protected abstract searchByString(value: string): void;
}

