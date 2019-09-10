import {AbstractControl} from '@angular/forms';
import {Subject} from 'rxjs';

export abstract class BaseAutoCompleter<T> {

    private static readonly EMPTY_ELEMENTS: any[] = [];

    protected readonly _filteredElements = new Subject<T[]>();

    private lastRequested: number;

    protected constructor(control: AbstractControl) {
        this._filteredElements.next(BaseAutoCompleter.EMPTY_ELEMENTS);
        control.valueChanges
            .subscribe(value => this.searchByValue(value));
    }

    protected searchByValue(value: T | string): void {
        if (typeof value === 'string' && value.length > 0) {
            if (this.getSearchTimeout() < 1) {
                this.searchByString(value);
            } else {
                this.lazySearchByString(value);
            }
        } else {
            this.resetToDefault();
        }
    }

    protected resetToDefault(): void {
        this._filteredElements.next(BaseAutoCompleter.EMPTY_ELEMENTS);
    }

    private lazySearchByString(value: string): void {

        const searchTimeout = this.getSearchTimeout();
        const newRequestedTime = Date.now();

        this.lastRequested = newRequestedTime;

        setTimeout(() => {
            if (newRequestedTime == this.lastRequested) {
                this.searchByString(value);
            }
        }, searchTimeout);
    }

    protected getSearchTimeout(): number {
        return 500;
    }

    protected abstract searchByString(value: string): void;
}

