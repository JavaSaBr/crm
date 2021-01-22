import {AbstractControl} from '@angular/forms';
import {Subject} from 'rxjs';

export abstract class BaseAutoCompleter<T> {

    private static readonly emptyElements: any[] = [];

    protected readonly _filteredElements = new Subject<T[]>();

    private _lastRequestedTime: number;

    protected constructor(control: AbstractControl) {
        this.resetToDefault();
        control.valueChanges
            .subscribe(value => this.searchByValue(value));
    }

    protected searchByValue(value: T | string): void {
        if (typeof value === 'string' && value.length > 0) {
            if (this.searchTimeout() < 1) {
                this.searchByString(value);
            } else {
                this.lazySearchByString(value);
            }
        } else {
            this.resetToDefault();
        }
    }

    protected resetToDefault(): void {
        this._filteredElements.next(BaseAutoCompleter.emptyElements);
    }

    protected applyNewElements(elements: T[]): void {
        this._filteredElements.next(elements);
    }

    private lazySearchByString(value: string): void {

        const searchTimeout = this.searchTimeout();
        const newRequestedTime = Date.now();

        this.updateLastRequestedTime(newRequestedTime);

        setTimeout(() => {
            if (this.isLastRequestedTime(newRequestedTime)) {
                this.searchByString(value);
            }
        }, searchTimeout);
    }

    protected searchTimeout(): number {
        return 500;
    }

    private updateLastRequestedTime(newRequestedTime: number): void {
        this._lastRequestedTime = newRequestedTime;
    }

    private isLastRequestedTime(newRequestedTime: number): boolean {
        return newRequestedTime == this._lastRequestedTime;
    }

    protected abstract searchByString(value: string): void;
}

